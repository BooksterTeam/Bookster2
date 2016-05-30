package io.bookster.web.rest;

import io.bookster.Bookster2App;
import io.bookster.domain.Lending;
import io.bookster.repository.LendingRepository;
import io.bookster.service.LendingService;
import io.bookster.repository.search.LendingSearchRepository;

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


/**
 * Test class for the LendingResource REST controller.
 *
 * @see LendingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Bookster2App.class)
@WebAppConfiguration
@IntegrationTest
public class LendingResourceIntTest {


    private static final LocalDate DEFAULT_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DUE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private LendingRepository lendingRepository;

    @Inject
    private LendingService lendingService;

    @Inject
    private LendingSearchRepository lendingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLendingMockMvc;

    private Lending lending;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LendingResource lendingResource = new LendingResource();
        ReflectionTestUtils.setField(lendingResource, "lendingService", lendingService);
        this.restLendingMockMvc = MockMvcBuilders.standaloneSetup(lendingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lendingSearchRepository.deleteAll();
        lending = new Lending();
        lending.setFrom(DEFAULT_FROM);
        lending.setDue(DEFAULT_DUE);
    }

    @Test
    @Transactional
    public void createLending() throws Exception {
        int databaseSizeBeforeCreate = lendingRepository.findAll().size();

        // Create the Lending

        restLendingMockMvc.perform(post("/api/lendings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lending)))
                .andExpect(status().isCreated());

        // Validate the Lending in the database
        List<Lending> lendings = lendingRepository.findAll();
        assertThat(lendings).hasSize(databaseSizeBeforeCreate + 1);
        Lending testLending = lendings.get(lendings.size() - 1);
        assertThat(testLending.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testLending.getDue()).isEqualTo(DEFAULT_DUE);

        // Validate the Lending in ElasticSearch
        Lending lendingEs = lendingSearchRepository.findOne(testLending.getId());
        assertThat(lendingEs).isEqualToComparingFieldByField(testLending);
    }

    @Test
    @Transactional
    public void checkFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = lendingRepository.findAll().size();
        // set the field null
        lending.setFrom(null);

        // Create the Lending, which fails.

        restLendingMockMvc.perform(post("/api/lendings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lending)))
                .andExpect(status().isBadRequest());

        List<Lending> lendings = lendingRepository.findAll();
        assertThat(lendings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDueIsRequired() throws Exception {
        int databaseSizeBeforeTest = lendingRepository.findAll().size();
        // set the field null
        lending.setDue(null);

        // Create the Lending, which fails.

        restLendingMockMvc.perform(post("/api/lendings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lending)))
                .andExpect(status().isBadRequest());

        List<Lending> lendings = lendingRepository.findAll();
        assertThat(lendings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLendings() throws Exception {
        // Initialize the database
        lendingRepository.saveAndFlush(lending);

        // Get all the lendings
        restLendingMockMvc.perform(get("/api/lendings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lending.getId().intValue())))
                .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
                .andExpect(jsonPath("$.[*].due").value(hasItem(DEFAULT_DUE.toString())));
    }

    @Test
    @Transactional
    public void getLending() throws Exception {
        // Initialize the database
        lendingRepository.saveAndFlush(lending);

        // Get the lending
        restLendingMockMvc.perform(get("/api/lendings/{id}", lending.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lending.getId().intValue()))
            .andExpect(jsonPath("$.from").value(DEFAULT_FROM.toString()))
            .andExpect(jsonPath("$.due").value(DEFAULT_DUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLending() throws Exception {
        // Get the lending
        restLendingMockMvc.perform(get("/api/lendings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLending() throws Exception {
        // Initialize the database
        lendingService.save(lending);

        int databaseSizeBeforeUpdate = lendingRepository.findAll().size();

        // Update the lending
        Lending updatedLending = new Lending();
        updatedLending.setId(lending.getId());
        updatedLending.setFrom(UPDATED_FROM);
        updatedLending.setDue(UPDATED_DUE);

        restLendingMockMvc.perform(put("/api/lendings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLending)))
                .andExpect(status().isOk());

        // Validate the Lending in the database
        List<Lending> lendings = lendingRepository.findAll();
        assertThat(lendings).hasSize(databaseSizeBeforeUpdate);
        Lending testLending = lendings.get(lendings.size() - 1);
        assertThat(testLending.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testLending.getDue()).isEqualTo(UPDATED_DUE);

        // Validate the Lending in ElasticSearch
        Lending lendingEs = lendingSearchRepository.findOne(testLending.getId());
        assertThat(lendingEs).isEqualToComparingFieldByField(testLending);
    }

    @Test
    @Transactional
    public void deleteLending() throws Exception {
        // Initialize the database
        lendingService.save(lending);

        int databaseSizeBeforeDelete = lendingRepository.findAll().size();

        // Get the lending
        restLendingMockMvc.perform(delete("/api/lendings/{id}", lending.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lendingExistsInEs = lendingSearchRepository.exists(lending.getId());
        assertThat(lendingExistsInEs).isFalse();

        // Validate the database is empty
        List<Lending> lendings = lendingRepository.findAll();
        assertThat(lendings).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLending() throws Exception {
        // Initialize the database
        lendingService.save(lending);

        // Search the lending
        restLendingMockMvc.perform(get("/api/_search/lendings?query=id:" + lending.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lending.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].due").value(hasItem(DEFAULT_DUE.toString())));
    }
}
