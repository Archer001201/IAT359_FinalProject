package com.example.milestonemate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoRecyclerView extends RecyclerView.Adapter<TodoRecyclerView.ViewHolder>
{
    private List<TodoSlot> todoSlots;
    private Context context;


    public TodoRecyclerView(List<TodoSlot> slots, Context context){
        todoSlots = slots;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoRecyclerView.ViewHolder holder, int position) {
        holder.getTitleText().setText(todoSlots.get(position).getTitle());
        holder.getStateText().setText(todoSlots.get(position).getState());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TodoSlot currentSlot = todoSlots.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, DetailTodoActivity.class);
                intent.putExtra("title",currentSlot.getTitle());
                intent.putExtra("date",currentSlot.getDate());
                intent.putExtra("todo_id", currentSlot.getId());
                intent.putExtra("state", currentSlot.getState());
                intent.putExtra("description", currentSlot.getDescription());
                intent.putExtra("image_path", currentSlot.getImagePath());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoSlots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView titleText;
        private TextView stateText;

        public ViewHolder(View view){
            super(view);
            titleText = view.findViewById(R.id.todoTitle);
            stateText = view.findViewById(R.id.todoState);
        }

        public TextView getTitleText() { return titleText; }
        public TextView getStateText(){ return stateText; }
    }
}
