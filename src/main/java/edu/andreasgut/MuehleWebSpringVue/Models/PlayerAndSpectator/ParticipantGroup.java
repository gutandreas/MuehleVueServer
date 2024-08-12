package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


public class ParticipantGroup {

    private Long groupId;


    private Player admin;


    private Set<Player> members = new HashSet<>();

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long grouId) {
        this.groupId = grouId;
    }

    public Player getAdmin() {
        return admin;
    }

    public void setAdmin(Player admin) {
        this.admin = admin;
    }

    public boolean addPlayer(Player player){
        return members.add(player);
    }

    public boolean removePlayer(Player player){
        return members.remove(player);
    }

}
