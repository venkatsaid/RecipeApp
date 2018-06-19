package com.example.user.recipeapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.user.recipeapp.UrlGetter.BASE_URL;

public class NetworkUtilis {
    static Activity ct;
    List<Recipe> recipes;
    static int a;

    public NetworkUtilis(Activity ct) {
        this.ct = ct;
    }


    public static void getRecipes(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UrlGetter urlGetter=retrofit.create(UrlGetter.class);

        Call<List<Recipe>> call=urlGetter.getRecipe();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipeList=response.body();
                MainActivity.recyclerView.setLayoutManager(new GridLayoutManager(ct,a));
                MainActivity.recyclerView.setAdapter(new RecipeAdapter(ct,recipeList,1));


            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });

    }

}
