package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;

public class GameDto {
    PairingDto pairingDto;
    BoardDto boardDto;

    public GameDto(PairingDto pairingDto, BoardDto boardDto) {
        this.pairingDto = pairingDto;
        this.boardDto = boardDto;
    }

    public PairingDto getPairingDto() {
        return pairingDto;
    }

    public BoardDto getBoardDto() {
        return boardDto;
    }
}
