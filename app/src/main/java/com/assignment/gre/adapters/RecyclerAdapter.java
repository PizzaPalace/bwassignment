package com.assignment.gre.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.gre.R;
import com.assignment.gre.common.Constants;
import com.squareup.picasso.Picasso;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 25-12-2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    //String[] mDataset;
    ArrayList<HashMap<String,Object>> mDataset;
    private Context mContext;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        //private TextView mIdTextView;
        private TextView mWordTextView;
        private ImageView mImageView;
        private TextView mMeaningTextView;

        public ViewHolder(View v) {
            super(v);
            //mIdTextView = (TextView)v.findViewById(R.id.id_text_view);
            mWordTextView = (TextView)v.findViewById(R.id.word_text_view);
            mImageView = (ImageView)v.findViewById(R.id.image_view);
            mMeaningTextView = (TextView)v.findViewById(R.id.meaning_text_view);
        }
    }

    public RecyclerAdapter(ArrayList<HashMap<String,Object>> dataset, Context context){

        this.mDataset = dataset;
        this.mContext = context;
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

        int id = (int)map.get(Constants.ID);
        String word = (String)map.get(Constants.WORD);
        String meaning = (String)map.get(Constants.MEANING);
        double ratio = (double)map.get(Constants.RATIO);
        if(ratio > 0){

            String imageURL = Constants.partialImageURL + id + ".png";
            Picasso.with(mContext)
                    .load(imageURL)
                    .placeholder(R.drawable.notification_template_icon_bg)
                    .error(R.drawable.notification_template_icon_bg)
                    .into(holder.mImageView);
        }
        else{

        }
        //holder.mIdTextView.setText(Integer.toString(id));
        holder.mWordTextView.setText(word);
        holder.mMeaningTextView.setText(meaning);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
