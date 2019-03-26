package com.sasaj.todoapp.ui.edit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.CollapsibleActionView;
import android.view.MenuItem;
import android.view.View;

import com.sasaj.todoapp.R;
import com.sasaj.todoapp.ui.common.BaseActivity;
import com.sasaj.todoapp.ui.list.ToDoListActivity;

/**
 * An activity representing a single Edit Item detail screen.
 */
public class EditToDoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            EditToDoDetailFragment fragment = new EditToDoDetailFragment();
            if(getIntent().getStringExtra(EditToDoDetailFragment.ARG_ITEM_ID) != null){
                Bundle arguments = new Bundle();
                arguments.putString(EditToDoDetailFragment.ARG_ITEM_ID,
                        getIntent().getStringExtra(EditToDoDetailFragment.ARG_ITEM_ID));
                fragment.setArguments(arguments);
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.edit_todo_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
