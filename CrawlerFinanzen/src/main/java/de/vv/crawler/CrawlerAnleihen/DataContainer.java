package de.vv.crawler.CrawlerAnleihen;

public class DataContainer {

	public DataContainer(int amt){
		fieldNames = new String[amt];
		stringValues = new String[amt];
	}
	
	public String[] fieldNames;
	public String[] stringValues;
	public String url;
	public String isin;
	public String title;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("isin: ");
		sb.append(isin);
		sb.append("\nurl: ");
		sb.append(url);
		sb.append("\ntitle: ");
		sb.append(title);
		sb.append("\nResult:\n");
		for(int i = 0; i < fieldNames.length; i++){
			sb.append(fieldNames[i]);
			sb.append(": ");
			sb.append(stringValues[i]);
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
