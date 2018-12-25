package com.example.test;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;


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


//事件的标签设置页面，上一页面为“发布消息”页面
public class LabelForEvent extends AppCompatActivity {
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;
    public static final int RESULT_FIRST_USER = 1;

    //定义一个布尔数组存储主题标签的选中状态
    boolean[] mainLabel = new boolean[11];
    //定义一个布尔数组存储副标签的选中状态
    boolean[] subLabel = new boolean[11];
    //定义一个布尔数组存储活动形式标签的选中状态
    boolean[] activityLabel = new boolean[5];
    //计数所选主题标签数目，副标签数目，活动形式标签数目并初始化为0
    int mainCount = 0, subCount = 0, activityCount = 0;
    private String MainLabel="-",SecondLabel="-",ThemeLabel="-";
    Boolean flag1=false,flag2=false,flag3=false,savelabelFlag=false;
    //定义数组存储各个按钮的id
    Integer[] ButtonId = new Integer[]{
            R.id.Button_Main_Sci,R.id.Button_Main_Computer,R.id.Button_Main_PE,R.id.Button_Main_Practice,R.id.Button_Main_English,R.id.Button_Main_Economic,
            R.id.Button_Main_Chuangye,R.id.Button_Main_Literature,R.id.Button_Main_Movie,R.id.Button_Main_Volunteer,R.id.Button_Main_Art,
            R.id.Button_Sub_Sci,R.id.Button_Sub_Computer,R.id.Button_Sub_PE,R.id.Button_Sub_Practice,R.id.Button_Sub_English,R.id.Button_Sub_Economic,
            R.id.Button_Sub_Chuangye,R.id.Button_Sub_Literature,R.id.Button_Sub_Movie,R.id.Button_Sub_Volunteer,R.id.Button_Sub_Art,
            R.id.Button_Activity_Lecture,R.id.Button_Activity_Xsj,R.id.Button_Activity_Exhibition,R.id.Button_Activity_Competition,R.id.Button_Activity_Performance
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
    //定义标签内容数组
    String[] LabelName = new String[]{
            "科创","计算机","体育","实践","外语","经济","创业","文学","电影","志愿","艺术","讲座","学生节","展览","赛事","演出"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_for_event);

        //每次打开标签设置页面加载标签
        refreshLabel();
    }

    /*    *//*---------------------------------------------------------*//*
    //初始化标签选中状态，所有标签均为false
    //在新建一个事件的函数里应当有对标签的初始化，此处用于测试
    *//*---------------------------------------------------------*//*
    private void initLabel(View view)
    {
        for(int i=0;i<11;i++)
        {
            TypeLabel[i] = false;
        }
        for(int i=0;i<5;i++)
        {
            ActivityLabel[i] = false;
        }
    }
    *//*---------------------------------------------------------*//*
    //初始化标签选中状态，所有标签均为false
    //在新建一个事件的函数里应当有对标签的初始化，此处用于测试
    *//*---------------------------------------------------------*/

    //设置好标签之后再次打开标签页面时，直接加载已经存储好了的标签
    public void refreshLabel()
    {
        ImageButton LabelBtn;
        //刷新显示主标题：i=0~10
        for(int i=0;i<11;i++)
        {
            LabelBtn = (ImageButton)findViewById(ButtonId[i]);

            //UserLabel[i]==true 代表该标签被选中
            if(mainLabel[i])
            {
                LabelBtn.setImageDrawable(getDrawable(choosedImage[i]));
            }

            //UserLabel[i]==false 代表该标签未被选中
            else
            {
                LabelBtn.setImageDrawable(getDrawable(unchoosedImage[i]));
            }
        }
        //刷新显示副标题：i=11~21
        for(int i=11;i<21;i++)
        {
            LabelBtn = (ImageButton)findViewById(ButtonId[i]);

            //UserLabel[i]==true 代表该标签被选中
            if(subLabel[i-11])
            {
                LabelBtn.setImageDrawable(getDrawable(choosedImage[i-11]));
            }

            //UserLabel[i]==false 代表该标签未被选中
            else
            {
                LabelBtn.setImageDrawable(getDrawable(unchoosedImage[i-11]));
            }
        }
        //刷新显示活动形式标签：i=22~26
        for(int i=22;i<27;i++)
        {
            LabelBtn = (ImageButton)findViewById(ButtonId[i]);

            //activityLabel[i]==true 代表该标签被选中
            if(activityLabel[i-22])
            {
                LabelBtn.setImageDrawable(getDrawable(choosedImage[i-11]));
            }

            //UserLabel[i]==false 代表该标签未被选中
            else
            {
                LabelBtn.setImageDrawable(getDrawable(unchoosedImage[i-11]));
            }
        }
    }

    //监听Button
    public void setEventLabel(View view) {
        TextView message;
        message = (TextView)findViewById(R.id.TextSetEventLabelSuccessful);
        message.setVisibility(View.INVISIBLE);
        for(int i=0;i<27;i++) {
            if(view.getId() == ButtonId[i]){ //得到是第几个标签按钮被点击
                int clickNum = i;

                //主题标签
                if(clickNum < 11 && (!subLabel[clickNum])){ //主题标签被点击
                    if(mainCount < 1) {//此刻还未选择主题标签,有点击应该是添加主题标签
                        mainCount++;
                        mainLabel[clickNum] = !mainLabel[clickNum];
                        ImageButton Btn = (ImageButton)findViewById(ButtonId[clickNum]);
                        Btn.setImageDrawable(getDrawable(choosedImage[clickNum]));
                    }
                    else{  //此刻已有主题标签被选择，那么应该无法添加更多主题标签,只能删除已选主题标签
                        if(mainLabel[clickNum]){//所点击的是已经被选中的标签
                            mainLabel[clickNum] = !mainLabel[clickNum];
                            mainCount--;
                            ImageButton Btn = (ImageButton)findViewById(ButtonId[clickNum]);
                            Btn.setImageDrawable(getDrawable(unchoosedImage[clickNum]));
                        }
                        else{//所点击的是未被选中的标签，则删除原来选中的标签，选中这个点击的标签
                            for(int k = 0; k < 11; k++){//获得之前已经选中的标签的序号
                                if(mainLabel[k]){
                                    mainLabel[k] = !mainLabel[k];
                                    ImageButton formerBtn = (ImageButton)findViewById(ButtonId[k]);
                                    formerBtn.setImageDrawable(getDrawable(unchoosedImage[k]));
                                }
                            }
                            mainLabel[clickNum] = !mainLabel[clickNum];
                            ImageButton Btn = (ImageButton)findViewById(ButtonId[clickNum]);
                            Btn.setImageDrawable(getDrawable(choosedImage[clickNum]));
                        }
                    }
                }

                //副标签
                if(clickNum > 10 && clickNum < 22 && (!mainLabel[clickNum - 11])) //副标签被点击且该标签未在主标签里选择
                {
                    ImageButton Btn = (ImageButton)findViewById(ButtonId[clickNum]);
                    int subNum = clickNum - 11;//在副标签数组里的序号
                    if(subCount < 1){ //此刻还未选择副标签,有点击应该是添加标签
                        subCount++;
                        subLabel[subNum] = !subLabel[subNum];
                        Btn.setImageDrawable(getDrawable(choosedImage[subNum]));
                    }
                    else{  //此刻已有副标签被选择，那么应该无法添加更多副标签,只能删除已选副标签
                        if(subLabel[subNum]){//所点击的是已经被选中的标签
                            subLabel[subNum] = !subLabel[subNum];
                            subCount--;
                            ImageButton formerBtn = (ImageButton)findViewById(ButtonId[clickNum]);
                            formerBtn.setImageDrawable(getDrawable(unchoosedImage[subNum]));
                        }
                        else{//所点击的是未被选中的标签，则删除原来选中的标签，选中这个点击的标签
                            for(int k = 0; k < 11; k++){//获得之前已经选中的标签的序号,并将其图标换为未被点中状态
                                if(subLabel[k]){
                                    subLabel[k] = !subLabel[k];
                                    ImageButton formerBtn = (ImageButton)findViewById(ButtonId[k+11]);
                                    formerBtn.setImageDrawable(getDrawable(unchoosedImage[k]));
                                }
                            }
                            subLabel[subNum] = !subLabel[subNum];
                            Btn = (ImageButton)findViewById(ButtonId[clickNum]);
                            Btn.setImageDrawable(getDrawable(choosedImage[subNum]));
                        }
                    }
                }

                //活动形式标签
                if(clickNum > 21 && clickNum < 27 ) //活动形式标签被点击
                {
                    ImageButton Btn = (ImageButton)findViewById(ButtonId[clickNum]);
                    int activityNum = clickNum - 22;
                    if(activityCount < 1) //此刻还未选择活动形式标签,有点击应该是添加标签
                    {
                        activityCount++;
                        activityLabel[activityNum] = !activityLabel[activityNum];
                        Btn.setImageDrawable(getDrawable(choosedImage[activityNum + 11]));
                    }
                    else{  //此刻已有活动标签被选择，那么应该无法添加更多活动标签,只能删除已选活动标签
                        if(activityLabel[activityNum]){//所点击的是已经被选中的标签
                            activityLabel[activityNum] = !subLabel[activityNum];
                            activityCount--;
                            ImageButton formerBtn = (ImageButton)findViewById(ButtonId[clickNum]);
                            formerBtn.setImageDrawable(getDrawable(unchoosedImage[activityNum]));
                        }
                        else{//所点击的是未被选中的标签，则删除原来选中的标签，选中这个点击的标签
                            for(int k = 0; k < 5; k++){//获得之前已经选中的标签的序号,并将其图标换为未被点中状态
                                if(activityLabel[k]){
                                    activityLabel[k] = !activityLabel[k];
                                    ImageButton formerBtn = (ImageButton)findViewById(ButtonId[k+22]);
                                    formerBtn.setImageDrawable(getDrawable(unchoosedImage[k+11]));
                                }
                            }
                            activityLabel[activityNum] = !activityLabel[activityNum];
                            Btn = (ImageButton)findViewById(ButtonId[clickNum]);
                            Btn.setImageDrawable(getDrawable(choosedImage[activityNum+11]));
                        }
                    }
                }


            }
        }
    }

    public void saveLabel(View view)
    {
        //经过一段判断,是否三个标签都选择成功
        for (int i=0;i<11;i++){ //检查主标签是否被选中
            if(mainLabel[i]==true){
                MainLabel=LabelName[i];
                flag1=true;
            }
        }
        for(int i=0;i<11;i++){
            if(subLabel[i]==true){
                SecondLabel=LabelName[i];
                flag2=true;
            }
        }
        for(int i=0;i<5;i++){
            if(activityLabel[i]==true){
                ThemeLabel=LabelName[i+11];
                flag3=true;
            }
        }
        Intent intent=new Intent(LabelForEvent.this,CreateActivity.class);
        //佳妮的传回数据
        /*if(flag1==true & flag2==true & flag3==true){  //三类标签都选择成功,上传数据
            savelabelFlag=true;
            intent.putExtra("MainLabel",MainLabel);
            intent.putExtra("SecondLabel",SecondLabel);
            intent.putExtra("ThemeLabel",ThemeLabel);
            TextView Message;
            Message = (TextView) findViewById(R.id.TextSetEventLabelSuccessful);
            Message.setVisibility(View.VISIBLE);
            LabelForEvent.this.setResult(RESULT_OK, intent);
            //关闭Activity
            //LabelForEvent.this.finish();
        }*/

        if(flag1==true){  //主标签都选择成功,上传数据
            savelabelFlag=true;
            intent.putExtra("MainLabel",MainLabel);
            intent.putExtra("SecondLabel",SecondLabel);
            intent.putExtra("ThemeLabel",ThemeLabel);
            TextView Message;
            Message = (TextView) findViewById(R.id.TextSetEventLabelSuccessful);
            Message.setText("标签设置成功！");
            Message.setVisibility(View.VISIBLE);
            LabelForEvent.this.setResult(RESULT_OK, intent);
            //关闭Activity
            //LabelForEvent.this.finish();
        }
        else{
            TextView Message;
            Message = (TextView) findViewById(R.id.TextSetEventLabelSuccessful);
            Message.setText("请选择主题标签！");
            Message.setVisibility(View.VISIBLE);
        }
//        else{
//            intent.putExtra("MainLabel","NULL");
//            intent.putExtra("SecondLabel","NULL");
//            intent.putExtra("ThemeLabel","NULL");
//
//            TextView Message;
//            Message = (TextView) findViewById(R.id.TextSetEventLabelSuccessful);
//            Message.setText("请选择合适的标签");
//            Message.setVisibility(View.VISIBLE);
//            startActivity(intent);
//        }

       /* Timer timer;//定时器
        timer = new Timer();
        long time = 3000;
        timer.schedule(new timerTask(),3000);*/
    }

    //监测哪个标签被点击

    /*public void SciClicked(View view)
    {
        ImageButton SciBtn = (ImageButton)findViewById(R.id.Button_Sci);
        if(TypeLabel[0])
        {
            SciBtn.setImageDrawable(getDrawable(button_sci));
        }
        else
        {
            SciBtn.setImageDrawable(getDrawable(button_sci_del));
        }
        TypeLabel[0]=!TypeLabel[0];
    }*/


    //科创标签被点击

    public void goBack(View view)
    {
        if(savelabelFlag==false){Intent intent=new Intent(LabelForEvent.this,CreateActivity.class);
            intent.putExtra("MainLabel","-");
            intent.putExtra("SecondLabel","-");
            intent.putExtra("ThemeLabel","-");
            LabelForEvent.this.setResult(RESULT_OK, intent);}

        finish();
    }
}

