package com.taewon.mygallag.sprites;

import android.content.Context;

import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;

public class AlienShotSprite extends Sprite {
    private Context context; // Context 객체를 저장하는 변수
    private SpaceInvadersView game; // SpaceInvadersView 객체를 저장하는 변수

    public AlienShotSprite(Context context, SpaceInvadersView game,
                           float x, float y, int dy) {
        super(context, R.drawable.shot_001, x, y); // Sprite 클래스의 생성자 호출하여 스프라이트 초기화
        this.game = game; // SpaceInvadersView 객체 저장
        this.context = context; // Context 객체 저장
        setDy(dy); // 수직 이동 속도 설정
    }
}
