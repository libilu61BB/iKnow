package com.example.test;

import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import android.view.View;
import android.content.Intent;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailPage extends AppCompatActivity {
    TextView[] mTextview=new TextView[8];
    Button backButton,lastButton,tag1,tag2;
    private MySql mysql;
    private Cursor cursor;
    private int Id;
    private String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        Intent intent = getIntent();
        Id = intent.getIntExtra("ActivityId",0);
        mysql = new MySql(this);
        cursor = mysql.Query(Id);
        cursor.moveToFirst();

        try{
            JSONObject ThisUser = new JSONObject(GetData("UserInformation.txt"));
            Username = ThisUser.getString("Username");
        }catch(Exception e){
            e.printStackTrace();
        }

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
                Intent intent = new Intent(DetailPage.this, PrivatePage.class);
                startActivity(intent);
            }
        });
        lastButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mysql.Delete(Id);
                deleteUserActivity d = new deleteUserActivity();
                d.start();
                try{
                    d.join();
                }catch(Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(DetailPage.this, PrivatePage.class);
                startActivity(intent);
            }
        });
        setActivity();
    }

    private String GetData(String FileName){
        try {
            File file = new File(Environment.getExternalStorageDirectory(),FileName);
            FileInputStream fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int len = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = fis.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            String data = baos.toString();
            baos.close();
            fis.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class deleteUserActivity extends Thread{
        public void run(){
            try{
                JSONObject Json = new JSONObject();
                Json.put("Username", Username);
                Json.put("ActivityID", String.valueOf(Id));
                String content = String.valueOf(Json);

                URL url = new URL("https://iknow.gycis.me:8443/updateData/deletePrivateActivity");
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
                }
                else{
                    Log.i("Connection", "Fail");
                }
            }catch(Exception e){
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

    private void setActivity(){
        mTextview[0].setText(cursor.getString(cursor.getColumnIndex("name")));
        mTextview[1].setText(cursor.getString(cursor.getColumnIndex("name")));
        mTextview[2].setText(cursor.getString(cursor.getColumnIndex("year"))+"年"+
                cursor.getString(cursor.getColumnIndex("year"))+"月"+
                cursor.getString(cursor.getColumnIndex("year"))+"日");
        mTextview[3].setText(cursor.getString(cursor.getColumnIndex("startHour"))+":"+cursor.getString(cursor.getColumnIndex("startMinute")));
        mTextview[4].setText(cursor.getString(cursor.getColumnIndex("endHour"))+":"+cursor.getString(cursor.getColumnIndex("endMinute")));
        mTextview[5].setText(cursor.getString(cursor.getColumnIndex("place")));
        mTextview[6].setText(cursor.getString(cursor.getColumnIndex("host")));
        tag1.setText(cursor.getString(cursor.getColumnIndex("mainLabel")));
        tag2.setText(cursor.getString(cursor.getColumnIndex("subLabel")));
        mTextview[7].setText(cursor.getString(cursor.getColumnIndex("url")));
    }
}