package net.dreamcode.discordapi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiscordGuild {

    private String id;
    private String name;

    private ArrayList<DiscordChannel> channels;
    private ArrayList<DiscordUser> users;

    protected DiscordGuild(String id, String name, String auth) {
        this.id = id;
        this.name = name;
        this.users = new ArrayList<>();
        this.channels = new ArrayList<>();

        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", auth);

        try {
            String response = HttpClient.get(DiscordClient.BASE_ENDPOINT + "/guilds/" + this.id + "/channels", headers);
            JSONArray json = new JSONArray(response);


            for (int i = 0; i < json.length(); i++) {
                JSONObject current = json.getJSONObject(i);
                DiscordChannel channel = new DiscordChannel(current.getString("id"), current.getString("type"), current.getString("name"));
                this.channels.add(channel);
            }
        }
        catch (IOException e) {
            System.err.println("Cant build Guild object. Error: " + e.getMessage());
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<DiscordChannel> getChannels() {
        return channels;
    }

}
