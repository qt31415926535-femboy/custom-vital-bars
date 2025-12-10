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

    @ConfigItem(
			position = 21,
			keyName = "showPrayer",
			name = "Show Prayer",
			description = "Render Prayer bar"
	)
	default boolean renderPrayer() { return false; }

	@ConfigSection(
			position = 22,
			name = "Prayer Bar Settings",
			description = "Prayer Bar Settings"
	)
	String prayerSettingsSection = "prayerSettings";

	@ConfigItem(
			position = 23,
			keyName = "prayerSize",
			name = "Prayer Bar Size",
			description = "Choose the size of the Prayer bar",
			section = prayerSettingsSection
	)
	default Dimension prayerSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 24,
			keyName = "prayerFullnessDirection",
			name = "Prayer Fullness Direction",
			description = "Choose the direction of fullness of the Prayer bar",
			section = prayerSettingsSection
	)
	default FullnessDirection prayerFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 25,
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
			position = 26,
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
			position = 27,
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
			position = 28,
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
			position = 29,
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
			position = 30,
			keyName = "prayerGlowThresholdValue",
			name = "Prayer Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Prayer bar",
			section = prayerSettingsSection
	)
	default int prayerGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 31,
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
			position = 32,
			keyName = "prayerOutlineProgressSelection",
			name = "Prayer Bar Outline Progress Selection",
			description = "What to show with the progress of the outline of the Prayer bar (Prayer degen / Prayer Regeneration Potion)",
			section = prayerSettingsSection
	)
	default OutlineProgressSelection prayerOutlineProgressSelection() { return OutlineProgressSelection.SHOW_NATURAL_AND_CONSUMABLE_PROGRESS; }

	@ConfigItem(
			position = 33,
			keyName = "prayerOutlineProgressThreshold",
			name = "Prayer Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Prayer bar",
			section = prayerSettingsSection
	)
	default OutlineProgressThreshold prayerOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 34,
			keyName = "prayerIconScale",
			name = "Prayer Bar Icon Image Scale",
			description = "Choose the size scaling of the Prayer bar icon",
			section = prayerSettingsSection
	)
	default double prayerIconScale() { return 0d; }

	@ConfigItem(
			position = 35,
			keyName = "prayerIconPosition",
			name = "Prayer Bar Icon Position",
			description = "Choose the general location of the Prayer bar icon",
			section = prayerSettingsSection
	)
	default PlacementDirection prayerIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 36,
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
			position = 37,
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
            position = 38,
            keyName = "prayerRelativeToSidebarPanel",
            name = "Lock Prayer bar relative to sidebar",
            description = "Whether or not to move the Prayer bar relative to the sidebar (panel with inventory/prayers/spells)",
            section = prayerSettingsSection
    )
    default boolean prayerRelativeToInventory() { return false; }

    @ConfigItem(
            position = 39,
            keyName = "hidePrayerWhenSidebarPanelClosed",
            name = "Hide Prayer bar when sidebar is closed",
            description = "Whether or not to hide the Prayer bar when sidebar is closed (panel with inventory/prayers/spells)",
            section = prayerSettingsSection
    )
    default boolean hidePrayerWhenSidebarPanelClosed() { return false; }

    @ConfigItem(
            position = 40,
            keyName = "hidePrayerAfterCombatDelay",
            name = "Hide Prayer bar after combat delay",
            description = "Amount of ticks after combat has ended to hide the Prayer bar (0 = always show)",
            section = prayerSettingsSection
    )
    @Units(Units.TICKS)
    default int hidePrayerAfterCombatDelay() { return 0; }

    @ConfigItem(
			position = 41,
			keyName = "showEnergy",
			name = "Show Energy",
			description = "Render Run Energy bar"
	)
	default boolean renderEnergy() { return false; }

	@ConfigSection(
			position = 42,
			name = "Energy Bar Settings",
			description = "Run Energy Bar Settings"
	)
	String energySettingsSection = "energySettings";

	@ConfigItem(
			position = 43,
			keyName = "energySize",
			name = "Energy Bar Size",
			description = "Choose the size of the Run Energy bar",
			section = energySettingsSection
	)
	default Dimension energySize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 44,
			keyName = "energyFullnessDirection",
			name = "Energy Fullness Direction",
			description = "Choose the direction of fullness of the Run Energy bar",
			section = energySettingsSection
	)
	default FullnessDirection energyFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 45,
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
			position = 46,
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
			position = 47,
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
			position = 48,
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
			position = 49,
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
			position = 50,
			keyName = "energyGlowThresholdValue",
			name = "Energy Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Run Energy bar",
			section = energySettingsSection
	)
	default int energyGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 51,
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
			position = 52,
			keyName = "energyOutlineProgressSelection",
			name = "Energy Bar Outline Progress Selection",
			description = "What to show with the progress of the outline of the Run Energy bar (natural regen / Stamina Potion duration)",
			section = energySettingsSection
	)
	default OutlineProgressSelection energyOutlineProgressSelection() { return OutlineProgressSelection.SHOW_NATURAL_AND_CONSUMABLE_PROGRESS; }

	@ConfigItem(
			position = 53,
			keyName = "energyOutlineProgressThreshold",
			name = "Energy Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Run Energy bar",
			section = energySettingsSection
	)
	default OutlineProgressThreshold energyOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 54,
			keyName = "energyIconScale",
			name = "Energy Bar Icon Image Scale",
			description = "Choose the size scaling of the Run Energy bar icon",
			section = energySettingsSection
	)
	default double energyIconScale() { return 0d; }

	@ConfigItem(
			position = 55,
			keyName = "energyIconPosition",
			name = "Energy Bar Icon Position",
			description = "Choose the general location of the Run Energy bar icon",
			section = energySettingsSection
	)
	default PlacementDirection energyIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 56,
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
			position = 57,
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
            position = 58,
            keyName = "energyRelativeToSidebarPanel",
            name = "Lock Run Energy bar relative to sidebar",
            description = "Whether or not to move the Run Energy bar relative to the sidebar (panel with inventory/prayers/spells)",
            section = energySettingsSection
    )
    default boolean energyRelativeToInventory() { return false; }

    @ConfigItem(
            position = 59,
            keyName = "hideEnergyWhenSidebarPanelClosed",
            name = "Hide Run Energy bar when sidebar is closed",
            description = "Whether or not to hide the Run Energy bar when sidebar is closed (panel with inventory/prayers/spells)",
            section = energySettingsSection
    )
    default boolean hideEnergyWhenSidebarPanelClosed() { return false; }

    @ConfigItem(
            position = 60,
            keyName = "hideEnergyAfterCombatDelay",
            name = "Hide Run Energy bar after combat delay",
            description = "Amount of ticks after combat has ended to hide the Run Energy bar (0 = always show)",
            section = energySettingsSection
    )
    @Units(Units.TICKS)
    default int hideEnergyAfterCombatDelay() { return 0; }

    @ConfigItem(
			position = 61,
			keyName = "showSpecial",
			name = "Show Special",
			description = "Render Special Attack bar"
	)
	default boolean renderSpecial() { return false; }

	@ConfigSection(
			position = 62,
			name = "Special Bar Settings",
			description = "Special Bar Settings"
	)
	String specialSettingsSection = "specialSettings";

	@ConfigItem(
			position = 63,
			keyName = "specialSize",
			name = "Special Bar Size",
			description = "Choose the size of the Special bar",
			section = specialSettingsSection
	)
	default Dimension specialSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 64,
			keyName = "specialFullnessDirection",
			name = "Special Fullness Direction",
			description = "Choose the direction of fullness of the Special bar",
			section = specialSettingsSection
	)
	default FullnessDirection specialFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 65,
			keyName = "specialTextFormat",
			name = "Special Text Formatting",
			description = "Choose the formatting of the Special bar's text",
			section = specialSettingsSection
	)
	default TextFormatting specialTextFormat()
	{
		return TextFormatting.SHOW_CURRENT;
	}

	@ConfigItem(
			position = 66,
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
			position = 67,
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
			position = 68,
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
			position = 69,
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
			position = 70,
			keyName = "specialGlowThresholdValue",
			name = "Special Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Special bar",
			section = specialSettingsSection
	)
	default int specialGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 71,
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
			position = 72,
			keyName = "specialOutlineProgressSelection",
			name = "Special Bar Outline Progress Selection",
			description = "What to show with the progress of the outline of the Special bar (currently only supports natural regen)",
			section = specialSettingsSection
	)
	default OutlineProgressSelection specialOutlineProgressSelection() { return OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY; }

	@ConfigItem(
			position = 73,
			keyName = "specialOutlineProgressThreshold",
			name = "Special Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Special bar",
			section = specialSettingsSection
	)
	default OutlineProgressThreshold specialOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 74,
			keyName = "specialIconScale",
			name = "Special Bar Icon Image Scale",
			description = "Choose the size scaling of the Special bar icon",
			section = specialSettingsSection
	)
	default double specialIconScale() { return 0d; }

	@ConfigItem(
			position = 75,
			keyName = "specialIconPosition",
			name = "Special Bar Icon Position",
			description = "Choose the general location of the Special bar icon",
			section = specialSettingsSection
	)
	default PlacementDirection specialIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 76,
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
			position = 77,
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
            position = 78,
            keyName = "specialRelativeToSidebarPanel",
            name = "Lock Special bar relative to sidebar",
            description = "Whether or not to move the Special bar relative to the sidebar (panel with inventory/prayers/spells)",
            section = specialSettingsSection
    )
    default boolean specialRelativeToInventory() { return false; }

    @ConfigItem(
            position = 79,
            keyName = "hideSpecialWhenSidebarPanelClosed",
            name = "Hide Special bar when sidebar is closed",
            description = "Whether or not to hide the Special bar when sidebar is closed (panel with inventory/prayers/spells)",
            section = specialSettingsSection
    )
    default boolean hideSpecialWhenSidebarPanelClosed() { return false; }

    @ConfigItem(
            position = 80,
            keyName = "hideSpecialAfterCombatDelay",
            name = "Hide Special bar after combat delay",
            description = "Amount of ticks after combat has ended to hide the Special bar (0 = always show)",
            section = specialSettingsSection
    )
    @Units(Units.TICKS)
    default int hideSpecialAfterCombatDelay() { return 0; }

    @ConfigItem(
            position = 81,
            keyName = "showWarmthWithOptions",
            name = "Show Warmth (Conditionally?)",
            description = "Choose whether to hide, always show, or dynamically show the Warmth bar. Dynamically showing means the Warmth bar will display while the Hitpoints bar is hidden."
    )
    default WarmthRenderOptions renderWarmthWithOptions() { return WarmthRenderOptions.SHOW_DYNAMICALLY; }

    @ConfigSection(
            position = 82,
            name = "Warmth Bar Settings",
            description = "Warmth Bar Settings"
    )
    String warmthSettingsSection = "warmthSettings";

	@ConfigItem(
			position = 83,
			keyName = "warmthSize",
			name = "Warmth Bar Size",
			description = "Choose the size of the Warmth bar",
			section = warmthSettingsSection
	)
	default Dimension warmthSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 84,
			keyName = "warmthFullnessDirection",
			name = "Warmth Fullness Direction",
			description = "Choose the direction of fullness of the Warmth bar",
			section = warmthSettingsSection
	)
	default FullnessDirection warmthFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 85,
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
			position = 86,
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
			position = 87,
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
			position = 88,
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
			position = 89,
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
			position = 90,
			keyName = "warmthGlowThresholdValue",
			name = "Warmth Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Warmth bar",
			section = warmthSettingsSection
	)
	default int warmthGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 91,
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
			position = 92,
			keyName = "warmthOutlineProgressSelection",
			name = "Warmth Bar Outline Progress Selection",
			description = "What to show with the progress of the outline of the Warmth bar (currently only supports natural regen)",
			section = warmthSettingsSection
	)
	default OutlineProgressSelection warmthOutlineProgressSelection() { return OutlineProgressSelection.SHOW_NATURAL_PROGRESS_ONLY; }

	@ConfigItem(
			position = 93,
			keyName = "warmthOutlineProgressThreshold",
			name = "Warmth Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Warmth bar",
			section = warmthSettingsSection
	)
	default OutlineProgressThreshold warmthOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 94,
			keyName = "warmthIconScale",
			name = "Warmth Bar Icon Image Scale",
			description = "Choose the size scaling of the Warmth bar icon",
			section = warmthSettingsSection
	)
	default double warmthIconScale() { return 0d; }

	@ConfigItem(
			position = 95,
			keyName = "warmthIconPosition",
			name = "Warmth Bar Icon Position",
			description = "Choose the general location of the Warmth bar icon",
			section = warmthSettingsSection
	)
	default PlacementDirection warmthIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 96,
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
			position = 97,
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
            position = 98,
            keyName = "warmthRelativeToSidebarPanel",
            name = "Lock Warmth bar relative to sidebar",
            description = "Whether or not to move the Warmth bar relative to the sidebar (panel with inventory/prayers/spells)",
            section = warmthSettingsSection
    )
    default boolean warmthRelativeToInventory() { return false; }

    @ConfigItem(
            position = 99,
            keyName = "hideWarmthWhenSidebarPanelClosed",
            name = "Hide Warmth bar when sidebar is closed",
            description = "Whether or not to hide the Warmth bar when sidebar is closed (panel with inventory/prayers/spells)",
            section = warmthSettingsSection
    )
    default boolean hideWarmthWhenSidebarPanelClosed() { return false; }

    @ConfigItem(
            position = 100,
            keyName = "hideWarmthAfterCombatDelay",
            name = "Hide Warmth bar after combat delay",
            description = "Amount of ticks after combat has ended to hide the Warmth bar (0 = always show)",
            section = warmthSettingsSection
    )
    @Units(Units.TICKS)
    default int hideWarmthAfterCombatDelay() { return 0; }

    @ConfigItem(
			position = 101,
			keyName = "enableRestorationBars",
			name = "Show Restores",
			description = "Visually shows how much will be restored to your vital bars."
	)
	default boolean enableRestorationBars() { return true; }

	@ConfigItem(
			position = 102,
			keyName = "hideWhenLargeInterfacePanelsOpen",
			name = "Hide when large UI elements are open",
			description = "Whether or not to hide the vital bars when large UI elements are open (ie Bank, Clue Scroll text)"
	)
	default boolean hideWhenLargeInterfacePanelsOpen() { return true; }

    @ConfigSection(
            position = 103,
            name = "DEBUG",
            description = "various debug config variables to store coordinate data. please don't touch!"
    )
    String debugSection = "debugSection";

    @ConfigItem(
            position = 104,
            keyName = "debugSidebarPanelX",
            name = "debugSidebarPanelX",
            description = "debugSidebarPanelX",
            section = debugSection
    )
    default int debugSidebarPanelX() { return 0; }

    @ConfigItem(
            position = 105,
            keyName = "debugSidebarPanelY",
            name = "debugSidebarPanelY",
            description = "debugSidebarPanelY",
            section = debugSection
    )
    default int debugSidebarPanelY() { return 0; }

    @ConfigItem(
            position = 106,
            keyName = "debugEnergyDeltaX",
            name = "debugEnergyDeltaX",
            description = "debugEnergyDeltaX",
            section = debugSection
    )
    default int debugEnergyDeltaX() { return 0; }

    @ConfigItem(
            position = 107,
            keyName = "debugEnergyDeltaY",
            name = "debugEnergyDeltaY",
            description = "debugEnergyDeltaY",
            section = debugSection
    )
    default int debugEnergyDeltaY() { return 0; }

    @ConfigItem(
            position = 108,
            keyName = "debugHitpointsDeltaX",
            name = "debugHitpointsDeltaX",
            description = "debugHitpointsDeltaX",
            section = debugSection
    )
    default int debugHitpointsDeltaX() { return 0; }

    @ConfigItem(
            position = 109,
            keyName = "debugHitpointsDeltaY",
            name = "debugHitpointsDeltaY",
            description = "debugHitpointsDeltaY",
            section = debugSection
    )
    default int debugHitpointsDeltaY() { return 0; }

    @ConfigItem(
            position = 110,
            keyName = "debugPrayerDeltaX",
            name = "debugPrayerDeltaX",
            description = "debugPrayerDeltaX",
            section = debugSection
    )
    default int debugPrayerDeltaX() { return 0; }

    @ConfigItem(
            position = 111,
            keyName = "debugPrayerDeltaY",
            name = "debugPrayerDeltaY",
            description = "debugPrayerDeltaY",
            section = debugSection
    )
    default int debugPrayerDeltaY() { return 0; }

    @ConfigItem(
            position = 112,
            keyName = "debugSpecialDeltaX",
            name = "debugSpecialDeltaX",
            description = "debugSpecialDeltaX",
            section = debugSection
    )
    default int debugSpecialDeltaX() { return 0; }

    @ConfigItem(
            position = 113,
            keyName = "debugSpecialDeltaY",
            name = "debugSpecialDeltaY",
            description = "debugSpecialDeltaY",
            section = debugSection
    )
    default int debugSpecialDeltaY() { return 0; }

    @ConfigItem(
            position = 114,
            keyName = "debugWarmthDeltaX",
            name = "debugWarmthDeltaX",
            description = "debugWarmthDeltaX",
            section = debugSection
    )
    default int debugWarmthDeltaX() { return 0; }

    @ConfigItem(
            position = 115,
            keyName = "debugWarmthDeltaY",
            name = "debugWarmthDeltaY",
            description = "debugWarmthDeltaY",
            section = debugSection
    )
    default int debugWarmthDeltaY() { return 0; }
}
