/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Raqes <j.raqes@gmail.com>
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

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class CustomVitalBarsPrayerOverlay extends OverlayPanel{

    private static final int PRAYER_REGENERATION_INTERVAL_TICKS = 12;
    private static final long PRAYER_REGENERATION_INTERVAL_MILLISECONDS = (long)(PRAYER_REGENERATION_INTERVAL_TICKS * 0.6 * 1000);

    private final Client client;

    private final CustomVitalBarsPlugin plugin;

    private final CustomVitalBarsConfig config;

    private final ItemStatChangesService itemStatService;

    private CustomVitalBarsComponent barRenderer;

    private boolean regenPotionEffectActive = false;
    private boolean uiElementsOpen = false;
    private int prayerBonus;

    private long deltaTime;
    private long lastTime;

    private long elapsedPrayerTimeInMilliseconds;
    private double elapsedPrayerTimeInTicks;
    private double prayerConsumptionRateOrRegeneration;

    private final SkillIconManager skillIconManager;
    private final SpriteManager spriteManager;

    private double deltaX = 0, deltaY = 0;
    private double lastKnownSidebarX = 0, lastKnownSidebarY = 0;

    private Color prayerMainColour, prayerHealColour, prayerActiveColour, prayerRegenActiveColour, prayerRegenActivePrayerActiveColour;

    private int lastPrayerValue = 0;

    @Inject
    private OverlayManager overlayManager;

    private final ConfigManager configManager;

    @Inject
    private ItemManager itemManager;

    @Inject
    CustomVitalBarsPrayerOverlay( Client client, CustomVitalBarsPlugin plugin, CustomVitalBarsConfig config, SkillIconManager skillIconManager, ItemStatChangesService itemstatservice, SpriteManager spriteManager, ConfigManager configManager )
    {
        super(plugin);

        //setPriority(OverlayPriority.LOW);
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

        prayerMainColour = config.prayerMainColour();
        prayerHealColour = config.prayerHealColour();
        prayerActiveColour = config.prayerActiveColour();
        prayerRegenActiveColour = config.prayerRegenActiveColour();
        prayerRegenActivePrayerActiveColour = config.prayerRegenActivePrayerActiveColour();

        initRenderer();

        if ( config.prayerRelativeToInventory() )
        {
            toggleLock( true );
        }
    }

    private void initRenderer()
    {
        barRenderer = new CustomVitalBarsComponent(
                () -> inLms() ? Experience.MAX_REAL_LEVEL : client.getRealSkillLevel(Skill.PRAYER),
                () -> client.getBoostedSkillLevel(Skill.PRAYER),
                () -> getRestoreValue(Skill.PRAYER.getName()),
                () ->
                {
                    OutlineProgressSelection outlineOption = config.prayerOutlineProgressSelection();
                    Color prayerColor = (outlineOption == OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY ? prayerMainColour : (regenPotionEffectActive ? prayerRegenActiveColour : prayerMainColour));
                    for (Prayer pray : Prayer.values())
                    {
                        if (client.isPrayerActive(pray))
                        {
                            prayerColor = (outlineOption == OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY ? prayerActiveColour : (regenPotionEffectActive ? prayerRegenActiveColour : prayerActiveColour));

                            break;
                        }
                    }

                    return prayerColor;
                },
                () -> prayerHealColour,
                () -> prayerConsumptionRateOrRegeneration,
                () -> skillIconManager.getSkillImage(Skill.PRAYER, true)
        );
    }

    @Override
    public Dimension render( Graphics2D g )
    {
        deltaTime = java.time.Instant.now().toEpochMilli() - lastTime;
        lastTime = java.time.Instant.now().toEpochMilli();

        if ( !regenPotionEffectActive || (config.prayerOutlineProgressSelection() == OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY) )
        {
            double prayerTimeCost = getCurrentPrayerTimeCost();
            if (prayerTimeCost == -1) {
                prayerConsumptionRateOrRegeneration = 0;
                elapsedPrayerTimeInMilliseconds = 0;
            } else {
                elapsedPrayerTimeInMilliseconds = (long) ((elapsedPrayerTimeInMilliseconds + deltaTime) % prayerTimeCost);
                prayerConsumptionRateOrRegeneration = 1 - elapsedPrayerTimeInMilliseconds / prayerTimeCost;
            }
        }
        else
        {
            elapsedPrayerTimeInMilliseconds = (elapsedPrayerTimeInMilliseconds + deltaTime) % PRAYER_REGENERATION_INTERVAL_MILLISECONDS;

            prayerConsumptionRateOrRegeneration = (double)elapsedPrayerTimeInMilliseconds / PRAYER_REGENERATION_INTERVAL_MILLISECONDS;
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

        if ( config.hidePrayerWhenSidebarPanelClosed() )
        {
            if (curViewport == null)
            {
                return null;
            }
        }

        if ( config.prayerRelativeToInventory() )
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

        if ( plugin.isPrayerDisplayed() && config.renderPrayer() && !uiElementsOpen )
        {
            barRenderer.renderBar( config, g, panelComponent, Vital.PRAYER, regenPotionEffectActive, client );

            return config.prayerSize();
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

    private boolean inLms()
    {
        return client.getWidget(ComponentID.LMS_INGAME_INFO) != null;
    }

    @Subscribe
    public void onItemContainerChanged(final ItemContainerChanged event)
    {
        final int id = event.getContainerId();
        if (id == InventoryID.EQUIPMENT.getId())
        {
            prayerBonus = totalPrayerBonus(event.getItemContainer().getItems());
        }
    }

    @Subscribe
    public void onConfigChanged( ConfigChanged event )
    {
        if ( CustomVitalBarsConfig.GROUP.equals(event.getGroup()) && event.getKey().equals("prayerRelativeToSidebarPanel") )
        {
            toggleLock( false );
        }
        else if ( event.getKey().equals("prayerMainColour") )
        {
            prayerMainColour = config.prayerMainColour();
        }
        else if ( event.getKey().equals("prayerHealColour") )
        {
            prayerHealColour = config.prayerHealColour();
        }
        else if ( event.getKey().equals("prayerActiveColour") )
        {
            prayerActiveColour = config.prayerActiveColour();
        }
        else if ( event.getKey().equals("prayerRegenActiveColour") )
        {
            prayerRegenActiveColour = config.prayerRegenActiveColour();
        }
        else if ( event.getKey().equals("prayerRegenActivePrayerActiveColour") )
        {
            prayerRegenActivePrayerActiveColour = config.prayerRegenActivePrayerActiveColour();
        }
    }

    @Subscribe
    public void onGameTick( GameTick gameTick )
    {
        if ( !regenPotionEffectActive || (config.prayerOutlineProgressSelection() == OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY) )
        {
            if ( isAnyPrayerActive() )
            {
                if ( lastPrayerValue != client.getBoostedSkillLevel(Skill.PRAYER) )
                {
                    elapsedPrayerTimeInTicks = 0;
                    elapsedPrayerTimeInMilliseconds = 0;

                    prayerConsumptionRateOrRegeneration = 0;
                }
                else
                {
                    double _prayerTimeCost = getCurrentPrayerTimeCost();

                    elapsedPrayerTimeInTicks = (elapsedPrayerTimeInTicks + 1) % (_prayerTimeCost / 1000 / 0.6d);
                    elapsedPrayerTimeInMilliseconds = (long)((elapsedPrayerTimeInTicks * 0.6 * 1000) % _prayerTimeCost);

                    prayerConsumptionRateOrRegeneration = 1 - elapsedPrayerTimeInMilliseconds / _prayerTimeCost;
                }
                lastPrayerValue = client.getBoostedSkillLevel(Skill.PRAYER);
            }
            else
            {
                elapsedPrayerTimeInTicks = 0;
                elapsedPrayerTimeInMilliseconds = 0;

                prayerConsumptionRateOrRegeneration = 0;
            }
        }
        else
        {
            elapsedPrayerTimeInTicks = (elapsedPrayerTimeInTicks + 1) % PRAYER_REGENERATION_INTERVAL_TICKS;
            elapsedPrayerTimeInMilliseconds = (long)((elapsedPrayerTimeInTicks * 0.6 * 1000) % PRAYER_REGENERATION_INTERVAL_MILLISECONDS);

            prayerConsumptionRateOrRegeneration = elapsedPrayerTimeInTicks / PRAYER_REGENERATION_INTERVAL_TICKS;
        }
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
    protected void onVarbitChanged( VarbitChanged change )
    {
        // much love to supalosa's Prayer Regeneration Timer plugin
        if ( change.getVarbitId() == Varbits.BUFF_PRAYER_REGENERATION )
        {
            prayerConsumptionRateOrRegeneration = 0;
            elapsedPrayerTimeInTicks = 0;
            elapsedPrayerTimeInMilliseconds = 0;
            int value = change.getValue();
            regenPotionEffectActive = (value > 0);
        }
    }

    private boolean isAnyPrayerActive()
    {
        for (Prayer pray : Prayer.values())//Check if any prayers are active
        {
            if (client.isPrayerActive(pray))
            {
                return true;
            }
        }

        return false;
    }

    private int totalPrayerBonus(Item[] items)
    {
        int total = 0;
        for (Item item : items)
        {
            //ItemStats is = itemManager.getItemStats(item.getId(), false);
            ItemStats is = itemManager.getItemStats(item.getId());
            if (is != null && is.getEquipment() != null)
            {
                total += is.getEquipment().getPrayer();
            }
        }
        return total;
    }

    private int getDrainEffect(Client client)
    {
        int drainEffect = 0;

        for (PrayerType prayerType : PrayerType.values())
        {
            if (client.isPrayerActive(prayerType.getPrayer()))
            {
                drainEffect += prayerType.getDrainEffect();
            }
        }

        return drainEffect;
    }

    private double getCurrentPrayerTimeCost()
    {
        final int drainEffect = getDrainEffect(client);

        if (drainEffect == 0)
        {
            return -1;
        }

        // Calculate how many milliseconds each prayer points last so the prayer bonus can be applied
        // https://oldschool.runescape.wiki/w/Prayer#Prayer_drain_mechanics
        int drainResistance = 2 * prayerBonus + 60;
        return 1000 * 0.6d * (double)drainResistance / drainEffect;
    }

    private BufferedImage loadSprite(int spriteId)
    {
        return spriteManager.getSprite(spriteId, 0);
    }

    public void toggleLock( boolean start )
    {
        if ( deltaX == 0 && deltaY == 0 )
        {
            if ( start )
            {
                deltaX = config.debugPrayerDeltaX();
                deltaY = config.debugPrayerDeltaY();
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
        configManager.setConfiguration( "Custom Vital Bars", "debugPrayerDeltaX", (int) deltaX );
        configManager.setConfiguration( "Custom Vital Bars", "debugPrayerDeltaY", (int) deltaY );
    }
}
