package com.abhijeetdeshmukh.android.mhealthmamta.anm;

/*** Created by ABHIJEET on 26-06-2017.*/

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.abhijeetdeshmukh.android.mhealthmamta.R;

/***
 * @link ANMPageAdapter} is a {@link FragmentPagerAdapter} that can provide the layout for
 * each item based on a ANM visit form
 */
public class ANMPageAdapter extends FragmentPagerAdapter{

    /*** Context of the app*/
    private Context mContext;

    /**
     * Create a new {@link ANMPageAdapter} object.
     *
     * @param context is the context of the app
     * @param fm      is the fragment manager that will keep each fragment's state in the adapter
     *                across swipes.
     */
    public ANMPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ANM1Fragment();
        }else if (position == 1){
            return new ANM2Fragment();
        }else if (position == 2){
            return new ANM3Fragment();
        } else {
            return new ANM4Fragment();
        }
    }

    /***
     * @return the total number of pages
     */
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.visit_1);
        } else if (position == 1) {
            return mContext.getString(R.string.visit_2);
        } else if (position == 2) {
            return mContext.getString(R.string.visit_3);
        } else {
            return mContext.getString(R.string.visit_4);
        }
    }
}
