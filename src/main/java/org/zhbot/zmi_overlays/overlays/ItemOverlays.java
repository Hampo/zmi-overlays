package org.zhbot.zmi_overlays.overlays;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import org.zhbot.zmi_overlays.ZMIOverlaysConfig;
import org.zhbot.zmi_overlays.ZMIOverlaysPlugin;
import org.zhbot.zmi_overlays.enums.Pouch;
import org.zhbot.zmi_overlays.utils.GraphicsUtils;

import javax.inject.Inject;
import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemOverlays extends WidgetItemOverlay {
    private static final Set<Integer> ESSENCE_IDS = ImmutableSet.of(
            ItemID.BLANKRUNE_HIGH,
            ItemID.BLANKRUNE_DAEYALT
    );

    private static final Set<Integer> STAMINA_IDS = ImmutableSet.of(
            ItemID._1DOSESTAMINA,
            ItemID._2DOSESTAMINA,
            ItemID._3DOSESTAMINA,
            ItemID._4DOSESTAMINA,
            ItemID._1DOSE2STAMINA,
            ItemID._2DOSE2STAMINA,
            ItemID._3DOSE2STAMINA,
            ItemID._4DOSE2STAMINA,
            ItemID.BRUTAL_1DOSESTAMINA,
            ItemID.BRUTAL_2DOSESTAMINA
    );

    private final Client client;
    private final ZMIOverlaysPlugin plugin;
    private final ZMIOverlaysConfig config;
    private final GraphicsUtils graphicsUtils;

    private boolean hasStamina = false;
    private final Map<Pouch, Integer> pouchContents = new EnumMap<>(Pouch.class);

    @Inject
    private ItemOverlays(Client client, ZMIOverlaysPlugin plugin, ZMIOverlaysConfig config, GraphicsUtils graphicsUtils)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.graphicsUtils = graphicsUtils;

        showOnBank();
        showOnInventory();
        setPriority(2f);
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem) {
        if (plugin.outsideOuraniaArea())
            return;

        var pouch = Pouch.getByItemId(itemId);
        if (pouch != null)
        {
            if (!config.pouchShow())
                return;

            var interfaceID = WidgetUtil.componentToInterface(widgetItem.getWidget().getId());
            if (interfaceID != InterfaceID.INVENTORY)
                return;

            var empty = pouchContents.getOrDefault(pouch, 0) == 0;
            graphicsUtils.renderBox(graphics, widgetItem, empty ? config.pouchEmptyColour() : config.pouchColour());

            return;
        }

        if (ESSENCE_IDS.contains(itemId))
        {
            if (!config.essenceShow())
                return;

            var interfaceID = WidgetUtil.componentToInterface(widgetItem.getWidget().getId());
            if (interfaceID != InterfaceID.BANKMAIN)
                return;

            graphicsUtils.renderBox(graphics, widgetItem, config.essenceColour());

            return;
        }

        if (STAMINA_IDS.contains(itemId))
        {
            if (!config.staminaShow())
                return;

            if (hasStamina)
                return;

            if (client.getEnergy() / 100 > config.staminaThreshold())
                return;

            graphicsUtils.renderBox(graphics, widgetItem, config.staminaColour());

            return;
        }

        var food = config.foodShow();
        if (food.getId() == itemId)
        {
            var currentHealth = client.getBoostedSkillLevel(Skill.HITPOINTS);
            var maxHealth = client.getRealSkillLevel(Skill.HITPOINTS);

            if ((maxHealth - currentHealth) > food.getHeals())
                graphicsUtils.renderBox(graphics, widgetItem, config.foodColour());
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event)
    {
        var id = event.getVarbitId();

        if (id == VarbitID.STAMINA_ACTIVE)
        {
            hasStamina = event.getValue() != 0;
            return;
        }

        var pouch = Pouch.getByVarbit(id);
        if (pouch == null)
            return;

        pouchContents.put(pouch, event.getValue());
    }
}
