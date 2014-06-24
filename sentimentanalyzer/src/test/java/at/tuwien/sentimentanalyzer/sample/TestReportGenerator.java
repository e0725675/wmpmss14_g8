package at.tuwien.sentimentanalyzer.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import at.tuwien.sentimentanalyzer.beans.MessageMocker;
import at.tuwien.sentimentanalyzer.beans.ReportGenerator;

import com.itextpdf.text.DocumentException;

public class TestReportGenerator {
	
	
	@Test
	public void experimentGenerateReport() throws IOException, DocumentException {
		ReportGenerator r = new ReportGenerator();
		List<String> l = new ArrayList<String>();
		l.add("mock_swearwordlist.txt");
		l.add("mock_nonwordslist.txt");
		MessageMocker mm = new MessageMocker("mock_userlist.txt", "mock_wordlist.txt", l);
		
		r.generateAggregatedMessagesPDFReport(mm.nextAggregatedMessage());
	}
}
