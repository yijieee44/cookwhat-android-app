package com.example.cookwhat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.ProfilePicPopUp;
import com.example.cookwhat.R;

import java.util.List;

public class ProfilePicAdapter extends RecyclerView.Adapter<ProfilePicAdapter.ViewHolder> {

        private List <Integer> profilepic;
        boolean isTick = false;
        Integer selectedProfilePic ;

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
                        context = view.getContext();

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

        public ProfilePicAdapter(List<Integer> profilepic) {
               this.profilepic = profilepic;
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

                                System.out.println(context.toString());
                                ProfilePicPopUp profilePicPopUp =  new ProfilePicPopUp(p);
                                profilePicPopUp.show(((FragmentActivity)context).getSupportFragmentManager(),"ProfilePicDialog");
                                isTick = profilePicPopUp.getConfirm();
                                selectedProfilePic = p;

                                if(isTick){
                                        notifyDataSetChanged();
                                }
                                view.setClickable(true);
                                view.setEnabled(true);

                        };

                });
        }

        // Replace the contents of a view (invoked by the layout manager)

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

                // Get element from your dataset at this position and replace the
                // contents of the view with that element
                viewHolder.getImgView().setImageResource(profilepic.get(position));
                System.out.println("print");
                if(isTick && selectedProfilePic == position){
                        System.out.println("Here");
                        viewHolder.getImgView().findViewById(R.id.IV_Tick).setVisibility(View.VISIBLE);
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





}


