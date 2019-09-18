package com.ziadsyahrul.testuploadexcel2;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ziadsyahrul.testuploadexcel2.Database.Common;
import com.ziadsyahrul.testuploadexcel2.Database.DataSource.CartRepository;
import com.ziadsyahrul.testuploadexcel2.Database.Local.CartDataSource;
import com.ziadsyahrul.testuploadexcel2.Database.Local.CartDatabase;
import com.ziadsyahrul.testuploadexcel2.Database.ModelDB.Cart;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    String TAG = "main";
    private TextView textView;

    RecyclerView recycler_main;
    MainAdapter mainAdapter;
    List<Cart> cartList = new ArrayList<>();

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();
        textView = findViewById(R.id.textView);
        recycler_main = findViewById(R.id.recycler_main);
        recycler_main.setLayoutManager(new LinearLayoutManager(this));
        recycler_main.setHasFixedSize(true);


        initDB();
        readExcelFileFromAssets();

        if (Common.cartRepository.getCartItems() != null) {
            getData();
        } else {

            Toast.makeText(this, "Data Tidak Ada", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void getData() {
        compositeDisposable.add(Common.cartRepository.getCartItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {

                        displayData(carts);
                    }
                }));
    }

    private void displayData(List<Cart> carts) {
        cartList = carts;
        mainAdapter = new MainAdapter(this, carts);
        mainAdapter.notifyDataSetChanged();
        recycler_main.setAdapter(mainAdapter);

    }


    private void initDB() {
        Common.cartDatabase = CartDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
    }

    private void readExcelFileFromAssets() {

        try {
            InputStream myInput;
            AssetManager assetManager = getAssets();
            myInput = assetManager.open("excelsheet.xls");
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkbook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkbook.getSheetAt(0);

            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno = 0;
            textView.append("\n");
            while (rowIter.hasNext()) {
                Log.e(TAG, "row no : " + rowno);
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if (rowno != 0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno = 0;
                    String date = "", det = "";
                    double sno = 0;


                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno == 0) {
                            sno = Double.parseDouble(myCell.toString());
                        } else if (colno == 1) {
                            date = myCell.toString();
                        } else if (colno == 2) {
                            det = myCell.toString();
                        }

                        colno++;
                        Log.e(TAG, " Index : " + myCell.getColumnIndex() + " -- " + myCell.toString());
                    }

                    textView.append(sno + " -- " + date + " -- " + det + "\n");

                    if (Common.cartRepository.getCartItems() == Common.cartRepository.getCartItems()) {

                        try {

                            // Add to SQLITE
                            // CREATE NEW CART ITEM
                            Cart cartItem = new Cart();
                            cartItem.sno = (int) Math.round(sno);
                            cartItem.date = date;
                            cartItem.det = det;

                            // Add TO DB
                            Common.cartRepository.insertToCart(cartItem);


                            Log.d("READ_DATA_INSERT ", new Gson().toJson(cartItem));

                        } catch (Exception e) {
                            Log.d("READ_ERROR ", e.getMessage());
                        }
                    } else {
                        Toast.makeText(this, "Data Tidak Ada", Toast.LENGTH_SHORT).show();
                    }


                }
                rowno++;
            }

        } catch (IOException e) {
            Log.e(TAG, "Error : " + e.toString());
        }
    }
}
