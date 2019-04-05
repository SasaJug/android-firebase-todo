package com.sasaj.todoapp.presentation.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sasaj.todoapp.R;
import com.sasaj.todoapp.data.Repository;
import com.sasaj.todoapp.domain.ToDo;
import com.sasaj.todoapp.presentation.list.ToDoListActivity;

/**
 * A fragment representing a single ToDo detail screen.
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

        if (getArguments() != null && getArguments().containsKey(ARG_TODO_KEY)) {
            todoKey = getArguments().getString(ARG_TODO_KEY);
            todoReference = Repository.INSTANCE().getQueryForSingleUserTodo(todoKey);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        todoListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get _ToDo object and use the values to update the UI
                ToDo toDo = dataSnapshot.getValue(ToDo.class);
                if (toDo != null && isAdded()) {
                    Activity activity = ToDoDetailFragment.this.getActivity();
                    if (activity != null) {
                        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                        if (appBarLayout != null) {
                            appBarLayout.setTitle(toDo.title);
                        }
                        if (rootView != null) {
                            ((TextView) rootView.findViewById(R.id.todo_detail)).setText(toDo.description);
                            if (toDo.completed) {
                                ((ImageView) rootView.findViewById(R.id.checkBox_view)).setImageResource(R.drawable.ic_check_box_black_24dp);
                            } else {
                                ((ImageView) rootView.findViewById(R.id.checkBox_view)).setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                if (isAdded()) {
                    Activity activity = ToDoDetailFragment.this.getActivity();
                    if (activity != null) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        Toast.makeText(activity, activity.getString(R.string.db_error_msg),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        todoReference.addValueEventListener(todoListener);
    }

    @Override
    public void onStop() {

        // Remove post value event listener
        if (todoReference != null && todoListener != null) {
            todoReference.removeEventListener(todoListener);
        }
        super.onStop();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.todo_detail, container, false);

        return rootView;
    }
}
