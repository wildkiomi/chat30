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

    protected void start()
    {

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        String uri = "ws://localhost:8080/desktop-client";
        System.out.println("Connecting to " + uri);
        try {
            session = container.connectToServer(MyClientEndpoint.class, URI.create(uri));
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String args[]){
        ConsoleClients client = new ConsoleClients();
        client.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        try {
            while ((input = br.readLine())!=null){
                Message message=new Message();
                message.setSender("des");
                message.setContent(input);
                message.setReceived(new Date());
                try {
                    client.session.getBasicRemote().sendObject(message);
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}