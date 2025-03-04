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

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
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
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import static net.runelite.api.ItemID.*;

public class CustomVitalBarsWarmthOverlay extends OverlayPanel
{
    // love to StatusBars
    private static final Color WARMTH_COLOUR = new Color(244, 97, 0);

    private final Client client;

    private final CustomVitalBarsPlugin plugin;

    private final CustomVitalBarsConfig config;

    private final ItemStatChangesService itemStatService;

    private CustomVitalBarsComponent barRenderer;

    private boolean uiElementsOpen = false;

    private long deltaTime;
    private long lastTime;

    private final SkillIconManager skillIconManager;
    private final SpriteManager spriteManager;


    @Inject
    CustomVitalBarsWarmthOverlay(Client client, CustomVitalBarsPlugin plugin, CustomVitalBarsConfig config, SkillIconManager skillIconManager, ItemStatChangesService itemstatservice, SpriteManager spriteManager)
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

        initRenderer();
    }

    private void initRenderer()
    {
        barRenderer = new CustomVitalBarsComponent(
                () -> 100,
                () -> client.getVarbitValue(Varbits.WINTERTODT_WARMTH) / 10,
                () -> 0,
                () -> WARMTH_COLOUR,
                () -> null,
                () -> 0d,
                () -> skillIconManager.getSkillImage(Skill.FIREMAKING, true)
        );
    }

    @Override
    public Dimension render( Graphics2D g )
    {
        deltaTime = java.time.Instant.now().toEpochMilli() - lastTime;
        lastTime = java.time.Instant.now().toEpochMilli();

        if ( config.hideWhenSidebarPanelClosed() ) {
            Viewport curViewport = null;
            Widget curWidget = null;

            for (Viewport viewport : Viewport.values()) {
                final Widget viewportWidget = client.getWidget(viewport.getViewport());
                if (viewportWidget != null && !viewportWidget.isHidden()) {
                    curViewport = viewport;
                    curWidget = viewportWidget;
                    break;
                }
            }

            if (curViewport == null) {
                return null;
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
    public void onGameTick(GameTick event)
    {
    }

    private BufferedImage loadSprite(int spriteId)
    {
        return spriteManager.getSprite(spriteId, 0);
    }
}
