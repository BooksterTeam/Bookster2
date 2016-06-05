package io.bookster.service;

import io.bookster.domain.BooksterUser;
import io.bookster.domain.LendingRequest;
import io.bookster.repository.LendingRequestRepository;
import io.bookster.repository.search.LendingRequestSearchRepository;
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
 * Service Implementation for managing LendingRequest.
 */
@Service
@Transactional
public class LendingRequestService {

    private final Logger log = LoggerFactory.getLogger(LendingRequestService.class);

    @Inject
    private LendingRequestRepository lendingRequestRepository;

    @Inject
    private LendingRequestSearchRepository lendingRequestSearchRepository;

    /**
     * Save a lendingRequest.
     *
     * @param lendingRequest the entity to save
     * @return the persisted entity
     */
    public LendingRequest save(LendingRequest lendingRequest) {
        log.debug("Request to save LendingRequest : {}", lendingRequest);
        LendingRequest result = lendingRequestRepository.save(lendingRequest);
        lendingRequestSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the lendingRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LendingRequest> findAll(Pageable pageable) {
        log.debug("Request to get all LendingRequests");
        Page<LendingRequest> result = lendingRequestRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one lendingRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public LendingRequest findOne(Long id) {
        log.debug("Request to get LendingRequest : {}", id);
        LendingRequest lendingRequest = lendingRequestRepository.findOne(id);
        return lendingRequest;
    }

    /**
     * Delete the  lendingRequest by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LendingRequest : {}", id);
        lendingRequestRepository.delete(id);
        lendingRequestSearchRepository.delete(id);
    }

    /**
     * Search for the lendingRequest corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LendingRequest> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LendingRequests for query {}", query);
        return lendingRequestSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Transactional(readOnly = true)
    public List<LendingRequest> getAllLendingRequestsByUser(BooksterUser booksterUser) {
        return lendingRequestRepository.findAll().stream()
                .filter(lendingRequest -> lendingRequest.getBooksterUser().getId().equals(booksterUser.getId()))
                .collect(Collectors.toList());
    }
}
