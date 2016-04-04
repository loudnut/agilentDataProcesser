package commandLineStep;

import java.io.File;

public class LookDataList {

	public static void main(String args[]){
		File obj = new File("D:/eclipse/workspace/dataAnalysis/obj");
		
		String[] dataList = obj.list();
		
		for(String element : dataList)
			System.out.println(element.substring(0, element.lastIndexOf('.')));
			

	}
}
