package com.ziadsyahrul.testuploadexcel2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziadsyahrul.testuploadexcel2.Database.Common;
import com.ziadsyahrul.testuploadexcel2.Database.ModelDB.Cart;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHoder> {

    Context context;
    List<Cart> cartList;

    public MainAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public MainViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHoder(LayoutInflater.from(context).inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHoder holder, final int position) {

        holder.tv_main.setText(new StringBuilder(cartList.get(position).sno)
                .append(cartList.get(position).sno)
                .append(" -- ")
                .append(cartList.get(position).date)
                .append(" -- ")
                .append(cartList.get(position).det)
                .append("\n"));

        holder.tv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.cartData = cartList.get(position);
                context.startActivity(new Intent(context, DetailActivity.class));
            }
        });
        Log.d("DETAIL_SNO ", String.valueOf(cartList.get(position).sno));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class MainViewHoder extends RecyclerView.ViewHolder {

        TextView tv_main;

        public MainViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_main = itemView.findViewById(R.id.txt_main);
        }
    }
}
