package top.mcmtr;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import top.mcmtr.loader.neoforge.ClientProxy;
import top.mcmtr.loader.neoforge.ForgeUtilities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import top.mcmtr.loader.neoforge.MSDRegistryImpl;
import top.mcmtr.mod.NeoMSD;
import top.mcmtr.mod.NeoMSDClient;

@Mod(NeoMSD.MOD_ID)
public class NeoMSDForge {
	static {
		NeoMSD.init();
	}

	public NeoMSDForge(IEventBus eventBus) {
		MSDRegistryImpl.registerAllDeferred(eventBus);
		eventBus.register(NeoMSDEventBus.class);
		eventBus.register(ForgeUtilities.RegisterCreativeTabs.class);
		if (FMLEnvironment.dist.isClient()) {
			ClientProxy.registerConfigScreen();
			NeoForge.EVENT_BUS.register(ForgeUtilities.Events.class);
			eventBus.register(ForgeUtilities.ClientsideEvents.class);
		}
	}

	private static class NeoMSDEventBus {
		@SubscribeEvent
		public static void onClientSetupEvent(FMLClientSetupEvent event) {
			NeoMSDClient.init();
			event.enqueueWork(NeoMSDClient::registerItemModelPredicates);
		}

		@SubscribeEvent
		public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
			PayloadRegistrar registrar = event.registrar("1");
			MSDRegistryImpl.PACKET_REGISTRY.commit(registrar);
		}
	}
}
