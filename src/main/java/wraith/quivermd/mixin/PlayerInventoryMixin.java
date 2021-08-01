package wraith.quivermd.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.quivermd.item.QuiverItem;

import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow @Final private List<DefaultedList<ItemStack>> combinedInventory;

    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    public void insertStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() != Items.ARROW) {
            return;
        }
        for (DefaultedList<ItemStack> inventory : combinedInventory) {
            for (ItemStack currentStack : inventory) {
                if (!(currentStack.getItem() instanceof QuiverItem)) {
                    continue;
                }
                int storedAmount = QuiverItem.getStoredAmount(currentStack);
                int arrowAmount = stack.getCount();
                int capacity = ((QuiverItem) currentStack.getItem()).getCapacity();
                if (storedAmount >= capacity) {
                    continue;
                }
                NbtCompound tag = currentStack.getOrCreateTag();
                tag.putInt("stored_arrows", Math.min(capacity, storedAmount + arrowAmount));
                stack.decrement(QuiverItem.getStoredAmount(currentStack) - storedAmount);
            }
        }
        if (stack.isEmpty()) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "removeOne", at = @At("HEAD"), cancellable = true)
    public void removeOne(ItemStack stack, CallbackInfo ci) {
        if (stack.getItem() != Items.ARROW) {
            return;
        }
        for (DefaultedList<ItemStack> inventory : combinedInventory) {
            for (ItemStack currentStack : inventory){
                if (!(currentStack.getItem() instanceof QuiverItem)) {
                    continue;
                }
                QuiverItem.decrementStoredArrows(currentStack, 1);
                ci.cancel();
                break;
            }
        }
    }

}
