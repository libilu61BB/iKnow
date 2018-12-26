package com.example.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
    }

    //点击“开发者信息”按钮，进入OurInfo页面
    public void getDesignerInfo(View view)
    {
        Intent intent = new Intent(this, OurInfo.class);
        startActivity(intent);
    }

    //点击“反馈信息”页面，进入Feedback页面
    public void gotoFeedback(View view)
    {
        Intent intent = new Intent(this, FeedbackPage.class);
        startActivity(intent);
    }

    //返回上一页面
    public void goBack(View view)
    {
        finish();
    }
}
