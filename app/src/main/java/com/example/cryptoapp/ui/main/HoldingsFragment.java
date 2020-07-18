package com.example.cryptoapp.ui.main;

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


import com.example.cryptoapp.HoldingsAdapter;
import com.example.cryptoapp.R;
import com.example.cryptoapp.Transaction;

import java.util.ArrayList;

public class HoldingsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    ArrayList<Transaction> transactionArrayList = new ArrayList<>();
    View v;


    public HoldingsFragment(){

    }

    public static Fragment newInstance() {
        HoldingsFragment fragment = new HoldingsFragment();

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_holdings, container, false);


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
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        transactionArrayList.add(new Transaction(R.drawable.new_red, "Sold", "07-14-2020", "10 BTC"));
        transactionArrayList.add(new Transaction(R.drawable.resize_green, "Bought", "07-17-2020", "5 BTC"));
        transactionArrayList.add(new Transaction(R.drawable.resize_green, "Bought", "07-17-2020", "0.75 BTC"));

    }

}
