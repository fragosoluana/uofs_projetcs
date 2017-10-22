package com.usask.hci.arlss;

import android.content.Context;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mindorks.placeholderview.SwipeDirectionalView;

import java.util.Calendar;

/**
 * Created by administrator on 2017-10-21.
 */

public class InputBox {

    private int interfaceID;
    private int trial;
    private int userID;
    private long tStart;
    private boolean start;
    private SwipeDirectionalView mSwipeView;
    private Context mContext;
    private EditText editText;
    private Button submitButton;
    private Profile profile;

    public InputBox(Context mContext, SwipeDirectionalView mSwipeView, EditText editText, Button submitButton, int userID) {
        this.mContext = mContext;
        this.mSwipeView = mSwipeView;
        this.editText = editText;
        this.submitButton = submitButton;
        this.userID = userID;
        this.interfaceID = 3;
        this.trial = 0;
        start = true;
    }

    public SwipeDirectionalView show() {
        mSwipeView.SWIPE = false;

        editText.setFilters(new InputFilter[]{ new MinMaxFilter("0", "100")});

        for(Profile profile : Utils.loadProfiles(mContext)){
            this.profile = profile;
            trial++;
            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView, userID, interfaceID, trial));
        }

//        editText.setOnTouchListener(new View.OnTouchListener(){
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(start) {
//                    tStart = System.currentTimeMillis();
//                    Log.i("EVENT", tStart + "");
//                }
//
//                start = false;
//
//                return false;
//            }
//        });

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            start = true;

                if (Integer.parseInt(editText.getText().toString()) >= 50) {
                    mSwipeView.doSwipe(true);
                } else {
                    mSwipeView.doSwipe(false);
                }

                long tEnd = System.currentTimeMillis();

                String[] data = {Calendar.getInstance().getTime().toString(), userID + "", interfaceID + "", trial + "", profile.getID() + "",
                        (tEnd - tStart) / 1000.0 + "", editText.getText().toString() + ""};

                Utils.writeCSV(mContext, userID + "_" + interfaceID + ".csv", data);
            }
        });

        return mSwipeView;
    }
}
