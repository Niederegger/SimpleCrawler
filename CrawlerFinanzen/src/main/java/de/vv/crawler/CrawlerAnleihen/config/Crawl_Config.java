package de.vv.crawler.CrawlerAnleihen.config;

public class Crawl_Config {
	public String crawlId = "CRFTA";
	public String getJobCommand = "{call vvsp_get_crawler_job(?)}";
	public int timeSleep = 400;
	public int noJobSleep = 400;
	public String comment = "Crawler: ";
	public String userAgent = "Mozilla/5.0 (compatible; WpWiki-Bot; +http://wpwiki.de/ueber)";

}
