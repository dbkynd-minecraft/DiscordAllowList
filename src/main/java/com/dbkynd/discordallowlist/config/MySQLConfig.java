package com.dbkynd.discordallowlist.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MySQLConfig {
    public static ForgeConfigSpec.ConfigValue<String> host;
    public static ForgeConfigSpec.ConfigValue<String> port;
    public static ForgeConfigSpec.ConfigValue<String> database;
    public static ForgeConfigSpec.ConfigValue<String> table;
    public static ForgeConfigSpec.ConfigValue<String> username;
    public static ForgeConfigSpec.ConfigValue<String> password;

    public static void init(ForgeConfigSpec.Builder common) {
        common.comment("MySQL Config");
        host = common.comment("Hostname").define("sql.host", "");
        port = common.comment("Port").define("sql.port", "3306");
        database = common.comment("Database Name").define("sql.database", "");
        table = common.comment("Table Name").define("sql.table", "allow_list");
        username = common.comment("Username").define("sql.username", "");
        password = common.comment("Password").define("sql.password", "");
    }
}
