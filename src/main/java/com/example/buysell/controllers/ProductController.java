package com.example.buysell.controllers;

import com.example.buysell.models.Product;
import com.example.buysell.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product")
    public String Product (@RequestParam (name = "title",required = false) String title, Model model, Principal principal){
        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        return "products";
    }

    @PostMapping("/product")
    public String createProduct(Principal principal, @RequestParam("file") MultipartFile file,
                                @RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                Product product) throws IOException {
        productService.save(principal, product, file, file1, file2);
        return "redirect:/product";
    }

    @GetMapping("/product/{id}")
    public String ShowProductInfo (@PathVariable Long id, Model model){
        Product product = productService.show (id);
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "product-info";
    }

    @PostMapping("/product/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/product";
    }

    @GetMapping("/product/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.show(id);
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "product-edit";
    }

    @PostMapping("/product/{id}/edit")
    public String editProduct(@PathVariable("id") Long id, @ModelAttribute Product product,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              @RequestParam(value = "file1", required = false) MultipartFile file1,
                              @RequestParam(value = "file2", required = false) MultipartFile file2) throws IOException{
        productService.edit(id, product, file, file1, file2);
        return "redirect:/product";
    }
}
