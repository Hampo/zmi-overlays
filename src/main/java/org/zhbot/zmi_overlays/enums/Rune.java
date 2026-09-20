package org.zhbot.zmi_overlays.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum Rune {
    MIND("Mind rune"),
    AIR("Air rune"),
    WATER("Water rune"),
    EARTH("Earth rune"),
    FIRE("Fire rune"),
    BODY("Body rune"),
    COSMIC("Cosmic rune"),
    CHAOS("Chaos rune"),
    ASTRAL("Astral rune"),
    LAW("Law rune"),
    DEATH("Death rune"),
    BLOOD("Blood rune"),
    NATURE("Nature rune"),
    SOUL("Soul rune");

    @Getter
    private final String name;

    Rune(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    private static final Map<String, Rune> NAME_MAP = new HashMap<>();

    static {
        for (Rune rune : Rune.values())
            NAME_MAP.put(rune.getName(), rune);
    }

    public static Rune getByName(String name)
    {
        return NAME_MAP.get(name);
    }
}
