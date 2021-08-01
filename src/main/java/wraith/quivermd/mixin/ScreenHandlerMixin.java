package wraith.quivermd.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.quivermd.item.QuiverItem;

import java.util.List;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Shadow @Final public List<Slot> slots;

    @Shadow public abstract void sendContentUpdates();

    @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
    public void onSlotClick(int screenStackIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack cursorStack = player.inventory.getCursorStack();
        if (cursorStack.isEmpty()) {
            return;
        }
        if (screenStackIndex >= 0 && cursorStack.getItem() == Items.ARROW) {
            ItemStack selectedStack = slots.get(screenStackIndex).getStack();
            if (!(selectedStack.getItem() instanceof QuiverItem)) {
                return;
            }
            int amount = QuiverItem.getStoredAmount(selectedStack);
            int capacity = ((QuiverItem) selectedStack.getItem()).getCapacity();

            selectedStack.getOrCreateTag().putInt("stored_arrows", Math.min(capacity, amount + cursorStack.getCount()));
            cursorStack.decrement(QuiverItem.getStoredAmount(selectedStack) - amount);

            sendContentUpdates();

            cir.setReturnValue(cursorStack);
            cir.cancel();
        }
        else if (cursorStack.getItem() instanceof QuiverItem && button == 1) {
            int amount = QuiverItem.getStoredAmount(cursorStack);
            if (amount <= 0) {
                return;
            }
            ItemStack selectedStack = slots.get(screenStackIndex).getStack();
            int oldCount;
            int newCount;
            if (selectedStack.isEmpty()) {
                oldCount = 0;
                newCount = Screen.hasShiftDown() ? Math.min(Items.ARROW.getMaxCount(), amount) : 1;
            } else if (selectedStack.getItem() == Items.ARROW && selectedStack.getCount() < selectedStack.getMaxCount()) {
                oldCount = selectedStack.getCount();
                newCount = Screen.hasShiftDown() ? Math.min(Items.ARROW.getMaxCount(), oldCount + amount) : oldCount + 1;
            } else {
                sendContentUpdates();

                cir.setReturnValue(player.inventory.getCursorStack());
                cir.cancel();
                return;
            }
            ItemStack arrowStack = new ItemStack(Items.ARROW, newCount);
            slots.get(screenStackIndex).setStack(arrowStack);
            QuiverItem.decrementStoredArrows(cursorStack, newCount - oldCount);

            sendContentUpdates();

            cir.setReturnValue(cursorStack);
            cir.cancel();

        }
        /*
        else if (cursorStack.getItem() instanceof QuiverItem && (button == 1 || button == 4)) {
            int amount = QuiverItem.getStoredAmount(cursorStack);
            ItemStack quiverStack = new ItemStack(Items.ARROW, amount);
            ((IItemStack)(Object)quiverStack).setQuiverStack(cursorStack);
            player.inventory.setCursorStack(quiverStack);
        }
    }

    @Inject(method = "onSlotClick", at = @At("RETURN"), cancellable = true)
    public void afterSlotClick(int screenStackIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack cursorStack = player.inventory.getCursorStack();
        if (cursorStack.isEmpty() || cursorStack.getItem() != Items.ARROW) {
            return;
        }
        ItemStack quiverStack = ((IItemStack)(Object)cursorStack).getQuiverStack();
        if (quiverStack == null) {
            return;
        }
        player.inventory.setCursorStack(quiverStack);
        sendContentUpdates();
        cir.setReturnValue(quiverStack);
        cir.cancel();
         */
    }

}