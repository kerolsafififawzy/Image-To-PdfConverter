package com.kerols.pdfconverter;

public class PdfImageSetting {

    private int Top = 0;
    private int Bottom = 0;
    private int Left = 0;
    private int Right = 0;
    private int ImageHeight = -1;
    private int ImageWidth =  -1 ;


    public PdfImageSetting() { }


    /**
     *
     * Call Function To Add Margin For PDF Page
     * @param Top By Pixel Unit
     * @param Bottom By Pixel Unit
     * @param Right By Pixel Unit
     * @param left By Pixel Unit
     * */
    public void setMargin(int Top , int Bottom , int Right , int left) {
        // Size by pixel
        this.Top = Top;
        this.Bottom = Bottom;
        this.Right = Right;
        this.Left = left;

    }
    
    /**
     *
     * Call Function To Add Size of Image
     * @param ImageHeight Height Image By Pixel Unit
     * @param ImageWidth Width Image By Pixel Unit
     * */
    public void setImageSize(int ImageHeight , int ImageWidth) {
        // Size by pixel
        this.ImageHeight = ImageHeight;
        this.ImageWidth = ImageWidth;

    }
    /**
     *
     * Call Function To Add Size of Image By [InSize.enum]
     * @param inSize inSize is Image Size Value The Found IN Page Like InSize.FULL_PAGE_SIZE
     *
     * */
   public void setImageSize(InSize inSize) {
        // Size by pixel
       if (inSize != null) {
           switch (inSize) {
               case FULL_PAGE_SIZE:
                   ImageHeight = -1;
                   ImageWidth = -1;
                   break;

               case IMAGE_SIZE:

                   ImageHeight = -2;
                   ImageWidth = -2;

                   break;
               case DEFAULT:
               default:
                   ImageHeight = -3;
                   ImageWidth = -3;
           }
       }else  {

           ImageHeight = -3;
           ImageWidth = -3;

       }
        }


    public int getImageWidth() {
        return ImageWidth;
    }

    public int getImageHeight() {
        return ImageHeight;
    }
    
    public int getBottom() {
        return Bottom;
    }

    public int getLeft() {
        return Left;
    }

    public int getRight() {
        return Right;
    }

    public int getTop() {
        return Top;
    }


}
