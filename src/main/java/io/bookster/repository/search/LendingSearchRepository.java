package io.bookster.repository.search;

import io.bookster.domain.Lending;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Lending entity.
 */
public interface LendingSearchRepository extends ElasticsearchRepository<Lending, Long> {
}
