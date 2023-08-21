package org.example.part4;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Main11 {

    public static void main(String[] args) {

        ch11();
    }

    private static void ch11() {
        Optional<Leader> optMember = Optional.empty();

        Leader karina = new Leader("Karina");
        Optional<Leader> member = Optional.ofNullable(karina);
        Optional<String> name = member.map(Leader::getName);

        Idol aespa = new Idol("aespa");
        Optional<Idol> optIdol = Optional.ofNullable(aespa);
//        Optional<String> aespaLeaderCarName = optIdol.map(Idol::getLeader)
//                .map(Leader::getCar) // compile error : reason: no instance(s) of type variable(s) exist so that Optional<Leader> conforms to Leader
//                .map(Car::getName);

//        Optional<String> aespaLeaderCarName = optIdol.flatMap(Idol::getLeader)
//                .flatMap(Leader::getCar)
//                .map(Car::getName);
//
//        System.out.println(aespaLeaderCarName);
    }


    @Test
    public void test1() {

        Properties props = new Properties();
        props.setProperty("aespa", "is my life");
        props.setProperty("karina age", "23");
        props.setProperty("karina height", "170");
        props.setProperty("karina is leader", "true");
        props.setProperty("karina is men", "-1"); // -1 : false

        assertEquals(0, readValueOnlyNumber(props, "aespa"));
        assertEquals(23, readValueOnlyNumber(props, "karina age"));
        assertEquals(170, readValueOnlyNumber(props, "karina height"));
        assertEquals(0, readValueOnlyNumber(props, "karina is leader"));
        assertEquals(0, readValueOnlyNumber(props, "karina is men"));

    }


    // property의 값이 양의 정수 일 경우에만 반환, 아닐 경우 0 반환
    public int readValueOnlyNumber(Properties props, String name) {
//        String value = props.getProperty(name);
//        if(value != null){
//            try{
//                int i = Integer.parseInt(value);
//                if(i > 0){
//                    return i;
//                }
//            }catch(NumberFormatException e){
//                // do nothing
//            }
//        }
//        return 0;

        return Optional.ofNullable(props.getProperty(name)) // Optional<String>
                .flatMap(strVal -> strToInt(strVal)) // Optional<Integer>
                .filter(intVal -> intVal > 0)
                .orElse(0);

    }

    public static Optional<Integer> strToInt(String str) {
        try {
            return Optional.of(Integer.parseInt(str)); // return Optional<Integer>
        } catch (NumberFormatException e) {
            return Optional.empty(); // return Optional.empty()
        }
    }
    public Set<String> getLeadersCarName(List<Idol> idolList) {

        return idolList.stream()
                .map(Idol::getLeader) // Stream<Optional<Leader>>
                .map(optLeader -> optLeader.flatMap(Leader::getCar)) // Stream<Optional<Car>>
                .map(optCar -> optCar.map(Car::getName)) // Stream<Optional<String>>
//                .flatMap(Optional::stream) // Stream<Optional<String>> -> Stream<String>
//                .collect(Collectors.toSet()); // Stream<String> -> Set<String>
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public Optional<Car> findBiggestCarNullsafe(Optional<Idol> idol, Optional<Leader> leader) {
//        if (idol.isPresent() && leader.isPresent()) {
//            return Optional.of(findBiggestCar(idol.get(), leader.get()));
//        } else {
//            return Optional.empty();
//        }

        return idol.flatMap(i -> leader.map(l -> findBiggestCar(i, l)));
    }

    public Car findBiggestCar(Idol idol, Leader leader) {
        // buisness logic : find biggest car
        Car biggiesCar = null;
        return biggiesCar;

    }
}
