package com.haoyinrui.supermarketapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final List<Product> allProducts = new ArrayList<>();
    private LinearLayout layoutProductList;
    private TextView tvTotalPrice;
    private double cartTotalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutProductList = findViewById(R.id.layoutProductList);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        EditText etSearch = findViewById(R.id.etSearch);

        initProducts();
        renderProducts("");
        updateTotalPrice();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                renderProducts(s == null ? "" : s.toString().trim());
            }
        });
    }

    private void initProducts() {
        allProducts.clear();
        allProducts.add(new Product(R.string.product_water, 2.50, R.drawable.goods_water));
        allProducts.add(new Product(R.string.product_bread, 5.80, R.drawable.goods_bread));
        allProducts.add(new Product(R.string.product_milk, 12.90, R.drawable.goods_milk));
        allProducts.add(new Product(R.string.product_chips, 6.50, R.drawable.goods_chips));
        allProducts.add(new Product(R.string.product_coke, 3.50, R.drawable.goods_coke));
    }

    private void renderProducts(String keyword) {
        layoutProductList.removeAllViews();

        boolean hasMatchedProduct = false;
        for (Product product : allProducts) {
            String productName = getString(product.nameResId);
            if (keyword.isEmpty() || productName.contains(keyword)) {
                layoutProductList.addView(createProductCard(product));
                hasMatchedProduct = true;
            }
        }

        if (!hasMatchedProduct) {
            TextView emptyView = new TextView(this);
            emptyView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            emptyView.setPadding(dpToPx(12), dpToPx(28), dpToPx(12), dpToPx(28));
            emptyView.setGravity(Gravity.CENTER);
            emptyView.setText(R.string.empty_search_result);
            emptyView.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            layoutProductList.addView(emptyView);
        }
    }

    private View createProductCard(Product product) {
        String productName = getString(product.nameResId);

        LinearLayout cardLayout = new LinearLayout(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.bottomMargin = dpToPx(14);
        cardLayout.setLayoutParams(cardParams);
        cardLayout.setOrientation(LinearLayout.HORIZONTAL);
        cardLayout.setBackgroundResource(R.drawable.bg_product_card);
        cardLayout.setPadding(dpToPx(14), dpToPx(14), dpToPx(14), dpToPx(14));
        cardLayout.setGravity(Gravity.CENTER_VERTICAL);
        cardLayout.setElevation(dpToPx(2));

        ImageView ivProduct = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(dpToPx(88), dpToPx(88));
        imageParams.rightMargin = dpToPx(14);
        ivProduct.setLayoutParams(imageParams);
        ivProduct.setImageResource(product.imageResId);
        ivProduct.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivProduct.setContentDescription(getString(R.string.content_product_image, productName));
        cardLayout.addView(ivProduct);

        LinearLayout infoLayout = new LinearLayout(this);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        infoLayout.setLayoutParams(infoParams);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.addView(infoLayout);

        TextView tvName = new TextView(this);
        tvName.setText(productName);
        tvName.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tvName.setTypeface(tvName.getTypeface(), android.graphics.Typeface.BOLD);
        infoLayout.addView(tvName);

        TextView tvPrice = new TextView(this);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        priceParams.topMargin = dpToPx(6);
        tvPrice.setLayoutParams(priceParams);
        tvPrice.setText(String.format(Locale.CHINA, getString(R.string.label_price), product.price));
        tvPrice.setTextColor(ContextCompat.getColor(this, R.color.price_red));
        tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        tvPrice.setTypeface(tvPrice.getTypeface(), android.graphics.Typeface.BOLD);
        infoLayout.addView(tvPrice);

        LinearLayout quantityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams quantityLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        quantityLayoutParams.topMargin = dpToPx(12);
        quantityLayout.setLayoutParams(quantityLayoutParams);
        quantityLayout.setOrientation(LinearLayout.HORIZONTAL);
        quantityLayout.setGravity(Gravity.CENTER_VERTICAL);
        infoLayout.addView(quantityLayout);

        Button btnMinus = buildSmallButton(getString(R.string.action_decrease));
        quantityLayout.addView(btnMinus);

        TextView tvQuantity = new TextView(this);
        LinearLayout.LayoutParams quantityValueParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        quantityValueParams.leftMargin = dpToPx(12);
        quantityValueParams.rightMargin = dpToPx(12);
        tvQuantity.setLayoutParams(quantityValueParams);
        tvQuantity.setText(buildQuantityText(product.quantity));
        tvQuantity.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        tvQuantity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        quantityLayout.addView(tvQuantity);

        Button btnPlus = buildSmallButton(getString(R.string.action_increase));
        quantityLayout.addView(btnPlus);

        Button btnAddToCart = new Button(this);
        LinearLayout.LayoutParams addParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        addParams.topMargin = dpToPx(14);
        btnAddToCart.setLayoutParams(addParams);
        btnAddToCart.setBackgroundResource(R.drawable.bg_primary_button);
        btnAddToCart.setText(R.string.action_add_to_cart);
        btnAddToCart.setTextColor(ContextCompat.getColor(this, R.color.white));
        btnAddToCart.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        btnAddToCart.setAllCaps(false);
        btnAddToCart.setPadding(dpToPx(12), dpToPx(10), dpToPx(12), dpToPx(10));
        infoLayout.addView(btnAddToCart);

        btnMinus.setOnClickListener(v -> {
            if (product.quantity > 0) {
                product.quantity--;
                tvQuantity.setText(buildQuantityText(product.quantity));
            }
        });

        btnPlus.setOnClickListener(v -> {
            product.quantity++;
            tvQuantity.setText(buildQuantityText(product.quantity));
        });

        btnAddToCart.setOnClickListener(v -> {
            if (product.quantity == 0) {
                Toast.makeText(MainActivity.this, R.string.toast_select_quantity, Toast.LENGTH_SHORT).show();
                return;
            }

            cartTotalPrice += product.quantity * product.price;
            updateTotalPrice();
            Toast.makeText(
                    MainActivity.this,
                    getString(R.string.toast_added_to_cart, productName),
                    Toast.LENGTH_SHORT
            ).show();

            product.quantity = 0;
            tvQuantity.setText(buildQuantityText(product.quantity));
        });

        return cardLayout;
    }

    private Button buildSmallButton(String text) {
        Button button = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(40), dpToPx(40));
        button.setLayoutParams(params);
        button.setBackgroundResource(R.drawable.bg_quantity_button);
        button.setText(text);
        button.setTextColor(ContextCompat.getColor(this, R.color.primary_green_dark));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        button.setAllCaps(false);
        button.setPadding(0, 0, 0, 0);
        return button;
    }

    private void updateTotalPrice() {
        tvTotalPrice.setText(String.format(Locale.CHINA, getString(R.string.cart_total_format), cartTotalPrice));
    }

    private String buildQuantityText(int quantity) {
        return String.format(
                Locale.CHINA,
                getString(R.string.quantity_format),
                getString(R.string.label_quantity),
                quantity
        );
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    private static class Product {
        final int nameResId;
        final double price;
        final int imageResId;
        int quantity;

        Product(int nameResId, double price, int imageResId) {
            this.nameResId = nameResId;
            this.price = price;
            this.imageResId = imageResId;
            this.quantity = 0;
        }
    }
}
