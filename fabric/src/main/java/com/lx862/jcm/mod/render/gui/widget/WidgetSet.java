package com.lx862.jcm.mod.render.gui.widget;

import com.lx862.jcm.mod.render.RenderHelper;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.ClickableWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a set of widget within a defined area.
 * Able to tile the widgets horizontally and add new rows like a grid/table.
 */
public class WidgetSet extends ClickableWidgetExtension implements WidgetsWrapper, RenderHelper {
    private final List<List<MappedWidget>> widgetRows = new ArrayList<>();
    private final int maxWidgetHeight;
    public final int widgetXMargin;

    public WidgetSet(int maxWidgetHeight, int widgetXMargin) {
        super(0, 0, 0, 0);
        this.maxWidgetHeight = maxWidgetHeight;
        this.widgetXMargin = widgetXMargin;
    }
    public WidgetSet(int maxWidgetHeight) {
        this(maxWidgetHeight, 10);
    }

    /**
     * Set the position and size of the widget and reposition all added widget.
     * Make sure to call this after all widget has been added.
     */
    public void setXYSize(int x, int y, int width, int height) {
        setX2(x);
        setY2(y);
        setWidth2(width);
        setHeightMapped(height);
        positionWidgets();
    }

    public void addWidget(ButtonWidgetExtension widget) {
        addWidget(new MappedWidget(widget));
    }

    public void addWidget(MappedWidget widget) {
        if(widgetRows.isEmpty()) {
            insertRow();
        }

        int lastRowIndex = widgetRows.size() - 1;
        List<MappedWidget> lastRow = widgetRows.get(lastRowIndex);
        lastRow.add(widget);
        widgetRows.set(lastRowIndex, lastRow);
    }

    public void insertRow() {
        widgetRows.add(new ArrayList<>());
    }

    public void reset() {
        this.widgetRows.clear();
    }

    private void positionWidgets() {
        int x = getX2();
        int y = getY2();
        int width = getWidth2() + widgetXMargin;
        int widgetHeight = Math.min(maxWidgetHeight, (int)(maxWidgetHeight * ((double)getHeight2() / (widgetRows.size() * maxWidgetHeight))));

        for(int i = 0; i < widgetRows.size(); i++) {
            List<MappedWidget> rowWidgets = widgetRows.get(i);

            double perWidgetWidth = ((double)width / rowWidgets.size()) - widgetXMargin;
            int rowY = y + (i * widgetHeight);

            for (int j = 0; j < rowWidgets.size(); j++) {
                MappedWidget widget = new MappedWidget(rowWidgets.get(j));
                double widgetStartX = x + (j * perWidgetWidth) + (j * widgetXMargin);

                widget.setX((int)Math.round(widgetStartX));
                widget.setY(rowY);
                widget.setWidth((int)Math.round(perWidgetWidth));
            }
        }
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        for(List<MappedWidget> row : widgetRows) {
            for(MappedWidget widget : row) {
                widget.render(graphicsHolder, mouseX, mouseY, delta);
            }
        }
    }

    @Override
    public void setAllX(int newX) {
        super.setX2(newX);
        positionWidgets();
    }

    @Override
    public void setAllY(int newY) {
        super.setY2(newY);
        positionWidgets();
    }

    @Override
    public void setVisibleMapped(boolean visible) {
        for(List<MappedWidget> row : widgetRows) {
            for(MappedWidget widget : row) {
                widget.setVisible(visible);
            }
        }
        super.setVisibleMapped(visible);
    }
}
