package net.runelite.client.plugins.customvitalbars;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ThresholdGlowMode
{
    PERCENTAGE("Percentage"),
    FLAT_VALUE("Flat value"),
    NONE("Disable glow");

    private final String name;

    @Override
    public String toString()
    {
        return getName();
    }
}
