package com.example.kikuyumoviestore.model;

import jakarta.persistence.*;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(name = "cover_image",columnDefinition = "LONGBLOB")
    private byte[] coverImage;

    private String coverImageType;

    @Lob
    @Column(name = "trailer_data",columnDefinition = "LONGBLOB")
    private byte[] trailerData;

    private String trailerType;

    // Constructors
    public Movie() {
    }

    public Movie(String title, byte[] coverImage, String coverImageType, byte[] trailerData, String trailerType) {
        this.title = title;
        this.coverImage = coverImage;
        this.coverImageType = coverImageType;
        this.trailerData = trailerData;
        this.trailerType = trailerType;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public String getCoverImageType() {
        return coverImageType;
    }

    public void setCoverImageType(String coverImageType) {
        this.coverImageType = coverImageType;
    }

    public byte[] getTrailerData() {
        return trailerData;
    }

    public void setTrailerData(byte[] trailerData) {
        this.trailerData = trailerData;
    }

    public String getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(String trailerType) {
        this.trailerType = trailerType;
    }
}
