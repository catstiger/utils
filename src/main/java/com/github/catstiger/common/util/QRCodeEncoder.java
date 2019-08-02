package com.github.catstiger.common.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.common.base.Charsets;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeEncoder {
  /**
   * 生成的二维码的宽度
   */
  public static final int QR_IMAGE_WIDTH = 300;
  /**
   * 生成的二维码的高度
   */
  public static final int QR_IMAGE_HEIGHT = 300;
  /**
   * 生成的二维码的格式
   */
  public static final String QR_IMAGE_FORMAT = "jpg";

  /**
   * 将指定的内容编码为二维码文件，包括LOGO
   * 
   * @param contents 编码的内容
   * @param logo LOGO，可以为{@code null}，编码完成后会被关闭
   * @param qrCode 输出的Stream，完成后会关闭。
   */
  public static void encode(String contents, InputStream logo, OutputStream qrCode) throws IOException, WriterException {
    Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
    // 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）
    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    // 内容所使用字符集编码
    hints.put(EncodeHintType.CHARACTER_SET, Charsets.UTF_8);
    hints.put(EncodeHintType.MARGIN, 1);// 设置二维码边的空度，非负数

    BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, // 要编码的内容
        BarcodeFormat.QR_CODE, // 编码类型
        QR_IMAGE_WIDTH, // 条形码的宽度
        QR_IMAGE_HEIGHT, // 条形码的高度
        hints);// 生成条形码时的一些配置,此项可选
    // 生成二维码
    try {
      MatrixToImageWriter.writeToStream(bitMatrix, logo, qrCode, QR_IMAGE_FORMAT);
    } finally {
      qrCode.flush();
      IOHelper.closeQuietly(qrCode);
      IOHelper.closeQuietly(logo);
    }
  }

  public static void encode(String contents, OutputStream qrCode) throws IOException, WriterException {
    encode(contents, null, qrCode);
  }

  /**
   * 二维码的生成需要借助MatrixToImageWriter类
   */
  private static final class MatrixToImageWriter {
    private static final int BLACK = 0xFF000000;// 用于设置图案的颜色
    private static final int WHITE = 0xFFFFFFFF; // 用于背景色

    private MatrixToImageWriter() {
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
      int width = matrix.getWidth();
      int height = matrix.getHeight();
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
        }
      }
      return image;
    }

    public static void writeToStream(BitMatrix matrix, InputStream inputLogo, OutputStream stream, String format) throws IOException {
      BufferedImage image = toBufferedImage(matrix);
      // 设置logo图标
      if (inputLogo != null) {
        LogoConfig logoConfig = new LogoConfig();
        image = logoConfig.logoMatrix(image, inputLogo);
      }
      if (!ImageIO.write(image, format, stream)) {
        throw new IOException("Could not write an image of format " + format);
      }
    }
  }

  private static class LogoConfig {
    /**
     * 设置 logo
     * 
     * @param matrixImage 源二维码图片
     * @return 返回带有logo的二维码图片
     * @author Administrator sangwenhao
     */
    public BufferedImage logoMatrix(BufferedImage matrixImage, InputStream logoImage) throws IOException {
      Graphics2D g2 = matrixImage.createGraphics();
      int matrixWidth = matrixImage.getWidth();
      int matrixHeigh = matrixImage.getHeight();

      BufferedImage logo = ImageIO.read(logoImage);

      // 开始绘制图片
      g2.drawImage(logo, matrixWidth / 5 * 2, matrixHeigh / 5 * 2, matrixWidth / 5, matrixHeigh / 5, null);// 绘制
      BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
      g2.setStroke(stroke);// 设置笔画对象
      // 指定弧度的圆角矩形
      RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth / 5 * 2, matrixHeigh / 5 * 2, matrixWidth / 5, matrixHeigh / 5, 20, 20);
      g2.setColor(Color.white);
      g2.draw(round);// 绘制圆弧矩形

      // 设置logo 有一道灰色边框
      BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
      g2.setStroke(stroke2);// 设置笔画对象
      RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth / 5 * 2 + 2, matrixHeigh / 5 * 2 + 2, matrixWidth / 5 - 4, matrixHeigh / 5 - 4, 20,
          20);
      g2.setColor(new Color(128, 128, 128));
      g2.draw(round2);// 绘制圆弧矩形

      g2.dispose();
      matrixImage.flush();
      return matrixImage;
    }
  }

}
