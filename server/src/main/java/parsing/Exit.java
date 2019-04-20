package parsing;

import model.User;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Exit implements ICommand{

    private static Logger log = Logger.getLogger(Exit.class);

    public User execute(User user, String s) {
        try {
            new Writer().execute(user,"exit");
            user.getSession().close();
            log.info("client socket closed");
            System.exit(0);
        } catch (IOException e) {
            log.error("can't close client socket");
        }
        return null;
    }
}
