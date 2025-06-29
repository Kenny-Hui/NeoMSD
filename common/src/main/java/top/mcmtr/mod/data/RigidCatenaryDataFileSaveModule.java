package top.mcmtr.mod.data;

import mtr.data.MessagePackHelper;
import mtr.data.SerializedDataBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;
import top.mcmtr.mod.NeoMSD;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class RigidCatenaryDataFileSaveModule extends DataModuleBase {
    private final Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries;

    public RigidCatenaryDataFileSaveModule(Level world, Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenaries, Path savePath) {
        super(savePath.resolve("rigid_catenaries"), world);
        this.rigidCatenaries = rigidCatenaries;
        try {
            Files.createDirectories(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        existingFiles.clear();
        readMessagePackFromFile(filePath, RigidCatenaryEntry::new, rigidCatenaryEntry -> rigidCatenaries.put(rigidCatenaryEntry.pos, rigidCatenaryEntry.connections));
        NeoMSD.LOGGER.info("[{}] MTR Station Decoration Catenary data successfully load for {}", NeoMSD.BRAND, world.dimension().location());
        canAutoSave = true;
        dataLoaded = true;
    }

    public void autoSave() {
        checkDataLoaded();
        if (canAutoSave && checkFilesToDelete.isEmpty()) {
            enableAutoSave();
            dirtyPositions.addAll(rigidCatenaries.keySet());
        }
    }

    public boolean autoSaveTick() {
        if (canAutoSave) {
            final boolean deleteEmptyOld = checkFilesToDelete.isEmpty();
            boolean hasSpareTime = writeDirtyDataToFile(dirtyPositions, pos -> rigidCatenaries.containsKey(pos) ? new RigidCatenaryEntry(pos, rigidCatenaries.get(pos)) : null, BlockPos::asLong, filePath);
            final boolean doneWriting = dirtyPositions.isEmpty();
            if (hasSpareTime && !checkFilesToDelete.isEmpty() && doneWriting) {
                final Path path = checkFilesToDelete.remove(0);
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                existingFiles.remove(path);
                filesDeleted++;
            }
            if (!deleteEmptyOld && checkFilesToDelete.isEmpty()) {
                if (!useReducedHash || filesWritten > 0 || filesDeleted > 0) {
                    NeoMSD.LOGGER.info("[{}] MTR Station Decoration save complete for {} in {} second(s) ", NeoMSD.BRAND, world.dimension().location(), (System.currentTimeMillis() - autoSaveStartMillis) / 1000);
                    if (filesWritten > 0) {
                        NeoMSD.LOGGER.info("- Changed: {}", filesWritten);
                    }
                    if (filesDeleted > 0) {
                        NeoMSD.LOGGER.info("- Deleted: {}", filesDeleted);
                    }
                }
            }
            return doneWriting && checkFilesToDelete.isEmpty();
        } else {
            return true;
        }
    }

    private static class RigidCatenaryEntry extends SerializedDataBase {
        public final BlockPos pos;
        public final Map<BlockPos, RigidCatenary> connections;
        private static final String KEY_NODE_POS = "rigid_catenary_node_pos";
        private static final String KEY_CATENARY_CONNECTIONS = "rigid_catenary_connections";

        public RigidCatenaryEntry(BlockPos pos, Map<BlockPos, RigidCatenary> connections) {
            this.pos = pos;
            this.connections = connections;
        }

        public RigidCatenaryEntry(Map<String, Value> map) {
            final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
            this.pos = BlockPos.of(messagePackHelper.getLong(KEY_NODE_POS));
            this.connections = new HashMap<>();
            messagePackHelper.iterateArrayValue(KEY_CATENARY_CONNECTIONS, value -> {
                final Map<String, Value> mapSK = RigidCatenaryData.castMessagePackValueToSKMap(value);
                connections.put(BlockPos.of(new MessagePackHelper(mapSK).getLong(KEY_NODE_POS)), new RigidCatenary(mapSK));
            });
        }

        @Override
        public void toMessagePack(MessagePacker messagePacker) throws IOException {
            messagePacker.packString(KEY_NODE_POS).packLong(pos.asLong());
            messagePacker.packString(KEY_CATENARY_CONNECTIONS).packArrayHeader(connections.size());
            for (final Map.Entry<BlockPos, RigidCatenary> entry : connections.entrySet()) {
                final BlockPos endNodePos = entry.getKey();
                messagePacker.packMapHeader(entry.getValue().messagePackLength() + 1);
                messagePacker.packString(KEY_NODE_POS).packLong(endNodePos.asLong());
                entry.getValue().toMessagePack(messagePacker);
            }
        }

        @Override
        public int messagePackLength() {
            return 2;
        }

        @Override
        public void writePacket(FriendlyByteBuf friendlyByteBuf) {
        }
    }
}