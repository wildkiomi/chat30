package parsing;

import model.User;
import org.apache.log4j.Logger;

import static points.MyServerEndpoint.chats;
import static points.MyServerEndpoint.freeAgents;

public class Leave implements ICommand {

    private static Logger log = Logger.getLogger(Leave.class);

    public User execute(User user, String s) {

        new Writer().execute(user, "disconnect");
            freeAgents.add(chats.get(user.getNumberOfChat())[1]);
            User[] newChat = new User[2];
            newChat[0]=chats.get(user.getNumberOfChat())[0];
            chats.set(user.getNumberOfChat(), newChat);
        log.info("disconnect");

        return user;
    }
}