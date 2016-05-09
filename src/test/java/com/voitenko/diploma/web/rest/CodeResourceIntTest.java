package com.voitenko.diploma.web.rest;

import com.voitenko.diploma.DiplomaApp;
import com.voitenko.diploma.domain.Code;
import com.voitenko.diploma.repository.CodeRepository;

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
 * Test class for the CodeResource REST controller.
 *
 * @see CodeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DiplomaApp.class)
@WebAppConfiguration
@IntegrationTest
public class CodeResourceIntTest {

    private static final String DEFAULT_PICTURE = "AAAAA";
    private static final String UPDATED_PICTURE = "BBBBB";

    @Inject
    private CodeRepository codeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCodeMockMvc;

    private Code code;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CodeResource codeResource = new CodeResource();
        ReflectionTestUtils.setField(codeResource, "codeRepository", codeRepository);
        this.restCodeMockMvc = MockMvcBuilders.standaloneSetup(codeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        code = new Code();
        code.setPicture(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void createCode() throws Exception {
        int databaseSizeBeforeCreate = codeRepository.findAll().size();

        // Create the Code

        restCodeMockMvc.perform(post("/api/codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(code)))
                .andExpect(status().isCreated());

        // Validate the Code in the database
        List<Code> codes = codeRepository.findAll();
        assertThat(codes).hasSize(databaseSizeBeforeCreate + 1);
        Code testCode = codes.get(codes.size() - 1);
        assertThat(testCode.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void checkPictureIsRequired() throws Exception {
        int databaseSizeBeforeTest = codeRepository.findAll().size();
        // set the field null
        code.setPicture(null);

        // Create the Code, which fails.

        restCodeMockMvc.perform(post("/api/codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(code)))
                .andExpect(status().isBadRequest());

        List<Code> codes = codeRepository.findAll();
        assertThat(codes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCodes() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);

        // Get all the codes
        restCodeMockMvc.perform(get("/api/codes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(code.getId().intValue())))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())));
    }

    @Test
    @Transactional
    public void getCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);

        // Get the code
        restCodeMockMvc.perform(get("/api/codes/{id}", code.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(code.getId().intValue()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCode() throws Exception {
        // Get the code
        restCodeMockMvc.perform(get("/api/codes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);
        int databaseSizeBeforeUpdate = codeRepository.findAll().size();

        // Update the code
        Code updatedCode = new Code();
        updatedCode.setId(code.getId());
        updatedCode.setPicture(UPDATED_PICTURE);

        restCodeMockMvc.perform(put("/api/codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCode)))
                .andExpect(status().isOk());

        // Validate the Code in the database
        List<Code> codes = codeRepository.findAll();
        assertThat(codes).hasSize(databaseSizeBeforeUpdate);
        Code testCode = codes.get(codes.size() - 1);
        assertThat(testCode.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    public void deleteCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);
        int databaseSizeBeforeDelete = codeRepository.findAll().size();

        // Get the code
        restCodeMockMvc.perform(delete("/api/codes/{id}", code.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Code> codes = codeRepository.findAll();
        assertThat(codes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
