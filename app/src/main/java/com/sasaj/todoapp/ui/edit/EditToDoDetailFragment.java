package com.sasaj.todoapp.ui.edit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sasaj.todoapp.R;
import com.sasaj.todoapp.data.Repository;
import com.sasaj.todoapp.entity.ToDo;
import com.sasaj.todoapp.entity.User;

import java.util.HashMap;
import java.util.Map;

import static com.sasaj.todoapp.ui.view.ToDoDetailFragment.ARG_TODO_KEY;

/**
 * A fragment representing a single edit item detail screen.
 */
public class EditToDoDetailFragment extends Fragment {

    private static final String TAG = EditToDoDetailFragment.class.getSimpleName();

    private android.support.design.widget.TextInputLayout titleLayout;
    private android.support.design.widget.TextInputLayout descriptionLayout;
    private EditText title;
    private EditText description;
    private Button saveButton;
    private Button cancelButton;

    private DatabaseReference database;
    private DatabaseReference todoReference;
    private String todoKey;
    private View rootView;
    /**
     * The item content this fragment is presenting.
     */
    private ToDo toDo;


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

            todoReference = Repository.getDatabase().getReference("user-todos")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(todoKey);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.edit_todo_detail, container, false);

        title = rootView.findViewById(R.id.title);
        description = rootView.findViewById(R.id.description);
        titleLayout = rootView.findViewById(R.id.titleLayout);
        descriptionLayout = rootView.findViewById(R.id.descriptionLayout);
        saveButton = rootView.findViewById(R.id.saveButton);
        cancelButton = rootView.findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDo();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener todoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get _ToDo object and use the values to update the UI
                toDo = dataSnapshot.getValue(ToDo.class);
                if (toDo != null) {
                    Activity activity = EditToDoDetailFragment.this.getActivity();
                    if(activity != null){
                        Toolbar toolbar = activity.findViewById(R.id.toolbar);
                        if (toolbar != null) {
                            toolbar.setTitle(toDo.title);
                        }
                        if (rootView != null) {
                            ((EditText) rootView.findViewById(R.id.title)).setText(toDo.title);
                            ((EditText) rootView.findViewById(R.id.description)).setText(toDo.description);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(EditToDoDetailFragment.this.getActivity(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        if (todoKey != null)
            todoReference.addValueEventListener(todoListener);
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

            database = Repository.getDatabase().getReference();
            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            database.child("users").child(userId).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);

                            // [START_EXCLUDE]
                            if (user == null) {
                                // User is null, error out
                                Log.e(TAG, "User " + userId + " is unexpectedly null");
                                Toast.makeText(getActivity(),
                                        "Error: could not fetch user.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // Write new post
                                writeNewTodo(userId, title.getText().toString(), description.getText().toString());
                            }

                            setEditingEnabled(true);
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            // [START_EXCLUDE]
                            setEditingEnabled(true);
                            // [END_EXCLUDE]
                        }
                    });
            // [END single_value_read]
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

    private void writeNewTodo(String userId, String title, String description) {
        String timestamp = Long.toString(System.currentTimeMillis());
        ToDo toDo = new ToDo(userId, title, description, timestamp);

        if(todoKey == null){
            todoKey = database.child("todos").push().getKey();
        }
        Map<String, Object> postValues = toDo.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/todos/" + todoKey, postValues);
        childUpdates.put("/user-todos/" + userId + "/" + todoKey, postValues);

        database.updateChildren(childUpdates);
    }
}
