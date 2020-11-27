package com.example.voicerecorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<SplashScreenItem> listItemSplScreen;

   public SliderAdapter(Context context, List<SplashScreenItem> listSpl)
   {
       this.context = context;
       listItemSplScreen = listSpl;
   }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =layoutInflater.inflate(R.layout.slide_layout,null) ;

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(listItemSplScreen.get(position).getSlideImage());
        slideHeading.setText(listItemSplScreen.get(position).getSlideHeading());
        slideDescription.setText(listItemSplScreen.get(position).getSlideDesc());

        slideImageView.setTranslationX(900);
        slideHeading.setTranslationX(800);
        slideDescription.setTranslationX(800);


        slideHeading.setAlpha(0);
        slideDescription.setAlpha(0);

        slideImageView.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        slideHeading.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1200).start();
        slideDescription.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1400).start();

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return listItemSplScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);
    }
}
