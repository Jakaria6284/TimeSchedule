package com.example.timeschedule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class uploadScheduleActivity extends AppCompatActivity {
    EditText Subject, Building, Room, Teacher, Link, Interval, End, Begin, Stop,classtype;
    String Name;
    String UID;


    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String holi;
    FirebaseUser firebaseUser;
    Button Save;
    Calendar calendar;
    TextView Colorpicker;
    ColorPickerView colorPickerView;
    LinearLayout linearLayout;
    TextView textView, ok;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_schedule);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.bblack));
        }
        UID=getIntent().getStringExtra("ID");
        calendar = Calendar.getInstance();
        final Date selectedDate = (Date) getIntent().getSerializableExtra("SELECTED_DATE");

        Colorpicker = findViewById(R.id.colorpicker);
        Colorpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(uploadScheduleActivity.this);
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

        Save = findViewById(R.id.save);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

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


        firebaseFirestore.collection("USER")
                        .document(firebaseUser.getUid())
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        String classtypee= value.getString("class");
                                        String building= value.getString("building");
                                        String Rooom= value.getString("room");
                                        String Teacherr= value.getString("teacher");

                                        classtype.setHint(classtypee);
                                        Building.setHint(building);
                                        Room.setHint(Rooom);
                                        Teacher.setHint(Teacherr);



                                    }
                                });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sub = Subject.getText().toString();
                String classType=classtype.getText().toString();
                String Build = Building.getText().toString();
                String Rom = Room.getText().toString();
                String Tech = Teacher.getText().toString();
                String Links = Link.getText().toString();
                String Inter = Interval.getText().toString();
                String Endd = End.getText().toString();
                String Beginn = Begin.getText().toString();
                String Stopp = Stop.getText().toString();

                if (sub.isEmpty() || classType.isEmpty() || Build.isEmpty() || Rom.isEmpty() || Tech.isEmpty() || Links.isEmpty()
                        || Inter.isEmpty() || Endd.isEmpty() || Beginn.isEmpty() || Stopp.isEmpty()) {

                    Toast.makeText(uploadScheduleActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int INterval = Integer.parseInt(Inter);
                int ENDdate = Integer.parseInt(Endd);

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.US);
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(selectedDate);

                for (long i = 0; i < ENDdate / INterval; i++) {
                    formattedDate = sdf.format(selectedDate);
                    Map<String, Object> eventData = new HashMap<>();
                    eventData.put("subject", sub);
                    eventData.put("class",classType);
                    eventData.put("building", Build);
                    eventData.put("room", Rom);
                    eventData.put("teacher", Tech);
                    eventData.put("link", Links);
                    eventData.put("interval", Inter);
                    eventData.put("end", Endd);
                    eventData.put("begin", Beginn);
                    eventData.put("stop", Stopp);
                    eventData.put("color", holi);
                    eventData.put("date",formattedDate);
                    eventData.put("interval",Inter);
                    eventData.put("end",Endd);

                    firebaseFirestore.collection("USER")
                            .document(firebaseUser.getUid())
                            .collection("event")
                            .document(formattedDate)
                            .collection("data")
                            .add(eventData) // Use add to automatically generate document ID
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        // Update the document with the generated document ID
                                        String generatedDocumentId = task.getResult().getId();
                                        task.getResult().update("document_id", generatedDocumentId);

                                        // Handle completion as needed
                                    } else {
                                        // Handle the failure to add data
                                    }
                                }
                            });

                    currentCalendar.add(Calendar.DAY_OF_MONTH, INterval);
                    selectedDate.setTime(currentCalendar.getTimeInMillis());
                }
            }
        });
    }
}
