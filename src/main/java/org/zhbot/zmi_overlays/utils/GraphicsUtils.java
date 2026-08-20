package org.zhbot.zmi_overlays.utils;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ColorUtil;
import org.zhbot.zmi_overlays.enums.NPCRenderMode;
import org.zhbot.zmi_overlays.enums.ObjectRenderMode;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class GraphicsUtils {
    private final Client client;

    @Inject
    public GraphicsUtils(Client client)
    {
        this.client = client;
    }

    public void renderObject(Graphics2D graphics, GameObject gameObject, ObjectRenderMode renderMode, Color color)
    {
        var area = renderMode == ObjectRenderMode.CLICKBOX ? gameObject.getClickbox() : gameObject.getConvexHull();
        var mousePosition = client.getMouseCanvasPosition();

        var borderColour = ColorUtil.colorWithAlpha(color, 255);
        OverlayUtil.renderHoverableArea(graphics, area, mousePosition, color, borderColour, borderColour.darker());
    }

    public void renderNPC(Graphics2D graphics, NPC npc, NPCRenderMode renderMode, Color color)
    {
        var area = renderMode == NPCRenderMode.TILE ? npc.getCanvasTilePoly() : npc.getConvexHull();
        var mousePosition = client.getMouseCanvasPosition();

        var borderColour = ColorUtil.colorWithAlpha(color, 255);
        OverlayUtil.renderHoverableArea(graphics, area, mousePosition, color, borderColour, borderColour.darker());
    }

    public void renderBox(Graphics2D graphics, Widget widget, Color color)
    {
        renderBox(graphics, widget.getBounds(), color);
    }

    public void renderBox(Graphics2D graphics, WidgetItem widgetItem, Color color)
    {
        renderBox(graphics, widgetItem.getCanvasBounds(), color);
    }

    public void renderBox(Graphics2D graphics, Rectangle bounds, Color color)
    {
        if (bounds == null || bounds.width <= 0 || bounds.height <= 0)
            return;
        var mousePosition = client.getMouseCanvasPosition();

        var borderColour = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
        OverlayUtil.renderHoverableArea(graphics, bounds, mousePosition, color, borderColour, borderColour.darker());
    }
}
