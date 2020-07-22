package com.example.cryptoapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cryptoapp.Buy_SellActivity;
import com.example.cryptoapp.HoldingsAdapter;
import com.example.cryptoapp.R;
import com.example.cryptoapp.RecyclerViewActivity;
import com.example.cryptoapp.Transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HoldingsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    private TextView portfolio_value;
    private double getPrice;
    private double value;

    ArrayList<Transaction> transactionArrayList = new ArrayList<>();
    Button btn_add;
    Transaction transaction;
    View v;

    int image;
    String type, date, quantity, price_per_coin;


    public HoldingsFragment(double price) {
        getPrice = price;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_holdings, container, false);

        btn_add = v.findViewById(R.id.btn_add);
        portfolio_value = v.findViewById(R.id.tv_value);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendtoMakeTransaction = new Intent(getContext(), Buy_SellActivity.class);
                startActivityForResult(sendtoMakeTransaction, 1);
            }
        });


        mRecyclerView = v.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mAdapter = new HoldingsAdapter(getContext(), transactionArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        return v;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == -1) {
                image = data.getIntExtra("image", 0);
                type = data.getStringExtra("type");
                date = data.getStringExtra("date");
                quantity = data.getStringExtra("quantity");
                price_per_coin = data.getStringExtra("price_per_coin");

                transaction = new Transaction(image, type, date, quantity + " BTC", price_per_coin);
                transactionArrayList.add(transaction);

                DecimalFormat changeDecimals = new DecimalFormat("##.00");
                portfolio_value.setText("$" + changeDecimals.format(testing(transactionArrayList)));


                mRecyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                mAdapter = new HoldingsAdapter(getContext(), transactionArrayList);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(layoutManager);

            }
        }
    }

    public double testing(ArrayList<Transaction> arrayList) {
        double temp_val, coin_val;
        double val = 0.00;

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getType().equals("Bought")) {
                coin_val = Double.parseDouble(arrayList.get(i).getPaid()
                        .substring(6, arrayList.get(i).getPaid().indexOf(" ", 6))) - getPrice;
                if (coin_val < 0.00) {
                    coin_val = Double.parseDouble(arrayList.get(i).getPaid()
                            .substring(6, arrayList.get(i).getPaid().indexOf(" ", 6))) - coin_val;
                } else {
                    coin_val = Double.parseDouble(arrayList.get(i).getPaid()
                            .substring(6, arrayList.get(i).getPaid().indexOf(" ", 6))) + coin_val;
                }
                temp_val = coin_val * Double.parseDouble(arrayList.get(i).getAmount()
                        .substring(0, arrayList.get(i).getAmount().indexOf(" ")));

                val = val + temp_val;
            } else {
                coin_val = Double.parseDouble(arrayList.get(i).getPaid()
                        .substring(10, arrayList.get(i).getPaid().indexOf(" ", 9))) - getPrice;

                if (coin_val < 0.00) {
                    coin_val = Double.parseDouble(arrayList.get(i).getPaid()
                            .substring(10, arrayList.get(i).getPaid().indexOf(" ", 9))) - coin_val;
                } else {
                    coin_val = Double.parseDouble(arrayList.get(i).getPaid()
                            .substring(10, arrayList.get(i).getPaid().indexOf(" ", 9))) + coin_val;
                }
                temp_val = coin_val * Double.parseDouble(arrayList.get(i).getAmount()
                        .substring(0, arrayList.get(i).getAmount().indexOf(" ")));

                val = val - temp_val;


            }
        }
        return val;
    }
}
