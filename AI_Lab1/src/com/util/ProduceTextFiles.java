package com.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

/**
 * 将所有训练集输出到一个文件中，每张图片写为28*28的一行，最后一个字符表示该子母
 * @author duocai
 *
 */
public class ProduceTextFiles {
	
	private OutputStreamWriter osw;
	
	public static void main(String[] args) throws Exception {
		ProduceTextFiles produceTextFiles = new ProduceTextFiles();
		produceTextFiles.initOutputPath();
		
		for (int i = 0; i < 8; i++) {
			String inputDirectoryPath = "dataset_image/train/" + (char)('A'+i);
			produceTextFiles.writeGrey(inputDirectoryPath);
			System.out.println((char)('A'+i)+"已成功输出。");
		}
		produceTextFiles.osw.close();
	}
	
	/**
	 * 初始化训练集输出路径
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	private  void initOutputPath() throws UnsupportedEncodingException, FileNotFoundException {
		String outputFilePath = "dataset_image/train/train.txt";
		File outputFile = new File(outputFilePath);
		FileOutputStream fos = new FileOutputStream(outputFile);
		osw = new OutputStreamWriter(fos, "UTF-8");
	}

	// 该方法读入一个文件夹，文件夹名为字母，对文件夹中的每个图片文件转成灰度值写入文本文件
	public void writeGrey(String directoryPath) throws Exception {
		File directory = new File(directoryPath);
		File[] imageFiles = directory.listFiles();
		for (File imageFile : imageFiles) {
			writeGrey(imageFile, directoryPath);
		}
	}

	// 文件名后缀为.png，该方法读入图片文件，将灰度值写入文本文件
	public void writeGrey(File file, String directoryPath) throws Exception {
		String letter = directoryPath.charAt(directoryPath.length() - 1) + "";
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
//			line = line.trim();
//			osw.write(line + "\r\n");
			osw.write(line);
		}
		osw.write(letter + "\r\n");
	}
}