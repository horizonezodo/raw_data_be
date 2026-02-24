package com.naas.admin_service.core.utils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.security.SecureRandom;

public class CaptchaImageUtil {
    private CaptchaImageUtil() {}

    private static final SecureRandom RAND = new SecureRandom();

    private static Font comicSansMSFont(int fontSize) {
        try (InputStream is = CaptchaImageUtil.class.getResourceAsStream("/fonts/Comic-Sans-MS.ttf")) {
            if (is != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.BOLD, fontSize);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
                return font;
            }
        } catch (Exception e) {
            // ignore, fallback below
        }
        return new Font("Serif", Font.BOLD, fontSize); // fallback
    }

    public static BufferedImage createCaptchaImage(String text, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        // Bật anti-aliasing cho chữ mịn
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fill background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Add noise dots
        for (int i = 0; i < 40; i++) {
            int x = RAND.nextInt(width);
            int y = RAND.nextInt(height);
            g2d.setColor(new Color(RAND.nextInt(150), RAND.nextInt(150), RAND.nextInt(150), 80));
            g2d.fillRect(x, y, 2, 2);
        }

        // Add noise lines
        for (int i = 0; i < 4; i++) {
            g2d.setColor(new Color(RAND.nextInt(100), RAND.nextInt(100), RAND.nextInt(100), 60));
            int x1 = RAND.nextInt(width);
            int y1 = RAND.nextInt(height);
            int x2 = RAND.nextInt(width);
            int y2 = RAND.nextInt(height);
            g2d.setStroke(new BasicStroke(1.2f));
            g2d.drawLine(x1, y1, x2, y2);
        }

        // Draw captcha text with wave effect
        int fontSize = 20;
        Font font = comicSansMSFont(fontSize);
        int paddingLeft = 0;
        int paddingRight = 10;
        int availableWidth = width - paddingLeft - paddingRight;
        int charSpacing = !text.isEmpty() ? availableWidth / (text.length() + 1) : 0;
        int startX = paddingLeft + charSpacing;
        int startY = height / 2 + fontSize / 3;
        Color[] colors = new Color[] {
                new Color(44, 62, 80), new Color(231, 76, 60), new Color(52, 152, 219),
                new Color(243, 156, 18), new Color(155, 89, 182), new Color(26, 188, 156)
        };
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int charX = startX + i * charSpacing;
            double phase = RAND.nextDouble() * 2 * Math.PI;
            double wave = Math.sin(i * 0.7 + phase);
            int charY = startY + (int) (wave * 7); // giảm biên độ sóng

            g2d.setFont(font);
            g2d.setColor(colors[RAND.nextInt(colors.length)]);

            double angle = (RAND.nextDouble() - 0.5) * 0.5;
            AffineTransform old = g2d.getTransform();
            g2d.rotate(angle, charX, charY);

            g2d.drawString(String.valueOf(c), charX, charY);

            g2d.setTransform(old);
        }

        g2d.dispose();
        return image;
    }
}

