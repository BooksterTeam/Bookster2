package io.bookster.web.rest;

import io.bookster.Bookster2App;
import io.bookster.domain.LendingRequest;
import io.bookster.repository.LendingRequestRepository;
import io.bookster.service.LendingRequestService;
import io.bookster.repository.search.LendingRequestSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.bookster.domain.enumeration.RequestStatus;

/**
 * Test class for the LendingRequestResource REST controller.
 *
 * @see LendingRequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Bookster2App.class)
@WebAppConfiguration
@IntegrationTest
public class LendingRequestResourceIntTest {


    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final RequestStatus DEFAULT_STATUS = RequestStatus.PENDING;
    private static final RequestStatus UPDATED_STATUS = RequestStatus.CANCELED;

    @Inject
    private LendingRequestRepository lendingRequestRepository;

    @Inject
    private LendingRequestService lendingRequestService;

    @Inject
    private LendingRequestSearchRepository lendingRequestSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLendingRequestMockMvc;

    private LendingRequest lendingRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LendingRequestResource lendingRequestResource = new LendingRequestResource();
        ReflectionTestUtils.setField(lendingRequestResource, "lendingRequestService", lendingRequestService);
        this.restLendingRequestMockMvc = MockMvcBuilders.standaloneSetup(lendingRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lendingRequestSearchRepository.deleteAll();
        lendingRequest = new LendingRequest();
        lendingRequest.setDate(DEFAULT_DATE);
        lendingRequest.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createLendingRequest() throws Exception {
        int databaseSizeBeforeCreate = lendingRequestRepository.findAll().size();

        // Create the LendingRequest

        restLendingRequestMockMvc.perform(post("/api/lending-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lendingRequest)))
                .andExpect(status().isCreated());

        // Validate the LendingRequest in the database
        List<LendingRequest> lendingRequests = lendingRequestRepository.findAll();
        assertThat(lendingRequests).hasSize(databaseSizeBeforeCreate + 1);
        LendingRequest testLendingRequest = lendingRequests.get(lendingRequests.size() - 1);
        assertThat(testLendingRequest.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testLendingRequest.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the LendingRequest in ElasticSearch
        LendingRequest lendingRequestEs = lendingRequestSearchRepository.findOne(testLendingRequest.getId());
        assertThat(lendingRequestEs).isEqualToComparingFieldByField(testLendingRequest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = lendingRequestRepository.findAll().size();
        // set the field null
        lendingRequest.setDate(null);

        // Create the LendingRequest, which fails.

        restLendingRequestMockMvc.perform(post("/api/lending-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lendingRequest)))
                .andExpect(status().isBadRequest());

        List<LendingRequest> lendingRequests = lendingRequestRepository.findAll();
        assertThat(lendingRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = lendingRequestRepository.findAll().size();
        // set the field null
        lendingRequest.setStatus(null);

        // Create the LendingRequest, which fails.

        restLendingRequestMockMvc.perform(post("/api/lending-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lendingRequest)))
                .andExpect(status().isBadRequest());

        List<LendingRequest> lendingRequests = lendingRequestRepository.findAll();
        assertThat(lendingRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLendingRequests() throws Exception {
        // Initialize the database
        lendingRequestRepository.saveAndFlush(lendingRequest);

        // Get all the lendingRequests
        restLendingRequestMockMvc.perform(get("/api/lending-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lendingRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getLendingRequest() throws Exception {
        // Initialize the database
        lendingRequestRepository.saveAndFlush(lendingRequest);

        // Get the lendingRequest
        restLendingRequestMockMvc.perform(get("/api/lending-requests/{id}", lendingRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lendingRequest.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLendingRequest() throws Exception {
        // Get the lendingRequest
        restLendingRequestMockMvc.perform(get("/api/lending-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLendingRequest() throws Exception {
        // Initialize the database
        lendingRequestService.save(lendingRequest);

        int databaseSizeBeforeUpdate = lendingRequestRepository.findAll().size();

        // Update the lendingRequest
        LendingRequest updatedLendingRequest = new LendingRequest();
        updatedLendingRequest.setId(lendingRequest.getId());
        updatedLendingRequest.setDate(UPDATED_DATE);
        updatedLendingRequest.setStatus(UPDATED_STATUS);

        restLendingRequestMockMvc.perform(put("/api/lending-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLendingRequest)))
                .andExpect(status().isOk());

        // Validate the LendingRequest in the database
        List<LendingRequest> lendingRequests = lendingRequestRepository.findAll();
        assertThat(lendingRequests).hasSize(databaseSizeBeforeUpdate);
        LendingRequest testLendingRequest = lendingRequests.get(lendingRequests.size() - 1);
        assertThat(testLendingRequest.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testLendingRequest.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the LendingRequest in ElasticSearch
        LendingRequest lendingRequestEs = lendingRequestSearchRepository.findOne(testLendingRequest.getId());
        assertThat(lendingRequestEs).isEqualToComparingFieldByField(testLendingRequest);
    }

    @Test
    @Transactional
    public void deleteLendingRequest() throws Exception {
        // Initialize the database
        lendingRequestService.save(lendingRequest);

        int databaseSizeBeforeDelete = lendingRequestRepository.findAll().size();

        // Get the lendingRequest
        restLendingRequestMockMvc.perform(delete("/api/lending-requests/{id}", lendingRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lendingRequestExistsInEs = lendingRequestSearchRepository.exists(lendingRequest.getId());
        assertThat(lendingRequestExistsInEs).isFalse();

        // Validate the database is empty
        List<LendingRequest> lendingRequests = lendingRequestRepository.findAll();
        assertThat(lendingRequests).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLendingRequest() throws Exception {
        // Initialize the database
        lendingRequestService.save(lendingRequest);

        // Search the lendingRequest
        restLendingRequestMockMvc.perform(get("/api/_search/lending-requests?query=id:" + lendingRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lendingRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
