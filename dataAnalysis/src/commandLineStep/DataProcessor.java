package commandLineStep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataProcessor {

	File directory;
	File[] dataList;

	int IdPosition;
	int Id1Position;
	int Id2Position;

	public DataProcessor(File directory) {
		this.directory = directory;
		dataList = directory.listFiles();

	}

	public void handleAllSingleScans(double stopVoltage, Boolean includeBackwardScan)
			throws FileNotFoundException, IOException {
		for (int i = 0; i < dataList.length; i++)
			if (dataList[i].isFile())
				if (dataList[i].toString()
						.substring(dataList[i].toString().lastIndexOf("."))
						.indexOf("csv") != -1)
					handleOneSingleScan(dataList[i].toString(), stopVoltage,
							includeBackwardScan);
	}

	public void handleOneSingleScan(String fileName, double stopVoltage, Boolean includeBackwardScan)
			throws FileNotFoundException, IOException {
		String[] originalData = new String[3];
		Double[] handledData = new Double[5];
		int stop = 0;
		int dataStartParsing = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			String test = this.directory
					+ "\\"
					+ (includeBackwardScan ? "Single TFT_backward-included output data"
							: "Single TFT_output data")
					+ fileName.substring(fileName.lastIndexOf("\\"),
							fileName.lastIndexOf(".")) + ".txt";
			System.out.println(test);

			File temp = new File(
					this.directory
							+ (includeBackwardScan ? "\\Single TFT_backward-included output data"
									: "\\Single TFT_output data"));
			temp.mkdir();

			//
			System.out.println("\nAbout to create file: " + test);

			FileWriter writer = new FileWriter(test);

			//
			System.out.println("writer created");

			String aLineOfFile;

			while ((aLineOfFile = br.readLine()) != null) {

				//
				System.out.println(aLineOfFile);

				// termination condition for one-way scan
				if (stop == 1)
					break;

				if (aLineOfFile.indexOf("DataValue") != -1) {

					// trigger termination condition for backward-included scan
					dataStartParsing = 1;
					originalData = aLineOfFile.substring(
							aLineOfFile.indexOf(',') + 2).split(",");
					for (int i = 0; i < originalData.length; i++) {
						handledData[i] = Double.parseDouble(originalData[i]);
					}
					handledData[3] = Math.log10(handledData[IdPosition]);
					handledData[4] = Math.sqrt(handledData[IdPosition]);

					// trigger termination condition for one-way scan
					if (handledData[0] == stopVoltage && includeBackwardScan == false)
						stop = 1;

					for (int i = 0; i < handledData.length; i++) {
						writer.append(handledData[i].toString());
						if (i < handledData.length - 1)
							writer.append(",");
					}
					writer.append("\r\n");
				}

				else if (aLineOfFile.indexOf("DataName") != -1) {

					// I assume IdPosition depends on the output format of 4155
					IdPosition = (aLineOfFile.indexOf("ID") == 14) ? 1 : 2;

					writer.append(aLineOfFile.substring(aLineOfFile
							.indexOf(',') + 2)
							+ ", logID"
							+ ", ID^0.5"
							+ "\r\n");
				}

				// termination condition for backward-included scan
				else if (dataStartParsing == 1)
					break;
			}

			writer.flush();
			System.out.println(" - OK");
			writer.close();
		} catch (IOException e) {
			System.out.println("Error: Reading, Parsing, Computing");
		}

	}

	public void handleAllDoubleScans(double stopVoltage,
			Boolean includeBackwardScan) throws FileNotFoundException,
			IOException {
		for (int i = 0; i < dataList.length; i++)
			if (dataList[i].isFile())
				if (dataList[i].toString()
						.substring(dataList[i].toString().lastIndexOf("."))
						.indexOf("csv") != -1)
					handleOneDoubleScan(dataList[i].toString(), stopVoltage,
							includeBackwardScan);
	}

	public void handleOneDoubleScan(String fileName, double stopVoltage,
			Boolean includeBackwardScan) throws FileNotFoundException,
			IOException {
		String[] originalData = new String[4];
		Double[] handledData = new Double[8];
		int stop = 0;
		int dataStartParsing = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			String test = this.directory
					+ "\\"
					+ (includeBackwardScan ? "Double TFT_backward-included output data"
							: "Double TFT_output data")
					+ fileName.substring(fileName.lastIndexOf("\\"),
							fileName.lastIndexOf(".")) + ".txt";
			System.out.print(test);

			File temp = new File(
					this.directory
							+ (includeBackwardScan ? "\\Double TFT_backward-included output data"
									: "\\Double TFT_output data"));
			temp.mkdir();

			FileWriter writer = new FileWriter(test);
			String aLineOfFile;

			while ((aLineOfFile = br.readLine()) != null) {

				//
				System.out.println(aLineOfFile);

				// termination condition for one-way scan
				if (stop == 1)
					break;

				if (aLineOfFile.indexOf("DataValue") != -1) {

					// trigger termination condition for backward-included scan
					dataStartParsing = 1;
					originalData = aLineOfFile.substring(
							aLineOfFile.indexOf(',') + 2).split(",");
					for (int i = 0; i < originalData.length; i++) {
						handledData[i] = Double.parseDouble(originalData[i]);
					}
					handledData[4] = Math.log10(handledData[Id1Position]);
					handledData[5] = Math.sqrt(handledData[Id1Position]);
					handledData[6] = Math.log10(handledData[Id2Position]);
					handledData[7] = Math.sqrt(handledData[Id2Position]);

					// trigger termination condition for one-way scan
					// gate voltage range decision point!!!!!!!!!!!!!
					if (handledData[0] == stopVoltage
							&& includeBackwardScan == false)
						stop = 1;

					for (int i = 0; i < handledData.length; i++) {
						writer.append(handledData[i].toString());
						if (i < handledData.length - 1)
							writer.append(",");
					}
					writer.append("\r\n");
				}

				else if (aLineOfFile.indexOf("DataName") != -1) {

					// Hard-coded positions
					Id1Position = 2;
					Id2Position = 3;

					writer.append(aLineOfFile.substring(aLineOfFile
							.indexOf(',') + 2)
							+ ", logID1"
							+ ", ID1^0.5"
							+ ", logID2" + ", ID2^0.5" + "\r\n");
				}

				// termination condition for backward-included scan
				else if (dataStartParsing == 1)
					break;
			}

			writer.flush();
			System.out.println(" - OK");
			writer.close();
		} catch (IOException e) {
			System.out.println("Error: Reading, Parsing, Computing");
		}

	}

	public int getMeasurementTime(int measurementIndex) {
		int time = 0;
		String tempTimeArray[];
		if (dataList[measurementIndex].toString().indexOf("¤W¤È") != -1
				|| dataList[measurementIndex].toString().indexOf("¤U¤È 12_") != -1) {
			tempTimeArray = dataList[measurementIndex]
					.toString()
					.substring(
							dataList[measurementIndex].toString().indexOf("¤È ") + 2,
							dataList[measurementIndex].toString().indexOf("]"))
					.split("_");

			time = Integer.parseInt(tempTimeArray[0]) * 3600
					+ Integer.parseInt(tempTimeArray[1]) * 60
					+ Integer.parseInt(tempTimeArray[2]);
		} else if (dataList[measurementIndex].toString().indexOf("¤U¤È") != -1) {
			tempTimeArray = dataList[measurementIndex]
					.toString()
					.substring(
							dataList[measurementIndex].toString().indexOf("¤È ") + 2,
							dataList[measurementIndex].toString().indexOf("]"))
					.split("_");

			time = (Integer.parseInt(tempTimeArray[0]) + 12) * 3600
					+ Integer.parseInt(tempTimeArray[1]) * 60
					+ Integer.parseInt(tempTimeArray[2]);
		}
		return time;
	}

	public void handleAllRealTimeMeasurements() throws FileNotFoundException,
			IOException {
		String[] originalData = new String[5];
		Double[] handledData = new Double[3];

		// Create a new folder
		File temp = new File(this.directory + "\\"
				+ "Real Time Measurement_output data");
		temp.mkdir();

		// Set output txt filename
		String outputTxt = this.directory
				+ "\\"
				+ "Real Time Measurement_output data"
				+ "\\"
				+ this.directory.toString().substring(
						this.directory.toString().lastIndexOf("\\") + 1)
				+ ".txt";
		System.out.println(outputTxt);

		// Create a new file of handled data
		FileWriter writer = new FileWriter(outputTxt);

		// First line of file
		writer.append("Time" + ", ID1" + ", ID2" + "\r\n");

		// Choose a file
		int fileZeroIndex = 0;
		for (int i = 0; i < dataList.length; i++) {
			if (dataList[i].isFile())
				if (dataList[i].toString()
						.substring(dataList[i].toString().lastIndexOf("."))
						.indexOf("csv") != -1) {
					fileZeroIndex = i;
					break;
				}
		}

		// Set the initial measurement time
		int timeZero = getMeasurementTime(fileZeroIndex);
		for (int i = fileZeroIndex + 1; i < dataList.length; i++) {
			if (dataList[i].isFile())
				if (dataList[i].toString()
						.substring(dataList[i].toString().lastIndexOf("."))
						.indexOf("csv") != -1) {
					if (timeZero < getMeasurementTime(i))
						continue;
					else
						timeZero = getMeasurementTime(i);
				}
		}

		for (int i = 0; i < dataList.length; i++) {
			if (dataList[i].isFile())
				if (dataList[i].toString()
						.substring(dataList[i].toString().lastIndexOf("."))
						.indexOf("csv") != -1) {
					try {

						// Extract the IDs from each file (?)
						BufferedReader br = new BufferedReader(new FileReader(
								dataList[i].toString()));

						// System.out.println(dataList[i].toString());

						String aLineOfFile;
						while ((aLineOfFile = br.readLine()) != null) {

							// System.out.println(aLineOfFile);

							if (aLineOfFile.indexOf("DataValue") != -1) {
								originalData = aLineOfFile.substring(
										aLineOfFile.indexOf(',') + 2)
										.split(",");

								handledData[0] = (double) getMeasurementTime(i)
										- timeZero;
								handledData[1] = Double
										.parseDouble(originalData[3]);
								handledData[2] = Double
										.parseDouble(originalData[4]);

								/*
								 * System.out.println(handledData[0].toString()
								 * + handledData[1].toString() +
								 * handledData[2].toString());
								 */

								// writing this entry of handledData into the
								// output file
								for (int j = 0; j < handledData.length; j++) {
									writer.append(handledData[j].toString());
									if (j < handledData.length - 1)
										writer.append(",");
								}
								writer.append("\r\n");
								writer.flush();
								break;
							}
						}
						if (i == dataList.length - 1) {
							writer.flush();
							System.out.println(" - OK");
						}
					} catch (IOException e) {
						System.out
								.println("Error: Reading, Parsing, Computing");
					}

				}
		}
	}

	public void gettingCurrentAtSpecifiedVoltage(String targetColumn,
			String targetVoltage) throws FileNotFoundException, IOException {
		String[] originalData = new String[4];
		Double extractedDrainCurrent;

		// Create a new folder
		File temp = new File(this.directory + "\\"
				+ "Drain Current at Specified Voltage");
		temp.mkdir();

		// Set output txt filename
		String outputTxt = this.directory
				+ "\\"
				+ "Drain Current at Specified Voltage"
				+ "\\"
				+ this.directory.toString().substring(
						this.directory.toString().lastIndexOf("\\") + 1) + "-"
				+ targetColumn + ".txt";

		System.out.println(outputTxt);

		// Create a new file of handled data
		FileWriter writer = new FileWriter(outputTxt);

		// First line of file
		writer.append("File" + "\tVG\t" + targetColumn + "\r\n");

		// Choose a file
		int fileZeroIndex = 0;
		for (int i = 0; i < dataList.length; i++) {
			if (dataList[i].isFile())
				if (dataList[i].toString()
						.substring(dataList[i].toString().lastIndexOf("."))
						.indexOf("csv") != -1) {
					fileZeroIndex = i;
					break;
				}
		}

		for (int i = 0; i < dataList.length; i++) {
			if (dataList[i].isFile())
				if (dataList[i].toString()
						.substring(dataList[i].toString().lastIndexOf("."))
						.indexOf("csv") != -1) {
					try {

						// Extract the IDs from each file (?)
						BufferedReader br = new BufferedReader(new FileReader(
								dataList[i].toString()));

						System.out.println(dataList[i].toString());

						String aLineOfFile;
						while ((aLineOfFile = br.readLine()) != null) {

							System.out.println(aLineOfFile);

							if (aLineOfFile.indexOf("DataValue") != -1) {
								originalData = aLineOfFile.substring(
										aLineOfFile.indexOf(',') + 2)
										.split(",");

								if (originalData[0].equals(targetVoltage)) {
									extractedDrainCurrent = (targetColumn == "ID1") ? Double
											.parseDouble(originalData[2])
											: Double.parseDouble(originalData[3]);

									/*
									 * System.out.println(handledData[0].toString
									 * () + handledData[1].toString() +
									 * handledData[2].toString());
									 */

									// writing this entry of handledData into
									// the
									// output file
									writer.append(dataList[i]
											.toString()
											.substring(
													dataList[i]
															.toString()
															.indexOf(
																	"Id-Vg @5V [") + 11,
													dataList[i].toString()
															.lastIndexOf(")") + 1)
											+ "\t"
											+ targetVoltage
											+ "\t"
											+ extractedDrainCurrent.toString()
											+ "\r\n");
									writer.flush();
									break;
									// ... and end the parsing
								}
							}
						}
						if (i == dataList.length - 1) {
							writer.flush();
							System.out.println(" - OK");
						}
					} catch (IOException e) {
						System.out
								.println("Error: Reading, Parsing, Computing");
					}

				}
		}
	}
	/*
	 * public void handleOneRealTimeMeasurement(String fileName, Boolean
	 * includeBackwardScan) throws FileNotFoundException, IOException {
	 * 
	 * }
	 */
}