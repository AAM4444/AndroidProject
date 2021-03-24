package com.example.myapplication2;

//import
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    private RecyclerView recyclerViewButton;
    private ViewPager viewPager;
    private View backView;
    private RecyclerViewButtonAdapter adapterButton;
    private int totalPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewButton = findViewById(R.id.rv_button);
        viewPager = findViewById(R.id.view_pager);
        backView = findViewById(R.id.back_view);

        OkHttpInitialize okHttpInitialize = new OkHttpInitialize();
        okHttpInitialize.okHttp();

        //Restore buttons and custom View state after screen rotation
        if (savedInstanceState != null) {
            int pressedButtonIndex = savedInstanceState.getInt("currentSelectedIndex");
            int unPressedButtonIndex = savedInstanceState.getInt("lastSelectedIndex");
            totalPages = savedInstanceState.getInt("totalPages");

            onSetAdapterButton(totalPages);
            onSetViewPager(totalPages);
            onRestoreButtonState(pressedButtonIndex, unPressedButtonIndex);
            onSetViewPagerListener();
        }

        if (savedInstanceState == null & totalPages == 0) {
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
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

    public void onSetViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position != adapterButton.getCurrentSelectedIndex()) {
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
        if (adapterButton.getCurrentSelectedIndex() != i) {
            adapterButton.setLastSelectedIndex(adapterButton.getCurrentSelectedIndex());
            adapterButton.setCurrentSelectedIndex(i);
            adapterButton.notifyItemChanged(adapterButton.getCurrentSelectedIndex());
            adapterButton.notifyItemChanged(adapterButton.getLastSelectedIndex());
            viewPager.setCurrentItem(i);
            setBackView();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentSelectedIndex", adapterButton.getCurrentSelectedIndex());
        outState.putInt("lastSelectedIndex", adapterButton.getLastSelectedIndex());
        outState.putInt("totalPages", totalPages);
    }

    public void onSetAdapterButton(int totalPages) {
        adapterButton = new RecyclerViewButtonAdapter(totalPages);
        adapterButton.setOnButtonClickListener(this);
        recyclerViewButton.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewButton.setAdapter(adapterButton);
    }

    public void onSetViewPager(int totalPages) {
        PagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), totalPages);
        viewPager.setAdapter(pagerAdapter);
    }

    public void onRestoreButtonState(int clickedButtonIndex, int unClickedButtonIndex) {
        adapterButton.notifyItemChanged(clickedButtonIndex);
        adapterButton.notifyItemChanged(unClickedButtonIndex);
        setBackView();
    }

    private void setBackView() {
        int width = (int) Utils.getWidth()/2;
        ObjectAnimator animation = ObjectAnimator.ofFloat(backView, "translationX",
                adapterButton.getCurrentSelectedIndex() * width);
        animation.setDuration(250);
        animation.start();
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