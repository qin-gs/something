package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 12 * 24; i++) {
            System.out.println(random.nextInt(100));
        }
    }
}
