package com.qin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("q", "ww", "eee", "rrrrr");
        list.forEach(x -> {
            System.out.println(x.length());
        });
    }
}
