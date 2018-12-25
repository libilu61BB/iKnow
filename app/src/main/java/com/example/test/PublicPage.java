package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PublicPage extends AppCompatActivity  {
    Button browser, private_btn, setting_btn;
    Button[] dateBtns = new Button[14];
    int[] date = new int[3];
    String username;
    List<Activity> actList = new ArrayList<Activity>();
    int i = 0;
    private LinearLayout dateColumn;
    private LinearLayout publicActivity;
    private final static int REQUESTCODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username = "李碧璐";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_page);
        browser = findViewById(R.id.BrowserButton);
        browser.setOnClickListener(pageChangeListener);
        private_btn = findViewById(R.id.PrivateButton);
        setting_btn = findViewById(R.id.SettingButton);
        private_btn.setOnClickListener(switchButtonListener);
        setting_btn.setOnClickListener(switchButtonListener);
        dateColumn = findViewById(R.id.dateColumn);
        setSystemDate();
        initDateColumn(0);
        GetActivity(getSystemDate());

        publicActivity = findViewById(R.id.publicActivity);
        try{
            Thread.sleep(800);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        initPublicActivity();
    }
    /**
     * 向后端发送时间和用户，返回符合条件的事件
     */
    protected void GetActivity(final String s){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject Json = new JSONObject();  //把数据存成Json格式
                    Json.put("Username", username);
                    Json.put("Date", s);

                    String content = String.valueOf(Json);  //Json格式转成字符串来传输

                    URL url = new URL("https://iknow.gycis.me/downloadData/getAcByTag");  //不同的请求发送到不同的URL地址，见群里的“后端网页名字设计.docx”
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
                        //以字符串格式读取服务器的返回内容，Register功能只需返回普通字符串，如果请求的是活动信息则将会返回Json格式的字符串，
                        //可以用形如JSONObject Json = new JSONObject(String)的语句把字符串转成Json格式
                        String result = StreamToString(connection.getInputStream());
                        JSONObject thisJson = new JSONObject(result);
                        int acNum = thisJson.getInt("ActivityNumber");
                        JSONObject acList = new JSONObject(thisJson.getString("Activity"));
                        JSONObject[] ac = new JSONObject[acNum];
                        actList.clear();
                        List<Activity> tempActList = new ArrayList<Activity>();
                        for(int i = 0; i < acNum; i++){
                            ac[i] = new JSONObject(acList.getString("Activity"+String.valueOf(i+1)));
                            Activity temp = new Activity();
                            temp.setActivityId(ac[i].getInt("Id"));
                            temp.setStartHour(ac[i].getInt("Start_hour"));
                            temp.setStartMinute(ac[i].getInt("Start_minute"));
                            temp.setEndHour(ac[i].getInt("End_hour"));
                            temp.setEndMinute(ac[i].getInt("End_minute"));
                            temp.setDay(ac[i].getInt("Day"));
                            temp.setMonth(ac[i].getInt("Month"));
                            temp.setYear(ac[i].getInt("Year"));
                            temp.setName(ac[i].getString("Name"));
                            temp.setPlace(ac[i].getString("Address"));
                            temp.setHost(ac[i].getString("Holder"));
                            temp.setMainLabel(ac[i].getString("Tag1"));
                            temp.setSubLabel(ac[i].getString("Tag2"));
                            temp.setActivityLabel(ac[i].getString("Tag3"));
                            temp.setIntroduction(ac[i].getString("Introduction"));
                            temp.setUrl(ac[i].getString("Url"));
                            tempActList.add(temp);
                        }
                        actList = new ArrayList<>(tempActList);
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
    /**
     * 将输入流转换成字符串
     */
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
    /**
     * 返回系统时间
     */
    private String getSystemDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(year*10000+month*100+day);
    }
    /**
     * 设置自定义的系统时间控制数组
     */
    private void setSystemDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        date[0] = year;
        date[1] = month;
        date[2] = day;

    }
    /**
     * 初始化日历栏
     */
    private void initDateColumn(int a){
        LinearLayout dateCase = dateColumn;
        int Size = 14;
        String item = "";
        int day = (date[2]+2*date[1]+3*(date[1]+1)/5+date[0]+date[0]/4-date[0]/100+date[0]/400)%7;
        for(int i = 0; i < Size; i++){
            switch((day+i)%7) {
                case 0: item = "一\n\n"; break;
                case 1: item = "二\n\n"; break;
                case 2: item = "三\n\n"; break;
                case 3: item = "四\n\n"; break;
                case 4: item = "五\n\n"; break;
                case 5: item = "六\n\n"; break;
                case 6: item = "日\n\n"; break;
            }
            if(date[1]==1||date[1]==3||date[1]==5||date[1]==7||date[1]==8||date[1]==10||date[1]==12) {
                if(date[2]+i==31){
                    item += String.valueOf(date[2]+i);
                }
                else {
                    item += String.valueOf((date[2] + i) % 31);
                }
            }
            else if(date[1]==4||date[1]==6||date[1]==9||date[1]==11){
                if(date[2]+i==30){
                    item += String.valueOf(date[2]+i);
                }
                else{
                    item += String.valueOf((date[2]+i)%30);
                }
            }
            else if(date[1]==2) {
                if (date[2] + i == 30) {
                    item += String.valueOf(date[2] + i);
                } else {
                    item += String.valueOf((date[2] + i) % 28);
                }
            }
            WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();

            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(width/7, LinearLayout.LayoutParams.MATCH_PARENT);
            itemParams.setMargins(0, 5, 0, 5);
            dateBtns[i] = (Button) LayoutInflater.from(this).inflate(R.layout.date_button, null);
            dateBtns[i].setText(item);
            dateBtns[i].setTag(item);
            dateBtns[i].setId(i);
            dateBtns[i].setLayoutParams(itemParams);
            dateBtns[i].setOnClickListener(dateChangeListener);
            dateBtns[a].setBackgroundColor(android.graphics.Color.rgb(237,189,101));
            dateCase.addView(dateBtns[i]);
            item = "";
        }
    }
    /**
     * 初始化公共界面
     */
    private void initPublicActivity(){
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        //System.out.println(actList[0].getStartMinute());
        if (actList == null){
            publicActivity.addView(null);
        }
        else {
            LinearLayout activityList = publicActivity;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); // 每行的水平LinearLayout
            layoutParams.setMargins(0, 0, 0, 0);
            String circleButtonText = "";
            String barButtonText = "";
            i = 0;
            System.out.println(actList);
            for (Activity ac : actList) {
                String a = "";
                String b = "";
                if (ac.getStartMinute() < 10) {
                    a = "0" + String.valueOf(ac.getStartMinute());
                } else {
                    a = String.valueOf(ac.getStartMinute());
                }
                if (ac.getEndMinute() < 10) {
                    b = "0" + String.valueOf(ac.getEndMinute());
                } else {
                    b = String.valueOf(ac.getEndMinute());
                }
                circleButtonText = String.valueOf(ac.getStartHour()) + ":" + a + "\n————\n" + String.valueOf(ac.getEndHour()) + ":" + b;
                barButtonText = ac.getName() + "\n\n" + ac.getPlace();
                LinearLayout.LayoutParams circleButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                circleButtonParams.setMargins(0, 0, 0, 0);
                circleButtonParams.weight = 5;
                LinearLayout.LayoutParams barButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                barButtonParams.setMargins(0, 0, 0, 0);
                barButtonParams.weight = 1;
                Button circleBtn = (Button) LayoutInflater.from(this).inflate(R.layout.circle_button, null);
                Button barBtn = (Button) LayoutInflater.from(this).inflate(R.layout.bar_button, null);
                circleBtn.setText(circleButtonText);
                barBtn.setText(barButtonText);
                barBtn.setId(500+i);
                circleBtn.setLayoutParams(circleButtonParams);
                barBtn.setLayoutParams(barButtonParams);
                barBtn.setOnClickListener(activityDetailListener);
                if(ac.getMainLabel().equals("科创")) {
                    barBtn.getBackground().setColorFilter(android.graphics.Color.rgb(191,191,191),PorterDuff.Mode.ADD);
                }
                else if(ac.getMainLabel().equals("计算机")) {
                    barBtn.getBackground().setColorFilter(android.graphics.Color.rgb(255,223,127),PorterDuff.Mode.ADD);
                }
                else if(ac.getMainLabel().equals("实践")) {
                    barBtn.getBackground().setColorFilter(android.graphics.Color.rgb(255,181,181),PorterDuff.Mode.ADD);
                }
                else if(ac.getMainLabel().equals("外语")) {
                    barBtn.getBackground().setColorFilter(android.graphics.Color.rgb(229,229,255),PorterDuff.Mode.ADD);
                }
                else if(ac.getMainLabel().equals("经济")) {
                    barBtn.getBackground().setColorFilter(android.graphics.Color.rgb(255,255,204),PorterDuff.Mode.ADD);
                }
                else if(ac.getMainLabel().equals("文学")) {
                    barBtn.getBackground().setColorFilter(android.graphics.Color.rgb(183,151,207),PorterDuff.Mode.ADD);
                }
                else if(ac.getMainLabel().equals("创业")) {
                    barBtn.getBackground().setColorFilter(android.graphics.Color.rgb(127,215,247),PorterDuff.Mode.ADD);
                }
                else if(ac.getMainLabel().equals("电影")) {
                    barBtn.getBackground().setColorFilter(android.graphics.Color.rgb(226,172,136),PorterDuff.Mode.ADD);
                }
                else if(ac.getMainLabel().equals("体育")) {
                    barBtn.getBackground().setColorFilter(android.graphics.Color.rgb(200,231,167),PorterDuff.Mode.ADD);
                }
                else if(ac.getMainLabel().equals("志愿")) {
                    barBtn.getBackground().setColorFilter(android.graphics.Color.rgb(255,128,128),PorterDuff.Mode.ADD);
                }
                else if(ac.getMainLabel().equals("艺术")) {
                    barBtn.getBackground().setColorFilter(android.graphics.Color.rgb(150,186,218),PorterDuff.Mode.ADD);
                }

                LinearLayout activityCase = new LinearLayout(this);
                activityCase.setOrientation(LinearLayout.HORIZONTAL);
                activityCase.setLayoutParams(layoutParams);
                activityCase.addView(circleBtn);
                activityCase.addView(barBtn);
                activityList.addView(activityCase);
                i++;
            }
        }
    }

    Button.OnClickListener pageChangeListener = new Button.OnClickListener() {
        public void onClick(View v) { //转换到搜索页面的监听
            Intent intent = new Intent(PublicPage.this, SearchPage.class);
            startActivity(intent);
            PublicPage.this.finish();
        }
    };

    Button.OnClickListener dateChangeListener = new Button.OnClickListener() {
        public void onClick(View v){ //日期栏变动的监听
            int n = v.getId();
            publicActivity.removeAllViews();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, n);
            String targetDay = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
            GetActivity(targetDay);
            initPublicActivity();
            dateColumn.removeAllViews();
            initDateColumn(n);
        }
    };
    Button.OnClickListener activityDetailListener = new Button.OnClickListener(){
        public void onClick(View v){  //事件详情页面监听
            int n = v.getId();
            int no = actList.get(n-500).getActivityId();
            Intent intent = new Intent(PublicPage.this, ResultDetailPage.class);
            intent.putExtra("ActivityNum",no);
            startActivityForResult(intent,REQUESTCODE);
        }
    };
    Button.OnClickListener switchButtonListener = new Button.OnClickListener() {
        public void onClick(View v) { //页面转换栏监听
            if(v.getId()==R.id.PrivateButton) {
                Intent intent = new Intent(PublicPage.this, PrivatePage.class);
                startActivity(intent);
                PublicPage.this.finish();
            }
            else if(v.getId()==R.id.SettingButton){
                Intent intent = new Intent(PublicPage.this, SearchPage.class);
                startActivity(intent);
                PublicPage.this.finish();
            }
        }
    };
}


