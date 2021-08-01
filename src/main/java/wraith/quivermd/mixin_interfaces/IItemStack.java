package wraith.quivermd.mixin_interfaces;

import net.minecraft.item.ItemStack;

public interface IItemStack {

    void setQuiverStack(ItemStack stack);
    ItemStack getQuiverStack();

}
