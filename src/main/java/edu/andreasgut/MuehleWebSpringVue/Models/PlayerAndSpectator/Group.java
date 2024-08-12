package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @OneToOne
    @JoinColumn(name = "admin_player_uuid")
    private Player admin;
    @OneToMany
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
