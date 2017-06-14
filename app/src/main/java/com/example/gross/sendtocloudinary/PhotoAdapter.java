package com.example.gross.sendtocloudinary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class PhotoAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<String> itemList;
    private LayoutInflater inflater;

    public PhotoAdapter(Context context, List<String> itemList) {
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = inflater.inflate(R.layout.item_card, parent, false);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        Picasso.with(holder.itemView.getContext())
                .load(itemList.get(position))
                .resize(250, 250)
                .into(holder.cloudImage);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}

class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView cloudImage;

    RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        cloudImage = (ImageView) itemView.findViewById(R.id.imgShow);


    }

    @Override
    public void onClick(View v) {

    }
}
