package com.example.test;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

public class AccountSetting extends AppCompatActivity {

    public ImageButton backBtn,changeNameBtn,changeCollegeBtn;
    public Button changePasswordBtn;
    public EditText nameText,collegeText,passwordText1,passwordText2,passwordText3;
    private Boolean[] flag1={true,true,true}; //用于记录是否所有的事件都通过检查
    private String oldpassword,newpassword,confirmpassword;
    //private User  定义一个用户类，首先从服务器调取其信息，显示在网页上
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        //首先从服务器调取用户的信息
        //user=...
        backBtn=(ImageButton)findViewById(R.id.backtoSetting);
        changePasswordBtn=(Button)findViewById(R.id.changePassword);
        //changeNameBtn=(ImageButton)findViewById(R.id.changeName);
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
/*        changeNameBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                nameText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        String name1=nameText.getText().toString(); //设置listener
                        checkName(name1);
                    }
                });
            }
        });*/
        changeCollegeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                collegeText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        String college1=collegeText.getText().toString(); //设置listener
                        checkCollege(college1);
                    }
                });
            }
        });
        //修改密码
        changePasswordBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                oldpassword=passwordText1.getText().toString();
                //if(oldpassword==user.password)
                newpassword=passwordText2.getText().toString();
                checkpassword1(newpassword);
                confirmpassword=passwordText3.getText().toString();
                checkpassword2(confirmpassword);
                //else{显示第一个错误信息}
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
            newpassword=p;
            flag1[1]=true;
        }
        else{ //返回错误
            TextView Message1;
            Message1 = (TextView)findViewById(R.id.pwerrpr1);
            Message1.setVisibility(View.VISIBLE);
            flag1[1]=false;
        }
    }
    public void checkpassword2(String pw){
        if(pw.isEmpty()==false & pw==newpassword){
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

}
