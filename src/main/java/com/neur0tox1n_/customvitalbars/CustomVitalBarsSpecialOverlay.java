package com.neur0tox1n_.customvitalbars;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.util.Text;

public class CustomVitalBarsSpecialOverlay extends OverlayPanel
{
    private static final int MAX_SPECIAL_ATTACK_VALUE = 100;

    private final Client client;

    private final CustomVitalBarsPlugin plugin;

    private final CustomVitalBarsConfig config;

    private final ItemStatChangesService itemStatService;

    private CustomVitalBarsComponent barRenderer;

    private boolean uiElementsOpen = false;

    private static final int SPEC_REGEN_TICKS = 50;
    private static final int SURGE_POTION_BASE_COOLDOWN_MILLISECONDS = 300000;
    @Getter
    private double specialPercentageOrSurgeCooldown;

    private int ticksSinceSpecRegen;
    private boolean wearingLightbearer;

    private long millisecondsSinceSpecRegen;
    private long deltaTime;
    private long lastTime;

    private long surgePotionCooldown = 0;

    private final SkillIconManager skillIconManager;
    private final SpriteManager spriteManager;

    private double deltaX = 0, deltaY = 0;
    private double lastKnownSidebarX = 0, lastKnownSidebarY = 0;

    private Color specialMainColour, specialHealColour, specialSurgeCooldownColour;

    private boolean isDrinking = false;
    private final Map<Integer, Integer> previousInventory = new HashMap<>();

    @Inject
    private OverlayManager overlayManager;

    private final ConfigManager configManager;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomVitalBarsComponent.class);


    @Inject
    CustomVitalBarsSpecialOverlay( Client client, CustomVitalBarsPlugin plugin, CustomVitalBarsConfig config, SkillIconManager skillIconManager, ItemStatChangesService itemstatservice, SpriteManager spriteManager, ConfigManager configManager )
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

        specialMainColour = config.specialMainColour();
        specialHealColour = config.specialHealColour();
        specialSurgeCooldownColour = config.specialSurgeCooldownColour();

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
                () -> (surgePotionCooldown > 0 ? specialSurgeCooldownColour : specialMainColour),
                () -> specialHealColour,
                () -> specialPercentageOrSurgeCooldown,
                () -> loadSprite(SpriteID.MINIMAP_ORB_SPECIAL_ICON)
        );
    }

    @Override
    public Dimension render( Graphics2D g )
    {
        deltaTime = java.time.Instant.now().toEpochMilli() - lastTime;
        lastTime = java.time.Instant.now().toEpochMilli();

        if ( client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) == 1000 && surgePotionCooldown <= 0 )
        {
            millisecondsSinceSpecRegen = 0;
            specialPercentageOrSurgeCooldown = 0;
            surgePotionCooldown = 0;
        }
        else
        {
            double millisecondsPerSpecRegen = wearingLightbearer ? SPEC_REGEN_TICKS / 2 : SPEC_REGEN_TICKS;
            millisecondsPerSpecRegen *= 0.6d * 1000;
            millisecondsSinceSpecRegen = (long)((millisecondsSinceSpecRegen + deltaTime) % millisecondsPerSpecRegen);


            if ( surgePotionCooldown <= 0 )
            {
                surgePotionCooldown = 0;
                specialPercentageOrSurgeCooldown = millisecondsSinceSpecRegen / millisecondsPerSpecRegen;
            }
            else
            {
                surgePotionCooldown -= deltaTime;

                OutlineProgressSelection outlineOption = config.specialOutlineProgressSelection();
                if ( outlineOption == OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY )
                {
                    specialPercentageOrSurgeCooldown = millisecondsSinceSpecRegen / millisecondsPerSpecRegen;
                }
                else if ( outlineOption == OutlineProgressSelection.SHOW_CONSUMABLE_PROGRESS_ONLY || outlineOption == OutlineProgressSelection.SHOW_NATURAL_AND_CONSUMABLE_PROGRESS )
                {
                    specialPercentageOrSurgeCooldown = (double) surgePotionCooldown / SURGE_POTION_BASE_COOLDOWN_MILLISECONDS;
                }
            }
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
                    overlayManager.saveOverlay( this );
                }
            }
        }

        if ( plugin.isSpecialDisplayed() && config.renderSpecial() && !uiElementsOpen )
        {
            barRenderer.renderBar( config, g, panelComponent, Vital.SPECIAL_ENERGY, (surgePotionCooldown > 0), client );

            return config.specialSize();
        }

        return null;
    }

    @Subscribe
    public void onConfigChanged( ConfigChanged event )
    {
        if ( CustomVitalBarsConfig.GROUP.equals(event.getGroup()) && event.getKey().equals("specialRelativeToSidebarPanel") )
        {
            toggleLock( false );
        }
        else if ( event.getKey().equals("specialMainColour") )
        {
            specialMainColour = config.specialMainColour();
        }
        else if ( event.getKey().equals("specialHealColour") )
        {
            specialHealColour = config.specialHealColour();
        }
        else if ( event.getKey().equals("specialSurgeCooldownColour") )
        {
            specialSurgeCooldownColour = config.specialSurgeCooldownColour();
        }
    }

    private void updateInventoryState()
    {
        previousInventory.clear();
        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
        if (inventory != null)
        {
            for (Item item : inventory.getItems())
            {
                previousInventory.merge(item.getId(), item.getQuantity(), Integer::sum);
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged ev)
    {
        if (ev.getGameState() == GameState.HOPPING || ev.getGameState() == GameState.LOGIN_SCREEN)
        {
            ticksSinceSpecRegen = 0;
        }

        if ( ev.getGameState().equals( GameState.LOGGED_IN ) )
        {
            isDrinking = false;
            updateInventoryState();
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event)
    {
        if ( event.getContainerId() == InventoryID.INVENTORY.getId() || event.getContainerId() == InventoryID.BANK.getId())
        {
            if ( isDrinking )
            {
                detectConsumableUsage();
                isDrinking = false;
            }
            updateInventoryState();
        }
        else if (event.getContainerId() != InventoryID.EQUIPMENT.getId())
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
        else if ( surgePotionCooldown == 0 )
        {
            ticksSinceSpecRegen = 0;
            millisecondsSinceSpecRegen = 0;
        }

        if ( surgePotionCooldown > 0 )
        {
            OutlineProgressSelection outlineOption = config.specialOutlineProgressSelection();
            if ( outlineOption == OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY )
            {
                specialPercentageOrSurgeCooldown = ticksSinceSpecRegen / (double) ticksPerSpecRegen;
            }
            else if ( outlineOption == OutlineProgressSelection.SHOW_CONSUMABLE_PROGRESS_ONLY || outlineOption == OutlineProgressSelection.SHOW_NATURAL_AND_CONSUMABLE_PROGRESS )
            {
                specialPercentageOrSurgeCooldown = (double) surgePotionCooldown / SURGE_POTION_BASE_COOLDOWN_MILLISECONDS;
            }
        }
        else
        {
            //surgePotionCooldown = 0;
            specialPercentageOrSurgeCooldown = ticksSinceSpecRegen / (double) ticksPerSpecRegen;
        }

        isDrinking = false;
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
            }
        }
        else
        {
            deltaX = 0;
            deltaY = 0;
        }
        configManager.setConfiguration( "Custom Vital Bars", "debugSpecialDeltaX", (int) deltaX );
        configManager.setConfiguration( "Custom Vital Bars", "debugSpecialDeltaY", (int) deltaY );
    }

    @Subscribe
    public void onMenuOptionClicked( MenuOptionClicked event )
    {
        String menuOption = Text.removeTags( event.getMenuOption() );
        if ( menuOption.equals( "Drink" ) && isApplicableConsumable( event.getItemId() ) )
        {
            isDrinking = true;
        }
    }

    private boolean isApplicableConsumable(int itemId)
    {
        SpecialEnergyRestoration item = SpecialEnergyRestoration.getSpecialRestorationByItemId(itemId);
        return item != null;
    }

    private void detectConsumableUsage()
    {
        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
        if (inventory == null)
        {
            return;
        }

        Map<Integer, Integer> currentInventory = new HashMap<>();
        for (Item item : inventory.getItems())
        {
            currentInventory.merge(item.getId(), item.getQuantity(), Integer::sum);
        }

        for (Map.Entry<Integer, Integer> entry : previousInventory.entrySet())
        {
            int itemID = entry.getKey();
            int previousItemQuantity = entry.getValue();
            if (previousItemQuantity > currentInventory.getOrDefault(itemID, 0) && isApplicableConsumable(itemID))
            {
                handleConsumable(itemID);
            }
        }
    }

    private void handleConsumable(int itemId)
    {
        SpecialEnergyRestoration item = SpecialEnergyRestoration.getSpecialRestorationByItemId(itemId);

        surgePotionCooldown = item.getCooldown();
    }
}
