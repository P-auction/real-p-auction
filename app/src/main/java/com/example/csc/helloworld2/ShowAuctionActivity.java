package com.example.csc.helloworld2;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAuctionActivity extends AppCompatActivity {
    Button alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_auction);

        Intent getIt = getIntent();

        //실제로는 DB에서 끌어와야 함
        String id_text = getIt.getExtras().getString("id");
        /*String password_text = getIt.getExtras().getString("password");
        String name_text = getIt.getExtras().getString("name");
        String department_text = getIt.getExtras().getString("department");
        String phone_text = getIt.getExtras().getString("phone");
        */
        //textView id 받기
        TextView name_TV= (TextView)findViewById(R.id.showAuction_name_TV);
        TextView newestPrice_TV= (TextView)findViewById(R.id.showAuction_newestPrice_TV);
        TextView stopPrice_TV= (TextView)findViewById(R.id.showAuction_stopPrice_TV);
        TextView stopDate_TV= (TextView)findViewById(R.id.showAuction_stopDate_TV);
        TextView description_TV= (TextView)findViewById(R.id.showAuction_description_VT);
        TextView sellerInfo_TV= (TextView)findViewById(R.id.showAuction_sellerInfo);

        //textView에 정보 뿌려줘야 함
        /*name_TV.setText();
        newestPrice_TV.setText();
        stopPrice_TV.setText();
        stopDate_TV.setText();
        description_TV.setText();
        sellerInfo_TV.setText();
        */



    }
}
