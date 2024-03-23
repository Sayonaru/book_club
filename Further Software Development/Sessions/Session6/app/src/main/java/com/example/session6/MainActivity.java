package com.example.session6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import android.os.Bundle;

public class MainActivity extends FragmentActivity implements ToolbarFragment.ToolbarListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(int fontsize, String text) {
        FirstFragment firstFragment =
                (FirstFragment)
                        getSupportFragmentManager().findFragmentById(R.id.textView);

        firstFragment.changeTextProperties(fontsize, text);
    }
}