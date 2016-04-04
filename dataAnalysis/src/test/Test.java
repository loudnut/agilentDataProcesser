package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Test {

	public static void main(String args[]) throws FileNotFoundException,
			IOException {
		BufferedReader br = new BufferedReader(new FileReader("D:\\test.csv"));
		String aLineOfFile;
		while ((aLineOfFile = br.readLine()) != null)
			System.out.println(aLineOfFile);
	}
}