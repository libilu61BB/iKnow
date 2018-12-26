package com.example.test;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AccountSetting extends AppCompatActivity {
    public ImageButton backBtn,changeCollegeBtn;
    public Button changePasswordBtn;
    private EditText passwordText1, passwordText2, passwordText3;
    private TextView nameText,collegeText;
    private Boolean[] flag1={true,true,true}; //用于记录是否所有的事件都通过检查
    public String oldpassword,newpassword,confirmpassword;
    public Boolean Flag1=true;
    public Boolean changeFlag=false,changeDepartmentFlag=false;
    public String name,college="NULL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        //首先从本地调取用户的信息
        try{
            JSONObject ThisUser = new JSONObject(GetData("UserInformation.txt"));
            name = ThisUser.getString("Username");
        }catch(Exception e){
            e.printStackTrace();
        }
        getDepartment getDepartment = new getDepartment();
        getDepartment.start();
        try{
            getDepartment.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        backBtn=(ImageButton)findViewById(R.id.backtoSetting);
        changePasswordBtn=(Button)findViewById(R.id.changePassword);
        changeCollegeBtn=(ImageButton)findViewById(R.id.changeCollege);
        nameText=(TextView)findViewById(R.id.nameshow);
        nameText.setText(name);
        collegeText=(TextView)findViewById(R.id.Departmentshow);
        collegeText.setText(college);
        passwordText1=(EditText)findViewById(R.id.oldpassword);
        passwordText2=(EditText)findViewById(R.id.newpassword);
        passwordText3=(EditText)findViewById(R.id.confirmpassword);

        changeCollegeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                dialogChoice();
                /*if(changeDepartmentFlag==true){
                    //将更改后的标签写入本地文件
                    try{
                        JSONObject UserInformation = new JSONObject(GetData("UserInformation.txt"));
                        UserInformation.put("Department",college);
                        WriteToFile("UserInformation.txt", UserInformation.toString());
                        TextView Message1;
                        Message1 = (TextView) findViewById(R.id.changeCol);
                        Message1.setVisibility(View.VISIBLE);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }*/
            }
        });

        //修改密码
        changePasswordBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                oldpassword=passwordText1.getText().toString();
                newpassword = passwordText2.getText().toString();
                confirmpassword= passwordText3.getText().toString();
//                System.out.println(oldpassword);
//                System.out.println(newpassword);
//                System.out.println(confirmpassword);

                checkpassword1(newpassword);
                checkpassword2(confirmpassword,newpassword);
                for(int i=0;i<3;i++){
                    if(flag1[i]==false){
                        Flag1=false;
                    }
                }
                if(Flag1!=null& Flag1==true){
                    changePw sendUserLabel = new changePw();
                    sendUserLabel.start();
                    try{
                        sendUserLabel.join();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    //此处应该有错误提示
                }
                if(changeFlag==true){
                    TextView Message1;
                    Message1 = (TextView)findViewById(R.id.after);
                    Message1.setText("修改密码成功！");
                    Message1.setVisibility(View.VISIBLE);

                }
                else if(changeFlag==false){
                    TextView Message1;
                    Message1 = (TextView)findViewById(R.id.after);
                    Message1.setText("修改密码失败！");
                    Message1.setVisibility(View.VISIBLE);

                }
            }
        });
        //修改密码要检测
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountSetting.this.finish();
            };
        });

    }
    public void checkpassword1(String p){
        if(p.isEmpty()==false & p.length()<=20){  //检查密码是否合理
            TextView Message1;
            Message1 = (TextView) findViewById(R.id.pwerrpr1);
            Message1.setVisibility(View.INVISIBLE);
            flag1[1]=true;
        }
        else{ //返回错误
            TextView Message1;
            Message1 = (TextView)findViewById(R.id.pwerrpr1);
            Message1.setVisibility(View.VISIBLE);
            flag1[1]=false;
        }
    }
    public void checkpassword2(String pw,String p){
        //检查是不是两个密码相同
        if(pw.equals(p)){
            TextView Message1;
            Message1 = (TextView) findViewById(R.id.pwerror2);
            Message1.setVisibility(View.INVISIBLE);
            flag1[2]=true;
        }
        else{
            TextView Message1;
            Message1 = (TextView) findViewById(R.id.pwerror2);
            Message1.setVisibility(View.VISIBLE);
            flag1[2]=false;
        }
    }
    public void checkName(String n){

    }
    public void dialogChoice() {
        final String items[] = {"建筑学院","经济管理学院","土木水利学院","公共管理学院","环境学院","马克思主义学院","人文学院","机械工程学院","社会科学学院","信息科学技术学院","法学院","新闻与传播学院","五道口金融学院","材料学院","美术学院","工程物理系","化学工程系","核能与新能源技术研究院","理学院","体育部","艺术教育中心","生命科学学院","医学院","药学院","交叉信息研究院","苏世明书院","新雅书院"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("选择院系");
//        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(AccountSetting.this, items[which],Toast.LENGTH_SHORT).show();
                college=items[which];
                collegeText.setText(college);
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(AccountSetting.this, "确定", Toast.LENGTH_SHORT).show();
                changeDp sendDepartment = new changeDp();
                sendDepartment.start();
                try{
                    sendDepartment.join();
                }catch (Exception e){
                    e.printStackTrace();
                }
                dialog.dismiss();
                if(changeDepartmentFlag==true){
                    //将更改后的标签写入本地文件
                    try{
                        JSONObject UserInformation = new JSONObject(GetData("UserInformation.txt"));
                        UserInformation.put("Department",college);
                        WriteToFile("UserInformation.txt", UserInformation.toString());
                        TextView Message1;
                        Message1 = (TextView) findViewById(R.id.changeCol);
                        Message1.setVisibility(View.VISIBLE);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.create().show();

    }

    public class changePw extends Thread{
        public void run(){
            try {
                JSONObject Json = new JSONObject();
                Json.put("Username", name);
                Json.put("Password", oldpassword);
                Json.put("NewPassword", newpassword);

                String content = String.valueOf(Json);  //Json格式转成字符串来传输

                URL url = new URL("https://iknow.gycis.me:8443/updateData/changePassword");  //不同的请求发送到不同的URL地址，见群里的“后端网页名字设计.docx”
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

                Log.i("Connection", String.valueOf(connection.getResponseCode()));

                if (connection.getResponseCode() == 200) {//如果ResponseCode=200说明和服务器连接正确
                    String result = StreamToString(connection.getInputStream());
                    Log.i("Connection", result);
                    if (result.equals("change password succeed")) {
                        changeFlag = true;
                    } else {
                        changeFlag = false;
                    }
                } else {
                    Log.i("Connection", "Fail");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public class changeDp extends Thread{
        public void run(){
            try {
                JSONObject Json = new JSONObject();
                Json.put("Username", name);
                Json.put("NewDepartment", college);

                String content = String.valueOf(Json);  //Json格式转成字符串来传输

                URL url = new URL("https://iknow.gycis.me:8443/updateData/changeDepartment");  //不同的请求发送到不同的URL地址，见群里的“后端网页名字设计.docx”
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
                Log.i("Connection", String.valueOf(connection.getResponseCode()));
                if (connection.getResponseCode() == 200) {//如果ResponseCode=200说明和服务器连接正确
                    String result = StreamToString(connection.getInputStream());
                    Log.i("Connection", result);
                    if (result.equals("change department succeed")) {
                        changeDepartmentFlag = true;
                    } else {
                        changeDepartmentFlag = false;
                    }
                }
                else {
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
    public void WriteToFile(String filename, String content){
        try{
            File file = new File(Environment.getExternalStorageDirectory(),filename);
            if(!file.exists())
                file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            os.write(content.getBytes());
            os.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public class getDepartment extends Thread{
        public void run(){
            try {
                JSONObject Json = new JSONObject();  //把数据存成Json格式
                Json.put("Username", name);
                String content = String.valueOf(Json);  //Json格式转成字符串来传输
                URL url = new URL("https://iknow.gycis.me/downloadData/getDepartment");  //不同的请求发送到不同的URL地址，见群里的“后端网页名字设计.docx”
                HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
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
                    college = thisJson.getString("Department");
                }
                else{
                    Log.i("Connection", "Fail");
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}