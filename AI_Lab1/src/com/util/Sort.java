/**
 * 
 */
package com.util;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.stream.ImageInputStream;

/**
 * @author duocai
 *  
 */
public class Sort {
	private static final String inputPath = "dataset_image/validation/validation.txt";
	private static final String outputPath = "dataset_image/validation/validation2.txt";
	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		int outputSize = 8;
		List<List<String>> list = new CopyOnWriteArrayList<>();
		for (int i = 0; i < outputSize; i++) {
			list.add(new ArrayList<>());
		}
		
		//get input
		File input = new File(inputPath);
		BufferedReader fReader = new BufferedReader(new FileReader(input));
		while (fReader.ready()) {
			String string = fReader.readLine();
			char type = string.charAt(string.length()-1);
			//System.out.println(type);
			int id = 7-('H'-type);
			list.get(id).add(string);
		}
		fReader.close();
		
		//output
		File output = new File(outputPath);
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(output));
		while (true) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).isEmpty()) {
					list.remove(i);
				}
				else {
					String str = list.get(i).get(0);
					char type = str.charAt(str.length()-1);
					//System.out.println(type);
					int id = 7-('H'-type);
					bWriter.write(str.substring(0, str.length()-1)+id+"\r\n");
					list.get(i).remove(0);
				}
			}
			if (list.isEmpty())
				break;
		}
		bWriter.close();
		System.out.println("成功转换输出");
	}

}
