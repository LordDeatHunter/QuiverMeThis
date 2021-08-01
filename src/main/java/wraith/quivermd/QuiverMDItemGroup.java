package wraith.quivermd;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import wraith.quivermd.registry.ItemRegistry;

public class QuiverMDItemGroup {

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(Utils.ID("quivermd")).icon(() -> new ItemStack(ItemRegistry.get("leather_quiver"))).build();

}
