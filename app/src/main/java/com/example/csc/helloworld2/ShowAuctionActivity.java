package com.example.csc.helloworld2;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ShowAuctionActivity extends AppCompatActivity {
    private MyWebRequestReceiver receiver;
    Button alert;
    String code;
    String title;
    String description;
    String id_text;
    String item_id_text;
    String body_size;
    String image_size;
    String common_send;
    String body;
    String final_send;
    String auctionTime;
    String endingdate;
    String leastPrice;
    String maxPrice;
    String cellerInfo;
    String mail;
    String department;
    String phone;

    String respond;
    int resType;
    int resBodySize;
    int resImageSize;
    String resBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_auction);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SEND);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        registerReceiver(receiver, filter);

        Intent getIt = getIntent();
        id_text = getIt.getExtras().getString("id");
        item_id_text = getIt.getExtras().getString("item_id");

        //requst할 데이터 만드는 중
        code = "4";
        //auctionid \n \0
        body = item_id_text+"\n\0";
        body_size = String.valueOf(body.length()+1);
        image_size = "0";
        common_send = code + "\n" + body_size + "\n" + image_size + "\n" + id_text + "\n";
        common_send = CommonSendComplete(common_send);
        final_send = common_send + body;
        //final send 송신
        Intent it = new Intent(ShowAuctionActivity.this, HelloIntentService.class);
        it. putExtra("request", final_send);
        startService(it);

        //response 수신

        Button bettingBtn = (Button) findViewById(R.id.betting_button); //sign in button click
        bettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //betting dialog 열기
                Intent it = new Intent(ShowAuctionActivity.this, CustomDialog.class);
                it.putExtra("id",id_text);
                it.putExtra("item_id",item_id_text);
                it.putExtra("min_price",leastPrice);
                startActivity(it);
            }
        });
    }

    public String CommonSendComplete(String common_send){
        while(common_send.length()<256){
            common_send = common_send+"\0";
        }
        return common_send;
    }

    public void CommonReceive(String common_receive){
        resType = Integer.valueOf(common_receive.split("\n")[0]);
        resBodySize = Integer.valueOf(common_receive.split("\n")[1]);
        resImageSize = Integer.valueOf(common_receive.split("\n")[2]);
        if(resBodySize!=0) {//ShowAuctionupload 성공, body 있음
            resBody = common_receive.split("\n")[3];
            if(resType>0) dataReceive(resBody);
        }
    }

    public void dataReceive(String reBody){ //dataRequest
        //4 : item_id \n title \n explanation \n ending_date \n auction_time \n min_price \n max_price \n id \n pw \n mail \n phone \n major \n
        code = "4"; //list 받아오기
        item_id_text = resBody.split("\n")[0];
        title = resBody.split("\n")[1];
        description = resBody.split("\n")[2];
        endingdate = resBody.split("\n")[3];
        auctionTime = resBody.split("\n")[4];
        leastPrice = resBody.split("\n")[5];
        maxPrice = resBody.split("\n")[6];
        id_text = resBody.split("\n")[7];
        mail = resBody.split("\n")[9];
        phone = resBody.split("\n")[10];
        department = resBody.split("\n")[11];
        cellerInfo = id_text +"/" + mail + "/n" + phone+ "/" + department;
        textSplend();
    }

    public void onDestroy(){
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    public class MyWebRequestReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            respond = intent.getStringExtra("response");
            CommonReceive(respond);
            if(resType>0) {//만약에 성공하면
                Toast toast = Toast.makeText(ShowAuctionActivity.this,resBody,Toast.LENGTH_SHORT);
                toast.show();
                ShowAuctionActivity.this.textSplend();
            }
        }
    }

    public void textSplend(){
        //textView id 받기
        TextView name_TV= (TextView)findViewById(R.id.showAuction_name_TV);
        TextView newestPrice_TV= (TextView)findViewById(R.id.showAuction_newestPrice_TV);
        TextView stopPrice_TV= (TextView)findViewById(R.id.showAuction_stopPrice_TV);
        TextView stopDate_TV= (TextView)findViewById(R.id.showAuction_stopDate_TV);
        TextView description_TV= (TextView)findViewById(R.id.showAuction_description_VT);
        TextView sellerInfo_TV= (TextView)findViewById(R.id.showAuction_sellerInfo);

        //textView에 정보 뿌려줘야 함
        name_TV.setText(title);
        newestPrice_TV.setText(leastPrice);
        stopPrice_TV.setText(maxPrice);
        stopDate_TV.setText(endingdate);
        description_TV.setText(description);
        sellerInfo_TV.setText(cellerInfo);
    }
}
