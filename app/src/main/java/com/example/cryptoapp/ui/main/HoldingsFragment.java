package com.example.cryptoapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.cryptoapp.R;

public class HoldingsFragment extends Fragment {



    public HoldingsFragment(){

    }

    public static Fragment newInstance() {
        HoldingsFragment fragment = new HoldingsFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_holdings, container, false);




        return view;
    }
}
