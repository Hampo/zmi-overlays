package org.zhbot.zmi_overlays.enums;

import lombok.Getter;
import net.runelite.api.gameval.ItemID;

// Pulled from https://oldschool.runescape.wiki/w/Food#Popular_foods
@Getter
public enum Food {
    NONE("None", -1, 0),
    SHRIMPS("Shrimps", ItemID.SHRIMP, 3),
    COOKED_CHICKEN("Cooked Chicken", ItemID.COOKED_CHICKEN, 3),
    COOKED_MEAT("Cooked Meat", ItemID.COOKED_MEAT, 3),
    SARDINE("Sardine", ItemID.SARDINE, 4),
    BREAD("Bread", ItemID.BREAD, 5),
    HERRING("Herring", ItemID.HERRING, 5),
    MACKEREL("Mackerel", ItemID.MACKEREL, 6),
    TROUT("Trout", ItemID.TROUT, 7),
    PIKE("Pike", ItemID.PIKE, 8),
    PEACH("Peach", ItemID.PEACH, 8),
    RED_CRAB_MEAT("Red Crab Meat", ItemID.RED_CRAB_MEAT, 8),
    SALMON("Salmon", ItemID.SALMON, 9),
    COOKED_TBONE_STEAK("Cooked T-Bone Steak", ItemID.TBONE_STEAK, 9),
    TUNA("Tuna", ItemID.TUNA, 10),
    JUG_OF_WINE("Jug of Wine", ItemID.JUG_WINE, 11),
    LOBSTER("Lobster", ItemID.LOBSTER, 12),
    BASS("Bass", ItemID.BASS, 13),
    SWORDFISH("Swordfish", ItemID.SWORDFISH, 14),
    BLUE_CRAB_MEAT("Blue Crab Meat", ItemID.BLUE_CRAB_MEAT, 14),
    SNOWY_KNIGHT("Snowy Knight", ItemID.BLUE_CRAB_MEAT, 15),
    POTATO_WITH_CHEESE("Potato with Cheese", ItemID.POTATO_CHEESE, 16),
    MONKFISH("Monkfish", ItemID.MONKFISH, 16),
    IXCOZTIC_WHITE("Ixcoztic White", ItemID.IXCOZTIC_WHITE, 16),
    GIANT_KRILL("Giant Krill", ItemID.GIANT_KRILL, 17),
    COOKED_KARAMBWAN("Cooked Karambwan", ItemID.TBWT_COOKED_KARAMBWAN, 18),
    HADDOCK("Haddock", ItemID.HADDOCK, 18),
    CURRY("Curry", ItemID.CURRY, 19),
    RAINBOW_CRAB_MEAT("Rainbow Crab Meat", ItemID.RAINBOW_CRAB_MEAT, 19),
    YELLOWFIN("Yellowfin", ItemID.YELLOWFIN, 19),
    SHARK("Shark", ItemID.SHARK, 20);
    // 20 heals are probably enough, I'll add more if requested

    private final String name;
    private final int id;
    private final int heals;

    Food(String name, int id, int heals)
    {
        this.name = name;
        this.id = id;
        this.heals = heals;
    }

    @Override
    public String toString() {
        return name;
    }
}
