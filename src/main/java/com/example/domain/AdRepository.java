package com.example.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * Created by sovd on 04.06.2016.
 */
@Component
public interface AdRepository extends PagingAndSortingRepository <Ad, Long> {
}
