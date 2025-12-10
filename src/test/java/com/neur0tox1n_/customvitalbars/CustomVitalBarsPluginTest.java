package net.runelite.client.plugins.customvitalbars.src.test.java.com.neur0tox1n_.customvitalbars;


import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;
import com.neur0tox1n_.customvitalbars.CustomVitalBarsPlugin;

public class CustomVitalBarsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CustomVitalBarsPlugin.class);
		RuneLite.main(args);
	}
}