package org.example.part2;

import java.util.Arrays;
import java.util.List;

public class Main5 {

    public static void main(String[] args) {

        List<String> members = Arrays.asList("KARINA", "WINTER", "GISELLE", "NINGNING", "KARINA");

        members.stream().distinct().forEach(member -> System.out.println("member = " + member));

    }
}
