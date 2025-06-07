package top.mcmtr.mod.screen;

import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.screen.WidgetBetterTextField;
import mtr.screen.WidgetShorterSlider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import top.mcmtr.mod.data.BlockLocation;
import top.mcmtr.mod.packet.MSDPacketTrainDataGuiClient;

public class BlockNodeScreen extends ScreenMapper implements IGui {
    private final WidgetShorterSlider sliderPositionX;
    private final WidgetShorterSlider sliderPositionY;
    private final WidgetShorterSlider sliderPositionZ;
    private final BlockLocation location;
    private final BlockPos pos;
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

    public BlockNodeScreen(BlockLocation location, BlockPos pos) {
        super(Text.literal(""));
        this.location = location;
        this.pos = pos;
        this.sliderPositionX = new WidgetShorterSlider(0, 0, 16, num -> String.format("%d", num - 8), null);
        this.sliderPositionY = new WidgetShorterSlider(0, 0, 16, num -> String.format("%d", num - 8), null);
        this.sliderPositionZ = new WidgetShorterSlider(0, 0, 16, num -> String.format("%d", num - 8), null);
    }

    @Override
    protected void init() {
        super.init();
        int i = 1;
        setPositionAndWidth(sliderPositionX, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - font.width("-8") + (SQUARE_SIZE * 9));
        setPositionAndWidth(sliderPositionY, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - font.width("-8") + (SQUARE_SIZE * 9));
        setPositionAndWidth(sliderPositionZ, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, (SQUARE_SIZE + TEXT_FIELD_PADDING) * i + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - font.width("-8") + (SQUARE_SIZE * 9));
        sliderPositionX.setHeight(BUTTON_HEIGHT);
        sliderPositionY.setHeight(BUTTON_HEIGHT);
        sliderPositionZ.setHeight(BUTTON_HEIGHT);
        sliderPositionX.setValue((int) (location.getX() * 16 + 8));
        sliderPositionY.setValue((int) (location.getY() * 16 + 8));
        sliderPositionZ.setValue((int) (location.getZ() * 16 + 8));
        addDrawableChild(sliderPositionX);
        addDrawableChild(sliderPositionY);
        addDrawableChild(sliderPositionZ);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderBackground(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(font, Text.translatable("gui.msd.point_position_options"), width / 2, TEXT_PADDING, ARGB_WHITE);
        int i = 1;
        guiGraphics.drawString(font, Text.translatable("options.msd.point_position_x"), SQUARE_SIZE, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
        guiGraphics.drawString(font, Text.translatable("options.msd.point_position_y"), SQUARE_SIZE, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
        guiGraphics.drawString(font, Text.translatable("options.msd.point_position_z"), SQUARE_SIZE, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
    }

    @Override
    public void onClose() {
        super.onClose();
        location.setX((sliderPositionX.getIntValue() - 8) / 16D);
        location.setY((sliderPositionY.getIntValue() - 8) / 16D);
        location.setZ((sliderPositionZ.getIntValue() - 8) / 16D);
        MSDPacketTrainDataGuiClient.sendBlockNodeLocationC2S(location, pos);
    }

    static void setPositionAndWidth(AbstractWidget widget, int x, int y, int widgetWidth) {
        UtilitiesClient.setWidgetX(widget, x);
        UtilitiesClient.setWidgetY(widget, y);
        widget.setWidth(Mth.clamp(widgetWidth, 0, 380 - (widget instanceof WidgetBetterTextField ? TEXT_FIELD_PADDING : 0)));
    }
}