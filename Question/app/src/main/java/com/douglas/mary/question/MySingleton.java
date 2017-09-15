package com.douglas.mary.question;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by Mary on 2017-05-18.
 */

public class MySingleton extends Application{

    private List<Questions> listCSV=new ArrayList<>();
    private List<Answer> answerWrite = new ArrayList<>();
  //  List<String> qst = new ArrayList<>();

    Calendar c;
    String timeDateCreateFile;
    SimpleDateFormat sdf;

    public List<Answer> getAnswerWrite() { return answerWrite; }
    public void setAnswerWrite(List<Answer> answerWrite) { this.answerWrite = answerWrite;}

    public List<Questions> getListCSV() {
        return listCSV;
    }
    public void setListCSV(List<Questions> listCSV) {
        this.listCSV = listCSV;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        read_file();

     //   readDataCSV();
    }

    public void add(Questions questions) { listCSV.add(questions); }
    public Questions getQuestions(int index){
        return listCSV.get(index);
    }

    public void addAnswer(Answer answer) { answerWrite.add(answer); }
    public Answer getAnswer(int index){
        return answerWrite.get(index);
    }



    public String[] readTitel(){
         List<Questions> questionsArrLable=new ArrayList<Questions>();
        String[] qstList = new String[6+listCSV.size()];

        qstList[0]="Trigger";
        qstList[1]="Trigger_date_time";
        qstList[2]="Trigger_Tag";
        qstList[3]="Q_lable";
        qstList[4]="Q_start_date_time";
        qstList[5]="Q_finish_date_time";

        //====================================
        boolean existe=false;

        for (int i = 6; i < listCSV.size()+6; i++) {
            Questions temp = new Questions();
            temp = listCSV.get(i-6);

            existe = false;

            for (int j = 6; j < questionsArrLable.size()+6 && !existe; j++) {
                if (questionsArrLable.get(j-6).getLable().trim().equalsIgnoreCase(temp.getLable().trim())) {
                    existe = true;
                }
            }
            if (!existe) {

                questionsArrLable.add(temp);
            }
        }

        for (int i = 6; i < questionsArrLable.size()+6; i++) {
            qstList[i]=questionsArrLable.get(i-6).getLable();
        }
        return  qstList;
    }

    //============================ READ DATA FROM raw ( Internal Storage) =============
    public  void readDataCSV(){

        InputStream in= getResources().openRawResource(R.raw.questionsdouglas);

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("windows-1252")));
        String line="";
        try {
            while ((line = reader.readLine()) != null) {

                //split by ","
                String[] tokens= line.split(",");
                Questions qst= new Questions();
                if (tokens[0]!=""){
                    qst.setLable(tokens[0]);
                }else
                {
                    qst.setLable("");
                }
                if (tokens[1]!=""){
                    qst.setQuestion(tokens[1]);
                }else
                {
                    qst.setQuestion("");
                }
                if (tokens[2]!=""){
                    qst.setAnswer(tokens[2]);
                }else
                {
                    qst.setAnswer("");
                }
                if (tokens[3]!=""){
                    qst.setType(tokens[3]);
                }else
                {
                    qst.setType("");
                }
                if (tokens[4]!=""){
                    qst.setNameQuestion(tokens[4]);
                }else
                {
                    qst.setNameQuestion("");
                }
                if (tokens[5]!=""){
                    qst.setNecessary(tokens[5]);
                }else
                {
                    qst.setNecessary("");
                }

                add(qst);
            }
            in.close();
            reader.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    //============================== WRITE DATA IN External MEMORY( DOCUMENTS ) ==========================

    public  void write_file(Answer answer)  {
        String[] data;

        try {
         if(isExternalStorageAvailable()) {
                String my_file = "outputqst.csv";


                File root = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

                File outfile = new File(root, "Douglas");
                if (!outfile.exists()) {
                    outfile.mkdir();
                    Toast.makeText(getBaseContext(), (outfile.exists()) ? "True douglas": "False douglas",
                            Toast.LENGTH_SHORT).show();
                }
                File myFile = new File (outfile, my_file);

                FileWriter mFileWriter;
                CSVWriter writer;

                if (!myFile.exists()) {
                    c = Calendar.getInstance();
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    timeDateCreateFile = sdf.format(c.getTime());

                    myFile.createNewFile();
                    mFileWriter = new FileWriter(myFile , true);
                    writer = new CSVWriter(mFileWriter);

                           String[] dateCreate={timeDateCreateFile.toString()+" Company: Douglas Institute"};

                    writer.writeNext(dateCreate);
             writer.writeNext(readTitel());



                }
                else {
                    mFileWriter = new FileWriter(myFile , true);
                    writer = new CSVWriter(mFileWriter);

                }

                data= new String[answer.getAnswre().size()];

            for (int i = 0;i<answer.getAnswre().size();i++)
                data[i]= answer.getAnswer(i);

                writer.writeNext(data);
                writer.flush();
                writer.close();


         }
            else
            {
                Toast.makeText(this, "External storage not mounted",
                        Toast.LENGTH_LONG).show();
            }
        }     catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("FILE ", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //=================== /* Checks if external storage is available for read and write */========================
    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    //========================== READ DATA FROM internal memory??? FILE =======================================

    public  void read_file() {

        String FILE_NAME = "questionsdouglas.csv";
        if (isExternalStorageAvailable() ) {
           File baseDir =  getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            File file = new File(baseDir, FILE_NAME);

            String line = "";
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader bReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), "windows-1252"));
                while( (line = bReader.readLine()) != null  ){

                    //split by ","
                    String[] tokens= line.split(",");
                    Questions qst= new Questions();
                    if (tokens[0]!=""){
                        qst.setLable(tokens[0]);
                    }else
                    {
                        qst.setLable("");
                    }
                    if (tokens[1]!=""){
                        qst.setQuestion(tokens[1]);
                    }else
                    {
                        qst.setQuestion("");
                    }
                    if (tokens[2]!=""){
                        qst.setAnswer(tokens[2]);
                    }else
                    {
                        qst.setAnswer("");
                    }
                    if (tokens[3]!=""){
                        qst.setType(tokens[3]);
                    }else
                    {
                        qst.setType("");
                    }
                    if (tokens[4]!=""){
                        qst.setNameQuestion(tokens[4]);
                    }else
                    {
                        qst.setNameQuestion("");
                    }
                    if (tokens[5]!=""){
                        qst.setNecessary(tokens[5]);
                    }else
                    {
                        qst.setNecessary("");
                    }
                    add(qst);
                }
                bReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

//=========================================== END ===========================

}
