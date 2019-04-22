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

@Path("/clients")
@Api(value = "Chat", description = "APIs for working with clients")
public class Clients{
    public static ArrayList<User[]> clients = MyServerEndpoint.chats ;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "all clients")
    public String getAllClients() {
        ArrayList<String> output=new ArrayList<String>();
        for (User[] client: clients) {
            output.add("\n\n"+client[0].getName());
        }
        return output.toString();
    }
    @GET
    @Path("/free")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "free clients")
    public String getFreeClients() {
        ArrayList<String> output=new ArrayList<String>();
        for (User[] client: clients) {
            if (client[1]==null)
            output.add("\n\n"+client[0].getName());
        }
        return output.toString();
    }
    @GET
    @Path("/amount")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "amount of clients")
    public String getAmount() {
        return Integer.toString(clients.size());
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "info about client by name")
    public String getInfo(@PathParam("name") String name){
        String info="";
        for (User[] client: clients){
            if (name.equals(client[0].getName())){
                info=client[0].getName()+" "+client[0].getNumberOfChat();
            }
        }
        return info;
    }

    @PUT
    @Path("/register/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "register new agent")
    public String register(@PathParam("name") String name) {
        User user=new User();
        user.setType("client");
        user.setName(name);
        User[] chat=new User[2];
        chat[0]=user;
        points.MyServerEndpoint.chats.add(chat);
        return "client "+user.getName()+" registrated";
    }
    @PUT
    @Path("/send_message/{name}/{message}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "send message to client")
    public String sendMessage(@PathParam("name") String name,@PathParam("message") String message) {
        Message newMessage=new Message();
        newMessage.setContent(message);
        newMessage.setSender("restClient");
        for (User[] client: clients) {
            if (name.equals(client[0].getName())) {
                try {
                    client[0].getSession().getBasicRemote().sendObject(newMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
            }

        }
            return "message "+message+" received";
        }
    @PUT
    @Path("/leave/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "send message to client")
    public String leaveChat(@PathParam("name") String name) {
        sendMessage(name,"/leave");
        return "leave chat";
    }




}