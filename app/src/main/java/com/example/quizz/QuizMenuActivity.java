package com.example.quizz;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class QuizMenuActivity extends AppCompatActivity {

    private StorageReference mStorageRef;

    ListView listView;
    ImageView litImg, attImg, phisImg, socImg;
    TextView textLit, textAtt, textPhis, textMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_menu);





        textLit = findViewById(R.id.textLit);
        textPhis= findViewById(R.id.textPhis);
        textAtt = findViewById(R.id.textAtt);

        String[] imagesStr = {"lit.jpeg",  "phis.jpeg", "dost.jpeg", "soc.jpeg"};
        ImageView[] imgViews = {litImg,phisImg, attImg , socImg};


//        try {
//            for (int n=0; n<4; ++n){
//
//                mStorageRef = FirebaseStorage.getInstance().getReference().child("images_menu/"+imagesStr[n]);
//                textMenu.setText(imagesStr[n].split("\\.")[0] + imagesStr[n].split("\\.")[1]);
//                fileLocal = File.createTempFile(imagesStr[n].split("\\.")[0], imagesStr[n].split("\\.")[1]);
//                mStorageRef.getFile(fileLocal);
//                Bitmap bitmap = BitmapFactory.decodeFile(fileLocal.getAbsolutePath());
//                imgViews[n].setImageBitmap(bitmap);
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


       setOnClickQuizOpen(textLit, QuizThemes.LITERATURE);


        setOnClickQuizOpen(textPhis, QuizThemes.PHYSICS);

       //setOnClickQuizOpen(attImg, QuizThemes.ATTRACTIONS);
        //setOnClickQuizOpen(textAtt, QuizThemes.ATTRACTIONS);




    }

    private void setOnClickQuizOpen(View view, QuizThemes theme){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),  QuizActivity.class);
                intent.putExtra("QUIZ_THEME", theme.toString());
                startActivity(intent);
            }
        });
    }



}