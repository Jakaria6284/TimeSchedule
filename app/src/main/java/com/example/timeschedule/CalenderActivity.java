package com.example.timeschedule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalenderActivity extends AppCompatActivity {

    CalendarView calendarView;
    Button button;
   public static String formattedDate;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    public static String id;
    List<homeModel> homeModelList=new ArrayList<>();
    LinearLayout calenderlayout;
    LinearLayout FrndSchedule;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        calendarView=findViewById(R.id.calender);
        button=findViewById(R.id.showschedule);
        calenderlayout=findViewById(R.id.calenderlayout);
        FrndSchedule=findViewById(R.id.frndschedule);
        recyclerView=findViewById(R.id.frndrecyclerview);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        id=getIntent().getStringExtra("userID");

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.bblack));
        }


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault());
                 formattedDate = sdf.format(selectedDate.getTime());
            }
        });

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat defaultSdf = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault());
        formattedDate = defaultSdf.format(currentCalendar.getTime());
        calendarView.setDate(currentCalendar.getTimeInMillis());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calenderlayout.setVisibility(View.GONE);
                FrndSchedule.setVisibility(View.VISIBLE);
                uploadData(formattedDate);
            }
        });
    }

    public  void uploadData(String date)
    {
        firebaseFirestore.collection("USER")
                .document(id)
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

                                homeAdapter adapter = new homeAdapter(homeModelList,false);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                recyclerView.setVisibility(View.VISIBLE);
                            }


                        }else {
                            Toast.makeText(CalenderActivity.this, "NO Event Available in this day", Toast.LENGTH_SHORT).show();
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                });
    }
}