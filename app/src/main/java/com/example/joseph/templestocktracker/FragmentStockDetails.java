package com.example.joseph.templestocktracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;


public class FragmentStockDetails extends Fragment implements getBundle{


    TextView companyName;
    TextView opening;
    TextView current;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_details, container, false);

        companyName = (TextView) view.findViewById(R.id.Details_companyName);
        opening = (TextView) view.findViewById(R.id.detailsOpening);
        current = (TextView) view.findViewById(R.id.detailsCurrent);

        return view;

        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void getBundle() {

        companyName.setText(getArguments().getString("name"));
        opening.setText(getArguments().getString("opening"));
        current.setText(getArguments().getString("currentPrice"));
        //load the web view
        String symbol = getArguments().getString("symbol");

        WebView wv = getActivity().findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(("https://macc.io/lab/cis3515/?symbol=" + symbol));
    }
}
