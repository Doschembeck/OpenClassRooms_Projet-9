package com.openclassrooms.realestatemanager.ui.fragments.Filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class FilterPropertyAdapter extends RecyclerView.Adapter<FilterPropertyViewHolder> {

    // FOR DATA
    private List<String> mListFilterProperty;

    // CONSTRUCTOR
    public FilterPropertyAdapter(List<String> listFilterProperty) {
        this.mListFilterProperty = listFilterProperty;
    }

    @Override
    public FilterPropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_filter_item, parent, false);

        return new FilterPropertyViewHolder(view);
    }

    // UPDATE VIEW HOLDER
    @Override
    public void onBindViewHolder(FilterPropertyViewHolder viewHolder, int position) {
        viewHolder.updateWithFilterProperty(this.mListFilterProperty.get(position));
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.mListFilterProperty.size();
    }
}