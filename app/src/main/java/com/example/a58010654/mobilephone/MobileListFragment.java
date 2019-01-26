package com.example.a58010654.mobilephone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MobileListFragment extends Fragment {

    static public int onClickId;
    private Context mContext;
    private RecyclerView recyclerView;
    private MobileCardAdapter adapter;
    private static List<MobileCard> mobileCardList;
    private static List<MobileCard> mobileCardFavoriteList;
    private String[] sortList = new String[3];
    private int indexSortList;
    private ImageView imageView_sort;

    DBHelper mHelper;
    static private List<Integer> favoriteId;


    public MobileListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mobile_list, container, false);
        mContext = view.getContext();
        imageView_sort = (ImageView)((MainActivity)getActivity()).findViewById(R.id.imageView_sort);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mHelper = new DBHelper(getActivity());
        favoriteId = mHelper.getFavoriteList();

        sortList[0] = "Price low to high";
        sortList[1] = "Price high to low";
        sortList[2] = "Rating 5-1";
        indexSortList = 0;

        mobileCardList = new ArrayList<>();
        mobileCardFavoriteList = new ArrayList<>();

        recyclerView.setHasTransientState(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MobileCardAdapter(MobileListFragment.this,mContext,mobileCardList,new ClickListener()
        {
            @Override public void onPositionClicked(int position) {
                switch (onClickId)
                {
                    case 1: setFavorite(position); break;
                    case 2: openDetailActivity(mobileCardList.get(position)); break;
                    default:break;
                }
            }

            @Override public void onLongClicked(int position) {
            }
        });

        recyclerView.setAdapter(adapter);
        imageView_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiqlog();
            }
        });
        return view;
    }

    public void showDiqlog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(((MainActivity)getActivity()));
        builder.setSingleChoiceItems(sortList, indexSortList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                indexSortList = index;

                switch (index)
                {
                    case 0: sortByPriceLowToHigh(); break;
                    case 1: sortByPriceHighToLow(); break;
                    case 2: sortByRating(); break;
                    default:break;
                }

                dialog.dismiss();
            }
        });

        builder.create();
        builder.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    private void sortByPriceLowToHigh(){
        Collections.sort(mobileCardList, new Comparator<MobileCard>() {
            @Override
            public int compare(MobileCard lhs, MobileCard rhs) {
                return Double.compare(lhs.getPrice(),rhs.getPrice());
            }
        });
        adapter.notifyDataSetChanged();

        Collections.sort(mobileCardFavoriteList, new Comparator<MobileCard>() {
            @Override
            public int compare(MobileCard lhs, MobileCard rhs) {
                return Double.compare(lhs.getPrice(),rhs.getPrice());
            }
        });
        ((MainActivity)getActivity()).getFavoriteFragment().setMobileCardList(mobileCardFavoriteList);
    }

    private void sortByPriceHighToLow(){
        Collections.sort(mobileCardList, new Comparator<MobileCard>() {
            @Override
            public int compare(MobileCard rhs, MobileCard lhs) {
                return Double.compare(lhs.getPrice(),rhs.getPrice());
            }
        });
        adapter.notifyDataSetChanged();

        Collections.sort(mobileCardFavoriteList, new Comparator<MobileCard>() {
            @Override
            public int compare(MobileCard rhs, MobileCard lhs) {
                return Double.compare(lhs.getPrice(),rhs.getPrice());
            }
        });
        ((MainActivity)getActivity()).getFavoriteFragment().setMobileCardList(mobileCardFavoriteList);
    }

    private void sortByRating(){
        Collections.sort(mobileCardList, new Comparator<MobileCard>() {
            @Override
            public int compare(MobileCard rhs, MobileCard lhs) {
                return Double.compare(lhs.getRating(),rhs.getRating());
            }
        });
        adapter.notifyDataSetChanged();

        Collections.sort(mobileCardFavoriteList, new Comparator<MobileCard>() {
            @Override
            public int compare(MobileCard rhs, MobileCard lhs) {
                return Double.compare(lhs.getRating(),rhs.getRating());
            }
        });
        ((MainActivity)getActivity()).getFavoriteFragment().setMobileCardList(mobileCardFavoriteList);
    }

    private void sort(int id) {
        if(id == 0)
            sortByPriceLowToHigh();
        else if(id == 1)
            sortByPriceHighToLow();
        else if(id == 2)
            sortByRating();
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

    private void setFavorite(int index){
        if(mobileCardList.get(index).getFavorite() == false){
            mHelper.addFavorite(new FavoriteId(mobileCardList.get(index).getId()));
            mobileCardList.get(index).setFavorite(true);
            mobileCardFavoriteList.add(mobileCardList.get(index));
        }
        else{
            deleteItemById(mobileCardList.get(index).getId());
            mHelper.deleteFavorite(mobileCardList.get(index).getId());
            mobileCardList.get(index).setFavorite(false);
        }
        sort(indexSortList);
        ((MainActivity)getActivity()).getFavoriteFragment().setMobileCardList(mobileCardFavoriteList);
        adapter.setMobileCardList(mobileCardList);
    }

    public void deleteFromFavorite(int id) {
        deleteItemById(id);
        mHelper.deleteFavorite(id);
        searchItemById(id).setFavorite(false);
        sort(indexSortList);
        adapter.setMobileCardList(mobileCardList);
    }

    public void deleteItemById(int Id){
        for(int i=0;i<mobileCardFavoriteList.size();i++){
            if(mobileCardFavoriteList.get(i).getId()==Id){
                mobileCardFavoriteList.remove(i);
            }
        }
    }

    public MobileCard searchItemById(int Id){
        for(int i=0;i<mobileCardList.size();i++){
            if(mobileCardList.get(i).getId()==Id){
                return mobileCardList.get(i);
            }
        }
        return null;
    }

    public void setMobileCardList(String json) {

        JSONArray arr;
        favoriteId = mHelper.getFavoriteList();

        try {
            arr = new JSONArray(json);

            for (int i = 0; i < arr.length(); i++) {
                int id = arr.getJSONObject(i).getInt("id");
                String name = arr.getJSONObject(i).getString("name");
                String description = arr.getJSONObject(i).getString("description");
                double price = arr.getJSONObject(i).getDouble("price");
                double rating = arr.getJSONObject(i).getDouble("rating");
                String image_url = arr.getJSONObject(i).getString("thumbImageURL");
                String brand = arr.getJSONObject(i).getString("brand");
                Boolean isFavorite = false;

                if(favoriteId.contains(id)){
                    isFavorite = true;
                }

                mobileCardList.add(
                        new MobileCard(
                                id,
                                name,
                                description,
                                price,
                                rating,
                                isFavorite,
                                image_url,
                                brand
                        )
                );

                if(isFavorite)
                    mobileCardFavoriteList.add(new MobileCard(
                            id,
                            name,
                            description,
                            price,
                            rating,
                            isFavorite,
                            image_url,
                            brand
                    ));
            }

            adapter.setMobileCardList(mobileCardList);
            ((MainActivity)getActivity()).getFavoriteFragment().setMobileCardList(mobileCardFavoriteList);
            sortByPriceLowToHigh();

        }catch(Exception e){ Log.e("error:",e.toString());}

    }
}
