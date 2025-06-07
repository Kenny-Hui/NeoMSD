package top.mcmtr.mod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockStandingSignPole extends BlockChangeModelBase {
    public BlockStandingSignPole() {
        super(2, Properties.of().requiresCorrectToolForDrops().strength(2.0F).lightLevel((state) -> 5));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(7.375, 0, 7.375, 8.625, 16, 8.625);
    }
}