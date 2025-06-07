package top.mcmtr.mod.packet;

import net.minecraft.resources.ResourceLocation;
import top.mcmtr.mod.NeoMSD;

public interface MSDPacket {
    ResourceLocation PACKET_OPEN_YAMANOTE_RAILWAY_SIGN_SCREEN = NeoMSD.id("packet_open_yamanote_railway_sign_screen");
    ResourceLocation PACKET_YAMANOTE_SIGN_TYPES = NeoMSD.id("packet_yamanote_sign_types");
    ResourceLocation PACKET_REMOVE_CATENARY_NODE = NeoMSD.id("packet_remove_catenary_node");
    ResourceLocation PACKET_REMOVE_CATENARY = NeoMSD.id("packet_remove_catenary");
    ResourceLocation PACKET_CREATE_CATENARY = NeoMSD.id("packet_create_catenary");
    ResourceLocation PACKET_WRITE_CATENARY = NeoMSD.id("write_catenary");
    ResourceLocation PACKET_REMOVE_RIGID_CATENARY_NODE = NeoMSD.id("packet_remove_rigid_catenary_node");
    ResourceLocation PACKET_REMOVE_RIGID_CATENARY = NeoMSD.id("packet_remove_rigid_catenary");
    ResourceLocation PACKET_CREATE_RIGID_CATENARY = NeoMSD.id("packet_create_rigid_catenary");
    ResourceLocation PACKET_WRITE_RIGID_CATENARY = NeoMSD.id("packet_write_rigid_catenary");
    ResourceLocation PACKET_OPEN_CUSTOM_TEXT_SIGN_CONFIG_SCREEN = NeoMSD.id("packet_open_custom_test_sign_config_screen");
    ResourceLocation PACKET_CUSTOM_TEXT_SIGN_UPDATE = NeoMSD.id("packet_custom_text_sign_update");
    ResourceLocation PACKET_REMOVE_TRANS_CATENARY_NODE = NeoMSD.id("packet_remove_trans_catenary_node");
    ResourceLocation PACKET_REMOVE_TRANS_CATENARY = NeoMSD.id("packet_remove_trans_catenary");
    ResourceLocation PACKET_CREATE_TRANS_CATENARY = NeoMSD.id("packet_create_trans_catenary");
    ResourceLocation PACKET_WRITE_TRANS_CATENARY = NeoMSD.id("write_trans_catenary");
    ResourceLocation PACKET_OPEN_BLOCK_NODE_SCREEN = NeoMSD.id("packet_open_block_node_screen");
    ResourceLocation PACKET_BLOCK_NODE_POS_UPDATE = NeoMSD.id("packet_block_node_pos_update");
}