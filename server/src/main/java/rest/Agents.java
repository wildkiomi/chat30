package rest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.Message;
import model.User;
import points.MyServerEndpoint;

import javax.websocket.EncodeException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;

@Path("/agents")
@Api(value = "Account", description = "APIs for working with agents")
public class Agents {
    ArrayList<User> agents = MyServerEndpoint.Agents;
    ArrayList<User> freeAgents = MyServerEndpoint.freeAgents;

    @GET
    @Path("/all")
    @ApiOperation(value = "all agents")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllAgents() {
        ArrayList<String> output = new ArrayList<String>();
        for (User agent : agents) {
            output.add("\n\n" + agent.getName());
        }
        return output.toString();
    }

    @GET
    @Path("/free")
    @ApiOperation(value = "all free agents")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFreeAgents() {
        ArrayList<String> output = new ArrayList<String>();
        for (User agent : freeAgents) {
            output.add("\n\n" + agent.getName());
        }
        return output.toString();
    }

    @GET
    @Path("/amount")
    @ApiOperation(value = "amount of free agents")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAmount() {
        return Integer.toString(freeAgents.size());
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "info about agent by name")
    public String getInfo(@PathParam("name") String name) {
        String info = "";
        for (User agent : freeAgents) {
            if (name.equals(agent.getName())) {
                info = agent.getName() + " " + agent.getNumberOfChat();
            }
        }
        return info;
    }

    @PUT
    @Path("/register/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "register new agent")
    public String register(@PathParam("name") String name) {
        User user = new User();
        user.setType("agent");
        user.setName(name);
        points.MyServerEndpoint.freeAgents.add(user);
        return "agent " + user.getName() + " registrated";
    }

    @PUT
    @Path("/send_message/{name}/{message}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "send message to agent")
    public String sendMessage(@PathParam("name") String name, @PathParam("message") String message) {
        Message newMessage = new Message();
        newMessage.setContent(message);
        newMessage.setSender("restClient");
        for (User agent : freeAgents) {
            if (name.equals(agent.getName())) {
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

    @PUT
    @Path("/leave/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "send message to agent")
    public String leaveChat(@PathParam("name") String name) {
        sendMessage(name,"/leave");
        return "leave";
    }


}