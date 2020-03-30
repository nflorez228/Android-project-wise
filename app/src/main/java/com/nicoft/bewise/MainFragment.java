package com.nicoft.bewise;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment {

    private static final String ARG_TEXT = "text";
    String text;
    public MainFragment() {
        text="";
    }

    public static MainFragment newInstance(String text) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TEXT, text);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.textview, container, false);

        /*view.setVisibility(View.INVISIBLE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    // get the center for the clipping circle
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;

// get the final radius for the clipping circle
                    int finalRadius = Math.max(view.getWidth(), view.getHeight());

// create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

// make the view visible and start the animation
                    view.setVisibility(View.VISIBLE);
                    anim.start();
                }
            });
        }*/


        if (getArguments() != null && getArguments().containsKey(ARG_TEXT)) {
            text = getArguments().getString(ARG_TEXT);
        }
        return view;
    }

    public void onResume() {
        super.onResume();
        final ImageView im = (ImageView) getView().findViewById(R.id.imageView);
        final ImageView im2 = (ImageView) getView().findViewById(R.id.imageView2);
        final ImageView im3 = (ImageView) getView().findViewById(R.id.imageView3);
        final ImageView im4 = (ImageView) getView().findViewById(R.id.imageView4);
        final ImageView im5 = (ImageView) getView().findViewById(R.id.imageView5);
        final ImageView im6 = (ImageView) getView().findViewById(R.id.imageView6);

        im.setVisibility(View.INVISIBLE);
        im2.setVisibility(View.INVISIBLE);
        im3.setVisibility(View.INVISIBLE);
        im4.setVisibility(View.INVISIBLE);
        im5.setVisibility(View.INVISIBLE);
        im6.setVisibility(View.INVISIBLE);

        if(text.equals("2")){
            im.setImageResource(R.drawable.ic_remove_circle_outline_black_48dp);
            im2.setImageResource(R.drawable.ic_remove_circle_outline_black_48dp);
            im3.setImageResource(R.drawable.ic_remove_circle_outline_black_48dp);
            im4.setImageResource(R.drawable.ic_remove_circle_outline_black_48dp);
            im5.setImageResource(R.drawable.ic_remove_circle_outline_black_48dp);
            im6.setImageResource(R.drawable.ic_remove_circle_outline_black_48dp);


        }


        int fadeInDuration = 700;
        int init=1000;
        long divider=(new Double(5)).longValue();

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        Animation fadeIn2 = new AlphaAnimation(0, 1);
        fadeIn2.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn2.setDuration(fadeInDuration);

        Animation fadeIn3 = new AlphaAnimation(0, 1);
        fadeIn3.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn3.setDuration(fadeInDuration);


        final AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
        animation.setRepeatCount(1);

        final AnimationSet animation2 = new AnimationSet(false); // change to false
        animation2.addAnimation(fadeIn2);
        animation2.setRepeatCount(1);

        final AnimationSet animation3 = new AnimationSet(false); // change to false
        animation3.addAnimation(fadeIn3);
        animation3.setRepeatCount(1);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //Toast.makeText(getActivity().getApplicationContext(),"onresume",Toast.LENGTH_SHORT).show();
                        im.setVisibility(View.VISIBLE);
                        im.setAnimation(animation);
                    }
                }, init);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        im2.setVisibility(View.VISIBLE);
                        im2.setAnimation(animation2);
                        animation2.start();
                        im3.setVisibility(View.VISIBLE);
                        im3.setAnimation(animation2);

                    }
                }, init+fadeInDuration/divider);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        im4.setVisibility(View.VISIBLE);
                        animation3.start();
                        im5.setVisibility(View.VISIBLE);
                        im4.setAnimation(animation3);
                        im5.setAnimation(animation3);

                    }
                }, init+fadeInDuration/divider*2);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        im.setAnimation(null);
                        animation.reset();
                        im6.setVisibility(View.VISIBLE);
                        animation.start();
                        im6.setAnimation(animation);
                    }
                }, init+fadeInDuration/divider*3);

    }

}
