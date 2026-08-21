package org.zhbot.zmi_overlays;

import net.runelite.client.config.*;
import org.zhbot.zmi_overlays.enums.Food;
import org.zhbot.zmi_overlays.enums.NPCRenderMode;
import org.zhbot.zmi_overlays.enums.ObjectRenderMode;

import java.awt.*;

@ConfigGroup(ZMIOverlaysConfig.group)
public interface ZMIOverlaysConfig extends Config
{
	String group = "zmi-overlays";

	@ConfigItem(
			keyName = "zmiWorldsOnly",
			name = "ZMI Worlds Only",
			description = "Only enable overlays on ZMI worlds",
			position = 0
	)
	default boolean ZMIWorldsOnly()
	{
		return true;
	}

	@ConfigSection(
			name = "Runes Panel",
			description = "Configure the runes panel",
			position = 1
	)
	String runesPanelSection = "runesPanelSection";

	@ConfigItem(
			keyName = "runesPanelShow",
			name = "Show",
			description = "Show the runes panel",
			section = runesPanelSection,
			position = 0
	)
	default boolean runesPanelShow()
	{
		return true;
	}

	@ConfigSection(
			name = "Altar Overlay",
			description = "Configure the altar overlay",
			position = 2
	)
	String altarOverlaySection = "altarOverlaySection";

	@ConfigItem(
			keyName = "altarShow",
			name = "Show",
			description = "Show the altar overlay",
			section = altarOverlaySection,
			position = 0
	)
	default boolean altarShow()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "altarColour",
			name = "Colour",
			description = "The overlay colour for the altar",
			section = altarOverlaySection,
			position = 1
	)
	default Color altarColour()
	{
		return new Color(0, 255, 0, 50);
	}

	@ConfigItem(
			keyName = "altarRenderMode",
			name = "Render Mode",
			description = "The render mode for the altar",
			section = altarOverlaySection,
			position = 2
	)
	default ObjectRenderMode altarRenderMode()
	{
		return ObjectRenderMode.CLICKBOX;
	}

	@ConfigSection(
			name = "Entrance Ladder Overlay",
			description = "Configure the entrance ladder overlay",
			position = 3
	)
	String entranceLadderOverlaySection = "entranceLadderOverlaySection";

	@ConfigItem(
			keyName = "entranceLadderShow",
			name = "Show",
			description = "Show the entrance ladder overlay",
			section = entranceLadderOverlaySection,
			position = 0
	)
	default boolean entranceLadderShow()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "entranceLadderColour",
			name = "Colour",
			description = "The overlay colour for the entrance ladder",
			section = entranceLadderOverlaySection,
			position = 1
	)
	default Color entranceLadderColour()
	{
		return new Color(0, 255, 0, 50);
	}

	@ConfigItem(
			keyName = "entranceLadderRenderMode",
			name = "Render Mode",
			description = "The render mode for the entrance ladder",
			section = entranceLadderOverlaySection,
			position = 2
	)
	default ObjectRenderMode entranceLadderRenderMode()
	{
		return ObjectRenderMode.CLICKBOX;
	}

	@ConfigSection(
			name = "Eniola (Banker) Overlay",
			description = "Configure the Eniola (banker) overlay",
			position = 4
	)
	String eniolaOverlaySection = "eniolaOverlaySection";

	@ConfigItem(
			keyName = "eniolaShow",
			name = "Show",
			description = "Show the Eniola overlay",
			section = eniolaOverlaySection,
			position = 0
	)
	default boolean eniolaShow()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "eniolaColour",
			name = "Colour",
			description = "The overlay colour for Eniola",
			section = eniolaOverlaySection,
			position = 1
	)
	default Color eniolaColour()
	{
		return new Color(0, 255, 255, 50);
	}

	@ConfigItem(
			keyName = "eniolaRenderMode",
			name = "Render Mode",
			description = "The render mode for Eniola",
			section = eniolaOverlaySection,
			position = 2
	)
	default NPCRenderMode eniolaRenderMode()
	{
		return NPCRenderMode.HULL;
	}

	@ConfigSection(
			name = "Enemy Overlay",
			description = "Configure the enemy overlay",
			position = 5
	)
	String enemyOverlaySection = "enemyOverlaySection";

	@ConfigItem(
			keyName = "enemyShow",
			name = "Show",
			description = "Show the enemy overlay",
			section = enemyOverlaySection,
			position = 0
	)
	default boolean enemyShow()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
			keyName = "enemyColour",
			name = "Colour",
			description = "The overlay colour for enemies",
			section = enemyOverlaySection,
			position = 1
	)
	default Color enemyColour()
	{
		return new Color(255, 0, 0, 50);
	}

	@ConfigItem(
			keyName = "enemyRenderMode",
			name = "Render Mode",
			description = "The render mode for enemies",
			section = enemyOverlaySection,
			position = 2
	)
	default NPCRenderMode enemyRenderMode()
	{
		return NPCRenderMode.TILE;
	}

	@ConfigSection(
			name = "Pouch Overlay",
			description = "Configure the pouch overlay",
			position = 6
	)
	String pouchOverlaySection = "pouchOverlaySection";

	@ConfigItem(
			keyName = "pouchShow",
			name = "Show",
			description = "Show the pouch overlay",
			section = pouchOverlaySection,
			position = 0
	)
	default boolean pouchShow()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "pouchColour",
			name = "Colour",
			description = "The overlay colour for pouches",
			section = pouchOverlaySection,
			position = 1
	)
	default Color pouchColour()
	{
		return new Color(0, 255, 255, 50);
	}

	@Alpha
	@ConfigItem(
			keyName = "pouchEmptyColour",
			name = "Empty Colour",
			description = "The overlay colour for empty pouches",
			section = pouchOverlaySection,
			position = 2
	)
	default Color pouchEmptyColour()
	{
		return new Color(255, 0, 0, 50);
	}

	@ConfigSection(
			name = "Essence Overlay",
			description = "Configure the essence overlay",
			position = 7
	)
	String essenceOverlaySection = "essenceOverlaySection";

	@ConfigItem(
			keyName = "essenceShow",
			name = "Show",
			description = "Show the essence overlay",
			section = essenceOverlaySection,
			position = 0
	)
	default boolean essenceShow()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "essenceColour",
			name = "Colour",
			description = "The overlay colour for essence",
			section = essenceOverlaySection,
			position = 1
	)
	default Color essenceColour()
	{
		return new Color(0, 255, 255, 50);
	}

	@ConfigSection(
			name = "Food Overlay",
			description = "Configure the food overlay",
			position = 8
	)
	String foodOverlaySection = "foodOverlaySection";

	@ConfigItem(
			keyName = "foodShow",
			name = "Show",
			description = "Which food to overlay",
			section = foodOverlaySection,
			position = 0
	)
	default Food foodShow()
	{
		return Food.MONKFISH;
	}

	@Alpha
	@ConfigItem(
			keyName = "foodColour",
			name = "Colour",
			description = "The overlay colour for food",
			section = foodOverlaySection,
			position = 1
	)
	default Color foodColour()
	{
		return new Color(0, 255, 0, 50);
	}

	@ConfigSection(
			name = "Stamina Overlay",
			description = "Configure the stamina potion overlay",
			position = 9
	)
	String staminaOverlaySection = "staminaOverlaySection";

	@ConfigItem(
			keyName = "staminaShow",
			name = "Show",
			description = "Show the stamina potion overlay",
			section = staminaOverlaySection,
			position = 0
	)
	default boolean staminaShow()
	{
		return true;
	}

	@Units(Units.PERCENT)
	@Range(max = 100)
	@ConfigItem(
			keyName = "staminaThreshold",
			name = "Threshold",
			description = "The threshold to indicate drinking a stamina potion",
			section = staminaOverlaySection,
			position = 1
	)
	default int staminaThreshold()
	{
		return 75;
	}

	@Alpha
	@ConfigItem(
			keyName = "staminaColour",
			name = "Colour",
			description = "The overlay colour for stamina potions",
			section = staminaOverlaySection,
			position = 2
	)
	default Color staminaColour()
	{
		return new Color(255, 255, 0, 50);
	}

	@ConfigSection(
			name = "Teleport Overlay",
			description = "Configure the teleport overlay",
			position = 10
	)
	String teleportOverlaySection = "teleportOverlaySection";

	@ConfigItem(
			keyName = "teleportShow",
			name = "Show",
			description = "Show the teleport overlay",
			section = teleportOverlaySection,
			position = 0
	)
	default boolean teleportShow()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "teleportColour",
			name = "Colour",
			description = "The overlay colour for the teleport",
			section = teleportOverlaySection,
			position = 1
	)
	default Color teleportColour()
	{
		return new Color(0, 255, 0, 50);
	}

	@Alpha
	@ConfigItem(
			keyName = "teleportBadColour",
			name = "Bad Colour",
			description = "The overlay colour for the teleport when you still have essence",
			section = teleportOverlaySection,
			position = 2
	)
	default Color teleportBadColour()
	{
		return new Color(255, 0, 0, 50);
	}

	@ConfigSection(
			name = "Teleport Disable",
			description = "Configure the teleport disable",
			position = 11
	)
	String teleportDisableSection = "teleportDisableSection";

	@ConfigItem(
			keyName = "teleportDisable",
			name = "Disable",
			description = "Disables the teleport spell when you still have essence",
			section = teleportDisableSection,
			position = 0
	)
	default boolean teleportDisable()
	{
		return true;
	}
}
