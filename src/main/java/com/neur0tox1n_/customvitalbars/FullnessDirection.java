package com.neur0tox1n_.customvitalbars;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FullnessDirection
{
    LEFT("Left"),
    RIGHT("Right"),
    TOP("Top"),
    BOTTOM("Bottom"),
    MIDDLE_VERTICAL( "Middle-Vertical" ),
    MIDDLE_HORIZONTAL( "Middle-Horizontal" );

    private final String name;

    @Override
    public String toString()
    {
        return getName();
    }
}
