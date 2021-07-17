package com.dbkynd.discordallowlist.sql;

public class UserRecord {
    String discordId;
    String minecraftName;
    String uuid;

    public UserRecord(String discordId, String minecraftName, String uuid) {
        this.discordId = discordId;
        this.minecraftName = minecraftName;
        this.uuid = uuid;
    }

    public String getDiscordId() {
        return discordId;
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public String getUUID() {
        return uuid;
    }
}
