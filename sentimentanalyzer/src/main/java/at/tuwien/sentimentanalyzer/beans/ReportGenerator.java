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
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.camel.Exchange;
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
import org.jfree.util.Log;

import at.tuwien.sentimentanalyzer.entities.AggregatedMessages;
import at.tuwien.sentimentanalyzer.entities.Message.Sentiment;
import at.tuwien.sentimentanalyzer.entities.Message.Source;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
/**
 * Generated PDF Reports
 * Needs to be instanced.
 * @author CLF
 *
 */
public class ReportGenerator {
	public ReportGenerator() {

	}
	/**
	 * Helper class to store generated image filepaths
	 * @author CLF
	 *
	 */
	private static class ImageSources {
		public String imgSentimentpercentagesTotal = "imgSentimentpercentagesTotal";
		public List<String> imgSentimentpercentagesPerSource = new ArrayList<String>();
		public String imgWordcountsTotal = "imgWordcountsTotal";
		public String imgMessagesTotal = "imgMessagesTotal";
		public List<String> imgWordcountsPerSource = new ArrayList<String>();
	}
	
	
	/**
	 * Generates an PDF report for AggregatedMessage
	 * @param body
	 * @return path of the generated PDF
	 * @throws IOException
	 * @throws DocumentException 
	 */
	public void generateAggregatedMessagesPDFReport(Exchange exchange,AggregatedMessages body) throws IOException, DocumentException {
		exchange.setOut(exchange.getIn());
		body.validate();
		
		String baseFolder = "";
		File file = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		do {
			Date date = new Date();
			baseFolder = "htmlMessageReports/"+
					df.format(date)+"_"+
					ReportGenerator.getRandomAlphaNumericString(4)+
					"_from_"+df.format(body.getMinTimePosted())+
					"_to_"+df.format(body.getMaxTimePosted());
			file = new File(baseFolder);
		} while(file.exists());
		
		
		ImageSources imageSources = ReportGenerator.generateChartImages(baseFolder, body);
		File outFile = new File(baseFolder+"/report.pdf");
		stringToFile(body.toString(), baseFolder+"/aggregatedMessage.txt");
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
		
		Paragraph pss = new Paragraph();
		pss.setAlignment(Paragraph.ALIGN_CENTER);
		for (String s : imageSources.imgSentimentpercentagesPerSource) {
			Image imgSentimentpercentagesSource = Image.getInstance(s);
			imgSentimentpercentagesSource.scaleToFit(200, 200);
			pss.add(new Chunk(imgSentimentpercentagesSource,0,0,true));
		}
		//document.add(Chunk.NEWLINE);
		document.add(pss);
		document.add(Chunk.NEXTPAGE);
		
		Image imgWordcountsTotal = Image.getInstance(imageSources.imgWordcountsTotal);
		imgWordcountsTotal.setAlignment(Image.ALIGN_CENTER);
		imgWordcountsTotal.scaleToFit(300, 300);
		document.add(imgWordcountsTotal);
		
		Paragraph pws = new Paragraph();
		pws.setAlignment(Paragraph.ALIGN_CENTER);
		for (String s : imageSources.imgWordcountsPerSource) {
			Image imgWordCountsSource = Image.getInstance(s);
			imgWordCountsSource.scaleToFit(200, 200);
			pws.add(new Chunk(imgWordCountsSource,0,0,true));
		}
		document.add(pws);
		document.add(Chunk.NEXTPAGE);
		
		Image imgMessagesTotal = Image.getInstance(imageSources.imgMessagesTotal);
		imgMessagesTotal.setAlignment(Image.ALIGN_CENTER);
		imgMessagesTotal.scaleToFit(300, 300);
		document.add(imgMessagesTotal);
		
		document.close();
		writer.close();
		exchange.getOut().setHeader("reportfilename", outFile.getPath());
	}

	public static void stringToFile(String string, String filename) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filename);
		out.print(string);
		out.close();
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
	public static class Tuple<S extends Comparable<S>,T extends Number> implements Comparable<Tuple<S,T>> {
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
		// a negative integer, zero, 
		// or a positive integer as this object is less 
		// than, equal to, or greater than the specified object. 
		@Override
		public int compareTo(Tuple<S,T> o) {
			Number other = o.t;
			if (this.t == other) {
				return 0;
			}
			if (this.t.doubleValue() < other.doubleValue()) {
				return -1;
			} else {
				return 1;
			}
		}
		@Override
		public String toString() {
			return "Tuple [s=" + s + ", t=" + t + "]";
		}


	}
	private static final int MAXWORDCOUNT = 20;
	/**
	 * Generates chart images
	 * @param targetFolder
	 * @param agm
	 * @return
	 * @throws IOException
	 */
	private static ImageSources generateChartImages(String targetFolder, AggregatedMessages agm) throws IOException {
		ImageSources out = new ImageSources();
		File folder = new File(targetFolder);
		folder.mkdirs();
		ArrayList<Tuple<String, Number>> wordCountsTotal;
		wordCountsTotal = new ArrayList<Tuple<String, Number>>();
		int wordcounter = 0;
		int totalwordcounts = 0;
		for (String key : agm.getWordCounts().keySet()) {
			int i=agm.getWordCounts().get(key);
			totalwordcounts+=i;
			wordcounter++;
			Tuple<String, Number> t = new Tuple<String, Number>(key+" ("+i+")",i);
			wordCountsTotal.add(t);
			if (wordcounter>= MAXWORDCOUNT) {
				break;
			}
		}
		Collections.sort(wordCountsTotal);
		Collections.reverse(wordCountsTotal);
	
		
		List<Tuple<String, Number>> l_sourceCounts = new ArrayList<Tuple<String, Number>>();
		int totalsourcecounts = 0;
		for (Source key : agm.getSourceCounts().keySet()) {
			int i=agm.getSourceCounts().get(key);
			totalsourcecounts+=i;
			Tuple<String, Number> t = new Tuple<String, Number>(key.toString()+" ("+i+")",i);
			l_sourceCounts.add(t);
		}
		Collections.sort(l_sourceCounts);
		Collections.reverse(l_sourceCounts);
//		TreeMap<String, Number> mtotalmap = new TreeMap<String, Number>();
//		for (int i=0; i<Math.min(l.size(), 10);i++) {
//			Tuple<String, Number> t = l.get(i);
//			mtotalmap.put(t.getS(), t.getT());
//		}


		List<Tuple<String, Number>> sentimentCountsTotal = new ArrayList<Tuple<String, Number>>();
		if (agm.getSentimentCounts().size() != 3) {
			throw new ReportGeneratorException("Invalid SentimentCountsSize. should be 3 but was "+agm.getSentimentCounts().size());
		}
		SortedSet<Sentiment> sentimentkeyset = new TreeSet<Sentiment>(agm.getSentimentCounts().keySet());
		//Collections.sort(sentimentkeyset);
		int totalsentiments = 0;
		for (Sentiment key : sentimentkeyset) {
			int i=agm.getSentimentCounts().get(key);
			totalsentiments+=i;
			Tuple<String, Number> t = new Tuple<String ,Number>(key.toString()+" ("+i+")", i);
			sentimentCountsTotal.add(t);
		}
		//Collections.sort(sentimentCountsTotal);
		//Collections.reverse(sentimentCountsTotal);

		//log.info("treemap:"+mtotalmap.size()+" "+mtotalmap.toString());
		out.imgWordcountsTotal = ReportGenerator.createPieChartImage(targetFolder+"/wordcountsTotal.png", wordCountsTotal, "Wordcounts"+" ("+totalwordcounts+")", null, 800, 600);
		out.imgMessagesTotal = ReportGenerator.createBarChartImage(targetFolder+"/messagesTotal.png", l_sourceCounts, "Messages per Source"+" ("+totalsourcecounts+")", null, "Sources", "Number of Messages", 800, 600);
		out.imgSentimentpercentagesTotal = ReportGenerator.createPieChartImage(targetFolder+"/sentimentsTotal.png", sentimentCountsTotal, "Sentiments"+" ("+totalsentiments+")", null, 800, 600);
		//ReportGenerator.createPieChartImage(targetFolder+"/messagesTotal.png", wctotalmap, "Wordcounts per Source", null, 800, 600);

		
		// per source
		List<Source> sources = new ArrayList<Source>(agm.getSourceCounts().keySet());
		Collections.sort(sources);
		for (Source source : sources) {
			// sentiments
			List<Tuple<String,Number>> sentimentCounts = new ArrayList<Tuple<String,Number>>();
			HashMap<Sentiment, Integer> sentimentsPerSource = agm.getSentimentCountsBySource().getByGroupKeyMapped(source);
			int counter = 0;
			SortedSet<Sentiment> sentimentPerSourcekeyset = new TreeSet<Sentiment>(sentimentsPerSource.keySet());
			int totalSentimentsPerSource=0;
			for (Sentiment s : sentimentPerSourcekeyset) {
				int i = sentimentsPerSource.get(s);
				totalSentimentsPerSource+=i;
				sentimentCounts.add(new Tuple<String,Number>(s.toString()+" ("+i+")", i));
				counter++;
				if (counter > 10) {
					break;
				}
			}
			Collections.sort(sentimentCounts);
			Collections.reverse(sentimentCounts);
			out.imgSentimentpercentagesPerSource.add(ReportGenerator.createPieChartImage(targetFolder+"/sentiments"+source+".png", sentimentCounts, "Sentiments "+source+" ("+totalSentimentsPerSource+")", null, 800, 600));
			
			//wordcounts
			List<Tuple<String,Number>> wordCounts = new ArrayList<Tuple<String,Number>>();
			HashMap<String, Integer> wordsPerSource = agm.getWordCountsBySource().getByGroupKeyMapped(source);
			counter = 0;
			int totalWordsPerSource=0;
			for (String s : wordsPerSource.keySet()) {
				int i = wordsPerSource.get(s);
				totalWordsPerSource+=i;
				wordCounts.add(new Tuple<String,Number>(s+" ("+i+")", i));
				counter++;
				if (counter > 10) {
					break;
				}
			}
			Collections.sort(wordCounts);
			Collections.reverse(wordCounts);
			out.imgWordcountsPerSource.add(ReportGenerator.createPieChartImage(targetFolder+"/wordcountsTotal"+source+".png", wordCounts, "Wordcounts "+source+" ("+totalWordsPerSource+")", null, 800, 600));
		}
		
		//out.imgSentimentpercentagesPerSource = ReportGenerator.createPieChartImage(targetFolder+"/sentimentsPerSource.png", sentimentsPerSource, "Wordcounts", null, 800, 600);
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
	/**
	 * Creates a PieChart image
	 * @param targetFile
	 * @param namesAndValues
	 * @param title
	 * @param subTitle
	 * @param widthPx
	 * @param heightPx
	 * @return
	 */
	public static String createPieChartImage(String targetFile, List<Tuple<String,Number>> namesAndValues, String title, String subTitle, int widthPx, int heightPx) {

		DefaultPieDataset dpd = new DefaultPieDataset();
		for (Tuple<String, ? extends Number> entry : namesAndValues) {
			dpd.setValue(entry.getS(), entry.getT());
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
			ChartUtilities.saveChartAsPNG(outFile, chart, widthPx, heightPx);
		} catch (IOException e) {
			throw new ReportGeneratorException("IOException occured on saving Chart to image file", e);
		}

		return targetFile;
	}
	/**
	 * Creates a BarChart image
	 * @param targetFile
	 * @param namesAndValues
	 * @param title
	 * @param subTitle
	 * @param categoryAxisLabel
	 * @param valueAxisLabel
	 * @param widthPx
	 * @param heightPx
	 * @return
	 */
	public static String createBarChartImage(String targetFile, 
			List<Tuple<String,Number>> namesAndValues, 
			String title, String subTitle, String categoryAxisLabel, 
			String valueAxisLabel, int widthPx, int heightPx) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (Tuple<String,Number> name : namesAndValues) {
			dataset.addValue(name.getT(), name.getS(), "");
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
		return targetFile;
	}
	/**
	 * Replace text in input string
	 * @param string
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static String replaceTextinString(String string,String regex, String replacement) {
		String inStr = string;
		Pattern p = Pattern.compile(regex); // Compiles regular expression into Pattern.
		Matcher m = p.matcher(inStr); // Creates Matcher with subject s and Pattern p.
		if (!m.find()) {
			return string;
		}
		String outStr = inStr.replaceAll(regex, replacement);
		return outStr;
	}
	/**
	 * Returns matching regex groups from the input string using the input regex
	 * @param string
	 * @param regex
	 * @return
	 */
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
	/**
	 * Loads the input textfile and replaces text by regex
	 * @param file
	 * @param regex
	 * @param replacement
	 * @param outputFile
	 * @throws IOException
	 */
	public static void replaceTextinFile(File file,String regex, String replacement, File outputFile) throws IOException {
		String inStr = fileToString(file);
		String outStr = replaceTextinString(inStr, regex, replacement);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		writer.write(outStr);
		writer.close();
	}
	/**
	 * Helper method to load a file to string
	 * @param file
	 * @return
	 * @throws IOException
	 */
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
	/**
	 * Exception thrown by ReportGenerator
	 * @author CLF
	 *
	 */
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
