package com.example.test;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Network;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import android.util.*;
import java.util.*;
import android.graphics.drawable.*;

public class PrivatePage extends AppCompatActivity {
    private int DateButtonNum = 28;
    private Button[] DateButton = new Button[DateButtonNum];
    private int CurrentDateId = 0;
    private int CurrentYear, CurrentMonth, CurrentDate;
    private MySql mysql;
    private Cursor cursor;
    private int ShowNum = 0;
    String[] Week = { "日\n", "一\n", "二\n", "三\n", "四\n", "五\n", "六\n"};
    int[] MonthDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private Color[] MyColor = new Color[11];

    private void Init(){
        LinearLayout layout = findViewById(R.id.DateViewLayout);
        Calendar Current = Calendar.getInstance();
        Current.setTimeInMillis(System.currentTimeMillis());
        CurrentYear = Current.get(Calendar.YEAR);
        CurrentMonth = Current.get(Calendar.MONTH);
        CurrentDate = Current.get(Calendar.DATE);
        int CurrentWeekday = Current.get(Calendar.DAY_OF_WEEK);

        for(int i = 0; i < DateButtonNum; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getPixelsFromDp(50), LinearLayout.LayoutParams.MATCH_PARENT);
            DateButton[i] = new Button(PrivatePage.this);
            if(i == CurrentDateId)
                DateButton[i].setBackgroundColor(Color.parseColor("#64c6a121"));
            else
                DateButton[i].setBackgroundColor(Color.WHITE);
            DateButton[i].setText(Week[(CurrentWeekday - 1 + i)%7] + String.valueOf(((CurrentDate+i-1)%MonthDay[CurrentMonth]+1)));
            DateButton[i].setId(i);
            DateButton[i].setLayoutParams(params);
            DateButton[i].setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    DateButton[CurrentDateId].setBackgroundColor(Color.WHITE);
                    CurrentDateId = v.getId();
                    DateButton[CurrentDateId].setBackgroundColor(Color.parseColor("#64c6a121"));
                    ShowActivity();
                }
            });
            layout.addView(DateButton[i]);
        }
    }

    private void ShowActivity(){
        mysql = new MySql(this);
        for(int i = 20181225; i < 20181225+ShowNum; i++){
            View v = findViewById(i);
            ViewGroup vg = (ViewGroup) v.getParent();
            vg.removeView(v);
        }
        int DateNow = (CurrentDate+CurrentDateId-1)%MonthDay[CurrentMonth]+1;
        int MonthNow = (CurrentDate+CurrentDateId)>MonthDay[CurrentMonth]?(CurrentMonth+1)%12+1:CurrentMonth%12+1;
        int YearNow = MonthNow<CurrentMonth?CurrentYear+1:CurrentYear;
        ShowNum = 0;
        for(int i = 0; i < 9; i++){
            cursor = mysql.Query(YearNow, MonthNow, DateNow,2*i+6);
            int num = cursor.getCount();
            for(int j = 0; j < num; j++){
                cursor.moveToNext();
                addActivityButton(num,cursor,i,j);
            }
        }
    }

    private void addActivityButton(int n, Cursor ActivityCursor, int i, int j){
        final int ThisId = ActivityCursor.getInt(ActivityCursor.getColumnIndex("Id"));
        LinearLayout layout = findViewById(R.id.TimeView06 + i);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getPixelsFromDp(250/n), LinearLayout.LayoutParams.MATCH_PARENT);
        Button ActivityButton = new Button(this);
        ActivityButton.setId(20181225+ShowNum);
        String ActivityName = ActivityCursor.getString(ActivityCursor.getColumnIndex("name"));
        if(n == 1){
            if(ActivityName.length()>18)
                ActivityName = ActivityName.substring(0,18)+"...";
            if(Integer.valueOf(ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute")))<10
                    && Integer.valueOf(ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")))<10)
                ActivityButton.setText(ActivityName + "\n时间:" +
                        ActivityCursor.getString(ActivityCursor.getColumnIndex("startHour")) + ":0" + ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute"))
                        + "-" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endHour")) + ":0" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")) +
                        "      地点:" + ActivityCursor.getString(ActivityCursor.getColumnIndex("place")));
            else if(Integer.valueOf(ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute")))<10 )
                ActivityButton.setText(ActivityName + "\n时间:" +
                        ActivityCursor.getString(ActivityCursor.getColumnIndex("startHour")) + ":0" + ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute"))
                        + "-" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endHour")) + ":" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")) +
                        "      地点:" + ActivityCursor.getString(ActivityCursor.getColumnIndex("place")));
            else if(Integer.valueOf(ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")))<10 )
                ActivityButton.setText(ActivityName + "\n时间:" +
                        ActivityCursor.getString(ActivityCursor.getColumnIndex("startHour")) + ":" + ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute"))
                        + "-" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endHour")) + ":0" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")) +
                        "      地点:" + ActivityCursor.getString(ActivityCursor.getColumnIndex("place")));
            else
                ActivityButton.setText(ActivityName + "\n时间:" +
                        ActivityCursor.getString(ActivityCursor.getColumnIndex("startHour")) + ":" + ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute"))
                        + "-" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endHour")) + ":" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")) +
                        "      地点:" + ActivityCursor.getString(ActivityCursor.getColumnIndex("place")));
        }
        else{
            if(ActivityName.length()>8)
                ActivityName = ActivityName.substring(0,8)+"...";
            if(Integer.valueOf(ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute")))<10
                    && Integer.valueOf(ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")))<10)
                ActivityButton.setText(ActivityName + "\n时间:" +
                        ActivityCursor.getString(ActivityCursor.getColumnIndex("startHour")) + ":0" + ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute"))
                        + "-" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endHour")) + ":0" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")) +
                        "\n地点:" + ActivityCursor.getString(ActivityCursor.getColumnIndex("place")));
            else if(Integer.valueOf(ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute")))<10 )
                ActivityButton.setText(ActivityName + "\n时间:" +
                        ActivityCursor.getString(ActivityCursor.getColumnIndex("startHour")) + ":0" + ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute"))
                        + "-" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endHour")) + ":" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")) +
                        "\n地点:" + ActivityCursor.getString(ActivityCursor.getColumnIndex("place")));
            else if(Integer.valueOf(ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")))<10 )
                ActivityButton.setText(ActivityName + "\n时间:" +
                        ActivityCursor.getString(ActivityCursor.getColumnIndex("startHour")) + ":" + ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute"))
                        + "-" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endHour")) + ":0" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")) +
                        "\n地点:" + ActivityCursor.getString(ActivityCursor.getColumnIndex("place")));
            else
                ActivityButton.setText(ActivityName + "\n时间:" +
                        ActivityCursor.getString(ActivityCursor.getColumnIndex("startHour")) + ":" + ActivityCursor.getString(ActivityCursor.getColumnIndex("startMinute"))
                        + "-" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endHour")) + ":" + ActivityCursor.getString(ActivityCursor.getColumnIndex("endMinute")) +
                        "\n地点:" + ActivityCursor.getString(ActivityCursor.getColumnIndex("place")));
        }
        ActivityButton.setTextSize(14-2*n);
        ActivityButton.setMaxLines(3);
        ActivityButton.setBackgroundResource(R.drawable.button_myactivity1+ChooseColor(ActivityCursor.getString(ActivityCursor.getColumnIndex("mainLabel"))));
        ActivityButton.setLayoutParams(params);
        ActivityButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(PrivatePage.this, DetailPage.class);
                intent.putExtra("ActivityId", ThisId);
                startActivity(intent);
            }
        });
        layout.addView(ActivityButton);
        ShowNum += 1;
    }

    private int ChooseColor(String Tag1){
        switch(Tag1){
            case "科创":return 0;
            case "计算机":return 1;
            case "体育":return 2;
            case "实践":return 3;
            case "外语":return 4;
            case "经济":return 5;
            case "创业":return 6;
            case "文学":return 7;
            case "电影":return 8;
            case "志愿":return 9;
            case "艺术":return 10;
            default:return  1;
        }
    }

    private int getPixelsFromDp(int size){
        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return(size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_page);
        Init();

        /*mysql = new MySql(this);
        mysql.Delete();
        mysql.Insert(20,2018,12,23,8,15,9,45,"这是活动1","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组","www.baidu.com","圣诞聚餐20");
        mysql.Insert(1,2018,12,25,8,15,9,45,"这是活动1","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐1");
        mysql.Delete(1);
        mysql.Insert(2,2018,12,25,9,15,9,45,"这是活动2","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐2");
        mysql.Insert(3,2018,12,25,9,15,16,45,"这是活动3","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐3");
        mysql.Insert(4,2018,12,26,9,15,16,45,"这是活动3","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐4");
        mysql.Insert(5,2018,12,26,9,15,16,45,"这是活动3","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐5");
        mysql.Insert(6,2018,12,27,9,15,16,45,"这是活动3","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐6");
        mysql.Insert(7,2018,12,31,20,15,9,45,"这是活动1","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐7");
        mysql.Insert(8,2019,1,5,9,15,9,45,"这是活动2","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐8");
        mysql.Insert(9,2019,1,5,9,15,16,45,"这是活动3","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐9");
        mysql.Insert(10,2019,1,6,9,15,16,45,"这是活动3","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐10");
        mysql.Insert(11,2019,1,6,9,15,16,45,"这是活动3","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐11");
        mysql.Insert(12,2019,1,7,9,15,16,45,"这是活动3","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐12");
        mysql.Insert(13,2019,1,5,11,15,9,45,"这是活动2","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐13");
        mysql.Insert(14,2019,1,5,16,15,16,45,"这是活动3","主标签",
                "副标签","活动标签","清华大学","拒绝熬夜组",null,"圣诞聚餐14");*/
        ShowActivity();
    }
    //点击"公共日历"按钮，进入“公共日历”页面
    public void gotoPublicPage(View view){
        Intent intent = new Intent(PrivatePage.this, PublicPage.class);
        startActivity(intent);
        PrivatePage.this.finish();
    }

    //点击"软件设置"按钮，进入“软件设置”页面
    public void gotoSettingPage(View view) {
        Intent intent = new Intent(PrivatePage.this, SettingPage.class);
        startActivity(intent);
        PrivatePage.this.finish();
    }
}
