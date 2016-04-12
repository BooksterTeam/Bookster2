package io.bookster.web.rest;

import io.bookster.Bookster2App;
import io.bookster.domain.Copy;
import io.bookster.repository.CopyRepository;
import io.bookster.service.CopyService;
import io.bookster.repository.search.CopySearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CopyResource REST controller.
 *
 * @see CopyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Bookster2App.class)
@WebAppConfiguration
@IntegrationTest
public class CopyResourceIntTest {


    private static final Boolean DEFAULT_VERIFIED = false;
    private static final Boolean UPDATED_VERIFIED = true;

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    @Inject
    private CopyRepository copyRepository;

    @Inject
    private CopyService copyService;

    @Inject
    private CopySearchRepository copySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCopyMockMvc;

    private Copy copy;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CopyResource copyResource = new CopyResource();
        ReflectionTestUtils.setField(copyResource, "copyService", copyService);
        this.restCopyMockMvc = MockMvcBuilders.standaloneSetup(copyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        copySearchRepository.deleteAll();
        copy = new Copy();
        copy.setVerified(DEFAULT_VERIFIED);
        copy.setAvailable(DEFAULT_AVAILABLE);
    }

    @Test
    @Transactional
    public void createCopy() throws Exception {
        int databaseSizeBeforeCreate = copyRepository.findAll().size();

        // Create the Copy

        restCopyMockMvc.perform(post("/api/copies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(copy)))
                .andExpect(status().isCreated());

        // Validate the Copy in the database
        List<Copy> copies = copyRepository.findAll();
        assertThat(copies).hasSize(databaseSizeBeforeCreate + 1);
        Copy testCopy = copies.get(copies.size() - 1);
        assertThat(testCopy.isVerified()).isEqualTo(DEFAULT_VERIFIED);
        assertThat(testCopy.isAvailable()).isEqualTo(DEFAULT_AVAILABLE);

        // Validate the Copy in ElasticSearch
        Copy copyEs = copySearchRepository.findOne(testCopy.getId());
        assertThat(copyEs).isEqualToComparingFieldByField(testCopy);
    }

    @Test
    @Transactional
    public void checkVerifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = copyRepository.findAll().size();
        // set the field null
        copy.setVerified(null);

        // Create the Copy, which fails.

        restCopyMockMvc.perform(post("/api/copies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(copy)))
                .andExpect(status().isBadRequest());

        List<Copy> copies = copyRepository.findAll();
        assertThat(copies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = copyRepository.findAll().size();
        // set the field null
        copy.setAvailable(null);

        // Create the Copy, which fails.

        restCopyMockMvc.perform(post("/api/copies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(copy)))
                .andExpect(status().isBadRequest());

        List<Copy> copies = copyRepository.findAll();
        assertThat(copies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCopies() throws Exception {
        // Initialize the database
        copyRepository.saveAndFlush(copy);

        // Get all the copies
        restCopyMockMvc.perform(get("/api/copies?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(copy.getId().intValue())))
                .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())))
                .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())));
    }

    @Test
    @Transactional
    public void getCopy() throws Exception {
        // Initialize the database
        copyRepository.saveAndFlush(copy);

        // Get the copy
        restCopyMockMvc.perform(get("/api/copies/{id}", copy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(copy.getId().intValue()))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCopy() throws Exception {
        // Get the copy
        restCopyMockMvc.perform(get("/api/copies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCopy() throws Exception {
        // Initialize the database
        copyService.save(copy);

        int databaseSizeBeforeUpdate = copyRepository.findAll().size();

        // Update the copy
        Copy updatedCopy = new Copy();
        updatedCopy.setId(copy.getId());
        updatedCopy.setVerified(UPDATED_VERIFIED);
        updatedCopy.setAvailable(UPDATED_AVAILABLE);

        restCopyMockMvc.perform(put("/api/copies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCopy)))
                .andExpect(status().isOk());

        // Validate the Copy in the database
        List<Copy> copies = copyRepository.findAll();
        assertThat(copies).hasSize(databaseSizeBeforeUpdate);
        Copy testCopy = copies.get(copies.size() - 1);
        assertThat(testCopy.isVerified()).isEqualTo(UPDATED_VERIFIED);
        assertThat(testCopy.isAvailable()).isEqualTo(UPDATED_AVAILABLE);

        // Validate the Copy in ElasticSearch
        Copy copyEs = copySearchRepository.findOne(testCopy.getId());
        assertThat(copyEs).isEqualToComparingFieldByField(testCopy);
    }

    @Test
    @Transactional
    public void deleteCopy() throws Exception {
        // Initialize the database
        copyService.save(copy);

        int databaseSizeBeforeDelete = copyRepository.findAll().size();

        // Get the copy
        restCopyMockMvc.perform(delete("/api/copies/{id}", copy.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean copyExistsInEs = copySearchRepository.exists(copy.getId());
        assertThat(copyExistsInEs).isFalse();

        // Validate the database is empty
        List<Copy> copies = copyRepository.findAll();
        assertThat(copies).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCopy() throws Exception {
        // Initialize the database
        copyService.save(copy);

        // Search the copy
        restCopyMockMvc.perform(get("/api/_search/copies?query=id:" + copy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(copy.getId().intValue())))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())));
    }
}
