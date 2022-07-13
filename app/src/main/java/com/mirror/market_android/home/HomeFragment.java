package com.mirror.market_android.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.market_android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private View v;

    public static final String TAG = "HomeFragment";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("store");

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
//                if (user == null) {
//                    // 팝업창 구현
//                    Toast.makeText(getActivity(), "로그인이 필요한 기능입니다.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
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




        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        progress.setVisibility(View.VISIBLE);
        storeList.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.toString());
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