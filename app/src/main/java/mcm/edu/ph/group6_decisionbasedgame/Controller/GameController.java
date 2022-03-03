package mcm.edu.ph.group6_decisionbasedgame.Controller;

import java.util.Random;

public class GameController {

    private final Random random = new Random();
    boolean response, alive;

    public String randomizeSibling(){
        int sex = random.nextInt(2);
        String sibling;
        if (sex == 1){
            sibling = "sister";
        }
        else {
            sibling = "brother";
        }
        return sibling;
    }

    public boolean randomizeSurvival(){
        int luck = random.nextInt(2);
        if (luck == 1){
            alive = true; // live
        }
        else {
            alive = false; // die
        }
        return alive;
    }

    public boolean policeResponse(){
        int chance = random.nextInt(2);
        if (chance == 1){
            response = true; // respond
        }
        else {
            response = false; // ignore
        }
        return response;
    }

}
