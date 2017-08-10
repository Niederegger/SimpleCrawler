package de.vv.crawler.CrawlerAnleihen;

public class App {

	public static Config c = new Config(); // public Config Container <- beinhaltet weitere Configs (deshalb so kurzer Name)

	/**
	 * this Programm is used to crawl websiteds and fetching information
	 * 
	 * @param args
	 *          first Agrument has to contain Path of Configuration Files (xxx.conf{zz})
	 *          it need to have a DB_Config (xxx.confdb) and a crawler conf (xxx.confcr)
	 * 
	 *          further details to config are incoming
	 */
	public static void main(String[] args) {
		
		if(!validArgs(args)) return; 	// check args amount
	
		
		
		c.init(args[0], args[1]); 		// init Configs
		
		c.printAllTheConfigs();
		
		DBCon.openConnection();				// open DB Connection
		
		Sched.run();									// start the Crawler
		
		DBCon.closeConnection();			// close DB Connection
		
	}
	
	static boolean validArgs(String[] args){
		if(args.length != 2){
			System.out.println("Error: not right amount or args: " + args.length);
			return false;
		} else return true;
	}
	

}
