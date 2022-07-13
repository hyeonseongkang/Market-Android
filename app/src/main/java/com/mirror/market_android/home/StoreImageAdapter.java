package com.mirror.market_android.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.market_android.R;

import java.util.List;

class StoreImageAdapter extends RecyclerView.Adapter<StoreImageAdapter.MyViewHolder>{

    private List<PhotoData> dataList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        private ImageView photoImage;

        public MyViewHolder(View v) {
            super(v);

            photoImage = (ImageView) v.findViewById(R.id.photoImage);
            rootView = v;
        }
    }

    public StoreImageAdapter(List<PhotoData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_store_image, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(holder.rootView.getContext())
                .load(dataList.get(position).getPhotoUri())
                .into(holder.photoImage);
    }


    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }
}
