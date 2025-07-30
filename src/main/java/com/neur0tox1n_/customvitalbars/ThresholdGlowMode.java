package com.neur0tox1n_.customvitalbars;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ThresholdGlowMode
{
    ABOVE_PERCENTAGE("Percentage, above threshold"),
    ABOVE_FLAT_VALUE("Flat value, above threshold"),
    BELOW_PERCENTAGE("Percentage, below threshold"),
    BELOW_FLAT_VALUE("Flat value, below threshold"),
    NONE("Disable glow");

    private final String name;

    @Override
    public String toString()
    {
        return getName();
    }
}
