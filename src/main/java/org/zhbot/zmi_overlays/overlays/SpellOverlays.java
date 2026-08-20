package org.zhbot.zmi_overlays.overlays;

import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import org.zhbot.zmi_overlays.ZMIOverlaysConfig;
import org.zhbot.zmi_overlays.ZMIOverlaysPlugin;
import org.zhbot.zmi_overlays.utils.GraphicsUtils;

import javax.inject.Inject;
import java.awt.*;

public class SpellOverlays extends Overlay {
    private final Client client;
    private final ZMIOverlaysPlugin plugin;
    private final ZMIOverlaysConfig config;
    private final GraphicsUtils graphicsUtils;

    @Inject
    private SpellOverlays(Client client, ZMIOverlaysPlugin plugin, ZMIOverlaysConfig config, GraphicsUtils graphicsUtils)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.graphicsUtils = graphicsUtils;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.teleportShow())
            return null;

        if (plugin.outsideOuraniaArea())
            return null;

        var teleportWidget = client.getWidget(InterfaceID.MagicSpellbook.OURANIA_TELEPORT);
        if (teleportWidget == null || teleportWidget.isHidden())
            return null;

        graphicsUtils.renderBox(graphics, teleportWidget, config.teleportColour());
        return null;
    }
}
