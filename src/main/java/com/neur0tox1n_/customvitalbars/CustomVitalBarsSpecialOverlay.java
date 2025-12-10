package com.neur0tox1n_.customvitalbars;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.inject.Inject;

import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class CustomVitalBarsSpecialOverlay extends OverlayPanel{

    private static final Color SPECIAL_ATTACK_COLOR = new Color(3, 153, 0, 195);
    private static final int MAX_SPECIAL_ATTACK_VALUE = 100;

    private final Client client;

    private final CustomVitalBarsPlugin plugin;

    private final CustomVitalBarsConfig config;

    private final ItemStatChangesService itemStatService;

    private CustomVitalBarsComponent barRenderer;

    private boolean uiElementsOpen = false;

    private static final int SPEC_REGEN_TICKS = 50;
    @Getter
    private double specialPercentage;

    private int ticksSinceSpecRegen;
    private boolean wearingLightbearer;

    private long millisecondsSinceSpecRegen;
    private long deltaTime;
    private long lastTime;

    private final SkillIconManager skillIconManager;
    private final SpriteManager spriteManager;

    private double deltaX = 0, deltaY = 0;
    private double lastKnownSidebarX = 0, lastKnownSidebarY = 0;

    private boolean delayedToggleLock = false;

    @Inject
    private ConfigManager configManager;

    @Inject
    CustomVitalBarsSpecialOverlay( Client client, CustomVitalBarsPlugin plugin, CustomVitalBarsConfig config, SkillIconManager skillIconManager, ItemStatChangesService itemstatservice, SpriteManager spriteManager)
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

        lastKnownSidebarX = config.debugSidebarPanelX();
        lastKnownSidebarY = config.debugSidebarPanelY();

        initRenderer();

        if ( config.specialRelativeToInventory() )
        {
            toggleLock( true );
        }
    }

    private void initRenderer()
    {
        barRenderer = new CustomVitalBarsComponent(
                () -> MAX_SPECIAL_ATTACK_VALUE,
                () -> client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10,
                () -> 0,
                () -> SPECIAL_ATTACK_COLOR,
                () -> SPECIAL_ATTACK_COLOR,
                () -> specialPercentage,
                () -> loadSprite(SpriteID.MINIMAP_ORB_SPECIAL_ICON)
        );
    }

    @Override
    public Dimension render( Graphics2D g )
    {
        deltaTime = java.time.Instant.now().toEpochMilli() - lastTime;
        lastTime = java.time.Instant.now().toEpochMilli();

        if ( client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) == 1000 )
        {
            millisecondsSinceSpecRegen = 0;
            specialPercentage = 0;
        }
        else
        {
            double millisecondsPerSpecRegen = wearingLightbearer ? SPEC_REGEN_TICKS / 2 : SPEC_REGEN_TICKS;
            millisecondsPerSpecRegen *= 0.6d * 1000;

            millisecondsSinceSpecRegen = (long)((millisecondsSinceSpecRegen + deltaTime) % millisecondsPerSpecRegen);
            specialPercentage = millisecondsSinceSpecRegen / millisecondsPerSpecRegen;
        }

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

                if ( !viewportWidget.isHidden() )
                {
                    curViewport = viewport;
                    curWidget = viewportWidget;

                    break;
                }
            }
        }

        if ( config.hideSpecialWhenSidebarPanelClosed() )
        {
            if (curViewport == null)
            {
                return null;
            }
        }

        if ( config.specialRelativeToInventory() )
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

        if ( plugin.isSpecialDisplayed() && config.renderSpecial() && !uiElementsOpen )
        {
            barRenderer.renderBar( config, g, panelComponent, Vital.SPECIAL_ENERGY, false, client );

            return config.specialSize();
        }

        return null;
    }

    public void onGameStateChanged(GameStateChanged ev)
    {
        if (ev.getGameState() == GameState.HOPPING || ev.getGameState() == GameState.LOGIN_SCREEN)
        {
            ticksSinceSpecRegen = 0;
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event)
    {
        if (event.getContainerId() != InventoryID.EQUIPMENT.getId())
        {
            return;
        }

        ItemContainer equipment = event.getItemContainer();
        final boolean hasLightbearer = equipment.contains(ItemID.LIGHTBEARER);
        if (hasLightbearer == wearingLightbearer)
        {
            return;
        }

        ticksSinceSpecRegen = 0;
        wearingLightbearer = hasLightbearer;
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        final int ticksPerSpecRegen = wearingLightbearer ? SPEC_REGEN_TICKS / 2 : SPEC_REGEN_TICKS;

        if ( client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) < 1000 )
        {
            ticksSinceSpecRegen = (ticksSinceSpecRegen + 1) % ticksPerSpecRegen;
            millisecondsSinceSpecRegen = (long) (ticksSinceSpecRegen * 0.6 * 1000);
        }
        else
        {
            ticksSinceSpecRegen = 0;
            millisecondsSinceSpecRegen = 0;
        }
        specialPercentage = ticksSinceSpecRegen / (double) ticksPerSpecRegen;
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
                deltaX = config.debugSpecialDeltaX();
                deltaY = config.debugSpecialDeltaY();
            }
            else
            {
                deltaX = this.getPreferredLocation().getX() - lastKnownSidebarX;
                deltaY = this.getPreferredLocation().getY() - lastKnownSidebarY;
                configManager.setConfiguration( "Custom Vital Bars", "debugSpecialDeltaX", deltaX );
                configManager.setConfiguration( "Custom Vital Bars", "debugSpecialDeltaY", deltaY );
            }
        }
        else
        {
            deltaX = 0;
            deltaY = 0;
            configManager.setConfiguration( "Custom Vital Bars", "debugSpecialDeltaX", 0 );
            configManager.setConfiguration( "Custom Vital Bars", "debugSpecialDeltaY", 0 );
        }
    }
}
