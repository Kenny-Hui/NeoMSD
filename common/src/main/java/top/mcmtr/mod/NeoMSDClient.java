package top.mcmtr.mod;

import mtr.data.PIDSType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import top.mcmtr.loader.MSDRegistryClient;
import top.mcmtr.mod.client.MSDClientData;
import top.mcmtr.mod.config.Config;
import top.mcmtr.mod.items.ItemMSDBlockClickingBase;
import top.mcmtr.mod.packet.MSDPacket;
import top.mcmtr.mod.packet.MSDPacketTrainDataGuiClient;
import top.mcmtr.mod.render.RenderCustomTextSign;
import top.mcmtr.mod.render.RenderPIDS;
import top.mcmtr.mod.render.RenderYamanoteRailwaySign;

public class NeoMSDClient {
    public static void init() {
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.YUUNI_PIDS.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.YUUNI_2_PIDS.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.translucent(), MSDBlocks.SURVEILLANCE_CAMERAS.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.translucent(), MSDBlocks.SURVEILLANCE_CAMERAS_WALL.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.HALL_SEAT_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.HALL_SEAT_MIDDLE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.HALL_SEAT_SIDE_MIRROR.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_POLE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_POLE_TOP_MIDDLE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_POLE_TOP_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_POLE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_POLE_BOTH_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_BOTH_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_2.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_RACK_1.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_NODE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CATENARY_NODE_STYLE_2.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_RACK_POLE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_RACK_POLE_BOTH_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_RACK_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_RACK_BOTH_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_RACK.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_NODE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.SHORT_CATENARY_NODE_STYLE_2.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.ELECTRIC_POLE_TOP_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.ELECTRIC_POLE_TOP_BOTH_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.ELECTRIC_POLE_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.ELECTRIC_POLE_ANOTHER_SIDE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.ELECTRIC_NODE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.RIGID_CATENARY_NODE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.YUUNI_TICKET.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CEILING_DOUBLE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.CEILING_DOUBLE_LIGHT.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.STANDING_SIGN.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.STANDING_SIGN_POLE.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.STANDING_SIGN_1.get());
        MSDRegistryClient.registerBlockRenderType(RenderType.cutout(), MSDBlocks.TRANS_CATENARY_NODE.get());

        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YUUNI_PIDS_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 2, 2.5F, 7.5F, 6F, 6.5F, 27, true, false, PIDSType.PIDS, 0xFF9900, 0x33CC00, 1.25F, true));
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YUUNI_PIDS_2_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 1, 4F, 7.5F, 5.9F, 2.5F, 24, true, false, PIDSType.PIDS, 0xFF9900, 0xFFC0CB));
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_4_PIDS_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 3, 0F, 15F, 7F, 6F, 32, true, true, PIDSType.PIDS, 0x00FF00, 0xFF0000));
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_5_PIDS_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 3, -4F, 15F, 7F, 6F, 40, true, true, PIDSType.PIDS, 0x00FF00, 0xFF0000));
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_6_PIDS_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 3, -8F, 15F, 7F, 6F, 48, true, true, PIDSType.PIDS, 0x00FF00, 0xFF0000));
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_7_PIDS_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 3, -12F, 15F, 7F, 6F, 56, true, true, PIDSType.PIDS, 0x00FF00, 0xFF0000));
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_2_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_2_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_3_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_3_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_4_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_4_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_5_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_5_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_6_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_6_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_7_EVEN_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_7_ODD_TILE_ENTITY.get(), RenderYamanoteRailwaySign::new);
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.STANDING_SIGN_TILE_ENTITY.get(), dispatcher -> new RenderCustomTextSign<>(dispatcher, 3, 8F, 14.5F, 7.01F, 15F, 11, true, 0x000000, true, 0xFFFFFF, 2F, 3.1F, 6.2F, 0.004F));
        MSDRegistryClient.registerTileEntityRenderer(MSDBlockEntityTypes.STANDING_SIGN_1_TILE_ENTITY.get(), dispatcher -> new RenderCustomTextSign<>(dispatcher, 1, 2.5F, 9.25F, 7.65F, 4F, 11, true, 0xFFFFFF, false, 0xFFFFFF, 1.6F, 1.6F, 3.2F, 0.0625F));

        
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_OPEN_YAMANOTE_RAILWAY_SIGN_SCREEN, packet -> MSDPacketTrainDataGuiClient.openYamanoteRailwaySignScreenS2C(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_WRITE_CATENARY, packet -> MSDClientData.writeCatenaries(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_REMOVE_CATENARY_NODE, packet -> MSDPacketTrainDataGuiClient.removeCatenaryNodeS2C(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_CREATE_CATENARY, packet -> MSDPacketTrainDataGuiClient.createCatenaryS2C(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_REMOVE_CATENARY, packet -> MSDPacketTrainDataGuiClient.removeCatenaryConnectionS2C(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_WRITE_RIGID_CATENARY, packet -> MSDClientData.writeRigidCatenaries(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_REMOVE_RIGID_CATENARY_NODE, packet -> MSDPacketTrainDataGuiClient.removeRigidCatenaryNodeS2C(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_CREATE_RIGID_CATENARY, packet -> MSDPacketTrainDataGuiClient.createRigidCatenaryS2C(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_REMOVE_RIGID_CATENARY, packet -> MSDPacketTrainDataGuiClient.removeRigidCatenaryConnectionS2C(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_OPEN_CUSTOM_TEXT_SIGN_CONFIG_SCREEN, packet -> MSDPacketTrainDataGuiClient.openCustomTextSignConfigScreenS2C(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_OPEN_BLOCK_NODE_SCREEN, packet -> MSDPacketTrainDataGuiClient.openBlockNodeScreenS2C(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_WRITE_TRANS_CATENARY, packet -> MSDClientData.writeTransCatenaries(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_CREATE_TRANS_CATENARY, packet -> MSDPacketTrainDataGuiClient.createTransCatenaryS2C(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_REMOVE_TRANS_CATENARY, packet -> MSDPacketTrainDataGuiClient.removeTransCatenaryConnectionS2C(Minecraft.getInstance(), packet));
        MSDRegistryClient.registerNetworkReceiver(MSDPacket.PACKET_REMOVE_TRANS_CATENARY_NODE, packet -> MSDPacketTrainDataGuiClient.removeTransCatenaryNodeS2C(Minecraft.getInstance(), packet));
        Config.refreshProperties();
    }

    public static void registerItemModelPredicates() {
        MSDRegistryClient.registerItemModelPredicate(NeoMSD.MOD_ID + ":selected", MSDItems.CATENARY_REMOVER.get(), ItemMSDBlockClickingBase.TAG_POS);
        MSDRegistryClient.registerItemModelPredicate(NeoMSD.MOD_ID + ":selected", MSDItems.ELECTRIC_CONNECTOR.get(), ItemMSDBlockClickingBase.TAG_POS);
        MSDRegistryClient.registerItemModelPredicate(NeoMSD.MOD_ID + ":selected", MSDItems.CATENARY_CONNECTOR.get(), ItemMSDBlockClickingBase.TAG_POS);
        MSDRegistryClient.registerItemModelPredicate(NeoMSD.MOD_ID + ":selected", MSDItems.RIGID_CATENARY_CONNECTOR.get(), ItemMSDBlockClickingBase.TAG_POS);
        MSDRegistryClient.registerItemModelPredicate(NeoMSD.MOD_ID + ":selected", MSDItems.RIGID_CATENARY_REMOVER.get(), ItemMSDBlockClickingBase.TAG_POS);
        MSDRegistryClient.registerItemModelPredicate(NeoMSD.MOD_ID + ":selected", MSDItems.RIGID_SOFT_CATENARY_CONNECTOR.get(), ItemMSDBlockClickingBase.TAG_POS);
        MSDRegistryClient.registerItemModelPredicate(NeoMSD.MOD_ID + ":selected", MSDItems.TRANS_CATENARY_CONNECTOR.get(),ItemMSDBlockClickingBase.TAG_POS);
        MSDRegistryClient.registerItemModelPredicate(NeoMSD.MOD_ID + ":selected", MSDItems.TRANS_CATENARY_REMOVER.get(),ItemMSDBlockClickingBase.TAG_POS);
        MSDRegistryClient.registerItemModelPredicate(NeoMSD.MOD_ID + ":selected", MSDItems.TRANS_ELECTRIC_CONNECTOR.get(), ItemMSDBlockClickingBase.TAG_POS);
    }
}