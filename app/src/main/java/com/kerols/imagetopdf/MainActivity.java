package com.kerols.imagetopdf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.kerols.pdfconverter.CallBacks;
import com.kerols.pdfconverter.ImageToPdf;
import com.kerols.pdfconverter.PdfImageSetting;
import com.kerols.pdfconverter.PdfPage;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private MaterialButton Cancel;
    private MaterialButton GetImage;
    private ImageToPdf imageToPdf;


    private final String TAG = MainActivity.class.toString();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Cancel = findViewById(R.id.Cancel);
        GetImage = findViewById(R.id.GetImage);
        Cancel.setEnabled(false);

        // Setting for the page
        PdfPage pdfPage = new PdfPage(getApplicationContext());
        pdfPage.setPageSize(1000,1000);

        // Setting for a single image on a page

        PdfImageSetting mPdfImageSetting = new PdfImageSetting();
        mPdfImageSetting.setImageSize(200,200);
        mPdfImageSetting.setMargin(20,20,20,20);

        // Setting for the second image on the page

        PdfImageSetting mPdfImageSetting2 = new PdfImageSetting();
        mPdfImageSetting2.setImageSize(100,100);
        mPdfImageSetting2.setMargin(220,220,220,220);


        // Add photos that are set in one page
        pdfPage.add(mPdfImageSetting);
        pdfPage.add(mPdfImageSetting2);


        imageToPdf = new ImageToPdf(pdfPage,getApplicationContext());

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}
                ,1);


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageToPdf != null) {
                    imageToPdf.Cancel();
                }
            }
        });



        GetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(GetImage != null) {
            GetImage.setEnabled(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            assert data != null;

            // Use one of the method for convert To File PDf

            imageToPdf.DataToPDF(data,
                    new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "ImageToPdf.pdf"), new CallBacks() {
                        @Override
                        public void onFinish(String path) {
                            Cancel.setEnabled(false);
                            GetImage.setEnabled(true);
                            Toast.makeText(getApplicationContext(),"onFinish",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Cancel.setEnabled(false);
                            GetImage.setEnabled(true);

                            Toast.makeText(getApplicationContext(),"onError",Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onError: ", throwable );
                        }

                        @Override
                        public void onProgress(int progress , int max) {
                            Log.e(TAG, "onProgress: " +  progress  + "  " +  max );

                        }

                        @Override
                        public void onCancel() {
                            Cancel.setEnabled(false);
                            GetImage.setEnabled(true);
                            Toast.makeText(getApplicationContext(),"onCancel",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onStart() {
                            Cancel.setEnabled(true);
                            GetImage.setEnabled(false);
                            Toast.makeText(getApplicationContext(),"onStart",Toast.LENGTH_SHORT).show();

                        }
                    });


        }
    }
}