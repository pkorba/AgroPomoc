package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * Displays detailed data about particular Product. Allows downloading Product data sheet.
 */
public class SingleProductActivity extends AppCompatActivity {
    private SingleProductViewModel mProductViewModel;
    private Button downloadButton;
    private long downloadID;
    private final String fileMimeType = "application/pdf";
    private String fileUriString;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        TextView productTypeLabel = findViewById(R.id.single_product_type_label);
        TextView productActiveIngredientLabel = findViewById(R.id.single_product_active_ingredient_label);
        TextView productCropLabel = findViewById(R.id.single_product_crop_label);
        TextView productPestLabel = findViewById(R.id.single_product_pest_label);
        TextView productDosageLabel = findViewById(R.id.single_product_dosage_label);
        TextView productDateLabel = findViewById(R.id.single_product_date_label);
        TextView productMinorUseLabel = findViewById(R.id.single_product_minor_use_label);
        TextView productProUseLabel = findViewById(R.id.single_product_pro_use_label);
        TextView productName = findViewById(R.id.single_product_name);
        TextView productType = findViewById(R.id.single_product_type);
        TextView productActiveIngredient = findViewById(R.id.single_product_active_ingredient);
        TextView productCrop = findViewById(R.id.single_product_crop);
        TextView productPest = findViewById(R.id.single_product_pest);
        TextView productDosage = findViewById(R.id.single_product_dosage);
        TextView productDate = findViewById(R.id.single_product_date);
        TextView productMinorUse = findViewById(R.id.single_product_minor_use);
        TextView productProUse = findViewById(R.id.single_product_pro_use);
        TextView productExpiryDate = findViewById(R.id.single_product_expiry_date);
        TextView productSellDate = findViewById(R.id.single_product_sell_date);
        TextView productUseDate = findViewById(R.id.single_product_use_date);
        downloadButton = findViewById(R.id.button_download_label);
        Intent intent = getIntent();

        int productId = intent.getIntExtra(ProductListAdapter.PRODUCT_ID, 0);
        mContext = this;
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mProductViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SingleProductViewModel.class);
        mProductViewModel.getProduct(productId).observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                // Display labels only for values which are not null.
                productName.setText(product.getNazwa());
                if (!product.getRodzaj().equals("null")) {
                    productType.setText(product.getRodzaj());
                    productTypeLabel.setVisibility(View.VISIBLE);
                    productType.setVisibility(View.VISIBLE);
                }
                if (!product.getSubstancjaCzynna().equals("null")) {
                    productActiveIngredient.setText(product.getSubstancjaCzynna());
                    productActiveIngredientLabel.setVisibility(View.VISIBLE);
                    productActiveIngredient.setVisibility(View.VISIBLE);
                }
                if (!product.getUprawa().equals("null")) {
                    productCrop.setText(product.getUprawa());
                    productCropLabel.setVisibility(View.VISIBLE);
                    productCrop.setVisibility(View.VISIBLE);
                }
                if (!product.getAgrofag().equals("null")) {
                    productPest.setText(product.getAgrofag());
                    productPestLabel.setVisibility(View.VISIBLE);
                    productPest.setVisibility(View.VISIBLE);
                }
                if (!product.getDawka().equals("null")) {
                    productDosage.setText(product.getDawka());
                    productDosageLabel.setVisibility(View.VISIBLE);
                    productDosage.setVisibility(View.VISIBLE);
                }
                if (!product.getTermin().equals("null")) {
                    productDate.setText(product.getTermin());
                    productDateLabel.setVisibility(View.VISIBLE);
                    productDate.setVisibility(View.VISIBLE);
                }
                if (!product.getMaloobszarowe().equals("null")) {
                    productMinorUse.setText(product.getMaloobszarowe());
                    productMinorUseLabel.setVisibility(View.VISIBLE);
                    productMinorUse.setVisibility(View.VISIBLE);
                }
                if (!product.getZastosowanieUzytkownik().equals("null")) {
                    productProUse.setText(product.getZastosowanieUzytkownik());
                    productProUseLabel.setVisibility(View.VISIBLE);
                    productProUse.setVisibility(View.VISIBLE);
                }

                Calendar expiryDate = Calendar.getInstance();
                expiryDate.setTimeInMillis(product.getTerminZezw());
                productExpiryDate.setText(String.format("%02d.%02d.%d", expiryDate.get(Calendar.DAY_OF_MONTH), expiryDate.get(Calendar.MONTH), expiryDate.get(Calendar.YEAR)));

                Calendar sellDate = Calendar.getInstance();
                sellDate.setTimeInMillis(product.getTerminZezw());
                productSellDate.setText(String.format("%02d.%02d.%d", sellDate.get(Calendar.DAY_OF_MONTH), sellDate.get(Calendar.MONTH), sellDate.get(Calendar.YEAR)));

                Calendar useDate = Calendar.getInstance();
                useDate.setTimeInMillis(product.getTerminZezw());
                productUseDate.setText(String.format("%02d.%02d.%d", useDate.get(Calendar.DAY_OF_MONTH), useDate.get(Calendar.MONTH), useDate.get(Calendar.YEAR)));

                if (product.getEtykieta().equals("null")) {
                    downloadButton.setEnabled(false);
                }

                downloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Check network connection
                        if (NetworkUtils.checkNetworkConnection(new WeakReference<>(getApplicationContext()))) {
                            // Download data sheet for a Product and get its unique ID.
                            downloadID = NetworkUtils.downloadLabel(getApplicationContext(), product.getEtykieta(), product.getNazwa());
                        } else {
                            Snackbar.make(v, R.string.no_internet_connection, 5);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    /**
     * BroadcastReceiver that reacts to intents announcing completed download process.
     * Creates snackbar with an option to open downloaded file.
     */
    public BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                DownloadManager dlMgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                long dlID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadID);
                Cursor cursor = dlMgr.query(query);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                        fileUriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        if (downloadID == dlID) {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), R.string.sor_product_downloaded_snackbar, Snackbar.LENGTH_INDEFINITE);
                            // When clicked, open the file.
                            snackbar.setAction(R.string.sor_product_downloaded_button_snackbar, new SingleProductActivity.updateClickListener());
                            snackbar.show();
                        }
                    }
                }
            }
        }
    };

    public class updateClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            openFile(fileUriString);
        }
    }

    /**
     * Uses FileProvider to get URI for downloaded file. Creates new intent to open file in default application
     * used to handle PDF files in user's system.
     * @param uriString Uri for a downloaded file provided by DownloadManager
     */
    private void openFile(String uriString) {
        Uri tagUri = Uri.parse(uriString);
        if (tagUri != null) {
            if (ContentResolver.SCHEME_FILE.equals(tagUri.getScheme())) {
                File file = new File(tagUri.getPath());
                tagUri = FileProvider.getUriForFile(mContext, "com.piotrkorba.provider", file);;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(tagUri, fileMimeType);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, getString(R.string.cannot_open_file_snackbar), Toast.LENGTH_LONG).show();
            }
        }
    }
}