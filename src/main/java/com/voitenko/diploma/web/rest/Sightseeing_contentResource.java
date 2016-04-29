package com.voitenko.diploma.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.voitenko.diploma.domain.Sightseeing_content;
import com.voitenko.diploma.repository.Sightseeing_contentRepository;
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
 * REST controller for managing Sightseeing_content.
 */
@RestController
@RequestMapping("/api")
public class Sightseeing_contentResource {

    private final Logger log = LoggerFactory.getLogger(Sightseeing_contentResource.class);
        
    @Inject
    private Sightseeing_contentRepository sightseeing_contentRepository;
    
    /**
     * POST  /sightseeing-contents : Create a new sightseeing_content.
     *
     * @param sightseeing_content the sightseeing_content to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sightseeing_content, or with status 400 (Bad Request) if the sightseeing_content has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sightseeing-contents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sightseeing_content> createSightseeing_content(@RequestBody Sightseeing_content sightseeing_content) throws URISyntaxException {
        log.debug("REST request to save Sightseeing_content : {}", sightseeing_content);
        if (sightseeing_content.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sightseeing_content", "idexists", "A new sightseeing_content cannot already have an ID")).body(null);
        }
        Sightseeing_content result = sightseeing_contentRepository.save(sightseeing_content);
        return ResponseEntity.created(new URI("/api/sightseeing-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sightseeing_content", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sightseeing-contents : Updates an existing sightseeing_content.
     *
     * @param sightseeing_content the sightseeing_content to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sightseeing_content,
     * or with status 400 (Bad Request) if the sightseeing_content is not valid,
     * or with status 500 (Internal Server Error) if the sightseeing_content couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sightseeing-contents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sightseeing_content> updateSightseeing_content(@RequestBody Sightseeing_content sightseeing_content) throws URISyntaxException {
        log.debug("REST request to update Sightseeing_content : {}", sightseeing_content);
        if (sightseeing_content.getId() == null) {
            return createSightseeing_content(sightseeing_content);
        }
        Sightseeing_content result = sightseeing_contentRepository.save(sightseeing_content);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sightseeing_content", sightseeing_content.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sightseeing-contents : get all the sightseeing_contents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sightseeing_contents in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/sightseeing-contents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Sightseeing_content>> getAllSightseeing_contents(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Sightseeing_contents");
        Page<Sightseeing_content> page = sightseeing_contentRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sightseeing-contents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sightseeing-contents/:id : get the "id" sightseeing_content.
     *
     * @param id the id of the sightseeing_content to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sightseeing_content, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sightseeing-contents/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sightseeing_content> getSightseeing_content(@PathVariable Long id) {
        log.debug("REST request to get Sightseeing_content : {}", id);
        Sightseeing_content sightseeing_content = sightseeing_contentRepository.findOne(id);
        return Optional.ofNullable(sightseeing_content)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sightseeing-contents/:id : delete the "id" sightseeing_content.
     *
     * @param id the id of the sightseeing_content to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sightseeing-contents/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSightseeing_content(@PathVariable Long id) {
        log.debug("REST request to delete Sightseeing_content : {}", id);
        sightseeing_contentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sightseeing_content", id.toString())).build();
    }

}
