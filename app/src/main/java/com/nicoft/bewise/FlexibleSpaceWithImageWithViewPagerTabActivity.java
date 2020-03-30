/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nicoft.bewise;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.gc.materialdesign.views.ButtonFloat;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.rey.material.app.Dialog;

/**
 * <p>Another implementation of FlexibleImage pattern + ViewPager.</p>
 * <p/>
 * <p>This is a completely different approach comparing to FlexibleImageWithViewPager2Activity.
 * <p/>
 * <p>Descriptions of this pattern:</p>
 * <ul>
 * <li>When the current tab is changed, tabs will be translated in Y-axis
 * using scrollY of the new page's Fragment.</li>
 * <li>The parent Activity and children Fragments strongly depend on each other,
 * so if you need to use this pattern, maybe you should extract some interfaces from them.<br>
 * (This is just an example, so we won't do it here.)</li>
 * <li>The parent Activity and children Fragments communicate bidirectionally:
 * the parent Activity will update the Fragment's state when the tab is changed,
 * and Fragments will tell the parent Activity to update the tab's translationY.</li>
 * </ul>
 * <p/>
 * <p>SlidingTabLayout and SlidingTabStrip are from google/iosched:<br>
 * https://github.com/google/iosched</p>
 */
public class FlexibleSpaceWithImageWithViewPagerTabActivity extends BaseActivity
         {

    protected static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private ViewPager mPager;
    private NavigationAdapter mPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    private int mFlexibleSpaceHeight;
    private int mTabHeight;

    DatabaseLoginHelper myDb;
    DatabaseHelper myDb2;


    TextView titleView;


    String[] titles =new String[]{"Places","Rooms","Elements"};
    int globalPos=0;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    //private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar1);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);



        //mNavigationDrawerFragment = (NavigationDrawerFragment)
               // getFragmentManager().findFragmentById(R.id.Afragment_drawer1);

        // Set up the drawer.
        //mNavigationDrawerFragment.setup(R.id.Afragment_drawer1, (DrawerLayout) findViewById(R.id.drawer1), mToolbar);
        // populate the navigation drawer
        //mNavigationDrawerFragment.setUserData("John Doe", "johndoe@doe.com", BitmapFactory.decodeResource(getResources(), R.drawable.avatar));


        CircularImageView civ = (CircularImageView) findViewById(R.id.AimageRound);
        //civ.setw




        Bitmap bm =  BitmapFactory.decodeResource(this.getResources(),R.drawable.a);

        BlurBuilder bb = new BlurBuilder();
        Bitmap bmb=bb.blur(this,bm);


        KenBurnsView kbv = (KenBurnsView)findViewById(R.id.Aimage);
        kbv.setImageBitmap(bmb);
        kbv.setTransitionListener(new KenBurnsView.TransitionListener() {
            final KenBurnsView im = (KenBurnsView) findViewById(R.id.Aimage);

            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {

                final CircularImageView civ = (CircularImageView) findViewById(R.id.AimageRound);

                int fadeInDuration = 500; // Configure time values here
                int fadeOutDuration = 500;
                final Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
                fadeIn.setDuration(fadeInDuration);
                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
                fadeOut.setDuration(fadeOutDuration);
                fadeOut.reset();
                im.setAnimation(fadeOut);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                im.setAnimation(fadeIn);
                                Bitmap bm;
                                BlurBuilder bb = new BlurBuilder();
                                Bitmap bmb;

                                if (im.getTag().equals("a")) {
                                    bm=BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.b);
                                    bmb=bb.blur(getApplicationContext(), bm);
                                    im.setImageBitmap(bmb);

                                    civ.setImageResource(R.drawable.b);

                                    // im.setImageResource(R.drawable.b);
                                    im.setTag("b");
                                } else if (im.getTag().equals("b")) {
                                    bm=BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.c);
                                    bmb=bb.blur(getApplicationContext(), bm);
                                    im.setImageBitmap(bmb);

                                    civ.setImageResource(R.drawable.c);

                                    //im.setImageResource(R.drawable.c);
                                    im.setTag("c");
                                } else {
                                    bm=BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.a);
                                    bmb=bb.blur(getApplicationContext(), bm);
                                    im.setImageBitmap(bmb);

                                    civ.setImageResource(R.drawable.a);

                                    //im.setImageResource(R.drawable.a);
                                    im.setTag("a");
                                }

                            }
                        },fadeInDuration);
            }
        });


        myDb = new DatabaseLoginHelper(this);

        Cursor res = myDb.getAllData();

        while (res.moveToNext()) {
            if(res.getString(4).equals("YES")) {
                //mNavigationDrawerFragment.setUserData(res.getString(1), res.getString(2), BitmapFactory.decodeResource(getResources(), R.drawable.avatar));
                break;
            }

        }


        myDb2 = new DatabaseHelper(this);

        Cursor res2 = myDb2.getAllData(DatabaseHelper.TABLE_NAME_1);
        String[] toNavAdapter=new String[res2.getCount()];
        /*for (int i=0;i<res2.getCount();i++)
        {
            res2.moveToNext();
            toNavAdapter[i]=res2.getString(1);
        }*/



        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager(),toNavAdapter);
        mPager = (ViewPager) findViewById(R.id.Apager);
        mPager.setAdapter(mPagerAdapter);
        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);

        titleView = (TextView) findViewById(R.id.Atitle);
        titleView.setText(titles[globalPos]);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.Asliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mPager);

        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

                final ImageView im = (ImageView) findViewById(R.id.Aimage);


                int fadeInDuration = 500; // Configure time values here
                int timeBetween = 3000;
                int fadeOutDuration = 500;


                final Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
                fadeIn.setDuration(fadeInDuration);


                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
                //fadeOut.setStartOffset(fadeInDuration);
                fadeOut.setDuration(fadeOutDuration);

                //AnimationSet animation = new AnimationSet(false); // change to false
                //animation.addAnimation(fadeIn);
                //animation.addAnimation(fadeOut);
                //animation.setRepeatCount(1);

                fadeOut.reset();
                im.setAnimation(fadeOut);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                im.setAnimation(fadeIn);
                                switch (position) {
                                    case 0: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.a);
                                        im.setTag("a");
                                        break;
                                    }
                                    case 1: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.b);
                                        im.setTag("b");
                                        break;
                                    }
                                    case 2: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.c);
                                        im.setTag("c");
                                        break;
                                    }
                                    case 3: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.d);
                                        im.setTag("d");
                                        break;
                                    }
                                    case 4: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.e);
                                        im.setTag("e");
                                        break;
                                    }
                                    case 5: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.f);
                                        im.setTag("f");
                                        break;
                                    }
                                    case 6: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.g);
                                        im.setTag("g");
                                        break;
                                    }
                                    case 7: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.h);
                                        im.setTag("h");
                                        break;
                                    }
                                    case 8: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.i);
                                        im.setTag("i");
                                        break;
                                    }
                                    case 9: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.j);
                                        im.setTag("j");
                                        break;
                                    }
                                    case 10: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.k);
                                        im.setTag("k");
                                        break;
                                    }
                                    case 11: {
                                        fadeIn.reset();
                                        im.setImageResource(R.drawable.n);
                                        im.setTag("n");
                                        break;
                                    }
                                }

                            }
                        }, fadeInDuration);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // Initialize the first Fragment's state when layout is completed.
        ScrollUtils.addOnGlobalLayoutListener(mSlidingTabLayout, new Runnable() {
            @Override
            public void run() {
                translateTab(0, false);
            }
        });


        final ButtonFloat addButton = (ButtonFloat)findViewById(R.id.buttonFloat);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                Intent intent;
                                if(globalPos==0)
                                    intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
                                else if(globalPos==1)
                                    intent = new Intent(getApplicationContext(), AugmentedActivity.class);
                                else
                                    intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
                                //TODO verificar tiempo
                                startActivity(intent);
                            }
                        }, 300);

            }
        });

    }


    /**
     * Called by children Fragments when their scrollY are changed.
     * They all call this method even when they are inactive
     * but this Activity should listen only the active child,
     * so each Fragments will pass themselves for Activity to check if they are active.
     *
     * @param scrollY scroll position of Scrollable
     * @param s       caller Scrollable view
     */
    public void onScrollChanged(int scrollY, Scrollable s) {
        FlexibleSpaceWithImageBaseFragment fragment =
                (FlexibleSpaceWithImageBaseFragment) mPagerAdapter.getItemAt(mPager.getCurrentItem());
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }
        Scrollable scrollable = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollable == null) {
            return;
        }
        if (scrollable == s) {
            // This method is called by not only the current fragment but also other fragments
            // when their scrollY is changed.
            // So we need to check the caller(S) is the current fragment.
            int adjustedScrollY = Math.min(scrollY, mFlexibleSpaceHeight - mTabHeight);
            translateTab(adjustedScrollY, false);
            propagateScroll(adjustedScrollY);
        }
    }

    private void translateTab(int scrollY, boolean animated) {
        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        View imageView = findViewById(R.id.Aimage);
        View overlayView = findViewById(R.id.Aoverlay);
        TextView titleView = (TextView) findViewById(R.id.Atitle);

        // Translate overlay and image
        float flexibleRange = flexibleSpaceImageHeight - getActionBarSize();
        int minOverlayTransitionY = tabHeight*2- overlayView.getHeight();
        ViewHelper.setTranslationY(overlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(imageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));


        // Change alpha of overlay
        ViewHelper.setAlpha(overlayView, ScrollUtils.getFloat((float) scrollY / (flexibleSpaceImageHeight - getActionBarSize()*2), 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY - tabHeight) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle(titleView);
        ViewHelper.setPivotY(titleView, 0);
        ViewHelper.setScaleX(titleView, scale);
        ViewHelper.setScaleY(titleView, scale);

        // Translate title text
        int maxTitleTranslationY = flexibleSpaceImageHeight - tabHeight - getActionBarSize();
        int titleTranslationY = maxTitleTranslationY - scrollY;
        int titleTranslationX = scrollY/4;
        //Log.v("TAG",maxTitleTranslationY+"");
        if(titleTranslationY<0) {
            titleTranslationY=0;
            titleTranslationX=maxTitleTranslationY/4;
        }
        ViewHelper.setTranslationY(titleView, titleTranslationY);
        ViewHelper.setTranslationX(titleView, titleTranslationX);


        // If tabs are moving, cancel it to start a new animation.
        ViewPropertyAnimator.animate(mSlidingTabLayout).cancel();
        // Tabs will move between the top of the screen to the bottom of the image.
        float translationY = ScrollUtils.getFloat(-scrollY + mFlexibleSpaceHeight - mTabHeight, mTabHeight, mFlexibleSpaceHeight - mTabHeight);
        if (animated) {
            // Animation will be invoked only when the current tab is changed.
            ViewPropertyAnimator.animate(mSlidingTabLayout)
                    .translationY(translationY)
                    .setDuration(200)
                    .start();
        } else {
            // When Fragments' scroll, translate tabs immediately (without animation).
            ViewHelper.setTranslationY(mSlidingTabLayout, translationY);

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle(View view) {
        final TextView mTitleView = (TextView) view.findViewById(R.id.Atitle);
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(mTitleView, view.findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, 0);
        }
    }

    private void propagateScroll(int scrollY) {
        // Set scrollY for the fragments that are not created yet
        mPagerAdapter.setScrollY(scrollY);

        // Set scrollY for the active fragments
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            FlexibleSpaceWithImageBaseFragment f =
                    (FlexibleSpaceWithImageBaseFragment) mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            f.setScrollY(scrollY, mFlexibleSpaceHeight);
            f.updateFlexibleSpace(scrollY);
        }
    }

    /*@Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
        //mNavigationDrawerFragment.getMenu().add(new NavigationItem("itemsss 1", getResources().getDrawable(R.drawable.ic_menu_check)));

    }*/

    public void changeInterf(String item){
        globalPos++;
        //Toast.makeText(this,"Change interf called",Toast.LENGTH_SHORT).show();
        titleView.setText(titles[globalPos]);

        Cursor res2;
        if (globalPos==1)
            res2 = myDb2.getAllData(DatabaseHelper.TABLE_NAME_1);
        else if(globalPos==2)
            res2 = myDb2.getAllData(DatabaseHelper.TABLE_NAME_2);
        else
            res2 = myDb2.getAllData(DatabaseHelper.TABLE_NAME_3);

        String[] toNavAdapter=new String[res2.getCount()];
        for (int i=0;i<res2.getCount();i++)
        {
            res2.moveToNext();
            toNavAdapter[i]=res2.getString(1);
        }


        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager(),toNavAdapter);
        mPager.setAdapter(mPagerAdapter);
        mSlidingTabLayout.setViewPager(mPager);
    }

    public void changeInterfMinus(){
        globalPos--;
        //Toast.makeText(this,"Change interf called",Toast.LENGTH_SHORT).show();
        titleView.setText(titles[globalPos]);

        Cursor res2;
        if (globalPos==0)
            res2 = new Cursor() {
                @Override
                public int getCount() {
                    return 1;
                }

                @Override
                public int getPosition() {
                    return 0;
                }

                @Override
                public boolean move(int offset) {
                    return false;
                }

                @Override
                public boolean moveToPosition(int position) {
                    return false;
                }

                @Override
                public boolean moveToFirst() {
                    return false;
                }

                @Override
                public boolean moveToLast() {
                    return false;
                }

                @Override
                public boolean moveToNext() {
                    return true;
                }

                @Override
                public boolean moveToPrevious() {
                    return false;
                }

                @Override
                public boolean isFirst() {
                    return false;
                }

                @Override
                public boolean isLast() {
                    return false;
                }

                @Override
                public boolean isBeforeFirst() {
                    return false;
                }

                @Override
                public boolean isAfterLast() {
                    return false;
                }

                @Override
                public int getColumnIndex(String columnName) {
                    return 0;
                }

                @Override
                public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
                    return 0;
                }

                @Override
                public String getColumnName(int columnIndex) {
                    return null;
                }

                @Override
                public String[] getColumnNames() {
                    return new String[0];
                }

                @Override
                public int getColumnCount() {
                    return 0;
                }

                @Override
                public byte[] getBlob(int columnIndex) {
                    return new byte[0];
                }

                @Override
                public String getString(int columnIndex) {
                    return null;
                }

                @Override
                public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

                }

                @Override
                public short getShort(int columnIndex) {
                    return 0;
                }

                @Override
                public int getInt(int columnIndex) {
                    return 0;
                }

                @Override
                public long getLong(int columnIndex) {
                    return 0;
                }

                @Override
                public float getFloat(int columnIndex) {
                    return 0;
                }

                @Override
                public double getDouble(int columnIndex) {
                    return 0;
                }

                @Override
                public int getType(int columnIndex) {
                    return 0;
                }

                @Override
                public boolean isNull(int columnIndex) {
                    return false;
                }

                @Override
                public void deactivate() {

                }

                @Override
                public boolean requery() {
                    return false;
                }

                @Override
                public void close() {

                }

                @Override
                public boolean isClosed() {
                    return false;
                }

                @Override
                public void registerContentObserver(ContentObserver observer) {

                }

                @Override
                public void unregisterContentObserver(ContentObserver observer) {

                }

                @Override
                public void registerDataSetObserver(DataSetObserver observer) {

                }

                @Override
                public void unregisterDataSetObserver(DataSetObserver observer) {

                }

                @Override
                public void setNotificationUri(ContentResolver cr, Uri uri) {

                }

                @Override
                public Uri getNotificationUri() {
                    return null;
                }

                @Override
                public boolean getWantsAllOnMoveCalls() {
                    return false;
                }

                @Override
                public void setExtras(Bundle extras) {

                }

                @Override
                public Bundle getExtras() {
                    return null;
                }

                @Override
                public Bundle respond(Bundle extras) {
                    return null;
                }
            };
        else if(globalPos==1)
            res2 = myDb2.getAllData(DatabaseHelper.TABLE_NAME_1);
        else
            res2 = myDb2.getAllData(DatabaseHelper.TABLE_NAME_2);

        String[] toNavAdapter=new String[res2.getCount()];
        for (int i=0;i<res2.getCount();i++)
        {
            res2.moveToNext();
            toNavAdapter[i]=res2.getString(1);
        }


        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager(),toNavAdapter);
        mPager.setAdapter(mPagerAdapter);
        mSlidingTabLayout.setViewPager(mPager);
    }


    @Override
    public void onBackPressed() {
        /*if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else {
            if(globalPos>0)
                changeInterfMinus();
            else {
            super.onBackPressed();
            moveTaskToBack(true);
            }
        }*/

        moveTaskToBack(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            //return true;
        //}
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * This adapter provides three types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private String[] TITLES;

        private int mScrollY;

        public NavigationAdapter(FragmentManager fm,String[] titles) {
            super(fm);
            TITLES=titles;
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected Fragment createItem(int position) {
            FlexibleSpaceWithImageBaseFragment f;
            final int pattern = position % 4;
            f = new FlexibleSpaceWithImageGridViewFragment();
            /*switch (pattern) {
                case 0: {
                    f = new FlexibleSpaceWithImageScrollViewFragment();
                    break;
                }
                case 1: {
                    f = new FlexibleSpaceWithImageListViewFragment();
                    break;
                }
                case 2: {
                    f = new FlexibleSpaceWithImageRecyclerViewFragment();
                    break;
                }
                case 3:
                default: {
                    f = new FlexibleSpaceWithImageGridViewFragment();
                    break;
                }
            }*/
            f.setArguments(mScrollY);
            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }




}