package com.usask.hci.arlss;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mindorks.placeholderview.SwipeDirectionalView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by administrator on 2017-10-21.
 */

public class InputBox extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "Orders";
    private int trial = 0;
    private long tStart = 0;
    private boolean start = true;
    private SwipeDirectionalView mSwipeView;
    private EditText editText;
    private String[] orders;
    private long tStimulus;
    private List<Profile> profiles;

//    @Override
//    public void onBackPressed(){
//        Log.i("TEST", "IM HERE");
//        mSwipeView.getLayoutParams().height = 1600;
//        findViewById(R.id.frameCardView).getLayoutParams().height = 1600;
//    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch(keyCode){
//            case KeyEvent.KEYCODE_BACK:
//                Log.i("TEST", "IM HERE");
//                mSwipeView.getLayoutParams().height = 1600;
//                findViewById(R.id.frameCardView).getLayoutParams().height = 1600;
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_input_box);

        mSwipeView = (SwipeDirectionalView) findViewById(R.id.swipeView);

        mSwipeView.SWIPE = false;
        mSwipeView.getBuilder().setDisplayViewCount(1);

        editText = (EditText) findViewById(R.id.editText);
        editText.setFilters(new InputFilter[]{new MinMaxFilter("0", "100")});

        orders = getIntent().getStringArrayExtra(MainActivityOrder.EXTRA_MESSAGE);
        tStimulus = System.currentTimeMillis();

        profiles = Utils.loadProfiles(getApplicationContext());

        for (Profile profile : profiles) {
            mSwipeView.addView(new TinderCard(getApplicationContext(), profile, mSwipeView,
                    getIntent().getIntExtra(MainActivity.USER, 0),
                    3, trial, orders));
        }

        editText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (start) {
                    tStart = System.currentTimeMillis();
                }

                mSwipeView.getLayoutParams().height = 1100;
                findViewById(R.id.frameCardView).getLayoutParams().height = 1100;

                start = false;

                return false;
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    start = true;
                    trial++;

                    if (!editText.getText().toString().equals("")) {
                        if (Integer.parseInt(editText.getText().toString()) >= 50) {
                            mSwipeView.doSwipe(true);
                        } else {
                            mSwipeView.doSwipe(false);
                        }

                        long tEnd = System.currentTimeMillis();

                        String[] data = {Calendar.getInstance().getTime().toString(), getIntent().getIntExtra(MainActivity.USER, 0)
                                + "", 3 + "",  Arrays.toString(Arrays.copyOfRange(orders, 1, orders.length)) + "", trial + "",
                                profiles.get(trial - 1).getID() + "", (tEnd - tStimulus) / 1000.0 + "", (tEnd - tStart) / 1000.0 + "",
                                editText.getText().toString() + ""};

                        Utils.writeCSV(getIntent().getIntExtra(MainActivity.USER, 0) + "_" + 3 + ".csv",
                                data);

                        InputMethodManager imm = (InputMethodManager) getApplicationContext()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

                        editText.setText("");

                        mSwipeView.getLayoutParams().height = 1600;
                        findViewById(R.id.frameCardView).getLayoutParams().height = 1600;

                        return true;
                    } else {
                        mSwipeView.getLayoutParams().height = 1600;
                        findViewById(R.id.frameCardView).getLayoutParams().height = 1600;
                    }
                }

                return false;
            }
        });

        mSwipeView.addItemRemoveListener(new ItemRemovedListener() {
            @Override
            public void onItemRemoved(int count) {
                tStimulus = System.currentTimeMillis();

                if (count == 0) {
                    int i = (Integer.parseInt(orders[0]) + 1);
                    orders[0] = i + "";
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, orders);
                    startActivity(intent);
                }
            }
        });
    }
}
