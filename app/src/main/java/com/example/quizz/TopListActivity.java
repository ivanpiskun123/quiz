package com.example.quizz;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopListActivity extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ListView listView;
    TextView textOne;
    TextView textBuf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        textBuf = findViewById(R.id.textBuf);

        final int[] i = {0};

        textBuf.setText("111");
        fStore.collection("Records").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                i[0] = i[0] + 1;
                                textBuf.setText(String.valueOf(i[0]));
                                new Record(document.getString("email"), Integer.parseInt(document.getString("record")), QuizThemes.valueOf(document.getString("Theme")));
                            }

                            Record.recordsList.sort(new Comparator<Record>(){
                                @Override
                                public int compare(Record o1, Record o2) {
                                    return o1.record < o2.record ? 1 : -1;
                                }
                            });
                        }
                        else
                        {
                        }
                    }
                });

        ListView listView = findViewById(R.id.listView);
        List<Record> topList = new ArrayList<Record>();

        for(int j = 0; j < 3; ++j){
            topList.add(Record.recordsList.get(j));
        }






    }
}