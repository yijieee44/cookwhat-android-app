package com.example.cookwhat.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.models.RecipeStepModel;

import java.util.List;

public class EditPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecipeStepModel> recipeStepModels;
    private Context context;

    public List<RecipeStepModel> getRecipeStepModels() {
        return recipeStepModels;
    }

    public EditPhotosAdapter(Context context, List<RecipeStepModel> recipeStepModels) {
        this.recipeStepModels = recipeStepModels;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_edit_photo_card, parent, false);
        return new ViewHolder(view, new MyClickListener() {
            @Override
            public void onUp(int p) {
                if(p>0) {
                    swapItems(p-1, p);
                }
            }

            @Override
            public void onDown(int p) {
                if(p<recipeStepModels.size()-1) {
                    swapItems(p+1, p);
                }
            }

            @Override
            public void onDelete(int p) {

            }

            public void swapItems(int itemAIndex, int itemBIndex) {
                RecipeStepModel itemA = recipeStepModels.get(itemAIndex);
                RecipeStepModel itemB = recipeStepModels.get(itemBIndex);
                recipeStepModels.set(itemAIndex, itemB);
                recipeStepModels.set(itemBIndex, itemA);

                notifyDataSetChanged();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).imageView.setImageURI(Uri.parse(recipeStepModels.get(position).getImage()));
    }


    @Override
    public int getItemCount() {
        return recipeStepModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        MyClickListener listener;

        ImageView imageView;
        Button up;
        Button down;
        Button delete;

        public ViewHolder(View itemView, MyClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.IVEditPhotos);
            up = (Button) itemView.findViewById(R.id.BtnUp);
            down = (Button) itemView.findViewById(R.id.BtnDown);
            delete = (Button) itemView.findViewById(R.id.BtnDelete);

            this.listener = listener;

            up.setOnClickListener(this);
            down.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.BtnUp:
                    listener.onUp(this.getLayoutPosition());
                    break;
                case R.id.BtnDown:
                    listener.onDown(this.getLayoutPosition());
                    break;
                case R.id.BtnDelete:
                    listener.onDelete(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public interface MyClickListener {
        void onUp(int p);
        void onDown(int p);
        void onDelete(int p);
    }
}
