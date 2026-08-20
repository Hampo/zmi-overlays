package org.zhbot.zmi_overlays.enums;

public enum NPCRenderMode {
    TILE("Tile"),
    HULL("Hull");

    private final String name;

    NPCRenderMode(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
