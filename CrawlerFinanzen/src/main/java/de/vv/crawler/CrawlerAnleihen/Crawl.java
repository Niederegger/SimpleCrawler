package de.vv.crawler.CrawlerAnleihen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crawl {
	final private static Logger l = LoggerFactory.getLogger(Crawl.class);

	static Document doc;

	/**
	 * opening connection to html
	 * 
	 * @param site
	 * @return
	 */
	public static boolean getDoc(String site) {
		try {
			doc = Jsoup.connect(site).userAgent(App.c.crawl_conf.userAgent).get();
			return true;
		} catch (Exception e) {
			BF.log_err(l, "Exception: {}", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * fetching data to write to db
	 * 
	 * @param dat
	 * @param form
	 * @param reg2
	 * @return
	 */
	public static DataContainer fetchData(String[] dat, String[] form, String reg2) {
		DataContainer dc = new DataContainer(dat.length);
		String d;
		Pattern pt = Pattern.compile("<title>(.)*</title>");
		Matcher matchert = pt.matcher(doc.toString());
		if (matchert.find()) {
			String ma = matchert.group(0); 					// getting complete Table Row

			String res = ma.split(">")[1].split("<")[0];
			dc.title = res;
		}
		for (int i = 0; i < dat.length; i++) {

			d = dat[i];
			Pattern p = Pattern.compile(form[0] + d + form[1] + reg2 + form[2]);
			Matcher matcher = p.matcher(doc.toString());
			if (matcher.find()) {
				String ma = matcher.group(0); 					// getting complete Table Row

				String res = ma.split("<td>")[2].split("</td>")[0];
				dc.fieldNames[i] = BF.replacer(d);
				if (res.contains("href=\"")) {
					res = res.split(">")[1].split("<")[0];
				}
				dc.stringValues[i] = BF.replacer(res);
			}
		}
		return dc;
	}

	/**
	 * reading robots.txt file and deciding whether it's allowed to crawl or na
	 * 
	 * @param url
	 *          of robots.txt data
	 * @return whether its allowed to crawl or not
	 */
	public static boolean checkRobotsTxt(String url) {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
			String line = null;
			String cUA; // current user Agent
			boolean imp = false;
			while ((line = in.readLine()) != null) {
				if (line.contains("User-agent:")) {														// traegt dne zuletzt gesehenen useragent mit w/e
					cUA = line;
					if (cUA.contains("*") || cUA.contains("WpWiki-Bot")) {			// falls der useragent allg. oder auf uns spezifisch ist
						imp = true;																								// wird es relevant zu horchen
					} else
						imp = false;
				} else {
					if (imp) {																									// horche nur wenn es relevant ist

						if (line.contains("Disallow:") && !line.contains("/")){ 		// dies ist der Fall in dem alles erlaubt ist zu crawlen
							return true;
						}

						if (line.contains(App.c.data_conf.usedRoutes) 
								|| line.trim().equals("Disallow: /")) {
							BF.log_inf(l, "Unsere Route wurde gesperrt {}", line);	// eintrag ins logbuch
							return false;																						// sie haben uns die route gesperrt,
						}																													// wir beenden das crawlen
					}
				}
			}
		} catch (IOException e) {
			BF.log_err(l, "IOException: {}", e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

}
