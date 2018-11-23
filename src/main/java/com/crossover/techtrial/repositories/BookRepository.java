/**
 * 
 */
package com.crossover.techtrial.repositories;

import com.crossover.techtrial.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author crossover
 *
 */
@RestResource(exported = false)
public interface BookRepository extends CrudRepository<Book, Long> {

}
