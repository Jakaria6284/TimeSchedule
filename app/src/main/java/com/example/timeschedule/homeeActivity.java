package com.example.timeschedule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class homeeActivity extends AppCompatActivity {
  List<homeModel>homeModelList=new ArrayList<>();
    private ImageView previousbtn,nextbtn,Threebar;
    private TextView currentdate,Apptitle;
    ImageView callender;
    Calendar calendar;
    FloatingActionButton EventAdd;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homee);
        calendar=Calendar.getInstance();
        previousbtn=findViewById(R.id.previousdate);
        nextbtn=findViewById(R.id.nextdate);
        callender=findViewById(R.id.calender);
        currentdate=findViewById(R.id.currentdate);
        EventAdd=findViewById(R.id.eventadd);
        recyclerView=findViewById(R.id.eventrecycler);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        Apptitle=findViewById(R.id.Title);

        firebaseFirestore.collection("USER")
                .document(firebaseUser.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String name=value.getString("AppName");

                        Apptitle.setText(name);
                    }
                });


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Threebar=findViewById(R.id.threeber);

        Threebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(homeeActivity.this,settingActivity.class));
            }
        });







        callender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(homeeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                Calendar selectedCalendar = Calendar.getInstance();
                                selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                                Date selectedDate = selectedCalendar.getTime();

                                // Format the date to the desired format
                                SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.US);
                                String formattedDate = sdf.format(selectedDate);
                                uploadData(formattedDate);
                                currentdate.setText(formattedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }

        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.bblack));
        }


        Date currentDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.US);
        String formattedDate = sdf.format(currentDate);
        currentdate.setText(formattedDate);
        uploadData(formattedDate);


         EventAdd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String formattedDateText = currentdate.getText().toString();

                 try {
                     // Parse the formatted date text into a Date object
                     SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.US);
                     Date selectedDate = sdf.parse(formattedDateText);

                     // Create an intent and pass the Date object as an extra
                     Intent intent = new Intent(homeeActivity.this, uploadScheduleActivity.class);
                     intent.putExtra("SELECTED_DATE", selectedDate);
                     startActivity(intent);
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
             }
         });

        previousbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                Date currentDate = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.US);
                String formattedDateee = sdf.format(currentDate);
                currentdate.setText(formattedDateee);
                uploadData(formattedDateee);
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date currentDate = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.US);
                String formattedDat = sdf.format(currentDate);
                currentdate.setText(formattedDat);
                uploadData(formattedDat);

            }
        });






    }


    public  void uploadData(String date)
    {
        firebaseFirestore.collection("USER")
                .document(firebaseUser.getUid())
                .collection("event")
                .document(date)
                .collection("data")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            // Handle errors here
                            return;
                        }
                        homeModelList.clear();
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            homeModelList.clear();

                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                if (document.exists()) {
                                    String sub = document.getString("subject");
                                    String tec = document.getString("teacher");
                                    String col=document.getString("color");
                                    String id=document.getString("document_id");
                                    String date=document.getString("date");
                                    homeModel model = new homeModel(sub, tec,col,id,date);
                                    homeModelList.add(model);

                                }

                                homeAdapter adapter = new homeAdapter(homeModelList,true);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                recyclerView.setVisibility(View.VISIBLE);
                            }


                        }else {
                            // No data for the selected date, hide the RecyclerView
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                });
    }

}