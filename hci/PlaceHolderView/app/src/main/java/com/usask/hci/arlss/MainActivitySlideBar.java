//package com.usask.hci.arlss;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Toast;
//
//import com.mindorks.placeholderview.SwipeDirectionalView;
//
//import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
//
//
//public class MainActivitySlideBar extends AppCompatActivity {
//
//    private DiscreteSeekBar seekBar;
//    private SwipeDirectionalView mSwipeView;
//    private Context mContext;
//    private int count = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_slide_bar);
//
//        mSwipeView = (SwipeDirectionalView)findViewById(R.id.swipeView);
//        seekBar = (DiscreteSeekBar)findViewById(R.id.simpleSeekBar);
//        mContext = getApplicationContext();
//
//        mSwipeView.SWIPE = false;
//
//
//        for(Profile profile : Utils.loadProfiles(this.getApplicationContext())){
//            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
//        }
//
//        seekBar.setIndicatorFormatter("%d%%");
//        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener()
//        {
//
//            @Override
//            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
//                if(count == 0) {
//                    Toast.makeText(MainActivitySlideBar.this, "Seek bar progress started",
//                            Toast.LENGTH_SHORT).show();
//                }
//
//                count++;
//            }
//
//            @Override
//            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
//
//            }
//        });
//
//        findViewById(R.id.button_id).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                count = 0;
//
//                if(seekBar.getProgress() >= 50) {
//                    mSwipeView.doSwipe(true);
//                } else {
//                    mSwipeView.doSwipe(false);
//                }
//            }
//        });
//    }
//}