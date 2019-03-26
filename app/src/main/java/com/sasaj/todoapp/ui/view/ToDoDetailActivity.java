package com.sasaj.todoapp.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sasaj.todoapp.R;
import com.sasaj.todoapp.ui.common.BaseActivity;
import com.sasaj.todoapp.ui.edit.EditToDoDetailActivity;
import com.sasaj.todoapp.ui.list.ToDoListActivity;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ToDoListActivity}.
 */
public class ToDoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoDetailActivity.this, EditToDoDetailActivity.class);
                intent.putExtra(ToDoDetailFragment.ARG_ITEM_ID, getIntent().getStringExtra(ToDoDetailFragment.ARG_ITEM_ID));
                startActivity(intent);
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(ToDoDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ToDoDetailFragment.ARG_ITEM_ID));
            ToDoDetailFragment fragment = new ToDoDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.todo_detail_container, fragment)
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
