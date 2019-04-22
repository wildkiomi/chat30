package rest;
import model.User;
import points.MyServerEndpoint;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/clients")
public class Clients{
    public static ArrayList<User[]> clients = MyServerEndpoint.chats ;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
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
    public String getAmount() {
        return Integer.toString(clients.size());
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfo(@PathParam("name") String name){
        String info="";
        for (User[] client: clients){
            if (name.equals(client[0].getName())){
                info=client[0].getName()+" "+client[0].getNumberOfChat();
            }
        }
        return info;
    }




}