package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SearchPage extends AppCompatActivity {

    private static String[] testHistory = {"科研","创新","研究生","智能汽车大赛",
            "佐贺偶像是传奇","口腔喷剂","悲惨世界","简明物理化学","魂","1024节",
            "环境学院","神奇口袋","KDA","1001","すバらしい","大学物理","充电台灯",
            "毕业剧","philips","Python"};
    private static String[] labelNames = {"科创","计算机","体育","实践","外语","经济","创业","文学","电影","志愿",
            "艺术","讲座","学生节","展览","赛事","演出","全部标签"};
    List<Activity> acResult = new ArrayList<Activity>();
    Activity temp = new Activity();
    private LinearLayout historyView, resultView, switchView, cover, resultList;
    private SearchView browser;
    private Button cancel;
    private ListView mListView;
    private static String[] mStrs = {"AAA","BBB"} ;
    private int[] idCollector={R.id.firstDateSelect_btn,R.id.firstTimeSelect_btn,200};
    int i = 0;
    private final static int REQUESTCODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        browser = findViewById(R.id.browser);
        browser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                switchView.removeAllViews();
                View v = getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                historyView.removeAllViews();
                resultView.removeAllViews();
                resultList.removeAllViews();
                cover.removeAllViews();
                GetActivity(query);
                initSwitchView();
                initResultView();
                initResultList(acResult);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.clearTextFilter();
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });
        mListView = findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));
        mListView.setTextFilterEnabled(true);
        cancel = findViewById(R.id.CancelButton);
        cancel.setOnClickListener(returnButtonListener);
        historyView = findViewById(R.id.historyView);
        resultView = findViewById(R.id.resultView);
        switchView = findViewById(R.id.switchColumn);
        resultList = findViewById(R.id.result_list);
        cover = findViewById(R.id.cover);
        initHistoryView();
    }

    protected void GetActivity(final String s){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject Json = new JSONObject();  //把数据存成Json格式
                    Json.put("Keywords", s);
                    String content = String.valueOf(Json);  //Json格式转成字符串来传输

                    URL url = new URL("https://iknow.gycis.me/downloadData/getAcByKeywords");  //不同的请求发送到不同的URL地址，见群里的“后端网页名字设计.docx”
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
                        acResult.clear();
                        for(int i = 0; i < acNum; i++){
                            ac[i] = new JSONObject(acList.getString("Activity"+String.valueOf(i+1)));
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
                            acResult.add(temp);
                        }
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
    /**
     * 设置历史记录栏
     */
    private void initHistoryView(){
        LinearLayout historyCase = historyView;
        int size = testHistory.length;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 1, 10, 1);
        LinearLayout.LayoutParams historyBarLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        historyBarLayoutParams.setMargins(0, 0, 0, 0);
        ArrayList<Button> childBtns = new ArrayList<>();
        int totalBtns = 0;

        LinearLayout historyHintBar = new LinearLayout(this);
        historyHintBar.setOrientation(LinearLayout.HORIZONTAL);
        historyHintBar.setLayoutParams(historyBarLayoutParams);
        historyHintBar.setPadding(25,5,0,5);
        TextView searchHistory = new TextView(this);
        searchHistory.setTextColor(Color.BLACK);
        searchHistory.setTextSize(16);
        searchHistory.setText("搜索历史");
        historyHintBar.addView(searchHistory);
        historyCase.addView(historyHintBar);

        for(int i = 0; i < size; i++){
            String item = testHistory[i];
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int length= item.length();

            if(length < 4){
                itemParams.weight = 1;
                totalBtns++;
            }else if(length < 8){
                itemParams.weight = 2;
                totalBtns+=2;
            }else{
                itemParams.weight = 3;
                totalBtns+=3;
            }

            itemParams.width = 0;
            itemParams.setMargins(5, 5, 5, 5);
            Button childBtn = (Button) LayoutInflater.from(this).inflate(R.layout.history_button, null);
            childBtn.setText(item);
            childBtn.setOnClickListener(buttonOnClick);
            childBtn.setTag(item);
            childBtn.setId(100+i);//按钮编号100-119,监听，执行刷新页面及搜索操作
            childBtn.setLayoutParams(itemParams);
            childBtns.add(childBtn);

            if(totalBtns >= 5){
                LinearLayout  horizLL = new LinearLayout(this);
                horizLL.setOrientation(LinearLayout.HORIZONTAL);
                horizLL.setLayoutParams(layoutParams);

                for(Button addBtn:childBtns){
                    horizLL.addView(addBtn);
                }
                historyCase.addView(horizLL);
                childBtns.clear();
                totalBtns = 0;
            }
        }
        //调整最后一行的样式
        if(!childBtns.isEmpty()){
            LinearLayout horizLL = new LinearLayout(this);
            horizLL.setOrientation(LinearLayout.HORIZONTAL);
            horizLL.setLayoutParams(layoutParams);

            for(Button addBtn:childBtns){
                horizLL.addView(addBtn);
            }
            historyCase.addView(horizLL);
            childBtns.clear();
            totalBtns = 0;
        }
    }
    /**
     * 设置结果筛选栏
     */
    private void initResultView(){
        LinearLayout resultSelection = resultView;
        LinearLayout.LayoutParams resultColumnLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        resultColumnLayoutParams.setMargins(0, 5, 0, 5);
        LinearLayout.LayoutParams resultSelectButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        resultSelectButtonParams.weight = 1;

        Button leftButton = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
        Button middleButton = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
        Button rightButton = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
        leftButton.setText("日期");
        leftButton.setLayoutParams(resultSelectButtonParams);
        leftButton.setOnClickListener(resultSelectButtonListener);
        leftButton.setId(R.id.dateSelect_btn);
        middleButton.setText("时长");
        middleButton.setLayoutParams(resultSelectButtonParams);
        middleButton.setOnClickListener(resultSelectButtonListener);
        middleButton.setId(R.id.timeSelect_btn);
        rightButton.setText("标签");
        rightButton.setLayoutParams(resultSelectButtonParams);
        rightButton.setOnClickListener(resultSelectButtonListener);
        rightButton.setId(R.id.classificaitonSelect_btn);
        resultSelection.addView(leftButton);
        resultSelection.addView(middleButton);
        resultSelection.addView(rightButton);
    }
    /**
     * 设置结果栏
     * @param activities
     */
    private void initResultList(List<Activity> activities){
        //利用input的值进行搜索算法实现
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        //System.out.println(actList[0].getStartMinute());
        if (acResult == null){
            System.out.println("没东西");
            resultList.addView(null);
        }
        int num = activities.size();//事件数目
        LinearLayout activityList = resultList;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); // 每行的水平LinearLayout
        layoutParams.setMargins(0, 0, 0, 0);
        String circleButtonText = "";
        String barButtonText = "";
        activityList.setOrientation(LinearLayout.VERTICAL);
        i = 0;
        for(Activity ac : activities) {
            circleButtonText = ac.getStartHour() + "\n————\n" + ac.getEndHour();
            barButtonText = ac.getIntroduction().substring(0, 14) + "...|" + ac.getMainLabel() + "\n\n" + ac.getHost() + "\t\t" + ac.getPlace();
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
            circleBtn.setLayoutParams(circleButtonParams);
            barBtn.setLayoutParams(barButtonParams);
            barBtn.setOnClickListener(activityDetailListener);
            barBtn.setId(800+i);
            GradientDrawable gradientDrawable1 = (GradientDrawable) circleBtn.getBackground();
            GradientDrawable gradientDrawable2 = (GradientDrawable) barBtn.getBackground();
            if(ac.getMainLabel().equals("科创")) {
                gradientDrawable1.setColor(android.graphics.Color.rgb(218,112,214));
                gradientDrawable2.setColor(android.graphics.Color.rgb(218,112,214));
            }
            else if(ac.getMainLabel().equals("计算机")) {
                gradientDrawable1.setColor(android.graphics.Color.rgb(0,191,255));
                gradientDrawable2.setColor(android.graphics.Color.rgb(0,191,255));
            }
            else if(ac.getMainLabel().equals("体育")) {
                gradientDrawable1.setColor(android.graphics.Color.rgb(50,205,50));
                gradientDrawable2.setColor(android.graphics.Color.rgb(50,205,50));
            }
            else if(ac.getMainLabel().equals("文艺")) {
                gradientDrawable1.setColor(android.graphics.Color.rgb(240,230,140));
                gradientDrawable2.setColor(android.graphics.Color.rgb(240,230,140));
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
    /**
     * 设置转换页面栏
     */
    private void initSwitchView(){
        LinearLayout switchCase = switchView;
        LinearLayout.LayoutParams switchButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        switchButtonParams.weight = 1;
        Button privateButton = (Button) LayoutInflater.from(this).inflate(R.layout.private_button, null);
        Button publicButton = (Button) LayoutInflater.from(this).inflate(R.layout.public_button, null);
        Button settingButton = (Button) LayoutInflater.from(this).inflate(R.layout.setting_button, null);
        privateButton.setOnClickListener(switchButtonListener);
        publicButton.setOnClickListener(switchButtonListener);
        settingButton.setOnClickListener(switchButtonListener);
        privateButton.setLayoutParams(switchButtonParams);
        publicButton.setLayoutParams(switchButtonParams);
        settingButton.setLayoutParams(switchButtonParams);
        switchCase.addView(privateButton);
        switchCase.addView(publicButton);
        switchCase.addView(settingButton);
    }
    /**
     * 设置 shape 的颜色
     * @param num
     * @param id
     */
    private void generateCover(int num, int id){
        LinearLayout coverBox = cover;
        if(num == 1){
            LinearLayout.LayoutParams switchColumnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams switchButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            switchButtonParams.weight = 1;
            coverBox.setBackgroundColor(Color.BLACK);
            coverBox.getBackground().setAlpha(160);
            LinearLayout switchColumn = new LinearLayout(this);
            switchColumn.setLayoutParams(switchColumnParams);
            Button firstButton = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
            Button secondButton = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
            Button thirdButton = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
            Button forthButton = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
            firstButton.setText("全部日期");
            firstButton.setLayoutParams(switchButtonParams);
            firstButton.setOnClickListener(resultSelectButtonListener);
            firstButton.setId(R.id.firstDateSelect_btn);
            secondButton.setText("0-7天");
            secondButton.setLayoutParams(switchButtonParams);
            secondButton.setOnClickListener(resultSelectButtonListener);
            secondButton.setId(R.id.secondDateSelect_btn);
            thirdButton.setText("8-15天");
            thirdButton.setLayoutParams(switchButtonParams);
            thirdButton.setOnClickListener(resultSelectButtonListener);
            thirdButton.setId(R.id.thirdDateSelect_btn);
            forthButton.setText("16-30天");
            forthButton.setLayoutParams(switchButtonParams);
            forthButton.setOnClickListener(resultSelectButtonListener);
            forthButton.setId(R.id.forthDateSelect_btn);
            switch(id){
                case R.id.firstDateSelect_btn:
                    firstButton.setBackgroundColor(Color.rgb(237,189,101));
                    break;
                case R.id.secondDateSelect_btn:
                    secondButton.setBackgroundColor(Color.rgb(237,189,101));
                    break;
                case R.id.thirdDateSelect_btn:
                    thirdButton.setBackgroundColor(Color.rgb(237,189,101));
                    break;
                case R.id.forthDateSelect_btn:
                    forthButton.setBackgroundColor(Color.rgb(237,189,101));
                    break;
            }
            switchColumn.addView(firstButton);
            switchColumn.addView(secondButton);
            switchColumn.addView(thirdButton);
            switchColumn.addView(forthButton);
            coverBox.addView(switchColumn);
            Button coverButton = new Button(this);
            coverButton.setLayoutParams(switchButtonParams);
            coverButton.setBackgroundColor(Color.BLACK);
            coverButton.getBackground().setAlpha(0);
            coverButton.setId(R.id.blank_btn);
            coverButton.setOnClickListener(resultSelectButtonListener);
            coverBox.addView(coverButton);
        }
        else if(num == 2){
            LinearLayout.LayoutParams switchColumnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams switchButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            switchButtonParams.weight = 1;
            coverBox.setBackgroundColor(Color.BLACK);
            coverBox.getBackground().setAlpha(160);
            LinearLayout switchColumn = new LinearLayout(this);
            switchColumn.setLayoutParams(switchColumnParams);
            Button firstButton = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
            Button secondButton = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
            Button thirdButton = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
            Button forthButton = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
            firstButton.setText("全部时长");
            firstButton.setLayoutParams(switchButtonParams);
            firstButton.setOnClickListener(resultSelectButtonListener);
            firstButton.setId(R.id.firstTimeSelect_btn);
            secondButton.setText("0-1小时");
            secondButton.setLayoutParams(switchButtonParams);
            secondButton.setOnClickListener(resultSelectButtonListener);
            secondButton.setId(R.id.secondTimeSelect_btn);
            thirdButton.setText("1-3小时");
            thirdButton.setLayoutParams(switchButtonParams);
            thirdButton.setOnClickListener(resultSelectButtonListener);
            thirdButton.setId(R.id.thirdTimeSelect_btn);
            forthButton.setText("3小时以上");
            forthButton.setLayoutParams(switchButtonParams);
            forthButton.setOnClickListener(resultSelectButtonListener);
            forthButton.setId(R.id.forthTimeSelect_btn);
            switch(id){
                case R.id.firstTimeSelect_btn:
                    firstButton.setBackgroundColor(Color.rgb(237,189,101));
                    break;
                case R.id.secondTimeSelect_btn:
                    secondButton.setBackgroundColor(Color.rgb(237,189,101));
                    break;
                case R.id.thirdTimeSelect_btn:
                    thirdButton.setBackgroundColor(Color.rgb(237,189,101));
                    break;
                case R.id.forthTimeSelect_btn:
                    forthButton.setBackgroundColor(Color.rgb(237,189,101));
                    break;
            }
            switchColumn.addView(firstButton);
            switchColumn.addView(secondButton);
            switchColumn.addView(thirdButton);
            switchColumn.addView(forthButton);
            coverBox.addView(switchColumn);
            Button coverButton = new Button(this);
            coverButton.setLayoutParams(switchButtonParams);
            coverButton.setBackgroundColor(Color.BLACK);
            coverButton.getBackground().setAlpha(0);
            coverButton.setId(R.id.blank_btn);
            coverButton.setOnClickListener(resultSelectButtonListener);
            coverBox.addView(coverButton);
        }
        else if(num == 3){
            LinearLayout.LayoutParams switchColumnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams switchButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            switchButtonParams.weight = 1;
            coverBox.setBackgroundColor(Color.BLACK);
            coverBox.getBackground().setAlpha(160);
            int size = labelNames.length;
            ArrayList<Button> labels = new ArrayList<>();
            int totalLabels = 0;

            for(int i = 0; i < size; i++){
                String item = labelNames[i];
                LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                itemParams.weight = 1;
                Button label = (Button) LayoutInflater.from(this).inflate(R.layout.result_select_button, null);
                label.setText(item);
                label.setOnClickListener(resultSelectButtonListener);
                label.setId(200+i);//按钮编号200-215,监听tag事件
                if(label.getId()==id){
                    label.setBackgroundColor(Color.rgb(237,189,101));
                }
                label.setLayoutParams(itemParams);
                labels.add(label);
                totalLabels++;
                if(totalLabels >= 4){
                    LinearLayout  labelColumn = new LinearLayout(this);
                    labelColumn.setOrientation(LinearLayout.HORIZONTAL);
                    labelColumn.setLayoutParams(switchColumnParams);
                    labelColumn.setBackgroundColor(Color.WHITE);
                    for(Button addBtn:labels){
                        labelColumn.addView(addBtn);
                    }
                    coverBox.addView(labelColumn);
                    labels.clear();
                    totalLabels = 0;
                }
            }
            //调整最后一行的样式
            if(!labels.isEmpty()){
                LinearLayout horizLL = new LinearLayout(this);
                horizLL.setOrientation(LinearLayout.HORIZONTAL);
                horizLL.setLayoutParams(switchColumnParams);
                for(Button addBtn:labels){
                    horizLL.addView(addBtn);
                }
                coverBox.addView(horizLL);
                labels.clear();
            }
            Button coverButton = new Button(this);
            coverButton.setLayoutParams(switchButtonParams);
            coverButton.setBackgroundColor(Color.BLACK);
            coverButton.getBackground().setAlpha(0);
            coverButton.setId(R.id.blank_btn);
            coverButton.setOnClickListener(resultSelectButtonListener);
            coverBox.addView(coverButton);
        }
    }


    Button.OnClickListener returnButtonListener = new Button.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(SearchPage.this, PublicPage.class);
            startActivity(intent);
            SearchPage.this.finish();
        }
    };
    Button.OnClickListener buttonOnClick = new Button.OnClickListener() {
        public void onClick(View v) {
            int n = v.getId();
            //执行搜索算法
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            resultView.removeAllViews();
            historyView.removeAllViews();
            initSwitchView();
            initResultView();
            GetActivity(testHistory[n-100]);
            initResultList(acResult);
        }
    };
    Button.OnClickListener switchButtonListener = new Button.OnClickListener() {
        public void onClick(View v) {
            if(v.getId()==R.id.private_btn) {
                Intent intent = new Intent(SearchPage.this, PrivatePage.class);
                startActivity(intent);
                SearchPage.this.finish();
                //现在是跳转到public，之后合并再说
            }
            else if(v.getId()==R.id.public_btn){
                Intent intent = new Intent(SearchPage.this, PublicPage.class);
                startActivity(intent);
                SearchPage.this.finish();
            }
            else if(v.getId()==R.id.setting_btn){
                Intent intent = new Intent(SearchPage.this, SettingPage.class);
                startActivity(intent);
                SearchPage.this.finish();
                //现在是跳转到public，之后合并再说
            }
        }
    };
    Button.OnClickListener resultSelectButtonListener = new Button.OnClickListener() {
        public void onClick(View v) {
            int a = v.getId();
            if(v.getId()==R.id.dateSelect_btn) {
                generateCover(1,idCollector[0]);

            }
            else if(v.getId()==R.id.timeSelect_btn){
                generateCover(2,idCollector[1]);
            }
            else if(v.getId()==R.id.classificaitonSelect_btn){
                generateCover(3,idCollector[2]);
            }
            else if(v.getId()==R.id.firstDateSelect_btn){
                cover.removeAllViews();
                cover.getBackground().setAlpha(0);
                idCollector[0] = R.id.firstDateSelect_btn;
            }
            else if(v.getId()==R.id.secondDateSelect_btn){
                cover.removeAllViews();
                cover.getBackground().setAlpha(0);
                idCollector[0] = R.id.secondDateSelect_btn;
            }
            else if(v.getId()==R.id.thirdDateSelect_btn){
                cover.removeAllViews();
                cover.getBackground().setAlpha(0);
                idCollector[0] = R.id.thirdDateSelect_btn;
            }
            else if(v.getId()==R.id.forthDateSelect_btn){
                cover.removeAllViews();
                cover.getBackground().setAlpha(0);
                idCollector[0] = R.id.forthDateSelect_btn;
            }
            else if(v.getId()==R.id.firstTimeSelect_btn){
                cover.removeAllViews();
                cover.getBackground().setAlpha(0);
                idCollector[1] = R.id.firstTimeSelect_btn;
            }
            else if(v.getId()==R.id.secondTimeSelect_btn){
                cover.removeAllViews();
                cover.getBackground().setAlpha(0);
                idCollector[1] = R.id.secondTimeSelect_btn;
            }
            else if(v.getId()==R.id.thirdTimeSelect_btn){
                cover.removeAllViews();
                cover.getBackground().setAlpha(0);
                idCollector[1] = R.id.thirdTimeSelect_btn;
            }
            else if(v.getId()==R.id.forthTimeSelect_btn){
                cover.removeAllViews();
                cover.getBackground().setAlpha(0);
                idCollector[1] = R.id.forthTimeSelect_btn;
            }
            else if(a<=216 && a>=200) {
                cover.removeAllViews();
                cover.getBackground().setAlpha(0);
                idCollector[2] = v.getId();
            }
            else {
                cover.removeAllViews();
                cover.getBackground().setAlpha(0);
            }
        }
    };
    Button.OnClickListener activityDetailListener = new Button.OnClickListener(){
        public void onClick(View v){
            int n = v.getId();
            int no = acResult.get(n-800).getActivityId();
            Intent intent = new Intent(SearchPage.this, ResultDetailPage.class);
            intent.putExtra("ActivityNum",no);
            startActivityForResult(intent,REQUESTCODE);
        }
    };
}
