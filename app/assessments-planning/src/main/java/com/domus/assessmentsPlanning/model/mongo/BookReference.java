package com.domus.assessmentsPlanning.model.mongo;

import lombok.Data;

@Data
public class BookReference {
    private String title;
    private String author;
    private String isbn;
}