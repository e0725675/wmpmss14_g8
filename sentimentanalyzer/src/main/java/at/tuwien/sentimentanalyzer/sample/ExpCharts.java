package at.tuwien.sentimentanalyzer.sample;

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.beans.ReportGenerator;

public class ExpCharts {
	private static Logger log = Logger.getLogger(ExpCharts.class);
	public static void main(String[] args) {
		SortedMap<String, Integer> namesAndValues = new TreeMap<String,Integer>();
		namesAndValues.put("1", 5);
		namesAndValues.put("test2x", 10);
		namesAndValues.put("test3xx", 20);
		namesAndValues.put("test4xxxxxxx", 30);
		namesAndValues.put("test5xxxxxxxxxxx", 40);
		String outFile;
		outFile = ReportGenerator.createPieChartImage("target/testPieChart.png", namesAndValues , "Title", "SubTitle", 800, 600);
		log.info("Generated file: "+outFile);
		outFile = ReportGenerator.createBarChartImage("target/testBarChart.png", namesAndValues , "Title", "SubTitle", "CategoryAxis", "ValueAxis", 800, 600);
		log.info("Generated file: "+outFile);
	}

}
