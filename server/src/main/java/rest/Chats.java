package rest;

import model.Chat;
import points.MyServerEndpoint;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;


@Path("/chats")
public class Chats {

    private JsonObject toObject(Chat chat){
        return Json.createObjectBuilder()
                .add("date", chat.getDate().toString())
                .add("owner", chat.getOwner().getName())
                .add("hashcode", chat.hashCode())
                .add("members",chat.getMembers().toString())
                .build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllChats() {
        ArrayList<JsonObject> jsonList=new ArrayList<JsonObject>();
        for (Chat chat: MyServerEndpoint.allChats) {
            jsonList.add(toObject(chat));
        }
        return jsonList.toString();
    }

    @GET
    @Path("/{owner}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfo(@PathParam("owner") String owner) {
        ArrayList<JsonObject> jsonList=new ArrayList<JsonObject>();
        Chat newChat=null;
        for (Chat chat: MyServerEndpoint.allChats) {
            if (chat.getOwner().getName().equals(owner))
                newChat=chat;
        }
        return toObject(newChat).toString();
    }
}
