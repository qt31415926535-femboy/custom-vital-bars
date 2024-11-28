package net.runelite.client.plugins.customvitalbars;

import net.runelite.client.config.*;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import java.awt.*;

@ConfigGroup("Custom Vital Bars")
public interface CustomVitalBarsConfig extends Config
{
	String GROUP = "minibars";

	@ConfigItem(
			position = 1,
			keyName = "showHitpoints",
			name = "Show Hitpoints",
			description = "Render Hitpoints overlay"
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
			keyName = "hitpointsLabelStyle",
			name = "Hitpoints Label Style",
			description = "Choose the style of the Hitpoints bar's label",
			section = hitpointsSettingsSection
	)
	default LabelStyle hitpointsLabelStyle()
	{
		return LabelStyle.SHOW_CURRENT_AND_MAXIMUM;
	}

	@ConfigItem(
			position = 6,
			keyName = "hitpointsLabelPosition",
			name = "Hitpoints Label Position",
			description = "Choose the location of the Hitpoints bar's label",
			section = hitpointsSettingsSection
	)
	default LabelPlacement hitpointsLabelPosition()
	{
		return LabelPlacement.TOP;
	}

	@ConfigItem(
			position = 7,
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
			position = 8,
			keyName = "hitpointsGlowThresholdValue",
			name = "Hitpoints Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Hitpoints bar",
			section = hitpointsSettingsSection
	)
	default int hitpointsGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 9,
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
			position = 10,
			keyName = "hitpointsOutlineProgressThreshold",
			name = "Hitpoints Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Hitpoints bar",
			section = hitpointsSettingsSection
	)
	default OutlineProgressThreshold hitpointsOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }
	
	@ConfigItem(
			position = 11,
			keyName = "showPrayer",
			name = "Show Prayer",
			description = "Render Prayer overlay"
	)
	default boolean renderPrayer() { return false; }

	@ConfigSection(
			position = 12,
			name = "Prayer Bar Settings",
			description = "Prayer Bar Settings"
	)
	String prayerSettingsSection = "prayerSettings";

	@ConfigItem(
			position = 13,
			keyName = "prayerSize",
			name = "Prayer Bar Size",
			description = "Choose the size of the Prayer bar",
			section = prayerSettingsSection
	)
	default Dimension prayerSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 14,
			keyName = "prayerFullnessDirection",
			name = "Prayer Fullness Direction",
			description = "Choose the direction of fullness of the Prayer bar",
			section = prayerSettingsSection
	)
	default FullnessDirection prayerFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 15,
			keyName = "prayerLabelStyle",
			name = "Prayer Label Style",
			description = "Choose the style of the Prayer bar's label",
			section = prayerSettingsSection
	)
	default LabelStyle prayerLabelStyle()
	{
		return LabelStyle.SHOW_CURRENT_AND_MAXIMUM;
	}

	@ConfigItem(
			position = 16,
			keyName = "prayerLabelPosition",
			name = "Prayer Label Position",
			description = "Choose the location of the Prayer bar's label",
			section = prayerSettingsSection
	)
	default LabelPlacement prayerLabelPosition()
	{
		return LabelPlacement.TOP;
	}

	@ConfigItem(
			position = 17,
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
			position = 18,
			keyName = "prayerGlowThresholdValue",
			name = "Prayer Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Prayer bar",
			section = prayerSettingsSection
	)
	default int prayerGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 19,
			keyName = "prayerOutlineThickness",
			name = "Prayer Bar Outline Effects Thickness",
			description = "How thick to draw an outline around the Prayer bars to show Prayer point consumption progress or Prayer Regeneration Potion regeneration.",
			section = prayerSettingsSection
	)
	@Range(
			min = 0,
			max = 5
	)
	default int prayerOutlineThickness() { return 3; }

	@ConfigItem(
			position = 20,
			keyName = "prayerOutlineProgressThreshold",
			name = "Prayer Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Prayer bar",
			section = prayerSettingsSection
	)
	default OutlineProgressThreshold prayerOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 21,
			keyName = "showEnergy",
			name = "Show Energy",
			description = "Render Run Energy overlay"
	)
	default boolean renderEnergy() { return false; }

	@ConfigSection(
			position = 22,
			name = "Energy Bar Settings",
			description = "Energy Bar Settings"
	)
	String energySettingsSection = "energySettings";

	@ConfigItem(
			position = 23,
			keyName = "energySize",
			name = "Energy Bar Size",
			description = "Choose the size of the Energy bar",
			section = energySettingsSection
	)
	default Dimension energySize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 24,
			keyName = "energyFullnessDirection",
			name = "Energy Fullness Direction",
			description = "Choose the direction of fullness of the Energy bar",
			section = energySettingsSection
	)
	default FullnessDirection energyFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 25,
			keyName = "energyLabelStyle",
			name = "Energy Label Style",
			description = "Choose the style of the Energy bar's label",
			section = energySettingsSection
	)
	default LabelStyle energyLabelStyle()
	{
		return LabelStyle.SHOW_CURRENT_AND_MAXIMUM;
	}

	@ConfigItem(
			position = 26,
			keyName = "energyLabelPosition",
			name = "Energy Label Position",
			description = "Choose the location of the Energy bar's label",
			section = energySettingsSection
	)
	default LabelPlacement energyLabelPosition()
	{
		return LabelPlacement.TOP;
	}

	@ConfigItem(
			position = 27,
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
			position = 28,
			keyName = "energyGlowThresholdValue",
			name = "Energy Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Energy bar",
			section = energySettingsSection
	)
	default int energyGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 29,
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
			position = 30,
			keyName = "energyOutlineProgressThreshold",
			name = "Energy Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Energy bar",
			section = energySettingsSection
	)
	default OutlineProgressThreshold energyOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 31,
			keyName = "showSpecial",
			name = "Show Special",
			description = "Render Special Attack overlay"
	)
	default boolean renderSpecial() { return false; }

	@ConfigSection(
			position = 32,
			name = "Special Bar Settings",
			description = "Special Bar Settings"
	)
	String specialSettingsSection = "specialSettings";

	@ConfigItem(
			position = 33,
			keyName = "specialSize",
			name = "Special Bar Size",
			description = "Choose the size of the Special bar",
			section = specialSettingsSection
	)
	default Dimension specialSize() { return new Dimension(ComponentConstants.STANDARD_WIDTH, 15 ); }

	@ConfigItem(
			position = 34,
			keyName = "specialFullnessDirection",
			name = "Special Fullness Direction",
			description = "Choose the direction of fullness of the Special bar",
			section = specialSettingsSection
	)
	default FullnessDirection specialFullnessDirection() { return FullnessDirection.RIGHT; }

	@ConfigItem(
			position = 35,
			keyName = "specialLabelStyle",
			name = "Special Label Style",
			description = "Choose the style of the Special bar's label",
			section = specialSettingsSection
	)
	default LabelStyle specialLabelStyle()
	{
		return LabelStyle.SHOW_CURRENT_AND_MAXIMUM;
	}

	@ConfigItem(
			position = 36,
			keyName = "specialLabelPosition",
			name = "Special Label Position",
			description = "Choose the location of the Special bar's label",
			section = specialSettingsSection
	)
	default LabelPlacement specialLabelPosition()
	{
		return LabelPlacement.TOP;
	}

	@ConfigItem(
			position = 37,
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
			position = 38,
			keyName = "specialGlowThresholdValue",
			name = "Special Bar Glow Threshold Value",
			description = "Choose what value of the chosen mode to start glowing the Special bar",
			section = specialSettingsSection
	)
	default int specialGlowThresholdValue() { return 10; }

	@ConfigItem(
			position = 39,
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
			position = 40,
			keyName = "specialOutlineProgressThreshold",
			name = "Special Bar Outline Progress Threshold",
			description = "When to stop progress on the outline of the Special bar",
			section = specialSettingsSection
	)
	default OutlineProgressThreshold specialOutlineProgressThreshold() { return OutlineProgressThreshold.RELATED_STAT_AT_MAX; }

	@ConfigItem(
			position = 41,
			keyName = "enableRestorationBars",
			name = "Show Restores",
			description = "Visually shows how much will be restored to your bars."
	)
	default boolean enableRestorationBars() { return true; }

	@ConfigItem(
			position = 42,
			keyName = "hideAfterCombatDelay",
			name = "Hide after combat delay",
			description = "Amount of ticks before hiding status bars after no longer in combat. 0 = always show status bars."
	)
	@Units(Units.TICKS)
	default int hideAfterCombatDelay() { return 0; }

}
