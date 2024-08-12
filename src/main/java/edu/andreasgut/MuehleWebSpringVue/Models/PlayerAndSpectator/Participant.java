package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public abstract class Participant {


    @Id
    private String pariticipantUuid;
    private String name;

    public Participant(String name) {
        this.name = name;
        this.pariticipantUuid = UUID.randomUUID().toString();
    }

    public Participant() {

    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return pariticipantUuid;
    }
}
