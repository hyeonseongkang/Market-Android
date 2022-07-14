package com.mirror.market_android.home;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.market_android.MainActivity;
import com.mirror.market_android.R;
import com.mirror.market_android.chat.ChatData;
import com.mirror.market_android.chat.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private View v;

    public static final String TAG = "HomeFragment";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("store");
    DatabaseReference myChatRef = database.getReference("chats");

    NotificationManager manager;
    NotificationCompat.Builder builder;

    private RelativeLayout writeContent;

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static List<StoreData> storeList = new ArrayList<>();

    private TextView location;
    private ImageView locationList;

    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);


        //init();

        progress = (ProgressBar) v.findViewById(R.id.progress);

        location = (TextView) v.findViewById(R.id.location);
        locationList = (ImageView) v.findViewById(R.id.locationList);
        locationList.setEnabled(true);
        locationList.setClickable(true);
        locationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), DormitorySelectActivity.class);
//                startActivityResult.launch(intent);
            }
        });

        writeContent = (RelativeLayout) v.findViewById(R.id.writeContent);
        writeContent.setEnabled(true);
        writeContent.setClickable(true);
        writeContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateStoreActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // res/drawable 에 있는 이미지를 bitmap으로 가져오기
        // Bitmap testImage = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.test);

        /*
        imageView의 resource를 bitmap으로 가져오기
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();


        Bitmap 을 ImageView 의 Background 로 지정하기
        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
        imageView.setBackground(ob);
        */




        adapter = new HomeAdapter(storeList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();

                if (tag != null) {
                    int position = (int)tag;
                    Intent intent = new Intent(getActivity(), StoreActivity.class);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("key", storeList.get(position).getKey());

                    startActivity(intent);

                }
            }
        });

        recyclerView.setAdapter(adapter);

        messageNoti();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        progress.setVisibility(View.VISIBLE);
        storeList.clear();
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    System.out.println(snapshot1.toString());
//                    StoreData store = snapshot1.getValue(StoreData.class);
//                    storeList.add(store);
//                }
//                adapter.notifyDataSetChanged();
//                progress.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }
//        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.toString());
                storeList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    System.out.println(snapshot1.toString());
                    StoreData store = snapshot1.getValue(StoreData.class);
                    storeList.add(store);
                }
                adapter.notifyDataSetChanged();
                progress.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void messageNoti() {

        myChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // 1번 key 전부 가져옴
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    //Log.d("Snapshot1", snapshot1.toString());

                    for (DataSnapshot snapshot2: snapshot1.getChildren()) {
                        // snapshot2 key: chat, info, users   value :
                        String key = "";
                        String user = "";
                        String lastMessage = "";
                        String uri = "";
                        String title = "";

                        // users ==> myId
                        if (snapshot2.getKey().equals("users")) {
                            Users users = snapshot2.getValue(Users.class);
                            if (users.getUser1().equals(MainActivity.myId) || users.getUser2().equals(MainActivity.myId)) {
                                key = snapshot1.getKey();

                                if (snapshot1.child("chat").getChildrenCount() > 0) {
                                    System.out.println("알림기능활성화~!!!!!!!!!!!!!!!!!!!!!!!!1");
                                    for (DataSnapshot snapshot3: snapshot1.child("chat").getChildren()) {
                                        ChatData chatdata = snapshot3.getValue(ChatData.class);

                                        if (!(chatdata.getUser().equals(MainActivity.myId))) {
                                            showNoti(chatdata.getUser(), chatdata.getMessage());
                                        }

                                        break;
                                    }
                                }
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void showNoti(String title, String message){
        try {
            NotificationManager notificationManager=(NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder= null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                String channelID="channel_01";
                String channelName="MyChannel01";

                NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);

                notificationManager.createNotificationChannel(channel);

                builder=new NotificationCompat.Builder(getActivity(), channelID);
            }

            builder.setSmallIcon(android.R.drawable.ic_menu_view);

            builder.setContentTitle(title);
            builder.setContentText(message);
            Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.logo2);
            builder.setLargeIcon(bm);

            Notification notification=builder.build();
            notificationManager.notify(1, notification);
        } catch (Exception e) {

        }

    }
    /*
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        String data = intent.getStringExtra("result");

                        List<HomeData> newData = new ArrayList<>();
                        for (HomeData home: homeDataList) {
                            if (home.getLocation().equals(data)) {
                                newData.add(home);
                            }
                        }
                        if (result != null) {
                            location.setText(data);
                            // data기준으로 리스트 변경
                        }


                    }
                }
            });
     */

}