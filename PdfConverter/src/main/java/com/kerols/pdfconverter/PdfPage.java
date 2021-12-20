package com.kerols.pdfconverter;

import android.content.Context;

import java.util.ArrayList;

public class PdfPage extends ArrayList<PdfImageSetting> {

    private int PageHeight = -1;
    private int PageWidth = -1;
    private final Context context;

    public PdfPage (Context context) {

        this.context = context;
    }


    /**
     *
     * Call Function To Add Size of Page By [Value.enum]
     * @param PageSize PageSize is  Page Size Value Like Value.PAGE_A4
     *
     * */

    public void  setPageSize(Value PageSize) {
        // Size by pixel
        if (PageSize != null) {
            switch (PageSize) {

                case PAGE_A4:
                    PageHeight = 842;
                    PageWidth = 595;

                    break;
                case PAGE_A3:
                    PageHeight = 1191;
                    PageWidth = 842;

                    break;
                case PAGE_A2:
                    PageHeight = 1684;
                    PageWidth = 1191;

                    break;
                case PAGE_A1:
                    PageHeight = 2384;
                    PageWidth = 1684;

                    break;

                case IMAGE_SIZE:

                    PageHeight = -1;
                    PageWidth = -1;

                    break;
                case DEFAULT:
                default:
                    PageHeight = -2;
                    PageWidth = -2;


            }
        }  else  {

            PageHeight = -1;
            PageWidth = -1;

        }


    }
    /**
     *
     * Call Function To Add Size of Image
     * @param PageHeight int Page Height By Pixel Unit
     * @param PageWidth int Page Width By  Pixel Unit
     *
     * */
    public void  setPageSize(int PageHeight , int PageWidth) {
        // Size by pixel
        this.PageHeight = PageHeight;
        this.PageWidth = PageWidth;
    }


    public int getPageHeight() {
        return PageHeight;
    }

    public int getPageWidth() {
        return PageWidth;
    }

    public Context getContext() {
        return context;
    }
}
