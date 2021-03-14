package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class SingleProductActivity extends AppCompatActivity {
    private SingleProductViewModel mProductViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

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
        Intent intent = getIntent();

        int productId = intent.getIntExtra(ProductListAdapter.PRODUCT_ID, 0);

        mProductViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SingleProductViewModel.class);
        mProductViewModel.getProduct(productId).observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                productName.setText(product.getNazwa());
                productType.setText(product.getRodzaj());
                productActiveIngredient.setText(product.getSubstancjaCzynna());
                productCrop.setText(product.getUprawa());
                productPest.setText(product.getAgrofag());
                productDosage.setText(product.getDawka());
                productDate.setText(product.getTermin());
                productMinorUse.setText(product.getMaloobszarowe());
                productProUse.setText(product.getZastosowanieUzytkownik());

                // TODO: przekonwertowanie na datetime i dodanie warunkowego wyświetlania
            }
        });
    }
}