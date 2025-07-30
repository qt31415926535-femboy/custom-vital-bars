package com.neur0tox1n_.customvitalbars;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Vital
{
    HITPOINTS(0),
    PRAYER(1),
    RUN_ENERGY(2),
    SPECIAL_ENERGY(3),
    WARMTH(4);

    private final int value;

    @Override
    public String toString()
    {
        return value + "";
    }
}
