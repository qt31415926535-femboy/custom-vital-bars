package com.neur0tox1n_.customvitalbars;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public enum SpecialEnergyRestoration
{
    SURGE_POTION_1(ItemID.SURGE_POTION1, 300000),
    SURGE_POTION_2(ItemID.SURGE_POTION2, 300000),
    SURGE_POTION_3(ItemID.SURGE_POTION3, 300000),
    SURGE_POTION_4(ItemID.SURGE_POTION4, 300000);

    @Getter
    private final int itemId;
    @Getter
    private final int cooldown;

    private static final Map<Integer, SpecialEnergyRestoration> ITEM_MAP = new HashMap<>();

    static
    {
        for (SpecialEnergyRestoration heal : SpecialEnergyRestoration.values())
        {
            ITEM_MAP.put(heal.itemId, heal);
        }
    }

    public static SpecialEnergyRestoration getSpecialRestorationByItemId(int itemId)
    {
        return ITEM_MAP.get(itemId);
    }

}
