package com.kerols.pdfconverter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


public class ImageToPdf {

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private FileOutputStream fos;
    private PdfDocument.Page page;
    private PdfDocument.PageInfo pageInfo;
    private Canvas canvas;
    private PdfDocument document;
    private int PageHeight ;
    private int PageWidth ;
    private int Top;
    private int Bottom;
    private int Left;
    private int Right;
    private int ImageHeight;
    private int ImageWidth;
    private int height = 1010;
    private int width = 714;
    private int reqW = width;
    private int reqH ;
    private int Pro = 0;
    private Context context = null;
    private boolean Cancel;
    private int ImagesCount = 0;
    private PdfPage pdfPage;
    private int pagenum ;
    private String ErrorMessage;

/*
*
* constructor For Create ImageToPdf Class
*
* */

    public ImageToPdf (@NonNull PdfPage pdfPage
            ,@NonNull Context context) {

        this.pdfPage = pdfPage;

        if (pdfPage != null ) {

            this.PageHeight = pdfPage.getPageHeight();
            this.PageWidth = pdfPage.getPageWidth();

            if (pdfPage.getContext() != null) {
                this.context = pdfPage.getContext();
            } else {
                this.context = context;
            }

        }else  {
            this.PageHeight = -1;
            this.PageWidth = -1;
        }
    }


    /**
       * <h3> Convert Files Paths to File PDF </h3>
       * @param  array   Image File Paths
       * @param  SrcPDF  pdf file path for Export
       * @param  callBacks  Conversion callbacks
    * */
    public void PathsToPDF( @NonNull ArrayList<String>array
            ,@NonNull File SrcPDF ,@NonNull CallBacks callBacks) {

         //To Start Again
        Pro = 0;
        Cancel = false;
        //

        // Start Converter Paths to Pdf
        callBacks.onStart();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                setErrorMessage("Paths error, can be found in Arraylist string");
                Converter(array,SrcPDF,callBacks);

            }
        });


    }




    /**
     * <h3> Call this function to Convert Intent Data to PDF File</h3>
     * @param  data   Intent.getData from onActivityResult()
     *               or ActivityResultLauncher<Intent> or MediaStore.Query
     * @param  SrcPDF  pdf file path  for Export
     * @param  callBacks  Conversion callbacks
     * onStart()
     * */
    public void DataToPDF( @NonNull Intent data
            ,@NonNull File SrcPDF ,@NonNull CallBacks callBacks)  {

        //To Start Again
        Pro = 0;
        Cancel = false;
       // Start Converter Data to Pdf

        callBacks.onStart();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                ArrayList<String> array = new ArrayList<>(Data(data, context));
                setErrorMessage("Data error can be found in Intent.data");
                Converter(array,SrcPDF,callBacks);


            }
        });


    }




    /**
     * <h3> Convert Files Array Data to File PDF </h3>
     * @param  array   Image Files in Arraylist
     * @param  SrcPDF  pdf file path for Export
     * @param  callBacks  Conversion callbacks
     * */
    public void FileToPDF(@NonNull ArrayList<File>array
            ,@NonNull File SrcPDF ,@NonNull CallBacks callBacks)  {
        //To Start Again
        Pro = 0;
        Cancel = false;
        // Start Converter Files to Pdf
        callBacks.onStart();


        ArrayList<String> arrayList = new ArrayList<>();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                setErrorMessage("Found Error, it may be Because of " +
                        "one Element in ArrayList<File> or more or Arraylist is Damaged");

                for ( File item : array)
                {
                    arrayList.add(item.getPath());
                }

                Converter(arrayList,SrcPDF,callBacks);
            }
        });
    }





    /**
     * <h3> Convert Bitmap to PDF File </h3>
     * @param  bitmap   Bitmap object
     * @param  SrcPDF  pdf file path for Export
     * @param  callBacks  Conversion callbacks
     * */

    public void BitmapToPDF(@NonNull Bitmap bitmap ,
                            @NonNull File SrcPDF
                           ,@NonNull CallBacks callBacks){

        //To Start Again
        Pro = 0;
        Cancel = false;
        ImagesCount = 0;

        // Start Converter Bitmap to Pdf
        callBacks.onStart();
        startProgress(callBacks,1);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

        document = new PdfDocument();
        try {
            if (ImagesCount <= pdfPage.size()) {
                SetSize(ImagesCount,pdfPage);
            }

                if (bitmap != null) {
                    pageInfo = new PdfDocument.PageInfo.Builder(getPageWidth(bitmap), getPageHeight(bitmap), 1).create();
                    startProgress(callBacks,1);
                    page = document.startPage(pageInfo);
                    canvas = page.getCanvas();
                    startProgress(callBacks,1);
                   // Log.e("PDF", "pdf = " + bitmap.getWidth() + "x" + bitmap.getHeight());
                    canvas.drawBitmap(
                            bitmap,
                            Left
                            , Top,
                            new Paint(Paint.FILTER_BITMAP_FLAG));

                    document.finishPage(page);
                    startProgress(callBacks,1);
                } else{
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (Cancel){
                                callBacks.onCancel();
                            }else  {
                                callBacks.onError(new RuntimeException("Bitmap Error"));
                            }

                        }
                    });

                }


        }catch (RuntimeException | OutOfMemoryError ignored) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
            if (Cancel){
                callBacks.onCancel();

            }else  {
                callBacks.onError(ignored);
            }
                    if (SrcPDF.exists()) {
                        SrcPDF.delete();
                    }
                }
            });


        }

        try {

            fos = new FileOutputStream(SrcPDF);
            document.writeTo(fos);
            document.close();
            fos.close();

            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    callBacks.onFinish(SrcPDF.getPath());
                }
            });
            if (SrcPDF.length() <= 0) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBacks.onError(new RuntimeException("Bitmap Error"));
                        if (SrcPDF.exists()) {
                            SrcPDF.delete();
                        }
                    }
                });
            }
        } catch (IOException | OutOfMemoryError e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callBacks.onError(e);
                    if (SrcPDF.exists()) {
                        SrcPDF.delete();
                    }
                }
            });
        } catch (RuntimeException runtimeException) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
            if (Cancel){
                callBacks.onCancel();
            }else  {
                callBacks.onError(runtimeException);
            }
            if (SrcPDF.exists()) {
                SrcPDF.delete();
            }
                }
            });

        }
                startProgress(callBacks,1);
            }
        });
    }



    /**
     * <h3> Convert Bitmap to PDF File </h3>
     * @param  bitmap   Bitmap object
     * @param  SrcPDF  pdf file path for Export
     * @param  callBacks  Conversion callbacks
     * */

    public void BitmapToPDF(@NonNull ArrayList<Bitmap> bitmap ,
                            @NonNull File SrcPDF
                           ,@NonNull CallBacks callBacks){

        //To Start Again
        Pro = 0;
        Cancel = false;
        pagenum = bitmap.size();
        ImagesCount = 0;

        // Start Converter Bitmap to Pdf
        callBacks.onStart();
        startProgress(callBacks,1);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                document = new PdfDocument();
                try {
                    for (int i = 0; i < bitmap.size() ; i++) {

                        if (ImagesCount <= pdfPage.size()) {
                            SetSize(ImagesCount,pdfPage);
                        }

                        if (bitmap.get(i) != null) {
                            if (ImagesCount == 0)

                            {
                                pageInfo = new PdfDocument.PageInfo.Builder(getPageWidth(bitmap.get(i)),
                                        getPageHeight(bitmap.get(i)), 1).create();
                                page = document.startPage(pageInfo);
                                canvas = page.getCanvas();
                            }

                            startProgress(callBacks,bitmap.size());

                            if (ImagesCount <= pdfPage.size()) {

                                canvas.drawBitmap(
                                        bitmap.get(i),
                                          getLeftCanvas(bitmap.get(i))
                                        , getTopCanvas(bitmap.get(i)),
                                        new Paint(Paint.FILTER_BITMAP_FLAG));

                            }

                            startProgress(callBacks,bitmap.size());
                            ImagesCount = ImagesCount + 1;
                            pagenum  = pagenum - 1;
                            startProgress(callBacks,bitmap.size());


                            if (ImagesCount == pdfPage.size() || 0 == pagenum) {
                                document.finishPage(page);
                                ImagesCount = 0;

                            }

                            startProgress(callBacks,bitmap.size());

                        } else {

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (Cancel) {
                                        callBacks.onCancel();
                                    } else {
                                        callBacks.onError(new RuntimeException("Bitmap Error"));
                                    }
                                }
                            });


                        }
                    }


                }catch (RuntimeException | OutOfMemoryError ignored) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (Cancel){
                                callBacks.onCancel();

                            }else {
                                callBacks.onError(ignored);
                            }
                            if (SrcPDF.exists()){
                                SrcPDF.delete();
                            }
                        }
                    });

                }

                try {

                    fos = new FileOutputStream(SrcPDF);
                    document.writeTo(fos);
                    document.close();
                    fos.close();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            callBacks.onFinish(SrcPDF.getPath());
                        }
                    });


                   // Date date = new Date();
                    if (SrcPDF.length() <= 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBacks.onError(new RuntimeException("Bitmap Error"));
                                if (SrcPDF.exists()) {
                                    SrcPDF.delete();
                                }
                            }
                        });
                    }
                } catch (IOException | OutOfMemoryError e) {


                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (Cancel){
                                callBacks.onCancel();
                            }else  {
                                callBacks.onError(e);
                            }
                            if (SrcPDF.exists()) {
                                SrcPDF.delete();
                            }
                        }
                    });
                } catch (RuntimeException  runtimeException) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                    if (Cancel){
                        callBacks.onCancel();
                    }else  {
                        callBacks.onError(runtimeException);
                    }
                            if (SrcPDF.exists()) {
                                SrcPDF.delete();
                            }
                        }
                    });

                }
                startProgress(callBacks,1);
            }
        });
    }




    /**
     * <h3> Convert Image File to PDF File </h3>
     * @param  file   Image File
     * @param  SrcPDF  pdf file path  for Export
     * @param  callBacks  Conversion callbacks
     * */


    public void FileToPDF(@NonNull File file ,@NonNull File SrcPDF , CallBacks callBacks)  {

        //To Start Again
        Pro = 0;
        Cancel = false;
        ImagesCount = 0;
        // Start Converter File to Pdf
        callBacks.onStart();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {


        document = new PdfDocument();
        Bitmap bitmap ;
        try {
            if (ImagesCount <= pdfPage.size()) {
                SetSize(ImagesCount,pdfPage);
            }

                  bitmap = BitmapFactory.decodeFile(file.getPath());
                  startProgress(callBacks,1);
                 if (bitmap != null) {
                     ExifInterface exif = new ExifInterface(file.getPath());

                     int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                     Bitmap new_Bitmap = rotateBitmap(BitmapRotate(bitmap,orientation), orientation);
                     startProgress(callBacks,1);
                    pageInfo = new PdfDocument.PageInfo.Builder(getPageWidth(new_Bitmap), getPageHeight(new_Bitmap), 1).create();
                    page = document.startPage(pageInfo);
                    canvas = page.getCanvas();
                     startProgress(callBacks,1);

                     canvas.drawBitmap(
                             new_Bitmap,
                            getLeftCanvas(new_Bitmap)
                            ,getTopCanvas(new_Bitmap),
                            new Paint(Paint.FILTER_BITMAP_FLAG));

                    document.finishPage(page);
                     startProgress(callBacks,1);
                }else{
                     mHandler.post(new Runnable() {
                         @Override
                         public void run() {
                             if (Cancel){
                                 callBacks.onCancel();
                             }else  {
                                 callBacks.onError(new RuntimeException("Error in File"));
                             }

                         }
                     });


                }


        }catch (RuntimeException | OutOfMemoryError | IOException runtimeException) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
            if (Cancel){
                callBacks.onCancel();
            }else  {
                callBacks.onError(runtimeException);
            }
                    if (SrcPDF.exists()) {
                        SrcPDF.delete();
                    }
                }
            });

        }

        try {

            fos = new FileOutputStream(SrcPDF);
            document.writeTo(fos);
            document.close();
            fos.close();

            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    callBacks.onFinish(SrcPDF.getPath());
                }
            });


        } catch (IOException | OutOfMemoryError e) {


            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (Cancel){ callBacks.onCancel(); }else  { callBacks.onError(e); }
                    if (SrcPDF.exists()) {
                        SrcPDF.delete();
                    }
                }
            });
        } catch (RuntimeException runtimeException) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
            if (Cancel){
                callBacks.onCancel();
            }else  {
                callBacks.onError(runtimeException);
            }
                    if (SrcPDF.exists()) {
                        SrcPDF.delete();
                    }
                }
            });

        }
                startProgress(callBacks,1);
            }
        });

    }




    /**
     *
     * @return Page Height
     *
     * */


    private int getPageHeight(Bitmap bitmap) {

        if (PageWidth > 0 && PageHeight > 0) {

            return PageHeight;

        } else if (PageWidth == -1 || PageHeight == -1) {

            return bitmap.getHeight()  ;

        } else {

            reqH = width * bitmap.getHeight() / bitmap.getWidth();
            if (reqH < height) {
                return reqH;
            } else {
                reqH = height;
                return reqH;
            }
        }

    }
    /**
     *
     * @return Page Width
     *
     * */

    private int getPageWidth(Bitmap bitmap) {
        if (PageWidth > 0 && PageHeight > 0) {

            return PageWidth  ;

        } else if (PageWidth == -1 || PageHeight == -1) {

            return bitmap.getWidth() ;

        } else {

            reqH = width * bitmap.getHeight() / bitmap.getWidth();
            if (reqH < height) {
                return reqW;
            } else {
                reqW = height * bitmap.getWidth() / bitmap.getHeight();
                return reqW;
            }
        }
    }

    /**
     *
     * @return Image Width
     *
     * */

    private int getImageWidth(Bitmap bitmap) {

      //  int margin = Right + Left;

        if (ImageWidth > 0 && ImageHeight > 0) {

            return ImageWidth  ;

        } else if (ImageHeight == -1 || ImageWidth == -1) {

            return getPageWidth(bitmap)  ;

        }else if (ImageHeight == -2 || ImageWidth == -2) {

            return bitmap.getWidth()  - 5;

        } else {
            reqH = width * bitmap.getHeight() / bitmap.getWidth();
            if (reqH < height) {
                return reqW ;
            } else {
                reqW = height * bitmap.getWidth() / bitmap.getHeight();
                return reqW  ;
            }
        }
    }

    /**
     *
     * @return Image Height
     *
     * */
    private int getImageHeight(Bitmap bitmap) {

     //   int margin = Top + Bottom;

        if (ImageWidth > 0 && ImageHeight > 0) {
            return ImageHeight   ;

        } else if (ImageHeight == -1 || ImageWidth == -1) {

            return getPageHeight(bitmap) ;

        }else if (ImageHeight == -2 || ImageWidth == -2) {

            return bitmap.getHeight() -  5;

        } else {
            reqH = width * bitmap.getHeight() / bitmap.getWidth();
            if (reqH < height) {
               return reqH  ;
            } else {
                reqH = height;
                return reqH ;
            }
        }

    }


    /**
     *
     * @return Arraylist<String> Real Path from intent Data
     * Call this function When Convert intent.data of onActivityResult
     * or
     * ActivityResultLauncher<Intent> or MediaStore.Query
     * to Arraylist String Real Path
     *
     * */

    public ArrayList<String> Data(@NonNull  Intent intent
            ,@NonNull Context context) {

        ArrayList<String> File_orginal = new ArrayList<>();
        if (intent.getClipData() != null) {
           int Count = intent.getClipData().getItemCount();
            for (int i = 0; i < Count; i++) {
                Uri uri = intent.getClipData().getItemAt(i).getUri();
                try {
                    if (RealPathUtil.getRealPath(context, uri) == null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {


                            }
                        });


                    } else {

                        File_orginal.add(RealPathUtil.getRealPath(context, uri));
                    }
                } catch (RuntimeException | OutOfMemoryError e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }


            }
        } else if (intent.getData() != null) {
            Uri uri = intent.getData();

            try {
                if (RealPathUtil.getRealPath(context, uri) == null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });

                } else {


                    File_orginal.add(RealPathUtil.getRealPath(context, uri));


                }
            } catch (RuntimeException e) {
                e.printStackTrace();

            }
        }

           return File_orginal;

    }

    /**
     *
     * Call this function to cancel PDF Converter
     *
     * */
    public void Cancel() {
         this.Cancel = true;
         pageInfo = null;
         page = null;
         document = null;
         fos =  null;
    }

    /**
     *
     *  Call this function to start Progress in Callback
     *
     * */

    private void startProgress(CallBacks callBacks, int ArraySize) {
        Pro = Pro + 1;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int max = ArraySize * 4;
                callBacks.onProgress(Pro, max + 1);
            }
        });

    }

/*    private void addText(String Text,Canvas canvas,int Color) {

        Paint title = new Paint();
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(context, Color));
        title.setTextSize(12);
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("" + PageNum, 150, 240, title);


    }*/

    private void SetSize (int position , PdfPage pdfPage) {


        if (pdfPage != null) {

          try {
              this.Left = pdfPage.get(position).getLeft();
              this.Top = pdfPage.get(position).getTop();
              this.Right = pdfPage.get(position).getRight();
              this.Bottom = pdfPage.get(position).getBottom();
              this.ImageHeight = pdfPage.get(position).getImageHeight();
              this.ImageWidth = pdfPage.get(position).getImageWidth();
          }catch (IndexOutOfBoundsException indexOutOfBoundsException) {

              Log.e("TAG", "Error on Count Image ON PAGE: ");

          }

        }else  {
            this.Left = 0;
            this.Top = 0;
            this.Right = 0;
            this.Bottom = 0;
            this.ImageHeight = -3;
            this.ImageWidth = -3;
        }
    }


    private int getLeftCanvas (Bitmap bitmap) {


        if (Left > 0 && getImageWidth(bitmap) + Right + Left <= getPageWidth(bitmap)){

            return Left;

        }else {

            return Left -  Right ;

        }
    }

    private int getTopCanvas (Bitmap bitmap) {

        if ( Top > 0 && getImageHeight(bitmap) + Top + Bottom <= getPageHeight(bitmap)){

            return Top;

        }else {

            return Top - Bottom ;

        }
    }


    private void Converter (ArrayList<String> array , File SrcPDF ,CallBacks callBacks){
                document = new PdfDocument();
                Bitmap bitmap ;
                pagenum = array.size();
                ImagesCount = 0;
                try {

                    for (int i = 0; i < array.size(); i++) {

                        if (ImagesCount <= pdfPage.size()) {
                            SetSize(ImagesCount,pdfPage);
                        }

                        bitmap = BitmapFactory.decodeFile(array.get(i));
                        startProgress(callBacks,array.size());

                        if (bitmap != null) {

                            ExifInterface exif = new ExifInterface(array.get(i));

                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                            Bitmap newBitmap_orgnil = rotateBitmap(BitmapRotate(bitmap,orientation), orientation);

                            startProgress(callBacks,array.size());

                            if (ImagesCount == 0)

                            {
                                pageInfo = new PdfDocument.PageInfo.Builder(getPageWidth(newBitmap_orgnil), getPageHeight(newBitmap_orgnil), 1).create();
                                page = document.startPage(pageInfo);
                                canvas = page.getCanvas();
                            }
                            

                            if (ImagesCount <= pdfPage.size()) {

                                canvas.drawBitmap(
                                        newBitmap_orgnil,
                                        getLeftCanvas(newBitmap_orgnil)
                                        , getTopCanvas(newBitmap_orgnil),
                                        new Paint(Paint.FILTER_BITMAP_FLAG));

                            }

                            startProgress(callBacks,array.size());
                            ImagesCount = ImagesCount + 1;
                            pagenum  = pagenum - 1;


                          //  Log.e("TAG", "run: " + pagenum  + "  " + pdfPage.size());

                            if (ImagesCount == pdfPage.size() || 0 == pagenum) {
                                document.finishPage(page);
                                ImagesCount = 0;
                            }

                            startProgress(callBacks,array.size());

                        } else {

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                            if (Cancel){
                                callBacks.onCancel();
                            }else  {
                                callBacks.onError(new RuntimeException(getErrorMessage()));

                            }

                                }
                            });
                        }
                    }

                }catch (RuntimeException | OutOfMemoryError runtimeException) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (Cancel){
                                callBacks.onCancel();
                            }else  {
                                callBacks.onError(runtimeException);
                            }

                            if (SrcPDF.exists()) {
                                SrcPDF.delete();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

        try {

                    fos = new FileOutputStream(SrcPDF);

                    document.writeTo(fos);
                    document.close();
                    fos.close();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            callBacks.onFinish(SrcPDF.getPath());
                        }
                    });


                } catch (IOException | OutOfMemoryError e) {


                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (Cancel){ callBacks.onCancel(); }else  { callBacks.onError(e); }
                            if (SrcPDF.exists()) {
                                SrcPDF.delete();
                            }
                        }
                    });
                } catch (RuntimeException runtimeException) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (Cancel){
                                callBacks.onCancel();
                            }else  {
                                callBacks.onError(runtimeException);
                            }

                            if (SrcPDF.exists()) {
                                SrcPDF.delete();
                            }
                        }
                    });

                }

                startProgress(callBacks,array.size());
    }

    private void  setErrorMessage (String text) {

        this.ErrorMessage= text;

    }


    private String getErrorMessage () {
        return  ErrorMessage;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap BitmapRotate(Bitmap bitmap,int orientation) {
            switch (orientation) {
                case ExifInterface.ORIENTATION_TRANSPOSE:
                case ExifInterface.ORIENTATION_ROTATE_90:
                case ExifInterface.ORIENTATION_TRANSVERSE:
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return Bitmap.createScaledBitmap(bitmap, getImageHeight(bitmap), getImageWidth(bitmap), true);
                default:
                    return Bitmap.createScaledBitmap(bitmap, getImageWidth(bitmap), getImageHeight(bitmap), true);

            }

        }

}


