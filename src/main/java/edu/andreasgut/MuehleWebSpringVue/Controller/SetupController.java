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
@CrossOrigin(origins = "http://localhost:8080") // auf Klassenebene
public class SetupController {


    @PostMapping(
            path = "/setup",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Game> setup(@RequestBody Map<String, Object> jsonRequest, HttpServletRequest request) {

        String path = request.getRequestURI();
        System.out.println("Neuer Request an " + path);
        jsonRequest.forEach((key, value) -> System.out.println(key + ": " + value));

        Player player = new HumanPlayer((String) jsonRequest.get("name"), STONECOLOR.BLACK);
        Pairing pairing = new Pairing(player, 1);

        Game game = new Game(jsonRequest.get("gameCode").toString(), new Board(), pairing, 0);






        return ResponseEntity.status(HttpStatus.OK).body(game);



    }




}
