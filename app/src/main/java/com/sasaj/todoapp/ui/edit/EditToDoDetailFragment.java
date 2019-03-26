package com.sasaj.todoapp.ui.edit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasaj.todoapp.R;
import com.sasaj.todoapp.entity.ToDo;

/**
 * A fragment representing a single edit item detail screen.
 */
public class EditToDoDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

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

        Activity activity = this.getActivity();
        Toolbar toolbar = activity.findViewById(R.id.toolbar);

        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            toDo = new ToDo("title", "description", 111222333);
            if (toolbar != null) {
                toolbar.setTitle(toDo.getTitle());
            }
        } else {
            if (toolbar != null) {
                toolbar.setTitle("Create new todo");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_todo_detail, container, false);

        // Show the dummy content as text in a TextView.
//        if (toDo != null) {
//            ((TextView) rootView.findViewById(R.id.todo_detail)).setText(toDo.getDescription() + "/n/r"+toDo.getId());
//        }

        return rootView;
    }
}
