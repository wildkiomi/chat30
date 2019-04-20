package clients;



import model.Message;
import model.MessageDecoder;
import model.MessageEncoder;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.websocket.*;

@ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class MyClientEndpoint {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
    }

    @OnMessage
    public void onMessage(Message message) {
        System.out.println(String.format("[%s:%s] %s",
                simpleDateFormat.format(message.getReceived()), message.getSender(), message.getContent()));
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}