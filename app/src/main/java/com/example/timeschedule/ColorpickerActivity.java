package com.example.timeschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorListener;

public class ColorpickerActivity extends AppCompatActivity {

    ColorPickerView colorPickerView;
    LinearLayout linearLayout;
    TextView textView,ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorpicker);
        colorPickerView=findViewById(R.id.colorPickerView);
        linearLayout=findViewById(R.id.cclor);
        textView=findViewById(R.id.hexacolor);
        ok=findViewById(R.id.OK);

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
                Intent intent=new Intent(ColorpickerActivity.this, uploadScheduleActivity.class);
                intent.putExtra("C",textView.getText().toString().trim());
                startActivity(intent);
            }
        });

    }
}