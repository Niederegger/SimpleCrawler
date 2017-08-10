package de.vv.crawler.CrawlerAnleihen.config;

public class Data_Config {

	public String[] titles ={
			"Kupon in %",
			"Fälligkeit",
			"Nachrang",
			"Erstes Kupondatum",
			"Letztes Kupondatum",
			"Zinstermin Periode",
			"Zinstermine pro Jahr",
			"Zinslauf ab",
			"Emittent",
			"Emissionsvolumen\\*",
			"Emissionswährung",
			"Emissionsdatum",
			"Name",
			"WKN",
			"Anleihe-Typ",
			"Emittentengruppe",
			"Land",
			"Stückelung",
			"Stückelung Art",
		};
	
	
	public String form[] = { 
			"<tr>(\\s)*<td>", 
			"<\\/td>(\\s)*<td>",
			"<\\/td>(\\s)*<\\/tr>" 
		};
	
	public String url = "http://www.finanzen.net/anleihen/";
	
	public String robotsUrl = "http://www.finanzen.net/robots.txt";
	public String usedRoutes = "/anleihen/";

}
