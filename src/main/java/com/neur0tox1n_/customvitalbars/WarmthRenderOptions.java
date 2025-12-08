package com.neur0tox1n_.customvitalbars;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WarmthRenderOptions
{
    HIDE("Hide"),
    SHOW_ALWAYS("Always Show"),
    SHOW_DYNAMICALLY("Dynamically Show");

    private final String name;

    @Override
    public String toString()
    {
        return getName();
    }
}
