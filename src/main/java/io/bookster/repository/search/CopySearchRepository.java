package io.bookster.repository.search;

import io.bookster.domain.Copy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Copy entity.
 */
public interface CopySearchRepository extends ElasticsearchRepository<Copy, Long> {
}
