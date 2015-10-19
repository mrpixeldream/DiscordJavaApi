package net.dreamcode.discordapi;

import org.json.JSONObject;

import javax.print.URIException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * A API for Discord to create your own Bot
 */


public abstract class DiscordClient {

    public static final String AUTH_ENDPOINT = "https://discordapp.com/api/auth/login";
    public static final String BASE_ENDPOINT = "https://discordapp.com/api";
    public static final String BASE_ENDPOINT_U = "http://discordapp.com/api";

    private String email;
    private String password;

    private String authToken = null;
    private boolean isLoggedIn = false;

    private String userId;

    private DiscordGuild currentServer;
    private SocketConnector socket;

    private ArrayList<DiscordClient> discordClients  = new ArrayList<>();


    /**
     * The constructor tries to connect to Discord.
     * After the bot successfully connect to Discord,
     * he can receive messages.
     *
     * @param email The email from the bot account
     * @param password The password from the bot account
     *
     * @throws IOException
     * @throws URISyntaxException if the socket URI can't be converted
     */

    public DiscordClient(String email, String password) throws IOException,URISyntaxException {

        this.email = email;
        this.password = password;

            this.tryConnection();

    }

    /**
     *
     * @return the user ID as String
     */

    public String getUserId() {
        return userId;
    }

    private void tryConnection() throws IOException, URISyntaxException {
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


    /**
     * starts the chat socket
     *
     * @throws URISyntaxException
     * @throws IOException
     */

    private void connectSocket() throws URISyntaxException,IOException {

        Map<String, String> headers = new HashMap<>();

        headers.put("authorization", this.authToken);

        String response = HttpClient.get(DiscordClient.BASE_ENDPOINT_U + "/gateway", headers);
        JSONObject json = new JSONObject(response);

        socket = new SocketConnector(this, json.getString("url"), this.authToken);
        socket.connect();
    }

    public void stop() {
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

    public DiscordChannel findChannelById(String id) throws IOException {
        for (DiscordChannel current : this.getChannels()) {
            if (current.getId().equalsIgnoreCase(id)) {
                return current;
            }
        }

        return null;
    }

    public DiscordChannel findChannelByName(String name) throws IOException {
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

    public void deleteMessage(String channel_id, String messages_id) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", this.authToken);

        HttpClient.delete(DiscordClient.BASE_ENDPOINT + "/channels/" + channel_id + "/messages/" + messages_id, headers);

    }



    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public ArrayList<DiscordChannel> getChannels() throws IOException {
        return this.currentServer.getChannels();
    }

    public void addUser(DiscordClient client){

            for(DiscordClient temp : discordClients){
                if(temp.getUserId().equalsIgnoreCase(client.getUserId()));
            }


    }

    public abstract void on_Message(DiscordMessage message);


}
