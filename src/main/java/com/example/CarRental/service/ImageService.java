package com.example.CarRental.service;


import com.example.CarRental.model.Image;
import com.example.CarRental.repository.CarRepository;
import com.example.CarRental.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@AllArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final CarRepository carRepository;

    public void uploadImage(List<MultipartFile> uploadedImages, Long carId) throws IOException {
        for (MultipartFile uploadedImage : uploadedImages) {
            if (!uploadedImage.isEmpty()) {
                Image image = new Image();
                image.setBytes(compressBytes(uploadedImage.getBytes()));
                image.setName(uploadedImage.getOriginalFilename());
                image.setContentType(uploadedImage.getContentType());
                image.setCar(carRepository.findById(carId).get());
                imageRepository.save(image);
            }
            // Insert a default image
            else {
                String path = Objects.requireNonNull(getClass().getResource("/static/images/car_no_image.jpg")).getFile();
                MultipartFile img1 =  new MockMultipartFile("no_image.jpg", new FileInputStream(path));

                Image image = new Image();
                image.setBytes(compressBytes(img1.getBytes()));
                image.setName(img1.getOriginalFilename());
                image.setContentType(img1.getContentType());
                image.setCar(carRepository.findById(carId).get());
                imageRepository.save(image);
            }
        }
    }

    public List<String> getImageById(Long carId){
        List<Image> images = imageRepository.findByCarId(carId);
        List<String> encodedImg = new ArrayList<>();
        if(!images.isEmpty()) {
            for (Image img : images) {
                byte[] encoded = Base64.getEncoder().encode(decompressBytes(img.getBytes()));
                encodedImg.add(new String(encoded, StandardCharsets.UTF_8));
            }
        }
        return encodedImg;
    }

    public void deleteImage(Long carId){
        imageRepository.deleteAllByCar_Id(carId);
    }


//    compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished())
            outputStream.write(buffer, 0, deflater.deflate(buffer));
        try {
            outputStream.close();
        } catch (IOException ignored) { }

        return outputStream.toByteArray();
    }

//    decompress
    public byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished())
                outputStream.write(buffer, 0, inflater.inflate(buffer));
            outputStream.close();
        } catch (IOException | DataFormatException ignored) { }
        return outputStream.toByteArray();
    }
}
