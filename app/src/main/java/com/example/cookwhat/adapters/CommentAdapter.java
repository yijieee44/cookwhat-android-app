package com.example.cookwhat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cookwhat.R;
import com.example.cookwhat.models.RecipeCommentModel;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.UserModelDB;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends ArrayAdapter<RecipeCommentModel> {

    List<UserModelDB> userModelDBS;

    public CommentAdapter(Context context, List<RecipeCommentModel> commentorArrayList, List<UserModelDB> userModelDBS)
    {
        super(context, 0, commentorArrayList);
        this.userModelDBS = userModelDBS;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        RecipeCommentModel user = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_comment,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.profile_pic);
        TextView userName = convertView.findViewById(R.id.TVUsername);
        TextView comment = convertView.findViewById(R.id.TVComment);
        TextView time = convertView.findViewById(R.id.TVTimeCommented);

        String username = "";

        for(int i=0;i<userModelDBS.size();i++){
            if (user.getUserId().equals(userModelDBS.get(i).getUserId())){
                username = userModelDBS.get(i).getUserName();
            }
        }

        userName.setText(username);
        comment.setText(user.getComment());
        time.setText(user.getCreatedTime());

        return convertView;
    }
}
