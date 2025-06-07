package top.mcmtr;

import net.fabricmc.api.ClientModInitializer;
import top.mcmtr.loader.fabric.MSDRegistryImpl;
import top.mcmtr.mod.NeoMSDClient;

public class MSDMainFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NeoMSDClient.init();
        NeoMSDClient.registerItemModelPredicates();
        MSDRegistryImpl.PACKET_REGISTRY.commitClient();
    }
}