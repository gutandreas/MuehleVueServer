package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import java.util.UUID;

public class Spectator {


    String spectatorId;
    String name;
    boolean isRoboter;

    public Spectator(String name, boolean isRoboter) {
        this.spectatorId = UUID.randomUUID().toString();
        this.name = name;
        this.isRoboter = isRoboter;
    }

    public String getSpectatorId() {
        return spectatorId;
    }

    public String getName() {
        return name;
    }

    public boolean isRoboter() {
        return isRoboter;
    }
}
