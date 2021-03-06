package com.example.test;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.view.View.VISIBLE;
import static com.example.test.R.drawable.button_art;
import static com.example.test.R.drawable.button_art_del;
import static com.example.test.R.drawable.button_chuangye;
import static com.example.test.R.drawable.button_chuangye_del;
import static com.example.test.R.drawable.button_competition;
import static com.example.test.R.drawable.button_competition_del;
import static com.example.test.R.drawable.button_computer;
import static com.example.test.R.drawable.button_computer_del;
import static com.example.test.R.drawable.button_economic;
import static com.example.test.R.drawable.button_economic_del;
import static com.example.test.R.drawable.button_english;
import static com.example.test.R.drawable.button_english_del;
import static com.example.test.R.drawable.button_exhibition;
import static com.example.test.R.drawable.button_exhibition_del;
import static com.example.test.R.drawable.button_lecture;
import static com.example.test.R.drawable.button_lecture_del;
import static com.example.test.R.drawable.button_literature;
import static com.example.test.R.drawable.button_literature_del;
import static com.example.test.R.drawable.button_movie;
import static com.example.test.R.drawable.button_movie_del;
import static com.example.test.R.drawable.button_pe;
import static com.example.test.R.drawable.button_pe_del;
import static com.example.test.R.drawable.button_performance;
import static com.example.test.R.drawable.button_performance_del;
import static com.example.test.R.drawable.button_practice;
import static com.example.test.R.drawable.button_practice_del;
import static com.example.test.R.drawable.button_sci;
import static com.example.test.R.drawable.button_sci_del;
import static com.example.test.R.drawable.button_volunteer;
import static com.example.test.R.drawable.button_volunteer_del;
import static com.example.test.R.drawable.button_xsj;
import static com.example.test.R.drawable.button_xsj_del;

public class testUserLabel extends AppCompatActivity {

    private boolean setLabelSucceedFlag = false;

    String UserName;
    int LabelNum = 16;

    //定义一个布尔数组存储标签的选中状态:0~10储存活动主题标签，11~15储存活动形式标签
    boolean[] UserLabel = new boolean[LabelNum];


    //定义一个LabelCount计数选择了的标签数
    int UserLabelCount = 0;

    //定义标签内容数组
    String[] LabelName = new String[]{
            "科创","计算机","体育","实践","外语","经济","创业","文学","电影","志愿","艺术","讲座","学生节","展览","赛事","演出"
    };

    //定义一个数组存储各个标签的id
    Integer[] ButtonId = new Integer[]{
            R.id.Button_Sci,R.id.Button_Computer,R.id.Button_PE,R.id.Button_Practice,R.id.Button_English,R.id.Button_Economic,
            R.id.Button_Chuangye,R.id.Button_Literature,R.id.Button_Movie,R.id.Button_Volunteer,R.id.Button_Art,
            R.id.Button_Lecture,R.id.Button_Xsj,R.id.Button_Exhibition,R.id.Button_Competition,R.id.Button_Performance
    };

    //定义数组存储选中状态下的标签图片id
    Integer[] choosedImage = new Integer[]{
            button_sci_del,button_computer_del,button_pe_del,button_practice_del,button_english_del,button_economic_del,
            button_chuangye_del,button_literature_del,button_movie_del,button_volunteer_del,button_art_del,
            button_lecture_del,button_xsj_del,button_exhibition_del,button_competition_del,button_performance_del
    };

    //定义数组存储选中状态下的标签图片id
    Integer[] unchoosedImage = new Integer[]{
            button_sci,button_computer,button_pe,button_practice,button_english,button_economic,
            button_chuangye,button_literature,button_movie,button_volunteer,button_art,
            button_lecture,button_xsj,button_exhibition,button_competition,button_performance
    };

    //定义一个可变长度数组存储用户所选标签的序号
    ArrayList<Integer> choosedLabelId = new ArrayList<>();

    TextView Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_user_label);
        File file = new File(Environment.getExternalStorageDirectory(),"testUserLabel.txt");

        JSONObject Json = new JSONObject();
        try{
            if (!file.exists()){
                file.createNewFile();
                Json.put("Username","李碧璐");
                Json.put("testTag1","0000000000000000");
                Json.put("testTag2","1000000000000000");
                Json.put("testTag3","0100000000000000");
                Json.put("testTag4","0010000000000000");
                Json.put("testTag5","0001000000000000");
                Json.put("testTag6","0000100000000000");
                Json.put("testTag7","0000010000000000");
                Json.put("testTag8","0000001000000000");
                Json.put("testTag9","0000000100000000");
                Json.put("testTag10","0000000010000000");
                Json.put("testTag11","0000000001000000");
                Json.put("testTag12","0000000000100000");
                Json.put("testTag13","0000000000010000");
                Json.put("testTag14","0000000000001000");
                Json.put("testTag15","0000000000000100");
                Json.put("testTag16","0000000000000010");
                Json.put("testTag17","0000000000000001");
                Json.put("testTag18","1111111111111111");

               // String content = String.valueOf(Json);
                WriteToFile("testUserLabel.txt", Json.toString());
                /*FileOutputStream os = new FileOutputStream(file);
                os.write(content.getBytes());
                os.close();*/
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        getUserName();
        for(int k = 0;k < 18;k++){
        //获得本地文件中的Tag1~Tag16
        try{
            JSONObject data = new JSONObject(GetData("testUserLabel.txt"));
            Log.i("Connection",data.toString());
            String Tag = data.getString("testTag"+String.valueOf(k+1));
            for(int num = 0;num < 16; num ++){
                char a = Tag.charAt(num);
                if(a == '1'){
                    UserLabel[num] = true;
                    UserLabelCount ++;
                }
                else UserLabel[num] = false;
            }


        }catch(Exception e){
            e.printStackTrace();
        }

        ImageButton LabelBtn;
        for(int i=0;i<LabelNum;i++) {
            LabelBtn = (ImageButton)findViewById(ButtonId[i]);

            //UserLabel[i]==true 代表该标签被选中
            if(UserLabel[i]) {
                LabelBtn.setImageDrawable(getDrawable(choosedImage[i]));
            }

            //UserLabel[i]==false 代表该标签未被选中
            else {
                LabelBtn.setImageDrawable(getDrawable(unchoosedImage[i]));
            }
        }

            for(int i = 0;i < LabelNum; i ++){
                if(UserLabel[i]){
                    choosedLabelId.add(i);
                }
            }

            sendLabel sendUserLabel = new sendLabel();
            sendUserLabel.start();
            try{
                sendUserLabel.join();
            }catch (Exception e){
                e.printStackTrace();
            }
    }






    }





    //从本地文件里获得用户标签情况
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

    //获得用户名
    public void getUserName(){
        try{
            JSONObject ThisUser = new JSONObject(GetData("testUserLabel.txt"));
            UserName = ThisUser.getString("Username");
        }catch(Exception e){
            e.printStackTrace();
        }
    }




    //返回上一页面
    public void goBack(View view)
    {
        finish();
    }



    //后端数据传输函数
    public class sendLabel extends Thread{
        public void run(){
            try {
                //测试用户名
                int tagNum = 0;
                JSONObject Json = new JSONObject();  //把数据存成Json格式
                Json.put("Username", UserName);
                for(int i = 0;i < LabelNum; i++){
                    if(UserLabel[i]){
                        int num = tagNum + 1;
                        Json.put("Tag"+num, LabelName[i]);
                        tagNum ++;
                    }
                }
                for(int i = tagNum;i < 16;i++){
                    int num = i + 1;
                    Json.put("Tag"+num, "0");
                }

                String content = String.valueOf(Json);  //Json格式转成字符串来传输


                URL url = new URL("https://iknow.gycis.me:8443/updateData/changeTag");  //不同的请求发送到不同的URL地址，见群里的“后端网页名字设计.docx”
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

                Log.i("Connection", String.valueOf(connection.getResponseCode()));

                if (connection.getResponseCode() == 200) {//如果ResponseCode=200说明和服务器连接正确
                    String result = StreamToString(connection.getInputStream());
                    Log.i("Connection", result);
                    if(result.equals("change tag succeed")){
                        setLabelSucceedFlag = true;
                    }
                    else{
                        setLabelSucceedFlag = false;
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
}
