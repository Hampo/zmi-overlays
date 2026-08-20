package org.zhbot.zmi_overlays.overlays;

import lombok.Getter;
import lombok.Setter;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.TextComponent;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Map;

public class RuneGridComponent implements LayoutableRenderableEntity {
    public static final int ICONS_PER_ROW = 4;
    public static final int ICON_SIZE = 36;

    public static final int PADDING_X = 4;
    public static final int PADDING_Y = 2;

    private static final AlphaComposite DISABLED_COMPOSITE = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);

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

        this.setPreferredSize(getGridSize());
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        var renderDimension = getGridSize();

        if (runes.isEmpty())
        {
            bounds = new Rectangle(preferredLocation, renderDimension);
            return renderDimension;
        }

        var x = 0;
        var y = 0;

        var composite = graphics.getComposite();
        for (var entry : runes.entrySet())
        {
            var itemId = entry.getKey();
            var amount = entry.getValue();

            var showQuantity = amount > 0;
            var img = itemManager.getImage(itemId, amount, showQuantity);

            if (!showQuantity)
                graphics.setComposite(DISABLED_COMPOSITE);

            graphics.drawImage(img, preferredLocation.x + x + PADDING_X, preferredLocation.y + y + PADDING_Y, null);

            if (!showQuantity)
                graphics.setComposite(composite);

            x += ICON_SIZE;

            if (x >= ICON_SIZE * ICONS_PER_ROW)
            {
                x = 0;
                y += ICON_SIZE;
            }
        }

        var total = runes.values().stream().mapToInt(Integer::intValue).sum();

        var totalX = preferredLocation.x + x + PADDING_X;
        var totalY = preferredLocation.y + y + PADDING_Y;

        var text = new TextComponent();
        text.setColor(Color.WHITE);

        text.setText("Total:");
        text.setPosition(totalX, totalY + 16);
        text.render(graphics);

        text.setText(NumberFormat.getNumberInstance().format(total));
        text.setPosition(totalX, totalY + 29);
        text.render(graphics);

        bounds = new Rectangle(preferredLocation, renderDimension);
        return renderDimension;
    }

    private Dimension getGridSize()
    {
        var totalItems = runes.size();

        if (totalItems == 0)
            return new Dimension();

        var columns = Math.min(totalItems, ICONS_PER_ROW);
        var rows = (totalItems + ICONS_PER_ROW - 1) / ICONS_PER_ROW;

        return new Dimension(
                columns * ICON_SIZE,
                rows * ICON_SIZE
        );
    }
}
