package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.repository.search.SurveySearchRepository;
import eu.dzhw.fdz.metadatamanagement.web.rest.dto.SurveyDto;
import eu.dzhw.fdz.metadatamanagement.web.rest.mapper.SurveyMapper;
import eu.dzhw.fdz.metadatamanagement.web.rest.util.HeaderUtil;
import eu.dzhw.fdz.metadatamanagement.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Survey.
 */
@RestController
@RequestMapping("/api")
public class SurveyResource {

  private final Logger log = LoggerFactory.getLogger(SurveyResource.class);

  @Inject
  private SurveyRepository surveyRepository;

  @Inject
  private SurveyMapper surveyMapper;

  @Inject
  private SurveySearchRepository surveySearchRepository;

  /**
   * POST /surveys -> Create a new survey.
   */
  @RequestMapping(value = "/surveys", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<SurveyDto> createSurvey(@Valid @RequestBody SurveyDto surveyDto)
      throws URISyntaxException {
    log.debug("REST request to save Survey : {}", surveyDto);
    if (surveyDto.getId() != null) {
      return ResponseEntity.badRequest().header("Failure", "A new survey cannot already have an ID")
          .body(null);
    }
    Survey survey = surveyMapper.surveyDtoToSurvey(surveyDto);
    Survey result = surveyRepository.save(survey);
    surveySearchRepository.save(result);
    return ResponseEntity.created(new URI("/api/surveys/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert("survey", result.getId().toString()))
        .body(surveyMapper.surveyToSurveyDto(result));
  }

  /**
   * PUT /surveys -> Updates an existing survey.
   */
  @RequestMapping(value = "/surveys", method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<SurveyDto> updateSurvey(@Valid @RequestBody SurveyDto surveyDto)
      throws URISyntaxException {
    log.debug("REST request to update Survey : {}", surveyDto);
    if (surveyDto.getId() == null) {
      return createSurvey(surveyDto);
    }
    Survey survey = surveyMapper.surveyDtoToSurvey(surveyDto);
    Survey result = surveyRepository.save(survey);
    surveySearchRepository.save(survey);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert("survey", surveyDto.getId().toString()))
        .body(surveyMapper.surveyToSurveyDto(result));
  }

  /**
   * GET /surveys -> get all the surveys.
   */
  @RequestMapping(value = "/surveys", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Transactional(readOnly = true)
  public ResponseEntity<List<SurveyDto>> getAllSurveys(Pageable pageable)
      throws URISyntaxException {
    Page<Survey> page = surveyRepository.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/surveys");
    return new ResponseEntity<>(page.getContent().stream().map(surveyMapper::surveyToSurveyDto)
        .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
  }

  /**
   * GET /surveys/:id -> get the "id" survey.
   */
  @RequestMapping(value = "/surveys/{id}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<SurveyDto> getSurvey(@PathVariable Long id) {
    log.debug("REST request to get Survey : {}", id);
    return Optional.ofNullable(surveyRepository.findOne(id)).map(surveyMapper::surveyToSurveyDto)
        .map(surveyDTO -> new ResponseEntity<>(surveyDTO, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * DELETE /surveys/:id -> delete the "id" survey.
   */
  @RequestMapping(value = "/surveys/{id}", method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
    log.debug("REST request to delete Survey : {}", id);
    surveyRepository.delete(id);
    surveySearchRepository.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert("survey", id.toString())).build();
  }

  /**
   * SEARCH /_search/surveys/:query -> search for the survey corresponding to the query.
   */
  @RequestMapping(value = "/_search/surveys/{query}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<SurveyDto> searchSurveys(@PathVariable String query) {
    return StreamSupport
        .stream(surveySearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .map(surveyMapper::surveyToSurveyDto).collect(Collectors.toList());
  }
}
