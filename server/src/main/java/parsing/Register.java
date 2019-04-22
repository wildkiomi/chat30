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
            points.MyServerEndpoint.Agents.add(user);
        }
        user.setName(name);
        log.info("registration of  "+user.getName()+" "+user.getType());

        return user;
    }
}
