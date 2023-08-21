package org.example.part4;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
