package com.example.test;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SettingPage extends AppCompatActivity {
    ImageButton labelBtn1,publishBtn1,accountBtn1,contactBtn1;

    Button publicBtn,privateBtn,labelBtn2,publishBtn2,accountBtn2,contactBtn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        labelBtn1= (ImageButton)findViewById(R.id.turntoLabelSetting);
        publishBtn1= (ImageButton)findViewById(R.id.turntoPublishEvent);
        accountBtn1= (ImageButton)findViewById(R.id.turntoAccountSetting);
        contactBtn1=(ImageButton)findViewById(R.id.turntoContactUs);
        labelBtn2 = (Button)findViewById(R.id.ButtonLabelSetting);
        publishBtn2 = (Button)findViewById(R.id.ButtonPublishActivity);
        accountBtn2 = (Button)findViewById(R.id.ButtonAccountSetting);
        contactBtn2 = (Button)findViewById(R.id.ButtonContactUs);
        publicBtn= (Button)findViewById(R.id.PublicButton);
        privateBtn= (Button)findViewById(R.id.PrivateButton);


        //发布消息页面

        publishBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingPage.this,CreateActivity.class);
                startActivity(intent);
            };
        });
        publishBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingPage.this,CreateActivity.class);
                startActivity(intent);
            };
        });


        //用户设置页面

        accountBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(SettingPage.this,AccountSetting.class);
                startActivity(intent2);
            };
        });
        accountBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(SettingPage.this,AccountSetting.class);
                startActivity(intent2);
            };
        });

        //标签设置页面

        labelBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(SettingPage.this,SetLabelForUser.class);
                startActivity(intent2);
            };
        });
        labelBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(SettingPage.this,SetLabelForUser.class);
                startActivity(intent2);
            };
        });

        //联系我们页面
        contactBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingPage.this,ContactUs.class);
                startActivity(intent);
            };
        });
        contactBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingPage.this,ContactUs.class);
                startActivity(intent);
            };
        });


        //点击“公共日历”按钮，跳转到"公共日历"页面
        publicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(SettingPage.this,PublicPage.class);
                startActivity(intent2);
                SettingPage.this.finish();
            };
        });

        //点击“我的日历”按钮，跳转到"我的日历"页面
        privateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(SettingPage.this,PrivatePage.class);
                startActivity(intent2);
                SettingPage.this.finish();
            };
        });

    }




}
