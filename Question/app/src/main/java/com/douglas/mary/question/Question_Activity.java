package com.douglas.mary.question;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Question_Activity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {


   List<Questions> questionsList = new ArrayList<Questions>();
    private MySingleton mySingleton;
    private ImageButton nextButton;
    EditText editText;
    SeekBar seekBar;
    TextView textViewQuestion,min,max,percent;
    RadioGroup radG;
    Button btnSubmit;
    int questionIndex = 0;
    String value1="";
    String timeDateFile;
    String timeDateBTN;
    SimpleDateFormat sdf;
    Calendar c;
    String timeStart;
    String timeFinish;
    String answer="";
    Answer myAnswer;
    String tagBTN;
    Pattern pattern;
    Matcher matcher;
    public static List<Questions> questionsArrLable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_questions);
        mySingleton=(MySingleton)getApplication();
        timeDateFile=mySingleton.timeDateCreateFile;

        if (getIntent().getExtras()!=null){
            Intent intent=getIntent();
            value1 = intent.getStringExtra("nameQuestion");
            timeDateBTN=intent.getStringExtra("timeDateBTN");
           tagBTN=intent.getStringExtra("tagBTN");
            fillQuestion();

        }
       initialize();
        displayQ();

    }


    private void fillQuestion() {
        for (Questions questions : mySingleton.getListCSV() ){
            if (questions.getNameQuestion().equals(value1))
                questionsList.add(questions);
        }

    }



    private void fillAnswer(){

        questionsArrLable=new ArrayList<Questions>();
        myAnswer=new Answer();

        myAnswer.add(value1);
        myAnswer.add(timeDateBTN);
        myAnswer.add(tagBTN);
        myAnswer.add(questionsList.get(questionIndex).getLable());
        myAnswer.add(timeStart);
        myAnswer.add("");

        //====================================
        boolean existe=false;

        for (int i = 0; i < mySingleton.getListCSV().size(); i++) {
            Questions temp = new Questions();
            temp = mySingleton.getListCSV().get(i);
            existe = false;

            for (int j = 0; j < questionsArrLable.size() && !existe; j++) {
                if (questionsArrLable.get(j).getLable().trim().equalsIgnoreCase(temp.getLable().trim())) {
                    existe = true;
                }
            }
            if (!existe) {
                questionsArrLable.add(temp);
            }
        }


        for (int i = 0; i < questionsArrLable.size(); i++) {
            myAnswer.add("");
        }

    }

//=============================== method for displaying qetquestion============

    private void displayQ() {
        c = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeStart = sdf.format(c.getTime());

        answer="";
        getAnswers().isEmpty();
        radG=(RadioGroup)findViewById(R.id.radG);
        radG.clearCheck();
        editText.setText("");
        textViewQuestion.setText(questionsList.get(questionIndex).getQuestion().replace("|", ","));
        textViewQuestion.setTextSize(18);
        textViewQuestion.setTypeface(null, Typeface.BOLD);

        if (questionsList.size() - 1 > questionIndex)
            nextButton.setVisibility(View.VISIBLE);

        if (questionsList.size()-1 == questionIndex ) {
            btnSubmit.setVisibility(View.VISIBLE);
        }

        if (questionsList.get(questionIndex).getType().equalsIgnoreCase("Time")) {

            editText.setVisibility(View.VISIBLE);
            editText.setHint("Enter Time( 23:59)");
            editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
            editText.setTextSize(18);
            return;

        }
        if (questionsList.get(questionIndex).getType().equalsIgnoreCase("Date")) {

            editText.setVisibility(View.VISIBLE);
            editText.setHint("Enter Date( YYYY-MM-DD)");
            editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
            editText.setTextSize(18);
            return;
        }
        if (questionsList.get(questionIndex).getType().equalsIgnoreCase("Text")) {

            editText.setVisibility(View.VISIBLE);
            editText.setHint("Incident relié à la fatigue");
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextSize(18);
            return;
        }

        if (questionsList.get(questionIndex).getType().equalsIgnoreCase("Integer")) {

            editText.setVisibility(View.VISIBLE);
            editText.setHint("Enter Integer Number");
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            editText.setTextSize(18);
            return;
        }


        if (questionsList.get(questionIndex).getType().equalsIgnoreCase("Decimal")) {

            editText.setVisibility(View.VISIBLE);
            editText.setHint("Enter Decimal Number");
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText.setTextSize(18);
            editText.setGravity(1);
            return;
        }

        if (questionsList.get(questionIndex).getType().equalsIgnoreCase("VAS")) {

            //================================================GET ANSWERING FOR VAS ===============

            String[] tokens = questionsList.get(questionIndex).getAnswer().split(";");
            seekBar.setVisibility(View.VISIBLE);
            min.setVisibility(View.VISIBLE);
            max.setVisibility(View.VISIBLE);
            percent.setVisibility(View.VISIBLE);

            if (tokens[0] != "" && tokens[1] != "") {
                min.setText(tokens[0]);
                max.setText(tokens[1]);
            } else {
                min.setText("");
                max.setText("");
            }

            seekBar.setProgress(50);
            return;

        }

        //================================================GET QNSWERING FOR SELECT ONE ===============
        if (questionsList.get(questionIndex).getType().equalsIgnoreCase("Select One")) {

            radG.setVisibility(View.VISIBLE);
            String[] tokens = questionsList.get(questionIndex).getAnswer().split(";");

            for (int k = 0; k < tokens.length; k++) {

                RadioButton rad = new RadioButton(this);
                rad.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                rad.setText(tokens[k].replace("|", ","));
                rad.setId(k);
                radG.addView(rad);

            }
            return;
        }

//=========================== END OF Select One ==============================================

    }

    //============================================ INVISIBLE ALL ======================
    private void inVisibleAll() {

        editText.setVisibility(View.INVISIBLE);
        seekBar.setProgress(0);
        seekBar.setVisibility(View.INVISIBLE);
        min.setVisibility(View.INVISIBLE);
        max.setVisibility(View.INVISIBLE);
        percent.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        btnSubmit.setVisibility(View.INVISIBLE);
        radG.setVisibility(View.INVISIBLE);
        radG.removeAllViews();
    }
//==============================================================

    private void initialize() {

        nextButton = (ImageButton) findViewById(R.id.next);
        editText = (EditText) findViewById(R.id.editText);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
        min = (TextView) findViewById(R.id.min);
        max = (TextView) findViewById(R.id.max);
        percent = (TextView) findViewById(R.id.percend);
        radG=(RadioGroup)findViewById(R.id.radG);
        btnSubmit=(Button)findViewById(R.id.btnSubmit) ;
        inVisibleAll();
        seekBar.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) this);
        nextButton.setOnClickListener((View.OnClickListener) this);
        btnSubmit.setOnClickListener(this);

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        percent.setText(String.valueOf(progress));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }





    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.next:
                fillAnswer();

              if (questionsList.get(questionIndex).getNecessary().trim().equalsIgnoreCase("1")) {

                    if (getAnswers().isEmpty()){

                        Toast.makeText(this,"Please ask the question!",Toast.LENGTH_SHORT).show();
                        answer="";
                        getAnswers().isEmpty();
                        radG=(RadioGroup)findViewById(R.id.radG);
                        radG.clearCheck();
                        editText.setText("");
                        questionIndex=questionIndex;
                        inVisibleAll();

                        if (questionsList.size()-1 > questionIndex)
                            nextButton.setVisibility(View.VISIBLE);

                        displayQ();

                        break;
                    }
                    else {

                        if (questionsList.get(questionIndex).getType().trim().equalsIgnoreCase("Time")) {

                            if (!validateTime(getAnswers().toString())){
                                Toast.makeText(this,"Please enter correct time!",Toast.LENGTH_LONG).show();
                                answer="";
                                getAnswers().isEmpty();
                                radG=(RadioGroup)findViewById(R.id.radG);
                                radG.clearCheck();
                                editText.setText("");
                                questionIndex=questionIndex;
                                inVisibleAll();

                                if (questionsList.size()-1 > questionIndex)
                                    nextButton.setVisibility(View.VISIBLE);

                                displayQ();

                                break;
                            }
                            else if (validateTime(getAnswers().toString())){
                                for (int i = 0; i < questionsArrLable.size(); i++) {
                                    if (myAnswer.getAnswer(3).equals(questionsArrLable.get(i).getLable())) {

                                        myAnswer.setAnswer(i + 6, getAnswers());

                                    }

                                }
                                c = Calendar.getInstance();
                                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                timeFinish = sdf.format(c.getTime());
                                myAnswer.setAnswer(5, timeFinish);
                                mySingleton.addAnswer(myAnswer);
                                mySingleton.write_file(myAnswer);
                                answer = "";
                                getAnswers().isEmpty();
                                radG = (RadioGroup) findViewById(R.id.radG);
                                radG.clearCheck();
                                editText.setText("");
                                questionIndex++;
                                inVisibleAll();
                                if (questionsList.size() - 1 > questionIndex)
                                    nextButton.setVisibility(View.VISIBLE);
                                displayQ();

                                break;
                            }
                            break;
                        } else {
                            for (int i = 0; i < questionsArrLable.size(); i++) {
                                if (myAnswer.getAnswer(3).equals(questionsArrLable.get(i).getLable())) {
                                    myAnswer.setAnswer(i + 6, getAnswers());

                                }

                            }
                            c = Calendar.getInstance();
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            timeFinish = sdf.format(c.getTime());
                            myAnswer.setAnswer(5, timeFinish);
                            mySingleton.addAnswer(myAnswer);
                            mySingleton.write_file(myAnswer);
                            answer = "";
                            getAnswers().isEmpty();
                            radG = (RadioGroup) findViewById(R.id.radG);
                            radG.clearCheck();
                            editText.setText("");
                            questionIndex++;
                            inVisibleAll();
                            if (questionsList.size() - 1 > questionIndex)
                                nextButton.setVisibility(View.VISIBLE);
                            displayQ();

                            break;
                        }
                    }
                }

                if (questionsList.get(questionIndex).getNecessary().trim().equalsIgnoreCase("0")){

                    if (questionsList.get(questionIndex).getType().trim().equalsIgnoreCase("Time") && !getAnswers().isEmpty()) {

                        if (!validateTime(getAnswers().toString())){
                            Toast.makeText(this,"Please enter correct time!",Toast.LENGTH_LONG).show();
                            answer="";
                            getAnswers().isEmpty();
                            radG=(RadioGroup)findViewById(R.id.radG);
                            radG.clearCheck();
                            editText.setText("");
                            questionIndex=questionIndex;
                            inVisibleAll();

                            if (questionsList.size()-1 > questionIndex)
                                nextButton.setVisibility(View.VISIBLE);
                            displayQ();

                            break;
                        }
                        else if (validateTime(getAnswers().toString())){
                            for (int i = 0; i < questionsArrLable.size(); i++) {
                                if (myAnswer.getAnswer(3).equals(questionsArrLable.get(i).getLable())) {
                                    myAnswer.setAnswer(i + 6, getAnswers());
                                }

                            }
                            c = Calendar.getInstance();
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            timeFinish = sdf.format(c.getTime());
                            myAnswer.setAnswer(5, timeFinish);
                            mySingleton.addAnswer(myAnswer);
                            mySingleton.write_file(myAnswer);
                            answer = "";
                            getAnswers().isEmpty();
                            radG = (RadioGroup) findViewById(R.id.radG);
                            radG.clearCheck();
                            editText.setText("");
                            questionIndex++;
                            inVisibleAll();
                            if (questionsList.size() - 1 > questionIndex)
                                nextButton.setVisibility(View.VISIBLE);
                            displayQ();

                            break;
                        }
                        break;
                    } else {

                        for (int i = 0; i < questionsArrLable.size(); i++) {
                            if (myAnswer.getAnswer(3).equals(questionsArrLable.get(i).getLable())) {
                                myAnswer.setAnswer(i + 6, getAnswers());

                            }

                        }
                        c = Calendar.getInstance();
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        timeFinish = sdf.format(c.getTime());
                        myAnswer.setAnswer(5, timeFinish);
                        mySingleton.addAnswer(myAnswer);
                        mySingleton.write_file(myAnswer);
                        questionIndex++;

                        inVisibleAll();
                        if (questionsList.size() - 1 > questionIndex)
                            nextButton.setVisibility(View.VISIBLE);
                        editText.setText("");
                        radG.clearCheck();
                        displayQ();
                        break;
                    }
               }


            case R.id.btnSubmit:

                fillAnswer();
                if (questionsList.get(questionIndex).getNecessary().trim().equalsIgnoreCase("1")) {

                    if (getAnswers().isEmpty() || getAnswers().equals("0")){
                        Toast.makeText(this,"Please ask the question!",Toast.LENGTH_SHORT).show();
                        questionIndex=questionIndex;
                        inVisibleAll();

                        if (questionsList.size()-1 > questionIndex)
                            nextButton.setVisibility(View.VISIBLE);
                        editText.setText("");
                        radG.clearCheck();
                        displayQ();

                        break;
                    }
                    else {

                        if (questionsList.get(questionIndex).getType().trim().equalsIgnoreCase("Time")) {

                            if (!validateTime(getAnswers().toString())) {
                                Toast.makeText(this,"Please ask the question!",Toast.LENGTH_SHORT).show();
                                questionIndex=questionIndex;
                                inVisibleAll();
                                if (questionsList.size()-1 > questionIndex)
                                    nextButton.setVisibility(View.VISIBLE);
                                editText.setText("");
                                radG.clearCheck();
                                displayQ();

                                break;
                            } else {
                                for (int i = 0; i < questionsArrLable.size(); i++) {
                                    if (myAnswer.getAnswer(3).equals(questionsArrLable.get(i).getLable())) {
                                        myAnswer.setAnswer(i + 6, getAnswers());
                                    }

                                }
                                c = Calendar.getInstance();
                                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                timeFinish = sdf.format(c.getTime());
                                myAnswer.setAnswer(5, timeFinish);
                                mySingleton.addAnswer(myAnswer);
                                mySingleton.write_file(myAnswer);
                                editText.setText("");
                                radG.clearCheck();
                                getAnswers().isEmpty();
                                Intent intent2 = new Intent(Question_Activity.this, MainActivity.class);
                                startActivity(intent2);
                                break;
                            }

                        } else {

                            for (int i = 0; i < questionsArrLable.size(); i++) {
                                if (myAnswer.getAnswer(3).equals(questionsArrLable.get(i).getLable())) {
                                    myAnswer.setAnswer(i + 6, getAnswers());
                                }

                            }
                            c = Calendar.getInstance();
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            timeFinish = sdf.format(c.getTime());
                            myAnswer.setAnswer(5, timeFinish);
                            mySingleton.addAnswer(myAnswer);
                            mySingleton.write_file(myAnswer);

                            editText.setText("");
                            radG.clearCheck();
                            getAnswers().isEmpty();
                            Intent intent2 = new Intent(Question_Activity.this, MainActivity.class);
                            startActivity(intent2);
                            break;
                        }

                    }

                }

                if (questionsList.get(questionIndex).getNecessary().trim().equalsIgnoreCase("0")) {

                    if (questionsList.get(questionIndex).getType().trim().equalsIgnoreCase("Time") && !getAnswers().isEmpty()) {

                        if (!validateTime(getAnswers().toString())) {
                            Toast.makeText(this,"Please ask the question!",Toast.LENGTH_SHORT).show();
                            questionIndex=questionIndex;
                            inVisibleAll();
                            if (questionsList.size()-1 > questionIndex)
                                nextButton.setVisibility(View.VISIBLE);
                            editText.setText("");
                            radG.clearCheck();
                            displayQ();

                            break;
                        } else {
                            for (int i = 0; i < questionsArrLable.size(); i++) {
                                if (myAnswer.getAnswer(3).equals(questionsArrLable.get(i).getLable())) {
                                    myAnswer.setAnswer(i + 6, getAnswers());
                                }

                            }
                            c = Calendar.getInstance();
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            timeFinish = sdf.format(c.getTime());
                            myAnswer.setAnswer(5, timeFinish);
                            mySingleton.addAnswer(myAnswer);
                            mySingleton.write_file(myAnswer);
                            editText.setText("");
                            radG.clearCheck();
                            Intent intent2 = new Intent(Question_Activity.this, MainActivity.class);
                            startActivity(intent2);
                            break;
                        }

                    } else {
                        for (int i = 0; i < questionsArrLable.size(); i++) {
                            if (myAnswer.getAnswer(3).equals(questionsArrLable.get(i).getLable())) {
                                myAnswer.setAnswer(i + 6, getAnswers());
                            }
                        }
                        c = Calendar.getInstance();
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        timeFinish = sdf.format(c.getTime());
                        myAnswer.setAnswer(5, timeFinish);
                        mySingleton.addAnswer(myAnswer);
                        mySingleton.write_file(myAnswer);

                        editText.setText("");
                        radG.clearCheck();
                        Intent intent2 = new Intent(Question_Activity.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    }
                }
        }
    }
//===================== Method for Validation TIME =================
    private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    public boolean validateTime(String time) {
        pattern = Pattern.compile(TIME24HOURS_PATTERN);
        matcher = pattern.matcher(time);
        return matcher.matches();
    }
//=================================================================
    public String getAnswers() {

        if (questionsList.get(questionIndex).getType().equalsIgnoreCase("Date") ||
                    questionsList.get(questionIndex).getType().equalsIgnoreCase("Text") ||
                    questionsList.get(questionIndex).getType().equalsIgnoreCase("Integer") ||
                    questionsList.get(questionIndex).getType().equalsIgnoreCase("Decimal")) {
                answer = editText.getText().toString();
              return answer;
            }
        if (questionsList.get(questionIndex).getType().equalsIgnoreCase("Time")) {
            answer = editText.getText().toString();

        }
            if (questionsList.get(questionIndex).getType().equalsIgnoreCase("VAS")) {
              //  answer = String.valueOf(seekBar.getProgress());
                answer=percent.getText().toString();
            return answer;
            }
            if (questionsList.get(questionIndex).getType().equalsIgnoreCase("Select One")) {

                int selectedRadioButtonID = radG.getCheckedRadioButtonId();
                // If nothing is selected from Radio Group, then it return -1
                if (selectedRadioButtonID != -1) {
                    answer = String.valueOf(((RadioButton) findViewById(selectedRadioButtonID)).getText());
                    return answer;
                }

            }
        return answer;
    }
//==================================== End =============
}
