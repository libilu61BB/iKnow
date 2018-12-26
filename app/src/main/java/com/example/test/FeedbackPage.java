package com.example.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.graphics.Color;
import android.content.Intent;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FeedbackPage extends AppCompatActivity {
    private int TagNum = 3;
    private Button Back;
    private Button[] ButtonTag = new Button[TagNum];
    private boolean[] TagFlag = new boolean[TagNum];
    private EditText FeedBack;
    private Button Submit;
    private String[] TagContent = {"功能建议", "使用问题", "内容相关"};
    private TextView SubmitSucceed;
    private boolean feedbackFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_page);

        Back = findViewById(R.id.ButtonBack);
        Back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                FeedbackPage.this.finish();
            }
        });

        for(int i = 0; i < TagNum; i++){
            ButtonTag[i] = findViewById(R.id.ButtonTag1 + i);
            ButtonTag[i].setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    int n = v.getId() - R.id.ButtonTag1;
                    if(TagFlag[n] == false){
                        ButtonTag[n].setBackgroundColor(Color.parseColor("#b9b6b6"));
                        TagFlag[n] = true;
                    }
                    else{
                        ButtonTag[n].setBackgroundColor(Color.parseColor("#dcdcdc"));
                        TagFlag[n] = false;
                    }
                }
            });
        }

        FeedBack = findViewById(R.id.FeedBack);
        SubmitSucceed = findViewById(R.id.SubmitSucceed);

        Submit = findViewById(R.id.ButtonSubmit);
        Submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Submit submit = new Submit();
                submit.start();
                try{
                    submit.join();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(feedbackFlag){
                    SubmitSucceed.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    private class Submit extends Thread{
        public void run() {
            try {
                String feedback = FeedBack.getText().toString();
                JSONObject Json = new JSONObject();
                if(TagFlag[0])
                    Json.put("功能建议", "true");
                else
                    Json.put("功能建议", "false");
                if(TagFlag[1])
                    Json.put("使用问题", "true");
                else
                    Json.put("使用问题", "false");
                if(TagFlag[2])
                    Json.put("内容相关", "true");
                else
                    Json.put("内容相关", "false");
                Json.put("反馈内容",feedback);
                String content = String.valueOf(Json);


                URL url = new URL("https://iknow.gycis.me:8443/updateData/feedback");
                HttpURLConnection connection =  (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Log.i("Connection", content);
                OutputStream os = connection.getOutputStream();
                os.write(content.getBytes());
                os.flush();
                os.close();

                Log.i("Connection", String.valueOf(connection.getResponseCode()));
                if (connection.getResponseCode() == 200) {
                    String result = StreamToString(connection.getInputStream());
                    Log.i("Connection", result);
                    if(result.equals("Succeeded")){
                        feedbackFlag = true;
                    }
                    else{
                        feedbackFlag = false;
                    }
                }
                else{
                    Log.i("Connection", "Fail");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String StreamToString(InputStream is) {
        //把输入流转换成字符串
        try {
            ByteArrayOutputStream Baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1)
                Baos.write(buffer, 0, len);
            String result = Baos.toString();
            is.close();
            Baos.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void goBack(View view){finish();}
}