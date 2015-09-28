package net.dreamcode.discordapi;

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
        return this.name;
    }
}
