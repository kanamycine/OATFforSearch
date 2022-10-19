package com.team6.onandthefarm.entity.category;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SequenceGenerator(
        name="CATEGORY_SEQ_GENERATOR",
        sequenceName = "CATEGORY_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "CATEGORY_SEQ_GENERATOR")
    private Long categoryId;

    private String categoryName;
}
