package com.taewon.mygallag.items;


import android.content.Context;

import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.sprites.Sprite;

import java.util.Timer;
import java.util.TimerTask;

public class SpeedItemSprite extends Sprite {

    SpaceInvadersView game;

    public SpeedItemSprite(Context context, SpaceInvadersView game,
                           int x, int y, int dx, int dy){
        super(context, R.drawable.speed_item, x, y);
        this.game = game;
        this.dx = dx;
        this.dy = dy;
        // 15초 후에 스프라이트를 자동으로 제거하기 위해 TimerTask를 예약
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                autoRemove();
            }
        }, 15000);
    }
    private void autoRemove() {
        game.removeSprite(this);
    }

    public void move() {
        // 스프라이트가 게임 화면 경계에 도달했는지 확인
        if ((dx < 0) && (x < 120)) {
            dx *= -1; // 스프라이트가 왼쪽으로 이동하고 왼쪽 경계에 도달한 경우, 수평 방향을 반대로 변경
            return;
        }
        if ((dx > 0) && (x > game.screenW - 120)) {
            dx *= -1; // 스프라이트가 오른쪽으로 이동하고 오른쪽 경계에 도달한 경우, 수평 방향을 반대로 변경
            return;
        }
        if ((dy < 0) && (y < 120)) {
            dy *= -1; // 스프라이트가 위로 이동하고 위쪽 경계에 도달한 경우, 수직 방향을 반대로 변경
            return;
        }
        if ((dy > 0) && (y > game.screenH - 120)) {
            dy *= -1; // 스프라이트가 아래로 이동하고 아래쪽 경계에 도달한 경우, 수직 방향을 반대로 변경
            return;
        }
        super.move(); // 현재 방향과 속도에 따라 스프라이트를 이동
    }
}
