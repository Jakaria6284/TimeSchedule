package com.example.timeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnameChangeActivity extends AppCompatActivity {

    private TextView Apptitle,Submit;
    private TextView Info;
    LinearLayout settinglayout,Edittextfields;

    EditText classtyep, Building, Room, Teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editname_change);

        Apptitle=findViewById(R.id.Apptitel);
        Info=findViewById(R.id.info);
        settinglayout=findViewById(R.id.settinglayout);

        classtyep=findViewById(R.id.classtype);
        Building = findViewById(R.id.building);
        Room = findViewById(R.id.room);
        Teacher = findViewById(R.id.teacher);
        Submit=findViewById(R.id.Submit);
        Edittextfields=findViewById(R.id.edittextfields);

        Apptitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(editnameChangeActivity.this);
                dialog.setContentView(R.layout.apptiledialog);
                EditText editText=dialog.findViewById(R.id.Apptitelupload);
                TextView textView=dialog.findViewById(R.id.appsubmit);


                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String AppName=editText.getText().toString().toString();

                        if (AppName.isEmpty()) {

                            Toast.makeText(editnameChangeActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Map<String,Object> map=new HashMap<>();
                        map.put("AppName",AppName);

                        FirebaseFirestore.getInstance()
                                .collection("USER")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {


                                            editText.setText("");
                                            dialog.dismiss();
                                            Toast.makeText(editnameChangeActivity.this, "Successfully Add", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                    }
                });
                dialog.show();
            }
        });

        Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settinglayout.setVisibility(View.GONE);
                Edittextfields.setVisibility(View.VISIBLE);



            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String classType = classtyep.getText().toString();
                String Build = Building.getText().toString();
                String Rom = Room.getText().toString();
                String Tech = Teacher.getText().toString();

                Map<String, Object> eventData = new HashMap<>();

                // Check if the fields are not null or empty before adding them to the map
                if (!TextUtils.isEmpty(classType)) {
                    eventData.put("class", classType);
                }
                if (!TextUtils.isEmpty(Build)) {
                    eventData.put("building", Build);
                }
                if (!TextUtils.isEmpty(Rom)) {
                    eventData.put("room", Rom);
                }
                if (!TextUtils.isEmpty(Tech)) {
                    eventData.put("teacher", Tech);
                }

                if (eventData.isEmpty()) {
                    // All fields are empty, display a message or return
                    Toast.makeText(editnameChangeActivity.this, "No data to update", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore.getInstance()
                        .collection("USER")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update(eventData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(editnameChangeActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(editnameChangeActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}