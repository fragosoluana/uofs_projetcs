package com.usask.hci.arlss;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.SwipeDirectionalView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.FingerLift;
import com.mindorks.placeholderview.annotations.swipe.SwipeTouch;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

@Layout(R.layout.tinder_card_view)
public class TinderCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    @View(R.id.scoreRightTxt)
    private TextView scoreRightTxt;

    @View(R.id.scoreLeftTxt)
    private TextView scoreLeftTxt;

    private Profile mProfile;
    private Context mContext;
    private SwipeDirectionalView mSwipeView;
    private int score;
    private boolean start;
    private long tStart;
    private int userID;
    private int interfaceID;
    private int trial;

    public TinderCard(Context context, Profile profile, SwipeDirectionalView swipeView,
                      int userID, int interfaceID, int trial) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;
        score = 50;
        start = true;
        this.userID = userID;
        this.interfaceID = interfaceID;
        this.trial = trial;
    }

    @Resolve
    private void onResolved(){
        Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);
        nameAgeTxt.setText(mProfile.getName());
        locationNameTxt.setText(mProfile.getLocation());
    }

    @FingerLift
    private  void onFingerLift() {
        Snackbar.make(mSwipeView, "Score: " + score + "%", Snackbar.LENGTH_SHORT)
                .show();

        long tEnd = System.currentTimeMillis();
        String[] data = {Calendar.getInstance().getTime().toString(), userID + "", interfaceID + "", trial + "", mProfile.getID() + "",
                (tEnd - tStart) / 1000.0 + "", score + ""};

        Utils.writeCSV(mContext, userID + "_" + interfaceID + ".csv", data);
    }

    @SwipeTouch
    private void onSwipeTouch(float xStart, float yStart, float xCurrent, float yCurrent) {
        if(start==true) {
            tStart = System.currentTimeMillis();
        }

        start = false;

        if(xStart < mSwipeView.getWidth() / 2) {
            score = (int) (50 * (1 - xCurrent / xStart));
            score = Math.abs(50 - score);

            if  (score > 100) {
                score = 100;
            }

            if (xCurrent - xStart > 0) {
                scoreRightTxt.setText(score + "%");
            } else {
                scoreLeftTxt.setText(score + "%");
            }
        } else {
            score = (int) (100 - ((54000 - 50 * xCurrent) / (1080 - xStart)));

            if (score < 0) {
                score = 0;
            }

            if (xCurrent - xStart > 0) {
                scoreRightTxt.setText(score + "%");
            } else {
                scoreLeftTxt.setText(score + "%");
            }
        }
    }
}