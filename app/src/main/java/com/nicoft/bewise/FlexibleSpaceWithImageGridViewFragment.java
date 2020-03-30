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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

public class FlexibleSpaceWithImageGridViewFragment extends FlexibleSpaceWithImageBaseFragment<ObservableGridView> {

    DatabaseHelper myDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flexiblespacewithimagegridview, container, false);

        final ObservableGridView gridView = (ObservableGridView) view.findViewById(R.id.scroll);
        // Set padding view for GridView. This is the flexible space.
        View paddingView = new View(getActivity());
        final int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                flexibleSpaceImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);

        gridView.addHeaderView(paddingView);
        //setDummyData(gridView);

        myDb = new DatabaseHelper(getActivity().getApplicationContext());

        ArrayList<CampoDeGridView> campos = new ArrayList<CampoDeGridView>();

        ArrayList<String> items = new ArrayList<>();
        Cursor res;
        if (((FlexibleSpaceWithImageWithViewPagerTabActivity)getActivity()).globalPos==0)
            res = myDb.getAllData(DatabaseHelper.TABLE_NAME_1);
        else if(((FlexibleSpaceWithImageWithViewPagerTabActivity)getActivity()).globalPos==1)
            res = myDb.getAllData(DatabaseHelper.TABLE_NAME_2);
        else
            res = myDb.getAllData(DatabaseHelper.TABLE_NAME_3);


        while (res.moveToNext()) {
            items.add(res.getString(1));
            campos.add(new CampoDeGridView(res.getString(1),R.drawable.b,res.getString(2),res.getString(3)));

        }
        ImageYTextGridAdapter imAd=new ImageYTextGridAdapter(getActivity().getApplicationContext(),campos);




        //gridView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_2, items));
        gridView.setAdapter(imAd);

        //TODO if only one house inmidiately do intent to that house.


        if(campos.size() == 1)
        {
            Intent intent = new Intent(getContext(), PpalActivity.class);
            intent.putExtra("URL", ((CampoDeGridView)gridView.getItemAtPosition(1)).getURL());
            intent.putExtra("NAME", ((CampoDeGridView)gridView.getItemAtPosition(1)).getTitle());
            startActivity(intent);
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),((CampoDeGridView)gridView.getItemAtPosition(position)).getURL(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), PpalActivity.class);
                intent.putExtra("URL", ((CampoDeGridView)gridView.getItemAtPosition(position)).getURL());
                intent.putExtra("NAME", ((CampoDeGridView)gridView.getItemAtPosition(position)).getTitle());
                startActivity(intent);

                //((FlexibleSpaceWithImageWithViewPagerTabActivity)getActivity()).changeInterf(gridView.getItemAtPosition(position).toString());
            }
        });



        // TouchInterceptionViewGroup should be a parent view other than ViewPager.
        // This is a workaround for the issue #117:
        // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
        gridView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));

        // Scroll to the specified offset after layout
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_SCROLL_Y)) {
            final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
            ScrollUtils.addOnGlobalLayoutListener(gridView, new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    int offset = scrollY % flexibleSpaceImageHeight;
                    setSelectionFromTop(gridView, 0, -offset);
                }
            });
            updateFlexibleSpace(scrollY, view);
        } else {
            updateFlexibleSpace(0, view);
        }

        gridView.setScrollViewCallbacks(this);

        updateFlexibleSpace(0, view);

        return view;
    }

    @SuppressWarnings("NewApi")
    @Override
    public void setScrollY(int scrollY, int threshold) {
        View view = getView();
        if (view == null) {
            return;
        }
        ObservableGridView gridView = (ObservableGridView) view.findViewById(R.id.scroll);
        if (gridView == null) {
            return;
        }
        View firstVisibleChild = gridView.getChildAt(0);
        if (firstVisibleChild != null) {
            int offset = scrollY;
            int position = 0;
            if (threshold < scrollY) {
                int baseHeight = firstVisibleChild.getHeight();
                position = scrollY / baseHeight;
                offset = scrollY % baseHeight;
            }
            setSelectionFromTop(gridView, position, -offset);
        }
    }

    @Override
    protected void updateFlexibleSpace(int scrollY, View view) {
        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);

        View listBackgroundView = view.findViewById(R.id.list_background);

        // Translate list background
        ViewHelper.setTranslationY(listBackgroundView, Math.max(0, -scrollY + flexibleSpaceImageHeight));

        // Also pass this event to parent Activity
        FlexibleSpaceWithImageWithViewPagerTabActivity parentActivity =
                (FlexibleSpaceWithImageWithViewPagerTabActivity) getActivity();
        if (parentActivity != null) {
            parentActivity.onScrollChanged(scrollY, (ObservableGridView) view.findViewById(R.id.scroll));
        }
    }

    /*
     * setSelectionFromTop method has been moved from ListView to AbsListView since API level 21,
     * so for API level 21-, we need to use other method to scroll with offset.
     * smoothScrollToPositionFromTop seems to work, but it's from API level 11.
     * We can't use GridView for Gingerbread.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setSelectionFromTop(ObservableGridView gridView, int position, int offset) {
        if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
            gridView.setSelectionFromTop(position, offset);
        } else if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
            gridView.smoothScrollToPositionFromTop(position, offset, 0);
        }
    }
}
