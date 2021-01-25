package com.example.myapplication2;

//import
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    public APIInterface apiInterface;
    private RecyclerView recyclerViewButton;
    private ViewPager viewPager;
    private View backView;
    private RecyclerViewButtonAdapter adapterButton;
    public OkHttpInitialize okHttpInitialize = new OkHttpInitialize();
    private int totalPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewButton = findViewById(R.id.rv_button);
        viewPager = findViewById(R.id.view_pager);
        backView = findViewById(R.id.back_view);

        //OkHttp initialize
        okHttpInitialize.okHttp();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UserList> firstCall = apiInterface.doGetUserList("1");
        firstCall.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                UserList userList = response.body();

                totalPages = userList.totalPages;

                adapterButton = new RecyclerViewButtonAdapter(totalPages, MainActivity.this);
                recyclerViewButton.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewButton.setAdapter(adapterButton);
                adapterButton.setOnButtonClickListener(MainActivity.this);

                ViewPager pager = findViewById(R.id.view_pager);
                PagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), totalPages);
                pager.setAdapter(pagerAdapter);

                pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        if(position != adapterButton.currentSelectedIndex) {
                            onButtonClick(position);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                call.cancel();
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