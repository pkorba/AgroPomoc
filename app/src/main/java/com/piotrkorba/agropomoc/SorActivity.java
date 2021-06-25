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

/**
 * This class displays the list of Products in RecyclerView.
 * List is searchable using SearchView.
 * Elements in the list are clickable. When clicked, intent with element id is created to launch SingleProductActivity.
 * Class provides menu with option to update the ŚOR registry in database.
 * Class displays snackbar with reminder to update the database which has button that lets user start the update process.
 */
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

        // Create progress bar and textview to be displated when database is being updated in the background.
        mProgressBar = findViewById(R.id.progressBar);
        mUpdateTextview = findViewById(R.id.updateTextview);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new ProductListAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Add a separator between individual items in RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Set up ProductViewModel
        mProductViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);

        // Retrieve local prefererences
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        firstSnackbar = mPreferences.getBoolean(FIRST_SNACKBAR, false);

        // If mShowLoadingScreen changes in ViewModel, display Progressbar and hide RecyclerView
        mProductViewModel.mShowLoadingScreen.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mProgressBar.setVisibility(View.GONE);
                mUpdateTextview.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Change contents of RecyclerView based on content of allProductsFiltered object in ViewModel.
        mProductViewModel.allProductsFiltered.observe(this, new Observer<List<ProductCoreInfo>>() {
            @Override
            public void onChanged(List<ProductCoreInfo> productCoreInfos) {
                adapter.setProducts(productCoreInfos);
            }
        });

        // Let snackbar be displayed only if the day of the last check is different than current day.
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

        // Show snackbar only if the date is different (firstSnackbar value is true) and new registry version is available.
        mProductViewModel.mShowSnackbar.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (firstSnackbar && aBoolean) {
                    firstSnackbar = false;
                    Snackbar snackbar = Snackbar.make(recyclerView, R.string.sor_registry_update_available, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(R.string.sor_registry_update_button, new updateClickListener());
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
                // Launch update process - hide RecyclerView and show Progressbar, then run update
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

    /**
     * Launch update process - hide RecyclerView and show Progressbar, then run update when snackbar button is clicked.
     */
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