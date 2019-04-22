package clients;
import model.Message;
import model.MessageDecoder;
import model.MessageEncoder;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import javax.websocket.*;

@ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class MyClientEndpoint {

    private static Logger log= Logger.getLogger(MyClientEndpoint.class);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to endpoint");
        log.info("connected to client endpoint");
    }

    @OnMessage
    public void onMessage(Message message) {
        System.out.println(String.format("[%s:%s] %s",
                simpleDateFormat.format(message.getReceived()), message.getSender(), message.getContent()));
        log.info("message from server");
    }

    @OnError
    public void onError(Throwable t) {
        log.error("error");

    }
}