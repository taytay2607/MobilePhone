package com.example.a58010654.mobilephone;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerView;
    private MobileCardFavoriteAdapter adapter;
    private List<MobileCard> mobileCardList;
    private DBHelper mHelper;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mContext = view.getContext();
        mHelper = new DBHelper(getActivity());
        mobileCardList = new ArrayList<>();
        recyclerView.setHasTransientState(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MobileCardFavoriteAdapter(FavoriteFragment.this,mContext,mobileCardList,new ClickListener()
        {
            @Override public void onPositionClicked(int position) {
                openDetailActivity(mobileCardList.get(position));
            }

            @Override public void onLongClicked(int position) {
                Log.v(mContext+"","click id"+mobileCardList.get(position).getId());
            }
        });
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int index = mobileCardList.get(viewHolder.getLayoutPosition()).getId();
                    mobileCardList.remove(viewHolder.getAdapterPosition());
                    adapter.notifyDataSetChanged();
                    (((MainActivity)getActivity()).getMobileListFragment()).deleteFromFavorite(index);
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }

    public void setMobileCardList(List<MobileCard> mobileCardList) {
        this.mobileCardList = mobileCardList;
        adapter.setMobileCardList(mobileCardList);
    }

    private void openDetailActivity(MobileCard mobileCard){
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("id", mobileCard.getId()+"");
        intent.putExtra("name", mobileCard.getName()+"");
        intent.putExtra("brand", mobileCard.getBrand()+"");
        intent.putExtra("description", mobileCard.getDescription()+"");
        intent.putExtra("price", mobileCard.getPrice()+"");
        intent.putExtra("rating", mobileCard.getRating()+"");
        startActivity(intent);
    }
}
