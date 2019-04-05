package com.sasaj.todoapp.presentation.edit;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sasaj.todoapp.R;
import com.sasaj.todoapp.data.Repository;
import com.sasaj.todoapp.domain.ToDo;
import com.sasaj.todoapp.domain.User;

import static com.sasaj.todoapp.presentation.view.ToDoDetailFragment.ARG_TODO_KEY;

/**
 * A fragment representing a single edit item detail screen.
 */
public class EditToDoDetailFragment extends Fragment {

    private static final String TAG = EditToDoDetailFragment.class.getSimpleName();

    private android.support.design.widget.TextInputLayout titleLayout;
    private android.support.design.widget.TextInputLayout descriptionLayout;
    private EditText title;
    private EditText description;
    private ImageView checkBox;
    private Button saveButton;
    private Button cancelButton;
    private View rootView;

    private String todoKey;

    /**
     * The item content this fragment is presenting.
     */
    private ToDo toDo;
    private boolean completed = false;

    private Query todoQuery;
    private Query userQuery;
    private ValueEventListener todoListener;
    private ValueEventListener userListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EditToDoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_TODO_KEY)) {
            todoKey = getArguments().getString(ARG_TODO_KEY);
            todoQuery = Repository.INSTANCE().getQueryForSingleUserTodo(todoKey);
        }
        userQuery = Repository.INSTANCE().getQueryForUsers();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.edit_todo_detail, container, false);

        title = rootView.findViewById(R.id.title);
        description = rootView.findViewById(R.id.description);
        titleLayout = rootView.findViewById(R.id.titleLayout);
        descriptionLayout = rootView.findViewById(R.id.descriptionLayout);
        checkBox = rootView.findViewById(R.id.checkBox);
        saveButton = rootView.findViewById(R.id.saveButton);
        cancelButton = rootView.findViewById(R.id.cancelButton);

        checkBox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        checkBox.setOnClickListener(view -> {
            completed = !completed;
            if (completed) {
                checkBox.setImageResource(R.drawable.ic_check_box_black_24dp);
            } else {
                checkBox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
            }
        });

        saveButton.setOnClickListener(view -> saveToDo());

        cancelButton.setOnClickListener(view -> cancel());

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        todoListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get _ToDo object and use the values to update the UI
                toDo = dataSnapshot.getValue(ToDo.class);
                if (toDo != null) {
                    Activity activity = EditToDoDetailFragment.this.getActivity();
                    if (activity != null) {
                        Toolbar toolbar = activity.findViewById(R.id.toolbar);
                        if (toolbar != null) {
                            toolbar.setTitle(toDo.title);
                        }
                        if (rootView != null) {
                            title.setText(toDo.title);
                            description.setText(toDo.description);
                            if (toDo.completed) {
                                completed = true;
                                checkBox.setImageResource(R.drawable.ic_check_box_black_24dp);
                            } else {
                                completed = false;
                                checkBox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(EditToDoDetailFragment.this.getActivity(), "Failed to load ToDo.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        if (todoKey != null) {
            todoQuery.addValueEventListener(todoListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(todoQuery != null && todoListener != null){
            todoQuery.removeEventListener(todoListener);
        }
        if(userQuery != null && userListener != null){
            userQuery.removeEventListener(userListener);
        }
    }

    public void saveToDo() {
        resetErrors();
        if (title.getText().toString().equals("") || description.getText().toString().equals("")) {
            if (title.getText().toString().equals("")) {
                titleLayout.setError("Title must not be empty");
            }
            if (description.getText().toString().equals("")) {
                descriptionLayout.setError("Description must not be empty");
            }
            return;
        } else {

            setEditingEnabled(false);
            userListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Get user value
                    User user = dataSnapshot.getValue(User.class);

                    if (user == null) {
                        // User is null, error out
                        Log.e(TAG, "User is unexpectedly null");
                        Toast.makeText(getActivity(),
                                "Error: could not fetch user.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Write new todo
                        Repository.INSTANCE().writeNewTodo(title.getText().toString(), description.getText().toString(), completed, todoKey);
                    }

                    setEditingEnabled(true);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    setEditingEnabled(true);
                }
            };
            Repository.INSTANCE().getQueryForUsers().addListenerForSingleValueEvent(userListener);
        }
    }

    private void cancel() {
        if (getActivity() != null)
            getActivity().finish();
    }

    private void setEditingEnabled(boolean enabled) {
        if (title != null)
            title.setEnabled(enabled);

        if (description != null)
            description.setEnabled(enabled);

        if (saveButton != null)
            saveButton.setEnabled(enabled);

        if (cancelButton != null)
            cancelButton.setEnabled(enabled);
    }


    private void resetErrors() {
        titleLayout.setError(null);
        descriptionLayout.setError(null);
    }
}
