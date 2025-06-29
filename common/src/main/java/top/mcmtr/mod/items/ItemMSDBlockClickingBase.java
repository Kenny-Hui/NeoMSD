package top.mcmtr.mod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;

public abstract class ItemMSDBlockClickingBase extends Item {
    public static final String TAG_POS = "catenary_pos";

    public ItemMSDBlockClickingBase(Properties properties) {
        super(properties);
    }

    /**
     * 首先检测是否为客户端侧，之后检查手中物品存储信息的上下文，如果是第一个点则存储，如果是第二个点则和第一个点构成一条线，完成连接任务后就删除这些信息以供下一次使用
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide()) {
            if (clickCondition(context)) {
                final CustomData customData = context.getItemInHand().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                final CompoundTag compoundTag = customData.copyTag();
                if (compoundTag.contains(TAG_POS)) {
                    final BlockPos posEnd = BlockPos.of(compoundTag.getLong(TAG_POS));
                    onEndClick(context, posEnd, compoundTag);
                    compoundTag.remove(TAG_POS);
                } else {
                    compoundTag.putLong(TAG_POS, context.getClickedPos().asLong());
                    onStartClick(context, compoundTag);
                }
                context.getItemInHand().set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return super.useOn(context);
        }
    }

    /**
     * 第一次点击时的操作
     */
    protected abstract void onStartClick(UseOnContext context, CompoundTag compoundTag);

    /**
     * 第二次点击时的操作
     */
    protected abstract void onEndClick(UseOnContext context, BlockPos posEnd, CompoundTag compoundTag);

    /**
     * 点击操作具体内容
     */
    protected abstract boolean clickCondition(UseOnContext context);
}