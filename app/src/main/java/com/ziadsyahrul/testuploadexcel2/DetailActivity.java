package com.ziadsyahrul.testuploadexcel2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.ziadsyahrul.testuploadexcel2.Database.Common;
import com.ziadsyahrul.testuploadexcel2.Database.DataSource.CartRepository;
import com.ziadsyahrul.testuploadexcel2.Database.Local.CartDataSource;
import com.ziadsyahrul.testuploadexcel2.Database.Local.CartDatabase;
import com.ziadsyahrul.testuploadexcel2.Database.ModelDB.Cart;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class DetailActivity extends AppCompatActivity {

    EditText edt_date, edt_detail;
    Button btn_update, btn_delete;


    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        edt_date = findViewById(R.id.edt_date);
        edt_detail = findViewById(R.id.edt_detail);
        btn_delete = findViewById(R.id.btn_delete);
        btn_update = findViewById(R.id.btn_update);

        initDB();
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateData();
                finish();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                finish();
            }
        });


    }

    private void deleteData() {
        Cart cartItem = new Cart();
        cartItem.sno = Common.cartData.sno;
        cartItem.date = Common.cartData.date;
        cartItem.det = Common.cartData.det;
        Common.cartRepository.deleteCart(cartItem);

    }

    private void updateData() {

        try {
            Cart cartItem = new Cart();
            cartItem.sno = Common.cartData.sno;
            cartItem.date = edt_date.getText().toString();
            cartItem.det = edt_detail.getText().toString();
            Common.cartRepository.updateCart(cartItem);

            Log.d("READ_DATA_UPDATE ", new Gson().toJson(cartItem));
        } catch (Exception e) {
            Log.d("READ_ERROR ", e.getMessage());
        }
    }

    private void initDB() {
        Common.cartDatabase = CartDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
