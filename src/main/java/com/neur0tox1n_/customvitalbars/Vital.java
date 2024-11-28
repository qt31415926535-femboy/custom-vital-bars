package net.runelite.client.plugins.customvitalbars;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Vital
{
    HITPOINTS(0),
    PRAYER(1),
    RUN_ENERGY(2),
    SPECIAL_ENERGY(3);

    private final int value;

    @Override
    public String toString()
    {
        return value + "";
    }
}
