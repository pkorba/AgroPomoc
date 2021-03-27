package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SorActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ProductViewModel mProductViewModel;
    private ProductListAdapter adapter;
    private ProgressBar mProgressBar;
    private TextView mUpdateTextview;
    private RecyclerView recyclerView;
    private SharedPreferences mPreferences;
    private final String sharedPrefFile = "com.piotrkorba.agropomoc.agropomocsharedpref";
    private Boolean firstSnackbar = false;
    private final String FIRST_SNACKBAR = "com.piotrkorba.agropomoc.FIRST_SNACKBAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sor);

        mProgressBar = findViewById(R.id.progressBar);
        mUpdateTextview = findViewById(R.id.updateTextview);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new ProductListAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        mProductViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        firstSnackbar = mPreferences.getBoolean(FIRST_SNACKBAR, false);

        mProductViewModel.mShowLoadingScreen.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mProgressBar.setVisibility(View.GONE);
                mUpdateTextview.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        mProductViewModel.allProductsFiltered.observe(this, new Observer<List<ProductCoreInfo>>() {
            @Override
            public void onChanged(List<ProductCoreInfo> productCoreInfos) {
                adapter.setProducts(productCoreInfos);
            }
        });

        mProductViewModel.mDate.observe(this, new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                Date currentDate = new Date();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                if (!fmt.format(currentDate).equals(fmt.format(date))) {
                    firstSnackbar = true;
                    mProductViewModel.checkForUpdates(currentDate);
                }
            }
        });

        mProductViewModel.mShowSnackbar.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (firstSnackbar && aBoolean) {
                    firstSnackbar = false;
                    Snackbar snackbar = Snackbar.make(recyclerView, "Dostępna aktualizacja rejestru ŚOR", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Aktualizuj", new updateClickListener());
                    snackbar.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sor_menu, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                mProgressBar.setVisibility(View.VISIBLE);
                mUpdateTextview.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getString(R.string.check_update_text), Toast.LENGTH_SHORT).show();
                mProductViewModel.update();
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query != null) {
            mProductViewModel.searchForProducts(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (query != null) {
            mProductViewModel.searchForProducts(query);
        }
        return true;
    }

    public class updateClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mProgressBar.setVisibility(View.VISIBLE);
            mUpdateTextview.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            mProductViewModel.update();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putBoolean(FIRST_SNACKBAR, firstSnackbar);
        preferencesEditor.apply();
    }
}