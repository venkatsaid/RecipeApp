package com.example.user.recipeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{
    Activity ct;
    int id;
    List<Recipe> recipeList;
    public RecipeAdapter(Activity context, List<Recipe> recipeList,int id) {
        this.ct=context;
        this.recipeList=recipeList;
        this.id=id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (id==1){
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipelist,parent,false);}
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final RecipeAdapter.ViewHolder holder, int position) {
        if (id==1) {
            holder.recipeName.setText(recipeList.get(position).getName());

        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (id==1){
            count=recipeList.size();
        }
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        public ViewHolder(View itemView) {
            super(itemView);
            if (id==1){
                recipeName=itemView.findViewById(R.id.recipe_name);}
                recipeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i=getLayoutPosition();
                        Intent intent = new Intent(ct, StepListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("recipe",recipeList.get(i));
                        intent.putExtra("bundle",bundle);
                        v.getContext().startActivity(intent);
                        ct.startActivity(intent);

                    }
                });

        }
    }
}
