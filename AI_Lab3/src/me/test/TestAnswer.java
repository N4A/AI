package me.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import me.genetic.Individual;
import me.tsp.BasicTSP;

public class TestAnswer {

	public static void main(String[] args) {
		BasicTSP tsp = new BasicTSP();
		tsp.init(new File("testtsp/test"));
		Integer[] answer = readAnswer(new File("testtsp/answer"));
		System.out.println(1/tsp.fitness(new Individual<>(answer)));
	}

	private static Integer[] readAnswer(File file) {
		BufferedReader reader;
		Integer[] answer = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			int len = Integer.parseInt(reader.readLine());
			answer = new Integer[len];
			for (int i = 0; i < answer.length; i++) {
				answer[i] = Integer.parseInt(reader.readLine())-1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

}
