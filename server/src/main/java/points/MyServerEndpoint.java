package points;
import model.Message;
import model.MessageDecoder;
import model.MessageEncoder;
import model.User;
import org.apache.log4j.Logger;
import parsing.*;

import java.io.IOException;
import java.util.*;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value="/{client-id}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class MyServerEndpoint {


    public Session session;
    public User user;
    public static ArrayList<User[]> chats = new ArrayList();
    public static ArrayList<User> freeAgents = new ArrayList();
    public static Map<String, ICommand> map = new HashMap<String, ICommand>();
    private static Logger log= Logger.getLogger(MyServerEndpoint.class);

    {
        log.info("initialize arraylist");
        map.put("/register", new Register());
        map.put("/leave",new Leave());
        map.put("/exit",new Exit());
        map.put("/message", new Writer());
    }

    @OnOpen
    public void onOpen(final Session session) throws IOException, EncodeException {
        this.session=session;
        this.user=new User();
        user.setSession(session);
    }

    public void parsing(String s) {
        //log.info("parsing");
        String command = s;
        if (s.contains(" ")) command = s.substring(0, s.indexOf(" "));
        user = map.get(command).execute(user, s);
        if (!command.equals("/message")) connecting();
    }

    @OnMessage
    public void onMessage(final Message message, @PathParam("client-id") String clientId) {
            try {
                session.getBasicRemote().sendObject(message);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        parsing(message.getContent());

    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {

        //notify peers about leaving the chat room

    }

    public synchronized void connecting() {
        boolean connection = false;
        if (user.getType().equals("client")) {
            for (int i = 0; i < chats.size(); i++) {
                if ((chats.get(i)[1] != null) && (chats.get(i)[0] == null) && (user.getNumberOfChat() != chats.get(i)[1].getNumberOfChat())) {
                    user.setNumberOfChat(chats.get(i)[1].getNumberOfChat());
                    User[] users = {user, chats.get(i)[1]};
                    chats.set(i, users);
                    //log.info("connect " + chats.get(i)[0].getName() + " " + chats.get(i)[1].getName());
                    connection = true;
                    break;
                }
            }
            if (!connection) {
                User[] chat = new User[2];
                chat[0] = user;
                chats.add(chat);
                user.setNumberOfChat(chats.size() - 1);
            }
        }
        int k = 0;
        for (User agent : freeAgents) {
            for (int i = 0; i < chats.size(); i++) {
                if ((chats.get(i)[1] == null) && (agent.getNumberOfChat() != chats.get(i)[0].getNumberOfChat())) {
                    agent.setNumberOfChat(chats.get(i)[0].getNumberOfChat());
                    User[] users = {chats.get(i)[0], agent};
                    chats.set(i, users);
                    freeAgents.remove(k);
                    // log.info("connect " + chats.get(i)[0].getName() + " " + chats.get(i)[1].getName());
                    connection = true;
                    break;
                }
            }
            if (connection) {
                break;
            }
            k++;
        }
        if (connection) {
            new Writer().execute(user,"connect");
        }

    }
}