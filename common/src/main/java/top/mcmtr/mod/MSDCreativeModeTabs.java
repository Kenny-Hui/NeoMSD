package top.mcmtr.mod;

import mtr.mappings.Utilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import top.mcmtr.loader.MSDRegistry;

import java.util.function.Supplier;

public interface MSDCreativeModeTabs {
    Wrapper MSD_BLOCKS = new Wrapper(NeoMSD.id("msd_blocks"), () -> new ItemStack(MSDBlocks.RAILING_STAIR_FLAT.get()));
    Wrapper MSD_Station_Decoration = new Wrapper(NeoMSD.id("msd_station_blocks"), () -> new ItemStack(MSDBlocks.DECORATION_STAIR.get()));

    public static class Wrapper {
        public final ResourceLocation resourceLocation;
        private final Supplier<CreativeModeTab> creativeModeTabSupplier;
        private CreativeModeTab creativeModeTab;

        public Wrapper(ResourceLocation resourceLocation, Supplier<ItemStack> itemSupplier) {
            this.resourceLocation = resourceLocation;
            this.creativeModeTabSupplier = MSDRegistry.registerCreativeModeTab(resourceLocation, itemSupplier);
        }

        public CreativeModeTab get() {
            if (this.creativeModeTab == null) {
                this.creativeModeTab = (CreativeModeTab)this.creativeModeTabSupplier.get();
            }

            return this.creativeModeTab;
        }

        public Wrapper() {
            this.resourceLocation = ResourceLocation.parse("");
            this.creativeModeTabSupplier = Utilities::getDefaultTab;
        }
    }
}