package com.voitenko.diploma.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.voitenko.diploma.domain.Sightseeing;
import com.voitenko.diploma.repository.SightseeingRepository;
import com.voitenko.diploma.web.rest.util.HeaderUtil;
import com.voitenko.diploma.web.rest.util.PaginationUtil;
import org.apache.commons.io.IOUtils;
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
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * REST controller for managing Sightseeing.
 */
@RestController
@RequestMapping("/api")
public class SightseeingResource {

    private final Logger log = LoggerFactory.getLogger(SightseeingResource.class);

    @Inject
    private SightseeingRepository sightseeingRepository;

    /**
     * POST  /sightseeings : Create a new sightseeing.
     *
     * @param sightseeing the sightseeing to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sightseeing, or with status 400 (Bad Request) if the sightseeing has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sightseeings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sightseeing> createSightseeing(@Valid @RequestBody Sightseeing sightseeing) throws URISyntaxException {
        log.debug("REST request to save Sightseeing : {}", sightseeing);
        if (sightseeing.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sightseeing", "idexists", "A new sightseeing cannot already have an ID")).body(null);
        }
        Sightseeing result = sightseeingRepository.save(sightseeing);
        return ResponseEntity.created(new URI("/api/sightseeings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sightseeing", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sightseeings : Updates an existing sightseeing.
     *
     * @param sightseeing the sightseeing to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sightseeing,
     * or with status 400 (Bad Request) if the sightseeing is not valid,
     * or with status 500 (Internal Server Error) if the sightseeing couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sightseeings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sightseeing> updateSightseeing(@Valid @RequestBody Sightseeing sightseeing) throws URISyntaxException {
        log.debug("REST request to update Sightseeing : {}", sightseeing);
        if (sightseeing.getId() == null) {
            return createSightseeing(sightseeing);
        }
        Sightseeing result = sightseeingRepository.save(sightseeing);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sightseeing", sightseeing.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sightseeings : get all the sightseeings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sightseeings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/sightseeings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Sightseeing>> getAllSightseeings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Sightseeings");
        Page<Sightseeing> page = sightseeingRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sightseeings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "sightseeings/image/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> downloadUserAvatarImage(@PathVariable Long id) throws IOException {
        Properties properties = new Properties();
        String folder = "";
        String resourceName = "content.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = loader.getResourceAsStream(resourceName)) {
            properties.load(inputStream);
            folder = properties.getProperty("folder");

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        Sightseeing sightseeing = sightseeingRepository.findOne(id);
        InputStream in = new BufferedInputStream(new FileInputStream(folder  + sightseeing.getPhoto()));

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
    }

    /**
     * GET  /sightseeings/:id : get the "id" sightseeing.
     *
     * @param id the id of the sightseeing to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sightseeing, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sightseeings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sightseeing> getSightseeing(@PathVariable Long id) {
        log.debug("REST request to get Sightseeing : {}", id);
        Sightseeing sightseeing = sightseeingRepository.findOne(id);
        return Optional.ofNullable(sightseeing)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sightseeings/:id : delete the "id" sightseeing.
     *
     * @param id the id of the sightseeing to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sightseeings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSightseeing(@PathVariable Long id) {
        log.debug("REST request to delete Sightseeing : {}", id);
        sightseeingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sightseeing", id.toString())).build();
    }

}
