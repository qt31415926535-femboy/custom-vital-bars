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

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.util.RSTimeUnit;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import static com.neur0tox1n_.customvitalbars.GameTimer.STAMINA;
import static net.runelite.api.ItemID.*;

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
    private final int STAMINA_DURATION_TICKS = 200;

    private final Client client;

    private final CustomVitalBarsPlugin plugin;

    private final CustomVitalBarsConfig config;

    private final ItemStatChangesService itemStatService;

    private CustomVitalBarsComponent barRenderer;

    private boolean uiElementsOpen = false;

    private double staminaDurationRemainingPercentage = 0;
    private double runEnergyRegenerationPercentage = 0;

    private int lastStaminaEffectActive = 0;
    private int staminaEffectActive = 0;

    private int nextHighestRunEnergyMark = 0;
    private int ticksSinceRunEnergyRegen = 0;
    private int ticksToRunEnergyRegen;
    private long millisecondsToRunEnergyRegen;
    private long millisecondsSinceRunEnergyRegen;

    private long millisecondsSinceStaminaPotionDrink;
    private long ticksSinceStaminaPotionDrink;

    private boolean localPlayerRunningToDestination = false;
    private boolean lastLocalPlayerRunningToDestination = false;
    private boolean regenAlreadyStarted = false;

    private int baseStaminaDurationTicks = STAMINA_DURATION_TICKS;
    private int maxStaminaTicks = STAMINA_DURATION_TICKS;

    private WorldPoint prevLocalPlayerLocation;

    private long deltaTime;
    private long lastTime;

    private final Map<GameTimer, TimerTimer> varTimers = new EnumMap<>(GameTimer.class);

    private final SkillIconManager skillIconManager;
    private final SpriteManager spriteManager;

    private double deltaX = 0, deltaY = 0;
    private double lastKnownSidebarX = 0, lastKnownSidebarY = 0;

    private boolean delayedToggleLock = false;

    @Inject
    private ConfigManager configManager;

    @Inject
    CustomVitalBarsEnergyOverlay( Client client, CustomVitalBarsPlugin plugin, CustomVitalBarsConfig config, SkillIconManager skillIconManager, ItemStatChangesService itemstatservice, SpriteManager spriteManager )
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

        lastKnownSidebarX = config.debugSidebarPanelX();
        lastKnownSidebarY = config.debugSidebarPanelY();

        initRenderer();

        if ( config.energyRelativeToInventory() )
        {
            toggleLock( true );
        }
    }

    private void initRenderer()
    {
        barRenderer = new CustomVitalBarsComponent(
                () -> MAX_RUN_ENERGY_VALUE,
                () -> client.getEnergy() / 100,
                () -> getRestoreValue("Run Energy"),
                () ->
                {
                    if ( client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) != 0 )
                    {
                        return RUN_STAMINA_COLOR;
                    }
                    else
                    {
                        return ENERGY_COLOR;
                    }
                },
                () -> ENERGY_HEAL_COLOR,
                () ->
                {
                    if ( config.energyOutlineProgressSelection() == OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY )
                    {
                        return runEnergyRegenerationPercentage;
                    }
                    else if ( config.energyOutlineProgressSelection() == OutlineProgressSelection.SHOW_CONSUMABLE_PROGRESS_ONLY )
                    {
                        return staminaDurationRemainingPercentage;
                    }

                    return ((staminaEffectActive == 1) ? staminaDurationRemainingPercentage : runEnergyRegenerationPercentage);
                },
                () -> loadSprite(SpriteID.MINIMAP_ORB_WALK_ICON)
        );
    }

    @Override
    public Dimension render( Graphics2D g )
    {
        deltaTime = java.time.Instant.now().toEpochMilli() - lastTime;
        lastTime = java.time.Instant.now().toEpochMilli();

        if ( !localPlayerRunningToDestination )
        {
            if ( client.getEnergy() == MAX_RUN_ENERGY_VALUE * 100 )
            {
                nextHighestRunEnergyMark = 0;
                if ( config.energyOutlineProgressThreshold() == OutlineProgressThreshold.RELATED_STAT_AT_MAX )
                {
                    runEnergyRegenerationPercentage = 0;
                }
            }
            else if ( client.getEnergy() >= nextHighestRunEnergyMark )
            {
                int rawRunEnergyRegenPerTick = (int)Math.floor( (1 + (getGracefulRecoveryBoost() / 100.0d)) * (Math.floor( client.getBoostedSkillLevel( Skill.AGILITY ) / 10.0d ) + 15));

                nextHighestRunEnergyMark = ((client.getEnergy() + 99) / 100) * 100;

                ticksToRunEnergyRegen = (int) (Math.ceil((nextHighestRunEnergyMark - client.getEnergy()) / (double) rawRunEnergyRegenPerTick));
                millisecondsToRunEnergyRegen = (long)(ticksToRunEnergyRegen * 0.6 * 1000);

                millisecondsSinceRunEnergyRegen = 0;
                ticksSinceRunEnergyRegen = 0;
                runEnergyRegenerationPercentage = 0;
            }
            else {
                if (millisecondsToRunEnergyRegen > 0) {
                    millisecondsSinceRunEnergyRegen = (millisecondsSinceRunEnergyRegen + deltaTime) % millisecondsToRunEnergyRegen;
                    runEnergyRegenerationPercentage = millisecondsSinceRunEnergyRegen / (double) millisecondsToRunEnergyRegen;
                } else {
                    millisecondsSinceRunEnergyRegen = 0;
                    runEnergyRegenerationPercentage = 0;
                }
            }
        }

        /*
        if ( staminaEffectActive == 1 )
        {
            long millisecondsToStaminaPotionExpire = (long)(baseStaminaDurationTicks * 0.6 * 1000);

            millisecondsSinceStaminaPotionDrink = (millisecondsSinceStaminaPotionDrink + deltaTime) % millisecondsToStaminaPotionExpire;
            staminaDurationRemainingPercentage = 1 - ((double) millisecondsSinceStaminaPotionDrink / millisecondsToStaminaPotionExpire);
        }

         */

        Viewport curViewport = null;
        Widget curWidget = null;

        for (Viewport viewport : Viewport.values())
        {
            final Widget viewportWidget = client.getWidget(viewport.getViewport());
            if ( viewportWidget != null )
            {
                final net.runelite.api.Point location = viewportWidget.getCanvasLocation();
                lastKnownSidebarX = location.getX();
                lastKnownSidebarY = location.getY();
                configManager.setConfiguration( "Custom Vital Bars", "debugSidebarPanelX", lastKnownSidebarX );
                configManager.setConfiguration( "Custom Vital Bars", "debugSidebarPanelY", lastKnownSidebarY );

                if ( !viewportWidget.isHidden() )
                {
                    curViewport = viewport;
                    curWidget = viewportWidget;

                    break;
                }
            }
        }

        if ( config.hideEnergyWhenSidebarPanelClosed() )
        {
            if (curViewport == null)
            {
                return null;
            }
        }

        if ( config.energyRelativeToInventory() )
        {
            if (curViewport != null)
            {
                final net.runelite.api.Point location = curWidget.getCanvasLocation();

                if ( deltaX != 0 && deltaY != 0 )
                {
                    int newDeltaX = (int) (location.getX() + deltaX);
                    int newDeltaY = (int) (location.getY() + deltaY);
                    this.setPreferredLocation( new java.awt.Point(newDeltaX, newDeltaY) );
                }
            }
        }

        if ( plugin.isEnergyDisplayed() && config.renderEnergy() && !uiElementsOpen )
        {
            barRenderer.renderBar( config, g, panelComponent, Vital.RUN_ENERGY, (staminaEffectActive == 1), client );
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
            lastStaminaEffectActive = staminaEffectActive;
            staminaEffectActive = client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE);
            int staminaPotionEffectVarb = client.getVarbitValue(Varbits.STAMINA_EFFECT);
            int enduranceRingEffectVarb = client.getVarbitValue(Varbits.RING_OF_ENDURANCE_EFFECT);

            final int totalStaminaEffect = staminaPotionEffectVarb + enduranceRingEffectVarb;
            if ( staminaEffectActive == 1 )
            {
                updateVarTimer( STAMINA, totalStaminaEffect, i -> i * 10 );
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

    @Subscribe
    public void onGameTick(GameTick event) {
        lastLocalPlayerRunningToDestination = localPlayerRunningToDestination;
        localPlayerRunningToDestination =
                prevLocalPlayerLocation != null &&
                        client.getLocalDestinationLocation() != null &&
                        prevLocalPlayerLocation.distanceTo(client.getLocalPlayer().getWorldLocation()) > 1;
        prevLocalPlayerLocation = client.getLocalPlayer().getWorldLocation();

        if (!localPlayerRunningToDestination && !lastLocalPlayerRunningToDestination && !regenAlreadyStarted) {
            ticksSinceRunEnergyRegen = 0;
            millisecondsSinceRunEnergyRegen = 0;
            runEnergyRegenerationPercentage = 0;

            regenAlreadyStarted = true;
        }

        if (localPlayerRunningToDestination) {
            ticksSinceRunEnergyRegen = 0;
            millisecondsSinceRunEnergyRegen = 0;
            runEnergyRegenerationPercentage = 0;

            regenAlreadyStarted = false;
        } else {
            int currentRunEnergy = client.getEnergy();
            if (currentRunEnergy == MAX_RUN_ENERGY_VALUE * 100) {
                nextHighestRunEnergyMark = 0;
                if (config.energyOutlineProgressThreshold() == OutlineProgressThreshold.RELATED_STAT_AT_MAX) {
                    runEnergyRegenerationPercentage = 0;
                }
            }
            if (currentRunEnergy >= nextHighestRunEnergyMark) {
                int rawRunEnergyRegenPerTick = (int) Math.floor((1 + (getGracefulRecoveryBoost() / 100.0d)) * (Math.floor(client.getBoostedSkillLevel(Skill.AGILITY) / 10.0d) + 15));

                nextHighestRunEnergyMark = ((currentRunEnergy + 99) / 100) * 100;

                ticksToRunEnergyRegen = (int) (Math.ceil((nextHighestRunEnergyMark - currentRunEnergy) / (double) rawRunEnergyRegenPerTick));
                millisecondsToRunEnergyRegen = (long) (ticksToRunEnergyRegen * 0.6 * 1000);

                ticksSinceRunEnergyRegen = 0;
                millisecondsSinceRunEnergyRegen = 0;
                runEnergyRegenerationPercentage = 0;
            } else {
                if (ticksToRunEnergyRegen > 0) {
                    ticksSinceRunEnergyRegen = (ticksSinceRunEnergyRegen + 1) % ticksToRunEnergyRegen;
                    millisecondsSinceRunEnergyRegen = (long) (ticksSinceRunEnergyRegen * 0.6 * 1000);
                    runEnergyRegenerationPercentage = ticksSinceRunEnergyRegen / (double) ticksToRunEnergyRegen;
                } else {
                    ticksSinceRunEnergyRegen = 0;
                    millisecondsSinceRunEnergyRegen = 0;
                    runEnergyRegenerationPercentage = 0;
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

    private BufferedImage loadSprite(int spriteId)
    {
        return spriteManager.getSprite(spriteId, 0);
    }

    private void updateVarTimer( final GameTimer gameTimer, final int varValue, final IntUnaryOperator tickDuration )
    {
        updateVarTimer( gameTimer, varValue, i -> i == 0, tickDuration);
    }

    private void updateVarTimer( final GameTimer gameTimer, final int varValue, final IntPredicate removeTimerCheck, final IntUnaryOperator tickDuration )
    {
        TimerTimer timer = varTimers.get(gameTimer);
        int ticks = tickDuration.applyAsInt( varValue );
        final Duration duration = Duration.of( ticks, RSTimeUnit.GAME_TICKS );

        if ( removeTimerCheck.test( varValue ) )
        {
            removeVarTimer( gameTimer );
            ticks = 0;
        }
        // Reset the timer when its duration increases in order to allow it to turn red at the correct time even when refreshed early
        else if (timer == null || ticks > timer.ticks)
        {
            timer = createGameTimer(gameTimer, duration);
            timer.ticks = ticks;
            varTimers.put(gameTimer, timer);
        }
        else
        {
            timer.ticks = ticks;
            timer.updateDuration(duration);
        }

        staminaDurationRemainingPercentage = (double) ticks / maxStaminaTicks;
    }

    private TimerTimer createGameTimer(final GameTimer timer, Duration duration)
    {
        TimerTimer t = new TimerTimer(timer, duration, plugin);
        return t;
    }

    private void removeVarTimer( GameTimer gameTimer )
    {
        varTimers.remove( gameTimer );
    }

    public void toggleLock( boolean start )
    {
        if ( deltaX == 0 && deltaY == 0 )
        {
            if ( start )
            {
                deltaX = config.debugEnergyDeltaX();
                deltaY = config.debugEnergyDeltaY();
            }
            else
            {
                deltaX = this.getPreferredLocation().getX() - lastKnownSidebarX;
                deltaY = this.getPreferredLocation().getY() - lastKnownSidebarY;
                configManager.setConfiguration( "Custom Vital Bars", "debugEnergyDeltaX", deltaX );
                configManager.setConfiguration( "Custom Vital Bars", "debugEnergyDeltaY", deltaY );
            }
        }
        else
        {
            deltaX = 0;
            deltaY = 0;
            configManager.setConfiguration( "Custom Vital Bars", "debugEnergyDeltaX", 0 );
            configManager.setConfiguration( "Custom Vital Bars", "debugEnergyDeltaY", 0 );
        }
    }
}
