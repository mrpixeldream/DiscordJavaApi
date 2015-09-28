package net.dreamcode.discordapi;

import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public abstract class  DiscordClient {

    public static final String AUTH_ENDPOINT = "https://discordapp.com/api/auth/login";
    public static final String BASE_ENDPOINT = "https://discordapp.com/api";

    private String email;
    private String password;

    private String authToken = null;
    private boolean isLoggedIn = false;

    private String userId;

    private DiscordGuild currentServer;
    private SocketConnector socket;

    public DiscordClient(String email, String password) {
        this.email = email;
        this.password = password;

        try {
            this.tryConnection();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tryConnection() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("email", this.email);
        params.put("password", this.password);

        String response = HttpClient.post(DiscordClient.AUTH_ENDPOINT, params);

        JSONObject json = new JSONObject(response);

        if (json.has("message")) {
            System.err.println("Unexpected Response: " + json.getString("message"));
        }
        else {
            this.authToken = json.getString("token");
            System.out.println("Logged in. Token: " + this.authToken);

            Map<String, String> headers = new HashMap<>();

            headers.put("authorization", this.authToken);
            response = HttpClient.get(DiscordClient.BASE_ENDPOINT + "/users/@me", headers);
            json = new JSONObject(response);

            this.userId = json.getString("id");

            this.isLoggedIn = true;
            connectSocket();
        }
    }


    private void connectSocket() {
        try {
            socket = new SocketConnector(this, "ws://gateway-arthas.discord.gg", this.authToken);
            socket.connect();
        }
        catch (URISyntaxException e){
            e.printStackTrace();
        }
    }

    public void stop(){
        socket.close();
    }


    public void acceptInvite(String inviteCode) throws IOException {
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        headers.put("authorization", this.authToken);
        String response = HttpClient.post(DiscordClient.BASE_ENDPOINT + "/invite/" + inviteCode, params, headers);
        JSONObject json = new JSONObject(response);

        JSONObject guildObj = json.getJSONObject("guild");

        this.currentServer = new DiscordGuild(guildObj.getString("id"), guildObj.getString("name"), this.authToken);
    }

    public DiscordChannel findById(String id) throws IOException {
        for (DiscordChannel current : this.getChannels()) {
            if (current.getId().equalsIgnoreCase(id)) {
                return current;
            }
        }

        return null;
    }

    public DiscordChannel findByName(String name) throws IOException {
        for (DiscordChannel current : this.getChannels()) {
            if (current.getName().equalsIgnoreCase(name)) {
                return current;
            }
        }

        return null;
    }

    public void sendMessage(DiscordChannel channel, String message, boolean tts) {
        if (channel.getType().equalsIgnoreCase("text")) {
            channel.sendMessage(this.authToken, message, tts);
        }
    }

    public void clearChannel(DiscordChannel channel) {
        if (channel.getType().equalsIgnoreCase("text")) {
            channel.clearChat(this.authToken);
        }
    }

    public void deleteMessage(String channel_id, String messages_id) throws IOException{
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", this.authToken);

        HttpClient.delete(DiscordClient.BASE_ENDPOINT + "/channels/" + channel_id + "/messages/" + messages_id, headers);

    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public DiscordChannel[] getChannels() throws IOException {
        return this.currentServer.getChannels();
    }

    public abstract void  on_Message(DiscordMessage message);



}
