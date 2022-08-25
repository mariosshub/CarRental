package com.example.CarRental.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.io.InputStream;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image implements MultipartFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contentType;

    @Transient
    private String originalFilename;
    @Transient
    private boolean empty = false;

    @ManyToOne(targetEntity = Car.class)
    @JoinColumn(name = "car_id", nullable = false, foreignKey = @ForeignKey(name = "car_id_fkey"))
    @JsonBackReference
    private Car car;
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] bytes;


    @Override
    public long getSize() {
        return 0;
    }


    @Override
    public InputStream getInputStream() {
        return null;
    }

    @Override
    public void transferTo(File file) throws IllegalStateException {

    }
}
