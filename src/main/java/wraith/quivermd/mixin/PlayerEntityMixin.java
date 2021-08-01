package wraith.quivermd.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.quivermd.item.QuiverItem;
import wraith.quivermd.mixin_interfaces.IItemStack;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Shadow @Final public PlayerInventory inventory;

    @Inject(method = "getArrowType", at = @At("HEAD"), cancellable = true)
    public void getArrowType(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (!(stack.getItem() instanceof RangedWeaponItem)) {
            return;
        }
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack currentStack = inventory.getStack(i);
            if (!(currentStack.getItem() instanceof QuiverItem)) {
                continue;
            }
            int amount = QuiverItem.getStoredAmount(currentStack);
            if (amount <= 0) {
                continue;
            }
            ItemStack arrowStack = new ItemStack(Items.ARROW, amount);
            ((IItemStack)(Object)arrowStack).setQuiverStack(currentStack);
            cir.setReturnValue(arrowStack);
            cir.cancel();
            break;
        }
    }

}