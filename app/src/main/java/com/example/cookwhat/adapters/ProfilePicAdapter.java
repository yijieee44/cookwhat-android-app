package com.example.cookwhat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;

import java.util.List;

public class ProfilePicAdapter extends RecyclerView.Adapter<ProfilePicAdapter.ViewHolder> {

        private List <Integer> profilepic;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
                private final ImageView imgView;

                public ViewHolder(View view) {
                        super(view);
                        // Define click listener for the ViewHolder's View

                        imgView = (ImageView) view.findViewById(R.id.IV_Profilepic1);
                }

                public ImageView getImgView() {
                        return imgView;
                }
        }

        public ProfilePicAdapter(List<Integer> profilepic) {
               this.profilepic = profilepic;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                // Create a new view, which defines the UI of the list item
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.profile_pic_item, viewGroup, false);

                return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

                // Get element from your dataset at this position and replace the
                // contents of the view with that element
                viewHolder.getImgView().setImageResource(profilepic.get(position));

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
                return profilepic.size();
        }
}
