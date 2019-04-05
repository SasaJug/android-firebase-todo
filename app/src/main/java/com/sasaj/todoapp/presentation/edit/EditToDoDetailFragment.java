package com.sasaj.todoapp.presentation.edit;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.sasaj.todoapp.TodoApplication;
import com.sasaj.todoapp.data.RepositoryImpl;
import com.sasaj.todoapp.domain.entities.ToDo;
import com.sasaj.todoapp.presentation.common.BaseActivity;

import javax.inject.Inject;

import static com.sasaj.todoapp.presentation.edit.EditTodoViewState.COMPLETED;
import static com.sasaj.todoapp.presentation.edit.EditTodoViewState.ERROR;
import static com.sasaj.todoapp.presentation.edit.EditTodoViewState.LOADING;
import static com.sasaj.todoapp.presentation.edit.EditTodoViewState.SUCCESS;
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

    @Inject
    public EditTodoVMFactory editTodoVMFactory;

    public EditTodoViewModel editTodoViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TodoApplication) getActivity().getApplication()).applicationComponent.inject(this);

        if (getArguments() != null && getArguments().containsKey(ARG_TODO_KEY)) {
            todoKey = getArguments().getString(ARG_TODO_KEY);
            todoQuery = RepositoryImpl.INSTANCE().getQueryForSingleUserTodo(todoKey);
        }
        userQuery = RepositoryImpl.INSTANCE().getQueryForUsers();

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editTodoViewModel = ViewModelProviders.of(this, editTodoVMFactory).get(EditTodoViewModel.class);
        editTodoViewModel.editTodoLiveData.observe(getActivity(), this::handleViewState);
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
        if (todoQuery != null && todoListener != null) {
            todoQuery.removeEventListener(todoListener);
        }
        if (userQuery != null && userListener != null) {
            userQuery.removeEventListener(userListener);
        }
    }

    public void saveToDo() {
        resetErrors();
        if (title.getText().toString().equals("") || description.getText().toString().equals("")) {
            if (title.getText().toString().equals("")) {
                titleLayout.setError(getString(R.string.title_error_msg));
            }
            if (description.getText().toString().equals("")) {
                descriptionLayout.setError(getString(R.string.description_error_msg));
            }
            return;
        } else {

            setEditingEnabled(false);
            editTodoViewModel.editToDo(todoKey, title.getText().toString(), description.getText().toString(), completed);
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

    private void handleViewState(EditTodoViewState editTodoViewState) {
        int state = editTodoViewState.state;
        switch (state) {
            case LOADING:
                Log.i(TAG, "handleViewState: loading");
                setEditingEnabled(false);
                break;
            case SUCCESS:
                Log.e(TAG, "handleViewState: success");
                setEditingEnabled(true);
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
            case ERROR:
                Log.e(TAG, "handleViewState: error");
                setEditingEnabled(true);
                if (getActivity() != null) {
                    ((BaseActivity) getActivity()).showErrorDialog(editTodoViewState.throwable);
                }
                break;
            case COMPLETED:
                Log.e(TAG, "handleViewState: completed");
                setEditingEnabled(true);
                break;
        }
    }
}
