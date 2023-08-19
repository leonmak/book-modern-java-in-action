package org.example.part3;

import org.example.part2.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Main10 {

    public static void main(String[] args) {
        ch101();
    }

    private static void ch101() {
        List<Member> memberList = Arrays.asList(new Member("karina", Member.Team.AESPA, true, 23)
                , new Member("winter", Member.Team.AESPA, true, 23)
                , new Member("gisele", Member.Team.AESPA, false, 20)
                , new Member("ningning", Member.Team.AESPA, false, 18)
                , new Member("REI", Member.Team.IVE, true, 19)
                , new Member("AN YUJIN", Member.Team.IVE, true, 19)
                , new Member("hani", Member.Team.NEW_JEANS, false, 20)
                , new Member("hyerin", Member.Team.NEW_JEANS, false, 18)
                , new Member("minzi", Member.Team.NEW_JEANS, false, 20));

        Map<Member.Team, Map<Member.AgeLevel, List<Member>>> memberByTeamAndAge1 = memberList.stream()
                .collect(groupingBy(Member::getTeam, groupingBy(Member::getAgeLevel)));
        System.out.println(memberByTeamAndAge1);

        // Collector nesting
        Collector<Member, ?, Map<Member.Team, Map<Member.AgeLevel, List<Member>>>> collectorNested1
                = groupingBy(Member::getTeam, groupingBy(Member::getAgeLevel));

        Map<Member.Team, Map<Member.AgeLevel, List<Member>>> memberByTeamAndAge2 = memberList.stream().collect(collectorNested1);
        System.out.println(memberByTeamAndAge2);

        Collector<? super Member, ?, Map<Member.Team, Map<Member.AgeLevel, List<Member>>>> collectorNested2
                = GroupingBuilder.groupOn(Member::getAgeLevel).after(Member::getTeam).get();

        Map<Member.Team, Map<Member.AgeLevel, List<Member>>> memberByTeamAndAge3 = memberList.stream().collect(collectorNested2);
        System.out.println(memberByTeamAndAge3);


    }

    private static class GroupingBuilder<T, D, K> {
        private final Collector<? super T, ?, Map<K, D>> collector;

        private GroupingBuilder(Collector<? super T, ?, Map<K, D>> collector) {
            this.collector = collector;
        }

        public Collector<? super T, ?, Map<K, D>> get() {
            return collector;
        }

        public <J> GroupingBuilder<T, Map<K, D>, J> after(Function<? super T, ? extends J> classifier) {
            return new GroupingBuilder<>(groupingBy(classifier, collector));
        }

        public static <T, D, K> GroupingBuilder<T, List<T>, K> groupOn(Function<? super T, ? extends K> classifier) {
            return new GroupingBuilder<>(groupingBy(classifier));
        }
    }
}
