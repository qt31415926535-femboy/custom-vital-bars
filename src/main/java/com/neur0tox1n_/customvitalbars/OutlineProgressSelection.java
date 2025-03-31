package net.runelite.client.plugins.customvitalbars;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutlineProgressSelection
{
    SHOW_NATURAL_AND_CONSUMABLE_PROGRESS("Show all progress"),
    SHOW_NATURAL_PROGRESS_ONLY("Show progress related to natural regen/degen only"),
    SHOW_CONSUMABLE_PROGRESS_ONLY("Show progress related to consumables only"),
    HIDE("Always disabled");

    private final String name;

    @Override
    public String toString()
    {
        return getName();
    }
}
