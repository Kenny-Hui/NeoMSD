package top.mcmtr.loader.fabric;

import mtr.mappings.NetworkUtilities;
import mtr.RegistryObject;
import mtr.mappings.Text;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import top.mcmtr.mod.MSDCreativeModeTabs;
import top.mcmtr.mod.NeoMSD;
import top.mcmtr.mod.items.ItemWithCreativeTabBase;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MSDRegistryImpl {
    public static CompatPacketRegistry PACKET_REGISTRY = new CompatPacketRegistry();

    public static void registerBlock(String id, RegistryObject<Block> block) {
        Registry.register(BuiltInRegistries.BLOCK, NeoMSD.id(id), block.get());
    }

    public static void registerBlockAndItem(String id, RegistryObject<Block> block, MSDCreativeModeTabs.Wrapper tab) {
        registerBlock(id, block);
        final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, NeoMSD.id(id), blockItem);
        hookItemToCreativeTab(tab.get(), blockItem);
    }

    public static void registerItem(String id, RegistryObject<? extends Item> item, MSDCreativeModeTabs.Wrapper tab) {
        Registry.register(BuiltInRegistries.ITEM, NeoMSD.id(id), item.get());

        if(tab != null) {
            hookItemToCreativeTab(tab.get(), item.get());
        }
    }

    public static Supplier<CreativeModeTab> registerCreativeModeTab(ResourceLocation id, Supplier<ItemStack> supplier) {
        return () -> {
            String normalizedPath = id.getPath().startsWith(id.getNamespace() + "_") ? id.getPath().substring(id.getNamespace().length() + 1) : id.getPath();
            CreativeModeTab tab = FabricItemGroup.builder()
                    .icon(supplier)
                    .title(Text.translatable(String.format("itemGroup.%s.%s", id.getNamespace(), normalizedPath)))
                    .build();
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id, tab);
            return tab;
        };
    }

    public static void registerBlockEntityType(String id, RegistryObject<? extends BlockEntityType<? extends BlockEntity>> blockEntityType) {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, NeoMSD.id(id), blockEntityType.get());
    }

    public static void registerEntityType(String id, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, NeoMSD.id(id), entityType.get());
    }

    public static void registerSoundEvent(String id, SoundEvent soundEvent) {
        Registry.register(BuiltInRegistries.SOUND_EVENT, NeoMSD.id(id), soundEvent);
    }

    public static void registerParticleType(String id, ParticleType<?> particleType) {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, NeoMSD.id(id), particleType);
    }

    public static SimpleParticleType createParticleType(boolean overrideLimiter) {
        return FabricParticleTypes.simple(overrideLimiter);
    }

    public static void registerNetworkPacket(ResourceLocation resourceLocation) {
        PACKET_REGISTRY.registerPacket(resourceLocation);
    }

    public static void registerNetworkReceiver(ResourceLocation resourceLocation, NetworkUtilities.PacketCallback packetCallback) {
        PACKET_REGISTRY.registerNetworkReceiverC2S(resourceLocation, packetCallback);
    }

    public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf packet) {
        PACKET_REGISTRY.sendS2C(player, id, packet);
    }

    public static void registerPlayerJoinEvent(Consumer<ServerPlayer> consumer) {
        ServerEntityEvents.ENTITY_LOAD.register((entity, serverWorld) -> {
            if (entity instanceof ServerPlayer) {
                consumer.accept((ServerPlayer) entity);
            }
        });
    }

    public static void registerPlayerQuitEvent(Consumer<ServerPlayer> consumer) {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> consumer.accept(handler.player));
    }

    public static void registerTickEvent(Consumer<MinecraftServer> consumer) {
        ServerTickEvents.START_SERVER_TICK.register(consumer::accept);
    }

    private static void hookItemToCreativeTab(CreativeModeTab creativeModeTab, Item item) {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, entries) -> {
            if (tab == creativeModeTab) entries.accept(item);
        });
    }
}
