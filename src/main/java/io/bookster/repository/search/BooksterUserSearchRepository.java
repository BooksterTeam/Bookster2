package io.bookster.repository.search;

import io.bookster.domain.BooksterUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BooksterUser entity.
 */
public interface BooksterUserSearchRepository extends ElasticsearchRepository<BooksterUser, Long> {
}
