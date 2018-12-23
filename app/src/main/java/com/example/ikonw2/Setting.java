package com.example.ikonw2;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Setting extends AppCompatActivity {
    ImageButton labelBtn,publishBtn,accountBtn,contactBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        labelBtn= (ImageButton)findViewById(R.id.turntoLabelSetting);
        publishBtn= (ImageButton)findViewById(R.id.turntoPublishEvent);
        accountBtn= (ImageButton)findViewById(R.id.turntoAccountSetting);
        contactBtn=(ImageButton)findViewById(R.id.turntoContactUs);
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Setting.this,CreatActivity.class);
                startActivity(intent);
            }
        });
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(Setting.this,AccountSetting.class);
                startActivity(intent2);
            }
        });
    }

}
