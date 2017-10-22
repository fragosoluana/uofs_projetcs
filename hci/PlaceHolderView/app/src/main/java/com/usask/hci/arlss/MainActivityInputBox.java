//package com.usask.hci.arlss;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.text.InputFilter;
//import android.view.View;
//import android.widget.EditText;
//
//import com.mindorks.placeholderview.SwipeDirectionalView;
//
//public class MainActivityInputBox extends AppCompatActivity {
//
//    private SwipeDirectionalView mSwipeView;
//    private Context mContext;
//    private EditText editText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_input_box);
//        mContext = getApplicationContext();
//
//        mSwipeView = (SwipeDirectionalView)findViewById(R.id.swipeView);
//        mSwipeView.SWIPE = false;
//
//        editText = (EditText)findViewById(R.id.number);
//        editText.setFilters(new InputFilter[]{ new MinMaxFilter("0", "100")});
//
//        for(Profile profile : Utils.loadProfiles(this.getApplicationContext())){
//            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
//        }
//
//        findViewById(R.id.button_id).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if(Integer.parseInt(editText.getText().toString()) >= 50) {
//                    mSwipeView.doSwipe(true);
//                } else {
//                    mSwipeView.doSwipe(false);
//                }
//            }
//        });
//    }
//}