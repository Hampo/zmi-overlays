package org.zhbot.zmi_overlays.enums;

public enum ObjectRenderMode {
    CLICKBOX("Clickbox"),
    HULL("Hull");

    private final String name;

    ObjectRenderMode(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
