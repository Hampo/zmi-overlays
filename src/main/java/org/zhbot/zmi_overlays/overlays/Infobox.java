package org.zhbot.zmi_overlays.overlays;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import org.zhbot.zmi_overlays.ZMIOverlaysConfig;
import org.zhbot.zmi_overlays.ZMIOverlaysPlugin;

import javax.inject.Inject;
import java.util.List;
import java.awt.*;
import java.util.*;

public class Infobox extends OverlayPanel {
    private static final Integer[] RUNE_IDS_ARRAY = {
            ItemID.AIRRUNE,
            ItemID.MINDRUNE,
            ItemID.WATERRUNE,
            ItemID.EARTHRUNE,
            ItemID.FIRERUNE,
            ItemID.BODYRUNE,
            ItemID.COSMICRUNE,
            ItemID.CHAOSRUNE,
            ItemID.ASTRALRUNE,
            ItemID.NATURERUNE,
            ItemID.LAWRUNE,
            ItemID.DEATHRUNE,
            ItemID.BLOODRUNE,
            ItemID.SOULRUNE
    };
    private static final Set<Integer> RUNE_IDS = ImmutableSet.copyOf(RUNE_IDS_ARRAY);

    private static final int[] RUNEPOUCH_RUNE_VARBITS = {
            VarbitID.RUNE_POUCH_TYPE_1,
            VarbitID.RUNE_POUCH_TYPE_2,
            VarbitID.RUNE_POUCH_TYPE_3,
            VarbitID.RUNE_POUCH_TYPE_4,
            VarbitID.RUNE_POUCH_TYPE_5,
            VarbitID.RUNE_POUCH_TYPE_6
    };

    private static final int[] RUNEPOUCH_AMOUNT_VARBITS = {
            VarbitID.RUNE_POUCH_QUANTITY_1,
            VarbitID.RUNE_POUCH_QUANTITY_2,
            VarbitID.RUNE_POUCH_QUANTITY_3,
            VarbitID.RUNE_POUCH_QUANTITY_4,
            VarbitID.RUNE_POUCH_QUANTITY_5,
            VarbitID.RUNE_POUCH_QUANTITY_6
    };

    private final Client client;
    private final ItemManager itemManager;
    private final ZMIOverlaysPlugin plugin;
    private final ZMIOverlaysConfig config;

    private final Map<Integer, Integer> sessionRunes = new LinkedHashMap<>();

    private Map<Integer, Integer> previousRuneState = new HashMap<>();
    private boolean craftedThisTick = false;
    private boolean justHopped = false;

    private long lapStartTime = -1;
    private final List<Long> lapTimes = new ArrayList<>();

    @Inject
    private Infobox(Client client, ItemManager itemManager, ZMIOverlaysPlugin plugin, ZMIOverlaysConfig config)
    {
        this.client = client;
        this.itemManager = itemManager;
        this.plugin = plugin;
        this.config = config;

        setPosition(OverlayPosition.TOP_LEFT);
        setResizable(false);

        for (var runeID : RUNE_IDS_ARRAY)
            sessionRunes.put(runeID, 0);

        addMenuEntry(MenuAction.RUNELITE_OVERLAY, "Reset", "Lap Times", (entry) ->
        {
            lapStartTime = -1;
            lapTimes.clear();

            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ff0000>[ZMI Overlays] Reset lap times.</col>", null);
        });

        addMenuEntry(MenuAction.RUNELITE_OVERLAY, "Reset", "Runes", (entry) ->
        {
            for (var runeID : RUNE_IDS_ARRAY)
                sessionRunes.put(runeID, 0);

            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ff0000>[ZMI Overlays] Reset runes.</col>", null);
        });
    }

    public void cleanup()
    {
        sessionRunes.clear();
        previousRuneState.clear();
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.infoboxEnabled())
            return null;

        if (plugin.outsideOuraniaArea())
            return null;

        panelComponent.getChildren().clear();
        panelComponent.setPreferredSize(new Dimension(RuneGridComponent.ICON_SIZE * RuneGridComponent.ICONS_PER_ROW + RuneGridComponent.PADDING_X * 2, 0));

        panelComponent.getChildren().add(TitleComponent.builder()
                .text("ZMI")
                .color(Color.GREEN)
                .build());

        if (config.infoboxShowCurrentLap())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Current Lap")
                    .right(lapStartTime == -1 ? "-" : String.valueOf(((System.currentTimeMillis() - lapStartTime) / 1000)))
                    .build());
        }

        if (config.infoboxShowPreviousLap())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Previous Lap")
                    .right(lapTimes.isEmpty() ? "-" : String.format("%.3f", (lapTimes.get(lapTimes.size() - 1) / 1000.0)))
                    .build());
        }

        if (config.infoboxShowAverageLap())
        {
            var average = "-";

            if (!lapTimes.isEmpty())
            {
                var total = 0L;

                for (var lapTime : lapTimes)
                    total += lapTime;

                average = String.format("%.3f", (total / (double)lapTimes.size()) / 1000.0);
            }

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Average Lap")
                    .right(average)
                    .build());
        }

        if (config.infoboxShowTotalLaps())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Total Laps")
                    .right(String.valueOf(lapTimes.size()))
                    .build());
        }

        if (config.runesPanelShow())
        {

            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Runes Crafted")
                    .color(Color.GREEN)
                    .build());

            panelComponent.getChildren().add(new RuneGridComponent(itemManager, sessionRunes));
        }

        return super.render(graphics);
    }

    @Subscribe
    public void onStatChanged(StatChanged event)
    {
        if (plugin.outsideOuraniaArea())
            return;

        if (event.getSkill() != Skill.RUNECRAFT)
            return;

        craftedThisTick = true;
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN || event.getGameState() == GameState.HOPPING)
            justHopped = true;
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (plugin.outsideOuraniaArea())
        {
            lapStartTime = -1;
            return;
        }

        var currentState = getCurrentRuneState();

        if (justHopped) {
            previousRuneState = currentState;
            craftedThisTick = false;
            justHopped = false;
            return;
        }

        if (craftedThisTick)
        {
            for (var entry : currentState.entrySet())
            {
                var itemId = entry.getKey();
                var currentAmount = entry.getValue();
                var previousAmount = previousRuneState.getOrDefault(itemId, 0);

                if (currentAmount <= previousAmount)
                    continue;

                var diff = currentAmount - previousAmount;
                sessionRunes.merge(itemId, diff, Integer::sum);
            }

            craftedThisTick = false;
        }

        previousRuneState = currentState;
    }

    @Subscribe
    public void onWidgetClosed(WidgetClosed event)
    {
        if (plugin.outsideOuraniaArea())
            return;

        if (event.getGroupId() != InterfaceID.BANKMAIN)
            return;

        if (lapStartTime == -1)
        {
            lapStartTime = System.currentTimeMillis();
        }
        else
        {
            var now = System.currentTimeMillis();
            var lapTime = now - lapStartTime;

            lapTimes.add(lapTime);
            lapStartTime = now;
        }
    }

    private Map<Integer, Integer> getCurrentRuneState()
    {
        Map<Integer, Integer> state = new HashMap<>();

        var inventory = client.getItemContainer(InventoryID.INV);
        if (inventory != null)
        {
            for (var item : inventory.getItems())
            {
                var id = item.getId();
                if (RUNE_IDS.contains(id))
                    state.merge(id, item.getQuantity(), Integer::sum);
            }
        }

        for (var i = 0; i < RUNEPOUCH_RUNE_VARBITS.length; i++)
        {
            var runeId = client.getVarbitValue(RUNEPOUCH_RUNE_VARBITS[i]);
            var amount = client.getVarbitValue(RUNEPOUCH_AMOUNT_VARBITS[i]);

            if (runeId <= 0 || amount <= 0)
                continue;

            var itemId = client.getEnum(EnumID.RUNEPOUCH_RUNE).getIntValue(runeId);
            if (!RUNE_IDS.contains(itemId))
                continue;

            state.merge(itemId, amount, Integer::sum);
        }

        return state;
    }
}
