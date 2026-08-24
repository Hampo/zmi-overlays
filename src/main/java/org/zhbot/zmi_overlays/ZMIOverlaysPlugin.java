package org.zhbot.zmi_overlays;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.PostMenuSort;
import net.runelite.api.events.WorldChanged;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.WorldService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;
import org.zhbot.zmi_overlays.enums.Pouch;
import org.zhbot.zmi_overlays.overlays.ItemOverlays;
import org.zhbot.zmi_overlays.overlays.Infobox;
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
	private ClientThread clientThread;

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
	private Infobox infobox;

	private boolean zmiWorld = false;
	private boolean inOuraniaArea = false;

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

		overlayManager.add(infobox);
		eventBus.register(infobox);

		worldOverlays.startup();

		clientThread.invoke(this::checkZMIWorld);
	}

	@Override
	protected void shutDown() throws Exception
	{
		pouchUtils.cleanup();
		worldOverlays.cleanup();
		infobox.cleanup();

		eventBus.unregister(pouchUtils);

		overlayManager.remove(worldOverlays);
		eventBus.unregister(worldOverlays);

		overlayManager.remove(itemOverlays);
		eventBus.unregister(itemOverlays);

		overlayManager.remove(spellOverlays);
		eventBus.unregister(spellOverlays);

		overlayManager.remove(infobox);
		eventBus.unregister(infobox);
	}

	@Provides
	ZMIOverlaysConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ZMIOverlaysConfig.class);
	}

	@Subscribe
	public void onWorldChanged(WorldChanged event)
	{
		checkZMIWorld();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		var localPlayer = client.getLocalPlayer();
		if (localPlayer == null)
		{
			inOuraniaArea = false;
			return;
		}

		var regionID = localPlayer.getWorldLocation().getRegionID();

		inOuraniaArea = regionID == OURANIA_CAVE_REGION_ID || regionID == OURANIA_SURFACE_REGION_ID;
	}

	@Subscribe
	public void onPostMenuSort(PostMenuSort event)
	{
		var menu = client.getMenu();

		var entries = menu.getMenuEntries();
		for (var entry : entries)
		{
			var target = Text.removeTags(entry.getTarget());
			var pouch = Pouch.getByName(target);
			if (pouch == null)
				continue;

			var option = Text.removeTags(entry.getOption());
			switch (option)
			{
				case "Empty":
					if (!isBankOpen())
						continue;
				case "Empty-to-inventory":
					if (!config.emptyDisable())
						continue;

					menu.removeMenuEntry(entry);
					break;
				case "Fill":
					if (!config.fillDisable())
						continue;

					if (isBankOpen())
						continue;

					menu.removeMenuEntry(entry);
					break;
			}
		}
	}

	public boolean outsideOuraniaArea()
	{
		return (config.ZMIWorldsOnly() && !zmiWorld) || !inOuraniaArea;
	}

	private boolean isBankOpen()
	{
		var bank = client.getWidget(InterfaceID.Bankmain.UNIVERSE);
		return bank != null && !bank.isHidden();
	}

	private void checkZMIWorld()
	{
		zmiWorld = false;

		var worlds = worldService.getWorlds();
		if (worlds == null)
			return;

		var world = worlds.findWorld(client.getWorld());
		if (world == null)
			return;

		zmiWorld = world.getActivity().equalsIgnoreCase("Ourania Altar");
	}
}
