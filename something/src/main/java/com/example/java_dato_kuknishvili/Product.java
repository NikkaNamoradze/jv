package com.example.java_dato_kuknishvili;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    private int id;
    private String book_name;
    private String book_author;
    private int book_price;

}
