package org.example.part3;

import java.util.*;
import java.util.stream.Collectors;

public class Main8 {
    public static void main(String[] args) {
//        ch81();
//        ch82();
        ch83();
    }

    private static void ch83() {

        Map<String, Integer> aespaAge = Map.of("Karina", 21, "Winter", 21, "Giselle", 20, "Ningning", 18);
        // forEach
        for (Map.Entry<String, Integer> entry : aespaAge.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        // BiConsumer
        aespaAge.forEach((memberName, age) -> System.out.println(memberName + " : " + age));

        // Sorting
        aespaAge.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(System.out::println);

        // getOrDefault
//        System.out.println(aespaAge.get("hani").toString());
        System.out.println(aespaAge.getOrDefault("hani", -1).toString());

        Map<String, List<String>> idolWithMember = new HashMap<>();
        idolWithMember.put("NewJeans", List.of("Minzi", "Hani", "Haerin"));
        idolWithMember.put("RedVelvet", List.of("Irene", "Seulgi", "Wendy", "Joy", "Yeri"));

        String aespa = "Aespa";
//        List<String> aespaMemberList = idolWithMember.get(aespa);
//        if (aespaMemberList == null) {
//            aespaMemberList = new ArrayList<>();
//            idolWithMember.put(aespa, aespaMemberList);
//        }
//        System.out.println(idolWithMember.toString());
//
//        aespaMemberList.add("Karina");
//        aespaMemberList.add("Winter");
//        aespaMemberList.add("Giselle");
//        aespaMemberList.add("Ningning");
//
//        System.out.println(idolWithMember.toString());

        idolWithMember.computeIfAbsent("Aespa", groupName -> new ArrayList<>()).addAll(List.of("Karina", "Winter", "Giselle", "Ningning"));

        Map<String, String> idolWithLeader = new HashMap<>();
        idolWithLeader.put("NewJeans", "Minzi");
        idolWithLeader.put("RedVelvet", "Irene");
        idolWithLeader.put("Aespa", "Karina");

        String nameKarina = "Karina";
        String nameAespa = "Aespa";

//        if (idolWithLeader.containsKey(nameAespa)
//                && Objects.equals(idolWithLeader.get(nameAespa), nameKarina)) {
//            System.out.println("Aespa는 더 이상 리더 직급이 없습니다.");
//            idolWithLeader.remove(nameAespa);
//        }

        // Java 8
//        idolWithLeader.remove(nameAespa, nameKarina);

//        idolWithLeader.replaceAll((groupName, leaderName) -> leaderName.toUpperCase());
        System.out.println(idolWithLeader);


        Map<String, String> nickNameIdol = Map.ofEntries(
                Map.entry("유지민", "Karina"),
                Map.entry("애리", "GiSelle"),
                Map.entry("김민지", "민지")
        );

        Map<String, String> nickNameActor = Map.ofEntries(
                Map.entry("공지철", "공유"),
                Map.entry("유지민", "지민") // duplicate key
        );

        Map<String, String> everyOne1 = new HashMap<>(nickNameIdol);
        everyOne1.putAll(nickNameActor);
        System.out.println(everyOne1);

        // Java 8
        Map<String, String> everyOne2 = new HashMap<>(nickNameIdol);
        nickNameActor.forEach((name, age) -> everyOne2.merge(name, age, (nick1, nick2) -> nick1 + " & " + nick2));
        System.out.println(everyOne2);


        Map<String, Integer> memberAge = new HashMap<>();
        memberAge.put("Karina", 20);
        memberAge.put("Giselle", 20);
        memberAge.put("Winter", 19);

//        String nameKarina = "Karina";
        Integer age = memberAge.get(nameKarina);
        if (age == null) {
            memberAge.put(nameKarina, 20);
        } else {
            memberAge.put(nameKarina, age + 1);
        }

        // Java 8
        memberAge.merge(nameKarina, 1, (k, v) -> v + 1);
        System.out.println(memberAge);

        // quiz 8.2

        Map<String, Integer> movies = new HashMap<>();
        movies.put("JamesBond", 20);
        movies.put("Matrix", 15);
        movies.put("Harry Potter", 5);

        movies.entrySet().removeIf(movie -> movie.getValue() < 10);

    }

    private static void ch82() {

        List<String> memberList = new ArrayList<>();
        memberList.add("Karina");
        memberList.add("Giselle");
        memberList.add("Winter");
        memberList.add("Ningning");

        // ConcurrentModificationException
//        for (String memberName : memberList) {
//            if (memberName.equals("Karina")) {
//                memberList.remove(memberName);
//            }
//        }

//        for (Iterator<String> iterator = memberList.iterator();
//             iterator.hasNext(); ) {
//            String memberName = iterator.next();
//            if (memberName.equals("Karina")) {
//                memberList.remove("Karina");
//            }
//        }

        // iterator.remove()를 사용하면 ConcurrentModificationException이 발생하지 않는다.
        for (Iterator<String> iterator = memberList.iterator();
             iterator.hasNext(); ) {
            String memberName = iterator.next();
            if (memberName.equals("Karina")) {
                iterator.remove();
            }
        }

        // Java 8
        memberList.removeIf(memberName -> memberName.equals("Karina"));

        memberList = null;
        memberList = new ArrayList<>();
        memberList.add("Karina");
        memberList.add("Giselle");
        memberList.add("Winter");
        memberList.add("Ningning");
        memberList.stream().map(memberName -> "original : " + memberName + " | upper : " + memberName.toUpperCase())
                .collect(Collectors.toList())
                .forEach(System.out::println);

//        for (ListIterator<String> iterator = memberList.listIterator(); iterator.hasNext(); ) {
//            String memberName = iterator.next();
//            iterator.set("original : " + memberName + " | upper : " + memberName.toUpperCase());
//        }
//        System.out.println(memberList);

        // Java 8
        memberList.replaceAll(memberName -> "original : " + memberName + " | upper : " + memberName.toUpperCase());
        System.out.println(memberList);
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
