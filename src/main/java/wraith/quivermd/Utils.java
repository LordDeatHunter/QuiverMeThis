package wraith.quivermd;

import net.minecraft.util.Identifier;

public class Utils {

    public static Identifier ID(String path) {
        return new Identifier(QuiverMD.MOD_ID, path);
    }

}
