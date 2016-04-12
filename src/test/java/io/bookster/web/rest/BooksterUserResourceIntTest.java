package io.bookster.web.rest;

import io.bookster.Bookster2App;
import io.bookster.domain.BooksterUser;
import io.bookster.repository.BooksterUserRepository;
import io.bookster.service.BooksterUserService;
import io.bookster.repository.search.BooksterUserSearchRepository;

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
 * Test class for the BooksterUserResource REST controller.
 *
 * @see BooksterUserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Bookster2App.class)
@WebAppConfiguration
@IntegrationTest
public class BooksterUserResourceIntTest {


    @Inject
    private BooksterUserRepository booksterUserRepository;

    @Inject
    private BooksterUserService booksterUserService;

    @Inject
    private BooksterUserSearchRepository booksterUserSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBooksterUserMockMvc;

    private BooksterUser booksterUser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BooksterUserResource booksterUserResource = new BooksterUserResource();
        ReflectionTestUtils.setField(booksterUserResource, "booksterUserService", booksterUserService);
        this.restBooksterUserMockMvc = MockMvcBuilders.standaloneSetup(booksterUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        booksterUserSearchRepository.deleteAll();
        booksterUser = new BooksterUser();
    }

    @Test
    @Transactional
    public void createBooksterUser() throws Exception {
        int databaseSizeBeforeCreate = booksterUserRepository.findAll().size();

        // Create the BooksterUser

        restBooksterUserMockMvc.perform(post("/api/bookster-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booksterUser)))
                .andExpect(status().isCreated());

        // Validate the BooksterUser in the database
        List<BooksterUser> booksterUsers = booksterUserRepository.findAll();
        assertThat(booksterUsers).hasSize(databaseSizeBeforeCreate + 1);
        BooksterUser testBooksterUser = booksterUsers.get(booksterUsers.size() - 1);

        // Validate the BooksterUser in ElasticSearch
        BooksterUser booksterUserEs = booksterUserSearchRepository.findOne(testBooksterUser.getId());
        assertThat(booksterUserEs).isEqualToComparingFieldByField(testBooksterUser);
    }

    @Test
    @Transactional
    public void getAllBooksterUsers() throws Exception {
        // Initialize the database
        booksterUserRepository.saveAndFlush(booksterUser);

        // Get all the booksterUsers
        restBooksterUserMockMvc.perform(get("/api/bookster-users?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(booksterUser.getId().intValue())));
    }

    @Test
    @Transactional
    public void getBooksterUser() throws Exception {
        // Initialize the database
        booksterUserRepository.saveAndFlush(booksterUser);

        // Get the booksterUser
        restBooksterUserMockMvc.perform(get("/api/bookster-users/{id}", booksterUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(booksterUser.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBooksterUser() throws Exception {
        // Get the booksterUser
        restBooksterUserMockMvc.perform(get("/api/bookster-users/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooksterUser() throws Exception {
        // Initialize the database
        booksterUserService.save(booksterUser);

        int databaseSizeBeforeUpdate = booksterUserRepository.findAll().size();

        // Update the booksterUser
        BooksterUser updatedBooksterUser = new BooksterUser();
        updatedBooksterUser.setId(booksterUser.getId());

        restBooksterUserMockMvc.perform(put("/api/bookster-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBooksterUser)))
                .andExpect(status().isOk());

        // Validate the BooksterUser in the database
        List<BooksterUser> booksterUsers = booksterUserRepository.findAll();
        assertThat(booksterUsers).hasSize(databaseSizeBeforeUpdate);
        BooksterUser testBooksterUser = booksterUsers.get(booksterUsers.size() - 1);

        // Validate the BooksterUser in ElasticSearch
        BooksterUser booksterUserEs = booksterUserSearchRepository.findOne(testBooksterUser.getId());
        assertThat(booksterUserEs).isEqualToComparingFieldByField(testBooksterUser);
    }

    @Test
    @Transactional
    public void deleteBooksterUser() throws Exception {
        // Initialize the database
        booksterUserService.save(booksterUser);

        int databaseSizeBeforeDelete = booksterUserRepository.findAll().size();

        // Get the booksterUser
        restBooksterUserMockMvc.perform(delete("/api/bookster-users/{id}", booksterUser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean booksterUserExistsInEs = booksterUserSearchRepository.exists(booksterUser.getId());
        assertThat(booksterUserExistsInEs).isFalse();

        // Validate the database is empty
        List<BooksterUser> booksterUsers = booksterUserRepository.findAll();
        assertThat(booksterUsers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBooksterUser() throws Exception {
        // Initialize the database
        booksterUserService.save(booksterUser);

        // Search the booksterUser
        restBooksterUserMockMvc.perform(get("/api/_search/bookster-users?query=id:" + booksterUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksterUser.getId().intValue())));
    }
}
