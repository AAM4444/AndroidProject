//import
package com.example.myapplication2;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import com.activeandroid.query.Select;
import com.example.myapplication2.adapter.RecyclerViewAdapter;
import com.example.myapplication2.adapter.RecyclerViewButtonAdapter;
import com.example.myapplication2.fragments.UserListFragment;
import com.example.myapplication2.pojo.UserList;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnItemClickInterface, onButtonClickInterface {

    APIInterface apiInterface;
    RecyclerView recyclerViewButton;
    ArrayList<UserInfo> userInfoArrayList;
    ViewPager pager;
    View backView;
    private static final String TAG = "myLogs";
    private RecyclerViewButtonAdapter adapterButton;
    public int totalPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewButton = findViewById(R.id.rvButton);
        pager = findViewById(R.id.viewpager);
        backView = findViewById(R.id.BackView);


        //Logger
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        //Stetho
            Stetho.initializeWithDefaults(this);
            Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
            initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
            initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));
            Stetho.Initializer initializer = initializerBuilder.build();
            Stetho.initialize(initializer);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UserList> firstCall = apiInterface.doGetUserList("1");
        firstCall.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                    Log.d("TAG", "MainActivity.onResponse");
                UserList userList = response.body();

                totalPages = userList.totalPages;

                    Log.d("TAG", "MainActivity recyclerViewButton.setAdapter");
                adapterButton = new RecyclerViewButtonAdapter(totalPages, MainActivity.this);
                recyclerViewButton.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewButton.setAdapter(adapterButton);
                adapterButton.setOnButtonClickListener(MainActivity.this);

                    Log.d("TAG", "MainActivity ViewPager.setAdapter");
                ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
                PagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), totalPages);
                    Log.d("TAG", "MainActivity totalPages = " + totalPages);
                pager.setAdapter(pagerAdapter);
                pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if(position != adapterButton.currentSelectedIndex) {
                            Log.d("TAG2", "onPageSelected position = " + position);
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
                    Log.d("TAG", "MainActivity.onFailure");
                call.cancel();
            }
        });

    }

    @Override
    public void onItemClick(String name, String lastName, String email, String avatarLink, int remoteId) {
            Log.d("TAG", "MainActivity.onItemClick");
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("last_name", lastName);
        intent.putExtra("email", email);
        intent.putExtra("avatar", avatarLink);
        intent.putExtra("remoteId", remoteId);
        startActivity(intent);
    }

    public ArrayList<UserInfo> getUsersListFromDb(int pageSelected) {
            Log.d("TAG", "MainActivity.getUsersListFromDb");
        return new Select()
                .from(UserInfo.class)
                .where("from_page = " + pageSelected)
                .orderBy("first_name DESC")
                .execute();
    }

    @Override
    public void onButtonClick(final int i) {
                Log.d("TAG2", "onButtonClick currentSelectedIndex = " + adapterButton.currentSelectedIndex);
                Log.d("TAG2", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!onButtonClick i = " + i);
        if (adapterButton.currentSelectedIndex == i) {
                Log.d("TAG2", "onButtonClick return");
            return;
        } else {
                Log.d("TAG2", "saveLastSelectedIndex = " + adapterButton.currentSelectedIndex);
            adapterButton.saveLastSelectedIndex(adapterButton.currentSelectedIndex);
                Log.d("TAG2", "saveCurrentSelectedIndex = " + i);
            adapterButton.saveCurrentSelectedIndex(i);
                Log.d("TAG2", "onButtonClick setCurrentItem i = " + i);
            pager.setCurrentItem(i);
                Log.d("TAG2", "notifyItemChanged i = " + i);
            adapterButton.notifyItemChanged(adapterButton.currentSelectedIndex);
            adapterButton.notifyItemChanged(adapterButton.lastSelectedIndex);


            //Animation
                ObjectAnimator animation = ObjectAnimator.ofFloat(backView, "translationX",
                        adapterButton.currentSelectedIndex*275f);
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
            Log.d("TAG", "ViewPagerAdapter");
            return(UserListFragment.newInstance(position));
        }

        @Override
        public int getCount() {
            return totalPages;
        }
    }

}