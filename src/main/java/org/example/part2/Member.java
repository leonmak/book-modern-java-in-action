package org.example.part2;

public class Member {

    private final String name;

    private final Team team;
    private final boolean isDebut;

    private final int age;

    public Member() {
        this.name = "unknown";
        this.isDebut = false;
        this.team = null;
        this.age = 0;
    }

    public Member(String name, Team team, boolean isDebut, int age) {
        this.name = name;
        this.isDebut = isDebut;
        this.team = team;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public boolean isDebut() {
        return isDebut;
    }

    public Team getTeam() {
        return team;
    }

    public int getAge() {
        return age;
    }

    public enum Team {
        NEW_JEANS, AESPA, RED_VELVET, IVE

    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", isDebut=" + isDebut +
                ", team=" + team +
                ", age=" + age +
                '}';
    }
}

