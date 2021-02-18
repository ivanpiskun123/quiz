package com.example.quizz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class QuizActivity extends AppCompatActivity {

    TextView textQuestionNumber, textTime, textTimeLabel, textQuestion, textQuizTitle;
    Button btnNext;
    RadioButton rb1, rb2, rb3;
    RadioGroup rg;

    private AdView mAdView;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    int answeredQuestionsCount;

    Boolean[] resultsAnswers = {false,false,false,false,false,false};

    LocalTime startQuizTime;
    LocalTime endQuizTime;

    final String[] rAnswer = {""};

    private void clearAnswersResult(){
        for(int i=0; i<6; ++i)
            resultsAnswers[i] = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        textQuizTitle = findViewById(R.id.textQuizTitle);
        textQuestion = findViewById(R.id.textQuestion);
        textQuestionNumber = findViewById(R.id.textQnumber2);
        textQuestionNumber.setText("1");
        textTime = findViewById(R.id.textTime2);
        textTimeLabel = findViewById(R.id.textTime);

        textTime.setVisibility(View.INVISIBLE);
        textTimeLabel.setVisibility(View.INVISIBLE);

        btnNext = findViewById(R.id.btnNext);
        btnNext.setText("NEXT");
        rb1 = findViewById(R.id.rbOne);
        rb2 = findViewById(R.id.rbTwo);
        rb3 = findViewById(R.id.rbThree);
        rg = findViewById(R.id.rgAnswers);

        String theme = getIntent().getStringExtra("QUIZ_THEME").toLowerCase();
        textQuizTitle.setText(theme.substring(0,1).toUpperCase()+theme.substring(1));


        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        answeredQuestionsCount = 0;
        clearAnswersResult();


         showNewQuestion(0);
        //startQuizTime = java.time.LocalTime.now();

//
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRB = rg.getCheckedRadioButtonId();
                if( selectedRB == -1 )
                {
                    Toast.makeText(QuizActivity.this, "Nothing is selected.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if (answeredQuestionsCount != 5)
                    {
                        answeredQuestionsCount++;

                        if( ((RadioButton)findViewById(selectedRB)).getText().equals(rAnswer[0])) {
                            Toast.makeText(QuizActivity.this, "Right", Toast.LENGTH_SHORT).show();
                            showNewQuestion(answeredQuestionsCount);
                            resultsAnswers[answeredQuestionsCount - 1] = true;
                        }
                        else{
                            Toast.makeText(QuizActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                            showNewQuestion(answeredQuestionsCount);
                        }

                        textQuestionNumber.setText(String.valueOf(answeredQuestionsCount+1));

                        if(answeredQuestionsCount == 5)
                            btnNext.setText("Finish Quiz");
                    }
                    else
                    {
                        answeredQuestionsCount++;

                        if( ((RadioButton)findViewById(selectedRB)).getText().equals(rAnswer[0])) {
                            Toast.makeText(QuizActivity.this, "Right", Toast.LENGTH_SHORT).show();
                            resultsAnswers[answeredQuestionsCount - 1] = true;
                        }
                        else{
                            Toast.makeText(QuizActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                        }
                        sendRecord();

                        Intent intent = new Intent(getBaseContext(),  QuizResultActivity.class);
                        intent.putExtra("RIGHT", String.valueOf(calculateRightAnswers()));
                        startActivity(intent);


                    }


                }

            }
        });





    }
    private int calculateRightAnswers(){
        int nRight = 0;
        for(int j=0; j < 6; ++j){
            if(resultsAnswers[j])
                nRight++;
        }
        return nRight;
    }


    private void sendRecord(){
        Map<String, Object> record = new HashMap<>();
        record.put("Theme", getIntent().getStringExtra("QUIZ_THEME"));

      int nRight = calculateRightAnswers();

        record.put("record", String.valueOf(nRight));
        record.put("email", fAuth.getCurrentUser().getEmail().toString());

        fStore.collection("Records").add(record).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(QuizActivity.this, "Quiz passed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNewQuestion(int numberQ){
        DocumentReference documentReference = fStore.collection("Questions_"+textQuizTitle.getText().toString()).document(String.valueOf(numberQ));
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                textQuestion.setText( value.getString("Text"));
                rb1.setText(value.getString("ans1"));
                rb2.setText(value.getString("ans2"));
                rb3.setText(value.getString("ans3"));
                rAnswer[0] = value.getString("rAnswer");
            }
        });
    }

}
