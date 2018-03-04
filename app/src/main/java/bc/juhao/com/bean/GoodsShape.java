package bc.juhao.com.bean;

import java.io.Serializable;

/**
 * @author: Jun
 * @date : 2017/6/13 17:38
 * @description :
 */
public class GoodsShape implements Serializable {
    private int height;
    private int width;
    private int y;
    private int x;
    private float Rotate;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getRotate() {
        return Rotate;
    }

    public void setRotate(float rotate) {
        Rotate = rotate;
    }

    @Override
    public String toString() {
        return "GoodsShape{" +
                "height=" + height +
                ", width=" + width +
                ", y=" + y +
                ", x=" + x +
                ", Rotate=" + Rotate +
                '}';
    }
}
