package com.example.timeschedule;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class homeAdapter extends RecyclerView.Adapter<homeAdapter.viewHolder> {


    public List<homeModel>homeModelList;
    Boolean  touch;
    public homeAdapter(List<homeModel> homeModelList,Boolean touch) {
        this.homeModelList = homeModelList;
        this.touch=touch;
    }


    @NonNull
    @Override
    public homeAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.homeitem,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull homeAdapter.viewHolder holder, int position) {
      String Subject=homeModelList.get(position).getSubject();
      String Teacher=homeModelList.get(position).getTeacher();
      String Collor=homeModelList.get(position).getColor();
      String dID=homeModelList.get(position).getId();
      String Date=homeModelList.get(position).getDate();

        if (Collor != null && !Collor.isEmpty()) {
            //programetically set background in recyleritem
            holder.itemView.setBackgroundResource(R.drawable.round);

            int color = Color.parseColor(Collor);

            // change color after add background
            holder.itemView.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_OVER);
        } else {
            // Handle the case where colorCode is null or empty, e.g., set a default color.
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(touch==true) {
                    Intent intent = new Intent(holder.itemView.getContext(), scheduleViewActivity.class);
                    intent.putExtra("ID", dID);
                    intent.putExtra("D", Date);
                    holder.itemView.getContext().startActivity(intent);
                }else
                {
                    Intent intent = new Intent(holder.itemView.getContext(), showFrndzeventActivity.class);
                    holder.itemView.getContext().startActivity(intent);

                }


            }
        });


      holder.setData(Subject,Teacher);
    }

    @Override
    public int getItemCount() {
        return homeModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView Sub,TEc;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
           Sub=itemView.findViewById(R.id.SUbject);
            TEc=itemView.findViewById(R.id.TeaCher);

        }
        public void setData(String s,String t)
        {
            Sub.setText(s);
            TEc.setText(t);
        }
    }
}
