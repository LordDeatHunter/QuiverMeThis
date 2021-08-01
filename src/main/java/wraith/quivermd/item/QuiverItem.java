package wraith.quivermd.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuiverItem extends Item {

    private final int capacity;

    public QuiverItem(int capacity, Settings settings) {
        super(settings);
        this.capacity = capacity;
    }

    public static int getStoredAmount(ItemStack item) {
        if (!(item.getItem() instanceof QuiverItem)) {
            return 0;
        }
        NbtCompound tag = item.getTag();
        if (tag == null || !tag.contains("stored_arrows")) {
            return 0;
        }
        return Math.min(((QuiverItem) item.getItem()).getCapacity(), item.getTag().getInt("stored_arrows"));
    }

    public static void decrementStoredArrows(ItemStack quiverStack, int amount) {
        if (!(quiverStack.getItem() instanceof QuiverItem)) {
            return;
        }
        int storedAmount = QuiverItem.getStoredAmount(quiverStack);
        NbtCompound tag = quiverStack.getOrCreateTag();
        tag.putInt("stored_arrows", Math.max(0, storedAmount - amount));
    }

    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (!(stack.getItem() instanceof QuiverItem)) {
            return;
        }
        tooltip.add(new TranslatableText("tooltip.quivermd.stored_arrows").append(getStoredAmount(stack) + " / " + ((QuiverItem) stack.getItem()).getCapacity()));
    }

}
