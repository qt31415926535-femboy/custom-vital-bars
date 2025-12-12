package com.neur0tox1n_.customvitalbars;

import net.runelite.client.config.*;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import java.awt.*;


@ConfigGroup("Custom Vital Bars")
public interface CustomVitalBarsConfig extends Config
{
	String GROUP = "Custom Vital Bars";

	@ConfigItem(
			position = 1,
			keyName = "showHitpoints",
			name = "Show Hitpoints",
			description = "Render Hitpoints bar"
	)
	default boolean renderHitpoints() { return false; }

	@ConfigSection(
			position = 2,
			name = "Hitpoints Bar Settings",
			description = "Hitpoints Bar Settings"
	)
	String hitpointsSettingsSection = "hitpointsSettings";

	@ConfigItem(
			position = 3,
			keyName = "hitpointsSize",
			name = "Hitpoints Bar Size",
			description = "Choose the size of the Hitpoints bar",
			section = hitpointsSettingsSection
	)
	default Dimension hitpointsSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 4,
			keyName = "hitpointsFullnessDirection",
			name = "Hitpoints Fullness Direction",
			description = "Choose the direction of fullness of the Hitpoints bar",
			section = hitpointsSettingsSection
	)
	default FullnessDirection hitpointsFullnessDirection()
	{
		return FullnessDirection.RIGHT;
	}

	@ConfigItem(
			position = 5,
			keyName = "hitpointsTextFormat",
			name = "Hitpoints Text Format",
			description = "Choose the formatting of the Hitpoints bar's text",
			section = hitpointsSettingsSection
	)
	default TextFormatting hitpointsTextFormat()
	{
		return TextFormatting.SHOW_CURRENT_AND_MAXIMUM;
	}

	@ConfigItem(
			position = 6,
			keyName = "hitpointsTextPosition",
			name = "Hitpoints Text Position",
			description = "Choose the general location of the Hitpoints bar's text",
			section = hitpointsSettingsSection
	)
	default PlacementDirection hitpointsTextPosition()
	{
		return PlacementDirection.TOP;
	}

	@ConfigItem(
			position = 7,
			keyName = "hitpointstextOffsetX",
			name = "Hitpoints Text Offset - X axis",
			description = "Choose the X-offset for the Hitpoints bar's text",
			section = hitpointsSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int hitpointsTextOffsetX() { return 0; }

	@ConfigItem(
			position = 8,
			keyName = "hitpointsTextOffsetY",
			name = "Hitpoints Text Offset - Y axis",
			description = "Choose the Y-offset for the Hitpoints bar's text",
			section = hitpointsSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int hitpointsTextOffsetY() { return 0; }

	@ConfigItem(
			position = 9,
			keyName = "hitpointsGlowThresholdMode",
			name = "Hitpoints Bar Glow Threshold Mode",
			description = "Choose how to determine the critical threshold at which to glow the Hitpoints bar",
			section = hitpointsSettingsSection
	)
	default ThresholdGlowMode hitpointsGlowThresholdMode()
	{
		return ThresholdGlowMode.BELOW_PERCENTAGE;
	}

	@ConfigItem(
			position = 10,
			keyName = "hitpointsGlowThresholdValue",
			name = "Hitpoints Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Hitpoints bar",
			section = hitpointsSettingsSection
	)
	default int hitpointsGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 11,
			keyName = "hitpointsOutlineThickness",
			name = "Hitpoints Bar Outline Effects Thickness",
			description = "How thick to draw an outline around the Hitpoints bar",
			section = hitpointsSettingsSection
	)
	@Range(
			min = 0,
			max = 5
	)
	default int hitpointsOutlineThickness() { return 3; }

	@ConfigItem(
			position = 12,
			keyName = "hitpointsOutlineProgressSelection",
			name = "Hitpoints Bar Outline Progress Selection",
			description = "What to show with the progress of the outline of the Hitpoints bar (natural regen / Hunter meats)",
			section = hitpointsSettingsSection
	)
	default OutlineProgressSelection hitpointsOutlineProgressSelection() { return OutlineProgressSelection.SHOW_NATURAL_AND_CONSUMABLE_PROGRESS; }

	@ConfigItem(
			position = 13,
			keyName = "hitpointsOutlineProgressThreshold",
			name = "Hitpoints Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Hitpoints bar",
			section = hitpointsSettingsSection
	)
	default OutlineProgressThreshold hitpointsOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 14,
			keyName = "hitpointsIconScale",
			name = "Hitpoints Bar Icon Image Scale",
			description = "Choose the size scaling of the Hitpoints bar icon",
			section = hitpointsSettingsSection
	)
	default double hitpointsIconScale() { return 0d; }

	@ConfigItem(
			position = 15,
			keyName = "hitpointsIconPosition",
			name = "Hitpoints Bar Icon Position",
			description = "Choose the general location of the Hitpoints bar icon",
			section = hitpointsSettingsSection
	)
	default PlacementDirection hitpointsIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 16,
			keyName = "hitpointsIconOffsetX",
			name = "Hitpoints Bar Icon Offset - X axis",
			description = "The X-offset for the icon of the Hitpoints bar",
			section = hitpointsSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int hitpointsIconOffsetX() { return 0; }

	@ConfigItem(
			position = 17,
			keyName = "hitpointsIconOffsetY",
			name = "Hitpoints Bar Icon Offset - Y axis",
			description = "The Y-offset for the icon of the Hitpoints bar",
			section = hitpointsSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int hitpointsIconOffsetY() { return 0; }

    @ConfigItem(
            position = 18,
            keyName = "hitpointsRelativeToSidebarPanel",
            name = "Lock Hitpoints bar relative to sidebar",
            description = "Whether or not to move the Hitpoints bar relative to the sidebar (panel with inventory/prayers/spells)",
            section = hitpointsSettingsSection
    )
    default boolean hitpointsRelativeToInventory() { return false; }

    @ConfigItem(
            position = 19,
            keyName = "hideHitpointsWhenSidebarPanelClosed",
            name = "Hide Hitpoints bar when sidebar is closed",
            description = "Whether or not to hide the Hitpoints bar when sidebar is closed (panel with inventory/prayers/spells)",
            section = hitpointsSettingsSection
    )
    default boolean hideHitpointsWhenSidebarPanelClosed() { return false; }

    @ConfigItem(
            position = 20,
            keyName = "hideHitpointsAfterCombatDelay",
            name = "Hide Hitpoints bar after combat delay",
            description = "Amount of ticks after combat has ended to hide the Hitpoints bar (0 = always show)",
            section = hitpointsSettingsSection
    )
    @Units(Units.TICKS)
    default int hideHitpointsAfterCombatDelay() { return 0; }

    @Alpha
    @ConfigItem(
            position = 21,
            keyName = "hitpointsTextColour",
            name = "Hitpoints Bar - Text Colour",
            description = "Colour of the text of the Hitpoints bar",
            section = hitpointsSettingsSection
    )
    default Color hitpointsTextColour() { return Color.WHITE; }

    @ConfigItem(
            position = 22,
            keyName = "hitpointsTextOutline",
            name = "Hitpoints Bar - Text Outlining",
            description = "Whether or not to outline the text of the Hitpoints bar",
            section = hitpointsSettingsSection
    )
    default boolean hitpointsTextOutline()
    {
        return false;
    }

    @Alpha
    @ConfigItem(
            position = 23,
            keyName = "hitpointsFramesColour",
            name = "Hitpoints Bar - Frame and Outline Frame Colour",
            description = "Colour of the Hitpoints bar Frame and Outline Frame",
            section = hitpointsSettingsSection
    )
    default Color hitpointsFramesColour() { return new Color(0, 0, 0, 255); }

    @Alpha
    @ConfigItem(
            position = 24,
            keyName = "hitpointsBackgroundColour",
            name = "Hitpoints Bar - Background Colour",
            description = "Background colour of the Hitpoints bar",
            section = hitpointsSettingsSection
    )
    default Color hitpointsBackgroundColour() { return new Color(0, 0, 0, 150); }

    @Alpha
    @ConfigItem(
            position = 25,
            keyName = "hitpointsMainColour",
            name = "Hitpoints Bar - Main Colour",
            description = "Default colour of the Hitpoints bar",
            section = hitpointsSettingsSection
    )
    default Color hitpointsMainColour()
    {
        return new Color(225, 35, 0, 175);
    }

    @Alpha
    @ConfigItem(
            position = 26,
            keyName = "hitpointsHealColour",
            name = "Hitpoints Bar - Heal Colour",
            description = "Colour of the part of the Hitpoints bar showing potential restoration",
            section = hitpointsSettingsSection
    )
    default Color hitpointsHealColour() { return new Color(255, 112, 6, 75 ); }

    @Alpha
    @ConfigItem(
            position = 27,
            keyName = "hitpointsOverhealColour",
            name = "Hitpoints Bar - Overheal Colour",
            description = "Colour of the part of the Hitpoints bar showing potential restoration when it would go over the maximum",
            section = hitpointsSettingsSection
    )
    default Color hitpointsOverhealColour() { return new Color(216, 255, 139, 75); }

    @Alpha
    @ConfigItem(
            position = 28,
            keyName = "hitpointsDelayedHealColour",
            name = "Hitpoints Bar - Delayed Heal Colour",
            description = "Colour of Hitpoints bar when a Hunter food item heal is pending",
            section = hitpointsSettingsSection
    )
    default Color hitpointsDelayedHealColour() { return new Color(255, 35, 111, 175 ); }

    @Alpha
    @ConfigItem(
            position = 29,
            keyName = "hitpointsPoisonedColour",
            name = "Hitpoints Bar - Poisoned Colour",
            description = "Colour of Hitpoints bar while Poisoned",
            section = hitpointsSettingsSection
    )
    default Color hitpointsPoisonedColour()
    {
        return new Color(0, 145, 0, 175 );
    }

    @Alpha
    @ConfigItem(
            position = 30,
            keyName = "hitpointsEnvenomedColour",
            name = "Hitpoints Bar - Envenomed Colour",
            description = "Colour of Hitpoints bar while Envenomed",
            section = hitpointsSettingsSection
    )
    default Color hitpointsEnvenomedColour() { return new Color(0, 65, 0, 175 ); }

    @Alpha
    @ConfigItem(
            position = 31,
            keyName = "hitpointsDiseasedColour",
            name = "Hitpoints Bar - Diseased Colour",
            description = "Colour of Hitpoints bar while Diseased",
            section = hitpointsSettingsSection
    )
    default Color hitpointsDiseasedColour() { return new Color(255, 193, 75, 175 ); }

    @Alpha
    @ConfigItem(
            position = 32,
            keyName = "hitpointsParasiteColour",
            name = "Hitpoints Bar - Parasite Infestation Colour",
            description = "Colour of Hitpoints bar while infested with a Parasite",
            section = hitpointsSettingsSection
    )
    default Color hitpointsParasiteColour() { return new Color(196, 62, 109, 175 ); }

    @ConfigItem(
			position = 33,
			keyName = "showPrayer",
			name = "Show Prayer",
			description = "Render Prayer bar"
	)
	default boolean renderPrayer() { return false; }

	@ConfigSection(
			position = 34,
			name = "Prayer Bar Settings",
			description = "Prayer Bar Settings"
	)
	String prayerSettingsSection = "prayerSettings";

	@ConfigItem(
			position = 35,
			keyName = "prayerSize",
			name = "Prayer Bar Size",
			description = "Choose the size of the Prayer bar",
			section = prayerSettingsSection
	)
	default Dimension prayerSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 36,
			keyName = "prayerFullnessDirection",
			name = "Prayer Fullness Direction",
			description = "Choose the direction of fullness of the Prayer bar",
			section = prayerSettingsSection
	)
	default FullnessDirection prayerFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 37,
			keyName = "prayerTextFormat",
			name = "Prayer Text Formatting",
			description = "Choose the formatting of the Prayer bar's text",
			section = prayerSettingsSection
	)
	default TextFormatting prayerTextFormat()
	{
		return TextFormatting.SHOW_CURRENT_AND_MAXIMUM;
	}

	@ConfigItem(
			position = 38,
			keyName = "prayerTextPosition",
			name = "Prayer Text Position",
			description = "Choose the general location of the Prayer bar's text",
			section = prayerSettingsSection
	)
	default PlacementDirection prayerTextPosition()
	{
		return PlacementDirection.TOP;
	}

	@ConfigItem(
			position = 39,
			keyName = "prayerTextOffsetX",
			name = "Prayer Bar Text Offset - X axis",
			description = "The X-offset for the text of the Prayer bar",
			section = prayerSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int prayerTextOffsetX() { return 0; }

	@ConfigItem(
			position = 40,
			keyName = "prayerTextOffsetY",
			name = "Prayer Bar Text Offset - Y axis",
			description = "The Y-offset for the text of the Prayer bar",
			section = prayerSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int prayerTextOffsetY() { return 0; }

	@ConfigItem(
			position = 41,
			keyName = "prayerGlowThresholdMode",
			name = "Prayer Bar Glow Threshold Mode",
			description = "Choose how to determine the critical threshold at which to glow the Prayer bar",
			section = prayerSettingsSection
	)
	default ThresholdGlowMode prayerGlowThresholdMode()
	{
		return ThresholdGlowMode.BELOW_PERCENTAGE;
	}

	@ConfigItem(
			position = 42,
			keyName = "prayerGlowThresholdValue",
			name = "Prayer Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Prayer bar",
			section = prayerSettingsSection
	)
	default int prayerGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 43,
			keyName = "prayerOutlineThickness",
			name = "Prayer Bar Outline Effects Thickness",
			description = "How thick to draw an outline around the Prayer bar",
			section = prayerSettingsSection
	)
	@Range(
			min = 0,
			max = 5
	)
	default int prayerOutlineThickness() { return 3; }

	@ConfigItem(
			position = 44,
			keyName = "prayerOutlineProgressSelection",
			name = "Prayer Bar Outline Progress Selection",
			description = "What to show with the progress of the outline of the Prayer bar (Prayer degen / Prayer Regeneration Potion)",
			section = prayerSettingsSection
	)
	default OutlineProgressSelection prayerOutlineProgressSelection() { return OutlineProgressSelection.SHOW_NATURAL_AND_CONSUMABLE_PROGRESS; }

	@ConfigItem(
			position = 45,
			keyName = "prayerOutlineProgressThreshold",
			name = "Prayer Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Prayer bar",
			section = prayerSettingsSection
	)
	default OutlineProgressThreshold prayerOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 46,
			keyName = "prayerIconScale",
			name = "Prayer Bar Icon Image Scale",
			description = "Choose the size scaling of the Prayer bar icon",
			section = prayerSettingsSection
	)
	default double prayerIconScale() { return 0d; }

	@ConfigItem(
			position = 47,
			keyName = "prayerIconPosition",
			name = "Prayer Bar Icon Position",
			description = "Choose the general location of the Prayer bar icon",
			section = prayerSettingsSection
	)
	default PlacementDirection prayerIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 48,
			keyName = "prayerIconOffsetX",
			name = "Prayer Bar Icon Offset - X axis",
			description = "The X-offset for the icon of the Prayer bar",
			section = prayerSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int prayerIconOffsetX() { return 0; }

	@ConfigItem(
			position = 49,
			keyName = "prayerIconOffsetY",
			name = "Prayer Bar Icon Offset - Y axis",
			description = "The Y-offset for the icon of the Prayer bar",
			section = prayerSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int prayerIconOffsetY() { return 0; }

    @ConfigItem(
            position = 50,
            keyName = "prayerRelativeToSidebarPanel",
            name = "Lock Prayer bar relative to sidebar",
            description = "Whether or not to move the Prayer bar relative to the sidebar (panel with inventory/prayers/spells)",
            section = prayerSettingsSection
    )
    default boolean prayerRelativeToInventory() { return false; }

    @ConfigItem(
            position = 51,
            keyName = "hidePrayerWhenSidebarPanelClosed",
            name = "Hide Prayer bar when sidebar is closed",
            description = "Whether or not to hide the Prayer bar when sidebar is closed (panel with inventory/prayers/spells)",
            section = prayerSettingsSection
    )
    default boolean hidePrayerWhenSidebarPanelClosed() { return false; }

    @ConfigItem(
            position = 52,
            keyName = "hidePrayerAfterCombatDelay",
            name = "Hide Prayer bar after combat delay",
            description = "Amount of ticks after combat has ended to hide the Prayer bar (0 = always show)",
            section = prayerSettingsSection
    )
    @Units(Units.TICKS)
    default int hidePrayerAfterCombatDelay() { return 0; }

    @Alpha
    @ConfigItem(
            position = 53,
            keyName = "prayerTextColour",
            name = "Prayer Bar - Text Colour",
            description = "Colour of the text of the Prayer bar",
            section = prayerSettingsSection
    )
    default Color prayerTextColour() { return Color.WHITE; }

    @ConfigItem(
            position = 54,
            keyName = "prayerTextOutline",
            name = "Prayer Bar - Text Outlining",
            description = "Whether or not to outline the text of the Prayer bar",
            section = prayerSettingsSection
    )
    default boolean prayerTextOutline()
    {
        return false;
    }

    @Alpha
    @ConfigItem(
            position = 55,
            keyName = "prayerFramesColour",
            name = "Prayer Bar - Frame and Outline Frame Colour",
            description = "Colour of the Prayer bar Frame and Outline Frame",
            section = prayerSettingsSection
    )
    default Color prayerFramesColour() { return new Color(0, 0, 0, 255); }

    @Alpha
    @ConfigItem(
            position = 56,
            keyName = "prayerBackgroundColour",
            name = "Prayer Bar - Background Colour",
            description = "Background colour of the Prayer bar",
            section = prayerSettingsSection
    )
    default Color prayerBackgroundColour()
    {
        return new Color(0, 0, 0, 150);
    }

    @Alpha
    @ConfigItem(
            position = 57,
            keyName = "prayerMainColour",
            name = "Prayer Bar - Main Colour",
            description = "Default colour of the Prayer bar",
            section = prayerSettingsSection
    )
    default Color prayerMainColour()
    {
        return new Color(50, 200, 200, 175);
    }

    @Alpha
    @ConfigItem(
            position = 58,
            keyName = "prayerHealColour",
            name = "Prayer Bar - Heal Colour",
            description = "Colour of the part of the Prayer bar showing potential restoration",
            section = prayerSettingsSection
    )
    default Color prayerHealColour() { return new Color(57, 255, 186, 75); }

    @Alpha
    @ConfigItem(
            position = 59,
            keyName = "prayerOverhealColour",
            name = "Prayer Bar - Overheal Colour",
            description = "Colour of the part of the Prayer bar showing potential restoration when it would go over the maximum",
            section = prayerSettingsSection
    )
    default Color prayerOverhealColour() { return new Color(216, 255, 139, 75); }

    @Alpha
    @ConfigItem(
            position = 60,
            keyName = "prayerActiveColour",
            name = "Prayer Bar - Prayer Active Colour",
            description = "Colour of Prayer bar while any Prayer is active",
            section = prayerSettingsSection
    )
    default Color prayerActiveColour() { return new Color(57, 255, 186, 175); }

    @Alpha
    @ConfigItem(
            position = 61,
            keyName = "prayerRegenActiveColour",
            name = "Prayer Bar - Prayer Regeneration Potion Active",
            description = "Colour of Prayer bar while a Prayer Regeneration potion is active",
            section = prayerSettingsSection
    )
    default Color prayerRegenActiveColour() { return new Color(15, 164, 112, 175); }

    @Alpha
    @ConfigItem(
            position = 62,
            keyName = "prayerRegenPrayerActiveColour",
            name = "Prayer Bar - Prayer Active Colour While Prayer Regeneration Potion Active",
            description = "Colour of Prayer bar while any Prayer is active and a Prayer Regeneration potion is also active",
            section = prayerSettingsSection
    )
    default Color prayerRegenActivePrayerActiveColour() { return new Color(120, 124, 102, 175); }

    @ConfigItem(
			position = 63,
			keyName = "showEnergy",
			name = "Show Energy",
			description = "Render Run Energy bar"
	)
	default boolean renderEnergy() { return false; }

	@ConfigSection(
			position = 64,
			name = "Energy Bar Settings",
			description = "Run Energy Bar Settings"
	)
	String energySettingsSection = "energySettings";

	@ConfigItem(
			position = 65,
			keyName = "energySize",
			name = "Energy Bar Size",
			description = "Choose the size of the Run Energy bar",
			section = energySettingsSection
	)
	default Dimension energySize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 66,
			keyName = "energyFullnessDirection",
			name = "Energy Fullness Direction",
			description = "Choose the direction of fullness of the Run Energy bar",
			section = energySettingsSection
	)
	default FullnessDirection energyFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 67,
			keyName = "energyTexFormat",
			name = "Energy Text Formatting",
			description = "Choose the formatting of the Run Energy bar's text",
			section = energySettingsSection
	)
	default TextFormatting energyTextFormat()
	{
		return TextFormatting.SHOW_CURRENT;
	}

	@ConfigItem(
			position = 68,
			keyName = "energyTextPosition",
			name = "Energy Text Position",
			description = "Choose the general location of the Run Energy bar's text",
			section = energySettingsSection
	)
	default PlacementDirection energyTextPosition()
	{
		return PlacementDirection.TOP;
	}

	@ConfigItem(
			position = 69,
			keyName = "energyTextOffsetX",
			name = "Energy Bar Text Offset - X axis",
			description = "The X-offset for the text of the Run Energy bar",
			section = energySettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int energyTextOffsetX() { return 0; }

	@ConfigItem(
			position = 70,
			keyName = "energyTextOffsetY",
			name = "Energy Bar Text Offset - Y axis",
			description = "The Y-offset for the text of the Run Energy bar",
			section = energySettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int energyTextOffsetY() { return 0; }

	@ConfigItem(
			position = 71,
			keyName = "energyGlowThresholdMode",
			name = "Energy Bar Glow Threshold Mode",
			description = "Choose how to determine the critical threshold at which to glow the Run Energy bar",
			section = energySettingsSection
	)
	default ThresholdGlowMode energyGlowThresholdMode()
	{
		return ThresholdGlowMode.BELOW_PERCENTAGE;
	}

	@ConfigItem(
			position = 72,
			keyName = "energyGlowThresholdValue",
			name = "Energy Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Run Energy bar",
			section = energySettingsSection
	)
	default int energyGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 73,
			keyName = "energyOutlineThickness",
			name = "Energy Bar Outline Effects Thickness",
			description = "How thick to draw an outline around the Run Energy bar",
			section = energySettingsSection
	)
	@Range(
			min = 0,
			max = 5
	)
	default int energyOutlineThickness() { return 3; }

	@ConfigItem(
			position = 74,
			keyName = "energyOutlineProgressSelection",
			name = "Energy Bar Outline Progress Selection",
			description = "What to show with the progress of the outline of the Run Energy bar (natural regen / Stamina Potion duration)",
			section = energySettingsSection
	)
	default OutlineProgressSelection energyOutlineProgressSelection() { return OutlineProgressSelection.SHOW_NATURAL_AND_CONSUMABLE_PROGRESS; }

	@ConfigItem(
			position = 75,
			keyName = "energyOutlineProgressThreshold",
			name = "Energy Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Run Energy bar",
			section = energySettingsSection
	)
	default OutlineProgressThreshold energyOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 76,
			keyName = "energyIconScale",
			name = "Energy Bar Icon Image Scale",
			description = "Choose the size scaling of the Run Energy bar icon",
			section = energySettingsSection
	)
	default double energyIconScale() { return 0d; }

	@ConfigItem(
			position = 77,
			keyName = "energyIconPosition",
			name = "Energy Bar Icon Position",
			description = "Choose the general location of the Run Energy bar icon",
			section = energySettingsSection
	)
	default PlacementDirection energyIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 78,
			keyName = "energyIconOffsetX",
			name = "Energy Bar Icon Offset - X axis",
			description = "The X-offset for the icon of the Run Energy bar",
			section = energySettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int energyIconOffsetX() { return 0; }

	@ConfigItem(
			position = 79,
			keyName = "energyIconOffsetY",
			name = "Energy Bar Icon Offset - Y axis",
			description = "The Y-offset for the icon of the Run Energy bar",
			section = energySettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int energyIconOffsetY() { return 0; }

    @ConfigItem(
            position = 80,
            keyName = "energyRelativeToSidebarPanel",
            name = "Lock Run Energy bar relative to sidebar",
            description = "Whether or not to move the Run Energy bar relative to the sidebar (panel with inventory/prayers/spells)",
            section = energySettingsSection
    )
    default boolean energyRelativeToInventory() { return false; }

    @ConfigItem(
            position = 81,
            keyName = "hideEnergyWhenSidebarPanelClosed",
            name = "Hide Run Energy bar when sidebar is closed",
            description = "Whether or not to hide the Run Energy bar when sidebar is closed (panel with inventory/prayers/spells)",
            section = energySettingsSection
    )
    default boolean hideEnergyWhenSidebarPanelClosed() { return false; }

    @ConfigItem(
            position = 82,
            keyName = "hideEnergyAfterCombatDelay",
            name = "Hide Run Energy bar after combat delay",
            description = "Amount of ticks after combat has ended to hide the Run Energy bar (0 = always show)",
            section = energySettingsSection
    )
    @Units(Units.TICKS)
    default int hideEnergyAfterCombatDelay() { return 0; }

    @Alpha
    @ConfigItem(
            position = 83,
            keyName = "energyTextColour",
            name = "Run Energy Bar - Text Colour",
            description = "Colour of the text of the Run Energy bar",
            section = energySettingsSection
    )
    default Color energyTextColour() { return Color.WHITE; }

    @ConfigItem(
            position = 84,
            keyName = "energyTextOutline",
            name = "Run Energy Bar - Text Outlining",
            description = "Whether or not to outline the text of the Run Energy bar",
            section = energySettingsSection
    )
    default boolean energyTextOutline()
    {
        return false;
    }

    @Alpha
    @ConfigItem(
            position = 85,
            keyName = "energyFramesColour",
            name = "Run Energy Bar - Frame and Outline Frame Colour",
            description = "Colour of the Run Energy bar Frame and Outline Frame",
            section = energySettingsSection
    )
    default Color energyFramesColour() { return new Color(0, 0, 0, 255); }

    @Alpha
    @ConfigItem(
            position = 86,
            keyName = "energyBackgroundColour",
            name = "Run Energy Bar - Background Colour",
            description = "Background colour of the Run Energy bar",
            section = energySettingsSection
    )
    default Color energyBackgroundColour()
    {
        return new Color(0, 0, 0, 150);
    }

    @Alpha
    @ConfigItem(
            position = 87,
            keyName = "energyMainColour",
            name = "Run Energy Bar - Main Colour",
            description = "Default colour of the Run Energy bar",
            section = energySettingsSection
    )
    default Color energyMainColour()
    {
        return new Color(199, 174, 0, 175);
    }

    @Alpha
    @ConfigItem(
            position = 88,
            keyName = "energyHealColour",
            name = "Run Energy Bar - Heal Colour",
            description = "Colour of the part of the Run Energy bar showing potential restoration",
            section = energySettingsSection
    )
    default Color energyHealColour() { return new Color (199,  118, 0, 75); }

    @Alpha
    @ConfigItem(
            position = 89,
            keyName = "energyOverhealColour",
            name = "Run Energy Bar - Overheal Colour",
            description = "Colour of the part of the Run Energy bar showing potential restoration when it would go over the maximum",
            section = energySettingsSection
    )
    default Color energyOverhealColour() { return new Color(216, 255, 139, 75); }

    @Alpha
    @ConfigItem(
            position = 90,
            keyName = "energyStaminaColour",
            name = "Run Energy Bar - Stamina Potion Active Colour",
            description = "Colour of Run Energy bar while a Stamina Potion is active",
            section = energySettingsSection
    )
    default Color energyStaminaColour() { return new Color(160, 124, 72, 175); };

    @ConfigItem(
			position = 91,
			keyName = "showSpecial",
			name = "Show Special",
			description = "Render Special Attack bar"
	)
	default boolean renderSpecial() { return false; }

	@ConfigSection(
			position = 92,
			name = "Special Bar Settings",
			description = "Special Bar Settings"
	)
	String specialSettingsSection = "specialSettings";

	@ConfigItem(
			position = 93,
			keyName = "specialSize",
			name = "Special Bar Size",
			description = "Choose the size of the Special bar",
			section = specialSettingsSection
	)
	default Dimension specialSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 94,
			keyName = "specialFullnessDirection",
			name = "Special Fullness Direction",
			description = "Choose the direction of fullness of the Special bar",
			section = specialSettingsSection
	)
	default FullnessDirection specialFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 95,
			keyName = "specialTextFormat",
			name = "Special Text Formatting",
			description = "Choose the formatting of the Special bar's text",
			section = specialSettingsSection
	)
	default TextFormatting specialTextFormat() { return TextFormatting.SHOW_CURRENT; }

	@ConfigItem(
			position = 96,
			keyName = "specialTextPosition",
			name = "Special Text Position",
			description = "Choose the general location of the Special bar's Text",
			section = specialSettingsSection
	)
	default PlacementDirection specialTextPosition()
	{
		return PlacementDirection.TOP;
	}

	@ConfigItem(
			position = 97,
			keyName = "specialTextOffsetX",
			name = "Special Bar Text Offset - X axis",
			description = "The X-offset for the text of the Special bar",
			section = specialSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int specialTextOffsetX() { return 0; }

	@ConfigItem(
			position = 98,
			keyName = "specialTextOffsetY",
			name = "Special Bar Text Offset - Y axis",
			description = "The Y-offset for the text of the Special bar",
			section = specialSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int specialTextOffsetY() { return 0; }

	@ConfigItem(
			position = 99,
			keyName = "specialGlowThresholdMode",
			name = "Special Bar Glow Threshold Mode",
			description = "Choose how to determine the critical threshold at which to glow the Special bar",
			section = specialSettingsSection
	)
	default ThresholdGlowMode specialGlowThresholdMode()
	{
		return ThresholdGlowMode.ABOVE_PERCENTAGE;
	}

	@ConfigItem(
			position = 100,
			keyName = "specialGlowThresholdValue",
			name = "Special Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Special bar",
			section = specialSettingsSection
	)
	default int specialGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 101,
			keyName = "specialOutlineThickness",
			name = "Special Bar Outline Effects Thickness",
			description = "How thick to draw an outline around the Special bar",
			section = specialSettingsSection
	)
	@Range(
			min = 0,
			max = 5
	)
	default int specialOutlineThickness() { return 3; }

	@ConfigItem(
			position = 102,
			keyName = "specialOutlineProgressSelection",
			name = "Special Bar Outline Progress Selection",
			description = "What to show with the progress of the outline of the Special bar (currently only supports natural regen)",
			section = specialSettingsSection
	)
	default OutlineProgressSelection specialOutlineProgressSelection() { return OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY; }

	@ConfigItem(
			position = 103,
			keyName = "specialOutlineProgressThreshold",
			name = "Special Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Special bar",
			section = specialSettingsSection
	)
	default OutlineProgressThreshold specialOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 104,
			keyName = "specialIconScale",
			name = "Special Bar Icon Image Scale",
			description = "Choose the size scaling of the Special bar icon",
			section = specialSettingsSection
	)
	default double specialIconScale() { return 0d; }

	@ConfigItem(
			position = 105,
			keyName = "specialIconPosition",
			name = "Special Bar Icon Position",
			description = "Choose the general location of the Special bar icon",
			section = specialSettingsSection
	)
	default PlacementDirection specialIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 106,
			keyName = "specialIconOffsetX",
			name = "Special Bar Icon Offset - X axis",
			description = "The X-offset for the icon of the Special bar",
			section = specialSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int specialIconOffsetX() { return 0; }

	@ConfigItem(
			position = 107,
			keyName = "specialIconOffsetY",
			name = "Special Bar Icon Offset - Y axis",
			description = "The Y-offset for the icon of the Special bar",
			section = specialSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int specialIconOffsetY() { return 0; }

    @ConfigItem(
            position = 108,
            keyName = "specialRelativeToSidebarPanel",
            name = "Lock Special bar relative to sidebar",
            description = "Whether or not to move the Special bar relative to the sidebar (panel with inventory/prayers/spells)",
            section = specialSettingsSection
    )
    default boolean specialRelativeToInventory() { return false; }

    @ConfigItem(
            position = 109,
            keyName = "hideSpecialWhenSidebarPanelClosed",
            name = "Hide Special bar when sidebar is closed",
            description = "Whether or not to hide the Special bar when sidebar is closed (panel with inventory/prayers/spells)",
            section = specialSettingsSection
    )
    default boolean hideSpecialWhenSidebarPanelClosed() { return false; }

    @ConfigItem(
            position = 110,
            keyName = "hideSpecialAfterCombatDelay",
            name = "Hide Special bar after combat delay",
            description = "Amount of ticks after combat has ended to hide the Special bar (0 = always show)",
            section = specialSettingsSection
    )
    @Units(Units.TICKS)
    default int hideSpecialAfterCombatDelay() { return 0; }

    @Alpha
    @ConfigItem(
            position = 111,
            keyName = "specialTextColour",
            name = "Special Bar - Text Colour",
            description = "Colour of the text of the Special bar",
            section = specialSettingsSection
    )
    default Color specialTextColour() { return Color.WHITE; }

    @ConfigItem(
            position = 112,
            keyName = "specialTextOutline",
            name = "Special Bar - Text Outlining",
            description = "Whether or not to outline the text of the Special bar",
            section = specialSettingsSection
    )
    default boolean specialTextOutline()
    {
        return false;
    }

    @Alpha
    @ConfigItem(
            position = 113,
            keyName = "specialFramesColour",
            name = "Special Bar - Frame and Outline Frame Colour",
            description = "Colour of the Special bar Frame and Outline Frame",
            section = specialSettingsSection
    )
    default Color specialFramesColour() { return new Color(0, 0, 0, 255); }

    @Alpha
    @ConfigItem(
            position = 114,
            keyName = "specialBackgroundColour",
            name = "Special Bar - Background Colour",
            description = "Background colour of the Special bar",
            section = specialSettingsSection
    )
    default Color specialBackgroundColour()
    {
        return new Color(0, 0, 0, 150);
    }

    @Alpha
    @ConfigItem(
            position = 115,
            keyName = "specialMainColour",
            name = "Special Bar - Main Colour",
            description = "Default colour of the Special bar",
            section = specialSettingsSection
    )
    default Color specialMainColour()
    {
        return new Color(3, 153, 0, 175);
    }

    @Alpha
    @ConfigItem(
            position = 116,
            keyName = "specialHealColour",
            name = "Special Bar - Heal Colour",
            description = "Colour of the part of the Special bar showing potential restoration",
            section = specialSettingsSection
    )
    default Color specialHealColour() { return new Color (2,  89, 0, 75); }

    @Alpha
    @ConfigItem(
            position = 117,
            keyName = "specialOverhealColour",
            name = "Special Bar - Overheal Colour",
            description = "Colour of the part of the Special bar showing potential restoration when it would go over the maximum",
            section = specialSettingsSection
    )
    default Color specialOverhealColour() { return new Color(216, 255, 139, 75); }

    @Alpha
    @ConfigItem(
            position = 118,
            keyName = "specialSurgeCooldownColour",
            name = "Special Bar - Surge Potion Cooldown Colour",
            description = "Colour of Special bar while Surge Potions are on cooldown",
            section = specialSettingsSection
    )
    default Color specialSurgeCooldownColour() { return new Color(106, 200, 104, 175); };

    @ConfigItem(
            position = 119,
            keyName = "showWarmthWithOptions",
            name = "Show Warmth (Conditionally?)",
            description = "Choose whether to hide, always show, or dynamically show the Warmth bar. Dynamically showing means the Warmth bar will display while the Hitpoints bar is hidden."
    )
    default WarmthRenderOptions renderWarmthWithOptions() { return WarmthRenderOptions.SHOW_DYNAMICALLY; }

    @ConfigSection(
            position = 120,
            name = "Warmth Bar Settings",
            description = "Warmth Bar Settings"
    )
    String warmthSettingsSection = "warmthSettings";

	@ConfigItem(
			position = 121,
			keyName = "warmthSize",
			name = "Warmth Bar Size",
			description = "Choose the size of the Warmth bar",
			section = warmthSettingsSection
	)
	default Dimension warmthSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 122,
			keyName = "warmthFullnessDirection",
			name = "Warmth Fullness Direction",
			description = "Choose the direction of fullness of the Warmth bar",
			section = warmthSettingsSection
	)
	default FullnessDirection warmthFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 123,
			keyName = "warmthTextFormat",
			name = "Warmth Text Formatting",
			description = "Choose the formatting of the Warmth bar's text",
			section = warmthSettingsSection
	)
	default TextFormatting warmthTextFormat()
	{
		return TextFormatting.SHOW_CURRENT;
	}

	@ConfigItem(
			position = 124,
			keyName = "warmthTextPosition",
			name = "Warmth Text Position",
			description = "Choose the general location of the Warmth bar's Text",
			section = warmthSettingsSection
	)
	default PlacementDirection warmthTextPosition()
	{
		return PlacementDirection.TOP;
	}

	@ConfigItem(
			position = 125,
			keyName = "warmthTextOffsetX",
			name = "Warmth Bar Text Offset - X axis",
			description = "The X-offset for the text of the Warmth bar",
			section = warmthSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int warmthTextOffsetX() { return 0; }

	@ConfigItem(
			position = 126,
			keyName = "warmthTextOffsetY",
			name = "Warmth Bar Text Offset - Y axis",
			description = "The Y-offset for the text of the Warmth bar",
			section = warmthSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int warmthTextOffsetY() { return 0; }

	@ConfigItem(
			position = 127,
			keyName = "warmthGlowThresholdMode",
			name = "Warmth Bar Glow Threshold Mode",
			description = "Choose how to determine the critical threshold at which to glow the Warmth bar",
			section = warmthSettingsSection
	)
	default ThresholdGlowMode warmthGlowThresholdMode()
	{
		return ThresholdGlowMode.ABOVE_PERCENTAGE;
	}

	@ConfigItem(
			position = 128,
			keyName = "warmthGlowThresholdValue",
			name = "Warmth Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Warmth bar",
			section = warmthSettingsSection
	)
	default int warmthGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 129,
			keyName = "warmthOutlineThickness",
			name = "Warmth Bar Outline Effects Thickness",
			description = "How thick to draw an outline around the Warmth bar",
			section = warmthSettingsSection
	)
	@Range(
			min = 0,
			max = 5
	)
	default int warmthOutlineThickness() { return 3; }

	@ConfigItem(
			position = 130,
			keyName = "warmthOutlineProgressSelection",
			name = "Warmth Bar Outline Progress Selection",
			description = "What to show with the progress of the outline of the Warmth bar (currently only supports natural regen)",
			section = warmthSettingsSection
	)
	default OutlineProgressSelection warmthOutlineProgressSelection() { return OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY; }

	@ConfigItem(
			position = 131,
			keyName = "warmthOutlineProgressThreshold",
			name = "Warmth Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Warmth bar",
			section = warmthSettingsSection
	)
	default OutlineProgressThreshold warmthOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 132,
			keyName = "warmthIconScale",
			name = "Warmth Bar Icon Image Scale",
			description = "Choose the size scaling of the Warmth bar icon",
			section = warmthSettingsSection
	)
	default double warmthIconScale() { return 0d; }

	@ConfigItem(
			position = 133,
			keyName = "warmthIconPosition",
			name = "Warmth Bar Icon Position",
			description = "Choose the general location of the Warmth bar icon",
			section = warmthSettingsSection
	)
	default PlacementDirection warmthIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 134,
			keyName = "warmthIconOffsetX",
			name = "Warmth Bar Icon Offset - X axis",
			description = "The X-offset for the icon of the Warmth bar",
			section = warmthSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int warmthIconOffsetX() { return 0; }

	@ConfigItem(
			position = 135,
			keyName = "warmthIconOffsetY",
			name = "Warmth Bar Icon Offset - Y axis",
			description = "The Y-offset for the icon of the Warmth bar",
			section = warmthSettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int warmthIconOffsetY() { return 0; }

    @ConfigItem(
            position = 136,
            keyName = "warmthRelativeToSidebarPanel",
            name = "Lock Warmth bar relative to sidebar",
            description = "Whether or not to move the Warmth bar relative to the sidebar (panel with inventory/prayers/spells)",
            section = warmthSettingsSection
    )
    default boolean warmthRelativeToInventory() { return false; }

    @ConfigItem(
            position = 137,
            keyName = "hideWarmthWhenSidebarPanelClosed",
            name = "Hide Warmth bar when sidebar is closed",
            description = "Whether or not to hide the Warmth bar when sidebar is closed (panel with inventory/prayers/spells)",
            section = warmthSettingsSection
    )
    default boolean hideWarmthWhenSidebarPanelClosed() { return false; }

    @ConfigItem(
            position = 138,
            keyName = "hideWarmthAfterCombatDelay",
            name = "Hide Warmth bar after combat delay",
            description = "Amount of ticks after combat has ended to hide the Warmth bar (0 = always show)",
            section = warmthSettingsSection
    )
    @Units(Units.TICKS)
    default int hideWarmthAfterCombatDelay() { return 0; }

    @Alpha
    @ConfigItem(
            position = 139,
            keyName = "warmthTextColour",
            name = "Warmth Bar - Text Colour",
            description = "Colour of the text of the Warmth bar",
            section = warmthSettingsSection
    )
    default Color warmthTextColour() { return Color.WHITE; }

    @ConfigItem(
            position = 140,
            keyName = "warmthTextOutline",
            name = "Warmth Bar - Text Outlining",
            description = "Whether or not to outline the text of the Warmth bar",
            section = warmthSettingsSection
    )
    default boolean warmthTextOutline()
    {
        return false;
    }

    @Alpha
    @ConfigItem(
            position = 141,
            keyName = "warmthFramesColour",
            name = "Warmth Bar - Frame and Outline Frame Colour",
            description = "Colour of the Warmth bar Frame and Outline Frame",
            section = warmthSettingsSection
    )
    default Color warmthFramesColour() { return new Color(0, 0, 0, 255); }

    @Alpha
    @ConfigItem(
            position = 142,
            keyName = "warmthBackgroundColour",
            name = "Warmth Bar - Background Colour",
            description = "Background colour of the Warmth bar",
            section = warmthSettingsSection
    )
    default Color warmthBackgroundColour()
    {
        return new Color(0, 0, 0, 150);
    }

    @Alpha
    @ConfigItem(
            position = 143,
            keyName = "warmthMainColour",
            name = "Warmth Bar - Main Colour",
            description = "Default colour of the Warmth bar",
            section = warmthSettingsSection
    )
    default Color warmthMainColour()
    {
        return new Color(244, 97, 0, 175);
    }

    @Alpha
    @ConfigItem(
            position = 144,
            keyName = "warmthHealColour",
            name = "Warmth Bar - Heal Colour",
            description = "Colour of the part of the Warmth bar showing potential restoration",
            section = warmthSettingsSection
    )
    default Color warmthHealColour() { return new Color (187,  75, 0, 75); }

    @Alpha
    @ConfigItem(
            position = 145,
            keyName = "warmthOverhealColour",
            name = "Warmth Bar - Overheal Colour",
            description = "Colour of the part of the Warmth bar showing potential restoration when it would go over the maximum",
            section = warmthSettingsSection
    )
    default Color warmthOverhealColour() { return new Color(216, 255, 139, 75); }

    @ConfigItem(
			position = 146,
			keyName = "enableRestorationBars",
			name = "Show Restores",
			description = "Visually shows how much will be restored to your vital bars."
	)
	default boolean enableRestorationBars() { return true; }

	@ConfigItem(
			position = 147,
			keyName = "hideWhenLargeInterfacePanelsOpen",
			name = "Hide when large UI elements are open",
			description = "Whether or not to hide the vital bars when large UI elements are open (ie Bank, Clue Scroll text)"
	)
	default boolean hideWhenLargeInterfacePanelsOpen() { return true; }

    @ConfigSection(
            position = 148,
            name = "DEBUG",
            description = "various debug config variables to store coordinate data. please don't touch!"
    )
    String debugSection = "debugSection";

    @ConfigItem(
            position = 149,
            keyName = "debugSidebarPanelX",
            name = "debugSidebarPanelX",
            description = "debugSidebarPanelX",
            section = debugSection
    )
    default int debugSidebarPanelX() { return 0; }

    @ConfigItem(
            position = 150,
            keyName = "debugSidebarPanelY",
            name = "debugSidebarPanelY",
            description = "debugSidebarPanelY",
            section = debugSection
    )
    default int debugSidebarPanelY() { return 0; }

    @ConfigItem(
            position = 151,
            keyName = "debugEnergyDeltaX",
            name = "debugEnergyDeltaX",
            description = "debugEnergyDeltaX",
            section = debugSection
    )
    default int debugEnergyDeltaX() { return 0; }

    @ConfigItem(
            position = 152,
            keyName = "debugEnergyDeltaY",
            name = "debugEnergyDeltaY",
            description = "debugEnergyDeltaY",
            section = debugSection
    )
    default int debugEnergyDeltaY() { return 0; }

    @ConfigItem(
            position = 153,
            keyName = "debugHitpointsDeltaX",
            name = "debugHitpointsDeltaX",
            description = "debugHitpointsDeltaX",
            section = debugSection
    )
    default int debugHitpointsDeltaX() { return 0; }

    @ConfigItem(
            position = 154,
            keyName = "debugHitpointsDeltaY",
            name = "debugHitpointsDeltaY",
            description = "debugHitpointsDeltaY",
            section = debugSection
    )
    default int debugHitpointsDeltaY() { return 0; }

    @ConfigItem(
            position = 155,
            keyName = "debugPrayerDeltaX",
            name = "debugPrayerDeltaX",
            description = "debugPrayerDeltaX",
            section = debugSection
    )
    default int debugPrayerDeltaX() { return 0; }

    @ConfigItem(
            position = 156,
            keyName = "debugPrayerDeltaY",
            name = "debugPrayerDeltaY",
            description = "debugPrayerDeltaY",
            section = debugSection
    )
    default int debugPrayerDeltaY() { return 0; }

    @ConfigItem(
            position = 157,
            keyName = "debugSpecialDeltaX",
            name = "debugSpecialDeltaX",
            description = "debugSpecialDeltaX",
            section = debugSection
    )
    default int debugSpecialDeltaX() { return 0; }

    @ConfigItem(
            position = 158,
            keyName = "debugSpecialDeltaY",
            name = "debugSpecialDeltaY",
            description = "debugSpecialDeltaY",
            section = debugSection
    )
    default int debugSpecialDeltaY() { return 0; }

    @ConfigItem(
            position = 159,
            keyName = "debugWarmthDeltaX",
            name = "debugWarmthDeltaX",
            description = "debugWarmthDeltaX",
            section = debugSection
    )
    default int debugWarmthDeltaX() { return 0; }

    @ConfigItem(
            position = 160,
            keyName = "debugWarmthDeltaY",
            name = "debugWarmthDeltaY",
            description = "debugWarmthDeltaY",
            section = debugSection
    )
    default int debugWarmthDeltaY() { return 0; }
}
