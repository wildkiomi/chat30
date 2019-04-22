package model;

import org.apache.log4j.Logger;
import util.JsonUtil;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {
    private static Logger log=Logger.getLogger(MessageEncoder.class);


    public void init(final EndpointConfig config) {
    }


    public void destroy() {
    }


    public String encode(final Message message) throws EncodeException {
        log.info("encoded message");
        return JsonUtil.formatMessage(message.getContent(), message.getSender());
    }

}
