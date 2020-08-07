package com.david0926.sunrinhack2020;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.david0926.sunrinhack2020.databinding.ActivityMainBinding;
import com.david0926.sunrinhack2020.fragment.MainFragment1;
import com.david0926.sunrinhack2020.fragment.MainFragment2;
import com.david0926.sunrinhack2020.fragment.MainFragment3;
import com.david0926.sunrinhack2020.fragment.MainFragment4;

public class MainActivity extends AppCompatActivity {

    //private BroadcastReceiver broadcastReceiver;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.fabMain.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ChatActivity.class)));

        binding.bottomnavMain.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_1:
                    switchFragment(MainFragment1.newInstance());
                    break;
                case R.id.action_2:
                    switchFragment(MainFragment2.newInstance());
                    break;
                case R.id.action_3:
                    switchFragment(MainFragment3.newInstance());
                    break;
                case R.id.action_4:
                    switchFragment(MainFragment4.newInstance());
                    break;
            }
            return true;
        });
        switchFragment(MainFragment1.newInstance());
    }

    void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }

}