package at.tuwien.sentimentanalyzer.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.beans.ReportGenerator;
import at.tuwien.sentimentanalyzer.beans.ReportGenerator.Tuple;

public class ExpCharts {
	private static Logger log = Logger.getLogger(ExpCharts.class);
	public static void main(String[] args) {
		List<Tuple<String, Number>> namesAndValues = new ArrayList<Tuple<String,Number>>();
		namesAndValues.add(new Tuple<String, Number>("1", 5));
		namesAndValues.add(new Tuple<String, Number>("test2x", 10));
		namesAndValues.add(new Tuple<String, Number>("test3xx", 20));
		namesAndValues.add(new Tuple<String, Number>("test4xxxxxxx", 30));
		namesAndValues.add(new Tuple<String, Number>("test5xxxxxxxxxxx", 40));
		Collections.sort(namesAndValues);
		Collections.reverse(namesAndValues);
		String outFile;
		outFile = ReportGenerator.createPieChartImage("target/testPieChart.png", namesAndValues , "Title", "SubTitle", 800, 600);
		log.info("Generated file: "+outFile);
		outFile = ReportGenerator.createBarChartImage("target/testBarChart.png", namesAndValues , "Title", "SubTitle", "CategoryAxis", "ValueAxis", 800, 600);
		log.info("Generated file: "+outFile);
	}

}
