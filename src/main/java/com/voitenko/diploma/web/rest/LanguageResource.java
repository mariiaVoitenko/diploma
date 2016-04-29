package com.voitenko.diploma.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.voitenko.diploma.domain.Language;
import com.voitenko.diploma.repository.LanguageRepository;
import com.voitenko.diploma.web.rest.util.HeaderUtil;
import com.voitenko.diploma.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Language.
 */
@RestController
@RequestMapping("/api")
public class LanguageResource {

    private final Logger log = LoggerFactory.getLogger(LanguageResource.class);
        
    @Inject
    private LanguageRepository languageRepository;
    
    /**
     * POST  /languages : Create a new language.
     *
     * @param language the language to create
     * @return the ResponseEntity with status 201 (Created) and with body the new language, or with status 400 (Bad Request) if the language has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/languages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Language> createLanguage(@RequestBody Language language) throws URISyntaxException {
        log.debug("REST request to save Language : {}", language);
        if (language.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("language", "idexists", "A new language cannot already have an ID")).body(null);
        }
        Language result = languageRepository.save(language);
        return ResponseEntity.created(new URI("/api/languages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("language", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /languages : Updates an existing language.
     *
     * @param language the language to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated language,
     * or with status 400 (Bad Request) if the language is not valid,
     * or with status 500 (Internal Server Error) if the language couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/languages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Language> updateLanguage(@RequestBody Language language) throws URISyntaxException {
        log.debug("REST request to update Language : {}", language);
        if (language.getId() == null) {
            return createLanguage(language);
        }
        Language result = languageRepository.save(language);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("language", language.getId().toString()))
            .body(result);
    }

    /**
     * GET  /languages : get all the languages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of languages in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/languages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Language>> getAllLanguages(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Languages");
        Page<Language> page = languageRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/languages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /languages/:id : get the "id" language.
     *
     * @param id the id of the language to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the language, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/languages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Language> getLanguage(@PathVariable Long id) {
        log.debug("REST request to get Language : {}", id);
        Language language = languageRepository.findOne(id);
        return Optional.ofNullable(language)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /languages/:id : delete the "id" language.
     *
     * @param id the id of the language to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/languages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        log.debug("REST request to delete Language : {}", id);
        languageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("language", id.toString())).build();
    }

}
