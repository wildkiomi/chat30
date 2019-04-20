package points;



import model.Message;
import model.MessageDecoder;
import model.MessageEncoder;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import static java.lang.String.format;

@ServerEndpoint(value="/{client-id}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class MyServerEndpoint {

    private static final LinkedList<Session> clients = new LinkedList<Session>();

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        clients.add(session);
        for (Session peer : clients) {
            Message message = new Message();
            message.setSender("Server");
            message.setContent(format("%s joined the chat room", "joy"));
            message.setReceived(new Date());
            peer.getBasicRemote().sendObject(message);
        }
    }
    @OnMessage
    public void onMessage(Message message,@PathParam("client-id") String clientId) {

        for (Session client : clients) {
            try {
                client.getBasicRemote().sendObject(message);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }
    }
    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        clients.remove(session);
        //notify peers about leaving the chat room
        for (Session peer : clients) {
            Message message = new Message();
            message.setSender("Server");
            message.setContent(format("%s left the chat room", (String) session.getUserProperties().get("user")));
            message.setReceived(new Date());
            peer.getBasicRemote().sendObject(message);
        }
    }
}