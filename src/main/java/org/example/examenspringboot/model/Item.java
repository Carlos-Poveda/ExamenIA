package org.example.examenspringboot.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "items")
public class Item {

    @Id
    private String id;
    private String title;
    private String price;
    private String category;
    private String description;
    private String rate;
    private Integer count;
    private String color;
    private String manufacturer;
    private String EAN;
    private String image;
}
