package io.bookster.repository.search;

import io.bookster.domain.LendingRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LendingRequest entity.
 */
public interface LendingRequestSearchRepository extends ElasticsearchRepository<LendingRequest, Long> {
}
