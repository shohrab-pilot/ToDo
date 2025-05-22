package com.example.yeshasprabhakar.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List; // Added import for List

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private List<TodoItem> todoItemList = new ArrayList<>(); // Changed to List<TodoItem>

    public ItemAdapter(Context context, List<TodoItem> initialList) { // Updated constructor parameter
        super();
        this.context = context;
        if (initialList != null) {
            this.todoItemList.addAll(initialList); // Initialize with initialList
        }
    }

    public void setItems(List<TodoItem> newTodoItems) {
        this.todoItemList.clear();
        this.todoItemList.addAll(newTodoItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.todoItemList.size(); // Use todoItemList
    }

    @Override
    public Object getItem(int position) {
        return todoItemList.get(position); // Use todoItemList
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) { // Added check for null convertView
            convertView = layoutInflater.inflate(R.layout.row_item, null);
        }
        TextView titleTextView = convertView.findViewById(R.id.title);
        TextView dateTextView = convertView.findViewById(R.id.dateTitle);
        TextView timeTextView = convertView.findViewById(R.id.timeTitle);
        final ImageView delImageView = convertView.findViewById(R.id.delete);
        // No need to setTag if we use the item from the list directly

        final View finalConvertView = convertView;
        final TodoItem currentItem = todoItemList.get(position); // Get current TodoItem

        //On delete icon click remove item from list and database
        delImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animSlideRight = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
                animSlideRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Fires when animation starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (context instanceof MainActivity) {
                            // Call MainActivity's delete method
                            ((MainActivity) context).deleteTodoItem(currentItem.getName(), currentItem.getDate(), currentItem.getTime());
                            // No need to call notifyDataSetChanged() here, LiveData will update the list.
                            // If not using LiveData for immediate UI update after delete, you might need to
                            // manually remove from todoItemList and call notifyDataSetChanged,
                            // but the preferred MVVM way is for MainActivity to observe and trigger adapter update.
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // ...
                    }
                });
                finalConvertView.startAnimation(animSlideRight);
            }
        });

        titleTextView.setText(currentItem.getName()); // Use getName() for title
        dateTextView.setText(currentItem.getDate());
        timeTextView.setText(currentItem.getTime());
        return convertView;
    }

    // Removed deleteItem and deleteItemFromDb methods as deletion is now handled by MainActivity

    //Create and call toast messages when necessary
    public void toastMsg(String msg) { // This method could potentially be removed if MainActivity handles all toasts
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
