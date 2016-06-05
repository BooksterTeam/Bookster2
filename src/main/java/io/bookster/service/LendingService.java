package io.bookster.service;

import io.bookster.domain.BooksterUser;
import io.bookster.domain.Lending;
import io.bookster.repository.LendingRepository;
import io.bookster.repository.search.LendingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Lending.
 */
@Service
@Transactional
public class LendingService {

    private final Logger log = LoggerFactory.getLogger(LendingService.class);

    @Inject
    private LendingRepository lendingRepository;

    @Inject
    private LendingSearchRepository lendingSearchRepository;

    /**
     * Save a lending.
     *
     * @param lending the entity to save
     * @return the persisted entity
     */
    public Lending save(Lending lending) {
        log.debug("Request to save Lending : {}", lending);
        Lending result = lendingRepository.save(lending);
        lendingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the lendings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Lending> findAll(Pageable pageable) {
        log.debug("Request to get all Lendings");
        Page<Lending> result = lendingRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one lending by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Lending findOne(Long id) {
        log.debug("Request to get Lending : {}", id);
        Lending lending = lendingRepository.findOne(id);
        return lending;
    }

    /**
     * Delete the  lending by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Lending : {}", id);
        lendingRepository.delete(id);
        lendingSearchRepository.delete(id);
    }

    /**
     * Search for the lending corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Lending> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Lendings for query {}", query);
        return lendingSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Transactional(readOnly = true)
    public List<Lending> getAllLendingsByUser(BooksterUser booksterUser) {
        log.info("Request all lendings of user {}", booksterUser.getUser().getLogin());
        return lendingRepository.findAll().stream()
                .filter(lending -> lending.getBooksterUser().getId().equals(booksterUser.getId()))
                .collect(Collectors.toList());
    }
}
