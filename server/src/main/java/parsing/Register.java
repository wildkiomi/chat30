package parsing;

import model.User;
import org.apache.log4j.Logger;

public class Register implements ICommand{
    private static Logger log = Logger.getLogger(Register.class);

    public synchronized User execute(User user, String message) {
        String type=message.substring(message.indexOf(" ")+1,message.lastIndexOf(" "));
        String name=message.substring(message.lastIndexOf(" ")+1);
        if (type.contains("client")) {
            user.setType("client");
        }
        if (type.contains("agent")) {
            user.setType("agent");
            points.MyServerEndpoint.freeAgents.add(user);
        }
        user.setName(name);
        //new Writer().execute(user,"register");
        log.info("registration of  "+user.getName());

        return user;
    }
}
