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

public class CatenaryDataFileSaveModule extends DataModuleBase {
    private final Map<BlockPos, Map<BlockPos, Catenary>> catenaries;

    /**
     * 文件存储到世界，并创建初始路径
     * */
    public CatenaryDataFileSaveModule(Level world, Map<BlockPos, Map<BlockPos, Catenary>> catenaries, Path savePath) {
        super(savePath.resolve("catenaries"), world);
        this.catenaries = catenaries;
        try {
            Files.createDirectories(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中加载数据
     * */
    public void load() {
        existingFiles.clear();
        readMessagePackFromFile(filePath, CatenaryEntry::new, catenaryEntry -> catenaries.put(catenaryEntry.pos, catenaryEntry.connections));
        NeoMSD.LOGGER.info("[{}] MTR Station Decoration data successfully load for {}", NeoMSD.BRAND, world.dimension().location());
        canAutoSave = true;
        dataLoaded = true;
    }

    @Override
    public void autoSave() {
        checkDataLoaded();
        if (canAutoSave && checkFilesToDelete.isEmpty()) {
            enableAutoSave();
            dirtyPositions.addAll(catenaries.keySet());
        }
    }

    public boolean autoSaveTick() {
        if (canAutoSave) {
            final boolean deleteEmptyOld = checkFilesToDelete.isEmpty();
            boolean hasSpareTime = writeDirtyDataToFile(dirtyPositions, pos -> catenaries.containsKey(pos) ? new CatenaryEntry(pos, catenaries.get(pos)) : null, BlockPos::asLong, filePath);
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
                    NeoMSD.LOGGER.info("[{}] MTR Station Decoration save complete for {} in {} second(s)", NeoMSD.BRAND, world.dimension().location(), (System.currentTimeMillis() - autoSaveStartMillis) / 1000);
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

    /**
     * 接触网存储本地及读取的入口
     * */
    private static class CatenaryEntry extends SerializedDataBase {
        public final BlockPos pos;
        public final Map<BlockPos, Catenary> connections;
        private static final String KEY_NODE_POS = "catenary_node_pos";
        private static final String KEY_CATENARY_CONNECTIONS = "catenary_connections";

        public CatenaryEntry(BlockPos pos, Map<BlockPos, Catenary> connections) {
            this.pos = pos;
            this.connections = connections;
        }

        public CatenaryEntry(Map<String, Value> map) {
            final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
            this.pos = BlockPos.of(messagePackHelper.getLong(KEY_NODE_POS));
            this.connections = new HashMap<>();
            messagePackHelper.iterateArrayValue(KEY_CATENARY_CONNECTIONS, value -> {
                final Map<String, Value> mapSK = CatenaryData.castMessagePackValueToSKMap(value);
                connections.put(BlockPos.of(new MessagePackHelper(mapSK).getLong(KEY_NODE_POS)), new Catenary(mapSK));
            });
        }

        @Override
        public void toMessagePack(MessagePacker messagePacker) throws IOException {
            messagePacker.packString(KEY_NODE_POS).packLong(pos.asLong());
            messagePacker.packString(KEY_CATENARY_CONNECTIONS).packArrayHeader(connections.size());
            for (final Map.Entry<BlockPos, Catenary> entry : connections.entrySet()) {
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
        public void writePacket(FriendlyByteBuf packet) {
        }
    }
}