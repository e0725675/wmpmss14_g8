package at.tuwien.sentimentanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("camel-config.xml");
		System.out.println("Type \"exit\" to exit.");
		while (true) {
			try {
				line = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your name!");
				System.exit(1);
			}
			if (line.equalsIgnoreCase("exit")) {
				break;
			}
		}
		ctx.registerShutdownHook();
	}

}
