package com.taewon.mygallag.sprites;


import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.taewon.mygallag.MainActivity;
import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.items.HealitemSprite;
import com.taewon.mygallag.items.PowerItemSprite;
import com.taewon.mygallag.items.SpeedItemSprite;

import java.util.ArrayList;

public class StarshipSprite extends Sprite {
    Context context;
    SpaceInvadersView game;
    public float speed;
    private int bullets, life = 3, powerLevel;
    private int specialShotCount;
    private boolean isSpecialShooting;
    private static ArrayList<Integer> bulletSprites = new ArrayList<>();
    public static final float MAX_SPEED = 3.5f;
    public static int MAX_HEART = 3;
    private RectF rectF;
    private boolean isReloading = false;

    public StarshipSprite(Context context, SpaceInvadersView game, int resId, int x, int y, float speed) {
        super(context, resId, x, y);
        this.context = context;
        this.game = game;
        this.speed = speed;
        init();
    }

    public void init() {
        dx = dy = 0;  // 스타쉽의 초기 속도도 설정
        bullets = 30; // 스타쉽의 초기 탄환 수를 설정
        life = 3; // 스타쉽의 초기 생명 수를 설정
        specialShotCount = 3; // 스타쉽의 특수 발사 횟수를 설정
        powerLevel = 0; // 스타쉽의 초기 파워 레벨을 설정
        Integer[] shots = {R.drawable.shot_001, R.drawable.shot_002, R.drawable.shot_003,
                R.drawable.shot_004, R.drawable.shot_005, R.drawable.shot_006, R.drawable.shot_007};

        for (int i = 0; i < shots.length; i++) {
            bulletSprites.add(shots[i]); // 각 리소스 ID를 bulletSprites 리스트에 추가하여 사용할 수 있도록 준비
        }
    }


    public void move() {
        //벽에 부딫히면 못 가게 하기
        if ((dx < 0) && (x < 120)) return;// 왼쪽 벽에 부딪혔을 경우
        if ((dx > 0) && (x > game.screenW - 120)) return; // 오른쪽 벽에 부딪혔을 경우
        if ((dy < 0) && (y < 120)) return; // 위쪽 벽에 부딪혔을 경우
        if ((dy > 0) && (y > game.screenH - 120)) return; // 아래쪽 벽에 부딪혔을 경우
        super.move(); // 벽에 부딪히지 않았을 경우 이동

    }

    public int getBulletsCount() {
        return bullets;
    }

    public void moveRight(double force) {
        setDx((float) (1 * force * speed));
    }

    public void moveLeft(double force) {
        setDx((float) (-1 * force * speed));
    }

    public void moveDown(double force) {
        setDy((float) (1 * force * speed));
    }

    public void moveUp(double force) {
        setDy((float) (-1 * force * speed));
    }

    public void resetDx() {
        setDx(0);
    }

    public void resetDy() {
        setDy(0);
    }

    public void plusSpeed(float speed) {
        this.speed += speed;
    }

    public void fire() {
        if (isReloading | isSpecialShooting) {return;}
        MainActivity.effectSound(MainActivity.PLAYER_SHOT);
        //ShotSprite 생성자 구현
        ShotSprite shot = new ShotSprite(context, game, bulletSprites.get(powerLevel), // ShotSprite의 생성자 매개변수로 사용할 bulletSprites 리스트에서 현재 파워 레벨에 해당하는 리소스 ID를 가져옴
                getX() + 10, getY() - 30, -16);

        game.getSprites().add(shot); // 생성된 ShotSprite를 게임의 스프라이트 리스트에 추가
        bullets--; // 탄환 수를 감소


        MainActivity.bulletCount.setText(bullets + "/30"); // 탄환 수를 화면에 표시하는 TextView를 업데이트
        Log.d("bullets", bullets + "/30");
        if (bullets == 0) { // 탄환 수가 0이 되면 재장전 메서드를 호출하고 메서드를 종료
            reloadBullets();
            return;
        }
    }

    public void powerUp(){ // 파워 레벨이 최대 레벨에 도달한 경우, 점수를 증가시키고 메서드를 종료
        if(powerLevel >= bulletSprites.size() -1){
            game.setScore(game.getScore() +1);
            MainActivity.scoreTv.setText(Integer.toString(game.getScore()));
            return;
        }
        powerLevel++; // 파워 레벨을 증가
        MainActivity.fireBtn.setImageResource(bulletSprites.get(powerLevel)); // 파워 레벨에 해당하는 발사 이미지를 Fire 버튼의 이미지로 설정
        MainActivity.fireBtn.setBackgroundResource(R.drawable.round_button_shape);
    }
    public void reloadBullets(){
        isReloading = true;
        MainActivity.effectSound(MainActivity.PLAYER_RELOAD);
        MainActivity.fireBtn.setEnabled(false); // Fire 버튼과 Reload 버튼을 비활성화
        MainActivity.reloadBtn.setEnabled(false);
        //Thread sleep 사용하지 않고 지연시키는 클래스
        new Handler().postDelayed(new Runnable() { // 2초 지연 후에 실행되는 코드를 설정
            @Override
            public void run() {
            bullets=30; // 탄환 수를 30으로 재설정
            MainActivity.fireBtn.setEnabled(true);  // Fire 버튼과 Reload 버튼을 다시 활성화
            MainActivity.reloadBtn.setEnabled(true);
            MainActivity.bulletCount.setText(bullets + "/30");  // 탄환 수를 화면에 표시하는 TextView를 업데이트
            MainActivity.bulletCount.invalidate(); // 화면을 새로고침
            isReloading = false; // 재장전 중임을 표시 해제
            }
        },2000); // 2초의 지연 시간을 설정합니다.
    }

    public void specialShot(){
        specialShotCount--; // 특수 발사 카운트를 감소
        //SpecialshotSprite 구현
        SpecialshotSprite shot = new SpecialshotSprite(context, game, R.drawable.laser,
                getRect().right - getRect().left, 0);

        //game -> SpaceInvadersView의 getSprites() : sprite에 shot 추가하기
        game.getSprites().add(shot);
    }

    public int getSpecialShotCount(){return specialShotCount;}  // 특수 발사 카운트를 반환
    public boolean isSpecialShooting(){return isSpecialShooting;} // 특수 발사 중인지 여부를 반환

    public void setSpecialShooting(boolean specialShooting) {
        isSpecialShooting= specialShooting;}
    public int getLife() {return life;} //스타쉽의 생명 수를 반환

    public void hurt() {
        life--; // 생명 수를 감소
        if(life<=0) { // 생명 수가 0 이하인 경우, 생명을 표시하는 ImageView의 이미지를 변경하고 게임을 종료
            ((ImageView) MainActivity.lifeFrame.getChildAt(life)).setImageResource(
                    (R.drawable.ic_baseline_favorite_border_24));
            // SpaceInvadersView의 endGame() 에서 game종료시키기
            game.endGame();
            return;
        }
            Log.d("hurt", Integer.toString(life)); //생명확인하기
            ((ImageView) MainActivity.lifeFrame.getChildAt(life)).setImageResource(
                    R.drawable.ic_baseline_favorite_border_24);
        }

        //생명 얻었을때
        public void heal(){
        Log.d("heal", Integer.toString(life));
        if(life + 1 > MAX_HEART){ // 생명 수가 최대 생명 수(MAX_HEART)를 초과하는 경우, 점수를 증가시키고 메서드를 종료
            game.setScore(game.getScore() +1);
            MainActivity.scoreTv.setText(Integer.toString(game.getScore()));
            return;
        }
            ((ImageView) MainActivity.lifeFrame.getChildAt(life)).setImageResource(
                    R.drawable.ic_baseline_favorite_24);
        life++; // 생명 수를 증가
    }
    //속도 올리기
    private void speedUp() { // 최대 속도(MAX_SPEED)가 현재 속도(speed + 0.2f)보다 크거나 같은 경우, 속도를 0.2만큼 증가
        if(MAX_SPEED >= speed + 0.2f) plusSpeed(0.2f);
        else { // 최대 속도를 초과하는 경우, 점수를 증가시키고 MainActivity의 scoreTv에 점수를 표시
            game.setScore(game.getScore() + 1);
            MainActivity.scoreTv.setText(Integer.toString((game.getScore())));
        }
    }
    //Sprite 의 handleCollision() -> 충돌처리
    public void handleCollision(Sprite other) {
        if(other instanceof AlienSprite){
            //Alien 아이템이면
            game.removeSprite(other);
            MainActivity.effectSound((MainActivity.PLAYER_HURT));
            hurt();
        }
        if(other instanceof SpeedItemSprite){
            //스피드 아이템이면
            game.removeSprite(other);
            MainActivity.effectSound((MainActivity.PLAYER_GET_ITEM));
            speedUp();
        }
        if(other instanceof AlienShotSprite){
            //총알 맞으면
            MainActivity.effectSound((MainActivity.PLAYER_HURT));
            game.removeSprite(other);
            hurt();
        }
        if(other instanceof PowerItemSprite){
            // 아이템을 맞으면
            MainActivity.effectSound((MainActivity.PLAYER_GET_ITEM));
            powerUp();
            game.removeSprite(other);
        }
        if(other instanceof HealitemSprite){
            //생명  아이템이면
            MainActivity.effectSound((MainActivity.PLAYER_GET_ITEM));
            game.removeSprite(other);
            heal();
        }
    }
    public int getPowerLevel(){return powerLevel; }
}


