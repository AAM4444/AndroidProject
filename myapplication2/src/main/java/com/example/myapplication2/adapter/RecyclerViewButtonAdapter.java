package com.example.myapplication2.adapter;

//import
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication2.R;
import com.example.myapplication2.Utils;
import com.example.myapplication2.onButtonClickInterface;

public class RecyclerViewButtonAdapter extends RecyclerView.Adapter <RecyclerViewButtonAdapter.ViewHolder> {

    private onButtonClickInterface listener;
    private final int totalPages;
    private int currentSelectedIndex, lastSelectedIndex = -1;

    public RecyclerViewButtonAdapter(int totalPages) {
        this.totalPages = totalPages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from (viewGroup.getContext ())
                .inflate (R.layout.button_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        int displayWidth = (int) Utils.getWidth();
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
        lp.width = (displayWidth / totalPages);
        viewHolder.itemView.setLayoutParams(lp);

        final int position = i + 1;
        viewHolder.pageButton.setText(" " + position);
        viewHolder.pageButton.setTextColor(ContextCompat.getColor(viewHolder.pageButton.getContext(),
                currentSelectedIndex == i ? R.color.color_white : R.color.color_black));

        viewHolder.pageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        listener.onButtonClick(i);
                }
            });
    }

    @Override
    public int getItemCount() {
        return totalPages;
    }

    public void setOnButtonClickListener (onButtonClickInterface listener) {
        this.listener = listener;
    }

    public int getCurrentSelectedIndex() {
        return currentSelectedIndex;
    }

    public void setCurrentSelectedIndex(int currentSelectedIndex) {
        this.currentSelectedIndex = currentSelectedIndex;
    }

    public int getLastSelectedIndex() {
        return lastSelectedIndex;
    }

    public void setLastSelectedIndex(int lastSelectedIndex) {
        this.lastSelectedIndex = lastSelectedIndex;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Button pageButton;

        public ViewHolder (@NonNull View itemView) {
            super (itemView);
            pageButton = itemView.findViewById(R.id.button);
        }

    }

}
