package com.mirror.market_android.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.market_android.R;

import java.util.List;

public class CreateContentActivityAdapter extends RecyclerView.Adapter<CreateContentActivityAdapter.MyViewHolder> {

    private List<PhotoData> dataList;
    static public View.OnClickListener onClick;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        private ImageView photo, deletePhoto;
        private TextView content;

        public MyViewHolder(View v) {
            super(v);


            photo = (ImageView) v.findViewById(R.id.photo);
            deletePhoto = (ImageView) v.findViewById(R.id.deletePhoto);
            deletePhoto.setEnabled(true);
            deletePhoto.setClickable(true);
            deletePhoto.setOnClickListener(onClick);
            //content = (TextView) v.findViewById(R.id.content);
            //content.setSelected(true);
            rootView = v;

        }
    }

    public CreateContentActivityAdapter(List<PhotoData> list, View.OnClickListener listener) {
        dataList = list;
        onClick = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.create_adapter, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // click 이벤트 동작할 위젯에 setTag걸기
        holder.deletePhoto.setTag(position);

        Glide.with(holder.rootView.getContext())
                .load(dataList.get(position).getPhotoUri())
                .into(holder.photo);

      //  holder.content.setText(dataList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

}
