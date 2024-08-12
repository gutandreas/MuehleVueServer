package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "grou_id", nullable = false)
    private Long grouId;

    @OneToOne
    @JoinColumn(name = "admin_player_uuid")
    private Player admin;
    @OneToMany
    private Set<Player> members = new HashSet<>();

    public Long getGrouId() {
        return grouId;
    }

    public void setGrouId(Long grouId) {
        this.grouId = grouId;
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
