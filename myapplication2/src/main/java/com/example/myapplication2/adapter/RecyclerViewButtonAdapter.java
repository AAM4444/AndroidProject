package com.example.myapplication2.adapter;

//import
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication2.MainActivity;
import com.example.myapplication2.R;
import com.example.myapplication2.Utils;
import com.example.myapplication2.onButtonClickInterface;

public class RecyclerViewButtonAdapter extends RecyclerView.Adapter <RecyclerViewButtonAdapter.ViewHolder> {

    private onButtonClickInterface listener;
    public int lastSelectedIndex = -1, currentSelectedIndex = 0, totalPages;
    MainActivity mainActivity = new MainActivity();

    public RecyclerViewButtonAdapter(int totalPages, Context mContext) {
        this.totalPages = totalPages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater
                .from (viewGroup.getContext ())
                .inflate (R.layout.button_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        int displayWidth = Utils.getWidth();
            Log.d("TAG3", "displayWidth = " + displayWidth);
            Log.d("TAG3", "i = " + i);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
        lp.width = (displayWidth / 2);
        viewHolder.itemView.setLayoutParams(lp);

        Log.d("TAG2", "onBindViewHolder i = " + i);
        final int position = i + 1;
        viewHolder.pageButton.setText(" " + position);
        Log.d("TAG2", "onBindViewHolder currentSelectedIndex = " + currentSelectedIndex);
        Log.d("TAG2", "onBindViewHolder position = " + position);
//        Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
//        viewHolder.pageButton.startAnimation(shake);
        viewHolder.pageButton.setTextColor(ContextCompat.getColor(viewHolder.pageButton.getContext(),
                currentSelectedIndex == i ? R.color.colorPrimaryDark : R.color.colorAccent));
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button pageButton;

        public ViewHolder (@NonNull View itemView) {
            super (itemView);
            pageButton = itemView.findViewById(R.id.page_button);
        }
    }

    public void setOnButtonClickListener (onButtonClickInterface listener) {
        this.listener = listener;
    }

    public void saveLastSelectedIndex(int i) {
        lastSelectedIndex = i;
    }

    public void saveCurrentSelectedIndex(int i) {
        currentSelectedIndex = i;
    }

}
