package com.neur0tox1n_.customvitalbars;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutlineProgressThreshold
{
    RELATED_STAT_AT_MAX("Related stat = max"),
    NO_THRESHOLD("Always progresses");

    private final String name;

    @Override
    public String toString()
    {
        return getName();
    }
}
