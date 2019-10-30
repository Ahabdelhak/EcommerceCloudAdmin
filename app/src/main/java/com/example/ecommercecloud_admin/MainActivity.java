package com.example.ecommercecloud_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

public class MainActivity extends AppCompatActivity implements AsyncCallback<Cart>, IPickResult {


    EditText prodName,prodPrice,prodQuantity;

    ImageView movImg;

    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        prodName=findViewById(R.id.prodName);
        prodPrice=findViewById(R.id.prodPrice);
        prodQuantity = findViewById(R.id.prodQuantity);

        movImg=findViewById(R.id.movImg);

        Backendless.initApp(this, "APP_ID", "YOUR_KEY");


    }

    public void save(View view) {



        if (bitmap != null) {
            Backendless.Files.Android.upload(bitmap, Bitmap.CompressFormat.PNG, 20
                    , prodName.getText().toString(), "images", new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(BackendlessFile response) {
                            Cart cart=new Cart();
                            cart.setName(prodName.getText().toString());
                            cart.setPrice(prodPrice.getText().toString());
                            cart.setQuantity(prodQuantity.getText().toString());
                            cart.setUrl(response.getFileURL());
                            Backendless.Persistence.save(cart,MainActivity.this);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(MainActivity.this, "image save error", Toast.LENGTH_SHORT).show();

                        }
                    });

        }
        else
            Toast.makeText(this, "image not exists", Toast.LENGTH_SHORT).show();


    }



    public void show(View view) {
        PickImageDialog.build(new PickSetup()).setOnPickResult(this).show(this);

    }

    @Override
    public void handleResponse(Cart response) {
        Toast.makeText(MainActivity.this, "Product saved success", Toast.LENGTH_SHORT).show();
        prodName.setText("");
        prodPrice.setText("");
        prodQuantity.setText("");

    }

    @Override
    public void handleFault(BackendlessFault fault) {

        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPickResult(PickResult r) {
        bitmap = r.getBitmap();
        movImg.setImageBitmap(bitmap);
    }
}
