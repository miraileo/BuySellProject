package com.example.buysell.services;

import com.example.buysell.models.Image;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.repositories.ImageRepository;
import com.example.buysell.repositories.ProductRepository;
import com.example.buysell.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    public List<Product> listProducts(String title){
        if(title!=null) return productRepository.findByTitle(title);
        return productRepository.findAll();
    }
    public void save(Principal principal, Product product, MultipartFile file, MultipartFile file1, MultipartFile file2) throws IOException {
        product.setUser(getUserByPrincipal(principal));
        Image image;
        Image image1;
        Image image2;
        if(file.getSize() != 0){
            image = toImageEntity(file);
            image.setPreviewImage(true);
            product.addImageToProduct(image);
        }
        if(file.getSize() != 0){
            image1 = toImageEntity(file1);
            product.addImageToProduct(image1);
        }
        if(file.getSize() != 0){
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    public User getUserByPrincipal(Principal principal) {
        if(principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public Product show(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void edit(Long id, Product updatedProduct, MultipartFile file, MultipartFile file1, MultipartFile file2) throws IOException {
        Product existingProduct = show(id);
        if (existingProduct != null) {
            existingProduct.setTitle(updatedProduct.getTitle());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setCity(updatedProduct.getCity());
            existingProduct.setPrice(updatedProduct.getPrice());
            editImages(existingProduct, file, file1, file2);
            productRepository.save(existingProduct);
        }
    }

    public void editImages(Product existingProduct, MultipartFile file, MultipartFile file1, MultipartFile file2) throws IOException{
        List<Image> images = new ArrayList<>();
        List<MultipartFile> files = new ArrayList<>();
        files.add(file);
        files.add(file1);
        files.add(file2);
        for (int i = 0; i<existingProduct.getImages().size();i++){
            Image image = existingProduct.getImages().get(i);
            if(files.get(i).getSize() != 0) {
                Image newImage = toImageEntity(files.get(i));
                images.add(newImage);
                if(i == 0){
                    images.get(0).setPreviewImage(true);
                }
            }
            else{
                images.add(image);
            }
        }
        for(int i = 0; i<images.size();i++){
            Image image1 = existingProduct.getImages().get(0);
            existingProduct.removeImageFromProduct(image1);
            imageRepository.delete(image1);
        }
        for (Image image : images) {
            existingProduct.addImageToProduct(image);
        }
    }
}
