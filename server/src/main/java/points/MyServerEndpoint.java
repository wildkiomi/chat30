package points;
import model.*;
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
    public static ArrayList<User> Agents = new ArrayList();
    public static ArrayList<User> clients = new ArrayList();
    public static Map<String, ICommand> map = new HashMap<String, ICommand>();
    private static Logger log= Logger.getLogger(MyServerEndpoint.class);
    public static Message message=null;
    public static Chat chat;
    public static ArrayList<Chat> allChats=new ArrayList<Chat>();

    {
        log.info("initialize arraylist");
        map.put("/register", new Register());
        map.put("/leave",new Leave());
        map.put("/exit",new Exit());
    }

    @OnOpen
    public void onOpen(final Session session) {
        this.session=session;
        this.user=new User();
        user.setSession(session);
        log.info("open server endpoint");
        this.chat=new Chat();
        chat.setDate(new Date());
        allChats.add(chat);
    }

    public void parsing(String s) {
        log.info("parsing");
        if (s.startsWith("/")) {
            String command = s;
            if (s.contains(" ")) command = s.substring(0, s.indexOf(" "));
            user = map.get(command).execute(user, s);
            connecting();
        }
        else new Writer().execute(user,s);
    }

    @OnMessage
    public void onMessage(final Message message, @PathParam("client-id") String clientId) {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException e) {
                log.info("can't get basic remote of session");
            } catch (EncodeException e) {
                log.info("can't send message to web client");
            }
        parsing(message.getContent());
            this.message=message;

    }

    @OnClose
    public void onClose(Session session) {
        new Exit().execute(user,"exit");
        log.info("close server");
    }

    public synchronized void connecting() {
        boolean connection = false;
        int k = 0;
        for (User agent : freeAgents) {
            for (int i = 0; i < chats.size(); i++) {
                if ((chats.get(i)[1] == null) && (agent.getNumberOfChat() != chats.get(i)[0].getNumberOfChat())) {
                    agent.setNumberOfChat(chats.get(i)[0].getNumberOfChat());
                    User[] users = {chats.get(i)[0], agent};
                    chats.set(i, users);
                    freeAgents.remove(k);
                    log.info("connect " + chats.get(i)[0].getName() + " " + chats.get(i)[1].getName());
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
            if (user.getType().equals("agent")) chat.addMembers(chats.get(user.getNumberOfChat())[0]);
            if (user.getType().equals("client")) chat.addMembers(chats.get(user.getNumberOfChat())[1]);
        }

    }
}