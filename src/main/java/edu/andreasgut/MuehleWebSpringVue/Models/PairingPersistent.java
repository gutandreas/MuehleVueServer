package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.PlayerPersistent;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class PairingPersistent extends Pairing{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID pairingUuid;

    @OneToOne(cascade = CascadeType.ALL)
    private PlayerPersistent player1;

    @OneToOne(cascade = CascadeType.ALL)
    private PlayerPersistent player2;

    public PairingPersistent(Player player1, Player player2, int startPlayerIndex) {
        super(player1, player2, startPlayerIndex);
    }

    public PairingPersistent(Player player1, int startPlayerIndex) {
        super(player1, startPlayerIndex);
    }

    public PairingPersistent() {
    }
}
