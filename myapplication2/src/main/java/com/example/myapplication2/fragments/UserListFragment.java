package com.example.myapplication2.fragments;
//import
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.activeandroid.query.Select;
import com.example.myapplication2.APIClient;
import com.example.myapplication2.APIInterface;
import com.example.myapplication2.MainActivity;
import com.example.myapplication2.OnItemClickInterface;
import com.example.myapplication2.R;
import com.example.myapplication2.UserInfo;
import com.example.myapplication2.adapter.RecyclerViewAdapter;
import com.example.myapplication2.adapter.RecyclerViewButtonAdapter;
import com.example.myapplication2.onButtonClickInterface;
import com.example.myapplication2.pojo.UserList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListFragment extends Fragment{

    private int pageNumber;
    private RecyclerViewAdapter adapter;
    APIInterface apiInterface;
    ArrayList<UserInfo> userInfoArrayList;

    public static UserListFragment newInstance(int pageNumber) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putInt("num", pageNumber + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
        Log.d("TAG", "UserListFragment pageNumber " + pageNumber);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         Log.d("TAG", "UserListFragment onCreateView");
         View view = inflater.inflate(R.layout.user_list_fragment, container, false);
         return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView recyclerView = view.findViewById(R.id.rvListUser);

        final int i = getArguments() != null ? getArguments().getInt("num") : 1;
        Log.d("TAG", "OnViewCreated i = " + i);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UserList> secondCall = apiInterface.doGetUserList(" " + i);
        secondCall.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                Log.d("TAG", "onResponse");
                UserList userList = response.body();

                List<UserList.Datum> datumList = userList.data;

                for(UserList.Datum datum: datumList) {
                    if(!isUserInDb(datum.id, i)) {
                        saveUserInDb(datum, i);
                    }
                }

                adapter = new RecyclerViewAdapter(getUsersListFromDb(i), getActivity());
                Log.d("TAG", "RecyclerViewAdapter Data" + getUsersListFromDb(i));
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener((OnItemClickInterface)getActivity());

                for(int z = 0; z < getUsersListFromDb(i).size(); z++) {
                    System.out.println(getUsersListFromDb(i).get(z).firstName);
                }
            }
            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                call.cancel();
            }
        });

        Log.d("TAG", "UserListFragment onViewCreated");
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getUsersListFromDb(pageNumber), getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnItemClickInterface)getActivity());
    }

    public ArrayList<UserInfo> getUsersListFromDb(int pageSelected) {
        Log.d("TAG", "getUsersListFromDb");
        return new Select()
                .from(UserInfo.class)
                .where("from_page = " + pageSelected)
//                .orderBy("first_name DESC")
                .execute();
    }

    public boolean isUserInDb(int checkRemoteId, int pageSelected) {
        userInfoArrayList = getUsersListFromDb(pageSelected);
        boolean isExist = false;
        if (userInfoArrayList.isEmpty()) {
            isExist = false;
        } else {
            for (int i = 0; i < userInfoArrayList.size(); ) {
                if (checkRemoteId != userInfoArrayList.get(i).remoteId) {
                    i++;
                } else {
                    isExist = true;
                    break;
                }
                isExist = false;
            }
        }
        return isExist;
    }

    public void saveUserInDb(UserList.Datum datum, int pageSelected) {
        UserInfo userInfo = new UserInfo();
        userInfo.avatar = datum.avatar;
        userInfo.email = datum.email;
        userInfo.firstName = datum.firstName;
        userInfo.lastName = datum.lastName;
        userInfo.remoteId = datum.id;
        userInfo.fromPage = pageSelected;
        userInfo.save();
    }
}

