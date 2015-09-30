package net.dreamcode.discordapi;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class DeleteThread extends Thread {
    private JSONObject obj;
    private Map<String, String> headers;
    private String channelId;

    public DeleteThread(String channelId, JSONObject obj, Map<String, String> headers) {
        this.headers = headers;
        this.obj = obj;
        this.channelId = channelId;
    }

    public void run() {
        try {
            HttpClient.delete(DiscordClient.BASE_ENDPOINT + "/channels/" + this.channelId + "/messages/" + obj.getString("id"), headers);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
