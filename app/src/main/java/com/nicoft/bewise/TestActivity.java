package com.nicoft.bewise;/*
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
import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

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
public class TestActivity extends Main2Activity {


}