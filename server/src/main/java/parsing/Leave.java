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
            User[] newChat = {chats.get(user.getNumberOfChat())[0],null};
            chats.set(user.getNumberOfChat(), newChat);
        log.info("disconnect");

        return user;
    }
}