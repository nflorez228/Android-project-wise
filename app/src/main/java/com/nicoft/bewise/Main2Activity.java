package com.nicoft.bewise;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.blunderer.materialdesignlibrary.activities.NavigationDrawerActivity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarDefaultHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsMenuHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerBottomHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerStyleHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerTopHandler;
import com.blunderer.materialdesignlibrary.models.Account;


public class Main2Activity extends NavigationDrawerActivity {
    DatabaseLoginHelper myDb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseLoginHelper(getApplicationContext());

    }

        @Override
    public NavigationDrawerStyleHandler getNavigationDrawerStyleHandler() {
        return new NavigationDrawerStyleHandler();
    }

    @Override
    public NavigationDrawerAccountsHandler getNavigationDrawerAccountsHandler() {
        NavigationDrawerAccountsHandler ndah = new NavigationDrawerAccountsHandler(getApplicationContext());
        StringBuffer buffer = new StringBuffer();
        //Cursor res = myDb.getAllData();
        //while (res.moveToNext()) {

            //ndah.addAccount(res.getString(1), res.getString(2),
            ndah.addAccount("p", "p@p.com",
                    R.drawable.profile1, R.drawable.profile1_background);
        //}

        return ndah;
    }

    @Override
    public NavigationDrawerAccountsMenuHandler getNavigationDrawerAccountsMenuHandler() {
        return new NavigationDrawerAccountsMenuHandler(this);
                //.addAddAccount(new Intent(getApplicationContext(), AddAccountActivity.class))
                //.addManageAccounts(new Intent(getApplicationContext(), ManageAccountsActivity.class));
    }

    @Override
    public void onNavigationDrawerAccountChange(Account account) {
        Toast.makeText(getApplicationContext(), "Account changed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public NavigationDrawerTopHandler getNavigationDrawerTopHandler() {
        return new NavigationDrawerTopHandler(getApplicationContext())
                .addItem(R.string.title_item1, new ViewPagerWithTabsFragment());
                //.addItem(R.string.title_item2, new ScrollViewFragment())
                //.addSection(R.string.title_section2)
                //.addItem(R.string.title_item3, new ScrollViewFragment());
                //.addItem(R.string.title_item4, new Intent(this, MyActivity.class));
    }

    @Override
    public NavigationDrawerBottomHandler getNavigationDrawerBottomHandler() {
        return new NavigationDrawerBottomHandler(getApplicationContext())
                .addSettings(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        //startActivity(intent);
                    }

                })
                .addHelpAndFeedback(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //Intent intent = new Intent(getApplicationContext(), HelpAndFeedbackActivity.class);
                        //startActivity(intent);
                    }

                });
    }

    @Override
    public boolean overlayActionBar() {
        return true;
    }

    @Override
    public boolean replaceActionBarTitleByNavigationDrawerItemTitle() {
        return true;
    }

    @Override
    public int defaultNavigationDrawerItemSelectedPosition() {
        return 0;
    }

    @Override
    protected boolean enableActionBarShadow() {
        return true;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return new ActionBarDefaultHandler(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!isNavigationDrawerOpen())
        {
            moveTaskToBack(true);
        }
    }


}
