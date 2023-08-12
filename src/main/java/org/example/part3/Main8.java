package org.example.part3;

import java.util.*;

public class Main8 {
    public static void main(String[] args) {
        ch81();
    }

    private static void ch81() {
        List<String> aespa = Arrays.asList("Karina", "Giselle", "Winter", "Ningning");
        aespa.set(0, "KARINA01");
        //aespa.add("새로운 멤버"); // UnsupportedOperationException

        Set<String> ive = new HashSet<>(Arrays.asList("Yujin", "Wonyoung"));
        ive.add("Yena"); // mutalbe

        aespa = null;
        aespa = List.of("Karina", "Giselle", "Winter", "Ningning");
        // aespa.set(0, "KARINA02"); // UnsupportedOperationException

        Set<String> aespaSet = Set.of("Karina", "Winter", "Giselle", "Ningning");
        // aespaSet.add("새로운 멤버"); // UnsupportedOperationException

        aespaSet = null;
//        aespaSet = Set.of("Karina", "Winter", "Giselle", "Ningning", "Karina");// IllegalArgumentException

        Map<String, Integer> aespaAge1 = Map.of("Karina", 21, "Winter", 21, "Giselle", 20, "Ningning", 18);

        Map<String, Integer> aespaAge2 = Map.ofEntries(
                Map.entry("Karina", 21),
                Map.entry("Winter", 21),
                Map.entry("Giselle", 20),
                Map.entry("Ningning", 18)
        );

//         aespaAge1.put("새로운 멤버", 0); // UnsupportedOperationException
//        aespaAge1.put("Karina", 22); // UnsupportedOperationException
    }

}
