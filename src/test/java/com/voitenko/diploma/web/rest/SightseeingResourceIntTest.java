package com.voitenko.diploma.web.rest;

import com.voitenko.diploma.DiplomaApp;
import com.voitenko.diploma.domain.Sightseeing;
import com.voitenko.diploma.repository.SightseeingRepository;

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
 * Test class for the SightseeingResource REST controller.
 *
 * @see SightseeingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DiplomaApp.class)
@WebAppConfiguration
@IntegrationTest
public class SightseeingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_INFO = "AAAAA";
    private static final String UPDATED_INFO = "BBBBB";

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;

    private static final Float DEFAULT_LONGITUDE = 1F;
    private static final Float UPDATED_LONGITUDE = 2F;
    private static final String DEFAULT_PHOTO = "AAAAA";
    private static final String UPDATED_PHOTO = "BBBBB";

    private static final Float DEFAULT_RATING = 1F;
    private static final Float UPDATED_RATING = 2F;

    @Inject
    private SightseeingRepository sightseeingRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSightseeingMockMvc;

    private Sightseeing sightseeing;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SightseeingResource sightseeingResource = new SightseeingResource();
        ReflectionTestUtils.setField(sightseeingResource, "sightseeingRepository", sightseeingRepository);
        this.restSightseeingMockMvc = MockMvcBuilders.standaloneSetup(sightseeingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sightseeing = new Sightseeing();
        sightseeing.setName(DEFAULT_NAME);
        sightseeing.setInfo(DEFAULT_INFO);
        sightseeing.setLatitude(DEFAULT_LATITUDE);
        sightseeing.setLongitude(DEFAULT_LONGITUDE);
        sightseeing.setPhoto(DEFAULT_PHOTO);
        sightseeing.setRating(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void createSightseeing() throws Exception {
        int databaseSizeBeforeCreate = sightseeingRepository.findAll().size();

        // Create the Sightseeing

        restSightseeingMockMvc.perform(post("/api/sightseeings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sightseeing)))
                .andExpect(status().isCreated());

        // Validate the Sightseeing in the database
        List<Sightseeing> sightseeings = sightseeingRepository.findAll();
        assertThat(sightseeings).hasSize(databaseSizeBeforeCreate + 1);
        Sightseeing testSightseeing = sightseeings.get(sightseeings.size() - 1);
        assertThat(testSightseeing.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSightseeing.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testSightseeing.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testSightseeing.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testSightseeing.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testSightseeing.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sightseeingRepository.findAll().size();
        // set the field null
        sightseeing.setName(null);

        // Create the Sightseeing, which fails.

        restSightseeingMockMvc.perform(post("/api/sightseeings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sightseeing)))
                .andExpect(status().isBadRequest());

        List<Sightseeing> sightseeings = sightseeingRepository.findAll();
        assertThat(sightseeings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInfoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sightseeingRepository.findAll().size();
        // set the field null
        sightseeing.setInfo(null);

        // Create the Sightseeing, which fails.

        restSightseeingMockMvc.perform(post("/api/sightseeings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sightseeing)))
                .andExpect(status().isBadRequest());

        List<Sightseeing> sightseeings = sightseeingRepository.findAll();
        assertThat(sightseeings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sightseeingRepository.findAll().size();
        // set the field null
        sightseeing.setLatitude(null);

        // Create the Sightseeing, which fails.

        restSightseeingMockMvc.perform(post("/api/sightseeings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sightseeing)))
                .andExpect(status().isBadRequest());

        List<Sightseeing> sightseeings = sightseeingRepository.findAll();
        assertThat(sightseeings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sightseeingRepository.findAll().size();
        // set the field null
        sightseeing.setLongitude(null);

        // Create the Sightseeing, which fails.

        restSightseeingMockMvc.perform(post("/api/sightseeings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sightseeing)))
                .andExpect(status().isBadRequest());

        List<Sightseeing> sightseeings = sightseeingRepository.findAll();
        assertThat(sightseeings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhotoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sightseeingRepository.findAll().size();
        // set the field null
        sightseeing.setPhoto(null);

        // Create the Sightseeing, which fails.

        restSightseeingMockMvc.perform(post("/api/sightseeings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sightseeing)))
                .andExpect(status().isBadRequest());

        List<Sightseeing> sightseeings = sightseeingRepository.findAll();
        assertThat(sightseeings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = sightseeingRepository.findAll().size();
        // set the field null
        sightseeing.setRating(null);

        // Create the Sightseeing, which fails.

        restSightseeingMockMvc.perform(post("/api/sightseeings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sightseeing)))
                .andExpect(status().isBadRequest());

        List<Sightseeing> sightseeings = sightseeingRepository.findAll();
        assertThat(sightseeings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSightseeings() throws Exception {
        // Initialize the database
        sightseeingRepository.saveAndFlush(sightseeing);

        // Get all the sightseeings
        restSightseeingMockMvc.perform(get("/api/sightseeings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sightseeing.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO.toString())))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())));
    }

    @Test
    @Transactional
    public void getSightseeing() throws Exception {
        // Initialize the database
        sightseeingRepository.saveAndFlush(sightseeing);

        // Get the sightseeing
        restSightseeingMockMvc.perform(get("/api/sightseeings/{id}", sightseeing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sightseeing.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSightseeing() throws Exception {
        // Get the sightseeing
        restSightseeingMockMvc.perform(get("/api/sightseeings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSightseeing() throws Exception {
        // Initialize the database
        sightseeingRepository.saveAndFlush(sightseeing);
        int databaseSizeBeforeUpdate = sightseeingRepository.findAll().size();

        // Update the sightseeing
        Sightseeing updatedSightseeing = new Sightseeing();
        updatedSightseeing.setId(sightseeing.getId());
        updatedSightseeing.setName(UPDATED_NAME);
        updatedSightseeing.setInfo(UPDATED_INFO);
        updatedSightseeing.setLatitude(UPDATED_LATITUDE);
        updatedSightseeing.setLongitude(UPDATED_LONGITUDE);
        updatedSightseeing.setPhoto(UPDATED_PHOTO);
        updatedSightseeing.setRating(UPDATED_RATING);

        restSightseeingMockMvc.perform(put("/api/sightseeings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSightseeing)))
                .andExpect(status().isOk());

        // Validate the Sightseeing in the database
        List<Sightseeing> sightseeings = sightseeingRepository.findAll();
        assertThat(sightseeings).hasSize(databaseSizeBeforeUpdate);
        Sightseeing testSightseeing = sightseeings.get(sightseeings.size() - 1);
        assertThat(testSightseeing.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSightseeing.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testSightseeing.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testSightseeing.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testSightseeing.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testSightseeing.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    public void deleteSightseeing() throws Exception {
        // Initialize the database
        sightseeingRepository.saveAndFlush(sightseeing);
        int databaseSizeBeforeDelete = sightseeingRepository.findAll().size();

        // Get the sightseeing
        restSightseeingMockMvc.perform(delete("/api/sightseeings/{id}", sightseeing.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sightseeing> sightseeings = sightseeingRepository.findAll();
        assertThat(sightseeings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
