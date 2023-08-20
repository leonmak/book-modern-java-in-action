package org.example.part3;

import org.example.part2.Member;
import org.example.part3.order.*;
import org.example.part3.order.mix.MixedBuilder;
import org.example.part3.order.mr.Tax;
import org.example.part3.order.mr.TaxCalculator;
import org.example.part3.order.withlamda.LambdaOrderBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.groupingBy;
import static org.example.part3.order.NestedFunctionOrderBuilder.*;

public class Main10 {

    public static void main(String[] args) {
//        ch101();
        // ch103();
        ch104();
    }

    private static void ch104() {
        List<Member> memberList = Arrays.asList(new Member("karina", Member.Team.AESPA, true, 23)
                , new Member("winter", Member.Team.AESPA, true, 23)
                , new Member("gisele", Member.Team.AESPA, false, 20)
                , new Member("ningning", Member.Team.AESPA, false, 18)
                , new Member("REI", Member.Team.IVE, true, 19)
                , new Member("AN YUJIN", Member.Team.IVE, true, 19)
                , new Member("hani", Member.Team.NEW_JEANS, false, 20)
                , new Member("hyerin", Member.Team.NEW_JEANS, false, 18)
                , new Member("minzi", Member.Team.NEW_JEANS, false, 20));

        memberList.stream().collect(groupingBy(Member::getTeam)).forEach((team, members) -> {
            members.forEach(m ->{
                System.out.println("team : " + team + ", member : " + m.getName());
            });
        });
    }

    private static void ch103() {
        Order orderJYP = new Order();
        orderJYP.setCustomer("BigBank");

        Trade trade1 = new Trade();
        trade1.setType(Trade.Type.BUY);

        Stock stockJYP = new Stock();
        stockJYP.setSymbol("JYP");
        stockJYP.setMarket("코스피");

        trade1.setStock(stockJYP);
        trade1.setPrice(125.00);
        trade1.setQuantity(80);
        orderJYP.addTrade(trade1);

        Trade trade2 = new Trade();
        trade2.setType(Trade.Type.BUY);

        Stock stockGOOGLE = new Stock();
        stockGOOGLE.setSymbol("GOOGLE");
        stockGOOGLE.setMarket("NASDAQ");

        trade2.setStock(stockGOOGLE);
        trade2.setPrice(375.00);
        trade2.setQuantity(50);
        orderJYP.addTrade(trade2);

        Order orderKAKAOAndSM = MethodChainingOrderBuilder.forCustomer("BigBank")
                .buy(80)
                .stock("KAKAO")
                .on("코스피")
                .at(125.00)
                .sell(50)
                .stock("SM")
                .on("코스피")
                .at(375.00)
                .end();

        Order orderSMAndJYP = NestedFunctionOrderBuilder.order("BigBank",
                buy(80,
                        stock("SM", "코스피"),
                        at(375.00)),
                sell(50,
                        stock("JYP", "코스피"),
                        at(125.00)));

        Order orderSMAndJYPLamda = LambdaOrderBuilder.order(o -> {
            o.forCustomer("BigBank");
            o.buy(t -> {
                t.quantity(80);
                t.price(375.00);
                t.stock(s -> {
                    s.symbol("SM");
                    s.market("코스피");
                });
            });
            o.sell(t -> {
                t.quantity(50);
                t.price(125.00);
                t.stock(s -> {
                    s.symbol("JYP");
                    s.market("코스피");
                });
            });
        });

        Order order = MixedBuilder.forCustomer("BigBank", // nested function : customer 생성
                MixedBuilder.buy(t -> t.quantity(80) // lambda expression : trade 생성
                        .stock("SM")
                        .on("코스피")
                        .at(375.00)), // method chaining : trade 필드 설정
                MixedBuilder.sell(t -> t.quantity(50)
                        .stock("JYP")
                        .on("코스피")
                        .at(125.00)));

        double value = new TaxCalculator().with(Tax::regional)
                .with(Tax::surcharge)
                .calculate(order);

        System.out.println("value = " + value);
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
