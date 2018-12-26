package com.example.test;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import android.text.Editable;
import android.text.TextWatcher;
import org.json.JSONObject;
import android.widget.ArrayAdapter;
import android.util.Log;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateActivity extends AppCompatActivity {
    private EditText nameText,starthourText,startminText,endhourText,endminText,addressText,sponserText,urlText,pictureText,introductionText;
    private DatePicker datePicker;
    private Button publishBtn;
    private ImageButton labelSetting;
    private ImageButton backBtn;
    private Spinner DepartmentSpinner;
    private Boolean[] flag={true,true,true,true,true,true,true,true,true,true,true,true}; //用于记录是否所有的事件都通过检查
    private String label1,label2,label3;
    private int year;
    private int month;
    private int day;
    private Boolean PublishSucceedFlag,UpdateFlag=true;
    private String Username="q";
    public class Event{
        String eventName;
        int starthour;
        int startmin;
        int endhour;
        int endmin;
        int year;
        int month;
        int day;
        String address;
        String department="0";
        String sponser;
        String url="0";
        String introduction="0";
        String MainLabel="0",SecondLabel="0",ThemeLabel="0"; //如果没有接收到数据，默认会传0到后端
        //定义各种set函数
        public void setEventName(String name){
            eventName=name;
        }
        public void setStarthour(int time1){
            starthour=time1;
        }
        public void setStartmin(int time2){
            startmin=time2;
        }
        public void setEndhour(int time3){
            endhour=time3;
        }
        public void setEndmin(int time4){
            endmin=time4;
        }
        public void setAddress(String add){
            address=add;
        }
        public void setSponser(String spon){
            sponser=spon;
        }
        public void setUrl(String urll){
            url=urll;
        }
        public void setIntroduction(String intro){
            introduction=intro;
        }
        public void setDate(int yearset,int monthset,int dayset){year=yearset;month=monthset;day=dayset;}
    }  //Event类的定义
    Event event=new Event();
    private List<String> DepartmentChoice(){
        List<String> data = new ArrayList<>();
        data.add("请选择您的院系:");
        data.add("建筑学院"); data.add("经济管理学院"); data.add("土木水利学院"); data.add("公共管理学院"); data.add("环境学院");data.add("马克思主义学院");
        data.add("人文学院"); data.add("机械工程学院"); data.add("社会科学学院"); data.add("信息科学技术学院"); data.add("法学院");data.add("新闻与传播学院");
        data.add("五道口金融学院");data.add("材料学院"); data.add("美术学院");data.add("工程物理系"); data.add("化学工程系");data.add("核能与新能源技术研究院");
        data.add("理学院"); data.add("体育部");data.add("艺术教育中心");data.add("生命科学学院");data.add("医学院");data.add("药学院");data.add("交叉信息研究院");
        data.add("苏世民学院");data.add("新雅书院");
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_activity);
        DepartmentSpinner = findViewById(R.id.Department);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DepartmentChoice());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DepartmentSpinner.setAdapter(adapter);
        publishBtn=(Button) findViewById(R.id.submitAll);
        backBtn=(ImageButton)findViewById(R.id.baccktoSetting);
        nameText = (EditText) findViewById(R.id.inputName);
        datePicker=(DatePicker) findViewById(R.id.inputeDate);
        starthourText=(EditText) findViewById(R.id.inputStartHour);
        startminText=(EditText)findViewById(R.id.inputStartMin);
        endhourText=(EditText)findViewById(R.id.inputEndHour);
        endminText=(EditText)findViewById(R.id.inputEndMin);
        addressText=(EditText)findViewById(R.id.inputAddress);
        sponserText=(EditText)findViewById(R.id.inputPeople);
        introductionText=(EditText)findViewById(R.id.editText2);
        urlText=(EditText)findViewById(R.id.inputUrl);
        labelSetting=(ImageButton)findViewById(R.id.turntoLabelSetting);
        //设置监听器，如果输入内容有改动，就更新其内容；但是不能针对“什么都没输入就点击按钮”的情况，因为文本框内容没有变动
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String name=nameText.getText().toString(); //设置listener
                checkName(name);
            }
        });
        starthourText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String starthour=starthourText.getText().toString();
                checkStartHour(starthour);
            }
        });
        startminText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String startmin=startminText.getText().toString();
                checkStartMin(startmin);
            }
        });
        endhourText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String endhour=endhourText.getText().toString();
                checkEndHour(endhour);
                checkEndTime();
            }
        });

        endminText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String endmin=endminText.getText().toString();
                checkEndMin(endmin);
                checkEndTime();
            }
        });

        addressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String address=addressText.getText().toString(); //设置listener
                checkAddress(address);
            }
        });
        sponserText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String sponser=sponserText.getText().toString(); //设置listener
                checkSponser(sponser);
            }
        });
        Calendar c=Calendar.getInstance();  // 获取现在的时间
        int year1=c.get(Calendar.YEAR);
        final int month1=c.get(Calendar.MONTH);
        int day1=c.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year1,month1,day1, new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int yearx, int monthOfYear, int dayOfMonth) {
                CreateActivity.this.year=yearx;
                CreateActivity.this.month=monthOfYear;
                CreateActivity.this.day=dayOfMonth;
                checkDate(year,month,day);
            }
        });

//        final Intent intent = getIntent();
//        label1 = intent.getStringExtra("MainLabel");
//        label2=intent.getStringExtra("SecondLabel");
//        label3=intent.getStringExtra("ThemeLabel");
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameText.getText().toString(); //设置listener
                checkName(name);
                String starthour=starthourText.getText().toString();
                checkStartHour(starthour);
                String startmin=startminText.getText().toString();
                checkStartMin(startmin);
                String endhour=endhourText.getText().toString();
                checkEndHour(endhour);
                checkEndTime();
                String endmin=endminText.getText().toString();
                checkEndMin(endmin);
                checkEndTime();
                String address=addressText.getText().toString();
                checkAddress(address);
                String sponser=sponserText.getText().toString();
                checkSponser(sponser);
                Calendar c=Calendar.getInstance();  // 获取现在的时间
                int year1=c.get(Calendar.YEAR);
                final int month1=c.get(Calendar.MONTH);
                int day1=c.get(Calendar.DAY_OF_MONTH);
                datePicker.init(year1,month1,day1, new OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int yearx, int monthOfYear, int dayOfMonth) {
                        CreateActivity.this.year=yearx;
                        CreateActivity.this.month=monthOfYear;
                        CreateActivity.this.day=dayOfMonth;
                        checkDate(year,month,day);
                    }
                });
                String Introduction=introductionText.getText().toString();
                if(Introduction!=null){ //上传Introduction和Url
                    event.introduction=Introduction;
                }
                String url=urlText.getText().toString();
                if(url!=null){
                    event.url=url;
                }
                //检查所属单位
                final String department1 = DepartmentSpinner.getSelectedItem().toString();
                if(department1.equals("请选择您的院系:")){
                    TextView Message1;
                    Message1 = (TextView) findViewById(R.id.collegeerror);
                    Message1.setVisibility(View.VISIBLE);
                    flag[8]=false;
                }
                else{
                    event.department=department1;
                }

                //检查标签是否选择
                if(label1==null||label2==null|| label3==null ){
                    TextView Message1;
                    Message1 = (TextView) findViewById(R.id.choosen);
                    Message1.setText("请选择标签");
                    Message1.setVisibility(View.VISIBLE);
                    flag[9]=false;
                }
                else{
                    event.MainLabel=label1;
                    event.SecondLabel=label2;
                    event.ThemeLabel=label3;
                }

                //检查所有标签是否都正确
                for(int i=0;i<12;i++){
                    if(flag[i]==false){
                        UpdateFlag=false;
                    }
                }
                if(UpdateFlag==true){
                    Publish publishActivity = new Publish();
                    publishActivity.start();
                    try {
                        publishActivity.join();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                if(PublishSucceedFlag){
                    TextView successInfo;
                    successInfo = (TextView)findViewById(R.id.setEventSuccessful);
                    successInfo.setVisibility(View.VISIBLE);
                }
            }
        });
        labelSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CreateActivity.this, LabelForEvent.class), 1);;
            };
        });
        //从标签页面获取标签

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            };
        });
    }//Oncreate结束

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("MainLabel");//得到新Activity 关闭后返回的数据
        String result2=data.getExtras().getString("SecondLabel");
        String result3=data.getExtras().getString("ThemeLabel");
        label1=result;
        label2=result2;
        label3=result3;
        TextView Message1;
        Message1 = (TextView) findViewById(R.id.choosen);
        Message1.setText(label1+"/"+label2+"/"+label3);
        Message1.setVisibility(View.VISIBLE);

    }

    public void checkDate (int year,int month, int day){
        Calendar d=Calendar.getInstance();  // 获取现在的时间
        int year2=d.get(Calendar.YEAR);
        final int month2=d.get(Calendar.MONTH);
        int day2=d.get(Calendar.DAY_OF_MONTH);
        if(year>year2|(year==year2 & month>month2 )|(year==year2 & month==month2 & day>=day2)) {  //时间的错误：如果发布的时间事件比当前日期早，就不行
            event.setDate(year,month,day);
            TextView Message3;
            Message3 = (TextView) findViewById(R.id.dateerror);
            Message3.setVisibility(View.INVISIBLE);
            flag[0]= true;
        }
        else
        { //返回错误
            TextView Message3;
            Message3 = (TextView) findViewById(R.id.dateerror);
            Message3.setVisibility(View.VISIBLE);
            flag[0] = false;
        }
    }
    public void checkName(String name){
        if(name.isEmpty()==false){  //检查事件名称的合理性：
            event.setEventName(name);
            TextView Message1;
            Message1 = (TextView) findViewById(R.id.namerror);
            Message1.setVisibility(View.INVISIBLE);
            flag[1]=true;
        }
        else{ //返回错误
            TextView Message1;
            Message1 = (TextView)findViewById(R.id.namerror);
            Message1.setVisibility(View.VISIBLE);
            flag[1]=false;
        }
    }
    public void checkStartHour(String a){
        if(a.isEmpty()==false){
            int starthour=Integer.parseInt(a); //设置listener
            if(starthour<24 & starthour>=0){  //检查事件名称的合理性：
                event.setStarthour(starthour);
                TextView Message1;
                Message1 = (TextView) findViewById(R.id.starthourerror);
                Message1.setVisibility(View.INVISIBLE);
                flag[2]=true;
            }
            else{
                TextView Message1;
                Message1 = (TextView)findViewById(R.id.starthourerror);
                Message1.setVisibility(View.VISIBLE);
                flag[3]=false;
            }
        }
        else{
            TextView Message1;
            Message1 = (TextView)findViewById(R.id.starthourerror);
            Message1.setVisibility(View.VISIBLE);
            flag[2]=false;
        }
    }
    public void checkStartMin(String a){
        if(a.isEmpty()==false){
            int startmin=Integer.parseInt(a); //设置listener
            if(startmin<60 & startmin>=0){  //检查事件名称的合理性：
                event.setStartmin(startmin);
                TextView Message1;
                Message1 = (TextView) findViewById(R.id.startminerror);
                Message1.setVisibility(View.INVISIBLE);
                flag[3]=true;
            }
            else{
                TextView Message1;
                Message1 = (TextView)findViewById(R.id.startminerror);
                Message1.setVisibility(View.VISIBLE);
                flag[3]=false;
            }
        }
        else{
            TextView Message1;
            Message1 = (TextView)findViewById(R.id.startminerror);
            Message1.setVisibility(View.VISIBLE);
            flag[3]=false;
        }
    }
    public void checkEndHour(String a){
        if(a.isEmpty()==false){
            int endhour=Integer.parseInt(a); //设置listener
            if(endhour<24 & endhour>=0){  //检查事件名称的合理性：
                event.setEndhour(endhour);
                TextView Message1;
                Message1 = (TextView) findViewById(R.id.endhourerror);
                Message1.setVisibility(View.INVISIBLE);
                flag[4]=true;
            }
            else{ //设置错误
                TextView Message1;
                Message1 = (TextView)findViewById(R.id.endhourerror);
                Message1.setVisibility(View.VISIBLE);
                flag[4]=false;
            }
        }
        else{
            TextView Message1;
            Message1 = (TextView)findViewById(R.id.endhourerror);
            Message1.setVisibility(View.VISIBLE);
            flag[4]=false;
        }
    }
    public void checkEndMin(String a){//& ((event.endhour>event.starthour)||(event.endhour==event.starthour & event.startmin<endmin))
        if(a.isEmpty()==false){
            int endmin=Integer.parseInt(a); //设置listener
            if(endmin<60 & endmin>=0 ){  //检查事件名称的合理性：
                event.setEndmin(endmin);
                TextView Message1;
                Message1 = (TextView) findViewById(R.id.endminerror);
                Message1.setVisibility(View.INVISIBLE);
                flag[5]=true;
            }
            else{
                TextView Message1;
                Message1 = (TextView)findViewById(R.id.endminerror);
                Message1.setVisibility(View.VISIBLE);
                flag[5]=false;
            }
        }
        else{
            TextView Message1;
            Message1 = (TextView)findViewById(R.id.endminerror);
            Message1.setVisibility(View.VISIBLE);
            flag[5]=false;
        }
    }
    public void checkEndTime(){
        if(((event.endhour>event.starthour)||(event.endhour==event.starthour & event.startmin<event.endmin)) &event.endmin<60 & event.endmin>=0 &event.endhour<24 & event.endhour>=0){
            TextView Message1;
            Message1 = (TextView) findViewById(R.id.endminerror);
            Message1.setVisibility(View.INVISIBLE);
            flag[5]=true;
            TextView Message2;
            Message2 = (TextView) findViewById(R.id.endhourerror);
            Message2.setVisibility(View.INVISIBLE);
            flag[4]=true;
        }
        else{
            TextView Message1;
            Message1 = (TextView) findViewById(R.id.endminerror);
            Message1.setVisibility(View.VISIBLE);
            flag[5]=false;
            TextView Message2;
            Message2 = (TextView) findViewById(R.id.endhourerror);
            Message2.setVisibility(View.VISIBLE);
            flag[4]=false;
        }
    }
    public void checkAddress(String address){
        if(address.isEmpty()==false){  //检查事件名称的合理性：
            event.setAddress(address);
            TextView Message1;
            Message1 = (TextView) findViewById(R.id.addresserror);
            Message1.setVisibility(View.INVISIBLE);
            flag[6]=true;
        }
        else{ //返回错误
            TextView Message1;
            Message1 = (TextView)findViewById(R.id.addresserror);
            Message1.setVisibility(View.VISIBLE);
            flag[6]=false;
        }
    }
    public void checkSponser(String sponser){
        if(sponser.isEmpty()==false){  //检查事件名称的合理性：
            event.setSponser(sponser);
            TextView Message1;
            Message1 = (TextView) findViewById(R.id.sponsererror);
            Message1.setVisibility(View.INVISIBLE);
            flag[7]=true;
        }
        else{ //返回错误
            TextView Message1;
            Message1 = (TextView)findViewById(R.id.sponsererror);
            Message1.setVisibility(View.VISIBLE);
            flag[7]=false;
        }
    }

    public class Publish extends Thread{
            public void run() {
                try{
                    JSONObject Json=new JSONObject();
                    //Json.put("Username",Username);
                    Json.put("Name",event.eventName);
                    Json.put("Year",event.year);
                    Json.put("Month",event.month+1);
                    Json.put("Day",event.day);
                    Json.put("StartHour",event.starthour);
                    Json.put("StartMin",event.startmin);
                    Json.put("EndHour",event.endhour);
                    Json.put("EndMin",event.endmin);
                    Json.put("Address",event.address);
                    Json.put("Holder",event.sponser);
                    Json.put("Department",event.department);
                    Json.put("Introduction",event.introduction);
                    Json.put("Url",event.url);
                    Json.put("MainLabel",event.MainLabel);
                    Json.put("SecondLabel",event.SecondLabel);
                    Json.put("ThemeLabel",event.ThemeLabel);

                    String content = String.valueOf(Json);  //Json格式转成字符串来传输

                    URL url = new URL("https://iknow.gycis.me:8443/updateData/addActivity");  //不同的请求发送到不同的URL地址，见群里的“后端网页名字设计.docx”
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

                    Log.i("Connection", String.valueOf(connection.getResponseCode()));  //如果ResponseCode=200说明和服务器连接正确
                    if (connection.getResponseCode() == 200) {
                        //以字符串格式读取服务器的返回内容，Register功能只需返回普通字符串，如果请求的是活动信息则将会返回Json格式的字符串，
                        //可以用形如JSONObject Json = new JSONObject(String)的语句把字符串转成Json格式
                        String result = StreamToString(connection.getInputStream());
                        Log.i("Connection", result);
                        if(result.equals("activity add succeed"))
                            PublishSucceedFlag = true;
                        else if(result.equals("Add fail"))
                            PublishSucceedFlag = false;
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

}
