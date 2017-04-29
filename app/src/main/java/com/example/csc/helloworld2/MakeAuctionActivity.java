package com.example.csc.helloworld2;


import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;


/**
 * A login screen that offers login via email/password.
 */
public class MakeAuctionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_auction);

    }

    public boolean onOptionsItemSelected(MenuItem item) {//뒤로가기 버튼을 눌렀을 때
        switch (item.getItemId()) {
            default:
                finish();
                return true;
        }
    }
}

