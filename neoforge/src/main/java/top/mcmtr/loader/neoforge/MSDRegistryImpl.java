package top.mcmtr.loader.neoforge;

import mtr.RegistryObject;
import mtr.item.ItemWithCreativeTabBase;
import mtr.mappings.NetworkUtilities;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
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
import net.neoforged.bus.api.IEventBus;
import top.mcmtr.loader.neoforge.mappings.ArchitecturyUtilities;
import top.mcmtr.loader.neoforge.mappings.CompatPacketRegistry;
import top.mcmtr.mod.MSDCreativeModeTabs;
import top.mcmtr.mod.NeoMSD;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MSDRegistryImpl {
    public static CompatPacketRegistry PACKET_REGISTRY = new CompatPacketRegistry();
    private static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(NeoMSD.MOD_ID, ForgeUtilities.registryGetItem());
    private static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(NeoMSD.MOD_ID, ForgeUtilities.registryGetBlock());
    private static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder<>(NeoMSD.MOD_ID, ForgeUtilities.registryGetBlockEntityType());
    private static final DeferredRegisterHolder<EntityType<?>> ENTITY_TYPES = new DeferredRegisterHolder<>(NeoMSD.MOD_ID, ForgeUtilities.registryGetEntityType());
    private static final DeferredRegisterHolder<SoundEvent> SOUND_EVENTS = new DeferredRegisterHolder<>(NeoMSD.MOD_ID, ForgeUtilities.registryGetSoundEvent());
    private static final DeferredRegisterHolder<ParticleType<?>> PARTICLE_TYPES = new DeferredRegisterHolder<>(NeoMSD.MOD_ID, ForgeUtilities.registryGetParticleType());
    private static final DeferredRegisterHolder<CreativeModeTab> CREATIVE_MODE_TABS = new DeferredRegisterHolder<>(NeoMSD.MOD_ID, Registries.CREATIVE_MODE_TAB);


    public static void registerBlock(String id, RegistryObject<Block> block) {
        BLOCKS.register(id, block::get);
    }

    public static void registerBlockAndItem(String id, RegistryObject<Block> block, MSDCreativeModeTabs.Wrapper tab) {
        BLOCKS.register(id, block::get);
        ITEMS.register(id, () -> {
            final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
            ForgeUtilities.hookItemToCreativeTab(tab.resourceLocation, blockItem);
            return blockItem;
        });
    }

    public static void registerItem(String id, RegistryObject<? extends Item> item, MSDCreativeModeTabs.Wrapper tab) {
        ITEMS.register(id, () -> {
            final Item itemObject = item.get();

            if(tab != null) {
                ForgeUtilities.hookItemToCreativeTab(tab.resourceLocation, itemObject);
            }
            return itemObject;
        });
    }

    public static void registerBlockEntityType(String id, RegistryObject<? extends BlockEntityType<? extends BlockEntity>> blockEntityType) {
        BLOCK_ENTITY_TYPES.register(id, blockEntityType::get);
    }

    public static void registerEntityType(String id, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        ENTITY_TYPES.register(id, entityType::get);
    }

    public static Supplier<CreativeModeTab> registerCreativeModeTab(ResourceLocation id, Supplier<ItemStack> supplier) {
        String normalizedPath = id.getPath().startsWith(id.getNamespace() + "_")
                ? id.getPath().substring(id.getNamespace().length() + 1) : id.getPath();
        return ForgeUtilities.registerCreativeModeTab(id, supplier,
                String.format("itemGroup.%s.%s", id.getNamespace(), normalizedPath));
    }

    public static void registerSoundEvent(String id, SoundEvent soundEvent) {
        SOUND_EVENTS.register(id, () -> soundEvent);
    }

    public static void registerParticleType(String id, ParticleType<?> particleType) {
        PARTICLE_TYPES.register(id, () -> particleType);
    }

    public static SimpleParticleType createParticleType(boolean overrideLimiter) {
        return new SimpleParticleType(overrideLimiter);
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
        ArchitecturyUtilities.registerPlayerJoinEvent(consumer);
        ArchitecturyUtilities.registerPlayerChangeDimensionEvent(consumer);
    }

    public static void registerPlayerQuitEvent(Consumer<ServerPlayer> consumer) {
        ArchitecturyUtilities.registerPlayerQuitEvent(consumer);
    }

    public static void registerTickEvent(Consumer<MinecraftServer> consumer) {
        ArchitecturyUtilities.registerTickEvent(consumer);
    }

    public static void registerAllDeferred(IEventBus eventBus) {
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
        ENTITY_TYPES.register(eventBus);
        SOUND_EVENTS.register(eventBus);
        PARTICLE_TYPES.register(eventBus);
        ForgeUtilities.registerCreativeModeTabsToDeferredRegistry(CREATIVE_MODE_TABS);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
