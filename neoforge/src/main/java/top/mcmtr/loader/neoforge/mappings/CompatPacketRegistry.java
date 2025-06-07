package top.mcmtr.loader.neoforge.mappings;

import mtr.mappings.NetworkUtilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CompatPacketRegistry {

    public HashMap<ResourceLocation, CompatPacket> packets = new HashMap<>();
    public HashMap<ResourceLocation, Consumer<FriendlyByteBuf>> packetsS2C = new HashMap<>();
    public HashMap<ResourceLocation, NetworkUtilities.PacketCallback> packetsC2S = new HashMap<>();

    public void registerPacket(ResourceLocation resourceLocation) {
        packets.computeIfAbsent(resourceLocation, CompatPacket::new);
    }

    public void registerNetworkReceiverS2C(ResourceLocation resourceLocation, Consumer<FriendlyByteBuf> consumer) {
        packets.computeIfAbsent(resourceLocation, CompatPacket::new);
        packetsS2C.put(resourceLocation, consumer);
    }

    public void registerNetworkReceiverC2S(ResourceLocation resourceLocation, NetworkUtilities.PacketCallback consumer) {
        packets.computeIfAbsent(resourceLocation, CompatPacket::new);
        packetsC2S.put(resourceLocation, consumer);
    }

    public void commit(PayloadRegistrar registrar) {
        for (Map.Entry<ResourceLocation, CompatPacket> packets : packets.entrySet()) {
            Consumer<FriendlyByteBuf> handlerS2C = packetsS2C.getOrDefault(packets.getKey(), arg -> {});
            NetworkUtilities.PacketCallback handlerC2S = packetsC2S.getOrDefault(packets.getKey(), (server, player, arg) -> {});
            CompatPacket packet = packets.getValue();
            registrar.playBidirectional(packet.TYPE, packet.STREAM_CODEC, new DirectionalPayloadHandler<>(
                    (arg, iPayloadContext) -> handlerS2C.accept(arg.buffer),
                    (arg, iPayloadContext) -> handlerC2S.packetCallback(
                            iPayloadContext.player().getServer(), (ServerPlayer)iPayloadContext.player(), arg.buffer)
            ));
        }
    }

    public void sendS2C(ServerPlayer player, ResourceLocation id, FriendlyByteBuf payload) {
        CompatPacket packet = packets.get(id);
        if(packet == null) throw new IllegalStateException("Packet " + id + " is not registered!");
        PacketDistributor.sendToPlayer(player, packet.new Payload(payload));
    }

    public void sendC2S(ResourceLocation id, FriendlyByteBuf payload) {
        CompatPacket packet = packets.get(id);
        if(packet == null) throw new IllegalStateException("Packet " + id + " is not registered!");
        PacketDistributor.sendToServer(packet.new Payload(payload));
    }
}
