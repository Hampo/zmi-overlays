package org.zhbot.zmi_overlays;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.WorldService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.zhbot.zmi_overlays.overlays.ItemOverlays;
import org.zhbot.zmi_overlays.overlays.RunesPanel;
import org.zhbot.zmi_overlays.overlays.SpellOverlays;
import org.zhbot.zmi_overlays.overlays.WorldOverlays;
import org.zhbot.zmi_overlays.utils.PouchUtils;

@Slf4j
@PluginDescriptor(
	name = "ZMI Overlays"
)
public class ZMIOverlaysPlugin extends Plugin
{
	private static final int OURANIA_CAVE_REGION_ID = 12119;
	private static final int OURANIA_SURFACE_REGION_ID = 9778;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private EventBus eventBus;

	@Inject
	private WorldService worldService;

	@Inject
	private ZMIOverlaysConfig config;

	@Inject
	private PouchUtils pouchUtils;

	@Inject
	private WorldOverlays worldOverlays;

	@Inject
	private ItemOverlays itemOverlays;

	@Inject
	private SpellOverlays spellOverlays;

	@Inject
	private RunesPanel runesPanel;

	private boolean zmiWorld = false;

	@Override
	protected void startUp() throws Exception
	{
		eventBus.register(pouchUtils);

		overlayManager.add(worldOverlays);
		eventBus.register(worldOverlays);

		overlayManager.add(itemOverlays);
		eventBus.register(itemOverlays);

		overlayManager.add(spellOverlays);
		eventBus.register(spellOverlays);

		overlayManager.add(runesPanel);
		eventBus.register(runesPanel);

		worldOverlays.startup();
	}

	@Override
	protected void shutDown() throws Exception
	{
		pouchUtils.cleanup();
		worldOverlays.cleanup();
		runesPanel.cleanup();

		eventBus.unregister(pouchUtils);

		overlayManager.remove(worldOverlays);
		eventBus.unregister(worldOverlays);

		overlayManager.remove(itemOverlays);
		eventBus.unregister(itemOverlays);

		overlayManager.remove(spellOverlays);
		eventBus.unregister(spellOverlays);

		overlayManager.remove(runesPanel);
		eventBus.unregister(runesPanel);
	}

	@Provides
	ZMIOverlaysConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ZMIOverlaysConfig.class);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
			return;

		zmiWorld = false;

		var worlds = worldService.getWorlds();
		if (worlds == null)
			return;

		var world = worlds.findWorld(client.getWorld());
		if (world == null)
			return;

		zmiWorld = world.getActivity().equalsIgnoreCase("Ourania Altar");
	}

	public boolean outsideOuraniaArea()
	{
		if (config.ZMIWorldsOnly() && !zmiWorld)
			return true;

		var localPlayer = client.getLocalPlayer();
		if (localPlayer == null)
			return true;

		var regionID = localPlayer.getWorldLocation().getRegionID();

		return regionID != OURANIA_CAVE_REGION_ID && regionID != OURANIA_SURFACE_REGION_ID;
	}
}
