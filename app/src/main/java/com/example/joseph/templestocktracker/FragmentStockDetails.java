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

        companyName = (TextView) this.getView().findViewById(R.id.Details_companyName);
        opening = (TextView) this.getView().findViewById(R.id.detailsOpening);
        current = (TextView) this.getView().findViewById(R.id.detailsCurrent);

        return view;

        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void getBundle(Bundle b) {

        companyName.setText(b.getString("name"));
        opening.setText(b.getString("opening"));
        current.setText(b.getString("currentPrice"));
        //load the web view
        String symbol = b.getString("symbol");

        WebView wv = getView().findViewById(R.id.webView);
        wv.loadUrl(("https://macc.io/lab/cis3515/?symbol=<stock_symbol>" + symbol));
    }
}
