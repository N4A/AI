/**
 * 
 */
package me.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author duocai
 * @date 2016年12月29日
 * @time 下午2:48:43
 */
public class Analyze {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("outputs/tsp/sequence");
		File[] files = file.listFiles(new FileFilter() {		
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
		 
		BufferedWriter bWriter = null;
		try {
			bWriter = new BufferedWriter(
					new FileWriter(new File("outputs/tsp_analyze_sequence")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			int cp = 7;
			int mp = name.indexOf("mp")+2;
			int s = name.indexOf('s', 10)+1;
			String cpStr = name.substring(cp,mp-2);
			String mpStr = name.substring(mp,s-1);
			String sStr = name.substring(s,name.length()-4);
			
			try {
				BufferedReader bReader = new BufferedReader(new FileReader(files[i]));
				for (int j = 0; j < 59; j++) {//ignore 33 lines
					bReader.readLine();
				}
				String last = bReader.readLine();
				String bestI = "best: ";
				String averageI = "average: ";
				int bs = last.indexOf(bestI)+bestI.length();
				int as = last.indexOf(averageI)+averageI.length();
				String best = last.substring(bs, bs+6);
				String average = last.substring(as,as+6);
				
				String best1 = bReader.readLine();
				int maohao = best1.indexOf(':');
				String g = best1.substring(0, maohao);
				String fitness = best1.substring(maohao+1,maohao+7);
				
				bWriter.write("|"+(i+1)+"|"+cpStr+"|"+mpStr+"|"+sStr+"|"+g+"|"+fitness+"|"
						+ best+"/"+average+"|\n");
				
				bReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
