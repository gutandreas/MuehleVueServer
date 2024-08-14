package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SenderService {

    SimpMessagingTemplate template;

    @Autowired
    public SenderService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void addGameToAdmin(Game game){
        String path = "/topic/manager/games/add";
        template.convertAndSend(path, game);

    }


}
