package com.usask.hci.arlss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "Orders";
    public static final String USER = "userID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String[] orders = intent.getStringArrayExtra(MainActivityOrder.EXTRA_MESSAGE);

        if(orders[0].equals("")) {
            orders[0] = 1 + "";
        }

        if(orders[0].equals(orders.length + "")) {
            setContentView(R.layout.activity_end);
        } else {
            switch (Integer.parseInt(orders[Integer.parseInt(orders[0])])) {
                case 1:
                    intent = new Intent(getApplicationContext(), Rlss.class);
                    intent.putExtra(EXTRA_MESSAGE, orders);
                    intent.putExtra(USER, MainActivityOrder.userID);
                    startActivity(intent);

                    break;

                case 2:
                    intent = new Intent(getApplicationContext(), SlideBar.class);
                    intent.putExtra(EXTRA_MESSAGE, orders);
                    intent.putExtra(USER, MainActivityOrder.userID);
                    startActivity(intent);

                    break;

                case 3:
                    intent = new Intent(getApplicationContext(), InputBox.class);
                    intent.putExtra(EXTRA_MESSAGE, orders);
                    intent.putExtra(USER, MainActivityOrder.userID);
                    startActivity(intent);

                    break;
            }
        }
    }
}