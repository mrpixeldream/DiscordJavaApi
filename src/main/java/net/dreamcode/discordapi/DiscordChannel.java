package net.dreamcode.discordapi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DiscordChannel {

    private String id;
    private String type;
    private String name;

    protected DiscordChannel(String id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name + ";" + this.id;
    }

    public void sendMessage(String token, String message, boolean tts) {
        JSONObject json = new JSONObject();
        json.put("tts", tts);
        json.put("channel_id", this.id);
        json.put("content", message);

        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", token);

        try {
            System.out.println(HttpClient.post(DiscordClient.BASE_ENDPOINT + "/channels/" + this.id + "/messages", json, headers));
        }
        catch (IOException ex) {
            System.err.println("Failed to send message, Error: " + ex.getMessage());
        }
    }

    public void clearChat(String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", token);
        try {
            String response = HttpClient.get(DiscordClient.BASE_ENDPOINT + "/channels/" +
                    this.id + "/messages?limit=5000", headers);

            JSONArray json = new JSONArray(response);
            System.out.println(json.length());

            for (int i = 0; i < json.length(); i++) {
                JSONObject obj = json.getJSONObject(i);

                Thread deleteThread = new DeleteThread(this.id, obj, headers);
                deleteThread.start();
            }
        }
        catch (IOException e) {
            System.err.println("Failed to clear chat, Error: " + e.getMessage());
        }
    }
}
