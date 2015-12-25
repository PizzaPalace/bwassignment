package com.assignment.gre.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.gre.R;
import com.assignment.gre.common.Constants;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 25-12-2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    //String[] mDataset;
    ArrayList<HashMap<String,Object>> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView mWordTextView;
        public TextView mMeaningTextView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mWordTextView = (TextView)v.findViewById(R.id.word_text_view);
            mMeaningTextView = (TextView)v.findViewById(R.id.meaning_text_view);
            mImageView = (ImageView)v.findViewById(R.id.image_view);
        }
    }

    public RecyclerAdapter(ArrayList<HashMap<String,Object>> dataset){

        this.mDataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_element,parent,false);

        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HashMap<String,Object> map = mDataset.get(position);

        String word = (String)map.get(Constants.WORD);
        String meaning = (String) map.get(Constants.MEANING);

        holder.mWordTextView.setText(word);
        holder.mMeaningTextView.setText(meaning);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
