package edu.andreasgut.MuehleWebSpringVue.Controller;

import edu.andreasgut.MuehleWebSpringVue.DTO.GameDto;
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

    @Autowired GameServices gameServices;


    @PostMapping(
            path = "/setup/c",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> setupComputer(@RequestBody Map<String, Object> jsonRequest) {

        System.out.println("Neuer Request: Setup Computerspiel");
        jsonRequest.forEach((key, value) -> System.out.println(key + ": " + value));


        if (jsonRequest.get("modus").equals("c")){
            GameDto gameDto = gameServices.setupComputerGame(jsonRequest);
            System.out.println("Computerspiel gestartet");
            return ResponseEntity.status(HttpStatus.OK).body(gameDto);
        }


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Das Game konnte nicht erstellt werden.");
    }

    @PostMapping(
            path = "/setup/l",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Game> setupLoginspiel(@RequestBody Map<String, Object> jsonRequest) {

        System.out.println("Neuer Request: Setup Loginspiel");
        jsonRequest.forEach((key, value) -> System.out.println(key + ": " + value));

        if (jsonRequest.get("modus").equals("l")){

            System.out.println("Computerspiel gestartet");

        }


        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(
            path = "/setup/b",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Game> setupBeobachten(@RequestBody Map<String, Object> jsonRequest) {

        System.out.println("Neuer Request: Setup Beobachten");
        jsonRequest.forEach((key, value) -> System.out.println(key + ": " + value));

        if (jsonRequest.get("modus").equals("b")){

            System.out.println("Computerspiel gestartet");

        }


        return ResponseEntity.status(HttpStatus.OK).body(null);
    }






}
