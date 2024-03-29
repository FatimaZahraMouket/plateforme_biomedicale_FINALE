package com.appbiomedicale.web.rest;

import com.appbiomedicale.domain.Mesure;
import com.appbiomedicale.repository.MesureRepository;
import com.appbiomedicale.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.appbiomedicale.domain.Mesure}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MesureResource {

    private final Logger log = LoggerFactory.getLogger(MesureResource.class);

    private static final String ENTITY_NAME = "mesure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MesureRepository mesureRepository;

    public MesureResource(MesureRepository mesureRepository) {
        this.mesureRepository = mesureRepository;
    }

    /**
     * {@code POST  /mesures} : Create a new mesure.
     *
     * @param mesure the mesure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mesure, or with status {@code 400 (Bad Request)} if the mesure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mesures")
    public ResponseEntity<Mesure> createMesure(@RequestBody Mesure mesure) throws URISyntaxException {
        log.debug("REST request to save Mesure : {}", mesure);
        if (mesure.getId() != null) {
            throw new BadRequestAlertException("A new mesure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mesure result = mesureRepository.save(mesure);
        return ResponseEntity
            .created(new URI("/api/mesures/" + result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /mesures/:id} : Updates an existing mesure.
     *
     * @param id the id of the mesure to save.
     * @param mesure the mesure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mesure,
     * or with status {@code 400 (Bad Request)} if the mesure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mesure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mesures/{id}")
    public ResponseEntity<Mesure> updateMesure(@PathVariable(value = "id", required = false) final Long id, @RequestBody Mesure mesure)
        throws URISyntaxException {
        log.debug("REST request to update Mesure : {}, {}", id, mesure);
        if (mesure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mesure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mesureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mesure result = mesureRepository.save(mesure);
        return ResponseEntity
            .ok()
            .body(result);
    }

    /**
     * {@code PATCH  /mesures/:id} : Partial updates given fields of an existing mesure, field will ignore if it is null
     *
     * @param id the id of the mesure to save.
     * @param mesure the mesure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mesure,
     * or with status {@code 400 (Bad Request)} if the mesure is not valid,
     * or with status {@code 404 (Not Found)} if the mesure is not found,
     * or with status {@code 500 (Internal Server Error)} if the mesure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mesures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Mesure> partialUpdateMesure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Mesure mesure
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mesure partially : {}, {}", id, mesure);
        if (mesure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mesure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mesureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mesure> result = mesureRepository
            .findById(mesure.getId())
            .map(existingMesure -> {
                if (mesure.getType() != null) {
                    existingMesure.setType(mesure.getType());
                }
                if (mesure.getValeur() != null) {
                    existingMesure.setValeur(mesure.getValeur());
                }
                if (mesure.getDate() != null) {
                    existingMesure.setDate(mesure.getDate());
                }

                return existingMesure;
            })
            .map(mesureRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mesure.getId().toString())
        );
    }

    /**
     * {@code GET  /mesures} : get all the mesures.
     *
     //* @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mesures in body.
     */
    @GetMapping("/mesures")
    public List<Mesure> getAllMesures() {
        log.debug("REST request to get all Mesures");
        return mesureRepository.findAll();
    }

    /**
     * {@code GET  /mesures/:id} : get the "id" mesure.
     *
     * @param id the id of the mesure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mesure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mesures/{id}")
    public ResponseEntity<Mesure> getMesure(@PathVariable Long id) {
        log.debug("REST request to get Mesure : {}", id);
        Optional<Mesure> mesure = mesureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mesure);
    }

    /**
     * {@code DELETE  /mesures/:id} : delete the "id" mesure.
     *
     * @param id the id of the mesure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mesures/{id}")
    public ResponseEntity<Void> deleteMesure(@PathVariable Long id) {
        log.debug("REST request to delete Mesure : {}", id);
        mesureRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
