package parsing;

import model.Message;
import model.User;

import java.util.Date;

public class Writer implements ICommand {
    public synchronized User execute(User user, String outputMessage) {
        outputMessage = outputMessage.substring(outputMessage.indexOf(" ") + 1);
        User[] chat = points.MyServerEndpoint.chats.get(user.getNumberOfChat());
        if (chat[1] == null) {
            chat[0].setdMessage(outputMessage);
        } else {
            if (chat[0].getdMessage()!=null){
                outputMessage=outputMessage+"\n"+chat[0].getName()+": "+chat[0].getdMessage();
                chat[0].devalueDMessage();}
            for (User member : chat) {
                try {
                    if ((member==user)&&(!outputMessage.contains("connect"))&&(!outputMessage.contains("disconnect"))){continue;}
                    Message message=new Message();
                    message.setSender(user.getName());
                    message.setContent(outputMessage);
                    message.setReceived(new Date());
                    member.getSession().getBasicRemote().sendObject(message);
                    //log.info("сообщение от " + user.getName() + " к " + member.getName());

                } catch (Exception e) {
                    //log.error("не отправляются сообщения собеседнику");
                }
            }
        }
        return user;

    }
}
