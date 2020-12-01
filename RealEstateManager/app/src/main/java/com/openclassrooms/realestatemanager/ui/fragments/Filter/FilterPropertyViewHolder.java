package com.openclassrooms.realestatemanager.ui.fragments.Filter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterPropertyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_filter_item_title) TextView textView;
    @BindView(R.id.fragment_filter_item_imageview_close) ImageView mImageViewClose;

    Context mContext;

    public FilterPropertyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }

    @OnClick(R.id.fragment_filter_item_imageview_close) public void onClickImageViewClose(){
        //todo: supprimer le filtre
    }

    public void updateWithFilterProperty(String filterproperty){
        this.textView.setText(filterproperty); // todo si itemType n'est pas String alors à remplacer

        Log.i("TAG1", "UpdateUI  !");
    }
}