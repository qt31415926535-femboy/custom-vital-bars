package net.runelite.client.plugins.customvitalbars;

import net.runelite.client.config.*;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import java.awt.*;

@ConfigGroup("Custom Vital Bars")
public interface CustomVitalBarsConfig extends Config
{
	String GROUP = "vitalbars";

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
			description = "How thick to draw an outline around the Hitpoints bar to show regeneration.",
			section = hitpointsSettingsSection
	)
	@Range(
			min = 0,
			max = 5
	)
	default int hitpointsOutlineThickness() { return 3; }

	@ConfigItem(
			position = 12,
			keyName = "hitpointsOutlineProgressThreshold",
			name = "Hitpoints Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Hitpoints bar",
			section = hitpointsSettingsSection
	)
	default OutlineProgressThreshold hitpointsOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }


	@ConfigItem(
			position = 13,
			keyName = "hitpointsIconScale",
			name = "Hitpoints Bar Icon Image Scale",
			description = "Choose the size scaling of the Hitpoints bar icon",
			section = hitpointsSettingsSection
	)
	default double hitpointsIconScale() { return 0d; }

	@ConfigItem(
			position = 14,
			keyName = "hitpointsIconPosition",
			name = "Hitpoints Bar Icon Position",
			description = "Choose the general location of the Hitpoints bar icon",
			section = hitpointsSettingsSection
	)
	default PlacementDirection hitpointsIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 15,
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
			position = 16,
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
			position = 17,
			keyName = "showPrayer",
			name = "Show Prayer",
			description = "Render Prayer bar"
	)
	default boolean renderPrayer() { return false; }

	@ConfigSection(
			position = 18,
			name = "Prayer Bar Settings",
			description = "Prayer Bar Settings"
	)
	String prayerSettingsSection = "prayerSettings";

	@ConfigItem(
			position = 19,
			keyName = "prayerSize",
			name = "Prayer Bar Size",
			description = "Choose the size of the Prayer bar",
			section = prayerSettingsSection
	)
	default Dimension prayerSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 20,
			keyName = "prayerFullnessDirection",
			name = "Prayer Fullness Direction",
			description = "Choose the direction of fullness of the Prayer bar",
			section = prayerSettingsSection
	)
	default FullnessDirection prayerFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 21,
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
			position = 22,
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
			position = 23,
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
			position = 24,
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
			position = 25,
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
			position = 26,
			keyName = "prayerGlowThresholdValue",
			name = "Prayer Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Prayer bar",
			section = prayerSettingsSection
	)
	default int prayerGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 27,
			keyName = "prayerOutlineThickness",
			name = "Prayer Bar Outline Effects Thickness",
			description = "How thick to draw an outline around the Prayer bar to show Prayer point consumption progress or Prayer Regeneration Potion regeneration.",
			section = prayerSettingsSection
	)
	@Range(
			min = 0,
			max = 5
	)
	default int prayerOutlineThickness() { return 3; }

	@ConfigItem(
			position = 28,
			keyName = "prayerOutlineProgressThreshold",
			name = "Prayer Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Prayer bar",
			section = prayerSettingsSection
	)
	default OutlineProgressThreshold prayerOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 29,
			keyName = "prayerIconScale",
			name = "Prayer Bar Icon Image Scale",
			description = "Choose the size scaling of the Prayer bar icon",
			section = prayerSettingsSection
	)
	default double prayerIconScale() { return 0d; }

	@ConfigItem(
			position = 30,
			keyName = "prayerIconPosition",
			name = "Prayer Bar Icon Position",
			description = "Choose the general location of the Prayer bar icon",
			section = prayerSettingsSection
	)
	default PlacementDirection prayerIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 31,
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
			position = 32,
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
			position = 33,
			keyName = "showEnergy",
			name = "Show Energy",
			description = "Render Run Energy bar"
	)
	default boolean renderEnergy() { return false; }

	@ConfigSection(
			position = 34,
			name = "Energy Bar Settings",
			description = "Energy Bar Settings"
	)
	String energySettingsSection = "energySettings";

	@ConfigItem(
			position = 35,
			keyName = "energySize",
			name = "Energy Bar Size",
			description = "Choose the size of the Energy bar",
			section = energySettingsSection
	)
	default Dimension energySize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 36,
			keyName = "energyFullnessDirection",
			name = "Energy Fullness Direction",
			description = "Choose the direction of fullness of the Energy bar",
			section = energySettingsSection
	)
	default FullnessDirection energyFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 37,
			keyName = "energyTexFormat",
			name = "Energy Text Formatting",
			description = "Choose the formatting of the Energy bar's text",
			section = energySettingsSection
	)
	default TextFormatting energyTextFormat()
	{
		return TextFormatting.SHOW_CURRENT;
	}

	@ConfigItem(
			position = 38,
			keyName = "energyTextPosition",
			name = "Energy Text Position",
			description = "Choose the general location of the Energy bar's text",
			section = energySettingsSection
	)
	default PlacementDirection energyTextPosition()
	{
		return PlacementDirection.TOP;
	}

	@ConfigItem(
			position = 39,
			keyName = "energyTextOffsetX",
			name = "Energy Bar Text Offset - X axis",
			description = "The X-offset for the text of the Energy bar",
			section = energySettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int energyTextOffsetX() { return 0; }

	@ConfigItem(
			position = 40,
			keyName = "energyTextOffsetY",
			name = "Energy Bar Text Offset - Y axis",
			description = "The Y-offset for the text of the Energy bar",
			section = energySettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int energyTextOffsetY() { return 0; }

	@ConfigItem(
			position = 41,
			keyName = "energyGlowThresholdMode",
			name = "Energy Bar Glow Threshold Mode",
			description = "Choose how to determine the critical threshold at which to glow the Energy bar",
			section = energySettingsSection
	)
	default ThresholdGlowMode energyGlowThresholdMode()
	{
		return ThresholdGlowMode.BELOW_PERCENTAGE;
	}

	@ConfigItem(
			position = 42,
			keyName = "energyGlowThresholdValue",
			name = "Energy Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Energy bar",
			section = energySettingsSection
	)
	default int energyGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 43,
			keyName = "energyOutlineThickness",
			name = "Energy Bar Outline Effects Thickness",
			description = "How thick to draw an outline around the Energy bar to show regeneration or Stamina Potion duration.",
			section = energySettingsSection
	)
	@Range(
			min = 0,
			max = 5
	)
	default int energyOutlineThickness() { return 3; }

	@ConfigItem(
			position = 44,
			keyName = "energyOutlineProgressThreshold",
			name = "Energy Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Energy bar",
			section = energySettingsSection
	)
	default OutlineProgressThreshold energyOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 45,
			keyName = "energyIconScale",
			name = "Energy Bar Icon Image Scale",
			description = "Choose the size scaling of the Energy bar icon",
			section = energySettingsSection
	)
	default double energyIconScale() { return 0d; }

	@ConfigItem(
			position = 46,
			keyName = "energyIconPosition",
			name = "Energy Bar Icon Position",
			description = "Choose the general location of the Energy bar icon",
			section = energySettingsSection
	)
	default PlacementDirection energyIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 47,
			keyName = "energyIconOffsetX",
			name = "Energy Bar Icon Offset - X axis",
			description = "The X-offset for the icon of the Energy bar",
			section = energySettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int energyIconOffsetX() { return 0; }

	@ConfigItem(
			position = 48,
			keyName = "energyIconOffsetY",
			name = "Energy Bar Icon Offset - Y axis",
			description = "The Y-offset for the icon of the Energy bar",
			section = energySettingsSection
	)
	@Range(
			min = -9999,
			max = 9999
	)
	default int energyIconOffsetY() { return 0; }

	@ConfigItem(
			position = 49,
			keyName = "showSpecial",
			name = "Show Special",
			description = "Render Special Attack bar"
	)
	default boolean renderSpecial() { return false; }

	@ConfigSection(
			position = 50,
			name = "Special Bar Settings",
			description = "Special Bar Settings"
	)
	String specialSettingsSection = "specialSettings";

	@ConfigItem(
			position = 51,
			keyName = "specialSize",
			name = "Special Bar Size",
			description = "Choose the size of the Special bar",
			section = specialSettingsSection
	)
	default Dimension specialSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 52,
			keyName = "specialFullnessDirection",
			name = "Special Fullness Direction",
			description = "Choose the direction of fullness of the Special bar",
			section = specialSettingsSection
	)
	default FullnessDirection specialFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 53,
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
			position = 54,
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
			position = 55,
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
			position = 56,
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
			position = 57,
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
			position = 58,
			keyName = "specialGlowThresholdValue",
			name = "Special Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Special bar",
			section = specialSettingsSection
	)
	default int specialGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 59,
			keyName = "specialOutlineThickness",
			name = "Special Bar Outline Effects Thickness",
			description = "How thick to draw an outline around the Special bar to show regeneration.",
			section = specialSettingsSection
	)
	@Range(
			min = 0,
			max = 5
	)
	default int specialOutlineThickness() { return 3; }

	@ConfigItem(
			position = 60,
			keyName = "specialOutlineProgressThreshold",
			name = "Special Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Special bar",
			section = specialSettingsSection
	)
	default OutlineProgressThreshold specialOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 61,
			keyName = "specialIconScale",
			name = "Special Bar Icon Image Scale",
			description = "Choose the size scaling of the Special bar icon",
			section = specialSettingsSection
	)
	default double specialIconScale() { return 0d; }

	@ConfigItem(
			position = 62,
			keyName = "specialIconPosition",
			name = "Special Bar Icon Position",
			description = "Choose the general location of the Special bar icon",
			section = specialSettingsSection
	)
	default PlacementDirection specialIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 63,
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
			position = 64,
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
			position = 65,
			keyName = "showWarmth",
			name = "Show Warmth",
			description = "Render Warmth overlay"
	)
	default boolean renderWarmth() { return false; }

	@ConfigSection(
			position = 66,
			name = "Warmth Bar Settings",
			description = "Warmth Bar Settings"
	)
	String warmthSettingsSection = "warmthSettings";

	@ConfigItem(
			position = 67,
			keyName = "warmthSize",
			name = "Warmth Bar Size",
			description = "Choose the size of the Warmth bar",
			section = warmthSettingsSection
	)
	default Dimension warmthSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 68,
			keyName = "warmthFullnessDirection",
			name = "Warmth Fullness Direction",
			description = "Choose the direction of fullness of the Warmth bar",
			section = warmthSettingsSection
	)
	default FullnessDirection warmthFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 69,
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
			position = 70,
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
			position = 71,
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
			position = 72,
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
			position = 73,
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
			position = 74,
			keyName = "warmthGlowThresholdValue",
			name = "Warmth Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Warmth bar",
			section = warmthSettingsSection
	)
	default int warmthGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 75,
			keyName = "warmthOutlineThickness",
			name = "Warmth Bar Outline Effects Thickness",
			description = "How thick to draw an outline around the Warmth bar to show regeneration (TO-DO).",
			section = warmthSettingsSection
	)
	@Range(
			min = 0,
			max = 5
	)
	default int warmthOutlineThickness() { return 3; }

	@ConfigItem(
			position = 76,
			keyName = "warmthOutlineProgressThreshold",
			name = "Warmth Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Warmth bar",
			section = warmthSettingsSection
	)
	default OutlineProgressThreshold warmthOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 77,
			keyName = "warmthIconScale",
			name = "Warmth Bar Icon Image Scale",
			description = "Choose the size scaling of the Warmth bar icon",
			section = warmthSettingsSection
	)
	default double warmthIconScale() { return 0d; }

	@ConfigItem(
			position = 78,
			keyName = "warmthIconPosition",
			name = "Warmth Bar Icon Position",
			description = "Choose the general location of the Warmth bar icon",
			section = warmthSettingsSection
	)
	default PlacementDirection warmthIconPosition() { return PlacementDirection.LEFT; }

	@ConfigItem(
			position = 79,
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
			position = 80,
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
			position = 81,
			keyName = "enableRestorationBars",
			name = "Show Restores",
			description = "Visually shows how much will be restored to your vital bars."
	)
	default boolean enableRestorationBars() { return true; }

	@ConfigItem(
			position = 82,
			keyName = "hideAfterCombatDelay",
			name = "Hide after combat delay",
			description = "Amount of ticks before hiding vital bars after no longer in combat. 0 = always show status bars."
	)
	@Units(Units.TICKS)
	default int hideAfterCombatDelay() { return 0; }

	@ConfigItem(
			position = 83,
			keyName = "hideWhenLargeInterfacePanelsOpen",
			name = "Hide when large UI elements are open",
			description = "Whether or not to hide the vital bars when large UI elements are open (ie Bank, Clue Scroll text)"
	)
	default boolean hideWhenLargeInterfacePanelsOpen() { return true; }

	@ConfigItem(
			position = 84,
			keyName = "hideWhenSidebarPanelClosed",
			name = "Hide when sidebar is closed",
			description = "Whether or not to hide the vital bars when sidebar is closed (ie the panel with the inventory/prayers/spells)"
	)
	default boolean hideWhenSidebarPanelClosed() { return false; }
}
