# ImageToPdf
the Android library for convert single Image or more to File Pdf

## Features

- customize size the image and page By Unit Pixel 
- Set Margin 
- Add one or more  Photos on one Page
- Convert Images from  Bitmap , Intent.data ,File , File Source to File Pdf
- min SDK 19
- [Demo](https://choosealicense.com/licenses/mit/) project by Kotlin


## Sample App

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
     alt="Get it on Google Play"
     height="70">](https://play.google.com/store/apps/details?id=com.stealthcotper.networktools)

## How To Use

- Add in build.gradle:
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


```
- Add gradle dependency:
```gradle
dependencies {
		    implementation 'com.github.kerolsafififawzy:Image-To-PdfConverter:1.0.2'
	}
```
- Add permission
```gradle
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```


- Add Pdf Page
```java
        PdfPage pdfPage = new PdfPage(getApplicationContext());
        pdfPage.setPageSize(1100,1100);

	    //          you Can Use the existing Pages Size
	   //           pdfPage.setPageSize(Value.IMAGE_SIZE);
          //    or      pdfPage.setPageSize(Value.PAGE_A1);
	 //    or      pdfPage.setPageSize(Value.PAGE_A2);

```

- Add Single Image per page in Pdf File
```java
 PdfImageSetting mPdfImageSetting = new PdfImageSetting();
          // Custom Image Size
	  
          mPdfImageSetting.setImageSize(200,200);
	  
	   // you Can Use the original  Image Size
	  // mPdfImageSetting.setImageSize(InSize.IMAGE_SIZE);
	 
	 // Set Margin
         mPdfImageSetting.setMargin(20,20,20,20);
```
- Add More Than One Image Per Page In Pdf File
```java

              // Setting for a single image on a page
	      
           PdfImageSetting mPdfImageSetting = new PdfImageSetting();
   
           mPdfImageSetting.setImageSize(200,200);

           mPdfImageSetting.setMargin(20,20,20,20);
	   
	     // Setting for the second image on the page
	     
	   PdfImageSetting mPdfImageSetting2 = new PdfImageSetting();
   
                  mPdfImageSetting2.setImageSize(100,100);
		  
                  mPdfImageSetting2.setMargin(220,220,220,220);
```

- Add photos that are set in one page

```java
        pdfPage.add(mPdfImageSetting);
        pdfPage.add(mPdfImageSetting2);
```
- Create ImageToPdf Class
```java
      // class call
      ImageToPdf  imageToPdf = new ImageToPdf(pdfPage,getApplicationContext());
      
```

## Example to Converter

- startActivityForResult or ActivityResultLauncher<Intent>

```java
  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(intent, 1);



```
- Convert Intent.getData() to File Pdf
```java
       @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            assert data != null;

            // Use one of the method for convert To File PDf

            imageToPdf.DataToPDF(data,
                    new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                            "iMAGEtoPDF.pdf"), new CallBacks() {
                        @Override
                        public void onFinish(String path) {
                            Toast.makeText(getApplicationContext(),"onFinish",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Toast.makeText(getApplicationContext(),"onError",Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onError: ", throwable );
                        }

                        @Override
                        public void onProgress(int progress , int max) {
                            Log.e(TAG, "onProgress: " +  progress  + "  " +  max );

                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(getApplicationContext(),"onCancel",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onStart() {
                            Toast.makeText(getApplicationContext(),"onStart",Toast.LENGTH_SHORT).show();

                        }
                    });


        }
    }
      
```

### To cancel the converter
``` java

imageToPdf.Cancel()

```



