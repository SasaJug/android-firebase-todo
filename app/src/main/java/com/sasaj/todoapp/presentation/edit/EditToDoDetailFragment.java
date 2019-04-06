package com.sasaj.todoapp.presentation.edit;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sasaj.todoapp.R;
import com.sasaj.todoapp.TodoApplication;
import com.sasaj.todoapp.data.RepositoryImpl;
import com.sasaj.todoapp.domain.entities.ToDo;
import com.sasaj.todoapp.presentation.common.BaseActivity;

import javax.inject.Inject;

import static com.sasaj.todoapp.presentation.edit.EditToDoViewState.COMPLETED;
import static com.sasaj.todoapp.presentation.edit.EditToDoViewState.ERROR;
import static com.sasaj.todoapp.presentation.edit.EditToDoViewState.LOADING;
import static com.sasaj.todoapp.presentation.edit.EditToDoViewState.SUCCESS;
import static com.sasaj.todoapp.presentation.edit.EditToDoViewState.SUCCESS_LOADING_TODO;
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

    private boolean completed = false;

    @Inject
    public EditToDoVMFactory editToDoVMFactory;
    public EditToDoViewModel editToDoViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EditToDoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TodoApplication) getActivity().getApplication()).applicationComponent.inject(this);

        if (getArguments() != null && getArguments().containsKey(ARG_TODO_KEY)) {
            todoKey = getArguments().getString(ARG_TODO_KEY);
        }

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

        editToDoViewModel = ViewModelProviders.of(this, editToDoVMFactory).get(EditToDoViewModel.class);
        editToDoViewModel.editTodoLiveData.observe(getActivity(), this::handleViewState);
        editToDoViewModel.getTodoLiveData.observe(getActivity(), this::handleViewState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(todoKey != null){
            editToDoViewModel.getToDo(todoKey);
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
            editToDoViewModel.editToDo(todoKey, title.getText().toString(), description.getText().toString(), completed);
        }
    }

    private void handleViewState(EditToDoViewState editToDoViewState) {
        int state = editToDoViewState.state;
        switch (state) {
            case LOADING:
                setEditingEnabled(false);
                break;
            case SUCCESS:
                setEditingEnabled(true);
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
            case SUCCESS_LOADING_TODO:
                setEditingEnabled(true);
                setTodo(editToDoViewState.todo);
                break;
            case ERROR:
                setEditingEnabled(true);
                if (getActivity() != null) {
                    ((BaseActivity) getActivity()).showErrorDialog(editToDoViewState.throwable);
                }
                break;
            case COMPLETED:
                setEditingEnabled(true);
                break;
        }
    }

    private void setTodo(ToDo todo) {
        if (todo != null) {
            Activity activity = EditToDoDetailFragment.this.getActivity();
            if (activity != null) {
                Toolbar toolbar = activity.findViewById(R.id.toolbar);
                if (toolbar != null) {
                    toolbar.setTitle(todo.title);
                }
                if (rootView != null) {
                    title.setText(todo.title);
                    description.setText(todo.description);
                    if (todo.completed) {
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
