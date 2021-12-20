
package com.example.cookwhat.activities;

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

        String averylongstring ="wadasdadjkasdnkjasbfjksbdfjksdbfgjksbfgjkfbkjfsdbgjksdbfgkjsd\nasdfbjhasdbfhjsabdfasfasdfhjasbdfs\nafbjahdbsjfbsjhjafbdsfjas";
        ArrayList<SlideModel> images = new ArrayList<>();
        images.add(new SlideModel("https://upload.wikimedia.org/wikipedia/commons/9/99/IMG-20200708-WA0006.jpg", averylongstring,null));
        images.add(new SlideModel("https://upload.wikimedia.org/wikipedia/commons/9/99/IMG-20200708-WA0006.jpg", averylongstring,null));
        images.add(new SlideModel("https://upload.wikimedia.org/wikipedia/commons/9/99/IMG-20200708-WA0006.jpg", averylongstring,null));

        imageSlider.setImageList(images);

    }
}