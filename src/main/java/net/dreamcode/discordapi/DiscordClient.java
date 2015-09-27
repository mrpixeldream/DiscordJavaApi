package net.dreamcode.discordapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DiscordClient {

    private final String AUTH_ENDPOINT = "http://discordapp.com/api/auth/login";

    private String email;
    private String password;

    private String authToken = null;

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

        String response = HttpClient.post(this.AUTH_ENDPOINT, params);

        
    }

}
