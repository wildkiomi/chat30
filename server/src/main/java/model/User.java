package model;

import javax.websocket.Session;

public class User {
    private String name;
    private Integer numberOfChat;
    private String dMessage=null;
    private String type;
    private Session session;

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfChat(Integer numberOfChat) {
        this.numberOfChat = numberOfChat;
    }



    public String getName() {
        return name;
    }


    public Integer getNumberOfChat() {
        return numberOfChat;
    }


    public String getdMessage() {
        return dMessage;
    }

    public void setdMessage(String dMessage) {
        if (this.dMessage==null)
            this.dMessage = dMessage;
        else {this.dMessage=this.dMessage+"\n"+dMessage;}
    }
    public void devalueDMessage(){
        this.dMessage=null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
