package org.example.part2;

public class Member {

    private final String name;

    private final Team team;
    private final boolean isDebut;

    private final int age;

    private Nation nation;

    public Member() {
        this.name = "unknown";
        this.isDebut = false;
        this.team = null;
        this.age = 0;
        this.nation = null;
    }

    public Member(String name, Team team, boolean isDebut, int age) {
        this.name = name;
        this.isDebut = isDebut;
        this.team = team;
        this.age = age;
    }

    public Member(String name, Team team, boolean isDebut, int age, Nation nation) {
        this.name = name;
        this.isDebut = isDebut;
        this.team = team;
        this.age = age;
        this.nation = nation;
    }

    public String getName() {
        return name;
    }

    public boolean getIsDebut() {
        return isDebut;
    }

    public boolean checkIsNotChild() {
        return age >= 10;
    }

    public boolean checkIsAdult() {
        return age >= 20;
    }

    public Team getTeam() {
        return team;
    }

    public int getAge() {
        return age;
    }

    public Boolean unknownTeam() {
        return this.team == null;
    }

    public Boolean isKorean() {
        return this.nation == Nation.KOREAN;
    }

    public boolean isDebut() {
        return isDebut;
    }

    public Nation getNation() {
        return nation;
    }

    public enum Team {
        NEW_JEANS, AESPA, RED_VELVET, IVE

    }

    public enum AgeLevel {
        CHILD, ADULT, SENIOR
    }

    public enum Nation {
        KOREAN, CHINESE, AMERICAN, AUSTRAILIAN
    }


    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", team=" + team +
                ", isDebut=" + isDebut +
                ", age=" + age +
                ", nation=" + nation +
                '}';
    }
}

