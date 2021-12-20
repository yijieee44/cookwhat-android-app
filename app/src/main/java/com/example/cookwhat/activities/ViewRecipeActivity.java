<<<<<<< HEAD
package com.example.cookwhat.activities;

=======
>>>>>>> refs/remotes/origin/main
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookwhat.R;

import java.util.ArrayList;

public class ViewRecipeActivity extends AppCompatActivity {

    ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        imageSlider = findViewById(R.id.image_slider);
<<<<<<< HEAD
        String averylongstring ="wadasdadjkasdnkjasbfjksbdfjksdbfgjksbfgjkfbkjfsdbgjksdbfgkjsd\nasdfbjhasdbfhjsabdfasfasdfhjasbdfs\nafbjahdbsjfbsjhjafbdsfjas";
        ArrayList<SlideModel> images = new ArrayList<>();
        images.add(new SlideModel("https://upload.wikimedia.org/wikipedia/commons/9/99/IMG-20200708-WA0006.jpg", averylongstring,null));
        images.add(new SlideModel("https://upload.wikimedia.org/wikipedia/commons/9/99/IMG-20200708-WA0006.jpg", averylongstring,null));
        images.add(new SlideModel("https://upload.wikimedia.org/wikipedia/commons/9/99/IMG-20200708-WA0006.jpg", averylongstring,null));
=======

        ArrayList<SlideModel> images = new ArrayList<>();
        images.add(new SlideModel("https://www.google.com/url?sa=i&url=https%3A%2F%2Ficatcare.org%2Fadvice%2Fthinking-of-getting-a-cat%2F&psig=AOvVaw1FI8DpJEOUdMZ5vT4TVdvA&ust=1640072487921000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCMjq-qLw8fQCFQAAAAAdAAAAABAD", null));
        images.add(new SlideModel("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.goodhousekeeping.com%2Flife%2Fpets%2Fa34774318%2Fboy-cat-names%2F&psig=AOvVaw1FI8DpJEOUdMZ5vT4TVdvA&ust=1640072487921000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCMjq-qLw8fQCFQAAAAAdAAAAABAJ", null));
        images.add(new SlideModel("https://www.google.com/url?sa=i&url=https%3A%2F%2Fprimalpetfoods.com%2Fblogs%2Fnews%2Fstages-of-a-cat-s-life&psig=AOvVaw1FI8DpJEOUdMZ5vT4TVdvA&ust=1640072487921000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCMjq-qLw8fQCFQAAAAAdAAAAABAS", null));
>>>>>>> refs/remotes/origin/main

        imageSlider.setImageList(images);

    }
}