package org.zhbot.zmi_overlays.overlays;

import lombok.Getter;
import lombok.Setter;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

import java.awt.*;
import java.util.Map;

public class RuneGridComponent implements LayoutableRenderableEntity {
    public static final int ICONS_PER_ROW = 4;
    public static final int ICON_SIZE = 36;

    public static final int PADDING_X = 4;
    public static final int PADDING_Y = 2;

    @Getter
    @Setter
    private Point preferredLocation = new Point();

    @Getter
    @Setter
    private Dimension preferredSize = new Dimension();

    @Getter
    private Rectangle bounds = new Rectangle();

    private final ItemManager itemManager;
    private final Map<Integer, Integer> runes;

    public RuneGridComponent(ItemManager itemManager, Map<Integer, Integer> runes)
    {
        this.itemManager = itemManager;
        this.runes = runes;

        var totalItems = runes.size();
        var columns = Math.min(totalItems, ICONS_PER_ROW);
        var rows = (int)Math.ceil((double)totalItems / ICONS_PER_ROW);

        var totalWidth = columns * ICON_SIZE + PADDING_X * 2;
        var totalHeight = rows * ICON_SIZE + PADDING_Y * 2;

        this.setPreferredSize(new Dimension(totalWidth, totalHeight));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (runes.isEmpty())
        {
            bounds = new Rectangle(preferredLocation, new Dimension(0, 0));
            return new Dimension(0, 0);
        }

        var totalItems = runes.size();
        var columns = Math.min(totalItems, ICONS_PER_ROW);
        var rows = (int)Math.ceil((double)totalItems / ICONS_PER_ROW);

        var totalWidth = columns * ICON_SIZE;
        var totalHeight = rows * ICON_SIZE;

        var x = 0;
        var y = 0;

        var composite = graphics.getComposite();
        for (var entry : runes.entrySet())
        {
            var itemId = entry.getKey();
            var amount = entry.getValue();

            var showQuantity = amount > 0;
            var img = itemManager.getImage(itemId, amount, showQuantity);

            if (showQuantity)
                graphics.setComposite(composite);
            else
                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));

            graphics.drawImage(img, preferredLocation.x + x + PADDING_X, preferredLocation.y + y + PADDING_Y, null);

            x += ICON_SIZE;

            if (x >= ICON_SIZE * ICONS_PER_ROW)
            {
                x = 0;
                y += ICON_SIZE;
            }
        }
        graphics.setComposite(composite);

        Dimension renderDimension = new Dimension(totalWidth, totalHeight);
        bounds = new Rectangle(preferredLocation, renderDimension);
        return renderDimension;
    }
}
