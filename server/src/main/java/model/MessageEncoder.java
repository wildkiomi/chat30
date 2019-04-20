package model;

import util.JsonUtil;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {


    public void init(final EndpointConfig config) {
    }


    public void destroy() {
    }


    public String encode(final Message message) throws EncodeException {
        return JsonUtil.formatMessage(message.getContent(), message.getSender());
    }

}
