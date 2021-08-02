package wraith.quivermd.compatibility.Curios;

import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosComponent;
import top.theillusivec4.curios.api.SlotTypeInfo;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.component.ICurio;
import wraith.quivermd.registry.ItemRegistry;

import java.util.Optional;

public final class CuriosCompat {

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    public static void init() {
        CuriosApi.enqueueSlotType(SlotTypeInfo.BuildScheme.REGISTER, SlotTypePreset.BACK.getInfoBuilder().build());
        for (Item quiver : ItemRegistry.getItems()) {
            ItemComponentCallbackV2.event(quiver).register((item, stack, componentContainer) -> componentContainer.put(CuriosComponent.ITEM, new QuiverCurio()));
        }
    }

    public static ItemStack checkForQuiver(LivingEntity entity) {
        for (Item quiver : ItemRegistry.getItems()) {
            Optional<ImmutableTriple<String, Integer, ItemStack>> equippedQuiver = CuriosApi.getCuriosHelper().findEquippedCurio(quiver, entity);
            if (equippedQuiver.isPresent()) {
                return equippedQuiver.get().getRight();
            }
        }
        return null;
    }

    static class QuiverCurio implements ICurio {
        @Override
        public boolean canRightClickEquip() {
            return true;
        }
    }

}
