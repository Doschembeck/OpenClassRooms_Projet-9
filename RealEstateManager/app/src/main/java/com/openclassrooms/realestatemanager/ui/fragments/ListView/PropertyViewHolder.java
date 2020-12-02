package com.openclassrooms.realestatemanager.ui.fragments.ListView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.ui.activities.DetailsActivity;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_listview_item_textview_price) TextView mTextViewPrice;
    @BindView(R.id.fragment_listview_item_imageview_photo) ImageView mImageViewPhoto;
    @BindView(R.id.fragment_listview_item_textview_sold) TextView mTextViewSold;
    @BindView(R.id.fragment_listview_item_imageview_sold) ImageView mImageViewSold;
    @BindView(R.id.fragment_listview_item_textview_city) TextView mTextViewCity;
    @BindView(R.id.fragment_listview_item_textview_rooms) TextView mTextViewRooms;
    @BindView(R.id.fragment_listview_item_textview_bedrooms) TextView mTextViewBedRooms;
    @BindView(R.id.fragment_listview_item_textview_area) TextView mTextViewArea;


    Context mContext;
    View mItemView;
    private String devise = "€"; //todo: mettre la devise dans les sharedPreferences

    public PropertyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        mItemView = itemView;
        mContext = itemView.getContext();
    }

    @OnClick(R.id.fragment_listview_item_cardview) public void onClickCardView(){
        mContext.startActivity(new Intent(mContext, DetailsActivity.class));
    }

    public void updateWithProperty(Property property){

        if (property.isSold()){
            mImageViewSold.setVisibility(View.VISIBLE);
            mTextViewSold.setVisibility(View.VISIBLE);
        }

        //todo: erreur ne charge pas l'image en ligne
//        Glide.with(mContext)
//                .load(property.getPhotoIdList().get(0).getPhoto())
//                .centerCrop()
//                .placeholder(R.drawable.test_property)
//                .into(mImageViewPhoto);

//        mTextViewCity.setText(property.getAddressId().getCity());
        mTextViewPrice.setText(Utils.formatEditTextWithDevise(property.getPrice(), devise));
        mTextViewRooms.setText(property.getNbOfRooms() + " Pièces");
        mTextViewBedRooms.setText(property.getNbOfBedRooms() + " Chambres");
        mTextViewArea.setText(property.getArea() + "m²");

    }
}