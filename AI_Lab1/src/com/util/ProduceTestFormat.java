/**
 * 
 */
package com.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

/**
 * @author duocai
 * @time ����7:58:25
 * @date 2016��10��16��
 */
public class ProduceTestFormat {
	private OutputStreamWriter osw;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ProduceTestFormat pFormat = new ProduceTestFormat();
		pFormat.initOutputPath();
		String inPath = "testletter";
		pFormat.writeGrey(inPath);
		pFormat.osw.close();
		System.out.println("finished.");
	}
	
	/**
	 * ��ʼ��ѵ�������·��
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	private  void initOutputPath() throws UnsupportedEncodingException, FileNotFoundException {
		String outputFilePath = "testbp/letter.txt";
		File outputFile = new File(outputFilePath);
		FileOutputStream fos = new FileOutputStream(outputFile);
		osw = new OutputStreamWriter(fos, "UTF-8");
	}
	
	// �÷�������һ���ļ��У��ļ�����Ϊ��ĸ�����ļ����е�ÿ��ͼƬ�ļ�ת�ɻҶ�ֵд���ı��ļ�
	public void writeGrey(String directoryPath) throws Exception {
		File directory = new File(directoryPath);
		File[] imageFiles = directory.listFiles();
		for (File imageFile : imageFiles) {
			writeGrey(imageFile, directoryPath);
		}
	}
	
	// �ļ�����׺Ϊ.png���÷�������ͼƬ�ļ������Ҷ�ֵд���ı��ļ�
	public void writeGrey(File file, String directoryPath) throws Exception {
		String filePath = file.getPath();
		File imageFile = new File(filePath);
		BufferedImage bi = (BufferedImage) ImageIO.read(imageFile);
		int width = bi.getWidth();
		int height = bi.getHeight();
		for (int x = 0; x < width; x++) {
			String line = "";
			for (int y = 0; y < height; y++) {
				int pixel = bi.getRGB(x, y);
				int red = (pixel & 0xff0000) >> 16;
				int green = (pixel & 0xff00) >> 8;
				int blue = (pixel & 0xff);
				int grey = (red * 299 + green * 587 + blue * 114) / 1000;
				line += grey + " ";
			}
			osw.write(line);
		}
		osw.write("\r\n");
	}
}
