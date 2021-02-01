package com.example.myapplication2;

//import
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication2.adapter.RecyclerViewButtonAdapter;
import com.example.myapplication2.fragments.UserListFragment;
import com.example.myapplication2.pojo.UserList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnItemClickInterface, onButtonClickInterface {

    public APIInterface apiInterface;
    private RecyclerView recyclerViewButton;
    private ViewPager viewPager;
    private View backView;
    public RecyclerViewButtonAdapter adapterButton;
    public OkHttpInitialize okHttpInitialize = new OkHttpInitialize();
    public int totalPages;
    public int pressedButtonIndex, unPressedButtonIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewButton = findViewById(R.id.rv_button);
        viewPager = findViewById(R.id.view_pager);
        backView = findViewById(R.id.back_view);

        //Restore buttons and custom View state after screen rotation
        if (savedInstanceState != null) {
            pressedButtonIndex = savedInstanceState.getInt("currentSelectedIndex");
            Log.d("TAG", "pressedButtonIndex = " + pressedButtonIndex);
            unPressedButtonIndex = savedInstanceState.getInt("lastSelectedIndex");
            Log.d("TAG", "unPressedButtonIndex = " + unPressedButtonIndex);
            totalPages = savedInstanceState.getInt("totalPages");
            Log.d("TAG", "totalPages = " + totalPages);
            onSetAdapterButton(totalPages);
            onSetViewPager(totalPages);
            onRestoreButtonState(pressedButtonIndex, unPressedButtonIndex);
            onSetViewPagerListener();
        }

        //OkHttp initialize
        okHttpInitialize.okHttp();

        if (savedInstanceState == null & totalPages == 0) {
            apiInterface = APIClient.getClient().create(APIInterface.class);
            Call<UserList> firstCall = apiInterface.doGetUserList("1");
            firstCall.enqueue(new Callback<UserList>() {
                @Override
                public void onResponse(Call<UserList> call, Response<UserList> response) {
                    UserList userList = response.body();

                    totalPages = userList.totalPages;

                    onSetAdapterButton(totalPages);
                    onSetViewPager(totalPages);
                    onSetViewPagerListener();
                }

                @Override
                public void onFailure(Call<UserList> call, Throwable t) {
                    call.cancel();
                }
            });

        }
    }

    public void onSetAdapterButton(int totalPages) {
        adapterButton = new RecyclerViewButtonAdapter(totalPages, MainActivity.this);
        recyclerViewButton.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewButton.setAdapter(adapterButton);
        adapterButton.setOnButtonClickListener(MainActivity.this);
    }

    public void onSetViewPager(int totalPages) {
        PagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), totalPages);
        viewPager.setAdapter(pagerAdapter);
    }

    public void onSetViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position != adapterButton.currentSelectedIndex) {
                    onButtonClick(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onItemClick(String name, String lastName, String email, String avatarLink, int remoteId) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("lastName", lastName);
        intent.putExtra("email", email);
        intent.putExtra("avatar", avatarLink);
        intent.putExtra("remoteId", remoteId);
        startActivity(intent);
    }

    @Override
    public void onButtonClick(final int i) {
        if (adapterButton.currentSelectedIndex == i) {
        } else {
            adapterButton.saveLastSelectedIndex(adapterButton.currentSelectedIndex);
            adapterButton.saveCurrentSelectedIndex(i);
            viewPager.setCurrentItem(i);
            adapterButton.notifyItemChanged(adapterButton.currentSelectedIndex);
            adapterButton.notifyItemChanged(adapterButton.lastSelectedIndex);

            int width = (int) Utils.getWidth()/2;
                ObjectAnimator animation = ObjectAnimator.ofFloat(backView, "translationX",
                        adapterButton.currentSelectedIndex * width);
            animation.setDuration(250);
            animation.start();
        }
    }

    public void onRestoreButtonState(int clickedButton, int unClickedButton) {
        adapterButton.notifyItemChanged(clickedButton);
        adapterButton.notifyItemChanged(unClickedButton);

        int width = (int) Utils.getWidth()/2;
        ObjectAnimator animation = ObjectAnimator.ofFloat(backView, "translationX",
                adapterButton.currentSelectedIndex * width);
        animation.setDuration(250);
        animation.start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentSelectedIndex", adapterButton.currentSelectedIndex);
        outState.putInt("lastSelectedIndex", adapterButton.lastSelectedIndex);
        outState.putInt("totalPages", totalPages);
        Log.d("TAG", "adapterButton.currentSelectedIndex = " + adapterButton.currentSelectedIndex);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm, int totalPages) {
            super(fm, totalPages);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return(UserListFragment.newInstance(position));
        }

        @Override
        public int getCount() {
            return totalPages;
        }
    }

}