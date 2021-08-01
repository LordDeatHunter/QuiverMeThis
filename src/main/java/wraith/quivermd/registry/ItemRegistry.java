package wraith.quivermd.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import wraith.quivermd.QuiverMDItemGroup;
import wraith.quivermd.Utils;
import wraith.quivermd.item.QuiverItem;

import java.util.HashMap;

public class ItemRegistry {

    private ItemRegistry() {}

    private static final HashMap<String, Item> ITEMS = new HashMap<>();

    public static void register(String id, Item item) {
        ITEMS.put(id, item);
        Registry.register(Registry.ITEM, Utils.ID(id), item);
    }

    public static Item get(String id) {
        return ITEMS.get(id);
    }

    public static void init() {
        if (!ITEMS.isEmpty()) {
            return;
        }
        register("leather_quiver", new QuiverItem(200, new FabricItemSettings().group(QuiverMDItemGroup.ITEM_GROUP)));
        register("iron_quiver", new QuiverItem(400, new FabricItemSettings().group(QuiverMDItemGroup.ITEM_GROUP)));
        register("golden_quiver", new QuiverItem(600, new FabricItemSettings().group(QuiverMDItemGroup.ITEM_GROUP)));
        register("diamond_quiver", new QuiverItem(800, new FabricItemSettings().group(QuiverMDItemGroup.ITEM_GROUP)));
        register("netherite_quiver", new QuiverItem(1000, new FabricItemSettings().group(QuiverMDItemGroup.ITEM_GROUP)));
        register("dragon_quiver", new QuiverItem(1200, new FabricItemSettings().group(QuiverMDItemGroup.ITEM_GROUP)));
    }

}
