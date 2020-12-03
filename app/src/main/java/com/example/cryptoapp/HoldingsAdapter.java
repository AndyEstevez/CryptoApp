package com.example.cryptoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


// used for handling the total
public class HoldingsAdapter extends RecyclerView.Adapter<HoldingsAdapter.RecyclerViewHolder>{

    private ArrayList<Transaction> transactions;
    Context mContext;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        // values needed for the holdings fragment for each transaction in the recyclerView
        public ImageView buy_sell_img;
        public TextView type_of_transaction;
        public TextView time;
        public TextView amount;
        public TextView paid;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            buy_sell_img = itemView.findViewById(R.id.buy_sell_img);
            type_of_transaction = itemView.findViewById(R.id.bought_sold);
            time = itemView.findViewById(R.id.time);
            amount = itemView.findViewById(R.id.amount_of_coin);
            paid = itemView.findViewById(R.id.paid);
        }

    }

    public HoldingsAdapter(Context context, ArrayList<Transaction> transactionArrayList){
        transactions = transactionArrayList;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_for_transactions, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    // updating the recycler view
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Transaction currentTransaction = transactions.get(position);

        holder.buy_sell_img.setImageResource(currentTransaction.getImage());
        holder.type_of_transaction.setText(currentTransaction.getType());
        holder.time.setText(currentTransaction.getTime());
        holder.amount.setText(currentTransaction.getAmount());
        holder.paid.setText(currentTransaction.getPaid());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }






}
