package com.sasaj.todoapp.ui.list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.sasaj.todoapp.R;
import com.sasaj.todoapp.entity.ToDo;
import com.sasaj.todoapp.ui.view.ToDoDetailActivity;
import com.sasaj.todoapp.ui.view.ToDoDetailFragment;

public class SimpleItemRecyclerViewAdapter extends FirebaseRecyclerAdapter<ToDo, SimpleItemRecyclerViewAdapter.ViewHolder> {


    public SimpleItemRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<ToDo> options) {
        super(options);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ToDo model) {
        final DatabaseReference todoRef = getRef(position);
        final String todoKey = todoRef.getKey();

        holder.title.setText(model.title);
        holder.description.setText(model.description);
        holder.itemView.setTag(model);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            if (mTwoPane) {
//                Bundle arguments = new Bundle();
//                arguments.putString(ToDoDetailFragment.ARG_TODO_KEY, item.timestamp);
//                ToDoDetailFragment fragment = new ToDoDetailFragment();
//                fragment.setArguments(arguments);
//                mParentActivity.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.todo_detail_container, fragment)
//                        .commit();
//            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ToDoDetailActivity.class);
                intent.putExtra(ToDoDetailFragment.ARG_TODO_KEY, todoKey);

                context.startActivity(intent);
            }
//        }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView description;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
        }
    }
}