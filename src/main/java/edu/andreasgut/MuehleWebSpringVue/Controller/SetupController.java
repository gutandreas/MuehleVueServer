package edu.andreasgut.MuehleWebSpringVue.Controller;

import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Services.GameServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class SetupController {


    @PostMapping(
            path = "/setup",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Game> setup(@RequestBody Map<String, Object> jsonRequest, HttpServletRequest request) {

        String path = request.getRequestURI();
        System.out.println("Neuer Request an " + path);
        jsonRequest.forEach((key, value) -> System.out.println(key + ": " + value));

        if (jsonRequest.get("modus").equals("c")){
            Game game = setupComputerGame(jsonRequest);
            System.out.println("Computerspiel gestartet");
            return ResponseEntity.status(HttpStatus.OK).body(game);
        }


        return ResponseEntity.status(HttpStatus.OK).body(null);







    }

    private Game setupComputerGame(Map<String, Object> jsonRequest){
        String gameCode = jsonRequest.get("gameCode").toString();
        STONECOLOR playerStonecolor = jsonRequest.get("color").toString().equals("BLACK") ? STONECOLOR.BLACK : STONECOLOR.WHITE;

        int level = Integer.parseInt(jsonRequest.get("level").toString());
        String computerName;
        if (level == 0){
            computerName = "Schwacher Computer";
        } else if (level == 1) {
            computerName = "Mittelstarker Computer";
        } else if (level == 2) {
            computerName = "Starker Computer";
        } else {
            computerName = "Computer (ungültiges Level)";
        }
        STONECOLOR computerStonecolor = playerStonecolor == STONECOLOR.BLACK ? STONECOLOR.WHITE : STONECOLOR.BLACK;

        Player humanPlayer = new HumanPlayer((String) jsonRequest.get("name"), playerStonecolor);
        Player computerPlayer = new StandardComputerPlayer(computerName, computerStonecolor, level);
        Pairing pairing = new Pairing(humanPlayer, computerPlayer, 1);

        return new Game(gameCode, new Board(), pairing, 0);
    }




}
