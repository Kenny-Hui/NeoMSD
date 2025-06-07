package top.mcmtr.loader.neoforge;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ForgeUtilities {

	private static Runnable renderTickAction = () -> {
	};
	private static Consumer<Object> renderGameOverlayAction = matrices -> {
	};
	private static Consumer<Object> textureStitchEvent = atlas -> {
	};
	private static final List<Consumer<RegisterCommandsEvent>> commandsEventListeners = new ArrayList<>();
	private static final List<ResourceLocation> CREATIVE_TAB_ORDER = new ArrayList<>();
	private static final Map<ResourceLocation, CreativeModeTabWrapper> CREATIVE_TABS = new HashMap<>();
	private static final Set<EntityRendererPair<?>> ENTITY_RENDERER_PAIRS = new HashSet<>();

	public static void registerKeyBinding(KeyMapping keyMapping) {
		KeyMappingRegistry.register(keyMapping);
	}

	public static Supplier<CreativeModeTab> registerCreativeModeTab(ResourceLocation resourceLocation, Supplier<ItemStack> iconSupplier, String translationKey) {
		if (!CREATIVE_TAB_ORDER.contains(resourceLocation)) {
			CREATIVE_TAB_ORDER.add(resourceLocation);
			CREATIVE_TABS.put(resourceLocation, new CreativeModeTabWrapper(iconSupplier, translationKey));
		}
		return CREATIVE_TABS.get(resourceLocation).creativeModeTabSupplier;
	}

	public static void hookItemToCreativeTab(ResourceLocation resourceLocation, Item item) {
		if (CREATIVE_TABS.containsKey(resourceLocation)) {
			CREATIVE_TABS.get(resourceLocation).items.add(item);
		}
	}

	public static ResourceKey<Registry<Item>> registryGetItem() {
		return Registries.ITEM;
	}

	public static ResourceKey<Registry<Block>> registryGetBlock() {
		return Registries.BLOCK;
	}

	public static ResourceKey<Registry<BlockEntityType<?>>> registryGetBlockEntityType() {
		return Registries.BLOCK_ENTITY_TYPE;
	}

	public static ResourceKey<Registry<EntityType<?>>> registryGetEntityType() {
		return Registries.ENTITY_TYPE;
	}

	public static ResourceKey<Registry<SoundEvent>> registryGetSoundEvent() {
		return Registries.SOUND_EVENT;
	}

	public static ResourceKey<Registry<ParticleType<?>>> registryGetParticleType() {
		return Registries.PARTICLE_TYPE;
	}

	public static void renderTickAction(Runnable runnable) {
		renderTickAction = runnable;
	}

	public static void renderGameOverlayAction(Consumer<Object> consumer) {
		renderGameOverlayAction = consumer;
	}

	public static void registerTextureStitchEvent(Consumer<Object> consumer) {
		textureStitchEvent = consumer;
	}

	public static <T extends Entity> void registerEntityRenderer(Supplier<EntityType<? extends T>> entityType, EntityRendererProvider<T> entityRendererProvider) {
		ENTITY_RENDERER_PAIRS.add(new EntityRendererPair<>(entityType, entityRendererProvider));
	}

	public static void registerCommandListener(Consumer<RegisterCommandsEvent> event) {
		commandsEventListeners.add(event);
	}

	public static class Events {

		@SubscribeEvent
		public static void onRenderTickEvent(RenderLevelStageEvent event) {
			if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) {
				renderTickAction.run();
			}
		}

		@SubscribeEvent
		public static void onRenderGameOverlayEvent(RenderGuiLayerEvent.Post event) {
			renderGameOverlayAction.accept(event.getGuiGraphics());
		}

		@SubscribeEvent
		public static void registerCommand(RegisterCommandsEvent registerCommandsEvent) {
			for(Consumer<RegisterCommandsEvent> listener : commandsEventListeners) {
				listener.accept(registerCommandsEvent);
			}
		}
	}

	public static class ClientsideEvents {

		@SubscribeEvent
		public static void onEntityRendererEvent(EntityRenderersEvent.RegisterRenderers event) {
			ENTITY_RENDERER_PAIRS.forEach(entityRendererPair -> entityRendererPair.register(event));
		}

		@SubscribeEvent
		public static void onTextureStitchEvent(TextureAtlasStitchedEvent event) {
			textureStitchEvent.accept(event.getAtlas());
		}
	}

	public static class RegisterCreativeTabs {

		@SubscribeEvent
		public static void onRegisterCreativeModeTabsEvent(BuildCreativeModeTabContentsEvent event) {
			CREATIVE_TABS.forEach((resourceLocation, creativeModeTabWrapper) -> {
				if (creativeModeTabWrapper.creativeModeTab.getDisplayName().equals(event.getTab().getDisplayName())) {
					creativeModeTabWrapper.items.forEach(item -> event.accept(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
				}
			});
		}
	}

	public static void registerCreativeModeTabsToDeferredRegistry(DeferredRegisterHolder<CreativeModeTab> registry) {
		for (int i = 0; i < CREATIVE_TAB_ORDER.size(); i++) {
			ResourceLocation resourceLocation = CREATIVE_TAB_ORDER.get(i);
			final CreativeModeTabWrapper creativeModeTabWrapper = CREATIVE_TABS.get(resourceLocation);
			CreativeModeTab.Builder builder = CreativeModeTab.builder()
					.icon(creativeModeTabWrapper.iconSupplier)
					.title(Component.translatable(creativeModeTabWrapper.translationKey));
			builder.withTabsBefore(CreativeModeTabs.SPAWN_EGGS);
			if (i > 0) {
				builder.withTabsBefore(CREATIVE_TAB_ORDER.get(i - 1));
			}
			if (i < CREATIVE_TAB_ORDER.size() - 1) {
				builder.withTabsAfter(CREATIVE_TAB_ORDER.get(i + 1));
			}
			creativeModeTabWrapper.creativeModeTab = builder.build();
			registry.register(resourceLocation.getPath(), () -> creativeModeTabWrapper.creativeModeTab);
		}
	}

	private static class EntityRendererPair<T extends Entity> {

		private final Supplier<EntityType<? extends T>> entityTypeSupplier;
		private final EntityRendererProvider<T> entityRendererProvider;

		private EntityRendererPair(Supplier<EntityType<? extends T>> entityTypeSupplier, EntityRendererProvider<T> entityRendererProvider) {
			this.entityTypeSupplier = entityTypeSupplier;
			this.entityRendererProvider = entityRendererProvider;
		}

		private void register(EntityRenderersEvent.RegisterRenderers event) {
			event.registerEntityRenderer(entityTypeSupplier.get(), entityRendererProvider);
		}
	}

	private static class CreativeModeTabWrapper {

		private CreativeModeTab creativeModeTab;
		private final Supplier<ItemStack> iconSupplier;
		private final Supplier<CreativeModeTab> creativeModeTabSupplier;
		private final String translationKey;
		private final List<Item> items = new ArrayList<>();

		private CreativeModeTabWrapper(Supplier<ItemStack> iconSupplier, String translationKey) {
			this.iconSupplier = iconSupplier;
			creativeModeTabSupplier = () -> creativeModeTab;
			this.translationKey = translationKey;
		}
	}
}
