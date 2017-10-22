package com.usask.hci.arlss;

import android.content.Context;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipeDirectionalView;

/**
 * Created by administrator on 2017-10-21.
 */

public class Rlss {

    private int interfaceID;
    private int trial;
    private int userID;
    private SwipeDirectionalView mSwipeView;
    private Context mContext;

    public Rlss(Context mContext, SwipeDirectionalView mSwipeView, int userID) {
        this.mContext = mContext;
        this.mSwipeView = mSwipeView;
        this.userID = userID;
        this.interfaceID = 1;
        this.trial = 0;
    }

    public SwipeDirectionalView show() {
        mSwipeView.SWIPE = true;

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeRotationAngle(5)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));


        for(Profile profile : Utils.loadProfiles(mContext)){
            trial++;
            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView, userID, interfaceID, trial));
        }

        return mSwipeView;
    }
}
