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
package net.runelite.client.plugins.customvitalbars;

import java.awt.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import javax.inject.Inject;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.runenergy.RunEnergyPlugin;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.util.RSTimeUnit;

import static net.runelite.api.ItemID.*;
import static net.runelite.api.ItemID.MAX_CAPE;

public class CustomVitalBarsEnergyOverlay extends OverlayPanel
{
    // love to RunEnergyPlugin
    @Getter
    protected enum GracefulEquipmentSlot
    {
        HEAD(EquipmentInventorySlot.HEAD.getSlotIdx(), 3, GRACEFUL_HOOD),
        BODY(EquipmentInventorySlot.BODY.getSlotIdx(), 4, GRACEFUL_TOP),
        LEGS(EquipmentInventorySlot.LEGS.getSlotIdx(), 4, GRACEFUL_LEGS),
        GLOVES(EquipmentInventorySlot.GLOVES.getSlotIdx(), 3, GRACEFUL_GLOVES),
        BOOTS(EquipmentInventorySlot.BOOTS.getSlotIdx(), 3, GRACEFUL_BOOTS),
        // Agility skill capes and the non-cosmetic Max capes also count for the Graceful set effect
        CAPE(EquipmentInventorySlot.CAPE.getSlotIdx(), 3, GRACEFUL_CAPE, AGILITY_CAPE, MAX_CAPE);

        private final int index;
        private final int boost;
        private final Set<Integer> items;

        GracefulEquipmentSlot(int index, int boost, int... baseItems)
        {
            this.index = index;
            this.boost = boost;

            final ImmutableSet.Builder<Integer> itemsBuilder = ImmutableSet.builder();
            for (int item : baseItems)
            {
                itemsBuilder.addAll(ItemVariationMapping.getVariations(item));
            }
            items = itemsBuilder.build();
        }

        private static final int TOTAL_BOOSTS = Arrays.stream(values()).mapToInt(GracefulEquipmentSlot::getBoost).sum();
    }
    // Full set grants an extra 10% boost to recovery rate
    private static final int GRACEFUL_FULL_SET_BOOST_BONUS = 10;

    private static final Color ENERGY_HEAL_COLOR = new Color (199,  118, 0, 218);
    private final Color RUN_STAMINA_COLOR = new Color(160, 124, 72, 255);
    private final Color ENERGY_COLOR = new Color(199, 174, 0, 220);
    private final int MAX_RUN_ENERGY_VALUE = 100;

    private final Client client;

    private final CustomVitalBarsPlugin plugin;

    private final CustomVitalBarsConfig config;

    private final ItemStatChangesService itemStatService;

    private CustomVitalBarsComponent barRenderer;

    private boolean uiElementsOpen = false;

    private double staminaDurationRemainingOrRegeneration = 0;

    private int staminaEffectActive = 0;

    private int ticksSinceRunEnergyRegen = 0;

    private int nextHighestRunEnergyMark = 0;
    private int ticksToRunEnergyRegen;
    private long millisecondsToRunEnergyRegen;

    private boolean localPlayerRunningToDestination = false;

    private WorldPoint prevLocalPlayerLocation;

    private long millisecondsSinceRunEnergyRegen;
    private long deltaTime;
    private long lastTime;

    @Inject
    CustomVitalBarsEnergyOverlay(
            Client client,
            CustomVitalBarsPlugin plugin,
            CustomVitalBarsConfig config,
            ItemStatChangesService itemstatservice)
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
        this.itemStatService = itemstatservice;

        initRenderer();
    }

    private void initRenderer()
    {
        barRenderer = new CustomVitalBarsComponent(
                () -> MAX_RUN_ENERGY_VALUE,
                () -> client.getEnergy() / 100,
                () -> getRestoreValue("Run Energy"),
                () ->
                {
                    if (client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) != 0)
                    {
                        return RUN_STAMINA_COLOR;
                    }
                    else
                    {
                        return ENERGY_COLOR;
                    }
                },
                () -> ENERGY_HEAL_COLOR,
                () -> staminaDurationRemainingOrRegeneration
        );
    }

    @Override
    public Dimension render( Graphics2D g )
    {
        deltaTime = java.time.Instant.now().toEpochMilli() - lastTime;
        lastTime = java.time.Instant.now().toEpochMilli();

        if ( client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) == 0 )
        {
            if ( !localPlayerRunningToDestination )
            {
                int rawRunEnergyRegenPerTick = (int)Math.floor( (1 + (getGracefulRecoveryBoost() / 100.0d)) * (Math.floor( client.getBoostedSkillLevel( Skill.AGILITY ) / 6.0d ) + 8));

                if ( client.getEnergy() > nextHighestRunEnergyMark )
                {
                    nextHighestRunEnergyMark = ((client.getEnergy() + 99) / 100) * 100;

                    ticksToRunEnergyRegen = (int) (Math.ceil((nextHighestRunEnergyMark - client.getEnergy()) / (double) rawRunEnergyRegenPerTick));
                    millisecondsToRunEnergyRegen = (long)(ticksToRunEnergyRegen * 0.6 * 1000);
                }

                if ( millisecondsToRunEnergyRegen > 0 )
                {
                    millisecondsSinceRunEnergyRegen = (millisecondsSinceRunEnergyRegen + deltaTime) % millisecondsToRunEnergyRegen;
                    staminaDurationRemainingOrRegeneration = millisecondsSinceRunEnergyRegen / (double)millisecondsToRunEnergyRegen;
                }
                else
                {
                    millisecondsSinceRunEnergyRegen = 0;
                    staminaDurationRemainingOrRegeneration = 0;
                }
                
                int currentRunEnergy = client.getEnergy();
                int maxRunEnergy = MAX_RUN_ENERGY_VALUE * 100;
                if ( currentRunEnergy == maxRunEnergy )
                {
                    nextHighestRunEnergyMark = maxRunEnergy - 1000;
                    if ( config.energyOutlineProgressThreshold() == OutlineProgressThreshold.RELATED_STAT_AT_MAX )
                    {
                        staminaDurationRemainingOrRegeneration = 0;
                    }
                }
            }
        }

        if ( plugin.isBarsDisplayed() && config.renderEnergy() && !uiElementsOpen )
        {
            barRenderer.renderBar( config, g, panelComponent, Vital.RUN_ENERGY );
            return config.energySize();
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

    public void onVarbitChanged( VarbitChanged event )
    {
        if (event.getVarbitId() == Varbits.RUN_SLOWED_DEPLETION_ACTIVE
                || event.getVarbitId() == Varbits.STAMINA_EFFECT
                || event.getVarbitId() == Varbits.RING_OF_ENDURANCE_EFFECT)
        {
            // staminaEffectActive is checked to match https://github.com/Joshua-F/cs2-scripts/blob/741271f0c3395048c1bad4af7881a13734516adf/scripts/%5Bproc%2Cbuff_bar_get_value%5D.cs2#L25
            staminaEffectActive = client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE);
            int staminaPotionEffectVarb = client.getVarbitValue(Varbits.STAMINA_EFFECT);
            int enduranceRingEffectVarb = client.getVarbitValue(Varbits.RING_OF_ENDURANCE_EFFECT);

            final int totalStaminaEffect = staminaPotionEffectVarb + enduranceRingEffectVarb;
            if ( staminaEffectActive == 1 )
            {
                updateStaminaTimer( totalStaminaEffect, i -> i * 10 );
            }
        }
    }

    public void onGameStateChanged(GameStateChanged ev)
    {
        if (ev.getGameState() == GameState.HOPPING || ev.getGameState() == GameState.LOGIN_SCREEN)
        {
            ticksSinceRunEnergyRegen = -2; // For some reason this makes this accurate
            millisecondsSinceRunEnergyRegen = (long)(ticksSinceRunEnergyRegen * 0.6 * 1000); // I guess I follow suit
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

    private void updateStaminaTimer( final int varValue, final IntUnaryOperator tickDuration )
    {
        updateStaminaTimer( varValue, i -> i == 0, tickDuration );
    }

    private void updateStaminaTimer( final int varValue, final IntPredicate removeTimerCheck, final IntUnaryOperator tickDuration )
    {
        int ticks = tickDuration.applyAsInt(varValue);
        final Duration duration = Duration.of(ticks, RSTimeUnit.GAME_TICKS);
        staminaDurationRemainingOrRegeneration = duration.getSeconds();
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if ( client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) == 0 )
        {
            localPlayerRunningToDestination =
                    prevLocalPlayerLocation != null &&
                            client.getLocalDestinationLocation() != null &&
                            prevLocalPlayerLocation.distanceTo(client.getLocalPlayer().getWorldLocation()) > 1;
            prevLocalPlayerLocation = client.getLocalPlayer().getWorldLocation();

            if ( localPlayerRunningToDestination )
            {
                ticksSinceRunEnergyRegen = 0;
                millisecondsSinceRunEnergyRegen = 0;
                staminaDurationRemainingOrRegeneration = 0;
            }
            else
            {
                int rawRunEnergyRegenPerTick = (int)Math.floor( (1 + (getGracefulRecoveryBoost() / 100.0d)) * (Math.floor( client.getBoostedSkillLevel( Skill.AGILITY ) / 6.0d ) + 8));

                if ( client.getEnergy() > nextHighestRunEnergyMark )
                {
                    nextHighestRunEnergyMark = ((client.getEnergy() + 99) / 100) * 100;

                    ticksToRunEnergyRegen = (int)(Math.ceil((nextHighestRunEnergyMark - client.getEnergy()) / (double) rawRunEnergyRegenPerTick));
                    millisecondsToRunEnergyRegen = (long)(ticksToRunEnergyRegen * 0.6 * 1000);
                }

                if ( ticksToRunEnergyRegen > 0 )
                {
                    ticksSinceRunEnergyRegen = (ticksSinceRunEnergyRegen + 1) % ticksToRunEnergyRegen;
                    millisecondsSinceRunEnergyRegen = (long)(ticksSinceRunEnergyRegen * 0.6 * 1000);
                    staminaDurationRemainingOrRegeneration = ticksSinceRunEnergyRegen / (double)ticksToRunEnergyRegen;
                }
                else
                {
                    ticksSinceRunEnergyRegen = 0;
                    millisecondsSinceRunEnergyRegen = 0;
                    staminaDurationRemainingOrRegeneration = 0;
                }

                int currentRunEnergy = client.getEnergy();
                int maxRunEnergy = MAX_RUN_ENERGY_VALUE * 100;
                if ( currentRunEnergy == maxRunEnergy )
                {
                    nextHighestRunEnergyMark = maxRunEnergy - 1000;
                    if ( config.energyOutlineProgressThreshold() == OutlineProgressThreshold.RELATED_STAT_AT_MAX )
                    {
                        staminaDurationRemainingOrRegeneration = 0;
                    }
                }
            }
        }
    }

    private int getGracefulRecoveryBoost()
    {
        final ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);

        if (equipment == null)
        {
            return 0;
        }

        final Item[] items = equipment.getItems();

        int boost = 0;

        for (final GracefulEquipmentSlot slot : GracefulEquipmentSlot.values())
        {
            if (items.length <= slot.getIndex())
            {
                continue;
            }

            final Item wornItem = items[slot.getIndex()];

            if (wornItem != null && slot.getItems().contains(wornItem.getId()))
            {
                boost += slot.getBoost();
            }
        }

        if (boost == GracefulEquipmentSlot.TOTAL_BOOSTS)
        {
            boost += GRACEFUL_FULL_SET_BOOST_BONUS;
        }

        return boost;
    }
}
