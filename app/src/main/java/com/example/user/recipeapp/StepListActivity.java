package com.example.user.recipeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    String ingredient_to_widget;
    private boolean mTwoPane;
    static  Recipe recipes;
    Bundle bundle1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        TextView ingredients=findViewById(R.id.ingredients);


        Bundle bundle = getIntent().getBundleExtra("bundle");
        Recipe recipedetails = bundle.getParcelable("recipe");
        recipes = recipedetails;

        bundle1 = new Bundle();
        bundle1.putParcelable("recipe", recipedetails);
        String ingredientdetails, fullingredients = "";
        for (int i = 0; i < recipes.getIngredients().size(); i++) {
            if (recipes.getIngredients().get(i) != null) {
                ingredientdetails =(i+1)+"."+recipes.getIngredients().get(i).getIngredient() + "\t" + recipes.getIngredients().get(i).getQuantity() +" "+ recipes.getIngredients().get(i).getMeasure() + "\n";
                fullingredients += ingredientdetails;
            }
        }
        ingredients.setText(fullingredients);
        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        RecyclerView recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, recipes, mTwoPane, bundle1));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_widget, menu);
        return true;
    }

    public void add_to_widget(MenuItem item) {
        String ingredients,measure;
        Float quantity;
        ingredient_to_widget="";
        for (int i = 0; i < recipes.getIngredients().size(); i++) {
            if (recipes.getIngredients().get(i) != null) {
                /*ingredients = recipes.getIngredients().get(i).getIngredient() ;
                   quantity=  recipes.getIngredients().get(i).getQuantity() ;
                       measure= recipes.getIngredients().get(i).getMeasure() ;*/
                ingredients =(i+1)+"."+recipes.getIngredients().get(i).getIngredient() + "\t" + recipes.getIngredients().get(i).getQuantity() +" "+ recipes.getIngredients().get(i).getMeasure() + "\n";
                 ingredient_to_widget += ingredients;
            }

        }
        IngredientWidget.Text=recipes.getName()+"\n"+ingredient_to_widget;
        Toast.makeText(this, recipes.getName()+" added to widget", Toast.LENGTH_SHORT).show();



    }



    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final StepListActivity mParentActivity;
        private final Recipe mValues;
        private final boolean mTwoPane;
        private Bundle bundle;

        SimpleItemRecyclerViewAdapter(StepListActivity parent, Recipe items, boolean twoPane,Bundle bundle) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            this.bundle=bundle;

        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.steps.setText(position+1+"."+mValues.getSteps().get(position).getShortDescription());

        }

        @Override
        public int getItemCount() {
            return mValues.getSteps().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView steps;


            ViewHolder(View view) {
                super(view);

                steps = (TextView) view.findViewById(R.id.id_text);

                    steps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int posi=getLayoutPosition();
                            Step item = mValues.getSteps().get(posi);

                            if (mTwoPane) {
                                Bundle arguments = new Bundle();
                                arguments.putParcelable("adapterbundle", item);
                                StepDetailFragment fragment = new StepDetailFragment();
                                fragment.setArguments(arguments);
                                mParentActivity.getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.step_detail_container, fragment)
                                        .commit();
                            } else {
                                Context context = mParentActivity;
                                Intent intent = new Intent(context, StepDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("stepObj", item);
                                intent.putExtra("stepsdetails", bundle);
                                v.getContext().startActivity(intent);
                            }
                        }
                    });
                }
            }
        }

}
