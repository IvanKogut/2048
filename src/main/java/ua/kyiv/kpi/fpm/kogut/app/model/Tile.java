package ua.kyiv.kpi.fpm.kogut.app.model;

import lombok.Data;

import java.awt.*;
import java.io.Serializable;

@Data
public class Tile implements Drawable, Serializable {

    private static final long serialVersionUID = 2438845260542108250L;

    private int value;

    boolean isEmpty() {
        return value == 0;
    }

    @Override
    public void draw(Graphics g, int leftUpperCornerX, int leftUpperCornerY) {
        g.setColor(getTileColor());

        g.drawRect(leftUpperCornerX, leftUpperCornerY, Model.TILE_LENGTH, Model.TILE_LENGTH);
        g.fillRect(leftUpperCornerX, leftUpperCornerY, Model.TILE_LENGTH, Model.TILE_LENGTH);

        g.setColor(getFontColor());

        final String value = isEmpty() ? "" : String.valueOf(this.value);

        g.drawString(value, leftUpperCornerX + Model.TILE_LENGTH / 2, leftUpperCornerY + Model.TILE_LENGTH / 2);
    }

    private Color getFontColor() {
        return new Color(value < 16 ? 0x776e65 : 0xf9f6f2);
    }

    private Color getTileColor() {
        final int rgb;

        switch (value) {
            case 0:
                rgb = 0xcdc1b4;
                break;
            case 2:
                rgb = 0xeee4da;
                break;
            case 4:
                rgb = 0xede0c8;
                break;
            case 8:
                rgb = 0xf2b179;
                break;
            case 16:
                rgb = 0xf59563;
                break;
            case 32:
                rgb = 0xf67c5f;
                break;
            case 64:
                rgb = 0xf65e3b;
                break;
            case 128:
                rgb = 0xedcf72;
                break;
            case 256:
                rgb = 0xedcc61;
                break;
            case 512:
                rgb = 0xedc850;
                break;
            case 1024:
                rgb = 0xedc53f;
                break;
            case 2048:
                rgb = 0xedc22e;
                break;
            default:
                rgb = 0xff0000;
                break;
        }

        return new Color(rgb);
    }
}