package com.example.musinsasearch.common.basetest;

import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
public class IntegrateBaseTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected BrandRepository brandRepository;

    @Autowired
    protected ProductRepository productRepository;

    protected MockMvc mockMvc;

    @BeforeEach
    protected void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}
