package com.example.user.recipeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.list_item);
        NetworkUtilis networkUtilis=new NetworkUtilis(this);
        networkUtilis.a=colspan();
        networkUtilis.getRecipes();

    }
    public int colspan(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nCols = width/widthDivider;
        if (nCols<=2){
            return 2;
        }
        return nCols;
    }
}
