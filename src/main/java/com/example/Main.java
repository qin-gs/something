package com.example;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("q", "ww", "eee", "rrrrr");
        list.forEach(x -> {
            System.out.println(x.length());
        });
    }
}
