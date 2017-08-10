package de.vv.crawler.CrawlerAnleihen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class is used to schedule the cralwer
 *
 */
public class Sched {
	final private static Logger l = LoggerFactory.getLogger(Sched.class);

	public static void run() {
		boolean running = true;

		while (running) {
			if (!Crawl.checkRobotsTxt(App.c.data_conf.robotsUrl)) {
				BF.log_inf(l, "Robots.txt excluded this cralwer: {}", App.c.crawl_conf.crawlId);
				break;
			}

			String isin = DBCon.fetchJob();
			BF.log_inf(l, "Fetched ISIN: {}", isin);
						
			if (isin != null) {
				String url = BF.getURl(isin);
				Crawl.getDoc(url);
							


				DataContainer dc = Crawl.fetchData(App.c.data_conf.titles, App.c.data_conf.form, "(.)*");
				dc.isin = isin;
				
				dc.url = url;

				System.out.println(dc.toString());
				
				DBCon.writeData(dc);

				// no sleeps for some time
				try {
					Thread.sleep(App.c.crawl_conf.timeSleep);
				} catch (InterruptedException e) {
					BF.log_err(l, "InterruptedException: {}", e.getMessage());
				}
				// if no job was found you this crawler sleeps for adjustet time
				// though was, is a job was found, crawler can recrawl after 10 secs, but if no job was found, 
				// crawler supposed to wait 300 seconds for example
			} else {
				BF.log_inf(l, "No Job found", "");
				
				try {
					Thread.sleep(App.c.crawl_conf.noJobSleep);
				} catch (InterruptedException e) {
					BF.log_err(l, "InterruptedException: {}", e.getMessage());
				}
			}
		}

		BF.log_inf(l, "Crawler endet: {}", App.c.crawl_conf.crawlId);

	}
}
