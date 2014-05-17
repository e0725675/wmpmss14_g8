package at.clf.tests.htmltopdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;


/**
 * Converts HTML-files, which are passed as arguments to pdf
**/
public class HtmlToPdfTest {
	public static void main(String[] args) {
		System.out.println("HtmlToPdfTest");
		if (args.length > 1) {
			for (int i=1; i<args.length; i++) {
				convertFile(new File(args[i]));
			}
		} else {
			String jarDir = new File(".").getAbsolutePath();
			File folder = new File(jarDir);
			if (!folder.isDirectory()) {
				System.out.println("Root is not a directory");
				return;
			}
			File[] listOfFiles = folder.listFiles();
	
			for (int i = 0; i < listOfFiles.length; i++) {
				if (!convertFile(listOfFiles[i])); {
					if (listOfFiles[i].isDirectory()) {
						System.out.println("Directory " + listOfFiles[i].getName());
					}
				}
				
			}
		}
	}

	public static boolean convertFile(File file) {
		if (file.isFile()) {
			String filename = file.getName();
			System.out.println("File " + filename);
			
			StringTokenizer tok = new StringTokenizer(filename, ".");
			String token;
			do {
				token = tok.nextToken();
			} while (tok.hasMoreElements());
			
			if (!"html".equals(token)) {
				return false;
			}
			System.out.println( "Start Converting" );
			// step 1
	        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
	        // step 2
	        PdfWriter writer;
			try {
				writer = PdfWriter.getInstance(document, new FileOutputStream(file.getName()+".pdf"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println( "fail 1" );
				return false;
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println( "fail 2" );
				return false;
			}
	        // step 3
	        document.open();
	        // step 4
	        try {
				XMLWorkerHelper.getInstance().parseXHtml(writer, document,
				        new FileInputStream(file.getName()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println( "fail 3" );
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println( "fail 4" );
				return false;
			}
	        //step 5
	         document.close();
	 
	        System.out.println( "PDF Created!" );
	        return true;
		} else {
			return false;
		}
	}
}
