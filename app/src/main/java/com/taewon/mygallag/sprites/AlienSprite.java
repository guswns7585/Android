package com.taewon.mygallag.sprites;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.taewon.mygallag.MainActivity;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.items.HealitemSprite;
import com.taewon.mygallag.items.PowerItemSprite;
import com.taewon.mygallag.items.SpeedItemSprite;

import java.util.ArrayList;
import java.util.Random;

public class AlienSprite extends Sprite {
    private Context context;
    private SpaceInvadersView game;
    ArrayList<AlienShotSprite> alienShotSprites;
    Handler fireHandler = null;
    boolean isDestroyed = false;

    public AlienSprite(Context context, SpaceInvadersView game, int resId, int x, int y) {
        super(context, resId, x, y); // Sprite 클래스의 생성자 호출하여 외계인 스프라이트 초기화
        this.context = context;
        this.game = game;
        alienShotSprites = new ArrayList<>(); // 외계인의 탄환을 저장하는 ArrayList 생성
        Random r = new Random();
        int randomDx = r.nextInt(5); // 랜덤한 수평 속도 생성
        int randomDy = r.nextInt(5); // 랜덤한 수직 속도 생성
        if (randomDy <= 0) dy = 1; // 수직 속도가 0 이하인 경우 1로 설정
        dx = randomDx; // 수평 속도 설정
        dy = randomDy; // 수직 속도 설정
        fireHandler = new Handler(Looper.getMainLooper()); // 메인 스레드에서 동작하는 Handler 생성
        fireHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("run", "동작");
                Random r = new Random();
                boolean isFire = r.nextInt(100) + 1 <= 30; // 일정 확률로 탄환 발사 결정
                if (isFire && !isDestroyed) {
                    fire(); // 탄환 발사
                    fireHandler.postDelayed(this, 1000); // 일정 시간 후에 다시 실행
                }
            }
        }, 1000); // 1초 후에 실행
    } // 1초에 한번씩 30%의 확률로 탄환 발사

    public void move() {
        super.move(); // 스프라이트 이동
        if (((dx < 0) && (x < 100)) || ((dx > 0) && (x > 800))) { // 외계인이 화면 경계에 도달한 경우
            dx = -dx; // 수평 속도 반전
            if (y > game.screenH) {
                game.removeSprite(this); // 외계인이 화면 아래로 벗어나면 제거
            }
        }
    }

    public void handleCollision(Sprite other) {
        if (other instanceof ShotSprite) { // 만약 충돌한 스프라이트가 ShotSprite인 경우
            game.removeSprite(other); // 충돌한 ShotSprite 제거
            game.removeSprite(this); // 현재 외계인 스프라이트 제거
            destroyAlien(); // 외계인 파괴 처리
            return;
        }
        if (other instanceof SpecialshotSprite) { // 만약 충돌한 스프라이트가 SpecialshotSprite인 경우
            game.removeSprite(this); // 현재 외계인 스프라이트 제거
            destroyAlien(); // 외계인 파괴 처리
            return;
        }
    }

    private void destroyAlien() {
        isDestroyed = true; // 외계인이 파괴되었음을 표시
        game.setCurrEnemyCount(game.getCurrEnemyCount() - 1); // 현재 적의 수 감소
        //외계인의 수에 따라 외계인을 추가 생성하므로 외계인의 파괴를 알림
        for (int i = 0; i < alienShotSprites.size(); i++) {
            game.removeSprite(alienShotSprites.get(i)); // 외계인 총알 스프라이트 제거
        }

        spawnHealItem(); // 힐 아이템 스프라이트 생성
        spawnPowerItem(); // 파워 아이템 스프라이트 생성
        spawnSpeedItem(); // 스피드 아이템 스프라이트 생성

        game.setScore(game.getScore() + 1); // 점수 증가
        MainActivity.scoreTv.setText(Integer.toString(game.getScore())); // MainActivity의 점수 텍스트뷰 업데이트
    }


    private void fire() {
        // 외계인 총알 스프라이트를 생성
        AlienShotSprite alienShotSprite = new AlienShotSprite(context, game, getX(), getY() + 30, 16);
        //외계인 총알 스프라이트를 생성 위치는 현재 외계인의 위치에서 일정한 값(Y축으로 30만큼 아래로)으로 설정되며, 속도는 16로 설정
        alienShotSprites.add(alienShotSprite); // 외계인 총알 스프라이트를 alienShotSprites 리스트에 추가
        game.getSprites().add(alienShotSprite); // 외계인 총알 스프라이트를 게임의 스프라이트 리스트에 추가
    }


    private void spawnSpeedItem() {
        Random r = new Random();
        int speedItemDrop = r.nextInt(100) + 1; // 1부터 100까지의 랜덤한 수를 생성
        if (speedItemDrop <= 5) { // 5%의 확률로 실행
            int dx = r.nextInt(10) + 1; // 1부터 10까지의 랜덤한 수를 생성하여 dx에 저장
            int dy = r.nextInt(10) + 5; // 5부터 14까지의 랜덤한 수를 생성하여 dy에 저장
            game.getSprites().add(new SpeedItemSprite(context, game,
                    (int) this.getX(), (int) this.getY(), dx, dy)); // 스피드 아이템 스프라이트를 생성하여 게임의 스프라이트 리스트에 추가
        }
    }


    private void spawnPowerItem() {
        Random r = new Random();
        int powerItemDrop = r.nextInt(100) + 1;// 1부터 100까지의 랜덤한 수를 생성
        if (powerItemDrop <= 3) { // 3%의 확률로 실행
            int dx = r.nextInt(10) + 1;  // 1부터 10까지의 랜덤한 수를 생성하여 dx에 저장
            int dy = r.nextInt(10) + 10;  // 10부터 19까지의 랜덤한 수를 생성하여 dy에 저장
            game.getSprites().add(new PowerItemSprite(context, game,
                    (int) this.getX(), (int) this.getY(), dx, dy)); // 파워 아이템 스프라이트를 생성하여 게임의 스프라이트 리스트에 추가
        }
    }

    private void spawnHealItem() {
        Random r = new Random();
        int healItemDrop = r.nextInt(100) + 1; // 1부터 100까지의 랜덤한 수를 생성
        if (healItemDrop <= 1) { // 1%의 확률로 실행
            int dx = r.nextInt(10) + 1; // 1부터 10까지의 랜덤한 수를 생성하여 dx에 저장
            int dy = r.nextInt(10) + 10; // 10부터 19까지의 랜덤한 수를 생성하여 dy에 저장
            game.getSprites().add(new HealitemSprite(context, game,
                    (int) this.getX(), (int) this.getY(), dx, dy)); // 힐 아이템 스프라이트를 생성하여 게임의 스프라이트 리스트에 추가
        }
    }
}