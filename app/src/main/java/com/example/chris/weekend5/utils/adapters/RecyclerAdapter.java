package com.example.chris.weekend5.utils.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.example.chris.weekend5.R;
import com.example.chris.weekend5.view.maps.MapsActivity;
import com.example.chris.weekend5.view.placedetail.PlaceDetailsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 12/6/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private static final String TAG = RecyclerAdapter.class.getSimpleName() + "_TAG";
    List<com.example.chris.weekend5.model.Place> places = new ArrayList<>();
    Context context;
    MapsActivity mapsActivity;
    private SharedPreferences sharedPreferences;
    
    public RecyclerAdapter(MapsActivity mapsActivity, List<com.example.chris.weekend5.model.Place> places)
    {
        this.places = places;
        this.mapsActivity = mapsActivity;
    }
    
    private void runEnterAnimation(View view)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        mapsActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        view.setTranslationY(metrics.heightPixels);
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, null);
        context = parent.getContext();
        sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position)
    {
//        Glide.with(context).load(memes.get(position)).into(holder.ivMeme);
        holder.tvName.setText(places.get(position).getName());
        
        runEnterAnimation(holder.itemView);
    }
    
    
    @Override
    public int getItemCount()
    {
        return places.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView tvName;
        public ViewHolder(View itemView)
        {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, PlaceDetailsActivity.class);
                    Log.d(TAG, "onClick: "+places.get(getAdapterPosition()).toString());
                    intent.putExtra("name", places.get(getAdapterPosition()).getName());
                    intent.putExtra("id", places.get(getAdapterPosition()).getId());
                    intent.putExtra("address", places.get(getAdapterPosition()).getAddress());
                    intent.putExtra("phone", places.get(getAdapterPosition()).getPhoneNumber());
                    intent.putIntegerArrayListExtra("type", (ArrayList<Integer>) places.get(getAdapterPosition()).getPlaceTypes());
                    intent.putExtra("price", places.get(getAdapterPosition()).getPriceLevel());
                    intent.putExtra("rating", places.get(getAdapterPosition()).getRating());
                    intent.putExtra("website", places.get(getAdapterPosition()).getWebsiteUri() + "");
                    context.startActivity(intent);
                }
            });
        }
    }
}
