package de.vv.crawler.CrawlerAnleihen;


import de.vv.crawler.CrawlerAnleihen.config.*;

/**
 * this class is used to bundle all configs together
 */
public class Config {

	public DB_Config db_conf;
	public Crawl_Config crawl_conf;
	public Data_Config data_conf;

	public boolean init(String name, String path) {
		return BF.initConfig(path, name);
	}

	public void printAllTheConfigs() {
		BF.printObjectAsJson(db_conf);
		BF.printObjectAsJson(crawl_conf);
		BF.printObjectAsJson(data_conf);
	}
}
