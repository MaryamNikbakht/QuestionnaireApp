package com.douglas.mary.question;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity{

    private MySingleton mySingleton;
    ArrayList<Questions> questionsArray;
    public static Questions temp=new Questions();


    int h=1;
    String timeDateBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    public void createButton(String temp){

        LinearLayout layout = (LinearLayout ) findViewById(R.id.layoutFirstPage);
        final Button btn = new Button(this);
        btn.setText(temp);
        LinearLayout.LayoutParams paramsQ =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        paramsQ.setMargins(10,10,10,10);
        btn.setLayoutParams(paramsQ);
      //  btn.setBackgroundColor(Color.parseColor("#58BCF0"));
        btn.setTag(h);

        btn.setTextSize(18);
        btn.setGravity(Gravity.CENTER);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting time date for every button
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timeDateBTN = sdf.format(c.getTime());
                Log.d("**** timeDateBTN **>> ", timeDateBTN.toString());
                String string=btn.getText().toString();
                Intent intent = new Intent(MainActivity.this, Question_Activity.class);

                intent.putExtra("nameQuestion",string);
                intent.putExtra("timeDateBTN", timeDateBTN);
                intent.putExtra("tagBTN",  btn.getTag().toString());
                startActivity(intent);
            }
        });

        layout.addView(btn);
        h++;
    }

    private void initialize() {
        mySingleton=(MySingleton)getApplication();
        questionsArray=new ArrayList<Questions>();
        int haveit=-1, index=0;

        questionsArray.add(mySingleton.getQuestions(0));

       for (Questions question :  mySingleton.getListCSV()  ) {
            haveit=-1;
            for (Questions question2 : questionsArray){
                if(question.getNameQuestion().trim().equals(question2.getNameQuestion().trim())){
                    haveit=1;
                    break;
                }
            }
            if (haveit==-1)
                questionsArray.add(question);
        }

        for (Questions question : questionsArray){
            createButton(question.getNameQuestion());
        }




    }

//=================================== END ====================
}
