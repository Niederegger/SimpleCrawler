package de.vv.crawler.CrawlerAnleihen;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class DBCon {
	final private static Logger l = LoggerFactory.getLogger(DBCon.class);

	static private Connection con;
	static private PreparedStatement psInsert;
	static private CallableStatement cstmtCallUpdate;
//	static private CallableStatement cstmtFetchJob;

	/**
	 * opening Connection to Ms SQL Database
	 */
	public static void openConnection() {
		SQLServerDataSource ds = new SQLServerDataSource();
		try {
			ds.setIntegratedSecurity(false);
			ds.setUser(App.c.db_conf.user);
			ds.setPassword(App.c.db_conf.pw);
			ds.setServerName(App.c.db_conf.serverName);
			ds.setPortNumber(App.c.db_conf.port);
			ds.setDatabaseName(App.c.db_conf.dbName);
			con = ds.getConnection();
			String query = "INSERT INTO vv_mastervalues_upload"
					+ "(MVU_DATA_ORIGIN, MVU_URLSOURCE, MVU_SOURCEFILE, MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_COMMENT) VALUES"
					+ "(?,?,?,?,?,?,?,?,?);";
			psInsert = DBCon.con.prepareStatement(query);
			cstmtCallUpdate = DBCon.con.prepareCall("{call vvsp_import_uploadV3 (?, ?, ?, ?, ?)};");
		} catch (Exception e) {
			BF.log_err(l, "Exception: {}", e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	/**
	 * closing connection
	 */
	public static void closeConnection() {
		try {
			con.close();
			psInsert.close();
		} catch (SQLException e) {
			BF.log_err(l, "SQLException: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * fetching next Job
	 * 
	 * @return wanted ISIN
	 */
	public static String fetchJob() {
		try {
			CallableStatement cstmtFetchJob = DBCon.con.prepareCall("{call vvsp_get_crawler_job (?)};");
			cstmtFetchJob.setString(1, App.c.crawl_conf.crawlId);
			ResultSet rs = cstmtFetchJob.executeQuery();
			int count = 0;																		// Diese Count-geschichte kommt daher:
			while (rs == null) {															// eine StoredProcedure kann mehrere ResultSet returnen
				count++;																				// MainInfo (v1) lieferte einige Sets zureuck, von denen
				cstmtFetchJob.getMoreResults();									// die ersten paar null waren, -> dadher die Iteration durch
				rs = cstmtFetchJob.getResultSet();							// die sets, // mainInfoV2 liefert aktuell direkt das
				if (count++ > 100)															// gewuenschte Set und macht dadurch dieses Handling 
					return null;																	// unnoetig
			}
			System.out.println("count: " + count);
			while(rs.next()){
				return rs.getString("cj_isin");
			}
		} catch (SQLException e) {
			BF.log_err(l, "SQLException: {}", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Inserting and calling Update on fetched Result
	 * 
	 * @param dc
	 *          DataContainer
	 */
	public static void writeData(DataContainer dc) {
		try {
			
//			String query = "INSERT INTO vv_mastervalues_upload"
//					+ "(MVU_DATA_ORIGIN, MVU_URLSOURCE, MVU_SOURCEFILE, MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_COMMENT) VALUES"
//					+ "(?,?,?,?,?,?,?,?,?);";
			
			// inserting data to Upload Table
			psInsert.setString(1, dc.title);												// Data Origin
			psInsert.setString(2, dc.url); 													// url-Source <- soll gemerkt werden
			psInsert.setString(3, "none"); 													// SourceFile <- keine
			psInsert.setString(4, App.c.crawl_conf.crawlId);				// CRFTA
			psInsert.setString(5, dc.isin);													// isin 			<- soll gemerkt werden
			psInsert.setString(6, "");															// mic				<- keine
			psInsert.setString(9, App.c.crawl_conf.comment);				// comment		<- Crawler
			for (int i = 0; i < dc.fieldNames.length; i++) {				// <- looping through crawlResult
				psInsert.setString(7, dc.fieldNames[i]);							// fieldname 	<- kommt durch loop
				psInsert.setString(8, dc.stringValues[i]);						// value			<- ebenfalls durch loop
				psInsert.executeUpdate();
			}

			// calling update Function
			cstmtCallUpdate.setString(1, App.c.crawl_conf.crawlId); 	// SourceId
			cstmtCallUpdate.setString(2, dc.title); 									// Data Origin
			cstmtCallUpdate.setString(3, dc.url);  										// UrlSource
			cstmtCallUpdate.setString(4, "none");  										// SourceFile
			cstmtCallUpdate.setString(5, App.c.crawl_conf.comment); 	// Comment
			cstmtCallUpdate.execute();
		} catch (SQLException e) {
			BF.log_err(l, "SQLException: {}", e.getMessage());
			e.printStackTrace();
		}
	}

}
