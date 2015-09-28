package net.dreamcode.discordapi;

import java.util.ArrayList;
import java.util.Date;


public class DiscordMessage {
    private String id;
    private boolean tts;
    private boolean mention_everyone;
    private String channel_id;
    private String author_id;
    private String content;
    private ArrayList<String> mentions;
    private Date date;

    public DiscordMessage(String id, boolean tts, boolean mention_everyone, String channel_id, String author_id, String content,ArrayList<String> mentions ) {
        this.id = id;
        this.tts = tts;
        this.mention_everyone = mention_everyone;
        this.channel_id = channel_id;
        this.author_id = author_id;
        this.content = content;
        this.mentions = mentions;
//        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public String getContent() {
        return content;
    }

    public boolean isTts() {
        return tts;
    }

    public boolean isMention_everyone() {
        return mention_everyone;
    }

    public String getChannel_id() {
        return channel_id;
    }


    public ArrayList<String> getMentions() {
        return mentions;
    }

    public Date getDate() {
        return date;
    }
}
