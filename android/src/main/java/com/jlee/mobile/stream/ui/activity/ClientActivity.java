package com.jlee.mobile.stream.ui.activity;

import android.animation.LayoutTransition;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.jlee.mobile.stream.R;
import com.jlee.mobile.stream.ui.fragment.BaseFragment;
import com.jlee.mobile.stream.ui.fragment.FragmentFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClientActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_bar)
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        searchView.setLayoutTransition(new LayoutTransition());

        setViewFragment(FragmentFactory.FragmentType.Viewer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setViewFragment(FragmentFactory.FragmentType type) {
        BaseFragment fragment = FragmentFactory.createNewFragment(type);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_fragment_container, fragment);
        transaction.addToBackStack(fragment.getFragmentId());
        transaction.commit();
    }
}
