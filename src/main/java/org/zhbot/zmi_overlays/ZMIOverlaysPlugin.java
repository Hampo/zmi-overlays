package org.zhbot.zmi_overlays;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.zhbot.zmi_overlays.overlays.ItemOverlays;
import org.zhbot.zmi_overlays.overlays.RunesPanel;
import org.zhbot.zmi_overlays.overlays.SpellOverlays;
import org.zhbot.zmi_overlays.overlays.WorldOverlays;

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
	private WorldOverlays worldOverlays;

	@Inject
	private ItemOverlays itemOverlays;

	@Inject
	private SpellOverlays spellOverlays;

	@Inject
	private RunesPanel runesPanel;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(worldOverlays);
		eventBus.register(worldOverlays);

		overlayManager.add(itemOverlays);
		eventBus.register(itemOverlays);

		overlayManager.add(spellOverlays);

		overlayManager.add(runesPanel);
		eventBus.register(runesPanel);

		worldOverlays.startup();
	}

	@Override
	protected void shutDown() throws Exception
	{
		worldOverlays.cleanup();
		runesPanel.cleanup();

		overlayManager.remove(worldOverlays);
		eventBus.unregister(worldOverlays);

		overlayManager.remove(itemOverlays);
		eventBus.unregister(itemOverlays);

		overlayManager.remove(spellOverlays);

		overlayManager.remove(runesPanel);
		eventBus.unregister(runesPanel);
	}

	@Provides
	ZMIOverlaysConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ZMIOverlaysConfig.class);
	}

	public boolean outsideOuraniaArea()
	{
		var localPlayer = client.getLocalPlayer();
		if (localPlayer == null)
			return true;

		var regionID = localPlayer.getWorldLocation().getRegionID();

		return regionID != OURANIA_CAVE_REGION_ID && regionID != OURANIA_SURFACE_REGION_ID;
	}
}
