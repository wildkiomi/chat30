package rest;
import model.User;
import points.MyServerEndpoint;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/agents")
public class Agents {
    ArrayList<User> agents=MyServerEndpoint.Agents;
    ArrayList<User> freeAgents=MyServerEndpoint.freeAgents;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllAgents() {
       ArrayList<String> output=new ArrayList<String>();
        for (User agent: agents) {
           output.add("\n\n"+agent.getName());
        }
        return output.toString();
    }
    @GET
    @Path("/free")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFreeAgents() {
        ArrayList<String> output=new ArrayList<String>();
        for (User agent: freeAgents) {
            output.add("\n\n"+agent.getName());
        }
        return output.toString();
    }
    @GET
    @Path("/amount")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAmount() {
        return Integer.toString(freeAgents.size());
    }




}