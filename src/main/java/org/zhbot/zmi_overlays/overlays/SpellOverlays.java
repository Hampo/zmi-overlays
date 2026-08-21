package org.zhbot.zmi_overlays.overlays;

import net.runelite.api.Client;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.VarbitChanged;
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
import org.zhbot.zmi_overlays.enums.Pouch;
import org.zhbot.zmi_overlays.utils.GraphicsUtils;

import javax.inject.Inject;
import java.awt.*;
import java.util.EnumSet;
import java.util.Set;

public class SpellOverlays extends Overlay {
    private static final int POUCH_TYPE_PURE = 2;
    private static final int POUCH_TYPE_DAEYALT = 3;

    private final Client client;
    private final ZMIOverlaysPlugin plugin;
    private final ZMIOverlaysConfig config;
    private final GraphicsUtils graphicsUtils;

    private boolean essenceInInventory = false;

    private final Set<Pouch> pouchesInInventory = EnumSet.noneOf(Pouch.class);
    private final Set<Pouch> pouchesWithEssence = EnumSet.noneOf(Pouch.class);

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

        pouchesInInventory.clear();
        for (var pouch : Pouch.values())
            if (inventory.contains(pouch.itemId) || (pouch.degradedItemId != -1 && inventory.contains(pouch.degradedItemId)))
                pouchesInInventory.add(pouch);
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event)
    {
        Pouch pouch = Pouch.getByVarbit(event.getVarbitId());

        if (pouch == null)
            return;

        int value = event.getValue();
        boolean hasEssence = (value == POUCH_TYPE_PURE || value == POUCH_TYPE_DAEYALT);

        if (hasEssence)
            pouchesWithEssence.add(pouch);
        else
            pouchesWithEssence.remove(pouch);
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
        if (essenceInInventory)
            return true;

        for (var pouch : pouchesInInventory)
            if (pouchesWithEssence.contains(pouch))
                return true;

        return false;
    }
}
