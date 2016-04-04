package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataProcessor {

	File directory;
	Boolean includeBackwardScan;
	File[] dataList;

	int IdPosition;

	public DataProcessor(File directory, Boolean includeBackwardScan) {
		this.directory = directory;
		this.includeBackwardScan = includeBackwardScan;
		dataList = directory.listFiles();

	}

	public void handleAllScans() throws FileNotFoundException, IOException {
		for (int i = 0; i < dataList.length; i++)
			handleOneScan(dataList[i].toString());
	}

	public void handleOneScan(String fileName) throws FileNotFoundException,
			IOException {
		String[] originalData = new String[3];
		Double[] handledData = new Double[5];
		int stop = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			String test = this.directory
					+ "\\"
					+ (includeBackwardScan ? "backward-included output data"
							: "output data")
					+ fileName.substring(fileName.lastIndexOf("\\"),
							fileName.lastIndexOf(".")) + ".txt";
			System.out.println(test);
			
			File temp = new File(this.directory + (includeBackwardScan ? "\\backward-included output data" : "\\output data"));
			temp.mkdir();
			
			FileWriter writer = new FileWriter(test);
			String aLineOfFile;

			while ((aLineOfFile = br.readLine()) != null) {
				if (stop == 1)
					break;

				if (aLineOfFile.indexOf("DataValue") != -1) {
					originalData = aLineOfFile.substring(
							aLineOfFile.indexOf(',') + 2).split(",");
					for (int i = 0; i < originalData.length; i++) {
						handledData[i] = Double.parseDouble(originalData[i]);
					}
					handledData[3] = Math.log10(handledData[IdPosition]);
					handledData[4] = Math.sqrt(handledData[IdPosition]);

					if (handledData[0] > 9.9 && includeBackwardScan == false)
						stop = 1;

					for (int i = 0; i < handledData.length; i++) {
						writer.append(handledData[i].toString());
						if (i < handledData.length - 1)
							writer.append(",");
					}
					writer.append("\r\n");
				}

				else if (aLineOfFile.indexOf("DataName") != -1) {

					// depends on the output format of 4155
					IdPosition = (aLineOfFile.indexOf("ID") == 14) ? 1 : 2;

					writer.append(aLineOfFile.substring(aLineOfFile
							.indexOf(',') + 2)
							+ ", logID"
							+ ", ID^0.5"
							+ "\r\n");
				}
			}

			writer.flush();

			System.out.print("OK");

		} catch (IOException e) {
			System.out.println("Error: Reading, Parsing, Computing");
		}

	}
}
