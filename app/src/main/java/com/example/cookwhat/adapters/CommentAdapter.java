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
import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends ArrayAdapter<RecipeCommentModel> {

    List<String> usernames;

    public CommentAdapter(Context context, ArrayList<RecipeCommentModel> commentorArrayList, List<String> usernames)
    {
        super(context, 0, commentorArrayList);
        this.usernames = usernames;
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

//        imageView.setImageResource(user.getUserId());
        userName.setText(usernames.get(position));
        comment.setText(user.getComment());
        time.setText(user.getCreatedTime());

        return convertView;



//        return super.getView(position, convertView, parent);
    }
}
