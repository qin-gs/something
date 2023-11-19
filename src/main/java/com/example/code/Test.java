package com.example.code;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Test {
	public static void main(String[] args) {
		Integer len = 8;
		Executor executor = Executors.newFixedThreadPool(len);

		for (Integer i = 0; i < len; i++) {
			executor.execute(() -> {
				while (true) {

				}
			});
		}
	}

}
