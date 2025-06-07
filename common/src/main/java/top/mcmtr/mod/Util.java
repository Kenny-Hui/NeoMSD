package top.mcmtr.mod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class Util {
    public static Vec3 toVec3(BlockPos pos) {
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }
}
