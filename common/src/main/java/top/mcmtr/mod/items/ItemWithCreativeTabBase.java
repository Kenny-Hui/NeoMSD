package top.mcmtr.mod.items;

import mtr.mappings.PlaceOnWaterBlockItem;
import mtr.mappings.RegistryUtilities;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import top.mcmtr.mod.MSDCreativeModeTabs;

import java.util.function.Function;

public class ItemWithCreativeTabBase extends Item {
    public final MSDCreativeModeTabs.Wrapper creativeModeTab;

    public ItemWithCreativeTabBase(MSDCreativeModeTabs.Wrapper creativeModeTab) {
        super(RegistryUtilities.createItemProperties(creativeModeTab::get));
        this.creativeModeTab = creativeModeTab;
    }

    public ItemWithCreativeTabBase(MSDCreativeModeTabs.Wrapper creativeModeTab, Function<Properties, Properties> propertiesConsumer) {
        super(propertiesConsumer.apply(RegistryUtilities.createItemProperties(creativeModeTab::get)));
        this.creativeModeTab = creativeModeTab;
    }

    public static class ItemPlaceOnWater extends PlaceOnWaterBlockItem {
        public final MSDCreativeModeTabs.Wrapper creativeModeTab;

        public ItemPlaceOnWater(MSDCreativeModeTabs.Wrapper creativeModeTab, Block block) {
            super(block, RegistryUtilities.createItemProperties(creativeModeTab::get));
            this.creativeModeTab = creativeModeTab;
        }
    }
}