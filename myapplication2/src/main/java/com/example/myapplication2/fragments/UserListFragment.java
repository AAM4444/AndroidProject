package com.example.myapplication2.fragments;

//import

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.activeandroid.query.Select;
import com.example.myapplication2.APIClient;
import com.example.myapplication2.APIInterface;
import com.example.myapplication2.OnItemClickInterface;
import com.example.myapplication2.R;
import com.example.myapplication2.UserInfo;
import com.example.myapplication2.adapter.RecyclerViewAdapter;
import com.example.myapplication2.pojo.UserList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListFragment extends Fragment{

    private int pageNumber;
    public APIInterface apiInterface;
    private RecyclerViewAdapter adapter;
    public ArrayList<UserInfo> userInfoArrayList;
    private List<UserList.Datum> datumList;
    public int currentVisiblePosition = 0;

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
        setRetainInstance(true);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.user_list_fragment, container, false);
         return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView recyclerView = view.findViewById(R.id.rv_list_user);
        final int i = getArguments() != null ? getArguments().getInt("num") : 1;

            apiInterface = APIClient.getClient().create(APIInterface.class);
            Call<UserList> secondCall = apiInterface.doGetUserList(" " + i);
            secondCall.enqueue(new Callback<UserList>() {
                @Override
                public void onResponse(Call<UserList> call, Response<UserList> response) {
                    UserList userList = response.body();

                    datumList = userList.data;

                    for (UserList.Datum datum : datumList) {
                        if (!isUserInDb(datum.id, i)) {
                            saveUserInDb(datum, i);
                        }
                    }
                    adapter = new RecyclerViewAdapter(getUsersListFromDb(pageNumber), getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener((OnItemClickInterface)getActivity());
                }

                @Override
                public void onFailure(Call<UserList> call, Throwable t) {
                    call.cancel();
                }
            });

        }

    public ArrayList<UserInfo> getUsersListFromDb(int pageSelected) {
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