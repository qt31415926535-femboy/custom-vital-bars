package net.runelite.client.plugins.customvitalbars;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.inject.Inject;

import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.AlternateSprites;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.util.ImageUtil;

public class CustomVitalBarsHitpointsOverlay extends OverlayPanel{

    private static final Color HEALTH_COLOR = new Color(225, 35, 0, 125);
    private static final Color POISONED_COLOR = new Color(0, 145, 0, 150);
    private static final Color VENOMED_COLOR = new Color(0, 65, 0, 150);
    private static final Color HEAL_COLOR = new Color(255, 112, 6, 150);
    private static final Color DISEASE_COLOR = new Color(255, 193, 75, 181);
    private static final Color PARASITE_COLOR = new Color(196, 62, 109, 181);

    private final Client client;

    private final CustomVitalBarsPlugin plugin;

    private final CustomVitalBarsConfig config;

    private final ItemStatChangesService itemStatService;

    private CustomVitalBarsComponent barRenderer;

    private boolean uiElementsOpen = false;

    private static final int NORMAL_HP_REGEN_TICKS = 100;

    @Getter
    private double hitpointsRegenerationPercentage;
    private int ticksSinceHPRegen;

    private long millisecondsSinceHPRegen;
    private long deltaTime;
    private long lastTime;

    private final SkillIconManager skillIconManager;
    private final SpriteManager spriteManager;
    private final Image heartDisease;
    private final Image heartPoison;
    private final Image heartVenom;

    @Inject
    CustomVitalBarsHitpointsOverlay( Client client, CustomVitalBarsPlugin plugin, CustomVitalBarsConfig config, SkillIconManager skillIconManager, ItemStatChangesService itemstatservice, SpriteManager spriteManager)
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

        heartDisease = ImageUtil.loadImageResource(AlternateSprites.class, AlternateSprites.DISEASE_HEART);
        heartPoison = ImageUtil.loadImageResource(AlternateSprites.class, AlternateSprites.POISON_HEART);
        heartVenom = ImageUtil.loadImageResource(AlternateSprites.class, AlternateSprites.VENOM_HEART);

        initRenderer();
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

                    return HEALTH_COLOR;
                },
                () -> HEAL_COLOR,
                () -> hitpointsRegenerationPercentage,
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

        long millisecondsPerHPRegen = (long)(NORMAL_HP_REGEN_TICKS * 0.6 * 1000);
        if (client.isPrayerActive(Prayer.RAPID_HEAL))
        {
            millisecondsPerHPRegen /= 2;
        }

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

        if ( plugin.isBarsDisplayed() && config.renderHitpoints() && !uiElementsOpen )
        {
            barRenderer.renderBar( config, g, panelComponent, Vital.HITPOINTS );
            return config.hitpointsSize();
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


    public void onGameStateChanged(GameStateChanged ev)
    {
        if (ev.getGameState() == GameState.HOPPING || ev.getGameState() == GameState.LOGIN_SCREEN)
        {
            ticksSinceHPRegen = -2; // For some reason this makes this accurate
            millisecondsSinceHPRegen = (long)(ticksSinceHPRegen * 0.6 * 1000);
        }
    }

    public void onVarbitChanged(VarbitChanged ev)
    {
        if (ev.getVarbitId() == Varbits.PRAYER_RAPID_HEAL)
        {
            ticksSinceHPRegen = 0;
            millisecondsSinceHPRegen = 0;
        }
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        int ticksPerHPRegen = NORMAL_HP_REGEN_TICKS;
        if (client.isPrayerActive(Prayer.RAPID_HEAL))
        {
            ticksPerHPRegen /= 2;
        }

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
}
