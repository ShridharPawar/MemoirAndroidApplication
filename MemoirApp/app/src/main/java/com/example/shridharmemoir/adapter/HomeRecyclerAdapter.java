package com.example.shridharmemoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shridharmemoir.R;

public class HomeRecyclerAdapter  extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {
    String data1[],data2[],data3[];
    Context context;

    public HomeRecyclerAdapter(Context ct, String[] movieNames, String[] releaseYear, String[] ratings)
    {
        context = ct;
        data1 = movieNames;
        data2 = releaseYear;
        data3 = ratings;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_rv,parent,false);
        return new MyViewHolder(view);
}

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
               holder.myText1.setText(data1[position]);
               holder.myText2.setText(data2[position]);
               holder.ratingBar.setRating(Float.parseFloat(data3[position]));

    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myText1, myText2;
        RatingBar ratingBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.movieName);
            myText2 = itemView.findViewById(R.id.releaseDate);
            ratingBar = itemView.findViewById(R.id.rating);
        }
    }


}
