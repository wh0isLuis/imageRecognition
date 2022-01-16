/*
    Aufgabe 3) Zweidimensionale Arrays und CodeDraw - "Schwärzen ähnlicher Bildbereiche"
*/

import codedraw.CodeDraw;
import codedraw.Palette;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;

public class Aufgabe3 {

    // converts BufferedImage object to a grayscale array
    private static int[][] convertImg2Array(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[][] imgArray = new int[height][width];
        Color tempColor;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                tempColor = new Color(img.getRGB(col, row));
                imgArray[row][col] = (int) (tempColor.getRed() * 0.3 + tempColor.getGreen() * 0.59 + tempColor.getBlue() * 0.11);
            }

        }
        return imgArray;
    }

    //draws the image array to the canvas
    private static void drawImage(int[][] imgArray) {
        CodeDraw cd = new CodeDraw(Math.max(imgArray[0].length, 150), Math.max(imgArray.length, 150));

        for (int y = 0; y < imgArray.length; y++) {
            for (int x = 0; x < imgArray[y].length; x++) {
                cd.setColor(Palette.fromGrayscale(imgArray[y][x]));
                cd.drawPixel(x, y);
            }
        }
        cd.show();
    }

    private static int[][] blackenSimilarRegions(int[][] imgArray, int rowStart, int rowEnd, int colStart, int colEnd, double threshold) {
        int[][] resArr= new int[imgArray.length][imgArray[0].length];
        //System.arraycopy(imgArray, 0 , resArr,0,imgArray.length);
        for (int row = 0; row < imgArray.length; row++) {
            for (int col = 0; col < imgArray[row].length; col++) {
                    resArr[row][col]=imgArray[row][col];
            }
        }
        int[][] refArr=refArr(imgArray,rowStart,rowEnd,colStart,colEnd);        //liefert refArr in eigener Methode

            for (int y = 0; y < resArr.length; y++) {
                for (int x = 0; x < resArr[y].length; x++) {
                    double sum = 0;
                    for (int refArrY = 0; refArrY < refArr.length; refArrY++) {
                        for (int refArrX = 0; refArrX < refArr[refArrY].length; refArrX++) {
                            int refXcoordinate = refArrX - (refArr[refArrY].length / 2);            //offset
                            int refYcoordinate = refArrY - (refArr.length / 2);                     //offset
                                if (refXcoordinate + x >= 0 && refXcoordinate + x < resArr[y].length && refYcoordinate + y >= 0 && refYcoordinate + y < resArr.length) {
                                    sum += Math.pow(resArr[refYcoordinate + y][refXcoordinate + x] - refArr[refArrY][refArrX], 2);
                                }

                        }
                    }
                    if (sum < threshold && y>20) {
                        for (int refArrY = 0; refArrY < refArr.length; refArrY++) {
                            for (int refArrX = 0; refArrX < refArr[refArrY].length; refArrX++) {
                                int refXcoordinate = refArrX - (refArr[refArrY].length / 2);
                                int refYcoordinate = refArrY - (refArr.length / 2);
                                resArr[refYcoordinate + y][refXcoordinate + x] = 0;
                            }
                        }
                    }
                }
            }
            return resArr;
    }

    private static int[][] refArr(int[][] imgArray, int rowStart, int rowEnd, int colStart, int colEnd) {
        int[][] refArr=new int[rowEnd-rowStart+1][colEnd-colStart+1];           //+1 da end inklusive ist
        for (int row=0; row<rowEnd-rowStart+1; row++) {
            for (int col = 0; col < colEnd-colStart+1; col++) {
                refArr[row][col]=imgArray[rowStart+row][colStart+col];
            }
        }
        return refArr;
    }

    public static void main(String[] args) {

        String fileName = "./src/page4.png";
        BufferedImage img = null;
        // try to open image file
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        int[][] imgArray = convertImg2Array(img);

        //blacken the "g"
       int[][] resultImg = blackenSimilarRegions(imgArray, 148, 158, 321, 328, 1e5);

        //blacken the "while"
        //int[][] resultImg = blackenSimilarRegions(imgArray, 214, 230, 233, 270, 1e6);

        //binarize by comparing to a single black pixel region
       // int[][] resultImg = blackenSimilarRegions(imgArray, 150, 150, 95, 95, 220 * 220);

        drawImage(imgArray);
        if (resultImg != null) {
            drawImage(resultImg);
        }
    }
}
