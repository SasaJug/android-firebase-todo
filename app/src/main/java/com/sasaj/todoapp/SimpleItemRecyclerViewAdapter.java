package com.sasaj.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sasaj.todoapp.dummy.DummyContent;
import com.sasaj.todoapp.entity.ToDo;

import java.util.ArrayList;
import java.util.List;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final ToDoListActivity mParentActivity;
    private List<ToDo> mValues ;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ToDo item = (ToDo) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putLong(ToDoDetailFragment.ARG_ITEM_ID, item.getId());
                ToDoDetailFragment fragment = new ToDoDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.todo_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ToDoDetailActivity.class);
                intent.putExtra(ToDoDetailFragment.ARG_ITEM_ID, item.getId());

                context.startActivity(intent);
            }
        }
    };

    SimpleItemRecyclerViewAdapter(ToDoListActivity parent,
                                  boolean twoPane) {
        mParentActivity = parent;
        mValues = new ArrayList<>();
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ToDo toDo = mValues.get(position);
        holder.title.setText(toDo.getTitle());
        holder.description.setText(toDo.getDescription());

        holder.itemView.setTag(toDo);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    public void setToDos(List<ToDo> toDos){
        this.mValues = toDos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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