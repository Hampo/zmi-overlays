package org.zhbot.zmi_overlays;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ZMIOverlaysPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ZMIOverlaysPlugin.class);
		RuneLite.main(args);
	}
}