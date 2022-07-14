package com.mirror.market_android.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mirror.market_android.R;
import com.mirror.market_android.chat.ChatActivity;
import com.mirror.market_android.chat.ChatData;
import com.mirror.market_android.chat.Users;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("store");

    DatabaseReference chatRef = database.getReference("chats");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private List<StoreData> storeList = new ArrayList<>();
    private List<String> photoKeys;
    private List<PhotoData> photoDataList = new ArrayList<>();

    private ProgressBar progress;

    private ImageButton backButton;
    private Button chatButton;

    private TextView title, content, price;
    private String getTitle, getContent, getPrice;

    private String myId = "user1";
    private String userId = "user2";

    private RecyclerView recyclerView;
    private StoreImageAdapter adapter;
    private LinearLayoutManager layoutManager;

    int position;
    String key;
    StoreData storeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Intent intent = getIntent();


        position = Integer.parseInt(intent.getStringExtra("position"));
        key = intent.getStringExtra("key");

        chatButton = (Button) findViewById(R.id.chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatKey = chatRef.push().getKey();
//                chatRef.child(chatKey).child("chat").push().setValue(new ChatData(myId, "my1", "1111"));
//                chatRef.child(chatKey).child("chat").push().setValue(new ChatData(userId, "user1", "3333"));
//                chatRef.child(chatKey).child("chat").push().setValue(new ChatData(myId, "my2", "2222"));
//                chatRef.child(chatKey).child("chat").push().setValue(new ChatData(userId, "user2", "3333"));
//                chatRef.child(chatKey).child("chat").push().setValue(new ChatData(myId, "my3", "2222"));
//                chatRef.child(chatKey).child("chat").push().setValue(new ChatData(myId, "my4", "2222"));
//                chatRef.child(chatKey).child("chat").push().setValue(new ChatData(userId, "user3", "3333"));
                chatRef.child(chatKey).child("info").child(key).setValue(storeData);
                chatRef.child(chatKey).child("users").setValue(new Users(myId, userId));

                Intent intent = new Intent(StoreActivity.this, ChatActivity.class);
                intent.putExtra("key", chatKey);
                intent.putExtra("title", getTitle);
                startActivity(intent);
            }
        });

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        price =  (TextView) findViewById(R.id.price);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new StoreImageAdapter(photoDataList);

        recyclerView.setAdapter(adapter);

        progress = (ProgressBar) findViewById(R.id.progress);

        init();

    }

    public void init() {
        progress.setVisibility(View.VISIBLE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    StoreData store = snapshot1.getValue(StoreData.class);
                    if (key.equals(store.getKey())) {
                        storeData = store;
                        userId = store.getId();
                        getTitle = store.getTitle();
                        getContent = store.getContent();
                        getPrice = store.getPrice();
                        photoKeys = store.getPhotoKeys();

                        title.setText(getTitle);
                        content.setText(getContent);
                        price.setText(getPrice);
                    }
                }
                for (int i = 0; i < photoKeys.size(); i++) {
                    StorageReference tempStorage = storageRef.child("store/" + photoKeys.get(i) + ".jpg");
                    tempStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            PhotoData photoData = new PhotoData(uri);
                            photoDataList.add(photoData);
                            System.out.println("이것이다!!: " + uri);
                            adapter.notifyDataSetChanged();
                            progress.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}