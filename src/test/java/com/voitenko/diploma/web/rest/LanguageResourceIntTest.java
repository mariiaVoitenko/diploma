package com.voitenko.diploma.web.rest;

import com.voitenko.diploma.DiplomaApp;
import com.voitenko.diploma.domain.Language;
import com.voitenko.diploma.repository.LanguageRepository;

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
 * Test class for the LanguageResource REST controller.
 *
 * @see LanguageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DiplomaApp.class)
@WebAppConfiguration
@IntegrationTest
public class LanguageResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private LanguageRepository languageRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLanguageMockMvc;

    private Language language;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LanguageResource languageResource = new LanguageResource();
        ReflectionTestUtils.setField(languageResource, "languageRepository", languageRepository);
        this.restLanguageMockMvc = MockMvcBuilders.standaloneSetup(languageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        language = new Language();
        language.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createLanguage() throws Exception {
        int databaseSizeBeforeCreate = languageRepository.findAll().size();

        // Create the Language

        restLanguageMockMvc.perform(post("/api/languages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(language)))
                .andExpect(status().isCreated());

        // Validate the Language in the database
        List<Language> languages = languageRepository.findAll();
        assertThat(languages).hasSize(databaseSizeBeforeCreate + 1);
        Language testLanguage = languages.get(languages.size() - 1);
        assertThat(testLanguage.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllLanguages() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languages
        restLanguageMockMvc.perform(get("/api/languages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get the language
        restLanguageMockMvc.perform(get("/api/languages/{id}", language.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(language.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLanguage() throws Exception {
        // Get the language
        restLanguageMockMvc.perform(get("/api/languages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Update the language
        Language updatedLanguage = new Language();
        updatedLanguage.setId(language.getId());
        updatedLanguage.setName(UPDATED_NAME);

        restLanguageMockMvc.perform(put("/api/languages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLanguage)))
                .andExpect(status().isOk());

        // Validate the Language in the database
        List<Language> languages = languageRepository.findAll();
        assertThat(languages).hasSize(databaseSizeBeforeUpdate);
        Language testLanguage = languages.get(languages.size() - 1);
        assertThat(testLanguage.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);
        int databaseSizeBeforeDelete = languageRepository.findAll().size();

        // Get the language
        restLanguageMockMvc.perform(delete("/api/languages/{id}", language.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Language> languages = languageRepository.findAll();
        assertThat(languages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
