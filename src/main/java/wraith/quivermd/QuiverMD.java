package wraith.quivermd;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wraith.quivermd.compatibility.Curios.CuriosCompat;
import wraith.quivermd.registry.ItemRegistry;

public class QuiverMD implements ModInitializer {

    public static final String MOD_ID = "quivermd";
    public static final String MOD_NAME = "QuiverMeThis";
    public static final Logger LOGGER = LogManager.getLogger();
    private static boolean isCuriosLoaded = false;

    @Override
    public void onInitialize() {
        ItemRegistry.init();
        if (FabricLoader.getInstance().isModLoaded("curios")) {
            isCuriosLoaded = true;
            CuriosCompat.init();
        }
        LOGGER.info("[" + MOD_NAME + "] has been initiated.");
    }

    public static boolean isCuriosLoaded() {
        return isCuriosLoaded;
    }

}
