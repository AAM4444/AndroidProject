package com.example.myapplication2.fragments;

//import
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplication2.R;

public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView UserFragment");
        return inflater.inflate(R.layout.fragment_user, container, false);
    }
}