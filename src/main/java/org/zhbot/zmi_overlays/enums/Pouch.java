package org.zhbot.zmi_overlays.enums;

import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarbitID;

import java.util.HashMap;
import java.util.Map;

public enum Pouch {
    SMALL("Small pouch", ItemID.RCU_POUCH_SMALL, -1, VarbitID.SMALL_ESSENCE_POUCH_TYPE, VarbitID.SMALL_ESSENCE_POUCH),
    MEDIUM("Medium pouch", ItemID.RCU_POUCH_MEDIUM, ItemID.RCU_POUCH_MEDIUM_DEGRADE, VarbitID.MEDIUM_ESSENCE_POUCH_TYPE, VarbitID.MEDIUM_ESSENCE_POUCH),
    LARGE("Large pouch", ItemID.RCU_POUCH_LARGE, ItemID.RCU_POUCH_LARGE_DEGRADE, VarbitID.LARGE_ESSENCE_POUCH_TYPE, VarbitID.LARGE_ESSENCE_POUCH),
    GIANT("Giant pouch", ItemID.RCU_POUCH_GIANT, ItemID.RCU_POUCH_GIANT_DEGRADE, VarbitID.GIANT_ESSENCE_POUCH_TYPE, VarbitID.GIANT_ESSENCE_POUCH),
    COLOSSAL("Colossal pouch", ItemID.RCU_POUCH_COLOSSAL, ItemID.RCU_POUCH_COLOSSAL_DEGRADE, VarbitID.COLOSSAL_ESSENCE_POUCH_TYPE, VarbitID.COLOSSAL_ESSENCE_POUCH);

    public final String name;
    public final int itemId;
    public final int degradedItemId;
    public final int typeVarbitId;
    public final int amountVarbitId;

    Pouch(String name, int itemId, int degradedItemId, int typeVarbitId, int amountVarbitId)
    {
        this.name = name;
        this.itemId = itemId;
        this.degradedItemId = degradedItemId;
        this.typeVarbitId = typeVarbitId;
        this.amountVarbitId = amountVarbitId;
    }

    private static final Map<String, Pouch> NAME_MAP = new HashMap<>();
    private static final Map<Integer, Pouch> ITEM_MAP = new HashMap<>();
    private static final Map<Integer, Pouch> VARBIT_MAP = new HashMap<>();

    static
    {
        for (var pouch : values())
        {
            NAME_MAP.put(pouch.name, pouch);

            ITEM_MAP.put(pouch.itemId, pouch);
            if (pouch.degradedItemId != -1)
                ITEM_MAP.put(pouch.degradedItemId, pouch);

            VARBIT_MAP.put(pouch.typeVarbitId, pouch);
            VARBIT_MAP.put(pouch.amountVarbitId, pouch);
        }
    }

    public static Pouch getByName(String name)
    {
        return NAME_MAP.get(name);
    }

    public static Pouch getByItemId(int itemId)
    {
        return ITEM_MAP.get(itemId);
    }

    public static Pouch getByVarbit(int varbitId)
    {
        return VARBIT_MAP.get(varbitId);
    }
}
