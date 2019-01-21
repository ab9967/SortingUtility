package com.sortingUtility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SortFeature {

	private static String featureFilePath = "C:\\Users\\Aniket\\Documents\\TestingSort\\MergeUtility\\src\\com\\data\\F77777";
	private static String  devFilePath = "C:\\Users\\Aniket\\Documents\\TestingSort\\MergeUtility\\src\\com\\data\\develop_branch";
	private static List<Integer> duplicateDevDataindex = new ArrayList<Integer> ();
	private static List<Integer> duplicateFeatDataindex = new ArrayList<Integer> ();
	private static List<Integer> exclusiveDevDataindex = new ArrayList<Integer> ();
	private static List<String> exclusiveDevData = new ArrayList<String> ();
	//Data impacted in Feature File Due to Duplicate Data or blank spaces present in Develop Data File
	//Will ab added an empty line at such lines in resulting Feature File
	private static List<String> impactedDataFromFeature = new ArrayList<String> ();
	private static List<String> curatedFeatureDataForDirtyDevData = new ArrayList<String> ();
	public static void main(String[] args) 
	{
		String[] featureDataArray, developDataArray; 
		
		//reading feature file data
		System.out.println("FEAT READ");
		featureDataArray = readData(featureFilePath);
		//printData(featureDataArray);
		//System.out.println("################################################");
		//Validate Develop File Data for Duplication or Blank Line
		String[] duplicateFeatureDataArray = duplicateDataCheck(featureDataArray);
		printData(duplicateFeatureDataArray);
		String[] featureFilteredDataArray = removeDupsAndWhitespaces(duplicateFeatureDataArray,featureDataArray);
		System.out.println("Feature Filtered Data");
		printData(featureFilteredDataArray);
		//reading develop file data
		System.out.println("DEV READ");
		developDataArray = readData(devFilePath);
		//printData(developDataArray);
		//System.out.println("################################################");
		//Validate Develop File Data for Duplication or Blank Line
		String[] duplicateDevDataArray = duplicateDataCheck(developDataArray);
		String[] devFilteredDataArray = removeDupsAndWhitespaces(duplicateDevDataArray,developDataArray);
		System.out.println("Dev Filtered Data");
		printData(devFilteredDataArray);
		/**
		 * Compare devData with feature and bring common data at beginning of both files
		 * Make featureData ending with exclusive data
		 * Print Exclusive Data if any found in DevData
		 */
		boolean operFlag = compareAndSort(featureFilteredDataArray,devFilteredDataArray);
		
		if(operFlag==true)
		{
			System.out.println("###########################################################");
			System.out.println("*****Feature File Successfully Sorted wrt Develop File*****");
			
		}
	}
	
	private static String[] removeDupsAndWhitespaces(String[] duplicateDataArray, String[] dataArray) {
		List<String> tempData = new ArrayList<String>();
		System.out.println("###########################################################");
		boolean flag;
		for(int j=0;j<dataArray.length;j++)
		{
			flag = false;
			if(dataArray[j].trim().isEmpty())
			{
				continue;
			}
			for(int i=0;i<duplicateDataArray.length;i++)
			{
				if((dataArray[j].equals(duplicateDataArray[i])))
				{
					flag = true;
				}
			}
			if(!flag)
			{
				tempData.add(dataArray[j]);
			}
		}
		for(String tmp : duplicateDataArray)
		{
			tempData.add(tmp);
		}
		
		return tempData.toArray(new String[0]);
	}

	private static String[] duplicateDataCheck(String[] dataArray) {
		List<String> duplicateData = new ArrayList<String>();
		for(int i=0;i<dataArray.length;i++)
		{
			for(int j=i+1;j<dataArray.length;j++)
			{
				if(dataArray[i].equals(dataArray[j])&&(i!=j)&&(!dataArray[i].trim().isEmpty()))
				{
					if(isDuplicateDataAlreadyLoaded(duplicateData,dataArray[i]))
					{	
					duplicateData.add(dataArray[i]);
					//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					//System.out.println("Found Duplicate Data at Line Number "+(i+1)+" & "+(j+1));
					//System.out.println("Dev Data : "+dataArray[i]);
					duplicateDevDataindex.add(j);
					}
				}
			}
		}
		return duplicateData.toArray(new String[0]);
	}
private static boolean isDuplicateDataAlreadyLoaded(List<String> duplicateData, String temp) {
		for(String tmp : duplicateData)
		{
			if(temp.equals(tmp))
			{
				return false;
			}
		}
		return true;
	}

//	private static String[] duplicateFeatDataCheck(String[] dataArray) {
//		List<String> duplicateData = new ArrayList<String>();
//		for(int i=0;i<dataArray.length;i++)
//		{
//			for(int j=i+1;j<dataArray.length;j++)
//			{
//				if(dataArray[i].equals(dataArray[j])&&(i!=j)&&(!dataArray[i].trim().isEmpty()))
//				{
//					duplicateData.add(dataArray[i]);
//					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//					System.out.println("Found Duplicate Data at Line Number "+(i+1)+" & "+(j+1));
//					System.out.println("Feature Data : "+dataArray[i]);
//					duplicateFeatDataindex.add(j);					
//				}
//			}
//		}
//		return duplicateData.toArray(new String[0]);
//	}
	
	private static boolean compareAndSort(String[] featureDataArray, String[] developDataArray)
	{
		int lineNumInFeature=0;
		for(int i=0;i<developDataArray.length;i++)
		{
//			if(duplicateDevDataindex.contains(i))
//			{
//				impactedDataFromFeature.add(featureDataArray[i]);
//				featureDataArray[i]="	";
//				continue;
//			}
			lineNumInFeature = getLineNumberOfDevDataFromFeature(featureDataArray,developDataArray[i]);
			//System.out.println("To be Index: "+i);
			if(lineNumInFeature == 999999)
			{
				System.out.println(developDataArray[i]);
			} else
			{
				//swap data in feature file
				featureDataArray = swapLinesInFeature(featureDataArray,i,lineNumInFeature);
			}			
		}
//		if(!duplicateDevDataindex.isEmpty())
//		{	
//			addImpactedDataToFeatureFile(featureDataArray);
//			printData(curatedFeatureDataForDirtyDevData.toArray(new String[0]));
//			boolean flag = writeToFile(curatedFeatureDataForDirtyDevData.toArray(new String[0]));
//			System.out.println("*****************************Sorted Data Wrote in Feature File*******************************");
//			featureDataArray = readData(featureFilePath);
//			printData(featureDataArray);
//			
//			return flag;
//		}
		
		
		
		//System.out.println("*****************************Dev Array Data*******************************");
		//printData(developDataArray);
		writeToFile(developDataArray,devFilePath);
		//System.out.println("*****************************Dev Data End*******************************");
		
		//System.out.println("*****************************Feature Array Data Sorted WRT Dev*******************************");
		//printData(featureDataArray);
		//System.out.println("*****************************Feature Data End*******************************");
		
		//write sorted data to feature File
		
		boolean flag = writeToFile(featureDataArray,featureFilePath);
		//System.out.println("*****************************Sorted Data Wrote in Feature File*******************************");
		//featureDataArray = readData(featureFilePath);
		//printData(featureDataArray);
		
		return flag;
	}
//	private static void addImpactedDataToFeatureFile(String[] featureDataArray) {
//		
//		for(int i=0;i<featureDataArray.length;i++)
//		{
//			curatedFeatureDataForDirtyDevData.add(featureDataArray[i]);
//		}		
//		
//		for( String tempData : impactedDataFromFeature )
//		{
//			curatedFeatureDataForDirtyDevData.add(tempData);
//		}
//	}
	private static boolean writeToFile(String[] dataArray, String path) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			
			for(int i=0;i<dataArray.length;i++)
			{
				writer.write(dataArray[i]);
				writer.newLine();
			}
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			System.out.println("Error : "+e);
		}
		return false;
	}
	private static String[] swapLinesInFeature(String[] featureDataArray, int i, int lineNumInFeature) {
		String temp = featureDataArray[i];
		featureDataArray[i] = featureDataArray[lineNumInFeature];
		featureDataArray[lineNumInFeature] = temp;
		return featureDataArray;
	}
	private static int getLineNumberOfDevDataFromFeature(String[] featureDataArray, String devData) {
		for(int i=0;i<featureDataArray.length;i++)
		{
			if(devData.equals(featureDataArray[i]))
			{
				return i;
			}
		}
		return 999999;
	}
	private static void printData(String[] dataArray)
	{
		for(int i=0;i<dataArray.length;i++)
		{
			System.out.println(dataArray[i]);
		}
	}
	
	private static String[] readData(String path)
	{
		String temp;
		List<String> list = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while((temp = reader.readLine())!=null)
			{
				list.add(temp);
			}
			reader.close();
			return list.toArray(new String[0]);
						
		} catch (FileNotFoundException e) {
			System.out.println("Error : "+e);
		} catch (IOException e) {
			System.out.println("Error : "+e);
		}
		return null;
	}
}
