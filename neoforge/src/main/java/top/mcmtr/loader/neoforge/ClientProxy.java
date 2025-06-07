package top.mcmtr.loader.neoforge;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import top.mcmtr.mod.screen.ConfigScreen;

public class ClientProxy {
    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> ((modContainer, arg) -> new ConfigScreen()));
    }
}
