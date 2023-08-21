package org.example.part4;

import java.util.Optional;

public class Idol {
    private Leader leader;

    private String name;

    public Idol(String name) {

        this.name = name;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<Leader> getLeader() {
        return Optional.ofNullable(leader);
    }
}
