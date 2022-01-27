package com.example.cookwhat.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.models.UserModelDB;
import com.example.cookwhat.utils.Constants;

import java.util.ArrayList;

public class EditProfilePicAdapter extends RecyclerView.Adapter<EditProfilePicAdapter.ViewHolder>{

    UserModelDB currentUser;
    LayoutInflater inflater;
    ConfirmListener confirmListener;

    public EditProfilePicAdapter(UserModelDB currentUser, Context ctx, ConfirmListener confirmListener){
        this.currentUser = currentUser;
        this.inflater = LayoutInflater.from(ctx);
        this.confirmListener = confirmListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.IV_ProfilePic);

            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pic.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.dark_yellow));
                    currentUser.setProfilePic(getAdapterPosition());
                    confirmListener.onItemClicked(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.profile_pic_item, parent, false);
        return new EditProfilePicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.pic.setImageResource(Constants.PROFILE_PIC[position]);
        if (position == currentUser.getProfilePic()){
            holder.pic.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.dark_yellow));
        }
        else{
            holder.pic.setBackgroundColor(0x00000000);
        }
    }

    @Override
    public int getItemCount() {
        return Constants.PROFILE_PIC.length;
    }

    public interface ConfirmListener{
        public void onItemClicked(int position);
    }

}
