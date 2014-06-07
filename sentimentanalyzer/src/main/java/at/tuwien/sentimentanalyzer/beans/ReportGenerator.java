package at.tuwien.sentimentanalyzer.beans;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;

import at.tuwien.sentimentanalyzer.entities.AggregatedMessages;

public class ReportGenerator {
	private static Logger log = Logger.getLogger(ReportGenerator.class);
	public ReportGenerator() {

	}
	private static  final String REGEX_sentimentpercentages_total = "<!--###sentimentpercentages_total###-->";
	private static  final String REGEX_wordcounts_total = "<!--###wordcounts_total###-->";
	private static  final String REGEX_messages_total = "<!--###messages_total###-->";

	private static  final String REGEX_sentimentpercentages_persource = "<!--###sentimentpercentages_persource###-->";
	private static  final String REGEX_wordcounts_persource = "<!--###wordcounts_persource###-->";

	private static  final String REGEX_get_template_sentimentpercentages_persource = "(?:<!--####sentimentpercentages_persource#)(.*?)(?:####-->)";
	private static  final String REGEX_get_template_wordcounts_persource = "(?:<!--####sentimentpercentages_persource#)(.*?)(?:####-->)";

	private static final String REGEX_alt = "##alt##";
	private static  final String REGEX_src = "##src##";

	private static  final String REGEX_comments = "(<!--.*?-->)";
	private static class ImageSources {
		public String imgSentimentpercentagesTotal = "imgSentimentpercentagesTotal";
		public String imgWordcountsTotal = "imgWordcountsTotal";
		public String imgMessagesTotal = "imgMessagesTotal";
		public List<String> imgSentimentsPerSource = new ArrayList<String>();
		public List<String> imgWordCountsPerSource = new ArrayList<String>();
	}
	/**
	 * Generates an HTML report for AggregatedMessages
	 * @param agm
	 * @return
	 * @throws IOException 
	 */
	public HtmlReport generateAggregatedMessagesHtmlReport(AggregatedMessages agm) throws IOException {
		String baseFolder = "";
		File file = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		do {
			Date date = new Date();
			baseFolder = "htmlMessageReports/"+df.format(date)+"_"+ReportGenerator.getRandomAlphaNumericString(4);
			file = new File(baseFolder);
		} while(file.exists());
		
		ImageSources imageSources = ReportGenerator.generateChartImages(baseFolder, agm);

		File template = new File("agm_report_template.html");
		String html = ReportGenerator.fileToString(template);

		String imgSentimentpercentagesTotal = imageSources.imgSentimentpercentagesTotal;
		String imgWordcountsTotal = imageSources.imgWordcountsTotal;
		String imgMessagesTotal = imageSources.imgMessagesTotal;

		List<String> imgSentimentsPerSource = imageSources.imgSentimentsPerSource;imgSentimentsPerSource.add("test1");
		List<String> imgWordCountsPerSource = imageSources.imgWordCountsPerSource;imgWordCountsPerSource.add("test2");

		// replace total markers with imagepaths
		html = ReportGenerator.replaceTextinString(html, REGEX_sentimentpercentages_total, imgSentimentpercentagesTotal);
		html = ReportGenerator.replaceTextinString(html, REGEX_wordcounts_total, imgWordcountsTotal);
		html = ReportGenerator.replaceTextinString(html, REGEX_messages_total, imgMessagesTotal);

		List<List<String>> temp;

		// get template for sentiment per source images
		temp = ReportGenerator.getMatchingGroups(html, REGEX_get_template_sentimentpercentages_persource);
		if (temp.size() != 1 || temp.get(0).size() != 1) {
			if (temp.size() == 0) {
				throw new ReportGeneratorException("Could not find template for sentiment per source images");
			} else {
				throw new ReportGeneratorException("Invalid number of templates for sentiment per source images");
			}
		}
		String sentimentpercentages_persource_template = temp.get(0).get(0);

		// get template for wordcounts per source images
		temp = ReportGenerator.getMatchingGroups(html, REGEX_get_template_wordcounts_persource);
		if (temp.size() != 1 || temp.get(0).size() != 1) {
			if (temp.size() == 0) {
				throw new ReportGeneratorException("Could not find template for wordcounts per source images");
			} else {
				throw new ReportGeneratorException("Invalid number of templates for wordcounts per source images");
			}
		}
		String wordcounts_persource_template = temp.get(0).get(0);

		// insert sentiments per source images
		String sentimentpercentagess_persource_out = "";
		for (String src : imgSentimentsPerSource) {
			String imgTag = ReportGenerator.fillAltAndSrc(sentimentpercentages_persource_template, src, src);
			sentimentpercentagess_persource_out+=imgTag;
		}
		html = ReportGenerator.replaceTextinString(html, REGEX_sentimentpercentages_persource, sentimentpercentagess_persource_out);

		// insert wordcounts per source images
		String wordcounts_persource_out = "";
		for (String src : imgSentimentsPerSource) {
			String imgTag = ReportGenerator.fillAltAndSrc(wordcounts_persource_template, src, src);
			wordcounts_persource_out+=imgTag;
		}
		html = ReportGenerator.replaceTextinString(html, REGEX_wordcounts_persource, wordcounts_persource_out);

		// replace remaining comments
		html = ReportGenerator.replaceTextinString(html, REGEX_comments, "");

		HtmlReport output = new HtmlReport(html, baseFolder);
		
		return output;
	}
	private static class Tuple<S extends Comparable<S>,T extends Comparable<T>> implements Comparable<Tuple<S,T>> {
		public Tuple(S s, T t) {
			super();
			this.s = s;
			this.t = t;
		}
		private S s = null;
		private T t = null;
		public S getS() {
			return s;
		}
		public void setS(S s) {
			this.s = s;
		}
		public T getT() {
			return t;
		}
		public void setT(T t) {
			this.t = t;
		}
		@Override
		public int compareTo(Tuple<S,T> o) {
			return this.s.compareTo(o.s);
		}
		@Override
		public String toString() {
			return "Tuple [s=" + s + ", t=" + t + "]";
		}

		
	}
	private static ImageSources generateChartImages(String targetFolder, AggregatedMessages agm) {
		ImageSources out = new ImageSources();
		File folder = new File(targetFolder);
		folder.mkdirs();
		
		ArrayList<Tuple<Integer, String>> l = new ArrayList<Tuple<Integer, String>>();
		for (String key : agm.getWordCounts().keySet()) {
			Tuple<Integer, String> t = new Tuple<Integer, String>(agm.getWordCounts().get(key), key);
			l.add(t);
		}
		Collections.sort(l);
		Collections.reverse(l);
		TreeMap<String, Integer> mtotalmap = new TreeMap<String, Integer>();
		for (int i=0; i<Math.min(l.size(), 10);i++) {
			Tuple<Integer, String> t = l.get(i);
			mtotalmap.put(t.getT(), t.getS());
		}
		//log.info("treemap:"+mtotalmap.size()+" "+mtotalmap.toString());
		out.imgMessagesTotal = ReportGenerator.createPieChartImage(targetFolder+"/messagesTotal.png", mtotalmap, "Wordcounts per Source", null, 800, 600);
		return out;
	}
	private static char[] alphaNumericCharacters;
	{
		StringBuilder tmp = new StringBuilder();
	    for (char ch = '0'; ch <= '9'; ++ch)
	      tmp.append(ch);
	    for (char ch = 'a'; ch <= 'z'; ++ch)
	      tmp.append(ch);
	    alphaNumericCharacters = tmp.toString().toCharArray();
	}
	public static String getRandomAlphaNumericString(int length) {
		Random r = new Random();
		String out = "";
		for (int i=0; i<length; i++) {
			int select = r.nextInt(ReportGenerator.alphaNumericCharacters.length);
			out += ReportGenerator.alphaNumericCharacters[select];
		}
		return out;
	}
	private static String createPieChartImage(String targetFile, SortedMap<? extends Comparable<?>, ? extends Number> namesAndValues, String title, String subTitle, int widthPx, int heightPx) {
		
		//log.info("createPieChartImage");
		//log.info("treemap:"+namesAndValues.size()+" "+namesAndValues.toString());
		DefaultPieDataset dpd = new DefaultPieDataset();
		for (Comparable<?> key : namesAndValues.keySet()) {
			dpd.setValue(key, namesAndValues.get(key));
		}
		JFreeChart chart = ChartFactory.createPieChart(
				title,  // chart title
				dpd,            // data
				false,              // no legend
				false,               // tooltips
				false               // no URL generation
				);
		
		// set a custom background for the chart
		chart.setBackgroundPaint(new GradientPaint(new Point(0, 0), 
				new Color(20, 20, 20), new Point(400, 200), Color.DARK_GRAY));

		// customise the title position and font
		TextTitle t = chart.getTitle();
		t.setHorizontalAlignment(HorizontalAlignment.LEFT);
		t.setPaint(new Color(240, 240, 240));
		t.setFont(new Font("Arial", Font.BOLD, 26));

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(null);
		plot.setInteriorGap(0.04);
		plot.setOutlineVisible(false);

		// customise the section label appearance
		plot.setLabelFont(new Font("Courier New", Font.BOLD, 20));
		plot.setLabelLinkPaint(Color.WHITE);
		plot.setLabelLinkStroke(new BasicStroke(2.0f));
		plot.setLabelOutlineStroke(null);
		plot.setLabelPaint(Color.WHITE);
		plot.setLabelBackgroundPaint(null);

		if (subTitle != null) {
			// add a subtitle giving the data source
			TextTitle source = new TextTitle(subTitle, new Font("Courier New", Font.PLAIN, 12));
			source.setPaint(Color.WHITE);
			source.setPosition(RectangleEdge.BOTTOM);
			source.setHorizontalAlignment(HorizontalAlignment.RIGHT);
			chart.addSubtitle(source);
		}

		File outFile = new File(targetFile);
		if (outFile.exists()) {
			if (!outFile.delete()) {
				throw new ReportGeneratorException("Cannot delete existing file "+outFile.getName());
			}
		}
		try {
			//log.info("Saving image");
			ChartUtilities.saveChartAsPNG(outFile, chart, widthPx, heightPx);
		} catch (IOException e) {
			throw new ReportGeneratorException("IOException occured on saving Chart to image file", e);
		}
		//log.info("createPieChartImage done "+targetFile);
		return targetFile;
	}
	private static String fillAltAndSrc(String input, String alt, String src) {
		String out = input;
		out = ReportGenerator.replaceTextinString(input, REGEX_alt, alt);
		out = ReportGenerator.replaceTextinString(out, REGEX_src, src);
		return out;
	}
	public static String replaceTextinString(String string,String regex, String replacement) {
		String inStr = string;
		Pattern p = Pattern.compile(regex); // Compiles regular expression into Pattern.
		Matcher m = p.matcher(inStr); // Creates Matcher with subject s and Pattern p.
		if (!m.find()) {
			throw new ReportGeneratorException("regex "+regex+" not found in string");
		}
		String outStr = inStr.replaceAll(regex, replacement);
		return outStr;
	}
	public static List<List<String>> getMatchingGroups(String string,String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		ArrayList<List<String>> out = new ArrayList<List<String>>();
		while (m.find()) {
			ArrayList<String> groups = new ArrayList<String>();
			for (int i=1; i<=m.groupCount();i++) {
				groups.add(m.group(i));
			}
			out.add(groups);
		}
		return out;
	}
	public static void replaceTextinFile(File file,String regex, String replacement, File outputFile) throws IOException {
		String inStr = fileToString(file);
		String outStr = replaceTextinString(inStr, regex, replacement);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		writer.write(outStr);
		writer.close();
	}

	public static String fileToString(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			StringBuilder stringBuilder = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				stringBuilder.append(line);
				stringBuilder.append("\n");
				line = br.readLine();
			}
			return stringBuilder.toString();
		} finally {
			br.close();
		}
	}
	public static class ReportGeneratorException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5598684572096219774L;
		public ReportGeneratorException(String msg) {
			super(msg);
		}
		public ReportGeneratorException(String msg, Exception e) {
			super(msg, e);
		}
	}
	/**
	 * Container class for Html Reports
	 * @author CLF
	 *
	 */
	public static class HtmlReport {
		HtmlReport(String html, String baseFolder) {
			this.html = html;
			this.baseFolder = baseFolder;
		}
		private String html = "";
		private String baseFolder = "";
		public String getHtml() {
			return html;
		}
		public void setHtml(String html) {
			this.html = html;
		}
		public String getBaseFolder() {
			return baseFolder;
		}
		public void setBaseFolder(String baseFolder) {
			this.baseFolder = baseFolder;
		}
		@Override
		public String toString() {
			return "HtmlOutput [html=" + html + ", baseFolder=" + baseFolder
					+ "]";
		}
	}
	
	
	
	public static void test() {
		DefaultPieDataset dpd = new DefaultPieDataset();
		dpd.setValue("Trololo", new Double(70));
		dpd.setValue("Trololo2", new Double(20));
		dpd.setValue("Trololo3", new Double(10));
		JFreeChart chart = ChartFactory.createPieChart(
				"Trololo Title",  // chart title
	            dpd,            // data
	            false,              // no legend
	            false,               // tooltips
	            false               // no URL generation
	            );
		// set a custom background for the chart
        chart.setBackgroundPaint(new GradientPaint(new Point(0, 0), 
                new Color(20, 20, 20), new Point(400, 200), Color.DARK_GRAY));

        // customise the title position and font
        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setPaint(new Color(240, 240, 240));
        t.setFont(new Font("Arial", Font.BOLD, 26));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.04);
        plot.setOutlineVisible(false);

        // use gradients and white borders for the section colours
        plot.setSectionPaint("Trololo", createGradientPaint(new Color(200, 200, 255), Color.BLUE));
        plot.setSectionPaint("Trololo2", createGradientPaint(new Color(255, 200, 200), Color.RED));
        plot.setSectionPaint("Trololo3", createGradientPaint(new Color(200, 255, 200), Color.GREEN));
       plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));

        // customise the section label appearance
        plot.setLabelFont(new Font("Courier New", Font.BOLD, 20));
        plot.setLabelLinkPaint(Color.WHITE);
        plot.setLabelLinkStroke(new BasicStroke(2.0f));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(null);
        
        // add a subtitle giving the data source
        TextTitle source = new TextTitle("Source: http://www.trololo.org/url", 
                new Font("Courier New", Font.PLAIN, 12));
        source.setPaint(Color.WHITE);
        source.setPosition(RectangleEdge.BOTTOM);
        source.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        chart.addSubtitle(source);
        
		try {
			//log.info("saving");
			ChartUtilities.saveChartAsPNG(new File("piechart_trololo.png"), chart, 800, 600);
			//log.info("saved");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/**
     * A utility method for creating gradient paints.
     * 
     * @param c1  color 1.
     * @param c2  color 2.
     * 
     * @return A radial gradient paint.
     */
    private static RadialGradientPaint createGradientPaint(Color c1, Color c2) {
        Point2D center = new Point2D.Float(0, 0);
        float radius = 200;
        float[] dist = {0.0f, 1.0f};
        return new RadialGradientPaint(center, radius, dist,
                new Color[] {c1, c2});
    }
}
