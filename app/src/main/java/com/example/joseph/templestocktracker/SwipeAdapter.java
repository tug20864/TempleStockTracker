package com.example.joseph.templestocktracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SwipeAdapter extends FragmentStatePagerAdapter {

    public SwipeAdapter (FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        //use if statement to determine what fragment to return
        Fragment page = null;
        if(i == 0) {
            page = new NavigationPane();
        }

        if(i == 1){
            page = new FragmentStockList();
        }
        if(i == 2)
        {
            page = new FragmentStockDetails();
        }


        return page;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
