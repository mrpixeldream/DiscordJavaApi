package net.dreamcode.discordapi;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DiscordClient {

    public static final String AUTH_ENDPOINT = "https://discordapp.com/api/auth/login";
    public static final String BASE_ENDPOINT = "https://discordapp.com/api";

    private String email;
    private String password;

    private String authToken = null;
    private boolean isLoggedIn = false;

    private String userId;

    private DiscordGuild currentServer;

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
            this.isLoggedIn = true;
        }
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

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public DiscordChannel[] getChannels() throws IOException {
        return this.currentServer.getChannels();
    }
}
