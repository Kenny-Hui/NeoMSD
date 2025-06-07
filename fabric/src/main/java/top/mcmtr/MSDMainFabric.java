package top.mcmtr;

import net.fabricmc.api.ModInitializer;
import top.mcmtr.loader.fabric.MSDRegistryImpl;
import top.mcmtr.mod.NeoMSD;

public class MSDMainFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        NeoMSD.init();
        MSDRegistryImpl.PACKET_REGISTRY.commitCommon();
    }
}