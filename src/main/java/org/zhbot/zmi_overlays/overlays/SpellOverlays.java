package org.zhbot.zmi_overlays.overlays;

import net.runelite.api.Client;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.util.Text;
import org.zhbot.zmi_overlays.ZMIOverlaysConfig;
import org.zhbot.zmi_overlays.ZMIOverlaysPlugin;
import org.zhbot.zmi_overlays.utils.GraphicsUtils;
import org.zhbot.zmi_overlays.utils.PouchUtils;

import javax.inject.Inject;
import java.awt.*;

public class SpellOverlays extends Overlay {
    private final Client client;
    private final ZMIOverlaysPlugin plugin;
    private final ZMIOverlaysConfig config;
    private final GraphicsUtils graphicsUtils;
    private final PouchUtils pouchUtils;

    private boolean essenceInInventory = false;

    @Inject
    private SpellOverlays(Client client, ZMIOverlaysPlugin plugin, ZMIOverlaysConfig config, GraphicsUtils graphicsUtils, PouchUtils pouchUtils)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.graphicsUtils = graphicsUtils;
        this.pouchUtils = pouchUtils;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.MANUAL);
        drawAfterInterface(InterfaceID.MAGIC_SPELLBOOK);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.teleportShow())
            return null;

        if (plugin.outsideOuraniaArea())
            return null;

        var teleportWidget = client.getWidget(InterfaceID.MagicSpellbook.OURANIA_TELEPORT);
        if (teleportWidget != null && !teleportWidget.isHidden())
            graphicsUtils.renderBox(graphics, teleportWidget, hasEssence() ? config.teleportBadColour() : config.teleportColour());

        return null;
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event)
    {
        if (event.getContainerId() != InventoryID.INV)
            return;

        var inventory = event.getItemContainer();

        essenceInInventory = inventory.contains(ItemID.BLANKRUNE_HIGH) || inventory.contains(ItemID.BLANKRUNE_DAEYALT);
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event)
    {
        if (!config.teleportDisable())
            return;

        if (plugin.outsideOuraniaArea())
            return;

        if (!event.getOption().equalsIgnoreCase("Cast"))
            return;

        var target = Text.removeTags(event.getTarget());
        if (!target.equalsIgnoreCase("Ourania Teleport"))
            return;

        if (!hasEssence())
            return;

        client.getMenu().removeMenuEntry(event.getMenuEntry());
    }

    private boolean hasEssence()
    {
        return essenceInInventory || pouchUtils.hasPouchWithEssence();
    }
}
