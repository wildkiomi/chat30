package util;
import org.apache.log4j.Logger;
import javax.json.Json;

public class JsonUtil {
    private static Logger log=Logger.getLogger(JsonUtil.class);

    public static String formatMessage(String message, String user) {
        log.info("creation of json object");
        return Json.createObjectBuilder()
                .add("message", message)
                .add("sender", user)
                .add("received", "")
                .build().toString();
    }

}