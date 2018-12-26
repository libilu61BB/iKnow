package com.example.test;

import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import org.json.JSONObject;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginPage extends AppCompatActivity {
    Button btn_login,btn_register;
    EditText Username,Password;
    TextView LoginUE, LoginPE;
    String ThisUsername;
    User Me=new User();
    boolean UsernameSucceedFlag = true;
    boolean PasswordSucceedFlag = true;
    private MySql mysql;

    class User{
        //存储用户的用户名、密码
        public String Username, Password;
        public void SetInformation(String Username, String Password){
            this.Username = Username;
            this.Password = Password;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        LoginUE = findViewById(R.id.LoginUsernameError);
        LoginPE = findViewById(R.id.LoginPasswordError);
        btn_login=findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String username = Username.getText().toString();
                final String password = Password.getText().toString();
                LoginUE.setVisibility(View.INVISIBLE);
                LoginPE.setVisibility(View.INVISIBLE);
                UsernameSucceedFlag = true;
                PasswordSucceedFlag = true;
                Me.SetInformation(username, password);
                Login login = new Login();
                login.start();
                try{
                    login.join();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if (UsernameSucceedFlag == false) {
                    LoginUE.setVisibility(View.VISIBLE);
                }else if(PasswordSucceedFlag == false){
                    LoginPE.setVisibility(View.VISIBLE);
                }else{
                    ThisUsername = Me.Username;
                    GetInfo getinfo = new GetInfo();
                    getinfo.start();
                    try{
                        getinfo.join();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(LoginPage.this, PrivatePage.class);
                    startActivity(intent);
                    LoginPage.this.finish();
                }
            }
        });
        btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    protected class Login extends Thread{
        public void run() {
            try{
                JSONObject Json = new JSONObject();
                Json.put("Username", Me.Username);
                Json.put("Password", Me.Password);
                String content = String.valueOf(Json);

                URL url = new URL("https://iknow.gycis.me:8443");
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
                    if(result.equals("username is not existed"))
                        UsernameSucceedFlag = false;
                    else if(result.equals("password is wrong"))
                        PasswordSucceedFlag = false;
                }
                else{
                    Log.i("Connection", "Fail");
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class GetInfo extends Thread{
        public void run(){
            try{
                JSONObject Json = new JSONObject();
                Json.put("Username", ThisUsername);
                String content = String.valueOf(Json);
                URL url = new URL("https://iknow.gycis.me:8443/downloadData/loginDownload");
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
                    JSONObject UserInfo = new JSONObject(result);

                    JSONObject User = new JSONObject(UserInfo.getString("User"));
                    int ActivityNumber = UserInfo.getInt("ActivityNumber");
                    JSONObject Activity = new JSONObject(UserInfo.getString("Activity"));
                    Log.i("Connection", User.toString());
                    Log.i("Connection", String.valueOf(ActivityNumber));
                    Log.i("Connection", Activity.toString());

                    CreateUserInformation(User);
                    CreateDatabase(ActivityNumber, Activity);
                }
                else{
                    Log.i("Connection", "Fail");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void CreateDatabase(int ActivityNumber, JSONObject Activity){
        mysql = new MySql(this);
        try{
            for(int i = 0; i < ActivityNumber; i++){
                JSONObject json = new JSONObject(Activity.getString("Activity" + String.valueOf(i+1)));
                mysql.Insert(json.getInt("Id"), json.getInt("Year"), json.getInt("Month"), json.getInt("Day"),
                        json.getInt("Start_hour"), json.getInt("Start_minute"), json.getInt("End_hour"), json.getInt("End_minute"),
                        json.getString("Introduction"), json.getString("Tag1"), json.getString("Tag2"), json.getString("Tag3"),
                        json.getString("Address"), json.getString("Holder"), json.getString("Url"), json.getString("Name"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void CreateUserInformation(JSONObject User){
        try{
            JSONObject UserInformation = new JSONObject();
            char[] tag = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                    '0', '0', '0', '0',};
            for(int i = 0; i < 10; i++){
                String temp = User.getString("Tag" + String.valueOf(i+1));
                switch(temp){
                    case "科创":
                        tag[0] = '1'; break;
                    case "计算机":
                        tag[1] = '1'; break;
                    case "体育":
                        tag[2] = '1'; break;
                    case "实践":
                        tag[3] = '1'; break;
                    case "外语":
                        tag[4] = '1'; break;
                    case "经济":
                        tag[5] = '1'; break;
                    case "创业":
                        tag[6] = '1'; break;
                    case "文学":
                        tag[7] = '1'; break;
                    case "电影":
                        tag[8] = '1'; break;
                    case "志愿":
                        tag[9] = '1'; break;
                    case "艺术":
                        tag[10] = '1'; break;
                    case "讲座":
                        tag[11] = '1'; break;
                    case "学生节":
                        tag[12] = '1'; break;
                    case "展览":
                        tag[13] = '1'; break;
                    case "赛事":
                        tag[14] = '1'; break;
                    case "演出":
                        tag[15] = '1'; break;
                }
            }
            String Tag = String.valueOf(tag);
            UserInformation.put("Username", ThisUsername);
            UserInformation.put("Department", User.getString("Department"));
            UserInformation.put("Tag", Tag);
            WriteToFile("UserInformation.txt", UserInformation.toString());
        }catch(Exception e){
            e.printStackTrace();
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

    public String StreamToString(InputStream is) {
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