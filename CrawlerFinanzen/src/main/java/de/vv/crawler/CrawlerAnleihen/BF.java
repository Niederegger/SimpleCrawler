package de.vv.crawler.CrawlerAnleihen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import de.vv.crawler.CrawlerAnleihen.config.*;

/**
 * BF -> Basic Functions
 * or BestFriend ;)
 * 
 * simply carries all Functions you might need somewhere.
 */
public class BF {
	final private static Logger l = LoggerFactory.getLogger(BF.class);

	/**
	 * loads Json Files
	 * 
	 * @param fullPath
	 *          absolutepath
	 * @param obj
	 *          !important! this object is supposed to be your wanted object class
	 * @return parsed json file to object class
	 */
	public static Object loadJson(String fullPath, Object obj) {
		Gson gson = new Gson();
		try {
			return gson.fromJson(new FileReader(fullPath), obj.getClass());
		} catch (JsonSyntaxException e) {
			log_err(l, "JsonSyntaxException: {}", e.getMessage());
			System.err.println("JsonSyntaxException: " + e.getMessage());
		} catch (JsonIOException e) {
			log_err(l, "JsonIOException: {}", e.getMessage());
			System.err.println("JsonIOException: " + e.getMessage());
		} catch (FileNotFoundException e) {
			log_err(l, "FileNotFoundException: {}", e.getMessage());
			System.err.println("FileNotFoundException: " + e.getMessage());
		}
		return null;
	}

	/**
	 * logs error with sl4fj
	 * 
	 * @param l
	 *          logger
	 * @param e
	 *          message
	 * @param d
	 *          insert messeage
	 */
	public static void log_err(Logger l, String e, String d) {
		l.error(App.c.crawl_conf.crawlId + " " + e, d);
	}

	/**
	 * logs trace with sl4fj
	 * 
	 * @param l
	 *          logger
	 * @param e
	 *          message
	 * @param d
	 *          insert messeage
	 */
	public static void log_trace(Logger l, String t, String d) {
		l.trace(App.c.crawl_conf.crawlId + " " + t, d);
	}

	/**
	 * logs info with sl4fj
	 * 
	 * @param l
	 *          logger
	 * @param e
	 *          message
	 * @param d
	 *          insert messeage
	 */
	public static void log_inf(Logger l, String i, String d) {
		l.info(App.c.crawl_conf.crawlId + " " + i, d);
	}

	/**
	 * conctas url for this dataset
	 * 
	 * @param isin
	 * @return
	 */
	public static String getURl(String isin) {
		return App.c.data_conf.url + isin;
	}

	/**
	 * removes * and \ characters
	 * 
	 * @param s
	 *          String
	 * @return
	 */
	public static String replacer(String s) {
		if (s.contains("*")) {
			s = s.replaceAll("\\*", "");
		}
		if (s.contains("\\")) {
			s = s.replaceAll("\\\\", "");
		}
		return s;
	}

	/**
	 * This function is used to print the Config, so one has the right format
	 */
	public static void printObjectAsJson(Object o) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonInString = gson.toJson(o);
		System.out.println(jsonInString);
	}

	final static int _DataConf = 0;
	final static int _CrawlConf = 1;
	final static int _DBConf = 2;

	/**
	 * initializes config
	 * 
	 * @param path
	 *          path of config directory
	 * @param name
	 *          name of configs for this bot
	 * @return
	 */
	public static boolean initConfig(String path, String name) {
		File[] configPath = { new File(path + name + ".confda"),	//_DataConf
				new File(path + name + ".confcr"),	//_CrawlConf
				new File(path + name + ".confdb"),	// _DBConf
		};

		// initialisiere die Configs im Programm
		Gson gson = new Gson();
		try {
			App.c.crawl_conf = gson.fromJson(new InputStreamReader(new FileInputStream(configPath[_CrawlConf]), "UTF-8"),
					Crawl_Config.class);
			App.c.data_conf = gson.fromJson(new InputStreamReader(new FileInputStream(configPath[_DataConf]), "UTF-8"),
					Data_Config.class);
			App.c.db_conf = gson.fromJson(new InputStreamReader(new FileInputStream(configPath[_DBConf]), "UTF-8"),
					DB_Config.class);
			return true;
		} catch (Exception e) {
			l.error("Exception: {}", e.getMessage());
		}
		return false;	// Config konnte nicht erstellt werden
	}

	/**
	 * returns a list of all files and folder in this path
	 * 
	 * @param path
	 * @return
	 */
	public static File[] getAllFileFromDir(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
	}
}
