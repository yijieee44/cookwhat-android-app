package com.example.cookwhat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.ProfilePicPopUp;
import com.example.cookwhat.R;

import java.util.ArrayList;

public class ProfilePicAdapter extends RecyclerView.Adapter<ProfilePicAdapter.ViewHolder> {

        private ArrayList <Integer> profilepic;
        boolean isTick = false;
        Integer tempSelected = -1;
        Integer selectedProfilePic = -1;
        ProfileClickListener2 clickListener;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                private final ImageView imgView;
                ProfileClickListener clickListener;
                Context context;


                public ViewHolder(View view, ProfileClickListener clickListener) {
                        super(view);

                        imgView = (ImageView) view.findViewById(R.id.IV_ProfilePic);
                        this.clickListener = clickListener;
                        imgView.setOnClickListener(this);
                        this.context = view.getContext();

                }

                public ImageView getImgView() {
                        return imgView;
                }

                @Override
                public void onClick(View view) {
                        view.setEnabled(false);
                        view.setClickable(false);
                        clickListener.onClick(this.getLayoutPosition(), this.context, view);

                }


        }

        public ProfilePicAdapter(ArrayList<Integer> profilepic, ProfileClickListener2 profileClickListener) {
               this.profilepic = profilepic;
               this.clickListener = profileClickListener;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                // Create a new view, which defines the UI of the list item
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.profile_pic_item, viewGroup, false);


                return new ViewHolder(view, new ProfileClickListener(){
                        @Override
                        public void onClick(int p, Context context, View view) {
                                ProfilePicPopUp profilePicPopUp =  new ProfilePicPopUp(p, new ProfilePicPopUp.profilePicPopUpListener() {
                                        @Override
                                        public void onConfirmClicked(boolean clicked) {
                                                isTick = clicked;
                                                clickListener.onItemClicked(p);
                                                selectedProfilePic = p;
                                                notifyItemChanged(p);
                                                if (tempSelected != -1){
                                                        notifyItemChanged(tempSelected);
                                                }
                                        }
                                });
                                profilePicPopUp.show(((FragmentActivity)context).getSupportFragmentManager(),"ProfilePicDialog");
                                view.setClickable(true);
                                view.setEnabled(true);

                        };

                });
        }

        // Replace the contents of a view (invoked by the layout manager)

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

                // Get element from your dataset at this position and replace the
                // contents of the view with that element
                viewHolder.getImgView().setImageResource(profilepic.get(position));
                if (selectedProfilePic == position && isTick){
                        viewHolder.getImgView().setBackgroundColor(ContextCompat.getColor(viewHolder.context, R.color.dark_yellow));
                        tempSelected = position;
                }
                else if (isTick) {
                        viewHolder.getImgView().setBackgroundColor(0x00000000);
                }
        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
                return profilepic.size();
        }

        public interface ProfileClickListener{
                void onClick(int p, Context context, View view);

        }

        public interface ProfileClickListener2 {
                public void onItemClicked(int position);
        }
}


