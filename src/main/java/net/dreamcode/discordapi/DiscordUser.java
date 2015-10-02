package net.dreamcode.discordapi;

/**
 * The Discord User
 */


public class DiscordUser {
    private String username;
    private String id;
    private String avatar;
    private String gameID;
    private boolean online;

    /**
     *
     * @param username The Username
     * @param id    UserID
     * @param avatar    AvatarID
     * @param gameID    GameID
     * @param online    True if online
     */

    public DiscordUser(String username, String id, String avatar, String gameID, boolean online) {
        this.username = username;
        this.id = id;
        this.avatar = avatar;
        this.gameID = gameID;
        this.online = online;
    }

    public String getId() {
        return id;
    }

    public String getAvatar() {

        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
