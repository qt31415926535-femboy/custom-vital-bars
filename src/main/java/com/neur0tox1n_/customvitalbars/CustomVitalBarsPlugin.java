/*
 * Copyright (c) 2019, Sean Dewar <https://github.com/seandewar>
 * Copyright (c) 2018, Abex
 * Copyright (c) 2018, Zimaya <https://github.com/Zimaya>
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Jos <Malevolentdev@gmail.com>
 * Copyright (c) 2024, Seung <swhahm94@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.neur0tox1n_.customvitalbars;

import javax.inject.Inject;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.itemstats.ItemStatPlugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Set;

import static net.runelite.api.ItemID.*;
import static net.runelite.api.ItemID.MAX_CAPE;

@PluginDescriptor(
	name = "Custom Vital Bars",
	description = "Draws configurable bars showing HP, Prayer, Run energy, Special energy, Warmth, and their respective restoration amounts",
		tags = {"status","vital","vitals","custom","bar","health","hp","hitpoints","pray","run","energy","stamina","special","combat","regen","heal","warmth","wintertodt","Hunter","food","potion"}
)
@PluginDependency(ItemStatPlugin.class)
public class CustomVitalBarsPlugin extends Plugin
{
	@Inject
	private CustomVitalBarsHitpointsOverlay healthOverlay;
	@Inject
	private CustomVitalBarsPrayerOverlay prayerOverlay;
	@Inject
	private CustomVitalBarsEnergyOverlay energyOverlay;
	@Inject
	private CustomVitalBarsSpecialOverlay specialOverlay;
	@Inject
	private CustomVitalBarsWarmthOverlay warmthOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private Client client;

	@Inject
	private CustomVitalBarsConfig config;

	@Inject
	private ClientThread clientThread;

	@Getter(AccessLevel.PACKAGE)
	private boolean hitpointsDisplayed;
    @Getter(AccessLevel.PACKAGE)
	private boolean prayerDisplayed;
    @Getter(AccessLevel.PACKAGE)
	private boolean energyDisplayed;
    @Getter(AccessLevel.PACKAGE)
	private boolean specialDisplayed;
    @Getter(AccessLevel.PACKAGE)
	private boolean warmthDisplayed;
	private boolean hideWhenBigUIOpen;

	private int lastCombatActionTickCount;


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomVitalBarsComponent.class);

	@Override
	protected void startUp() throws Exception
	{
		clientThread.invokeLater(this::checkCustomVitalBars);
		overlayManager.add( healthOverlay );
		overlayManager.add( prayerOverlay );
		overlayManager.add( energyOverlay );
		overlayManager.add( specialOverlay );
		overlayManager.add( warmthOverlay );
		hideWhenBigUIOpen = config.hideWhenLargeInterfacePanelsOpen();
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove( healthOverlay );
		overlayManager.remove( prayerOverlay );
		overlayManager.remove( energyOverlay );
		overlayManager.remove( specialOverlay );
		overlayManager.remove( warmthOverlay );
		hitpointsDisplayed = false;
		prayerDisplayed = false;
		energyDisplayed = false;
		specialDisplayed = false;
		warmthDisplayed = false;
	}

	@Provides
	CustomVitalBarsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CustomVitalBarsConfig.class);
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged ev)
	{
		healthOverlay.onGameStateChanged( ev );
		energyOverlay.onGameStateChanged( ev );
		specialOverlay.onGameStateChanged( ev );
	}

	@Subscribe
	public void onItemContainerChanged( ItemContainerChanged event )
	{
		healthOverlay.onItemContainerChanged( event );
		specialOverlay.onItemContainerChanged( event );
		prayerOverlay.onItemContainerChanged( event );
		warmthOverlay.onItemContainerChanged( event );
	}

	@Subscribe
	private void onVarbitChanged(VarbitChanged ev)
	{
		healthOverlay.onVarbitChanged( ev );
		prayerOverlay.onVarbitChanged( ev );
		energyOverlay.onVarbitChanged( ev );
		warmthOverlay.onVarbitChanged( ev );
	}

	@Subscribe
	public void onGameTick( GameTick gameTick )
	{
		checkCustomVitalBars();
		healthOverlay.onGameTick( gameTick );
		specialOverlay.onGameTick( gameTick );
		prayerOverlay.onGameTick( gameTick );
		energyOverlay.onGameTick( gameTick );
		warmthOverlay.onGameTick( gameTick );
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if ( CustomVitalBarsConfig.GROUP.equals(event.getGroup()) && event.getKey().contains( "AfterCombatDelay" ) )
        {
			clientThread.invokeLater( this::checkCustomVitalBars );
		}
        if ( CustomVitalBarsConfig.GROUP.equals(event.getGroup()) && event.getKey().equals("hideWhenLargeInterfacePanelsOpen") )
        {
            hideWhenBigUIOpen = config.hideWhenLargeInterfacePanelsOpen();
        }

        if ( CustomVitalBarsConfig.GROUP.equals(event.getGroup()) )
        {
            healthOverlay.onConfigChanged( event );
            specialOverlay.onConfigChanged( event );
            prayerOverlay.onConfigChanged( event );
            energyOverlay.onConfigChanged( event );
            warmthOverlay.onConfigChanged( event );
        }
	}

	@Subscribe
	public void onWidgetLoaded( WidgetLoaded widgetLoaded )
	{
		if ( !hideWhenBigUIOpen )
		{
			return;
		}

		if (    widgetLoaded.getGroupId() == InterfaceID.BANK ||
				widgetLoaded.getGroupId() == InterfaceID.BANK_INVENTORY ||
				widgetLoaded.getGroupId() == InterfaceID.BANK_PIN ||
				widgetLoaded.getGroupId() == InterfaceID.DEPOSIT_BOX ||
				widgetLoaded.getGroupId() == InterfaceID.FAIRY_RING_PANEL ||
				widgetLoaded.getGroupId() == InterfaceID.SEED_VAULT_INVENTORY ||
				widgetLoaded.getGroupId() == InterfaceID.ACHIEVEMENT_DIARY_SCROLL ||
				widgetLoaded.getGroupId() == InterfaceID.ADVENTURE_LOG ||
				widgetLoaded.getGroupId() == InterfaceID.BARROWS_PUZZLE ||
				widgetLoaded.getGroupId() == InterfaceID.CHAMBERS_OF_XERIC_STORAGE_UNIT_PRIVATE ||
				widgetLoaded.getGroupId() == InterfaceID.CHAMBERS_OF_XERIC_STORAGE_UNIT_SHARED ||
				widgetLoaded.getGroupId() == InterfaceID.GROUP_STORAGE ||
				widgetLoaded.getGroupId() == InterfaceID.GROUP_STORAGE_INVENTORY ||
				widgetLoaded.getGroupId() == InterfaceID.GENERIC_SCROLL ||
				widgetLoaded.getGroupId() == InterfaceID.CLUESCROLL ||
				widgetLoaded.getGroupId() == InterfaceID.CLUESCROLL_REWARD ||
				widgetLoaded.getGroupId() == InterfaceID.TRADE_INVENTORY ||
				widgetLoaded.getGroupId() == InterfaceID.SHOP_INVENTORY ||
				widgetLoaded.getGroupId() == InterfaceID.DUEL_INVENTORY ||
				widgetLoaded.getGroupId() == InterfaceID.GRAND_EXCHANGE_INVENTORY ||
				widgetLoaded.getGroupId() == InterfaceID.GUIDE_PRICES_INVENTORY ||
				widgetLoaded.getGroupId() == InterfaceID.EQUIPMENT_INVENTORY ||
				widgetLoaded.getGroupId() == InterfaceID.KEPT_ON_DEATH ||
				widgetLoaded.getGroupId() == InterfaceID.COLLECTION_LOG ||
				widgetLoaded.getGroupId() == InterfaceID.KILL_LOG ||
				widgetLoaded.getGroupId() == InterfaceID.WORLD_MAP )
		{
			healthOverlay.onWidgetLoaded(widgetLoaded);
			prayerOverlay.onWidgetLoaded(widgetLoaded);
			energyOverlay.onWidgetLoaded(widgetLoaded);
			specialOverlay.onWidgetLoaded(widgetLoaded);
			warmthOverlay.onWidgetLoaded(widgetLoaded);
		}
	}

	@Subscribe
	public void onWidgetClosed( WidgetClosed widgetClosed )
	{
		healthOverlay.onWidgetClosed( widgetClosed );
		prayerOverlay.onWidgetClosed( widgetClosed );
		energyOverlay.onWidgetClosed( widgetClosed );
		specialOverlay.onWidgetClosed( widgetClosed );
		warmthOverlay.onWidgetClosed( widgetClosed );
	}

	@Subscribe
	public void onMenuOptionClicked( MenuOptionClicked event )
	{
		healthOverlay.onMenuOptionClicked( event );
        specialOverlay.onMenuOptionClicked( event );
	}

	private void checkCustomVitalBars()
	{
		final Player localPlayer = client.getLocalPlayer();
		if (localPlayer == null)
		{
			return;
		}

		final Actor interacting = localPlayer.getInteracting();
        int hideHitpointAfterCombatDelay = config.hideHitpointsAfterCombatDelay();
        int hidePrayerAfterCombatDelay = config.hidePrayerAfterCombatDelay();
        int hideEnergyAfterCombatDelay = config.hideEnergyAfterCombatDelay();
        int hideSpecialAfterCombatDelay = config.hideSpecialAfterCombatDelay();
        int hideWarmthAfterCombatDelay = config.hideWarmthAfterCombatDelay();
        boolean doInteractionCheck = false;

        if ( hideHitpointAfterCombatDelay == 0 )
        {
            hitpointsDisplayed = true;
        }
        else
        {
            doInteractionCheck = true;
        }
        if ( hidePrayerAfterCombatDelay == 0 )
        {
            prayerDisplayed = true;
        }
        else
        {
            doInteractionCheck = true;
        }
        if ( hideEnergyAfterCombatDelay == 0 )
        {
            energyDisplayed = true;
        }
        else
        {
            doInteractionCheck = true;
        }
        if ( hideSpecialAfterCombatDelay == 0 )
        {
            specialDisplayed = true;
        }
        else
        {
            doInteractionCheck = true;
        }
        if ( hideWarmthAfterCombatDelay == 0 )
        {
            warmthDisplayed = true;
        }
        else
        {
            doInteractionCheck = true;
        }

		if ( doInteractionCheck && (interacting instanceof NPC && ArrayUtils.contains(((NPC) interacting).getComposition().getActions(), "Attack"))
                || (interacting instanceof Player && client.getVarbitValue(Varbits.PVP_SPEC_ORB) == 1) )
		{
			lastCombatActionTickCount = client.getTickCount();

            if ( config.hideHitpointsAfterCombatDelay() > 0 )
            {
                hitpointsDisplayed = true;
            }
            if ( config.hidePrayerAfterCombatDelay() > 0 )
            {
                prayerDisplayed = true;
            }
            if ( config.hideEnergyAfterCombatDelay() > 0 )
            {
                energyDisplayed = true;
            }
            if ( config.hideSpecialAfterCombatDelay() > 0 )
            {
                specialDisplayed = true;
            }
            if ( config.hideWarmthAfterCombatDelay() > 0 )
            {
                warmthDisplayed = true;
            }
		}
		else if ( doInteractionCheck )
		{
            if ( (client.getTickCount() - lastCombatActionTickCount >= hideHitpointAfterCombatDelay) && hideHitpointAfterCombatDelay > 0 )
            {
                hitpointsDisplayed = false;
            }
            if ( (client.getTickCount() - lastCombatActionTickCount >= hidePrayerAfterCombatDelay) && hidePrayerAfterCombatDelay > 0 )
            {
                prayerDisplayed = false;
            }
            if ( (client.getTickCount() - lastCombatActionTickCount >= hideEnergyAfterCombatDelay) && hideEnergyAfterCombatDelay > 0 )
            {
                energyDisplayed = false;
            }
            if ( (client.getTickCount() - lastCombatActionTickCount >= hideSpecialAfterCombatDelay) && hideSpecialAfterCombatDelay > 0 )
            {
                specialDisplayed = false;
            }
            if ( (client.getTickCount() - lastCombatActionTickCount >= hideWarmthAfterCombatDelay) && hideWarmthAfterCombatDelay > 0 )
            {
                warmthDisplayed = false;
            }
		}
	}
}

