package com.voitenko.diploma.web.rest;

import com.voitenko.diploma.DiplomaApp;
import com.voitenko.diploma.domain.Sightseeing_content;
import com.voitenko.diploma.repository.Sightseeing_contentRepository;

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
 * Test class for the Sightseeing_contentResource REST controller.
 *
 * @see Sightseeing_contentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DiplomaApp.class)
@WebAppConfiguration
@IntegrationTest
public class Sightseeing_contentResourceIntTest {


    @Inject
    private Sightseeing_contentRepository sightseeing_contentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSightseeing_contentMockMvc;

    private Sightseeing_content sightseeing_content;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Sightseeing_contentResource sightseeing_contentResource = new Sightseeing_contentResource();
        ReflectionTestUtils.setField(sightseeing_contentResource, "sightseeing_contentRepository", sightseeing_contentRepository);
        this.restSightseeing_contentMockMvc = MockMvcBuilders.standaloneSetup(sightseeing_contentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sightseeing_content = new Sightseeing_content();
    }

    @Test
    @Transactional
    public void createSightseeing_content() throws Exception {
        int databaseSizeBeforeCreate = sightseeing_contentRepository.findAll().size();

        // Create the Sightseeing_content

        restSightseeing_contentMockMvc.perform(post("/api/sightseeing-contents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sightseeing_content)))
                .andExpect(status().isCreated());

        // Validate the Sightseeing_content in the database
        List<Sightseeing_content> sightseeing_contents = sightseeing_contentRepository.findAll();
        assertThat(sightseeing_contents).hasSize(databaseSizeBeforeCreate + 1);
        Sightseeing_content testSightseeing_content = sightseeing_contents.get(sightseeing_contents.size() - 1);
    }

    @Test
    @Transactional
    public void getAllSightseeing_contents() throws Exception {
        // Initialize the database
        sightseeing_contentRepository.saveAndFlush(sightseeing_content);

        // Get all the sightseeing_contents
        restSightseeing_contentMockMvc.perform(get("/api/sightseeing-contents?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sightseeing_content.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSightseeing_content() throws Exception {
        // Initialize the database
        sightseeing_contentRepository.saveAndFlush(sightseeing_content);

        // Get the sightseeing_content
        restSightseeing_contentMockMvc.perform(get("/api/sightseeing-contents/{id}", sightseeing_content.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sightseeing_content.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSightseeing_content() throws Exception {
        // Get the sightseeing_content
        restSightseeing_contentMockMvc.perform(get("/api/sightseeing-contents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSightseeing_content() throws Exception {
        // Initialize the database
        sightseeing_contentRepository.saveAndFlush(sightseeing_content);
        int databaseSizeBeforeUpdate = sightseeing_contentRepository.findAll().size();

        // Update the sightseeing_content
        Sightseeing_content updatedSightseeing_content = new Sightseeing_content();
        updatedSightseeing_content.setId(sightseeing_content.getId());

        restSightseeing_contentMockMvc.perform(put("/api/sightseeing-contents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSightseeing_content)))
                .andExpect(status().isOk());

        // Validate the Sightseeing_content in the database
        List<Sightseeing_content> sightseeing_contents = sightseeing_contentRepository.findAll();
        assertThat(sightseeing_contents).hasSize(databaseSizeBeforeUpdate);
        Sightseeing_content testSightseeing_content = sightseeing_contents.get(sightseeing_contents.size() - 1);
    }

    @Test
    @Transactional
    public void deleteSightseeing_content() throws Exception {
        // Initialize the database
        sightseeing_contentRepository.saveAndFlush(sightseeing_content);
        int databaseSizeBeforeDelete = sightseeing_contentRepository.findAll().size();

        // Get the sightseeing_content
        restSightseeing_contentMockMvc.perform(delete("/api/sightseeing-contents/{id}", sightseeing_content.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sightseeing_content> sightseeing_contents = sightseeing_contentRepository.findAll();
        assertThat(sightseeing_contents).hasSize(databaseSizeBeforeDelete - 1);
    }
}
