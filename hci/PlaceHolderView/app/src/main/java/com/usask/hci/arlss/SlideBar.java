package com.usask.hci.arlss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mindorks.placeholderview.SwipeDirectionalView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by administrator on 2017-10-21.
 */

public class SlideBar extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "Orders";
    private int trial = 0;
    private long tStart = 0;
    private long tStimulus = 0;
    private boolean slideStart = true;
    private DiscreteSeekBar seekBar;
    private SwipeDirectionalView mSwipeView;
    private String[] orders;
    private List<Profile> profiles;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_slide_bar);

        mSwipeView = (SwipeDirectionalView) findViewById(R.id.swipeView);
        mSwipeView.SWIPE = false;

        seekBar = (DiscreteSeekBar)findViewById(R.id.simpleSeekBar);

        orders = getIntent().getStringArrayExtra(MainActivityOrder.EXTRA_MESSAGE);
        tStimulus = System.currentTimeMillis();

        profiles = Utils.loadProfiles(getApplicationContext());

        for(Profile profile : profiles){
            mSwipeView.addView(new TinderCard(getApplicationContext(), profile, mSwipeView,
                    getIntent().getIntExtra(MainActivity.USER, 0), 2, trial, orders));
        }

        seekBar.setIndicatorFormatter("%d%%");
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener()
        {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                if(slideStart) {
                    tStart = System.currentTimeMillis();
                }

                slideStart = false;
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        findViewById(R.id.button_id).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                slideStart = true;
                trial++;

                if(seekBar.getProgress() >= 50) {
                    mSwipeView.doSwipe(true);
                } else {
                    mSwipeView.doSwipe(false);
                }

                long tEnd = System.currentTimeMillis();

                String[] data = {Calendar.getInstance().getTime().toString(),
                        getIntent().getIntExtra(MainActivity.USER, 0) + "", 2 + "",
                        Arrays.toString(Arrays.copyOfRange(orders, 1, orders.length)),
//                                .replace("[","").replace(", ", "").replace("]", ""),
                        trial + "", profiles.get(trial - 1).getID() + "", (tEnd - tStimulus) / 1000.0 + "", (tEnd - tStart) / 1000.0 + "",
                        seekBar.getProgress() + ""};

                Utils.writeCSV(getIntent().getIntExtra(MainActivity.USER, 0) + "_" + 2 + ".csv", data);
            }
        });

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
