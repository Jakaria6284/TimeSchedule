package com.example.timeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.HashMap;
import java.util.Map;

public class scheduleViewActivity extends AppCompatActivity {

    EditText Subject, Building, Room, Teacher, Link, Interval, End, Begin, Stop,classType;
    Button Save;
    String col;
    FirebaseFirestore firebaseFirestore;
    TextView Colorpicker;
    FirebaseAuth firebaseAuth;
    String id,DAte;
    String holi;
    FirebaseUser firebaseUser;

    ColorPickerView colorPickerView;
    LinearLayout linearLayout;
    TextView textView, ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);
        Colorpicker = findViewById(R.id.colorpicker);

        Save = findViewById(R.id.save);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        id=getIntent().getStringExtra("ID");
        DAte=getIntent().getStringExtra("D");

        Subject = findViewById(R.id.subject);
        classType=findViewById(R.id.classtype);
        Building = findViewById(R.id.building);
        Room = findViewById(R.id.room);
        Teacher = findViewById(R.id.teacher);
        Link = findViewById(R.id.link);
        End = findViewById(R.id.end);

        Interval = findViewById(R.id.interval);
        Begin = findViewById(R.id.begin);
        Stop = findViewById(R.id.stop);
        Toast.makeText(this, ""+DAte+id, Toast.LENGTH_SHORT).show();


        firebaseFirestore.collection("USER")
                .document(firebaseUser.getUid())
                .collection("event")
                .document(DAte)
                .collection("data")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot document= task.getResult();
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
                                col=document.getString("color");




                                Subject.setText(sub);
                                classType.setText(calss);
                                Building.setText(building);
                                Room.setText(room);
                                Teacher.setText(tec);
                                Link.setText(link);
                               Interval.setText(inter);
                                End.setText(end);
                                Begin.setText(beggin);
                                Stop.setText(sttop);
                                Colorpicker.setBackgroundColor(Color.parseColor(col));



                            }
                        }
                    }
                });

        Colorpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(scheduleViewActivity.this);
                dialog.setContentView(R.layout.colordialog);

                linearLayout = dialog.findViewById(R.id.cclor);
                textView = dialog.findViewById(R.id.hexacolor);
                ok = dialog.findViewById(R.id.OK);

                colorPickerView = dialog.findViewById(R.id.colorPickerView);

                colorPickerView.setColorListener(new ColorEnvelopeListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                        linearLayout.setBackgroundColor(envelope.getColor());
                        textView.setText("#" + envelope.getHexCode());
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holi = textView.getText().toString().trim();
                        int rong = Color.parseColor(holi);
                        Colorpicker.setBackgroundColor(rong);

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sub = Subject.getText().toString();
                String classs=classType.getText().toString();
                String Build = Building.getText().toString();
                String Rom = Room.getText().toString();
                String Tech = Teacher.getText().toString();
                String Links = Link.getText().toString();
                String Inter = Interval.getText().toString();
                String Endd = End.getText().toString();
                String Beginn = Begin.getText().toString();
                String Stopp = Stop.getText().toString();

                Map<String, Object> eventData = new HashMap<>();
                eventData.put("subject", sub);
                eventData.put("class",classs);
                eventData.put("building", Build);
                eventData.put("room", Rom);
                eventData.put("teacher", Tech);
                eventData.put("link", Links);
                eventData.put("interval",Inter);
                eventData.put("end", Endd);
                eventData.put("begin", Beginn);
                eventData.put("stop", Stopp);
                eventData.put("color", holi);


                firebaseFirestore.collection("USER")
                        .document(firebaseUser.getUid())
                        .collection("event")
                        .document(DAte)
                        .collection("data")
                        .document(id)
                        .update(eventData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(scheduleViewActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




            }
        });
    }
}


