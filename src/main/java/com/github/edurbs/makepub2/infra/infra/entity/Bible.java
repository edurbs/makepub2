package com.github.edurbs.makepub2.infra.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Bible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include    
    private Long id;

    @Column
    @NotNull
    private String version;

    @Column
    @NotNull
    private String book;

    @Column
    @NotNull
    private String chapter;

    @Column
    @NotNull
    private String verse;

    @Column
    private String text;

}
