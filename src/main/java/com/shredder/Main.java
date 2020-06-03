package com.shredder;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

        //For demoing OpenNLP lemmatization
        OpenNLP openNlp = new OpenNLP();

        Map<String, String> map = openNlp.getLemmatizedMap("selling items");
        map.values().forEach(System.out::println);
    }
}
