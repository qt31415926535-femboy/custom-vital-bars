// huge shoutout to DapperMickie of DelayedHeals plugin

package com.neur0tox1n_.customvitalbars;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
public enum DelayedHeals
{
    COOKED_WILD_KEBBIT(ItemID.COOKED_WILD_KEBBIT, 7),
    COOKED_LARUPIA(ItemID.COOKED_LARUPIA, 7),
    COOKED_BARBTAILED_KEBBIT(ItemID.COOKED_BARBTAILED_KEBBIT, 7),
    COOKED_GRAAHK(ItemID.COOKED_GRAAHK, 7),
    COOKED_KYATT(ItemID.COOKED_KYATT, 7),
    COOKED_PYRE_FOX(ItemID.COOKED_PYRE_FOX, 7),
    COOKED_DASHING_KEBBIT(ItemID.COOKED_DASHING_KEBBIT, 7),
    COOKED_SUNLIGHT_ANTELOPE(ItemID.COOKED_SUNLIGHT_ANTELOPE, 7),
    COOKED_MOONLIGHT_ANTELOPE(ItemID.COOKED_MOONLIGHT_ANTELOPE, 7);

    @Getter
    private final int itemId;
    @Getter
    private final int tickDelay;

    private static final Map<Integer, DelayedHeals> ITEM_MAP = new HashMap<>();

    static
    {
        for (DelayedHeals heal : DelayedHeals.values())
        {
            ITEM_MAP.put(heal.itemId, heal);
        }
    }

    public static DelayedHeals getDelayedHealByItemId(int itemId)
    {
        return ITEM_MAP.get(itemId);
    }
}