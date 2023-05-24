package com.taewon.mygallag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.xml.transform.Result;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_dialog);
        init(); // 초기화 작업을 수행
    }

    private void init(){
        findViewById(R.id.goMainBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ((TextView)findViewById(R.id.userFinalScoreText)).setText(
                getIntent().getIntExtra("score", 0)+"");  // "userFinalScoreText" 텍스트 뷰에 인텐트로 전달받은 "score" 값을 설정
    }
}
