package id.putraprima.retrofit.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.Recipe;
import id.putraprima.retrofit.api.models.Recipes;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity {
    RecyclerView rv_resep;
    View rView;
    Button btn_load;
    int count = 0, page = 1;
    ProgressBar progressBar;

    ItemAdapter itemAdapter = new ItemAdapter();
    FastAdapter fastAdapter =FastAdapter.with(itemAdapter);
    List recipe = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        rView = findViewById(android.R.id.content).getRootView();
        rv_resep = findViewById(R.id.rv_resep);
        btn_load = findViewById(R.id.btn_load);

        progressBar = findViewById(R.id.progress);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getResep();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, 1500);
    }

    void getResep(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Recipes<Recipe>> call = service.getRecipe(page);
        call.enqueue(new Callback<Recipes<Recipe>>() {
            @Override
            public void onResponse(Call<Recipes<Recipe>> call, Response<Recipes<Recipe>> response) {
                if (response.isSuccessful()){

                    for (int i = 0; i < response.body().getData().size(); i++){
                        recipe.add(new Recipe(response.body().getData().get(i).getId(), response.body().getData().get(i).getNamaResep(), response.body().getData().get(i).getDeskripsi(), response.body().getData().get(i).getBahan(), response.body().getData().get(i).getLangkahPembuatan(), response.body().getData().get(i).getFoto()));
                        count = i;
                    }

                    setButton();

                    itemAdapter.add(recipe);
                    rv_resep.setAdapter(fastAdapter);

                    RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(RecipeActivity.this);
                    rv_resep.setLayoutManager(layoutManager);
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    Toast.makeText(RecipeActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Recipes<Recipe>> call, Throwable t) {
                setSnackbar(t.toString());
                System.out.println(t);
            }
        });
    }


    void setSnackbar(String r){
        Snackbar.make(rView, r, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public void handleMoreRecipe(View view) {
        progressBar.setVisibility(View.VISIBLE);
        page++;
        count = 0;
        itemAdapter.clear();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getResep();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, 500);
        System.out.println(page);
    }

    void setButton(){
        if (count < 9){
            btn_load.setEnabled(false);btn_load.setText("All Resep Loaded");
        }else{
            btn_load.setEnabled(true);
        }
    }
}
