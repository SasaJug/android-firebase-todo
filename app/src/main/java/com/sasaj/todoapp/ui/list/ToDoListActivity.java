package com.sasaj.todoapp.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.sasaj.todoapp.R;
import com.sasaj.todoapp.ui.edit.EditToDoDetailActivity;
import com.sasaj.todoapp.ui.view.ToDoDetailActivity;
import com.sasaj.todoapp.entity.ToDo;
import com.sasaj.todoapp.ui.common.BaseActivity;
import com.sasaj.todoapp.ui.view.ToDoDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of ToDos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ToDoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ToDoListActivity extends BaseActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter simpleItemRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoListActivity.this, EditToDoDetailActivity.class);
                startActivity(intent);
            }
        });

        if (findViewById(R.id.todo_detail_container) != null) {
            mTwoPane = true;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            setContent();
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        simpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(this, mTwoPane);
        recyclerView.setAdapter(simpleItemRecyclerViewAdapter);
        List<ToDo> todos = new ArrayList<ToDo>();
        todos.add(new ToDo("Title1", "Description lorem ipsum 1", System.currentTimeMillis()));
        todos.add(new ToDo("Title2", "Description lorem ipsum 2", System.currentTimeMillis()));
        simpleItemRecyclerViewAdapter.setToDos(todos);
    }

    protected void setContent(){
        RecyclerView recyclerView = findViewById(R.id.todo_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

}
