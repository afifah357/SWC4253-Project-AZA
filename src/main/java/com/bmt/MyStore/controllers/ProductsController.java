package com.bmt.MyStore.controllers;

import ch.qos.logback.core.net.server.Client;
import com.bmt.MyStore.models.Product;
import com.bmt.MyStore.models.ProductDto;
import com.bmt.MyStore.services.ProductsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsRepository repo;

    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Product> products = repo.findAll(); // Fetch products from DB
        model.addAttribute("products", products); // Add products to the model
        return "/products"; // Thymeleaf will resolve to templates/products.html
    }

    @GetMapping ("/create")
    public String showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "/products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ) {
        if (productDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "imageFile", "The image file is required"));
        }

        if (result.hasErrors()) {
            return "products/CreateProduct";
        }

        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date ();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);

        repo.save(product);

        return "redirect:/products";
    }


    @GetMapping("/edit")
    public String showEditPage (
            Model model,
            @RequestParam int id
    ){
        try {
            Product product = repo.findById((long) id).get();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());

            model.addAttribute("productDto", productDto);
        }
        catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/products";
        }

        return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String updateProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result,
            @RequestParam int id
    ) {
        if (result.hasErrors()) {
            return "products/EditProduct";
        }

        try {
            Product product = repo.findById((long) id).get();

            // Update the product fields
            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            // Handle new image upload if provided
            if (!productDto.getImageFile().isEmpty()) {
                // Delete old image
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
                Files.deleteIfExists(oldImagePath);

                // Save new image
                MultipartFile image = productDto.getImageFile();
                String storageFileName = new Date().getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                product.setImageFileName(storageFileName);
            }

            repo.save(product);
            return "redirect:/products";

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "products/EditProduct";
        }
    }

    @GetMapping("/delete")
    public String deleteProduct (
            @RequestParam int id
    ) {

        try {
            Product product = repo.findById((long) id).get();

            Path imagePath = Paths.get("public/images/" + product.getImageFileName());

            try {
                Files.delete(imagePath);
            }
            catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }

            repo.delete(product);
        }
        catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/products";
    }
}