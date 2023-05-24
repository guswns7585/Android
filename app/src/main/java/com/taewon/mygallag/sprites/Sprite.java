package com.taewon.mygallag.sprites;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Sprite {

    protected  float x, y;  // 스프라이트의 x, y 좌표
    protected int width, height;  // 스프라이트의 너비와 높이
    protected float dx, dy;  // 스프라이트의 x, y 방향 속도
    private Bitmap bitmap;  // 스프라이트의 비트맵 이미지
    protected int id;  // 스프라이트의 고유 식별자
    private RectF rect;  // 스프라이트의 사각형 영역

    public Sprite(Context context, int resourceId, float x, float y){
        this.id = resourceId;
        this.x = x;
        this.y = y;
        bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        rect = new RectF();
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void draw(Canvas canvas, Paint paint) {canvas.drawBitmap(bitmap, x, y, paint);}  // 주어진 캔버스에 스프라이트의 비트맵 이미지를 그려줌

    public void move(){ //스프라이트를 이동
        x=x+dx; y=y+dy;
        rect.left=x; rect.right = x+width;
        rect.top = y; rect.bottom = y + height;
    } //x,y 좌표 이동후  rect의 영역도 업데이트
    public float getX() { return x;}
    public float getY() { return y;}
    public float getDx() {return dx;}
    public float getDy() {return dy;}

    public void setDx(float dx) {this.dx = dx;}

    public void setDy(float dy) {this.dy = dy;}
    public RectF getRect() {return rect;}
    public boolean checkCollision(Sprite other) {  // 다른 스프라이트와의 충돌 여부를 확인
        return  RectF.intersects(this.getRect(), other.getRect());
        // 현재 스프라이트의 사각형 영역과 다른 스프라이트의 사각형 영역이 교차하는지 확인하여 교차하면 true를 반환
    }
    public void handleCollision(Sprite other){}//충돌처리

    public Bitmap getBitmap() {return bitmap;}

    public void setBitmap(Bitmap bitmap) {this.bitmap = bitmap;}
}
