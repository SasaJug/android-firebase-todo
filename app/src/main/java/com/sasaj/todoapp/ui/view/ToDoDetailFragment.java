package com.sasaj.todoapp.ui.view;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sasaj.todoapp.R;
import com.sasaj.todoapp.entity.ToDo;
import com.sasaj.todoapp.ui.list.ToDoListActivity;

/**
 * A fragment representing a single ToDo detail screen.
 * This fragment is either contained in a {@link ToDoListActivity}
 * in two-pane mode (on tablets) or a {@link ToDoDetailActivity}
 * on handsets.
 */
public class ToDoDetailFragment extends Fragment {
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
    public ToDoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            toDo = new ToDo("title", "description", getArguments().getLong(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(toDo.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.todo_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (toDo != null) {
            ((TextView) rootView.findViewById(R.id.todo_detail)).setText(toDo.getDescription() + "/n/r"+toDo.getId());
        }

        return rootView;
    }
}
