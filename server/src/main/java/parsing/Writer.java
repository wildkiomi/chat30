package parsing;

import model.Message;
import model.User;
import org.apache.log4j.Logger;

import java.util.Date;

public class Writer implements ICommand {

    private static Logger log=Logger.getLogger(Writer.class);

    public synchronized User execute(User user, String outputMessage) {
        User[] chat = points.MyServerEndpoint.chats.get(user.getNumberOfChat());
        if (chat[1] == null) {
            chat[0].setdMessage(outputMessage);
            log.info("message didn't send. it was delayed");
        } else {
            if (chat[0].getdMessage()!=null){
                outputMessage=outputMessage+"\n"+chat[0].getName()+": "+chat[0].getdMessage();
                chat[0].devalueDMessage();
                log.info("sent delayed messages");
            }
            for (User member : chat) {
                try {
                    if ((member==user)){continue;}
                        Message message = new Message();
                        message.setSender(user.getName());
                        message.setContent(outputMessage);
                        message.setReceived(new Date());
                        member.getSession().getBasicRemote().sendObject(message);
                        log.info("message from " + user.getName() + " to " + member.getName());

                } catch (Exception e) {
                    log.error("can't send message from server");
                }
            }
        }
        return user;

    }
}
