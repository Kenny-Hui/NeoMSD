package top.mcmtr.loader.neoforge;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import mtr.MTRClient;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.EntityRendererMapper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;
import java.util.function.Function;

public class MSDRegistryClientImpl {

	public static void registerBlockRenderType(RenderType type, Block block) {
		RegistryUtilitiesClient.registerRenderType(type, block);
	}

	public static void registerItemModelPredicate(String id, Item item, String tag) {
		RegistryUtilitiesClient.registerItemModelPredicate(id, item, tag);
	}

	public static <T extends BlockEntityMapper> void registerTileEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> function) {
		RegistryUtilitiesClient.registerBlockEntityRenderer(type, function);
	}

	public static <T extends Entity> void registerEntityRenderer(EntityType<T> type, Function<Object, EntityRendererMapper<T>> function) {
		RegistryUtilitiesClient.registerEntityRenderer(type, function::apply);
	}

	public static void registerKeyBinding(KeyMapping keyMapping) {
		ForgeUtilities.registerKeyBinding(keyMapping);
	}

	public static void registerBlockColors(Block block) {
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), block);
	}

	public static void registerNetworkReceiver(ResourceLocation resourceLocation, Consumer<FriendlyByteBuf> consumer) {
		MSDRegistryImpl.PACKET_REGISTRY.registerNetworkReceiverS2C(resourceLocation, consumer);
	}

	public static void registerPlayerJoinEvent(Consumer<LocalPlayer> consumer) {
		RegistryUtilitiesClient.registerPlayerJoinEvent(consumer);
	}

	public static void registerTickEvent(Consumer<Minecraft> consumer) {
		RegistryUtilitiesClient.registerClientTickEvent(consumer);
	}

	public static void sendToServer(ResourceLocation id, FriendlyByteBuf packet) {
		packet.resetReaderIndex();
		MSDRegistryImpl.PACKET_REGISTRY.sendC2S(id, packet);
	}

	private static class StationColor implements BlockColor {

		@Override
		public int getColor(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos pos, int i) {
			return MTRClient.getStationColor(pos);
		}
	}


	public interface RegistryUtilitiesClient {

		static void registerItemModelPredicate(String id, Item item, String tag) {
			ItemPropertiesRegistry.register(item, ResourceLocation.parse(id), (itemStack, clientWorld, livingEntity, i) ->
					itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains(tag) ? 1 : 0);
		}

		static <T extends BlockEntityMapper> void registerBlockEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> factory) {
			BlockEntityRendererRegistry.register(type, context -> factory.apply(null));
		}

		static <T extends Entity> void registerEntityRenderer(EntityType<T> type, Function<EntityRendererProvider.Context, EntityRendererMapper<T>> factory) {
		}

		static void registerRenderType(RenderType renderType, Block block) {
			RenderTypeRegistry.register(renderType, block);
		}

		static void registerBlockColors(BlockColor blockColor, Block block) {
			ColorHandlerRegistry.registerBlockColors(blockColor, block);
		}

		static void registerPlayerJoinEvent(Consumer<LocalPlayer> consumer) {
			ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(consumer::accept);
		}

		static void registerClientStoppingEvent(Consumer<Minecraft> consumer) {
			ClientLifecycleEvent.CLIENT_STOPPING.register(consumer::accept);
		}

		static void registerClientTickEvent(Consumer<Minecraft> consumer) {
			ClientTickEvent.CLIENT_PRE.register(consumer::accept);
		}
	}
}
