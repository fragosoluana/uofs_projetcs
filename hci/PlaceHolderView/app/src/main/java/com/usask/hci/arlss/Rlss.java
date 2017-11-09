package com.usask.hci.arlss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipeDirectionalView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;

/**
 * Created by administrator on 2017-10-21.
 */

public class Rlss extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "Orders";
    private int trial;
    private String[] orders;
    protected static long tStimulus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SwipeDirectionalView mSwipeView = (SwipeDirectionalView) findViewById(R.id.swipeView);
        mSwipeView.SWIPE = true;
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeRotationAngle(5)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        orders = getIntent().getStringArrayExtra(MainActivityOrder.EXTRA_MESSAGE);
        tStimulus = System.currentTimeMillis();

        for(Profile profile : Utils.loadProfiles(getApplicationContext())){
            trial++;
            mSwipeView.addView(new TinderCard(getApplicationContext(), profile, mSwipeView,
                    getIntent().getIntExtra(MainActivity.USER, 0)
                    , 1, trial, orders));
        }

        mSwipeView.addItemRemoveListener(new ItemRemovedListener() {
            @Override
            public void onItemRemoved(int count) {
                tStimulus = System.currentTimeMillis();

                if (count == 0) {
                    int i = (Integer.parseInt(orders[0]) + 1);
                    orders[0] =  i + "";
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, orders);
                    startActivity(intent);
                }
            }
        });

    }
}
