package com.mirror.market_android.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mirror.market_android.R;

public class StoreActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("test");

    private ImageView likeImage, dislikeImage, backButton;
    private TextView userName, location, time, title, content, totalPrice, price, likeCount, dislikeCount;
    private Button chatButton;

    int position;
    String getUserName;
    String getLocation;
    String getTime;
    String getTitle;
    String getContent;
    String getLikeCount;
    String getDislikeCount;
    String getTotalPrice;
    String getPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Intent intent = getIntent();


        position = Integer.parseInt(intent.getStringExtra("position"));
        getUserName = intent.getStringExtra("userName");
        getLocation = intent.getStringExtra("location");
        getTime = intent.getStringExtra("time");
        getTitle = intent.getStringExtra("title");
        getContent = intent.getStringExtra("content");
        getLikeCount = intent.getStringExtra("likeCount");
        getDislikeCount = intent.getStringExtra("dislikeCount");
        getTotalPrice = intent.getStringExtra("totalPrice");
        getPrice = intent.getStringExtra("price");

        chatButton = (Button) findViewById(R.id.chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeData homeData = HomeFragment.homeDataList.get(position);
               // ChatActivity.chatList.add(homeData);

             //   Intent intent1 = new Intent(DetailContentActivity.this, ChatScreenActivity.class);
             //   startActivity(intent1);
                finish();
                myRef.setValue("TEST DATA");
            }
        });

        userName = (TextView) findViewById(R.id.userName);
        location = (TextView) findViewById(R.id.location);
        time = (TextView) findViewById(R.id.time);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        price = (TextView) findViewById(R.id.price);
//        likeCount = (TextView) findViewById(R.id.likeCount);
//        dislikeCount = (TextView) findViewById(R.id.dislikeCount);
//
//        likeImage = (ImageView) findViewById(R.id.likeImage);
//        dislikeImage = (ImageView) findViewById(R.id.dislikeImage);
        backButton = (ImageView) findViewById(R.id.backButton);

//        likeImage.setColorFilter(Color.parseColor("#E62323"));
//        dislikeImage.setColorFilter(Color.parseColor("#0C6EE6"));
        backButton.setEnabled(true);
        backButton.setClickable(true);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userName.setText(getUserName);
        location.setText(getLocation);
        time.setText(getTime);
        title.setText(getTitle);
        content.setText(getContent);
//        likeCount.setText(getLikeCount);
//        dislikeCount.setText(getDislikeCount);
        totalPrice.setText(getTotalPrice);
        price.setText(getPrice);


    }
}