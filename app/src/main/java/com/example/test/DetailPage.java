package com.example.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import android.view.View;
import android.content.Intent;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailPage extends AppCompatActivity {

    TextView[] mTextview=new TextView[8];
    Button backButton,lastButton,tag1,tag2;
    int ActivityID;
    boolean getActivityflag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        mTextview[0]=findViewById(R.id.activityname);
        mTextview[1]=findViewById(R.id.tv1);
        mTextview[2]=findViewById(R.id.tv2);
        mTextview[3]=findViewById(R.id.tv3);
        mTextview[4]=findViewById(R.id.tv4);
        mTextview[5]=findViewById(R.id.tv5);
        mTextview[6]=findViewById(R.id.tv6);
        mTextview[7]=findViewById(R.id.tv7);
        backButton=findViewById(R.id.back_button);
        lastButton=findViewById(R.id.last_button);
        tag1=findViewById(R.id.tag1);
        tag2=findViewById(R.id.tag2);
        backButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //跳转的界面
                //Intent intent = new Intent(DetailPage.this,.class);
                //startActivity(intent);
            }
        });
        GetActivity();
    }

    protected void GetActivity(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject Json = new JSONObject();  //把数据存成Json格式
                    Json.put("ActivityID", ActivityID);
                    String content = String.valueOf(Json);  //Json格式转成字符串来传输

                    URL url = new URL("https://iknow.gycis.me/updateData/addNewUser");  //不同的请求发送到不同的URL地址，见群里的“后端网页名字设计.docx”
                    HttpURLConnection connection =  (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    Log.i("Connection", content);
                    OutputStream os = connection.getOutputStream();  //打开输出流传输数据
                    os.write(content.getBytes());
                    os.flush();
                    os.close();
                    Log.i("Connection", String.valueOf(connection.getResponseCode()));  //如果ResponseCode=200说明和服务器
                    if (connection.getResponseCode() == 200) {
                        String result = StreamToString(connection.getInputStream());
                        JSONObject thisJson = new JSONObject(result);
                        Log.i("Connection", result);
                        setActivity();
                    }
                    else{
                        Log.i("Connection", "Fail");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    private void setActivity(){
        mTextview[0].setText("活动名称");
        mTextview[1].setText("活动名称");
        mTextview[2].setText("8月1日");
        mTextview[3].setText("8：00");
        mTextview[4].setText("10：00");
        mTextview[5].setText("六教");
        mTextview[6].setText("工物系");
        tag1.setText("科创");
        tag2.setText("社工");
        mTextview[7].setText("URL");
    }
}
