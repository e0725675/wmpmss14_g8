package at.tuwien.sentimentanalyzer.beans;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

import at.tuwien.sentimentanalyzer.entities.AggregatedMessages;
import at.tuwien.sentimentanalyzer.entities.Message.Sentiment;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class ReportGenerator {
	private static Logger log = Logger.getLogger(ReportGenerator.class);
	public ReportGenerator() {

	}
	private static  final String REGEX_mintime = "<!--###mintime###-->";
	private static  final String REGEX_maxtime = "<!--###maxtime###-->";

	private static  final String REGEX_sentimentpercentages_total = "<!--###sentimentpercentages_total###-->";
	private static  final String REGEX_wordcounts_total = "<!--###wordcounts_total###-->";
	private static  final String REGEX_messages_total = "<!--###messages_total###-->";


	private static  final String REGEX_comments = "(<!--.*?-->)";
	private static class ImageSources {
		public String imgSentimentpercentagesTotal = "imgSentimentpercentagesTotal";
		public String imgWordcountsTotal = "imgWordcountsTotal";
		public String imgMessagesTotal = "imgMessagesTotal";
	}
	/**
	 * Generates an PDF report for AggregatedMessage
	 * @param agm
	 * @return path of the generated PDF
	 * @throws IOException
	 * @throws DocumentException 
	 */
	public String generateAggregatedMessagesPDFReport(AggregatedMessages agm) throws IOException, DocumentException {
		String baseFolder = "";
		File file = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		do {
			Date date = new Date();
			baseFolder = "htmlMessageReports/"+df.format(date)+"_"+ReportGenerator.getRandomAlphaNumericString(4);
			file = new File(baseFolder);
		} while(file.exists());

		ImageSources imageSources = ReportGenerator.generateChartImages(baseFolder, agm);
		File outFile = new File(baseFolder+"/report.pdf");
		Document document = new Document(PageSize.A4, 0, 0, 0, 0);
		PdfWriter writer;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(outFile));
		} catch (FileNotFoundException e) {
			throw new ReportGeneratorException("FileNotFoundException on FileOutputStream creation of "+outFile.getPath(),e);
		} catch (DocumentException e) {
			throw new ReportGeneratorException("DocumentException on PdfWriter.getInstance",e);
		}
		document.open();
		Paragraph p = new Paragraph();
		p.add("Sentiment Report");
		p.setAlignment(Paragraph.ALIGN_CENTER);
		Font font = new Font(Font.FontFamily.HELVETICA, 30);
		p.setFont(font);
		document.add(p);
		
		Image imgSentimentpercentagesTotal = Image.getInstance(imageSources.imgSentimentpercentagesTotal);
		imgSentimentpercentagesTotal.setAlignment(Image.ALIGN_CENTER);
		imgSentimentpercentagesTotal.scaleToFit(300, 300);
		document.add(imgSentimentpercentagesTotal);
		
//		Paragraph tp = new Paragraph(90);
//		tp.setAlignment(Paragraph.ALIGN_CENTER);
//		for (int i=0; i<10; i++) {
//			Image test = Image.getInstance(imageSources.imgSentimentpercentagesTotal);
//			test.scaleToFit(100, 100);
//			Chunk c = new Chunk(test,0,0);
//			tp.add(c);
//			
//		}
//		document.add(tp);
		
		Image imgWordcountsTotal = Image.getInstance(imageSources.imgWordcountsTotal);
		imgWordcountsTotal.setAlignment(Image.ALIGN_CENTER);
		imgWordcountsTotal.scaleToFit(300, 300);
		document.add(imgWordcountsTotal);
		
		Image imgMessagesTotal = Image.getInstance(imageSources.imgMessagesTotal);
		imgMessagesTotal.setAlignment(Image.ALIGN_CENTER);
		imgMessagesTotal.scaleToFit(300, 300);
		document.add(imgMessagesTotal);
		
//		document.newPage();
		

		
		document.close();
		writer.close();
		return outFile.getPath();
	}
	/**
	 * Generates an PDF report for AggregatedMessages
	 * doesnt work correctly
	 * @param agm
	 * @return path of the generated PDF
	 * @throws IOException 
	 */
	@Deprecated
	public String generateAggregatedMessagesPDFReportOld(AggregatedMessages agm) throws IOException {
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

		// replace times
		DateFormat df2 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		html = ReportGenerator.replaceTextinString(html, REGEX_mintime, df2.format(agm.getMinTimePosted()));
		html = ReportGenerator.replaceTextinString(html, REGEX_maxtime, df2.format(agm.getMaxTimePosted()));

		// replace total markers with imagepaths
		html = ReportGenerator.replaceTextinString(html, REGEX_sentimentpercentages_total, imgSentimentpercentagesTotal);
		html = ReportGenerator.replaceTextinString(html, REGEX_wordcounts_total, imgWordcountsTotal);
		html = ReportGenerator.replaceTextinString(html, REGEX_messages_total, imgMessagesTotal);

		// replace remaining comments
		html = ReportGenerator.replaceTextinString(html, REGEX_comments, "");

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(baseFolder+"/report.html")));
		bw.write(html);
		bw.close();
		convertFile(new File(baseFolder+"/report.html"),new File(baseFolder+"/report.pdf"));
		return baseFolder+"/report.pdf";
	}

	/**
	 * Convert input html file to output pdf file
	 * @param file
	 * @param outFile
	 * @return
	 */
	public static boolean convertFile(File file, File outFile) {
		if (file.isFile()) {
			Document document = new Document(PageSize.A4, 0, 0, 0, 0);
			PdfWriter writer;
			try {
				writer = PdfWriter.getInstance(document, new FileOutputStream(outFile));
			} catch (FileNotFoundException e) {
				throw new ReportGeneratorException("FileNotFoundException on FileOutputStream creation of "+outFile.getPath(),e);
			} catch (DocumentException e) {
				throw new ReportGeneratorException("DocumentException on PdfWriter.getInstance",e);
			}
			document.open();
			try {
				XMLWorkerHelper.getInstance().parseXHtml(writer, document,
						new FileInputStream(file));
			} catch (FileNotFoundException e) {
				throw new ReportGeneratorException("FileNotFoundException on parsing XHtml",e);
			} catch (IOException e) {
				throw new ReportGeneratorException("IOException on parsing XHtml",e);
			}
			document.close();
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Helper class
	 * @author CLF
	 *
	 * @param <S>
	 * @param <T>
	 */
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
		public T getT() {
			return t;
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

	private static ImageSources generateChartImages(String targetFolder, AggregatedMessages agm) throws IOException {
		ImageSources out = new ImageSources();
		File folder = new File(targetFolder);
		folder.mkdirs();
		ArrayList<Tuple<Integer, String>> l;
		l = new ArrayList<Tuple<Integer, String>>();
		for (String key : agm.getWordCounts().keySet()) {
			Tuple<Integer, String> t = new Tuple<Integer, String>(agm.getWordCounts().get(key), key);
			l.add(t);
		}
		Collections.sort(l);
		Collections.reverse(l);
		TreeMap<String, Integer> wctotalmap = new TreeMap<String, Integer>();
		for (int i=0; i<Math.min(l.size(), 10);i++) {
			Tuple<Integer, String> t = l.get(i);
			wctotalmap.put(t.getT(), t.getS());
		}

		l = new ArrayList<Tuple<Integer, String>>();
		for (String key : agm.getSourceCounts().keySet()) {
			Tuple<Integer, String> t = new Tuple<Integer, String>(agm.getSourceCounts().get(key), key);
			l.add(t);
		}
		Collections.sort(l);
		Collections.reverse(l);
		TreeMap<String, Integer> mtotalmap = new TreeMap<String, Integer>();
		for (int i=0; i<Math.min(l.size(), 10);i++) {
			Tuple<Integer, String> t = l.get(i);
			mtotalmap.put(t.getT(), t.getS());
		}


		ArrayList<Tuple<Integer, Sentiment>> l2 = new ArrayList<Tuple<Integer, Sentiment>>();
		if (agm.getSentimentCounts().size() != 3) {
			throw new ReportGeneratorException("Invalid SentimentCountsSize. should be 3 but was "+agm.getSentimentCounts().size());
		}
		for (Sentiment key : agm.getSentimentCounts().keySet()) {
			Tuple<Integer, Sentiment> t = new Tuple<Integer, Sentiment>(agm.getSentimentCounts().get(key), key);
			l2.add(t);
		}
		Collections.sort(l);
		Collections.reverse(l);
		TreeMap<Sentiment, Integer> senttotalmap = new TreeMap<Sentiment, Integer>();
		for (int i=0; i<Math.min(l2.size(), 10);i++) {
			Tuple<Integer, Sentiment> t = l2.get(i);
			senttotalmap.put(t.getT(), t.getS());
		}

		//log.info("treemap:"+mtotalmap.size()+" "+mtotalmap.toString());
		out.imgWordcountsTotal = ReportGenerator.createPieChartImage(targetFolder+"/wordcountsTotal.png", wctotalmap, "Wordcounts", null, 800, 600);
		out.imgMessagesTotal = ReportGenerator.createBarChartImage(targetFolder+"/messagesTotal.png", mtotalmap, "Messages per Source", null, "Sources", "Number of Messages", 800, 600);
		out.imgSentimentpercentagesTotal = ReportGenerator.createPieChartImage(targetFolder+"/sentimentsTotal.png", senttotalmap, "Sentiments", null, 800, 600);
		//ReportGenerator.createPieChartImage(targetFolder+"/messagesTotal.png", wctotalmap, "Wordcounts per Source", null, 800, 600);

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
	public static String createPieChartImage(String targetFile, SortedMap<? extends Comparable<?>, ? extends Number> namesAndValues, String title, String subTitle, int widthPx, int heightPx) {

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
		chart.setBackgroundPaint(Color.WHITE);

		// customise the title position and font
		TextTitle t = chart.getTitle();
		t.setHorizontalAlignment(HorizontalAlignment.CENTER);
		t.setPaint(Color.BLACK);
		t.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 26));

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(null);
		plot.setInteriorGap(0.04);
		plot.setOutlineVisible(false);

		// customise the section label appearance
		plot.setLabelLinksVisible(true);
		plot.setLabelFont(new java.awt.Font("Courier New", java.awt.Font.BOLD, 20));
		//plot.setLabelLinkPaint(Color.WHITE);
		//plot.setLabelLinkStroke(new BasicStroke(2.0f));
		plot.setLabelOutlineStroke(null);
		plot.setLabelPaint(Color.BLACK);
		plot.setLabelBackgroundPaint(Color.WHITE);
		plot.setLabelShadowPaint(null);


		if (subTitle != null) {
			// add a subtitle giving the data source
			TextTitle source = new TextTitle(subTitle, new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
			source.setPaint(Color.BLACK);
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
		//return outFile.getName();
		return targetFile;
	}

	public static String createBarChartImage(String targetFile, 
			SortedMap<? extends Comparable<?>, ? extends Number> namesAndValues, 
			String title, String subTitle, String categoryAxisLabel, 
			String valueAxisLabel, int widthPx, int heightPx) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		List<Comparable<?>> c = new LinkedList<Comparable<?>>(namesAndValues.keySet());
		for (int i=c.size()-1; i>=0; i--) {
			Number value = namesAndValues.get(c.get(i));
			dataset.addValue(value, c.get(i), "");
		}
		JFreeChart bchart = ChartFactory.createBarChart(
				title, 
				categoryAxisLabel, 
				valueAxisLabel, 
				dataset, 
				PlotOrientation.HORIZONTAL, 
				false, //legend
				false, //tooltips
				false); // urls
		// set a custom background for the chart
		bchart.setBackgroundPaint(Color.WHITE);

		// customise the title position and font
		TextTitle t = bchart.getTitle();
		t.setHorizontalAlignment(HorizontalAlignment.CENTER);
		t.setPaint(Color.BLACK);
		t.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 26));

		CategoryPlot plot = (CategoryPlot) bchart.getPlot();
		plot.setBackgroundPaint(null);
		CategoryItemLabelGenerator generator
		= new StandardCategoryItemLabelGenerator("{0}",
				NumberFormat.getInstance());
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 18));
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.CENTER, TextAnchor.BASELINE_CENTER));
		renderer.setMaximumBarWidth(1);
		renderer.setItemMargin(0.1);

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		if (subTitle != null) {
			// add a subtitle giving the data source
			TextTitle source = new TextTitle(subTitle, new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
			source.setPaint(Color.BLACK);
			source.setPosition(RectangleEdge.BOTTOM);
			source.setHorizontalAlignment(HorizontalAlignment.RIGHT);
			bchart.addSubtitle(source);
		}

		File outFile = new File(targetFile);
		if (outFile.exists()) {
			if (!outFile.delete()) {
				throw new ReportGeneratorException("Cannot delete existing file "+outFile.getName());
			}
		}
		try {
			//log.info("Saving image");
			ChartUtilities.saveChartAsPNG(outFile, bchart, widthPx, heightPx);
		} catch (IOException e) {
			throw new ReportGeneratorException("IOException occured on saving Chart to image file", e);
		}
		//log.info("createPieChartImage done "+targetFile);
		//return outFile.getName();
		return targetFile;
	}

	public static String replaceTextinString(String string,String regex, String replacement) {
		String inStr = string;
		Pattern p = Pattern.compile(regex); // Compiles regular expression into Pattern.
		Matcher m = p.matcher(inStr); // Creates Matcher with subject s and Pattern p.
		if (!m.find()) {
			//throw new ReportGeneratorException("regex "+regex+" not found in string");
			return string;
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

}
