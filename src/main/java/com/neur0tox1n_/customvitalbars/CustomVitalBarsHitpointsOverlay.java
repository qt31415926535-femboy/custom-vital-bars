// credits to DapperMickie for DelayedHeals code

package com.neur0tox1n_.customvitalbars;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.events.*;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.AlternateSprites;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

public class CustomVitalBarsHitpointsOverlay extends OverlayPanel{

    private static final Color HEALTH_COLOR = new Color(225, 35, 0, 125);
    private static final Color DELAYED_HEAL_COLOR = new Color(255, 35, 111, 150);
    private static final Color POISONED_COLOR = new Color(0, 145, 0, 150);
    private static final Color VENOMED_COLOR = new Color(0, 65, 0, 150);
    private static final Color HEAL_COLOR = new Color(255, 112, 6, 150);
    private static final Color DISEASE_COLOR = new Color(255, 193, 75, 181);
    private static final Color PARASITE_COLOR = new Color(196, 62, 109, 181);

    private static final int WINTERTODT_REGION = 6462;

    private final Client client;

    private final CustomVitalBarsPlugin plugin;

    private final CustomVitalBarsConfig config;

    private final ItemStatChangesService itemStatService;

    private CustomVitalBarsComponent barRenderer;

    private boolean uiElementsOpen = false;
    private boolean isWearingHPCape = false;
    private boolean isWearingRegenBracelet = false;

    private static final int NORMAL_HP_REGEN_TICKS = 100;

    @Getter
    private double hitpointsRegenerationPercentage, hitpointsRecoveryPercentage;
    private int ticksSinceHPRegen, ticksToDelayedRecovery, initialTicksToDelayedRecovery;
    private long millisecondsSinceHPRegen, millisecondsToDelayedRecovery;

    private long deltaTime;
    private long lastTime;

    private final SkillIconManager skillIconManager;
    private final SpriteManager spriteManager;
    private final Image heartDisease;
    private final Image heartPoison;
    private final Image heartVenom;

    private final Map<Integer, Integer> previousInventory = new HashMap<>();
    private boolean isEating = false;

    private double deltaX = 0, deltaY = 0;
    private double lastKnownSidebarX = 0, lastKnownSidebarY = 0;

    @Inject
    private OverlayManager overlayManager;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomVitalBarsComponent.class);

    private final ConfigManager configManager;

    @Inject
    CustomVitalBarsHitpointsOverlay( Client client, CustomVitalBarsPlugin plugin, CustomVitalBarsConfig config, SkillIconManager skillIconManager, ItemStatChangesService itemstatservice, SpriteManager spriteManager, ConfigManager configManager )
    {
        super(plugin);

        //setPriority(OverlayPriority.LOW);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);

        setMovable( true );
        setResizable( false );
        setSnappable( true );
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.skillIconManager = skillIconManager;
        this.spriteManager = spriteManager;
        this.itemStatService = itemstatservice;
        this.configManager = configManager;

        heartDisease = ImageUtil.loadImageResource(AlternateSprites.class, AlternateSprites.DISEASE_HEART);
        heartPoison = ImageUtil.loadImageResource(AlternateSprites.class, AlternateSprites.POISON_HEART);
        heartVenom = ImageUtil.loadImageResource(AlternateSprites.class, AlternateSprites.VENOM_HEART);

        lastKnownSidebarX = config.debugSidebarPanelX();
        lastKnownSidebarY = config.debugSidebarPanelY();

        initRenderer();

        if ( config.hitpointsRelativeToInventory() )
        {
            toggleLock( true );
        }
    }

    private void initRenderer()
    {
        barRenderer = new CustomVitalBarsComponent(
                () -> inLms() ? Experience.MAX_REAL_LEVEL : client.getRealSkillLevel(Skill.HITPOINTS),
                () -> client.getBoostedSkillLevel(Skill.HITPOINTS),
                () -> getRestoreValue(Skill.HITPOINTS.getName()),
                () ->
                {
                    final int poisonState = client.getVarpValue(VarPlayer.POISON);

                    if (poisonState >= 1000000)
                    {
                        return VENOMED_COLOR;
                    }

                    if (poisonState > 0)
                    {
                        return POISONED_COLOR;
                    }

                    if (client.getVarpValue(VarPlayer.DISEASE_VALUE) > 0)
                    {
                        return DISEASE_COLOR;
                    }

                    if (client.getVarbitValue(Varbits.PARASITE) >= 1)
                    {
                        return PARASITE_COLOR;
                    }

                    if ( ticksToDelayedRecovery > 0 )
                    {
                        return DELAYED_HEAL_COLOR;
                    }

                    return HEALTH_COLOR;
                },
                () -> HEAL_COLOR,
                () ->
                {
                    if ( config.hitpointsOutlineProgressSelection() == OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY )
                    {
                        return hitpointsRegenerationPercentage;
                    }
                    else if ( config.hitpointsOutlineProgressSelection() == OutlineProgressSelection.SHOW_CONSUMABLE_PROGRESS_ONLY )
                    {
                        return hitpointsRecoveryPercentage;
                    }

                    return (ticksToDelayedRecovery > 0 ? hitpointsRecoveryPercentage : hitpointsRegenerationPercentage);
                },
                () ->
                {
                    final int poisonState = client.getVarpValue(VarPlayer.POISON);

                    if (poisonState > 0 && poisonState < 50)
                    {
                        return heartPoison;
                    }

                    if (poisonState >= 1000000)
                    {
                        return heartVenom;
                    }

                    if (client.getVarpValue(VarPlayer.DISEASE_VALUE) > 0)
                    {
                        return heartDisease;
                    }

                    return loadSprite(SpriteID.MINIMAP_ORB_HITPOINTS_ICON);
                }
        );
    }

    @Override
    public Dimension render( Graphics2D g )
    {
        deltaTime = java.time.Instant.now().toEpochMilli() - lastTime;
        lastTime = java.time.Instant.now().toEpochMilli();

        long millisecondsPerHPRegen = (long)(getTicksPerHPRegen() * 0.6 * 1000);

        millisecondsSinceHPRegen = (millisecondsSinceHPRegen + deltaTime) % millisecondsPerHPRegen;
        hitpointsRegenerationPercentage = millisecondsSinceHPRegen / (double) millisecondsPerHPRegen;

        int currentHP = client.getBoostedSkillLevel(Skill.HITPOINTS);
        int maxHP = client.getRealSkillLevel(Skill.HITPOINTS);
        if ( currentHP == maxHP && config.hitpointsOutlineProgressThreshold() == OutlineProgressThreshold.RELATED_STAT_AT_MAX )
        {
            hitpointsRegenerationPercentage = 0;
        }
        else if ( currentHP > maxHP )
        {
            // Show it going down
            hitpointsRegenerationPercentage = 1 - hitpointsRegenerationPercentage;
        }

        if ( ticksToDelayedRecovery > 0 )
        {
            long millisecondsToHPRecovery = (long)(initialTicksToDelayedRecovery * 0.6 * 1000);

            millisecondsToDelayedRecovery = (millisecondsToDelayedRecovery - deltaTime) % millisecondsToHPRecovery;
            hitpointsRecoveryPercentage = millisecondsToDelayedRecovery / (double) millisecondsToHPRecovery;
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

        if ( config.hideHitpointsWhenSidebarPanelClosed() )
        {
            if (curViewport == null)
            {
                return null;
            }
        }

        if ( config.hitpointsRelativeToInventory() )
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

        if ( !(config.renderWarmthWithOptions() == WarmthRenderOptions.SHOW_DYNAMICALLY && isInWintertodtRegion()) && plugin.isHitpointsDisplayed() && config.renderHitpoints() && !uiElementsOpen )
        {
            barRenderer.renderBar( config, g, panelComponent, Vital.HITPOINTS, (ticksToDelayedRecovery > 0), client );
            return config.hitpointsSize();
        }

        //this.setPreferredLocation( new Point( x, y ) );

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
    public void onGameStateChanged( GameStateChanged ev )
    {
        if ( ev.getGameState() == GameState.HOPPING || ev.getGameState() == GameState.LOGIN_SCREEN )
        {
            ticksSinceHPRegen = -2; // For some reason this makes this accurate
            millisecondsSinceHPRegen = (long)(ticksSinceHPRegen * 0.6 * 1000);
        }

        if ( ev.getGameState().equals( GameState.LOGGED_IN ) )
        {
            isEating = false;
            updateInventoryState();
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged ev)
    {
        if (ev.getVarbitId() == Varbits.PRAYER_RAPID_HEAL)
        {
            ticksSinceHPRegen = 0;
            millisecondsSinceHPRegen = 0;
        }
    }

    @Subscribe
    public void onGameTick( GameTick event )
    {
        int ticksPerHPRegen = getTicksPerHPRegen();

        ticksSinceHPRegen = (ticksSinceHPRegen + 1) % ticksPerHPRegen;
        millisecondsSinceHPRegen = (long) (ticksSinceHPRegen * 0.6 * 1000);
        hitpointsRegenerationPercentage = ticksSinceHPRegen / (double) ticksPerHPRegen;

        int currentHP = client.getBoostedSkillLevel(Skill.HITPOINTS);
        int maxHP = client.getRealSkillLevel(Skill.HITPOINTS);
        if ( currentHP == maxHP && config.hitpointsOutlineProgressThreshold() == OutlineProgressThreshold.RELATED_STAT_AT_MAX )
        {
            hitpointsRegenerationPercentage = 0;
        }
        else if ( currentHP > maxHP )
        {
            // Show it going down
            hitpointsRegenerationPercentage = 1 - hitpointsRegenerationPercentage;
        }

        if ( ticksToDelayedRecovery > 0 )
        {
            int ticksToHPRecovery = initialTicksToDelayedRecovery;

            ticksToDelayedRecovery = (ticksToDelayedRecovery - 1) % ticksToHPRecovery;
            millisecondsToDelayedRecovery = (long) (ticksToDelayedRecovery * 0.6 * 1000);
            hitpointsRecoveryPercentage = (double) ticksToDelayedRecovery / ticksToHPRecovery;
        }
        else
        {
            ticksToDelayedRecovery = 0;
            millisecondsToDelayedRecovery = 0;
            initialTicksToDelayedRecovery = 0;
        }


        isEating = false;
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event)
    {
        if ( event.getContainerId() == InventoryID.EQUIPMENT.getId() )
        {
            ItemContainer equipment = event.getItemContainer();
            final boolean hasHPCape = (equipment.contains(ItemID.HITPOINTS_CAPE) || equipment.contains(ItemID.HITPOINTS_CAPET));
            final boolean hasRegenBracelet = equipment.contains(ItemID.REGEN_BRACELET);

            if ( hasHPCape != isWearingHPCape )
            {
                ticksSinceHPRegen = 0;
                isWearingHPCape = hasHPCape;
            }
            if ( hasRegenBracelet != isWearingRegenBracelet )
            {
                ticksSinceHPRegen = 0;
                isWearingRegenBracelet = hasRegenBracelet;
            }
        }
        else if ( event.getContainerId() == InventoryID.INVENTORY.getId() || event.getContainerId() == InventoryID.BANK.getId())
        {
            if ( isEating )
            {
                detectConsumableUsage();
                isEating = false;
            }
            updateInventoryState();
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
    public void onMenuOptionClicked(MenuOptionClicked event)
    {
        String menuOption = Text.removeTags( event.getMenuOption() );
        if ( menuOption.equals( "Eat" ) && isApplicableConsumable( event.getItemId() ) )
        {
            isEating = true;
        }
    }

    private BufferedImage loadSprite(int spriteId)
    {
        return spriteManager.getSprite(spriteId, 0);
    }

    private int getTicksPerHPRegen()
    {
        int ticksPerHPRegen = NORMAL_HP_REGEN_TICKS;
        if ( client.isPrayerActive( Prayer.RAPID_HEAL ) || isWearingHPCape )
        {
            ticksPerHPRegen /= 2;
        }
        if ( isWearingRegenBracelet )
        {
            ticksPerHPRegen /= 2;
        }

        return ticksPerHPRegen;
    }

    private boolean isInWintertodtRegion()
    {
        if (client.getLocalPlayer() != null)
        {
            return client.getLocalPlayer().getWorldLocation().getRegionID() == WINTERTODT_REGION;
        }

        return false;
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

    private boolean isApplicableConsumable(int itemId)
    {
        DelayedHeals item = DelayedHeals.getDelayedHealByItemId(itemId);
        return item != null;
    }

    private void handleConsumable(int itemId)
    {
        DelayedHeals item = DelayedHeals.getDelayedHealByItemId(itemId);

        ticksToDelayedRecovery = item.getTickDelay();
        initialTicksToDelayedRecovery = item.getTickDelay();
    }

    public void toggleLock( boolean start )
    {
        if ( deltaX == 0 && deltaY == 0 )
        {
            if ( start )
            {
                deltaX = config.debugHitpointsDeltaX();
                deltaY = config.debugHitpointsDeltaY();
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
        configManager.setConfiguration( "Custom Vital Bars", "debugHitpointsDeltaX", (int) deltaX );
        configManager.setConfiguration( "Custom Vital Bars", "debugHitpointsDeltaY", (int) deltaY );
    }

}
