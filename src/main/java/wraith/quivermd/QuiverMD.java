package wraith.quivermd;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wraith.quivermd.registry.ItemRegistry;

public class QuiverMD implements ModInitializer {

    public static final String MOD_ID = "quivermd";
    public static final String MOD_NAME = "QuiverMeThis";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        ItemRegistry.init();
        LOGGER.info("[" + MOD_NAME + "] has been initiated.");
    }

}
