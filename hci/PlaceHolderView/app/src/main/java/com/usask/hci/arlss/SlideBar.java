package com.usask.hci.arlss;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mindorks.placeholderview.SwipeDirectionalView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Calendar;

/**
 * Created by administrator on 2017-10-21.
 */

public class SlideBar {

    private int interfaceID;
    private int trial;
    private int userID;
    private long tStart;
    private boolean start;
    private SwipeDirectionalView mSwipeView;
    private Context mContext;
    private DiscreteSeekBar seekBar;
    private Button submitButton;

    public SlideBar(Context mContext, SwipeDirectionalView mSwipeView, DiscreteSeekBar seekBar, Button submitButton, int userID) {
        this.mContext = mContext;
        this.mSwipeView = mSwipeView;
        this.seekBar = seekBar;
        this.submitButton = submitButton;
        this.userID = userID;
        this.interfaceID = 2;
        this.trial = 0;
        start = true;
    }

    public SwipeDirectionalView show() {
        mSwipeView.SWIPE = false;

        for(Profile profile : Utils.loadProfiles(mContext)){
            trial++;
            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView, userID, interfaceID, trial));
        }

        seekBar.setIndicatorFormatter("%d%%");
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener()
        {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                if(start) {
                    tStart = System.currentTimeMillis();
                }

                start = false;
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                start = true;

                if(seekBar.getProgress() >= 50) {
                    mSwipeView.doSwipe(true);
                } else {
                    mSwipeView.doSwipe(false);
                }

                long tEnd = System.currentTimeMillis();

                Log.i("EVENT", "DATE: " + Calendar.getInstance().getTime()
                        + "\nUSER ID: " + userID
                        + "\nINTERFACE ID: " + interfaceID
                        + "\nTRIAL: " + trial
                        + "\nELAPASED TIME: " + (tEnd - tStart) / 1000.0
                        + "\nSCORE: " + seekBar.getProgress());
            }
        });

        return mSwipeView;
    }
}
