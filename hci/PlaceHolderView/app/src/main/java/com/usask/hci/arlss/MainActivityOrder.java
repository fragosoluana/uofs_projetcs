package com.usask.hci.arlss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.mindorks.placeholderview.SwipeDirectionalView;

public class MainActivityOrder extends AppCompatActivity {

    private SwipeDirectionalView mSwipeView;
    private String orders[];
    public static final String EXTRA_MESSAGE = "Orders";
    public static int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        (findViewById(R.id.buttonSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders = ((EditText) findViewById(R.id.textOrder)).getText().toString().split("");

                try {
                    userID = Integer.parseInt(((EditText) findViewById(R.id.textUser)).getText().toString());
                } catch (NumberFormatException e) {
                    userID = -1;
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE, orders);
                startActivity(intent);
            }
        });
    }
}