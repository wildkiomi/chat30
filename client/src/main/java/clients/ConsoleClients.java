package clients;

import model.Message;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Date;
import javax.websocket.*;

public class ConsoleClients {

    public Session session;
    private static Logger log= Logger.getLogger(ConsoleClients.class);

    public class IncomingReader implements Runnable {
        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = "";
            try {
                while ((input = br.readLine()) != null) {
                    Message message = new Message();
                    message.setSender("you");
                    message.setContent(input);
                    message.setReceived(new Date());
                    try {
                        session.getBasicRemote().sendObject(message);
                    } catch (EncodeException e) {
                        log.error("can't send message from client to server");
                    }
                }

            } catch (IOException e) {
                log.error("can't read message from console");
            }
        }
    }

    protected void go()
    {

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        String uri = "ws://localhost:8080/desktop-client";
        System.out.println("Connecting to " + uri);
        try {
            session = container.connectToServer(MyClientEndpoint.class, URI.create(uri));
        } catch (DeploymentException e) {
            log.error("can't connect to server");
        } catch (IOException e) {
            log.error("can't get websocket container");
        }
        Thread thread=new Thread(new IncomingReader());
        thread.start();

    }
    public ConsoleClients(){
        go();
    }

    private String parsing(String s){
        if (!s.startsWith("/"))
            s="/message "+s;
        log.info("parsing message");
        return s;
    }
    public static void main(String args[]){
        new ConsoleClients();
        log.info("new client chat");
    }
}