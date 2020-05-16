package me.moshe.dropstack.util;

import me.moshe.dropstack.DropStack;
import net.md_5.bungee.api.ChatColor;

import java.util.List;

public class Utils {
    static DropStack plugin;
    public Utils(DropStack plugin){
        this.plugin = plugin;
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String cs(String s) {
        return plugin.getConfig().getString(s);
    }

    public static String ucs(String s) {
        return Utils.color(plugin.getConfig().getString(s));
    }

    public static List<String> csl(String s) {
        return plugin.getConfig().getStringList(s);
    }

    public static Integer ci(String s) {
        return plugin.getConfig().getInt(s);
    }

    public static Boolean cb(String s) {
        return plugin.getConfig().getBoolean(s);
    }
}
