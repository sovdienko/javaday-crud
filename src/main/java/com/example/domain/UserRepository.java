package com.example.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Created by sovd on 04.06.2016.
 */
@Component
public interface UserRepository extends CrudRepository <User,Long>{

}
