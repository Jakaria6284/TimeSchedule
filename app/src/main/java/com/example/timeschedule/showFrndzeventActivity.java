package com.example.timeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class showFrndzeventActivity extends AppCompatActivity {

    EditText Subject, Building, Room, Teacher, Link, Interval, End, Begin, Stop,classtype;



    String date;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_frndzevent);

        Subject = findViewById(R.id.subject);
        classtype=findViewById(R.id.classtype);
        Building = findViewById(R.id.building);
        Room = findViewById(R.id.room);
        Teacher = findViewById(R.id.teacher);
        Link = findViewById(R.id.link);
        End = findViewById(R.id.end);

        Interval = findViewById(R.id.interval);
        Begin = findViewById(R.id.begin);
        Stop = findViewById(R.id.stop);
        Begin.setText("Start: 8:30 AM");
        Stop.setText("End: 5:30 PM");

        date=getIntent().getStringExtra("D");
        userID=getIntent().getStringExtra("ID");
        String Did=getIntent().getStringExtra("DID");


        FirebaseFirestore.getInstance()
                .collection("USER")
                .document(userID)
                .collection("event")
                .document(date)
                .collection("data")
                .document(Did)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot document=task.getResult();
                            if(document.exists())
                            {
                                String sub = document.getString("subject");
                                String calss = document.getString("class");
                                String building = document.getString("building");
                                String room = document.getString("room");

                                String tec = document.getString("teacher");
                                String link = document.getString("link");
                                String inter = document.getString("interval");
                                String end = document.getString("end");
                                String beggin = document.getString("begin");
                                String sttop = document.getString("stop");

                                Subject.setText(sub);
                                classtype.setText(calss);
                                Building.setText(building);
                                Room.setText(room);
                                Teacher.setText(tec);
                                Link.setText(link);
                                Interval.setText(inter);
                                End.setText(end);
                                Begin.setText(beggin);
                                Stop.setText(sttop);
                            }
                        }
                    }
                });



    }
}