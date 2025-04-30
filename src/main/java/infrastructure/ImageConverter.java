/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author zymci
 */
@Converter
public class ImageConverter implements AttributeConverter<Image, byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(Image image) {
        if (image == null) {
            return null;
        }
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage bufferedImage = (BufferedImage) image;
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert Image to byte array", e);
        }
    }

    @Override
    public Image convertToEntityAttribute(byte[] imageData) {
        if (imageData == null || imageData.length == 0) {
            return null;
        }
        
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageData)) {
            return ImageIO.read(bis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert byte array to Image", e);
        }
    }
}
