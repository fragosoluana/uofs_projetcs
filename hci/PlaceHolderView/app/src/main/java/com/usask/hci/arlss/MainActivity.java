package com.usask.hci.arlss;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.mindorks.placeholderview.SwipeDirectionalView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SwipeDirectionalView mSwipeView;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = new Random().nextInt();

//        setContentView(R.layout.activity_main);
//        mSwipeView = (SwipeDirectionalView)findViewById(R.id.swipeView);

//        mSwipeView = new Rlss(getApplicationContext(), (SwipeDirectionalView)findViewById(R.id.swipeView), userID)
//                .show();
//
//        mSwipeView.addItemRemoveListener(new ItemRemovedListener() {
//            @Override
//            public void onItemRemoved(int count) {
//                if(count == 0) {
//                    setContentView(R.layout.activity_main_slide_bar);
//                    mSwipeView = (SwipeDirectionalView)findViewById(R.id.swipeView);
//
//                    mSwipeView = new SlideBar(getApplicationContext(), mSwipeView, (DiscreteSeekBar)findViewById(R.id.simpleSeekBar),
//                            (Button)findViewById(R.id.button_id),userID).show();
//
//                    mSwipeView.addItemRemoveListener(new ItemRemovedListener() {
//                        @Override
//                        public void onItemRemoved(int count) {
//                            if(count == 0) {
                                setContentView(R.layout.activity_main_input_box);
                                mSwipeView = (SwipeDirectionalView) findViewById(R.id.swipeView);

                                mSwipeView = new InputBox(getApplicationContext(), mSwipeView, (EditText) findViewById(R.id.number),
                                        (Button) findViewById(R.id.button_id), userID).show();
                            }
//                        }
//                    });
//                }
//            }
//        });
//    }
}