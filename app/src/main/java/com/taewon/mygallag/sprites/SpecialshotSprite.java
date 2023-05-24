package com.taewon.mygallag.sprites;


import android.content.Context;

import com.taewon.mygallag.SpaceInvadersView;

import java.util.Timer;
import java.util.TimerTask;

public class SpecialshotSprite extends Sprite {
    private SpaceInvadersView game;

    public SpecialshotSprite(Context context, SpaceInvadersView game,
                             int resId, float x, float y) {
        super(context, resId, x, y);
        this.game = game;
        game.getPlayer().setSpecialShooting(true);

        // 5초 후에 자동으로 제거되도록 타이머 스케줄링
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                autoRemove();
            }
        }, 5000);
    }


    //  스프라이트를 이동시킵니다. 플레이어의 위치에 따라 이동
    public void move() {
        super.move();
        this.x = game.getPlayer().getX() - getWidth() + 240;
        this.y = game.getPlayer().getY() - getHeight();
    }


    // 스프라이트를 자동으로 제거합니다. 특수 발사 상태를 해제하고 게임에서 스프라이트를 제거
    public void autoRemove() {
        game.getPlayer().setSpecialShooting(false);
        game.removeSprite(this);
    }
}
