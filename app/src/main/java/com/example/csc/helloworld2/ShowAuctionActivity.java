package com.example.csc.helloworld2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    String bidder_id;

    String respond;
    int resType;
    int resBodySize;
    int resImageSize;
    String resBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_auction);

        Intent getIt = getIntent();
        id_text = getIt.getExtras().getString("id");
        item_id_text = getIt.getExtras().getString("item_id");

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SEND);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        registerReceiver(receiver, filter);

        //데이터 요청하기
        dataRequest();

        Button bettingBtn = (Button) findViewById(R.id.betting_button); //betting btn click
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

    public void dataRequest(){//보낼 메시지의 data를 Request한다
        //requst할 데이터 만드는 중
        code = "4";
        body = item_id_text+"\n\0"; //auctionid \n \0
        body_size = String.valueOf(body.length()+1);
        image_size = "0";
        common_send = code + "\n" + body_size + "\n" + image_size + "\n" + id_text + "\n";
        common_send = CommonSendComplete(common_send);
        final_send = common_send + body;
        //final send 송신
        Intent it = new Intent(ShowAuctionActivity.this, HelloIntentService.class);
        it. putExtra("request", final_send);
        startService(it);
    }

    public String CommonSendComplete(String common_send){//헤더를 완성하는 메소드(256byte로 맞추기)
        while(common_send.length()<256){
            common_send = common_send+"\0";
        }
        return common_send;
    }

    public void CommonReceive(String common_receive){//헤더랑 body 구분, 헤더 파싱
        resType = Integer.valueOf(common_receive.split("\n")[0]);
        resBodySize = Integer.valueOf(common_receive.split("\n")[1]);
        resImageSize = Integer.valueOf(common_receive.split("\n")[2]);
        if(resBodySize!=0) {//ShowAuctionupload 성공, body 있음
            resBody = common_receive.split("\n")[3];
            if(resType>0) dataReceive(common_receive);
        }
    }

    public void dataReceive(String reBody){ //respond body 파싱
        //4 : item_id \n title \n explanation \n ending_date \n auction_time \n min_price \n max_price \n id \n pw \n mail \n phone \n major \n
        code = "4"; //list 받아오기
        title = reBody.split("\n")[3];
        description = reBody.split("\n")[4];
        endingdate = reBody.split("\n")[5];
        endingdate = endingdate.substring(0,4) + "/" + endingdate.substring(4,6) + "/" + endingdate.substring(6,8);
        auctionTime = reBody.split("\n")[6];
        leastPrice = reBody.split("\n")[7];
        maxPrice = reBody.split("\n")[8];
        String seller_id_text = reBody.split("\n")[9];
        bidder_id = reBody.split("\n")[10];
        mail = reBody.split("\n")[13];
        phone = reBody.split("\n")[14];
        department = reBody.split("\n")[15];
        cellerInfo = seller_id_text +"/" + mail + "/" + phone+ "/" + department;
        textSplend();
    }

    public void onDestroy(){//receiver 파괴
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    public class MyWebRequestReceiver extends BroadcastReceiver {//respond 리스너
        @Override
        public void onReceive(Context context, Intent intent) {
            respond = intent.getStringExtra("response");
            CommonReceive(respond);
            if(resType>0) {//만약에 성공하면
                Toast toast = Toast.makeText(ShowAuctionActivity.this,"데이터가 성공적으로 확인되었습니다",Toast.LENGTH_SHORT);
                toast.show();
                ShowAuctionActivity.this.textSplend();
            }else{
                Toast toast1 = Toast.makeText(ShowAuctionActivity.this,resBody,Toast.LENGTH_SHORT);
                toast1.show();
            }
        }
    }

    public void textSplend(){//textview에 정보 보여주기
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
