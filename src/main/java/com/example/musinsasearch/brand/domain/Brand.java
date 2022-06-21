package com.example.musinsasearch.brand.domain;

import com.example.musinsasearch.common.domain.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BasicEntity {

    private String name;

    private Brand(String name) {
        this.name = name;
    }

    public static Brand createBrand(String name) {
        return new Brand(name);
    }
}
