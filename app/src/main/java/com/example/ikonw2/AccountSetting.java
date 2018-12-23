package com.example.ikonw2;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AccountSetting extends AppCompatActivity {
    public ImageButton backBtn,changeNameBtn,changeCollegeBtn;
    public Button changePasswordBtn;
    private Spinner DepartmentSpinner;
    private EditText nameText, collegeText, passwordText1, passwordText2, passwordText3;
    private Boolean[] flag1={true,true,true}; //用于记录是否所有的事件都通过检查
    public String oldpassword,newpassword,confirmpassword;
    public Boolean Flag1=true;
    public Boolean changeFlag=true;
    private List<String> DepartmentChoice(){
        List<String> data = new ArrayList<>();
        data.add("请选择您的院系:");
        data.add("建筑学院"); data.add("经济管理学院"); data.add("土木水利学院"); data.add("公共管理学院"); data.add("环境学院");
        data.add("人文学院"); data.add("机械工程学院"); data.add("社会科学学院"); data.add("信息科学技术学院"); data.add("法学院");
        data.add("新闻与传播学院"); data.add("材料学院"); data.add("美术学院"); data.add("理学院"); data.add("生命科学学院"); data.add("医学院");
        return data;
    }
    //private User  定义一个用户类，首先从服务器调取其信息，显示在网页上
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountsetting);
//        DepartmentSpinner = findViewById(R.id.Department);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DepartmentChoice());
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        DepartmentSpinner.setAdapter(adapter);
        //首先从服务器调取用户的信息
        backBtn=(ImageButton)findViewById(R.id.backtoSetting);
        changePasswordBtn=(Button)findViewById(R.id.changePassword);
        changeCollegeBtn=(ImageButton)findViewById(R.id.changeCollege);
        nameText=(EditText)findViewById(R.id.editName);
        collegeText=(EditText)findViewById(R.id.editCollege);
        passwordText1=(EditText)findViewById(R.id.oldpassword);
        passwordText2=(EditText)findViewById(R.id.newpassword);
        passwordText3=(EditText)findViewById(R.id.confirmpassword);
        //显示用户原有的姓名和院系
        //name.setText(user.name);
        //college.setText(user.college);
        //修改用户名和院系

//        changeCollegeBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View V){
//                collegeText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    }
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        String college1=collegeText.getText().toString(); //设置listener
//                        checkCollege(college1);
//                    }
//                });
//            }
//        });

        //修改密码
        changePasswordBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View V){
                oldpassword=passwordText1.getText().toString();
                newpassword = passwordText2.getText().toString();
                confirmpassword= passwordText3.getText().toString();
                System.out.println(oldpassword);
                System.out.println(newpassword);
                System.out.println(confirmpassword);

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
                    Message1=(TextView)findViewById(R.id.after);
                    Message1.setText("修改密码失败！");
                    Message1.setVisibility(View.VISIBLE);
                }
            }
        });
        //修改密码要检测
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    public void checkCollege(String c){

    }

    public class changePw extends Thread{
            public void run(){
                try {
                    JSONObject Json = new JSONObject();
                    Json.put("Username", "q");
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