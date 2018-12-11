package com.example.joseph.templestocktracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;


public class FragmentStockDetails extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view;
        //Bundle bundle = getArguments();
        //int pageNumber = bundle.getInt("pageNumber");

        view = inflater.inflate(R.layout.fragment_details, container, false);

        if(savedInstanceState != null) {

            //set the text
            TextView companyName = view.findViewById(R.id.Details_companyName);
            TextView opening = view.findViewById(R.id.detailsOpening);
            TextView current = view.findViewById(R.id.detailsCurrent);

            companyName.setText(savedInstanceState.getString("name"));
            opening.setText(savedInstanceState.getString("opening"));
            current.setText(savedInstanceState.getString("currentPrice"));
            //load the web view
            String symbol = savedInstanceState.getString("symbol");

            WebView wv = view.findViewById(R.id.webView);
            wv.loadUrl(("https://macc.io/lab/cis3515/?symbol=<stock_symbol>" + symbol));

        }

        return view;

        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
