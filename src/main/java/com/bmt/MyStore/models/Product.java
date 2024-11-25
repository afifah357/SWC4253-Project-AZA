package com.bmt.MyStore.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String brand;
    private String category;
    private double price;

    @Column(columnDefinition = "TEXT")
    private String description;
    private Date created_at;
    private String image_file_name;

    // Default constructor
    public Product() {
    }

    // Parameterized constructor
    public Product(String name, String brand, String category, double price, String description, Date created_at, String image_file_name) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.description = description;
        this.created_at = created_at;
        this.image_file_name = image_file_name;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public String getImageFileName() {
        return image_file_name;
    }

    public void setImageFileName(String image_file_name) {
        this.image_file_name = image_file_name;
    }

    // Override toString method (optional)
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", createdAt=" + created_at +
                ", imageFileName='" + image_file_name + '\'' +
                '}';
    }
}
