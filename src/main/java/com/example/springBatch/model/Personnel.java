package com.example.springBatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Personnel {

    String prenom;
    String nom;
    Integer age;
    Boolean active;
    String date;
}
