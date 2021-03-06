package com.dbkynd.discordallowlist.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class Config {
    public static final ForgeConfigSpec config;
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    static {
        MySQLConfig.init(builder);
        DiscordConfig.init(builder);
        BypassConfig.init(builder);
        config = builder.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }

    public static List<String> removeEmpty(List<String> items) {
        ArrayList<String> arrayList = new ArrayList<>(items);
        return arrayList.stream()
                .filter(item -> item != null && !item.isEmpty())
                .collect(Collectors.toList());
    }
}
