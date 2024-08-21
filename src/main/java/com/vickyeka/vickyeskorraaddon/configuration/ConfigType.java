package com.vickyeka.vickyeskorraaddon.configuration;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ConfigType {
    private static final HashMap<String, com.vickyeka.vickyeskorraaddon.configuration.ConfigType> ALL_TYPES = new HashMap();
    public static final com.vickyeka.vickyeskorraaddon.configuration.ConfigType DEFAULT = new com.vickyeka.vickyeskorraaddon.configuration.ConfigType("Default");
    public static final com.vickyeka.vickyeskorraaddon.configuration.ConfigType PRESETS = new com.vickyeka.vickyeskorraaddon.configuration.ConfigType("Presets");
    public static final com.vickyeka.vickyeskorraaddon.configuration.ConfigType LANGUAGE = new com.vickyeka.vickyeskorraaddon.configuration.ConfigType("Language");
    public static final com.vickyeka.vickyeskorraaddon.configuration.ConfigType[] CORE_TYPES;
    private final String string;

    public ConfigType(String string) {
        this.string = string;
        ALL_TYPES.put(string, this);
    }

    public static List<com.vickyeka.vickyeskorraaddon.configuration.ConfigType> addonValues() {
        List<com.vickyeka.vickyeskorraaddon.configuration.ConfigType> values = new ArrayList();
        Iterator var1 = ALL_TYPES.keySet().iterator();

        while(var1.hasNext()) {
            String key = (String)var1.next();
            if (!Arrays.asList(CORE_TYPES).contains(ALL_TYPES.get(key))) {
                values.add((com.vickyeka.vickyeskorraaddon.configuration.ConfigType)ALL_TYPES.get(key));
            }
        }

        return values;
    }

    public static List<com.vickyeka.vickyeskorraaddon.configuration.ConfigType> coreValues() {
        return Arrays.asList(CORE_TYPES);
    }

    public String toString() {
        return this.string;
    }

    public static List<com.vickyeka.vickyeskorraaddon.configuration.ConfigType> values() {
        List<com.vickyeka.vickyeskorraaddon.configuration.ConfigType> values = new ArrayList();
        Iterator var1 = ALL_TYPES.keySet().iterator();

        while(var1.hasNext()) {
            String key = (String)var1.next();
            values.add((com.vickyeka.vickyeskorraaddon.configuration.ConfigType)ALL_TYPES.get(key));
        }

        return values;
    }

    static {
        CORE_TYPES = new com.vickyeka.vickyeskorraaddon.configuration.ConfigType[]{DEFAULT, PRESETS, LANGUAGE};
    }
}

