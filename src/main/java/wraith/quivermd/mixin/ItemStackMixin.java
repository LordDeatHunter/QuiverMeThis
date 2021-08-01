package wraith.quivermd.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wraith.quivermd.item.QuiverItem;
import wraith.quivermd.mixin_interfaces.IItemStack;

@Mixin(ItemStack.class)
public class ItemStackMixin implements IItemStack {

    private ItemStack quiverStack = null;

    @Override
    public void setQuiverStack(ItemStack stack) {
        quiverStack = stack;
    }

    @Override
    public ItemStack getQuiverStack() {
        return quiverStack;
    }

    @Inject(method = "decrement", at = @At("HEAD"), cancellable = true)
    public void decrement(int amount, CallbackInfo ci) {
        if (quiverStack == null || !(quiverStack.getItem() instanceof QuiverItem)) {
            return;
        }
        int oldAmount = QuiverItem.getStoredAmount(quiverStack);
        NbtCompound tag = quiverStack.getOrCreateTag();
        tag.putInt("stored_arrows", Math.max(0, oldAmount - amount));
        ci.cancel();
    }

}
