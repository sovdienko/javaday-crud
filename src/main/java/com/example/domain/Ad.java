package com.example.domain;

import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by sergey.ovdienko on 03.06.2016.
 */

@Entity
public class Ad implements Identifiable<Long>{

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        BUY,
        SELL
    }

    @Column(nullable = false)
    private BigInteger amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public enum Currency {
        USD,
        EUR
    }

    @Column(nullable = false)
    private BigDecimal rate;


    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private Location location;

    @Embeddable
    public static class Location{

        @Column(nullable = false)
        private String city;

        private String area;
    }

    @Override
    public Long getId() {
        return null;
    }



}
