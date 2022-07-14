package com.mirror.market_android.home;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.market_android.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private List<StoreData> dataList;
    static public View.OnClickListener onClick;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        private ImageView image;
        private TextView title, content, price;

        public MyViewHolder(View v) {
            super(v);

            rootView = v;
            rootView.setEnabled(true);
            rootView.setClickable(true);
            rootView.setOnClickListener(onClick);

            image = (ImageView) v.findViewById(R.id.image);

            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);
            price = (TextView) v.findViewById(R.id.price);

            title.setSelected(true);
            content.setSelected(true);
            price.setSelected(true);



        }
    }

    public HomeAdapter(List<StoreData> list, View.OnClickListener listener) {
        dataList = list;
        onClick = listener;
    }

    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home, parent, false);

        HomeAdapter.MyViewHolder viewHolder = new HomeAdapter.MyViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(HomeAdapter.MyViewHolder holder, int position) {

        // click 이벤트 동작할 위젯에 setTag걸기
        holder.rootView.setTag(position);

        Uri uri = Uri.parse(dataList.get(position).getFirstUri());
        Glide.with(holder.rootView.getContext())
                .load(uri)
                .into(holder.image);

        holder.title.setText(dataList.get(position).getTitle());
        holder.content.setText(dataList.get(position).getContent());
        holder.price.setText(dataList.get(position).getPrice() + "원");

        System.out.println(dataList.get(position).getTitle());
        System.out.println(dataList.get(position).getContent());
        System.out.println(dataList.get(position).getPrice() + "원");
        /*

                // click 이벤트 동작할 위젯에 setTag걸기
        holder.rootView.setTag(position);

        Glide.with(holder.rootView.getContext())
                .load(dataList.get(position).getImage())
                .into(holder.image);

        holder.title.setText(dataList.get(position).getTitle());
        holder.content.setText(dataList.get(position).getContent());
        holder.location.setText(dataList.get(position).getLocation());
        holder.time.setText(dataList.get(position).getTime());
        holder.totalPrice.setText(dataList.get(position).getTotalPrice());
        holder.price.setText(dataList.get(position).getPrice());
         */

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

}
