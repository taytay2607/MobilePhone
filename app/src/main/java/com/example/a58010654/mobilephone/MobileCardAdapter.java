package com.example.a58010654.mobilephone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.lang.ref.WeakReference;
import java.util.List;


public class MobileCardAdapter extends RecyclerView.Adapter<MobileCardAdapter.MyViewHolder>{

    private Context mCtx;
    private List<MobileCard> mobileCardList;
    private MobileListFragment mobileListFragment;
    private final ClickListener listener;

    public MobileCardAdapter(MobileListFragment mobileListFragment,Context mCtx, List<MobileCard> mobileCardList, ClickListener listener ) {
        this.mCtx = mCtx;
        this.mobileCardList = mobileCardList;
        this.listener = listener;
        this.mobileListFragment = mobileListFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.mobile_card,null);
        return  new MyViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        MobileCard mobile = mobileCardList.get(position);
        holder.textView_name.setText(mobile.getName());
        holder.textView_description.setText(mobile.getDescription());
        holder.textView_price.setText("Price: $"+mobile.getPrice());
        holder.textView_rating.setText("Rating: "+mobile.getRating());

        if(mobile.getFavorite() == false)
            holder.imageView_favorite.setImageResource(R.drawable.baseline_favorite_border_black_48dp);
        else
            holder.imageView_favorite.setImageResource(R.drawable.baseline_favorite_black_48dp);

        Glide.with(mobileListFragment).load(mobile.getImage_url())
                .apply(new RequestOptions().placeholder(R.drawable.regtangle)).into(holder.imageView_mobile);

    }

    @Override
    public int getItemCount() {
        if(mobileCardList == null)
            return 0;
        else
            return mobileCardList.size();
    }

    public void setMobileCardList(List<MobileCard> mobileCardList) {
        this.mobileCardList = mobileCardList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        private View view;
        private ImageView imageView_mobile;
        private ImageView imageView_favorite;
        private TextView textView_name;
        private TextView textView_description;
        private TextView textView_price;
        private TextView textView_rating;
        private WeakReference<ClickListener> listenerRef;

        public MyViewHolder(View v,ClickListener listener) {
            super(v);
            view = itemView.findViewById(R.id.cardView_mobile);
            imageView_mobile = itemView.findViewById(R.id.imageView_mobile);
            imageView_favorite = itemView.findViewById(R.id.imageView_favorite);
            textView_name = itemView.findViewById(R.id.textView_name);
            textView_description = itemView.findViewById(R.id.textView_description);
            textView_price = itemView.findViewById(R.id.textView_price);
            textView_rating = itemView.findViewById(R.id.textView_rating);
            listenerRef = new WeakReference<>(listener);

            v.setOnClickListener(this);
            imageView_favorite.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (v.getId() == imageView_favorite.getId()) {
                MobileListFragment.onClickId = 1;
            }
            else {
                MobileListFragment.onClickId = 2;
            }
            listenerRef.get().onPositionClicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listenerRef.get().onLongClicked(getAdapterPosition());
            return true;
        }
    }

}
