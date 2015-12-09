package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.AbstractBasicTest;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.service.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the VariableResource REST controller.
 *
 * @author Daniel Katzberg
 *
 * @see VariableResource
 */
public class VariableResourceTest extends AbstractBasicTest {

  private static final String DEFAULT_ID = "VARID";
  private static final String DEFAULT_NAME = "AAAAA";
  private static final String UPDATED_NAME = "BBBBB";
  private static final DataType DEFAULT_DATA_TYPE = DataType.string;
  private static final DataType UPDATED_DATA_TYPE = DataType.numeric;
  private static final ScaleLevel DEFAULT_SCALE_LEVEL = ScaleLevel.ordinal;
  private static final ScaleLevel UPDATED_SCALE_LEVEL = ScaleLevel.nominal;
  private static final String DEFAULT_LABEL = "AAAAA";
  private static final String UPDATED_LABEL = "BBBBB";

  @Inject
  private VariableRepository variableRepository;

  @Inject
  private VariableService variableService;

  @Inject
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Inject
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Inject
  private ExceptionTranslator exceptionTranslator;

  private MockMvc restVariableMockMvc;

  private Variable variable;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    VariableResource variableResource = new VariableResource();
    ReflectionTestUtils.setField(variableResource, "variableService", variableService);
    this.restVariableMockMvc = MockMvcBuilders.standaloneSetup(variableResource)
      .setCustomArgumentResolvers(pageableArgumentResolver)
      .setMessageConverters(jacksonMessageConverter)
      .setControllerAdvice(exceptionTranslator)
      .build();
  }

  @Before
  public void initTest() {
    variableRepository.deleteAll();
    variable = new Variable();
    variable.setId(DEFAULT_ID);
    variable.setName(DEFAULT_NAME);
    variable.setDataType(DEFAULT_DATA_TYPE);
    variable.setScaleLevel(DEFAULT_SCALE_LEVEL);
    variable.setLabel(DEFAULT_LABEL);
  }

  @Test
  public void createVariable() throws Exception {
    int databaseSizeBeforeCreate = variableRepository.findAll()
      .size();

    // Create the Variable

    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    // Validate the Variable in the database
    List<Variable> variables = variableRepository.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeCreate + 1);
    Variable testVariable = variables.get(variables.size() - 1);
    assertThat(testVariable.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testVariable.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
    assertThat(testVariable.getScaleLevel()).isEqualTo(DEFAULT_SCALE_LEVEL);
    assertThat(testVariable.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  public void createVariableWithError() throws Exception {
    // Arrange

    // Act
    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(this.variable)))
      .andExpect(status().isCreated());

    // Assert
    // Bad Request -> ID exist
    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(this.variable)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = variableRepository.findAll()
      .size();
    // set the field null
    variable.setName(null);

    // Create the Variable, which fails.

    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());

    List<Variable> variables = variableRepository.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void checkDataTypeIsRequired() throws Exception {
    int databaseSizeBeforeTest = variableRepository.findAll()
      .size();
    // set the field null
    variable.setDataType(null);

    // Create the Variable, which fails.

    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());

    List<Variable> variables = variableRepository.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void checkScaleLevelIsRequired() throws Exception {
    int databaseSizeBeforeTest = variableRepository.findAll()
      .size();
    // set the field null
    variable.setScaleLevel(null);

    // Create the Variable, which fails.

    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());

    List<Variable> variables = variableRepository.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = variableRepository.findAll()
      .size();
    // set the field null
    variable.setLabel(null);

    // Create the Variable, which fails.

    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());

    List<Variable> variables = variableRepository.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void getAllVariables() throws Exception {
    // Initialize the database
    variableRepository.save(variable);

    // Get all the variables
    restVariableMockMvc.perform(get("/api/variables"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].id").value(hasItem(variable.getId())))
      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
      .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
      .andExpect(jsonPath("$.[*].scaleLevel").value(hasItem(DEFAULT_SCALE_LEVEL.toString())))
      .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  public void getVariable() throws Exception {
    // Initialize the database
    variableRepository.save(variable);

    // Get the variable
    restVariableMockMvc.perform(get("/api/variables/{id}", variable.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(variable.getId()))
      .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
      .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
      .andExpect(jsonPath("$.scaleLevel").value(DEFAULT_SCALE_LEVEL.toString()))
      .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  public void getNonExistingVariable() throws Exception {
    // Get the variable
    restVariableMockMvc.perform(get("/api/variables/{id}", Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  public void updateVariable() throws Exception {
    // Initialize the database
    variableRepository.save(variable);

    int databaseSizeBeforeUpdate = variableRepository.findAll()
      .size();

    // Update the variable
    variable.setName(UPDATED_NAME);
    variable.setDataType(UPDATED_DATA_TYPE);
    variable.setScaleLevel(UPDATED_SCALE_LEVEL);
    variable.setLabel(UPDATED_LABEL);

    restVariableMockMvc.perform(put("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isOk());

    // Validate the Variable in the database
    List<Variable> variables = variableRepository.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeUpdate);
    Variable testVariable = variables.get(variables.size() - 1);
    assertThat(testVariable.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testVariable.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
    assertThat(testVariable.getScaleLevel()).isEqualTo(UPDATED_SCALE_LEVEL);
    assertThat(testVariable.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  public void updateVariableWithError() throws Exception {

    // Arrange
    variableRepository.save(variable);
    variable.setName(UPDATED_NAME);
    variable.setDataType(UPDATED_DATA_TYPE);
    variable.setScaleLevel(UPDATED_SCALE_LEVEL);
    variable.setLabel(UPDATED_LABEL);
    variable.setId("WrongID");

    // Act

    // Assert
    restVariableMockMvc.perform(put("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void deleteVariable() throws Exception {
    // Initialize the database
    variableRepository.save(variable);

    int databaseSizeBeforeDelete = variableRepository.findAll()
      .size();

    // Get the variable
    restVariableMockMvc.perform(
        delete("/api/variables/{id}", variable.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk());

    // Validate the database is empty
    List<Variable> variables = variableRepository.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeDelete - 1);
  }
}
