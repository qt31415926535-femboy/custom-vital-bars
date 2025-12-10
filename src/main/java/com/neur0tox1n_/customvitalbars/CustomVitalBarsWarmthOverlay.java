/*
 * Copyright (c) 2018, Sean Dewar <https://github.com/seandewar>
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

import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CustomVitalBarsWarmthOverlay extends OverlayPanel
{
    // love to StatusBars
    private static final Color WARMTH_COLOUR = new Color(244, 97, 0);

    private static final int WINTERTODT_REGION = 6462;

    private final Client client;

    private final CustomVitalBarsPlugin plugin;

    private final CustomVitalBarsConfig config;

    private final ItemStatChangesService itemStatService;

    private CustomVitalBarsComponent barRenderer;

    private boolean uiElementsOpen = false;
    private boolean isWearingHPCape = false;
    private boolean isWearingRegenBracelet = false;

    private static final int NORMAL_WARMTH_REGEN_TICKS = 100;

    private double warmthRegenerationPercentage;
    private int ticksSinceWarmthRegen;
    private long millisecondsSinceWarmthRegen;

    private long deltaTime;
    private long lastTime;

    private final SkillIconManager skillIconManager;
    private final SpriteManager spriteManager;

    private double deltaX = 0, deltaY = 0;
    private double lastKnownSidebarX = 0, lastKnownSidebarY = 0;

    @Inject
    private OverlayManager overlayManager;

    private final ConfigManager configManager;

    @Inject
    CustomVitalBarsWarmthOverlay(Client client, CustomVitalBarsPlugin plugin, CustomVitalBarsConfig config, SkillIconManager skillIconManager, ItemStatChangesService itemstatservice, SpriteManager spriteManager, ConfigManager configManager )
    {
        super(plugin);

        setPriority(OverlayPriority.HIGH);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
        setMovable(true);
        setResizable( false );
        setSnappable( true );
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.skillIconManager = skillIconManager;
        this.spriteManager = spriteManager;
        this.itemStatService = itemstatservice;
        this.configManager = configManager;

        lastKnownSidebarX = config.debugSidebarPanelX();
        lastKnownSidebarY = config.debugSidebarPanelY();

        initRenderer();

        if ( config.warmthRelativeToInventory() )
        {
            toggleLock( true );
        }
    }

    private void initRenderer()
    {
        barRenderer = new CustomVitalBarsComponent(
                () -> 100,
                () -> client.getVarbitValue(Varbits.WINTERTODT_WARMTH) / 10,
                () -> 0,
                () -> WARMTH_COLOUR,
                () -> null,
                () -> warmthRegenerationPercentage,
                () -> skillIconManager.getSkillImage(Skill.FIREMAKING, true)
        );
    }

    @Override
    public Dimension render( Graphics2D g )
    {
        deltaTime = java.time.Instant.now().toEpochMilli() - lastTime;
        lastTime = java.time.Instant.now().toEpochMilli();

        long millisecondsPerWarmthRegen = (long)(getTicksPerWarmthRegen() * 0.6 * 1000);

        millisecondsSinceWarmthRegen = (millisecondsSinceWarmthRegen + deltaTime) % millisecondsPerWarmthRegen;
        warmthRegenerationPercentage = millisecondsSinceWarmthRegen / (double) millisecondsPerWarmthRegen;

        int currentWarmth = client.getVarbitValue(Varbits.WINTERTODT_WARMTH) / 10;
        int maxWarmth = 100;
        if ( currentWarmth == maxWarmth && config.warmthOutlineProgressThreshold() == OutlineProgressThreshold.RELATED_STAT_AT_MAX )
        {
            warmthRegenerationPercentage = 0;
        }

        Viewport curViewport = null;
        Widget curWidget = null;

        for (Viewport viewport : Viewport.values())
        {
            final Widget viewportWidget = client.getWidget(viewport.getViewport());
            if ( viewportWidget != null )
            {
                if ( !viewportWidget.isHidden() )
                {
                    curViewport = viewport;
                    curWidget = viewportWidget;

                    final net.runelite.api.Point location = viewportWidget.getCanvasLocation();
                    lastKnownSidebarX = location.getX();
                    lastKnownSidebarY = location.getY();

                    break;
                }
            }
        }

        if ( config.hideWarmthWhenSidebarPanelClosed() )
        {
            if (curViewport == null)
            {
                return null;
            }
        }

        if ( config.warmthRelativeToInventory() )
        {
            if (curViewport != null)
            {
                final net.runelite.api.Point location = curWidget.getCanvasLocation();

                if ( deltaX != 0 && deltaY != 0 )
                {
                    int newDeltaX = (int) (location.getX() + deltaX);
                    int newDeltaY = (int) (location.getY() + deltaY);
                    this.setPreferredLocation( new java.awt.Point(newDeltaX, newDeltaY) );
                    overlayManager.saveOverlay( this );
                }
            }
        }

        if ( ((config.renderWarmthWithOptions() == WarmthRenderOptions.SHOW_DYNAMICALLY && isInWintertodtRegion()) || config.renderWarmthWithOptions() == WarmthRenderOptions.SHOW_ALWAYS) && plugin.isWarmthDisplayed() && !uiElementsOpen )
        {
            barRenderer.renderBar( config, g, panelComponent, Vital.WARMTH, false, client );
            return config.warmthSize();
        }

        return null;
    }

    private int getRestoreValue(String skill)
    {
        final MenuEntry[] menu = client.getMenuEntries();
        final int menuSize = menu.length;
        if (menuSize == 0)
        {
            return 0;
        }

        final MenuEntry entry = menu[menuSize - 1];
        final Widget widget = entry.getWidget();
        int restoreValue = 0;

        if (widget != null && widget.getId() == ComponentID.INVENTORY_CONTAINER)
        {
            final Effect change = itemStatService.getItemStatChanges(widget.getItemId());

            if (change != null)
            {
                for (final StatChange c : change.calculate(client).getStatChanges())
                {
                    final int value = c.getTheoretical();

                    if (value != 0 && c.getStat().getName().equals(skill))
                    {
                        restoreValue = value;
                    }
                }
            }
        }

        return restoreValue;
    }

    @Subscribe
    public void onWidgetLoaded( WidgetLoaded widgetLoaded )
    {
        uiElementsOpen = true;
    }

    @Subscribe
    public void onWidgetClosed( WidgetClosed widgetClosed )
    {
        uiElementsOpen = false;
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event)
    {
        if (event.getContainerId() != InventoryID.EQUIPMENT.getId())
        {
            return;
        }

        ItemContainer equipment = event.getItemContainer();
        final boolean hasHPCape = (equipment.contains(ItemID.HITPOINTS_CAPE) || equipment.contains(ItemID.HITPOINTS_CAPET));
        final boolean hasRegenBracelet = equipment.contains(ItemID.REGEN_BRACELET);

        if ( hasHPCape != isWearingHPCape )
        {
            ticksSinceWarmthRegen = 0;
            millisecondsSinceWarmthRegen = 0;
            isWearingHPCape = hasHPCape;
        }
        if ( hasRegenBracelet != isWearingRegenBracelet )
        {
            ticksSinceWarmthRegen = 0;
            millisecondsSinceWarmthRegen = 0;
            isWearingRegenBracelet = hasRegenBracelet;
        }
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        int ticksPerWarmthRegen = getTicksPerWarmthRegen();

        ticksSinceWarmthRegen = (ticksSinceWarmthRegen + 1) % ticksPerWarmthRegen;
        millisecondsSinceWarmthRegen = (long) (ticksSinceWarmthRegen * 0.6 * 1000);
        warmthRegenerationPercentage = ticksSinceWarmthRegen / (double) ticksPerWarmthRegen;

        int currentWarmth = client.getVarbitValue(Varbits.WINTERTODT_WARMTH) / 10;
        int maxWarmth = 100;
        if ( currentWarmth == maxWarmth && config.warmthOutlineProgressThreshold() == OutlineProgressThreshold.RELATED_STAT_AT_MAX )
        {
            warmthRegenerationPercentage = 0;
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged ev)
    {
        if (ev.getVarbitId() == Varbits.PRAYER_RAPID_HEAL)
        {
            ticksSinceWarmthRegen = 0;
            millisecondsSinceWarmthRegen = 0;
        }
    }


    private BufferedImage loadSprite(int spriteId)
    {
        return spriteManager.getSprite(spriteId, 0);
    }

    private int getTicksPerWarmthRegen()
    {
        int ticksPerWarmthRegen = NORMAL_WARMTH_REGEN_TICKS;
        if ( client.isPrayerActive( Prayer.RAPID_HEAL ) || isWearingHPCape )
        {
            ticksPerWarmthRegen /= 2;
        }
        if ( isWearingRegenBracelet )
        {
            ticksPerWarmthRegen /= 2;
        }

        return ticksPerWarmthRegen;
    }

    private boolean isInWintertodtRegion()
    {
        if (client.getLocalPlayer() != null)
        {
            return client.getLocalPlayer().getWorldLocation().getRegionID() == WINTERTODT_REGION;
        }

        return false;
    }

    public void toggleLock( boolean start )
    {
        if ( deltaX == 0 && deltaY == 0 )
        {
            if ( start )
            {
                deltaX = config.debugWarmthDeltaX();
                deltaY = config.debugWarmthDeltaY();
            }
            else
            {
                deltaX = this.getPreferredLocation().getX() - lastKnownSidebarX;
                deltaY = this.getPreferredLocation().getY() - lastKnownSidebarY;
            }
        }
        else
        {
            deltaX = 0;
            deltaY = 0;
        }
        configManager.setConfiguration( "Custom Vital Bars", "debugWarmthDeltaX", (int) deltaX );
        configManager.setConfiguration( "Custom Vital Bars", "debugWarmthDeltaY", (int) deltaY );
    }
}
