package com.lx862.jcm.mod.data.pids.preset.components;

import com.google.gson.JsonObject;
import com.lx862.jcm.mod.data.KVPair;
import com.lx862.jcm.mod.data.pids.preset.PIDSContext;
import com.lx862.jcm.mod.data.pids.preset.components.base.PIDSComponent;
import com.lx862.jcm.mod.data.pids.preset.components.base.TextureComponent;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.GuiDrawing;

public class CustomTextureComponent extends TextureComponent {
    protected final Identifier texture;
    private final int color;
    public CustomTextureComponent(double x, double y, double width, double height, KVPair additionalParam) {
        super(x, y, width, height, additionalParam);
        this.color = additionalParam.getColor("tint", 0xFFFFFF);
        this.texture = Identifier.tryParse(additionalParam.get("textureId", ""));
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, GuiDrawing guiDrawing, Direction facing, PIDSContext context) {
        drawTexture(graphicsHolder, guiDrawing, facing, texture, 0, 0, width, height, color + ARGB_BLACK);
    }

    public static PIDSComponent parseComponent(double x, double y, double width, double height, JsonObject jsonObject) {
        return new CustomTextureComponent(x, y, width, height, new KVPair(jsonObject));
    }
}
