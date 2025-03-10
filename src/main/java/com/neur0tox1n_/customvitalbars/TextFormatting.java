package net.runelite.client.plugins.customvitalbars;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TextFormatting
{
    SHOW_CURRENT_AND_MAXIMUM("CURRENT / MAXIMUM"),
    SHOW_CURRENT("CURRENT"),
    SHOW_PERCENTAGE("CURRENT %"),
    HIDE("Hide label");

    private final String name;

    @Override
    public String toString()
    {
        return getName();
    }
}
