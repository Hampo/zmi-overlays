package org.zhbot.zmi_overlays.utils;

import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InventoryID;
import net.runelite.client.eventbus.Subscribe;
import org.zhbot.zmi_overlays.enums.Pouch;

import javax.inject.Singleton;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Singleton
public class PouchUtils {
    private static final int POUCH_TYPE_PURE = 2;
    private static final int POUCH_TYPE_DAEYALT = 3;

    private final Map<Pouch, Integer> pouchContents = new EnumMap<>(Pouch.class);

    private final Set<Pouch> pouchesInInventory = EnumSet.noneOf(Pouch.class);
    private final Set<Pouch> pouchesWithEssence = EnumSet.noneOf(Pouch.class);

    public void cleanup()
    {
        pouchContents.clear();
        pouchesInInventory.clear();
        pouchesWithEssence.clear();
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event)
    {
        if (event.getContainerId() != InventoryID.INV)
            return;

        var inventory = event.getItemContainer();

        pouchesInInventory.clear();
        for (var pouch : Pouch.values())
            if (inventory.contains(pouch.itemId) || (pouch.degradedItemId != -1 && inventory.contains(pouch.degradedItemId)))
                pouchesInInventory.add(pouch);
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event)
    {
        var id = event.getVarbitId();

        var pouch = Pouch.getByVarbit(id);
        if (pouch == null)
            return;

        var value = event.getValue();

        if (id == pouch.typeVarbitId)
        {
            if (value == POUCH_TYPE_PURE || value == POUCH_TYPE_DAEYALT)
                pouchesWithEssence.add(pouch);
            else
                pouchesWithEssence.remove(pouch);
        }
        else if (id == pouch.amountVarbitId)
        {
            pouchContents.put(pouch, value);
        }
    }

    public boolean hasPouchWithEssence()
    {
        for (var pouch : pouchesInInventory)
            if (pouchesWithEssence.contains(pouch))
                return true;

        return false;
    }

    public int getPouchContents(Pouch pouch)
    {
        return pouchContents.getOrDefault(pouch, 0);
    }
}
