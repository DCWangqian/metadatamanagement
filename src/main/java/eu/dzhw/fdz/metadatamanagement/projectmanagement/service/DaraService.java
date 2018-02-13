package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Charsets;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Access component for getting health information or registration or updates for dara and the doi.
 *
 * @author Daniel Katzberg
 */
@Service
@Slf4j
public class DaraService {

  public static final String IS_ALiVE_ENDPOINT = "api/isAlive";
  public static final String REGISTRATION_ENDPOINT = "study/importXML";

  @Autowired
  private MetadataManagementProperties metadataManagementProperties;

  @Autowired
  private DataAcquisitionProjectRepository projectRepository;

  @Autowired
  private StudyRepository studyRepository;
  
  @Autowired
  private SurveyRepository surveyRepository;
  
  @Autowired 
  private DataSetRepository dataSetRepository;
  
  @Autowired
  private VariableRepository variableRepository;
  
  @Autowired
  private RelatedPublicationRepository relatedPublicationRepository;

  @Value(value = "classpath:templates/dara/register.xml.tmpl")
  private Resource registerXml;

  @Autowired
  private Environment env;

  private RestTemplate restTemplate;

  //Key for Register XML Template
  private static final String KEY_REGISTER_XML_TMPL = "register.xml.tmpl";

  //Resource Type
  private static final String RESOURCE_TYPE_DATASET = "Dataset";

  //Availability Controlled
  private static final String AVAILABILITY_CONTROLLED_DELIVERY = "Delivery";

  /**
   * Constructor for Dara Services. Set the Rest Template.
   */
  public DaraService() {
    this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    this.restTemplate.getMessageConverters()
      .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
  }

  /**
   * Check the dara health endpoint.
   * @return Returns the status of the dara server.
   */
  public boolean isDaraHealthy() {

    final String daraHealthEndpoint = this.getApiEndpoint() + IS_ALiVE_ENDPOINT;

    ResponseEntity<String> result =
        this.restTemplate.getForEntity(daraHealthEndpoint, String.class);

    return result.getStatusCode().equals(HttpStatus.OK);
  }

  /**
   * Registers or updates a dataset with a given doi to dara.
   * @param project The Project.
   * @return The HttpStatus from Dara
   *        Returns a false, if something gone wrong.
   * @throws IOException the io exception for non readable xml file.
   * @throws TemplateException Exception for filling the template.
   */
  public HttpStatus registerOrUpdateProjectToDara(DataAcquisitionProject project)
      throws IOException, TemplateException {

    //Read register xml
    String registerXmlStr = IOUtils.toString(this.registerXml.getInputStream(), Charsets.UTF_8);

    //Fill template
    String filledTemplate = this.fillTemplate(registerXmlStr,
            this.getTemplateConfiguration(),
            this.getDataForTemplate(project, AVAILABILITY_CONTROLLED_DELIVERY),
            KEY_REGISTER_XML_TMPL);

    //Send Rest Call for Registration
    HttpStatus httpStatusFromDara =
        this.postToDaraImportXml(filledTemplate);
    return httpStatusFromDara;
  }
  
  /**
   * Registers or updates a dataset with a given doi to dara.
   * @param projectId The id of the Project.
   * @return The HttpStatus from Dara
   *        Returns a false, if something gone wrong.
   * @throws IOException the io exception for non readable xml file.
   * @throws TemplateException Exception for filling the template.
   */
  public HttpStatus registerOrUpdateProjectToDara(String projectId)
      throws IOException, TemplateException {
    
    //Load Project
    DataAcquisitionProject project = this.projectRepository.findOne(projectId);
    return this.registerOrUpdateProjectToDara(project);
  }

  /**
   * This is the kernel method for registration, update and unregister of a doi element.
   * @param filledTemplate The filled and used template.
   * @return the HttpStatus from Dara.
   */
  private HttpStatus postToDaraImportXml(String filledTemplate) {
    //TODO DKatzberg
    //Was a paramater before: hasBeenReleasedBefore
    
    log.debug("The filled Template for dara:");
    log.debug(filledTemplate);

    //Load Dara Information
    final String daraEndpoint =
        this.metadataManagementProperties.getDara().getEndpoint() + REGISTRATION_ENDPOINT;
    final String daraUsername = this.metadataManagementProperties.getDara().getUsername();
    final String daraPassword = this.metadataManagementProperties.getDara().getPassword();

    //Build Header
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/xml;charset=UTF-8");
    String auth = daraUsername + ":" + daraPassword;
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName(Charsets.UTF_8.name())));
    headers.add("Authorization", "Basic " + new String(encodedAuth, Charsets.UTF_8));

    //Build
    //TODO DKatzberg Change? Delete? WIP
    /*UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(daraEndpoint)
        .queryParam("registration", Boolean.valueOf(!hasBeenReleasedBefore).toString());*/
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(daraEndpoint)
        .queryParam("registration", "true");

    //Build Request
    HttpEntity<String> request = new HttpEntity<>(filledTemplate, headers);

    //Send Post
    //Info: result.getBody() has the registered DOI
    try {
      ResponseEntity<String> result =
            this.restTemplate.postForEntity(builder.build().toUri(), request, String.class);
      log.debug("Response code from Dara: {}", result.getStatusCode());
      log.debug("Response body from Dara: {}", result.getBody());
      return result.getStatusCode();
    } catch (HttpClientErrorException httpClientError) {
      log.debug("HTTP Error durind Dara call", httpClientError);
      log.debug("Dara Response Body:\n" + httpClientError.getResponseBodyAsString());
      //Has been released is false? Something went wrong at the local save?
      //Catch the second try for registring
      //Idempotent Method!
      if (httpClientError.getStatusCode().is4xxClientError()
          && httpClientError.getResponseBodyAsString()
            .equals("A resource with the given doiProposal exists in the system.")) {
        return HttpStatus.CREATED;
      } else {
        throw httpClientError;
      }
    }
  }

  /**
   * Load all needed Data for the XML Templates. The data is callable in freemarker by:
   *    study
   *    releaseDate
   *    availabilityControlled
   *    resourceType
   * @param project The project to find the study.
   * @param availabilityControlled The availability of the data.
   * @return Returns a Map of names and the depending objects.
   *     If the key is 'study' so the study object is the value.
   *     Study is the name for the object use in freemarker.
   */
  private Map<String, Object> getDataForTemplate(DataAcquisitionProject project,
      String availabilityControlled) {

    Map<String, Object> dataForTemplate = new HashMap<>();
    String projectId = project.getId();
    
    //Get Project Information
    dataForTemplate.put("dataAcquisitionProject", project);

    //Get Study Information
    Study study = this.studyRepository.findOneByDataAcquisitionProjectId(projectId);
    if (project.getRelease() != null) {
      study = this.updateDoi(study, project.getRelease().getVersion());
    } 
    dataForTemplate.put("study", study);
    
    //Get Surveys Information
    List<Survey> surveys = this.surveyRepository
        .findByDataAcquisitionProjectIdOrderByNumber(projectId);
    dataForTemplate.put("surveys", surveys);
    
    //Get Datasets Information
    List<DataSet> dataSets = this.dataSetRepository.findByDataAcquisitionProjectId(projectId);
    dataForTemplate.put("dataSets", dataSets);
    HashMap<String, Long> dataSetNumberOfVariablesMap = new HashMap<>();
    
    for (DataSet dataSet : dataSets) {
      long numberVariables = this.variableRepository.countByDataSetId(dataSet.getId());
      dataSetNumberOfVariablesMap.put(dataSet.getId(), numberVariables);
    }
    dataForTemplate.put("numberOfVariablesMap", dataSetNumberOfVariablesMap);
    
    //Get Related Publications
    List<RelatedPublication> relatedPublications = 
        this.relatedPublicationRepository.findByStudyIdsContaining(study.getId());
    dataForTemplate.put("relatedPublications", relatedPublications);
        
    //Add Date
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    dataForTemplate.put("releaseDate", formatter.format(LocalDate.now()));

    //Add Availability Controlled
    dataForTemplate.put("availabilityControlled", availabilityControlled);

    //Add Resource Type
    dataForTemplate.put("resourceType", RESOURCE_TYPE_DATASET);

    dataForTemplate.put("isDaraTest", !env.acceptsProfiles(Constants.SPRING_PROFILE_PROD));

    return dataForTemplate;
  }

  /**
   * @return a configratution object for the registration.
   */
  private Configuration getTemplateConfiguration() {
    // Configuration, based on Freemarker Version 2.3.23
    Configuration templateConfiguration = new Configuration(Configuration.VERSION_2_3_23);
    templateConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.toString());
    templateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    templateConfiguration.setNumberFormat("0.######");

    return templateConfiguration;
  }

  /**
   * This method fills the xml templates.
   *
   * @param templateContent The content of a xml template.
   * @param templateConfiguration The configuration for freemarker.
   * @param dataForTemplateThe data for a xml template.
   * @param fileName filename of the script which will be filled in this method.
   * @return The filled xml templates as byte array.
   * @throws IOException Handles IO Exception.
   * @throws TemplateException Handles template Exceptions.
   */
  private String fillTemplate(String templateContent,
      Configuration templateConfiguration, Map<String, Object> dataForTemplate, String fileName)
          throws IOException, TemplateException {
    // Read Template and escape elements
    Template texTemplate = new Template(fileName, templateContent, templateConfiguration);
    try (Writer stringWriter = new StringWriter()) {
      texTemplate.process(dataForTemplate, stringWriter);

      stringWriter.flush();
      return stringWriter.toString();
    }
  }
  
  /**
   * Updated the locally. Important: This method does not a save operation!!
   * @param study The actual Study Representation
   * @param newVersion the new version for the doi link
   * @return the updated study object (just locally!)
   */
  public Study updateDoi(Study study, String newVersion) {
    
    int lastIndexBeforeDoi = study.getDoi().lastIndexOf(":") + 1;
    String newDoi = study.getDoi().substring(0, lastIndexBeforeDoi) + newVersion;
    study.setDoi(newDoi);
    
    return study;
  }

  /**
   * Returns dara api endpont.
   * @return the api endpoint given by the configuration.
   */
  public String getApiEndpoint() {
    return this.metadataManagementProperties.getDara().getEndpoint();
  }

  public RestTemplate getRestTemplate() {
    return this.restTemplate;
  }
}
