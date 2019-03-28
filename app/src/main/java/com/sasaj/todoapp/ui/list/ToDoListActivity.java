package com.sasaj.todoapp.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.sasaj.todoapp.R;
import com.sasaj.todoapp.data.Repository;
import com.sasaj.todoapp.ui.common.BaseActivity;
import com.sasaj.todoapp.ui.edit.EditToDoDetailActivity;
import com.sasaj.todoapp.ui.view.ToDoDetailActivity;

/**
 * An activity representing a list of ToDos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ToDoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ToDoListActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 101;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private SimpleItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ToDoListActivity.this, EditToDoDetailActivity.class);
            startActivity(intent);
        });

        if (Repository.INSTANCE().getCurrentUser() == null) {
            startActivityForResult(
                    Repository.INSTANCE().getAuthUIIntent(),
                    RC_SIGN_IN);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Repository.INSTANCE().getCurrentUser() != null) {
            setContent();
        }
    }

    @Override
    protected void onStop() {
        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                Repository.INSTANCE().addUserToDatabase();
                setContent();
            } else {
                finish();
            }
        }
    }

    protected void setContent() {
        RecyclerView recyclerView = findViewById(R.id.todo_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        setupRecyclerView(recyclerView);
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new SimpleItemRecyclerViewAdapter(Repository.INSTANCE().getToDoListOptions());
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}
