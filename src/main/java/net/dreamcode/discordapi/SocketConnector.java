package net.dreamcode.discordapi;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;



public class SocketConnector extends WebSocketClient implements Runnable {


    private boolean online = false;
    private String socketURL;
    private String token;
    private DiscordClient whoIsCalling;
    private KeepALive aLive = new KeepALive(this, 5000);


    public SocketConnector(DiscordClient whoIsCalling, String url, String token) throws URISyntaxException{
            super(new URI(url), new Draft_10());

        this.socketURL = url;
        this.whoIsCalling = whoIsCalling;
        this.token = token;


    }

    public boolean isOnline() {
        return online;
    }

    public void login() {
        send("{\"op\":2,\"d\":{\"token\":\"" + token +"\",\"properties\":{\"$os\":\"Linux\",\"$browser\":\"BotisCalling\",\"$device\":\"BotisCalling\",\"$referrer\":\"\",\"$referring_domain\":\"\"},\"v\":2}}");
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("new connection opened");
        login();



    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

        if (code < 0 && !isOnline()){
            System.out.println("Can not connect to discord");
        }
        System.out.println("closed with exit code " + code + " additional info: " + reason);


    }




    @Override
    public void onMessage(String message) {
        String state;
        JSONObject json = new JSONObject(message);

        state = json.getString("t");

        switch (state) {
            case "READY":
                System.out.println("Connected and logged in to Discord");
                online = true;
                aLive.start();
                break;
            case "MESSAGE_CREATE": message_create(json);
                break;
            case "MESSAGE_UPDATE": message_create(json);
                break;
            case "VOICE_STATE_UPDATE":
                break;

        }

        System.out.println("received message: " + message);

    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

    @Override
    public void close(){
        online = false;
        aLive.interrupt();
        super.close();
    }

    private void message_create(JSONObject json) {

        JSONObject message =json.getJSONObject("d");

        String id = message.getString("id");
        boolean tts = message.getBoolean("tts");
        boolean mention_everyone = message.getBoolean("mention_everyone");
        String channel_id = message.getString("channel_id");
        String author_id  = message.getJSONObject("author").getString("id");
        String content = message.getString("content");
        ArrayList<String> mentions = new ArrayList<>();
        JSONArray jArray = message.getJSONArray("mentions");

        for (int i = 0; i< jArray.length(); i++) {

           mentions.add(jArray.getJSONObject(i).getString("id"));


        }
        // Date date = new Date()

        DiscordMessage discordmessage = new DiscordMessage(id,tts,mention_everyone,channel_id, author_id,content, mentions);

        whoIsCalling.on_Message(discordmessage);



    }
}
