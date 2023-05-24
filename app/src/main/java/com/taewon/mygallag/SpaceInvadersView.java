package com.taewon.mygallag;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.taewon.mygallag.sprites.AlienSprite;
import com.taewon.mygallag.sprites.Sprite;
import com.taewon.mygallag.sprites.StarshipSprite;

import java.util.ArrayList;
import java.util.Random;

public class SpaceInvadersView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    // SurfaceView 는 스레드를 이용해 강제로 화면에 그려주므로 View보다 빠르다. 애니메이션, 영상 처리에 이용
    // SurfaceHolder.Callback Surface 의 변화 감지를 위해 필요. 지금처럼 SurfaceView 와 거의 같이 사용

    private static int MAX_ENEMY_COUNT = 10;
    private Context context;
    private int characterId;
    private SurfaceHolder ourHolder; // 화면에 그리는데 View 보다 빠르게 그려준다
    private Paint paint;// canvas위에 paint로 좌표 잡아서 그림
    public int screenW, screenH;
    private Rect src, dst; // 사각형 그리는 클래스
    private ArrayList sprites = new ArrayList();
    private Sprite starship;
    private int score, currEnemyCount;
    private Thread gameThread = null;
    private volatile boolean running; // 휘발성 부울 함수
    private Canvas canvas;
    int mapBitmapY = 0;

    public SpaceInvadersView(Context context, int characterId, int x, int y){ //MainActivity, Intent(침략자), point x,y가 넘어온다
        super(context);
        this.context = context;
        this.characterId = characterId;
        ourHolder = getHolder(); // 현재 SurfaceView 를 리턴 받는다.
        paint = new Paint();
        screenW = x;
        screenH = y; // 받아온 x, y
        src = new Rect(); // 원본 사각형
        dst = new Rect(); // 사본 사각형
        dst.set(0, 0, screenW, screenH); // 시작 x, y 와 끝 x, y
        startGame();  //객체가 생성될 때 startGame() 메서드가 호출되어 게임 초기화가 수행
    }

    private void startGame(){
        sprites.clear(); // ArrayList 지우기
        initSprites(); // ArrayList 에 외계인 아이템들 추가하기
        score = 0;
    }

    public void endGame(){
        Log.e("GameOver", "GameOver"); // 게임 종료 로그를 출력
        Intent intent = new Intent(context, ResultActivity.class);  // ResultActivity로 이동하는 인텐트를 생성
        intent.putExtra("score", score);// "score"라는 키로 점수(score)를 인텐트에 추가
        context.startActivity(intent); // 인텐트를 실행하여 ResultActivity로 이동
        gameThread.stop(); // 게임 스레드를 중지
    }  //게임 스레드가 중지만 됐기 때문에 액티비티는 남아있다

    public void removeSprite(Sprite sprite) { //스프라이트 제거
        sprites.remove(sprite);
    }

    private void initSprites(){ // sprite 초기화   외계인(AlienSprite)과 우주선(StarshipSprite) 객체를 생성하고, 이들을 sprites 리스트에 추가합니다.
        // StarshipSprite 생성 아이템들 생성
        starship = new StarshipSprite(context, this, characterId, screenW / 2, screenH - 400, 1.5f);
        sprites.add(starship); // ArrayList 에 추가
        spawnEnemy(); //외계인 생성
        spawnEnemy();
    }

    public void spawnEnemy(){ //무작위 위치에 외계인을 추가
        Random r = new Random();
        int x = r.nextInt(300) + 100; // x 좌표를 100부터 400 사이의 랜덤 값으로 설정
        int y = r.nextInt(300) + 100; // y 좌표를 100부터 400 사이의 랜덤 값으로 설정
        // 외계인 아이템
        Sprite alien = new AlienSprite(context, this, R.drawable.ship_0002, 100 + x, 100 + y);
        sprites.add(alien); // 생성한 외계인을 sprites ArrayList에 추가
        currEnemyCount++; // 외계인 아이템 개수 증가
    }

    public ArrayList getSprites(){
        return sprites;
    }

    public void resume(){ // 사용자가 만드는 resume() 함수   게임 스레드가 시작되고, 메인 게임 루프인 run() 메서드가 실행
        running = true; // 게임 실행 상태를 true로 설정
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Sprite 를 StarshipSprite 로 형변환하여 리턴하기
    public StarshipSprite getPlayer(){
        return (StarshipSprite) starship;
    }

    public int getScore(){
        return  score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setCurrEnemyCount(int currEnemyCount){ //현재 적의 수를 설정
        this.currEnemyCount = currEnemyCount;
    }

    public int getCurrEnemyCount(){//현재 적의 수를 반환
        return currEnemyCount;
    }

    public void pause(){
        running = false;
        try{
            gameThread.join(); // 스레드 종료 대기하기
        }catch (InterruptedException e){
        }
    }

    @Override
    public void run() { // 주기적으로 외계인을 생성하고, 스프라이트의 이동, 충돌 체크, 그리기 작업을 수행
        while(running){
            Random r = new Random();
            boolean isEnemySpawn = r.nextInt(100) + 1 < (getPlayer().speed + (int)(getPlayer().getPowerLevel() /2)); //이동속도와 파워레벨을 기준으로 적 생성
            if(isEnemySpawn && currEnemyCount < MAX_ENEMY_COUNT) spawnEnemy(); // 적 생성 조건을 만족하면 적 생성
            for(int i = 0; i < sprites.size(); i++){
                Sprite sprite = (Sprite) sprites.get(i); // Arraylist 에서 하나씩 가져와서 움직이기
                sprite.move();
            }
            for(int p = 0; p < sprites.size(); p++){
                for(int s = p + 1; s < sprites.size(); s++){
                    try{
                        Sprite me = (Sprite) sprites.get(p);
                        Sprite other = (Sprite) sprites.get(s);
                        // 충돌 체크
                        if(me.checkCollision(other)){
                            me.handleCollision(other);
                            other.handleCollision(me);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            draw();
            try {
                Thread.sleep(10);  // 10밀리초만큼 스레드를 일시 정지
            }catch (Exception e){
            }
        }
    }

    public void draw(){ //sprites 리스트에 있는 모든 스프라이트를 캔버스에 그리는 역할을 수행
        if(ourHolder.getSurface().isValid()){
            canvas = ourHolder.lockCanvas(); // 캔버스를 잠그고 그리기 작업을 시작
            canvas.drawColor(Color.BLACK); // 배경을 검은색으로 칠한다
            mapBitmapY++; // 맵 비트맵의 Y 좌표를 증가
            if(mapBitmapY < 0) mapBitmapY = 0; // 맵 비트맵의 Y 좌표가 음수라면 0으로 설정
            paint.setColor(Color.BLUE); // 그리기에 사용할 색상을 파란색으로 설정
            for(int i = 0; i < sprites.size(); i++){
                Sprite sprite = (Sprite) sprites.get(i); // 스프라이트를 가져옴
                sprite.draw(canvas, paint); // 스프라이트를 캔버스에 그림
            }
            ourHolder.unlockCanvasAndPost(canvas); // 캔버스를 잠금 해제하고 그리기 작업을 종료
        }
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        startGame();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

}
