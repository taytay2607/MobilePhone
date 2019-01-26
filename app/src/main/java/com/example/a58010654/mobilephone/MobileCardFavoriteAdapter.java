package com.example.a58010654.mobilephone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MobileCardFavoriteAdapter extends RecyclerView.Adapter<MobileCardFavoriteAdapter.MyViewHolder>{

    private Context mCtx;
    private List<MobileCard> mobileCardList;
    private FavoriteFragment favoriteFragment;
    private final ClickListener listener;

    public MobileCardFavoriteAdapter(FavoriteFragment favoriteFragment, Context mCtx, List<MobileCard> mobileCardList, ClickListener listener ) {
        this.mCtx = mCtx;
        this.mobileCardList = mobileCardList;
        this.listener = listener;
        this.favoriteFragment = favoriteFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.mobile_card_favorite,null);
        return  new MyViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MobileCard mobile = mobileCardList.get(position);

        holder.textView_name.setText(mobile.getName());
        holder.textView_price.setText("Price: $"+mobile.getPrice());
        holder.textView_rating.setText("Rating: "+mobile.getRating());

        Glide.with(favoriteFragment).load(mobile.getImage_url())
                .apply(new RequestOptions().placeholder(R.drawable.regtangle)).into(holder.imageView_mobile);

    }

    @Override
    public int getItemCount() {
        return mobileCardList.size();
    }

    public void setMobileCardList(List<MobileCard> mobileCardList) {
        this.mobileCardList = new ArrayList<>();
        this.mobileCardList = mobileCardList;
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        private View view;
        private ImageView imageView_mobile;
        private TextView textView_name;
        private TextView textView_price;
        private TextView textView_rating;
        private WeakReference<ClickListener> listenerRef;

        public MyViewHolder(View v,ClickListener listener) {
            super(v);
            view = itemView.findViewById(R.id.cardView_mobile);
            imageView_mobile = itemView.findViewById(R.id.imageView_mobile);
            textView_name = itemView.findViewById(R.id.textView_name);
            textView_price = itemView.findViewById(R.id.textView_price);
            textView_rating = itemView.findViewById(R.id.textView_rating);
            listenerRef = new WeakReference<>(listener);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onPositionClicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listenerRef.get().onLongClicked(getAdapterPosition());
            return true;
        }

    }


}
