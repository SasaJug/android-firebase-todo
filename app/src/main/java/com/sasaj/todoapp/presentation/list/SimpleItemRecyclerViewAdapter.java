package com.sasaj.todoapp.presentation.list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.sasaj.todoapp.R;
import com.sasaj.todoapp.data.Repository;
import com.sasaj.todoapp.domain.ToDo;
import com.sasaj.todoapp.presentation.view.ToDoDetailActivity;
import com.sasaj.todoapp.presentation.view.ToDoDetailFragment;

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
        if(model.completed){
            holder.checkBox.setImageResource(R.drawable.ic_check_box_black_24dp);
        } else {
            holder.checkBox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }

        holder.itemView.setTag(model);
        holder.itemView.setOnClickListener(view -> {

            Context context = view.getContext();
            Intent intent = new Intent(context, ToDoDetailActivity.class);
            intent.putExtra(ToDoDetailFragment.ARG_TODO_KEY, todoKey);

            context.startActivity(intent);
        });

        holder.checkBox.setOnClickListener(view -> {
            model.completed = !model.completed;
            Repository.INSTANCE().writeNewTodo(model.title, model.description, model.completed, todoKey);
            notifyDataSetChanged();
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView description;
        final ImageView checkBox;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            checkBox = view.findViewById(R.id.checkBox);
        }
    }
}