package rest;

import model.Message;
import model.User;
import parsing.Leave;
import points.MyServerEndpoint;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Path("/clients")

public class Clients {
    ArrayList<User> clients = MyServerEndpoint.clients;

    private JsonObject toObject(User user){
        return Json.createObjectBuilder()
                .add("type", user.getType())
                .add("name", user.getName())
                .add("hashcode", user.hashCode())
                .build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllClients() {
        ArrayList<JsonObject> jsonList=new ArrayList<JsonObject>();
        for (User client: clients) {
            jsonList.add(toObject(client));
        }
        return jsonList.toString();
    }


    @GET
    @Path("/amount")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAmount() {
        return Integer.toString(clients.size());
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfo(@PathParam("name") String name) {
        User user=null;
        for (User client : clients) {
            if (name.equals(client.getName())) {
                user=client;
            }
        }
        return toObject(user).toString();
    }

    @GET
    @Path("/register/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String register(@PathParam("name") String name) {
        User user = new User();
        user.setType("agent");
        user.setName(name);
        MyServerEndpoint.clients.add(user);
        User[] chat=new User[2];
        chat[0]=user;
        MyServerEndpoint.chats.add(chat);
        return "registrated "+toObject(user);
    }

    @GET
    @Path("/send_message/{name}/{message}")
    @Produces(MediaType.APPLICATION_JSON)
    public String sendMessage(@PathParam("name") String name, @PathParam("message") String message) {
        Message newMessage = new Message();
        newMessage.setContent(message);
        newMessage.setSender("rest user");
        newMessage.setReceived(new Date());
        for (User client : clients) {
            if (client.getName().equals(name)) {
                try {
                    client.getSession().getBasicRemote().sendObject(newMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
            }
        }

        return "message " + message + " received";
    }

    @GET
    @Path("/leave/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String leaveChat(@PathParam("name") String name) {
        for (User client: clients){
            if (client.getName().equals(name))
                new Leave().execute(client,"leave");
        }

        return "leave";
    }
    @GET
    @Path("/get_message/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessage(@PathParam("name") String name) {
        return MyServerEndpoint.message.getContent();
    }


}