package com.george.bookopen.book;

/**
 * Created By George
 * Description:
 */
public class BookOpenViewValue {
    private int left;
    private int top;
    private int right;
    private int bottom;
    private float x;
    private float y;
    private float sX;
    private float sY;
    private float PivotX;
    private float PivotY;

    public BookOpenViewValue(){}

    public BookOpenViewValue(int left, int top, int right, int bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void setPivotX(float pivotX) {
        PivotX = pivotX;
    }

    public void setPivotY(float pivotY) {
        PivotY = pivotY;
    }

    public float getPivotX() {
        return PivotX;
    }

    public float getPivotY() {
        return PivotY;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getsX() {
        return sX;
    }

    public void setsX(float sX) {
        this.sX = sX;
    }

    public float getsY() {
        return sY;
    }

    public void setsY(float sY) {
        this.sY = sY;
    }

    @Override
    public String toString() {
        return "BookOpenViewValue{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", x=" + x +
                ", y=" + y +
                ", sX=" + sX +
                ", sY=" + sY +
                '}';
    }
}
