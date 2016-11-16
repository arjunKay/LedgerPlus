package com.svco.ledgerplus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Nidhin on 16-11-2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragment=new ArrayList<>();
    ArrayList<String> tabtitle=new ArrayList<>();
    public void addFragments(Fragment fragments, String tabtitles){
        this.fragment.add(fragments);
        this.tabtitle.add(tabtitles);
    }
    public ViewPagerAdapter(android.support.v4.app.FragmentManager fm){

        super(fm);
    }
    @Override
    public Fragment getItem(int position) {

        return fragment.get(position);

    }

    @Override
    public int getCount() {
        return fragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitle.get(position);
    }
}
