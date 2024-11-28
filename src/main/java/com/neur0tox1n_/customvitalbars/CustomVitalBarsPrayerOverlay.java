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

package net.runelite.client.plugins.customvitalbars;

import java.awt.*;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class CustomVitalBarsPrayerOverlay extends OverlayPanel{

    private static final Color PRAYER_COLOR = new Color(50, 200, 200, 175);
    private static final Color ACTIVE_PRAYER_COLOR = new Color(57, 255, 186, 225);
    private static final Color PRAYER_HEAL_COLOR = new Color(57, 255, 186, 75);
    private static final Color PRAYER_REGEN_COLOR = new Color(109, 125, 119, 255);
    private static final Color ACTIVE_PRAYER_AND_PRAYER_REGEN_COLOR = new Color(120, 124, 102, 255);

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

    @Inject
    private ItemManager itemManager;

    @Inject
    CustomVitalBarsPrayerOverlay(
            Client client,
            CustomVitalBarsPlugin plugin,
            CustomVitalBarsConfig config,
            ItemStatChangesService itemstatservice)
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
        this.itemStatService = itemstatservice;

        initRenderer();
    }

    private void initRenderer()
    {
        barRenderer = new CustomVitalBarsComponent(
                () -> inLms() ? Experience.MAX_REAL_LEVEL : client.getRealSkillLevel(Skill.PRAYER),
                () -> client.getBoostedSkillLevel(Skill.PRAYER),
                () -> getRestoreValue(Skill.PRAYER.getName()),
                () ->
                {
                    Color prayerColor = regenPotionEffectActive ? PRAYER_REGEN_COLOR : PRAYER_COLOR;
                    for (Prayer pray : Prayer.values())
                    {
                        if (client.isPrayerActive(pray))
                        {
                            prayerColor = regenPotionEffectActive ? ACTIVE_PRAYER_AND_PRAYER_REGEN_COLOR : ACTIVE_PRAYER_COLOR;
                            break;
                        }
                    }

                    return prayerColor;
                },
                () -> PRAYER_HEAL_COLOR,
                () -> prayerConsumptionRateOrRegeneration
        );
    }

    @Override
    public Dimension render( Graphics2D g )
    {
        deltaTime = java.time.Instant.now().toEpochMilli() - lastTime;
        lastTime = java.time.Instant.now().toEpochMilli();

        if ( !regenPotionEffectActive )
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

        if ( plugin.isBarsDisplayed() && config.renderPrayer() && !uiElementsOpen )
        {
            barRenderer.renderBar( config, g, panelComponent, Vital.PRAYER );

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

    public void onGameTick( GameTick gameTick )
    {
        if ( !regenPotionEffectActive )
        {
            if ( isAnyPrayerActive() )
            {
                double _prayerTimeCost = getCurrentPrayerTimeCost();

                elapsedPrayerTimeInTicks = (elapsedPrayerTimeInTicks + 1) % (_prayerTimeCost / 1000 / 0.6d);
                elapsedPrayerTimeInMilliseconds = (long)((elapsedPrayerTimeInTicks * 0.6 * 1000) % _prayerTimeCost);

                prayerConsumptionRateOrRegeneration = 1 - elapsedPrayerTimeInMilliseconds / _prayerTimeCost;
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
}
