package com.voitenko.diploma.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.voitenko.diploma.domain.Code;
import com.voitenko.diploma.repository.CodeRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Code.
 */
@RestController
@RequestMapping("/api")
public class CodeResource {

    private final Logger log = LoggerFactory.getLogger(CodeResource.class);
        
    @Inject
    private CodeRepository codeRepository;
    
    /**
     * POST  /codes : Create a new code.
     *
     * @param code the code to create
     * @return the ResponseEntity with status 201 (Created) and with body the new code, or with status 400 (Bad Request) if the code has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/codes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Code> createCode(@Valid @RequestBody Code code) throws URISyntaxException {
        log.debug("REST request to save Code : {}", code);
        if (code.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("code", "idexists", "A new code cannot already have an ID")).body(null);
        }
        Code result = codeRepository.save(code);
        return ResponseEntity.created(new URI("/api/codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("code", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /codes : Updates an existing code.
     *
     * @param code the code to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated code,
     * or with status 400 (Bad Request) if the code is not valid,
     * or with status 500 (Internal Server Error) if the code couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/codes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Code> updateCode(@Valid @RequestBody Code code) throws URISyntaxException {
        log.debug("REST request to update Code : {}", code);
        if (code.getId() == null) {
            return createCode(code);
        }
        Code result = codeRepository.save(code);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("code", code.getId().toString()))
            .body(result);
    }

    /**
     * GET  /codes : get all the codes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of codes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/codes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Code>> getAllCodes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Codes");
        Page<Code> page = codeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/codes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /codes/:id : get the "id" code.
     *
     * @param id the id of the code to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the code, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/codes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Code> getCode(@PathVariable Long id) {
        log.debug("REST request to get Code : {}", id);
        Code code = codeRepository.findOne(id);
        return Optional.ofNullable(code)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /codes/:id : delete the "id" code.
     *
     * @param id the id of the code to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/codes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCode(@PathVariable Long id) {
        log.debug("REST request to delete Code : {}", id);
        codeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("code", id.toString())).build();
    }

}
