package model;

import org.apache.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.util.Date;

public class MessageDecoder implements Decoder.Text<Message> {

    private static Logger log=Logger.getLogger(MessageDecoder.class);

    public void init(final EndpointConfig config) {
    }


    public void destroy() {
    }


    public Message decode(final String textMessage) throws DecodeException {
        Message message = new Message();
        JsonObject jsonObject = Json.createReader(new StringReader(textMessage)).readObject();
        message.setContent(jsonObject.getString("message"));
        message.setSender(jsonObject.getString("sender"));
        message.setReceived(new Date());
        log.info("decoded json message");
        return message;
    }


    public boolean willDecode(final String s) {
        return true;
    }

}
