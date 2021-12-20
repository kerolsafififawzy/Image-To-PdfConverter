package com.kerols.pdfconverter;

/**
 * Call functions step by step during conversion
 **/

public interface CallBacks {

    void onFinish(String path);
    void onError(Throwable throwable);
    void onProgress(int progress , int max);
    void onCancel();
    void onStart();



}
