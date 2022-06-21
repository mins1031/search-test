package com.example.musinsasearch.datainsert;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.product.domain.Product;
import com.example.musinsasearch.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class DataInserter {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

//    @Test
    public void 데이터_입력() {
        //카테고리
        Category 상의 = categoryRepository.save(Category.createCategory("상의"));
        Category 아우터 = categoryRepository.save(Category.createCategory("아우터"));
        Category 바지 = categoryRepository.save(Category.createCategory("바지"));
        Category 스니커즈 = categoryRepository.save(Category.createCategory("스니커즈"));
        Category 가방 = categoryRepository.save(Category.createCategory("가방"));
        Category 모자 = categoryRepository.save(Category.createCategory("모자"));
        Category 양말 = categoryRepository.save(Category.createCategory("양말"));
        Category 액세서리 = categoryRepository.save(Category.createCategory("액세서리"));

        //브랜드
        Brand a = brandRepository.save(Brand.createBrand("A"));
        Brand b = brandRepository.save(Brand.createBrand("B"));
        Brand c = brandRepository.save(Brand.createBrand("C"));
        Brand d = brandRepository.save(Brand.createBrand("D"));
        Brand e = brandRepository.save(Brand.createBrand("E"));
        Brand f = brandRepository.save(Brand.createBrand("F"));
        Brand g = brandRepository.save(Brand.createBrand("G"));
        Brand h = brandRepository.save(Brand.createBrand("H"));
        Brand i = brandRepository.save(Brand.createBrand("I"));

        //상품
        List<Integer> aPrices = Arrays.asList(11200, 5500, 4200, 9000, 2000, 1700, 1800, 2300);
        List<Integer> bPrices = Arrays.asList(10500, 5900, 3800, 9100, 2100, 2000, 2000, 2200);
        List<Integer> cPrices = Arrays.asList(10000, 6200, 3300, 9200, 2200, 1900, 2200, 2100);
        List<Integer> dPrices = Arrays.asList(10100, 5100, 3000, 9500, 2500, 1500, 2400, 2000);
        List<Integer> ePrices = Arrays.asList(10700, 5000, 3800, 9900, 2300, 1800, 2100, 2100);
        List<Integer> fPrices = Arrays.asList(11200, 7200, 4000, 9300, 2100, 1600, 2300, 1900);
        List<Integer> gPrices = Arrays.asList(10500, 5800, 3900, 9000, 2200, 1700, 2100, 2000);
        List<Integer> hPrices = Arrays.asList(10800, 6300, 3100, 9700, 2100, 1600, 2000, 2000);
        List<Integer> iPrices = Arrays.asList(11400, 6700, 3200, 9500, 2400, 1700, 1700, 2400);

        productRepository.save(Product.createProduct("상의1", 11200, a, 상의));
        productRepository.save(Product.createProduct("아우터1", 5500, a, 아우터));
        productRepository.save(Product.createProduct("바지1", 4200, a, 바지));
        productRepository.save(Product.createProduct("스니커즈1", 9000, a, 스니커즈));
        productRepository.save(Product.createProduct("가방1", 2000, a, 가방));
        productRepository.save(Product.createProduct("모자1", 1700, a, 모자));
        productRepository.save(Product.createProduct("양말1", 1800, a, 양말));
        productRepository.save(Product.createProduct("액세서리1", 2300, a, 액세서리));

        productRepository.save(Product.createProduct("상의2", 10500, b, 상의));
        productRepository.save(Product.createProduct("아우터2", 5900, b, 아우터));
        productRepository.save(Product.createProduct("바지2", 3800, b, 바지));
        productRepository.save(Product.createProduct("스니커즈2", 9100, b, 스니커즈));
        productRepository.save(Product.createProduct("가방2", 2100, b, 가방));
        productRepository.save(Product.createProduct("모자2", 2000, b, 모자));
        productRepository.save(Product.createProduct("양말2", 2000, b, 양말));
        productRepository.save(Product.createProduct("액세서리2", 2200, b, 액세서리));

        productRepository.save(Product.createProduct("상의3", 10000, c, 상의));
        productRepository.save(Product.createProduct("아우터3", 6200, c, 아우터));
        productRepository.save(Product.createProduct("바지3", 3300, c, 바지));
        productRepository.save(Product.createProduct("스니커즈3", 9200, c, 스니커즈));
        productRepository.save(Product.createProduct("가방3", 2200, c, 가방));
        productRepository.save(Product.createProduct("모자3", 1900, c, 모자));
        productRepository.save(Product.createProduct("양말3", 2200, c, 양말));
        productRepository.save(Product.createProduct("액세서리3", 2100, c, 액세서리));

        productRepository.save(Product.createProduct("상의4", 10100, d, 상의));
        productRepository.save(Product.createProduct("아우터4", 5100, d, 아우터));
        productRepository.save(Product.createProduct("바지4", 3000, d, 바지));
        productRepository.save(Product.createProduct("스니커즈4", 9500, d, 스니커즈));
        productRepository.save(Product.createProduct("가방4", 2500, d, 가방));
        productRepository.save(Product.createProduct("모자4", 1500, d, 모자));
        productRepository.save(Product.createProduct("양말4", 2400, d, 양말));
        productRepository.save(Product.createProduct("액세서리4", 2000, d, 액세서리));

        productRepository.save(Product.createProduct("상의5", 10700, e, 상의));
        productRepository.save(Product.createProduct("아우터5", 5000, e, 아우터));
        productRepository.save(Product.createProduct("바지5", 3800, e, 바지));
        productRepository.save(Product.createProduct("스니커즈5", 9900, e, 스니커즈));
        productRepository.save(Product.createProduct("가방5", 2300, e, 가방));
        productRepository.save(Product.createProduct("모자5", 1800, e, 모자));
        productRepository.save(Product.createProduct("양말5", 2100, e, 양말));
        productRepository.save(Product.createProduct("액세서리5", 2100, e, 액세서리));

        productRepository.save(Product.createProduct("상의6", 11200, f, 상의));
        productRepository.save(Product.createProduct("아우터6", 7200, f, 아우터));
        productRepository.save(Product.createProduct("바지6", 4000, f, 바지));
        productRepository.save(Product.createProduct("스니커즈6", 9300, f, 스니커즈));
        productRepository.save(Product.createProduct("가방6", 2100, f, 가방));
        productRepository.save(Product.createProduct("모자6", 1600, f, 모자));
        productRepository.save(Product.createProduct("양말6", 2300, f, 양말));
        productRepository.save(Product.createProduct("액세서리6", 1900, f, 액세서리));

        productRepository.save(Product.createProduct("상의7", 10500, g, 상의));
        productRepository.save(Product.createProduct("아우터7", 5800, g, 아우터));
        productRepository.save(Product.createProduct("바지7", 3900, g, 바지));
        productRepository.save(Product.createProduct("스니커즈7", 9000, g, 스니커즈));
        productRepository.save(Product.createProduct("가방7", 2200, g, 가방));
        productRepository.save(Product.createProduct("모자7", 1700, g, 모자));
        productRepository.save(Product.createProduct("양말7", 2100, g, 양말));
        productRepository.save(Product.createProduct("액세서리7", 2000, g, 액세서리));

        productRepository.save(Product.createProduct("상의8", 10800, h, 상의));
        productRepository.save(Product.createProduct("아우터8", 6300, h, 아우터));
        productRepository.save(Product.createProduct("바지8", 3100, h, 바지));
        productRepository.save(Product.createProduct("스니커즈8", 9700, h, 스니커즈));
        productRepository.save(Product.createProduct("가방8", 2100, h, 가방));
        productRepository.save(Product.createProduct("모자8", 1600, h, 모자));
        productRepository.save(Product.createProduct("양말8", 2000, h, 양말));
        productRepository.save(Product.createProduct("액세서리8", 2000, h, 액세서리));

        productRepository.save(Product.createProduct("상의9", 11400, i, 상의));
        productRepository.save(Product.createProduct("아우터9", 6700, i, 아우터));
        productRepository.save(Product.createProduct("바지9", 3200, i, 바지));
        productRepository.save(Product.createProduct("스니커즈9", 9500, i, 스니커즈));
        productRepository.save(Product.createProduct("가방9", 2400, i, 가방));
        productRepository.save(Product.createProduct("모자9", 1700, i, 모자));
        productRepository.save(Product.createProduct("양말9", 1700, i, 양말));
        productRepository.save(Product.createProduct("액세서리9", 2400, i, 액세서리));


        //1. select min(price) from product group by category_num
        //2. select b.num, b.name, sum(p.price) sum_price from product as p join brand as b on p.brand_num = b.num group by p.brand_num 후 price로
        //3. select max(price), min(price) from product where category_num = 9 -> 이름값으로 먼저 카테고리 조회한다음 해당 쿼리로 카테고리 num줘야할듯.

    }
}
