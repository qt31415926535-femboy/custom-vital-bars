package net.runelite.client.plugins.customvitalbars;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutlineProgressThreshold
{
    RELATED_STAT_AT_MAX("Related stat = max"),
    ALWAYS_ON("Always progresses"),
    ALWAYS_OFF("Always disabled");

    private final String name;

    @Override
    public String toString()
    {
        return getName();
    }
}
