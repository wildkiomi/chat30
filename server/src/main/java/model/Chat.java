package model;

import java.util.ArrayList;
import java.util.Date;

public class Chat {
    private Date date;
    private User owner;
    private static ArrayList<User> members=new ArrayList<User>();

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public static ArrayList<User> getMembers() {
        return members;
    }

    public void addMembers(User user) {
        this.members.add(user);
    }
}
