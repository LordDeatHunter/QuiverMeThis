package wraith.quivermd.mixin;

import net.minecraft.entity.player.PlayerEntity;
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
import wraith.quivermd.QuiverMD;
import wraith.quivermd.compatibility.Curios.CuriosCompat;
import wraith.quivermd.item.QuiverItem;

import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow @Final private List<DefaultedList<ItemStack>> combinedInventory;

    @Shadow @Final public PlayerEntity player;

    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    public void insertStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() != Items.ARROW) {
            return;
        }
        boolean quiverFound = false;
        if (QuiverMD.isCuriosLoaded()) {
            ItemStack quiverStack = CuriosCompat.checkForQuiver(player);
            if (quiverStack != null && quiverStack.getItem() instanceof QuiverItem) {
                int storedAmount = QuiverItem.getStoredAmount(quiverStack);
                int arrowAmount = stack.getCount();
                int capacity = ((QuiverItem) quiverStack.getItem()).getCapacity();
                if (storedAmount < capacity) {
                    quiverFound = true;
                    NbtCompound tag = quiverStack.getOrCreateTag();
                    tag.putInt("stored_arrows", Math.min(capacity, storedAmount + arrowAmount));
                    stack.decrement(QuiverItem.getStoredAmount(quiverStack) - storedAmount);
                }
            }
        }
        if (!quiverFound) {
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
        }
        if (stack.isEmpty()) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

}
