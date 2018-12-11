package com.example.joseph.templestocktracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NavigationPane extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view;
        //Bundle bundle = getArguments();
        //int pageNumber = bundle.getInt("pageNumber");

        view = inflater.inflate(R.layout.navigationpane, container, false);

        //WIRE UP NAV BUTTONS
        Button listButton = (Button)view.findViewById(R.id.stockListButtonNav);
        Button detailButton = (Button)view.findViewById(R.id.stockDetailsButtonNa);

        //link buttons to viewpager page
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager vp = (ViewPager)getActivity().findViewById(R.id.view_pager);
                vp.setCurrentItem(1);
            }
        });

        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager vp = (ViewPager)getActivity().findViewById(R.id.view_pager);
                vp.setCurrentItem(2);
            }
        });

        //WIRE UP CHECK FOR IF STOCKS ARE


        return view;
    }

}
