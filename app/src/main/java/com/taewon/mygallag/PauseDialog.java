package com.taewon.mygallag;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

public class PauseDialog extends Dialog {

    RadioGroup bgMusicOnOff;
    RadioGroup effectSoundOnOff;

    public PauseDialog(@NonNull Context context){
        super(context);
        setContentView(R.layout.pause_dialog);
        bgMusicOnOff = findViewById(R.id.bgMusicOnOff);
        effectSoundOnOff = findViewById(R.id.effectMusicOnOff);
        init();
    }

    public void init(){
        bgMusicOnOff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.bgMusicOn:
                        MainActivity.bgMusic.setVolume(1,1); // 배경 음악의 볼륨을 1로 설정
                        break;
                        case R.id.bgMusicOff:
                        MainActivity.bgMusic.setVolume(0,0); // 배경 음악의 볼륨을 0으로 설정
                        break;
                }
            } //재생중인 배경음악의 볼륨을 0으로 설정했기 때문에 들리지는 않지만 계속 재생중 현재 음악이 끝나고 다음 음악이 재생되면 소리가 다시 1이됨(?)
        });
        effectSoundOnOff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.effectSoundOn:
                        MainActivity.effectVolumn =1.0f; // 효과음 볼륨을 1.0으로 설정
                        break;
                    case R.id.effectSoundOff:
                        MainActivity.effectVolumn =0; // 효과음 볼륨을 0으로 설정
                        break;
                }
            }
        });
        findViewById(R.id.dialogCancelBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss(); //cancel보다 안전하게 화면종료, cancel과의 차이 이해하기
            }  //cancel()은 사용자의 동작에 의해 다이얼로그를 취소하는 경우에 호출되고, dismiss()는 코드에서 직접 다이얼로그를 닫는 경우에 호출
        });
        findViewById(R.id.dialogOkBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
