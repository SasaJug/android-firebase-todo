package com.sasaj.todoapp.ui.view;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sasaj.todoapp.R;
import com.sasaj.todoapp.data.Repository;
import com.sasaj.todoapp.entity.ToDo;
import com.sasaj.todoapp.ui.list.ToDoListActivity;

/**
 * A fragment representing a single _ToDo detail screen.
 * This fragment is either contained in a {@link ToDoListActivity}
 * in two-pane mode (on tablets) or a {@link ToDoDetailActivity}
 * on handsets.
 */
public class ToDoDetailFragment extends Fragment {
    /**
     * The fragment argument representing the TODO_KEY that this fragment
     * represents.
     */
    public static final String ARG_TODO_KEY = "TODO_KEY";
    private static final String TAG = ToDoDetailFragment.class.getSimpleName();
    private Query todoReference;
    private String todoKey;
    private View rootView;
    private ValueEventListener todoListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ToDoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_TODO_KEY)) {
            todoKey = getArguments().getString(ARG_TODO_KEY);
            todoReference = Repository.INSTANCE().getQueryForSingleUserTodo(todoKey);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        todoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get _ToDo object and use the values to update the UI
                ToDo toDo = dataSnapshot.getValue(ToDo.class);
                if (toDo != null) {
                    Activity activity = ToDoDetailFragment.this.getActivity();
                    CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(toDo.title);
                    }
                    if (rootView != null) {
                        ((TextView) rootView.findViewById(R.id.todo_detail)).setText(toDo.description);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(ToDoDetailFragment.this.getActivity(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        todoReference.addValueEventListener(todoListener);
    }

    @Override
    public void onStop() {

        // Remove post value event listener
        if (todoListener != null) {
            todoReference.removeEventListener(todoListener);
        }
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.todo_detail, container, false);

        return rootView;
    }
}
