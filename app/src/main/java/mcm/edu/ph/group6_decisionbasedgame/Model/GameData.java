package mcm.edu.ph.group6_decisionbasedgame.Model;

public class GameData {

    String userName;

    public GameData(){}

    public GameData(String userName){
        this.userName = userName;
    }


    //SETTERS
    public void setUserName (String userName) { this.userName = userName; }

    //GETTERS
    public String getUserName() { return userName; }


}


