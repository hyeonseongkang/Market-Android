package com.mirror.market_android.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.mirror.market_android.MainActivity;
import com.mirror.market_android.R;
import com.mirror.market_android.home.HomeAdapter;
import com.mirror.market_android.home.StoreActivity;
import com.mirror.market_android.home.StoreData;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    View v;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("chats");

    RecyclerView chatFragmentRecyclerView;
    ChatListAdapter chatListAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<ChatListData> chatListDataList = new ArrayList<>();

    String myId = MainActivity.myId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_chat, container, false);

        init();

        Log.d("ChatFragment1", myId+ " ") ;
        Log.d("ChatFragment2", MainActivity.myId);

        chatFragmentRecyclerView = (RecyclerView) v.findViewById(R.id.chatFragmentRecyclerView);
        chatFragmentRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        chatFragmentRecyclerView.setLayoutManager(layoutManager);
        chatListAdapter = new ChatListAdapter(chatListDataList, new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();

                if (tag != null) {
                    int position = (int)tag;
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("key", chatListDataList.get(position).getKey());
                    intent.putExtra("title", chatListDataList.get(position).getTitle());
                    startActivity(intent);

                }
            }
        });

        chatFragmentRecyclerView.setAdapter(chatListAdapter);

        /*
                recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

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
         */

        return v;
    }

    public void init() {
        /*
        DB 구조
        chats
            -N6v.....(key) 1.
                chat
                  - N6vFq0....(key)
                        - message: ""
                        - time: ""
                        - user: ""
                  - N6vQa0....(key)
                        - message: ""
                        - time: ""
                        - user: ""
                        .
                        .
                        .
               info
                  - N6rLao....(key)
                        content: ""
                        firstUri: ""
                        .
                        .
               users
                 - user1: ""
                 - user2: ""

         */
        chatListDataList.clear();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            System.out.println("!!!!!!!!!!!!!!!!!! " + myId);
                            if (users.getUser1().equals(MainActivity.myId) || users.getUser2().equals(MainActivity.myId)) {
                                key = snapshot1.getKey();
                                System.out.println(users.getUser1());
                                System.out.println(users.getUser2());

                                if (users.getUser1().equals(MainActivity.myId)) {
                                    user = users.getUser2();
                                } else {
                                    user = users.getUser1();
                                }

                                for (DataSnapshot snapshot3: snapshot1.child("chat").getChildren()) {
                                    ChatData chatdata = snapshot3.getValue(ChatData.class);

                                    // 내가 보낸 메시지가 아닐 경우
                                    if (!chatdata.getUser().equals(MainActivity.myId)) {
                                        lastMessage = chatdata.getMessage();
                                    }
                                }

                                for (DataSnapshot snapshot4: snapshot1.child("info").getChildren()) {
                                    StoreData storeData1 = snapshot4.getValue(StoreData.class);
                                    title = storeData1.getTitle();
                                    uri  = storeData1.getFirstUri();
                                    break;
                                }
                            }
                            ChatListData chatListData = new ChatListData(key, user, lastMessage, uri, title);
                            System.out.println(key);
                            System.out.println(user);
                            System.out.println(lastMessage);
                            System.out.println(title);

                            chatListDataList.add(chatListData);

                        }
                    }
                }
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        /*
        myRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot snapshot) {
                // snapshot key: chats, value:
                //System.out.println(snapshot.toString());

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
                            if (users.getUser1().equals(myId) || users.getUser2().equals(myId)) {
                                key = snapshot1.getKey();

                                if (users.getUser1().equals(myId)) {
                                    user = users.getUser2();
                                } else {
                                    user = users.getUser1();
                                }

                                for (DataSnapshot snapshot3: snapshot1.child("chat").getChildren()) {
                                    ChatData chatdata = snapshot3.getValue(ChatData.class);

                                    // 내가 보낸 메시지가 아닐 경우
                                    if (!chatdata.getUser().equals("user1")) {
                                        lastMessage = chatdata.getMessage();
                                    }
                                }

                                for (DataSnapshot snapshot4: snapshot1.child("info").getChildren()) {
                                    StoreData storeData1 = snapshot4.getValue(StoreData.class);
                                    title = storeData1.getTitle();
                                    uri  = storeData1.getFirstUri();
                                    break;
                                }
                            }

                            ChatListData chatListData = new ChatListData(key, user, lastMessage, uri, title);
                            chatListDataList.add(chatListData);

                        }
                    }
                }
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
*/

    }

}
