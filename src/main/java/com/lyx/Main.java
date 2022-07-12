package com.lyx;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {
    // 经过测试,dpi为96,100,105,120,150,200中,105显示效果较为清晰,体积稳定,dpi越高图片体积越大,一般电脑显示分辨率为96
    public static final float DEFAULT_DPI = 105;

    // 图片格式
    public static final String DEFAULT_FORMAT = "jpg";

    public static void main(String[] args) {

        String pdfFilePath = "/Users/lgf/my-dir/download/00_暂时不要删除/工程数学线性代数第六版.pdf";

        pdfToImage(pdfFilePath, "/Users/lgf/my-dir/download/00." + DEFAULT_FORMAT, 0);
        pdfToImage(pdfFilePath, "/Users/lgf/my-dir/download/01." + DEFAULT_FORMAT, 1);
        pdfToImage(pdfFilePath, "/Users/lgf/my-dir/download/02." + DEFAULT_FORMAT, 2);
    }


    /**
     * pdf转图片，单页.
     * @param pdfPath pdf文件路径.
     * @param imgPath 生成的图片路径
     * @param page 要转换的页面码.
     */
    public static void pdfToImage(String pdfPath, String imgPath,int page) {
        try {
            //图像合并使用参数
            // 总宽度
            int width = 0;
            // 保存一张图片中的RGB数据
            int[] singleImgRGB;
            int shiftHeight = 0;
            //保存每张图片的像素值
            BufferedImage imageResult = null;
            //利用PdfBox生成图像
            PDDocument pdDocument = PDDocument.load(new File(pdfPath));
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            //循环每个页码
            for (int i = 0, len = pdDocument.getNumberOfPages(); i < len; i++) {
                if (i==page) {
                    BufferedImage image = renderer.renderImageWithDPI(i, DEFAULT_DPI, ImageType.RGB);
                    int imageHeight = image.getHeight();
                    int imageWidth = image.getWidth();
                    //计算高度和偏移量
                    //使用第一张图片宽度;
                    width = imageWidth;
                    //保存每页图片的像素值
                    imageResult = new BufferedImage(width, imageHeight, BufferedImage.TYPE_INT_RGB);
                    //这里有高度，可以将imageHeight*len，我这里值提取一页所以不需要
                    singleImgRGB = image.getRGB(0, 0, width, imageHeight, null, 0, width);
                    // 写入流中
                    imageResult.setRGB(0, shiftHeight, width, imageHeight, singleImgRGB, 0, width);
                } else if(i > page) {
                    continue;
                }
            }
            pdDocument.close();
            ImageIO.write(imageResult, DEFAULT_FORMAT, new File(imgPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
