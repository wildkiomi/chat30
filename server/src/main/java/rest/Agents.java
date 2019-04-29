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

@Path("/agents")

public class Agents {
    ArrayList<User> agents = MyServerEndpoint.Agents;
    ArrayList<User> freeAgents = MyServerEndpoint.freeAgents;

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
    public String getAllAgents() {
        ArrayList<JsonObject> jsonList=new ArrayList<JsonObject>();
        for (User agent: agents) {
            jsonList.add(toObject(agent));
        }
        return jsonList.toString();
    }

    @GET
    @Path("/free")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFreeAgents() {
        ArrayList<JsonObject> jsonList=new ArrayList<JsonObject>();
        for (User agent: freeAgents) {
            jsonList.add(toObject(agent));
        }
        return jsonList.toString();
    }

    @GET
    @Path("/amount")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAmount() {
        return Integer.toString(agents.size());
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfo(@PathParam("name") String name) {
        User user=null;
        for (User agent : freeAgents) {
            if (name.equals(agent.getName())) {
                user=agent;
            }
        }
        return toObject(user).toString();
    }

    @GET
    @Path("/register/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public synchronized String register(@PathParam("name") String name) {
        User user = new User();
        user.setType("agent");
        user.setName(name);
        points.MyServerEndpoint.freeAgents.add(user);
        points.MyServerEndpoint.Agents.add(user);
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
        for (User agent : agents) {
            if (agent.getName().equals(name)) {
                try {
                    agent.getSession().getBasicRemote().sendObject(newMessage);
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
    public synchronized String leaveChat(@PathParam("name") String name) {
        for (User agent: agents){
            if (agent.getName().equals(name))
                new Leave().execute(agent,"leave");
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