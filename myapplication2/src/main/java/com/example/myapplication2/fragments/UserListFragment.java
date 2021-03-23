package com.example.myapplication2.fragments;

//import
import android.os.AsyncTask;
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

import com.example.myapplication2.APIClient;
import com.example.myapplication2.APIInterface;
import com.example.myapplication2.MyApplication;
import com.example.myapplication2.OnItemClickInterface;
import com.example.myapplication2.R;
import com.example.myapplication2.User;
import com.example.myapplication2.UserDao;
import com.example.myapplication2.UsersDatabase;
import com.example.myapplication2.adapter.RecyclerViewAdapter;
import com.example.myapplication2.pojo.UserList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListFragment extends Fragment {

    private int pageNumber;
    private APIInterface apiInterface;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<UserList.Datum> datumList;
    private UserTask userTask;
    private List<User> userList;
    UsersDatabase db = MyApplication.getInstance().getDatabase();
    UserDao userDao = db.userDao();

    public static UserListFragment newInstance(int pageNumber) {
        Log.d("TAG", "UserListFragment fragment");
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putInt("num", pageNumber + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "onCreate fragment");
        setRetainInstance(true);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView fragment");
        View view = inflater.inflate(R.layout.user_list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG", "onViewCreated fragment");
        recyclerView = view.findViewById(R.id.rv_list_user);
        final int i = getArguments() != null ? getArguments().getInt("num") : 1;
        if (datumList == null) {
            apiInterface = APIClient.getClient().create(APIInterface.class);
            Call<UserList> secondCall = apiInterface.doGetUserList(" " + i);
            secondCall.enqueue(new Callback<UserList>() {
                @Override
                public void onResponse(Call<UserList> call, Response<UserList> response) {
                    Log.d("TAG", "onResponse");
                    UserList userList = response.body();
                    datumList = userList.data;

                    userTask = new UserTask();
                    userTask.execute();
                }

                @Override
                public void onFailure(Call<UserList> call, Throwable t) {
                    call.cancel();
                }
            });
        }
        if(datumList != null) {
            onSetAdapter();
        }

    }

    public void onSetAdapter() {
        Log.d("TAG", "onSetAdapter fragment");
        adapter = new RecyclerViewAdapter(userList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnItemClickInterface) getActivity());
    }

    class UserTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (UserList.Datum datum : datumList) {
                User user = new User();
                user.remoteId = datum.id;
                user.email = datum.email;
                user.firstName = datum.firstName;
                user.lastName = datum.lastName;
                user.avatar = datum.avatar;
                user.fromPage = pageNumber;
                userDao.insert(user);
            }
            userList = userDao.getUsersByPage(pageNumber);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onSetAdapter();
        }

//        public void onSetAdapter() {
//            adapter = new RecyclerViewAdapter(userList, getContext());
//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            recyclerView.setAdapter(adapter);
//            adapter.setOnItemClickListener((OnItemClickInterface) getActivity());
        }
    }


