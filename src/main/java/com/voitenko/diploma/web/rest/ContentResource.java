package com.voitenko.diploma.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.voitenko.diploma.domain.Content;
import com.voitenko.diploma.repository.ContentRepository;
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
 * REST controller for managing Content.
 */
@RestController
@RequestMapping("/api")
public class ContentResource {

    private final Logger log = LoggerFactory.getLogger(ContentResource.class);

    @Inject
    private ContentRepository contentRepository;

    /**
     * POST  /contents : Create a new content.
     *
     * @param content the content to create
     * @return the ResponseEntity with status 201 (Created) and with body the new content, or with status 400 (Bad Request) if the content has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Content> createContent(@Valid @RequestBody Content content) throws URISyntaxException {
        log.debug("REST request to save Content : {}", content);
        if (content.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("content", "idexists", "A new content cannot already have an ID")).body(null);
        }
        Content result = contentRepository.save(content);
        return ResponseEntity.created(new URI("/api/contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("content", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contents : Updates an existing content.
     *
     * @param content the content to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated content,
     * or with status 400 (Bad Request) if the content is not valid,
     * or with status 500 (Internal Server Error) if the content couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Content> updateContent(@Valid @RequestBody Content content) throws URISyntaxException {
        log.debug("REST request to update Content : {}", content);
        if (content.getId() == null) {
            return createContent(content);
        }
        Content result = contentRepository.save(content);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("content", content.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contents : get all the contents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contents in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/contents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Content>> getAllContents(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Contents");
        Page<Content> page = contentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contents/:id : get the "id" content.
     *
     * @param id the id of the content to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the content, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contents/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Content> getContent(@PathVariable Long id) {
        log.debug("REST request to get Content : {}", id);
        Content content = contentRepository.findOne(id);
        return Optional.ofNullable(content)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contents/:id : delete the "id" content.
     *
     * @param id the id of the content to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/contents/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        log.debug("REST request to delete Content : {}", id);
        contentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("content", id.toString())).build();
    }

    @RequestMapping(value = "contents/audio/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> downloadContentAudio(@PathVariable Long id) throws IOException {
        Properties properties = new Properties();
        String folder = "";
        String resourceName = "content.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = loader.getResourceAsStream(resourceName)) {
            properties.load(inputStream);
            folder = properties.getProperty("lecturesFolder");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Content content = contentRepository.findOne(id);
        InputStream in = new BufferedInputStream(new FileInputStream(folder + content.getLink()));

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
    }

}
