package com.example.gupta.mdbsocial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Gupta on 9/25/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.CustomViewHolder> {

        private Context context;
        private ArrayList<Message> data;

        public FeedAdapter(Context context, ArrayList<Message> data) {
            this.context = context;
            this.data = data;
        }


        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
            return new CustomViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final CustomViewHolder holder, int position) {
            Message m = data.get(position);
            holder.nameView.setText(m.name);
            holder.interestedView.setText("Number interested: " + m.interested);
            holder.emailView.setText(m.email);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocial-d237d.appspot.com/").child(m.url + ".png"))
                    .into(holder.imageView);
        }


        @Override
        public int getItemCount() {
            if (data != null) {
                return data.size();
            }
            return 0;
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView nameView;
            ImageView imageView;
            TextView emailView;
            TextView interestedView;

            public CustomViewHolder (View view) {
                super(view);
                this.nameView = (TextView) view.findViewById(R.id.nameView);
                this.imageView = (ImageView) view.findViewById(R.id.imageView);
                this.emailView = (TextView) view.findViewById(R.id.emailView);
                this.interestedView = (TextView) view.findViewById(R.id.interestedView);
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        Message m = data.get(getAdapterPosition());
                        intent.putExtra("message", m);
                        context.startActivity(intent);
                    }
                });
            }
        }
}


