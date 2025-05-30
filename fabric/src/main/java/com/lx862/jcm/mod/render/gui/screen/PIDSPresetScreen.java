package com.lx862.jcm.mod.render.gui.screen;

import com.lx862.jcm.mod.block.entity.PIDSBlockEntity;
import com.lx862.jcm.mod.data.pids.PIDSManager;
import com.lx862.jcm.mod.data.pids.preset.PIDSPresetBase;
import com.lx862.jcm.mod.render.GuiHelper;
import com.lx862.jcm.mod.render.RenderHelper;
import com.lx862.jcm.mod.render.gui.screen.base.TitledScreen;
import com.lx862.jcm.mod.render.gui.widget.ContentItem;
import com.lx862.jcm.mod.render.gui.widget.HorizontalWidgetSet;
import com.lx862.jcm.mod.render.gui.widget.ListViewWidget;
import com.lx862.jcm.mod.render.gui.widget.MappedWidget;
import com.lx862.jcm.mod.util.TextCategory;
import com.lx862.jcm.mod.util.TextUtil;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GuiDrawing;
import org.mtr.mapping.mapper.TextFieldWidgetExtension;
import org.mtr.mapping.tool.TextCase;

import java.util.function.Consumer;

public class PIDSPresetScreen extends TitledScreen implements RenderHelper, GuiHelper {
    private static final Identifier PIDS_PREVIEW_BASE = new Identifier("jsblock:textures/gui/pids_preview.png");
    private final TextFieldWidgetExtension searchBox;
    private final ListViewWidget listViewWidget;
    private final Consumer<String> callback;
    private final String selectedPreset;
    private final String pidsType;
    public PIDSPresetScreen(BlockPos pos, String selectedPreset, Consumer<String> callback) {
        super(false);
        BlockEntity be = MinecraftClient.getInstance().getWorldMapped().getBlockEntity(pos);
        if(be != null && be.data instanceof PIDSBlockEntity) {
            this.pidsType = ((PIDSBlockEntity)be.data).getPIDSType();
        } else {
            this.pidsType = "";
        }
        this.selectedPreset = selectedPreset;
        this.callback = callback;
        this.listViewWidget = new ListViewWidget();
        this.searchBox = new TextFieldWidgetExtension(0, 0, 0, 22, 60, TextCase.DEFAULT, null, TextUtil.translatable(TextCategory.GUI, "widget.search").getString());
    }

    @Override
    protected void init2() {
        super.init2();
        int contentWidth = (int)Math.min((width * 0.75), MAX_CONTENT_WIDTH);
        int listViewHeight = (int)((height - 60) * 0.76);
        int startX = (width - contentWidth) / 2;
        int searchStartY = TEXT_PADDING * 5;
        int startY = searchStartY + (TEXT_PADDING * 3);

        listViewWidget.reset();
        addConfigEntries();
        searchBox.setX2(startX);
        searchBox.setY2(searchStartY);
        searchBox.setWidth2(contentWidth);
        searchBox.setChangedListener2(listViewWidget::setSearchTerm);
        searchBox.setText2(searchBox.getText2());

        listViewWidget.setXYSize(startX, startY, contentWidth, listViewHeight);
        addChild(new ClickableWidget(listViewWidget));
        addChild(new ClickableWidget(searchBox));
    }

    @Override
    public MutableText getScreenTitle() {
        return TextUtil.translatable(TextCategory.GUI, "pids_preset.title");
    }

    @Override
    public MutableText getScreenSubtitle() {
        return TextUtil.translatable(TextCategory.GUI, "pids_preset.subtitle", selectedPreset);
    }

    public void addConfigEntries() {
        listViewWidget.addCategory(TextUtil.translatable(TextCategory.GUI, "pids_preset.listview.category.builtin"));
        for(PIDSPresetBase preset : PIDSManager.getBuiltInPresets()) {
            addPreset(preset);
        }

        if(!PIDSManager.getCustomPresets().isEmpty()) {
            listViewWidget.addCategory(TextUtil.translatable(TextCategory.GUI, "pids_preset.listview.category.custom"));
            for(PIDSPresetBase preset : PIDSManager.getCustomPresets()) {
                addPreset(preset);
            }
        }
    }

    private void addPreset(PIDSPresetBase preset) {
        if(!preset.typeAllowed(pidsType)) return;

        ButtonWidgetExtension selectBtn = new ButtonWidgetExtension(0, 0, 60, 20, TextUtil.translatable(TextCategory.GUI, "pids_preset.listview.widget.choose"), (btn) -> {
            choose(preset.getId());
        });

        if(preset.getId().equals(selectedPreset)) {
            selectBtn.setMessage2(Text.cast(TextUtil.translatable(TextCategory.GUI, "pids_preset.listview.widget.selected")));
            selectBtn.active = false;
        }

        HorizontalWidgetSet widgetSet = new HorizontalWidgetSet();
        widgetSet.addWidget(new MappedWidget(selectBtn));
        widgetSet.setXYSize(0, 0, 100, 20);

        addChild(new ClickableWidget(selectBtn));
        addChild(new ClickableWidget(widgetSet));
        ContentItem contentItem = new ContentItem(TextUtil.literal(preset.getName()), new MappedWidget(widgetSet), 26);

        contentItem.setIconCallback((guiDrawing, startX, startY, width, height) -> {
            drawPIDSPreview(preset, guiDrawing, startX, startY, width, height, false);
        });
        listViewWidget.add(contentItem);
    }

    public static void drawPIDSPreview(PIDSPresetBase preset, GuiDrawing guiDrawing, int startX, int startY, int width, int height, boolean backgroundOnly) {
        final int offset = 6;

        // Background
        GuiHelper.drawTexture(guiDrawing, PIDS_PREVIEW_BASE, startX, startY, width, height);
        if(preset == null) return;

        GuiHelper.drawTexture(guiDrawing, preset.getThumbnail(), startX+0.5, startY+offset+0.5, width-1, height-offset-4);

        if(!backgroundOnly) {
            double perRow = height / 8.5;
            double rowHeight = Math.max(0.5, height / 24.0);
            for(int i = 0; i < 4; i++) {
                if(preset.isRowHidden(i)) continue;
                double curY = startY + offset + ((i+1) * perRow);
                GuiHelper.drawRectangle(guiDrawing, startX+1.5, curY, width * 0.55, rowHeight, preset.getTextColor());
                GuiHelper.drawRectangle(guiDrawing, startX + (width * 0.65), curY, rowHeight, rowHeight, preset.getTextColor());
                GuiHelper.drawRectangle(guiDrawing, startX + (width * 0.75), curY, (width * 0.2)-0.5, rowHeight, preset.getTextColor());
            }
        }
    }

    private void choose(String id) {
        callback.accept(id);
        onClose2();
    }

    @Override
    public boolean isPauseScreen2() {
        return false;
    }
}
