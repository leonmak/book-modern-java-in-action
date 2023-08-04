package org.example.part2;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Member> memberList = Arrays.asList(
                new Member("karina", Member.Team.AESPA, true, 23)
                , new Member("winter", Member.Team.AESPA, true, 23)
                , new Member("gisele", Member.Team.AESPA, false, 20)
                , new Member("ningning", Member.Team.AESPA, false, 18)
                , new Member("irene", Member.Team.RED_VELVET, true, 28)
                , new Member("seulgi", Member.Team.RED_VELVET, true, 27)
                , new Member("wendy", Member.Team.RED_VELVET, true, 27)
                , new Member("joy", Member.Team.RED_VELVET, true, 25)
                , new Member("yeri", Member.Team.RED_VELVET, true, 22)
                , new Member("hani", Member.Team.NEW_JEANS, false, 20)
                , new Member("hyerin", Member.Team.NEW_JEANS, false, 18)
                , new Member("minzi", Member.Team.NEW_JEANS, false, 20)
        );

        System.out.println("memberList = " + memberList);
    }
}
