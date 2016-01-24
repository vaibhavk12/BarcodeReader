package com.example.dell.barcodereader;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Bitmap img;
    Intent click;
    Uri imageuri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //checkit();
        setSupportActionBar(toolbar);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==0) {
             {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    clickpicture();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {
                    Toast.makeText(this, "mo permission:\n" , Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void ButtonClicked(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            clickpicture();
        }
        else{
           if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
               Toast.makeText(this,"you Need It",Toast.LENGTH_LONG).show();

           }
            final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions( permissions, 0);
        }
    }

    public void clickpicture(){
        click = new Intent( );
        click.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
            click.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
            imageuri = Uri.fromFile(photo);
        }catch (Exception e){

        }
        startActivityForResult(click, 0);
       // Imagebarcode();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri tmp=imageuri;
                    getContentResolver().notifyChange(tmp,null);
                    ContentResolver cr=getContentResolver();
                    // Image captured and saved to fileUri specified in the Intent

                    img=MediaStore.Images.Media.getBitmap(cr,imageuri);

                    ImageView iv = (ImageView) findViewById(R.id.imageView);
                    Toast.makeText(this, "Image saved to:\n" +
                            tmp.toString(), Toast.LENGTH_LONG).show();
                    iv.setImageBitmap(img);

                    Imagebarcode();
                }
                catch (Exception e){
                    Toast.makeText(this, "Image not saved \n"
                            , Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

    public void Imagebarcode() {
        TextView t = (TextView) findViewById(R.id.textView);

        Frame frame = new Frame.Builder().setBitmap(img).build();


        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();

        SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);

        t.setText(barcodes.valueAt(0).toString());

    }
    public void check(){


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
