package com.dbkynd.discordallowlist.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BypassConfig {
    public static ForgeConfigSpec.ConfigValue<String> bypassNames;


    public static void init(ForgeConfigSpec.Builder common) {
        common.comment("Bypass Config");
        bypassNames = common.comment("Comma delimited list of names that bypass the whitelist.").define("bypass.names", "");
    }

    public static List<String> getBypassNames() {
        String[] names = bypassNames.get().split("(\\s+)?,(\\s+)?");
        return new ArrayList<>(Arrays.asList(names)).stream()
                .filter(item -> item != null && !item.isEmpty())
                .map(String::toLowerCase).collect(Collectors.toList());
    }
}
