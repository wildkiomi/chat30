package parsing;

import model.User;
import org.apache.log4j.Logger;

import static points.MyServerEndpoint.chats;
import static points.MyServerEndpoint.freeAgents;

public class Leave implements ICommand {

    private static Logger log = Logger.getLogger(Leave.class);

    public User execute(User user, String s) {

        new Writer().execute(user, "disconnect");
        if (user.getType().equals("agent")) {
            freeAgents.add(user);
            User[] newChat = {chats.get(user.getNumberOfChat())[0], null};
            chats.set(user.getNumberOfChat(), newChat);
            log.info("leave agent");
        }
        if (user.getType().equals("client")) {
            log.info("leave client");
            User[] newChat = {null, chats.get(user.getNumberOfChat())[1]};
            chats.set(user.getNumberOfChat(), newChat);
        }
        log.info("disconnect");

        return user;
    }
}