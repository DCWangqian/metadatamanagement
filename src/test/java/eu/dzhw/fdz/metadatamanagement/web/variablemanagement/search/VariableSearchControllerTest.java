package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFilter;

/**
 * Test which checks if the {@link VariableSearchController} answers as expected
 * 
 * @author Amine Limouri
 * @author Daniel Katzberg
 */
public class VariableSearchControllerTest extends AbstractTest {

  @Autowired
  private VariableService variableService;

  @Autowired
  private DataTypesProvider dataTypesProvider;

  @Autowired
  private ScaleLevelProvider scaleLevelProvider;

  @Before
  public void before() {
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      LocaleContextHolder.setLocale(locale);
      for (int i = 1; i <= 9; i++) {
        VariableSurvey variableSurvey = new VariableSurveyBuilder()
            .withSurveyId("SearchUnitTest_Survey_ID").withTitle("SearchUnitTestTitle 0" + i)
            .withVariableAlias("SearchUnitTestVariableAlias 0" + i).build();

        VariableDocument variableDocument = new VariableDocumentBuilder()
            .withId("SearchUnitTest_ID0" + i).withName("SearchUnitTestName 0" + i)
            .withLabel("SearchUnitTestLabel 0" + i).withQuestion("SearchUnitTestQuestion 0" + i)
            .withDataType(this.dataTypesProvider.getNumericValueByLocale())
            .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
            .withVariableSurvey(variableSurvey).build();
        this.variableService.save(variableDocument);
      }
    }
  }

  @After
  public void after() {
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      LocaleContextHolder.setLocale(locale);
      // Delete
      for (int i = 1; i <= 9; i++) {
        this.variableService.delete("SearchUnitTest_ID0" + i);
      }
    }
  }

  @Test
  public void testGermanTemplate() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/search"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    VariableSearchPageResource resource =
        (VariableSearchPageResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??")))).andReturn();

    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "VariableSearchControllerTest.testGermanTemplate");

    assertThat(resource.getPage().getContent().size(), is(greaterThan(0)));
  }

  @Test
  public void testEnglishTemplate() throws Exception {
    // Arrange
    MvcResult mvcResult = this.mockMvc.perform(get("/en/variables/search"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();
    VariableSearchPageResource resource =
        (VariableSearchPageResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    mvcResult = this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Language"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??")))).andReturn();

    // Act
    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "VariableSearchControllerTest.testEnglishTemplate");

    // Assert
    assertThat(resource.getPage().getContent().size(), is(greaterThan(0)));
  }

  @Test
  public void testSearch() throws Exception {

    // Arrange
    MvcResult mvcResult =
        this.mockMvc.perform(get("/de/variables/search?query=SearchUnitTest_Survey_ID"))
            .andExpect(status().isOk()).andExpect(request().asyncStarted())
            .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act
    VariableSearchPageResource resource =
        (VariableSearchPageResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    VariableSearchFilter variableSearchFilter =
        (VariableSearchFilter) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("variableSearchFilter");

    // Assert
    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "variables/search");
    ModelAndViewAssert.assertModelAttributeAvailable((ModelAndView) mvcResult.getAsyncResult(),
        "resource");
    ModelAndViewAssert.assertAndReturnModelAttributeOfType(
        (ModelAndView) mvcResult.getAsyncResult(), "resource", VariableSearchPageResource.class);

    mvcResult =
        this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk()).andReturn();

    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "VariableSearchControllerTest.testSearch");

    assertThat(variableSearchFilter.getQuery(), is("SearchUnitTest_Survey_ID"));
    assertThat(variableSearchFilter.getScaleLevel(), is(nullValue()));
    assertThat(resource.getPage().getContent().size(), is(9));
  }

  @Test
  public void testSearchWithPage() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(get("/de/variables/search?query=SearchUnitTest_Survey_ID&page=1&size=3"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    // Act
    VariableSearchFilter variableSearchFilter =
        (VariableSearchFilter) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("variableSearchFilter");

    VariableSearchPageResource resource =
        (VariableSearchPageResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    // Assert
    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "variables/search");
    ModelAndViewAssert.assertModelAttributeAvailable((ModelAndView) mvcResult.getAsyncResult(),
        "resource");
    ModelAndViewAssert.assertAndReturnModelAttributeOfType(
        (ModelAndView) mvcResult.getAsyncResult(), "resource", VariableSearchPageResource.class);

    mvcResult =
        this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk()).andReturn();

    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "VariableSearchControllerTest.testSearchWithPage");

    assertThat(variableSearchFilter.getQuery(), is("SearchUnitTest_Survey_ID"));
    assertThat(variableSearchFilter.getScaleLevel(), is(nullValue()));
    assertThat(resource.getPage().getContent().size(), is(3));
    assertThat(resource.getPage().getId().getHref()
        .contains("/de/variables/search?query=SearchUnitTest_Survey_ID&page=1&size=3"), is(true));
    assertThat(resource.getPage().getPreviousLink().getHref()
        .contains("/de/variables/search?query=SearchUnitTest_Survey_ID&page=0&size=3"), is(true));
    assertThat(resource.getPage().getNextLink().getHref()
        .contains("/de/variables/search?query=SearchUnitTest_Survey_ID&page=2&size=3"), is(true));
  }

  @Test
  public void testSearchWithScaleLevel() throws Exception {

    // Arrange
    MvcResult mvcResult = this.mockMvc
        .perform(get("/de/variables/search?query=SearchUnitTestName&scaleLevel=metrisch"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();
    VariableSearchPageResource resource =
        (VariableSearchPageResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");
    VariableSearchFilter variableSearchFilter =
        (VariableSearchFilter) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("variableSearchFilter");

    // Act

    // Assert
    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "variables/search");

    mvcResult =
        this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk()).andReturn();

    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "VariableSearchControllerTest.testSearchWithScaleLevel");
    assertThat(variableSearchFilter.getQuery(), is("SearchUnitTestName"));
    assertThat(variableSearchFilter.getScaleLevel(), is("metrisch"));
    assertThat(resource.getPage().getContent().size(), greaterThan(0));
  }

  @Test
  public void testSearchWithScaleLevelWithNoResults() throws Exception {

    // Arrange
    MvcResult mvcResult =
        this.mockMvc.perform(get("/en/variables/search?query=&scaleLevel=ordinal"))
            .andExpect(status().isOk()).andExpect(request().asyncStarted())
            .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();


    // Act
    VariableSearchPageResource resource =
        (VariableSearchPageResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    VariableSearchFilter variableSearchFilter =
        (VariableSearchFilter) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("variableSearchFilter");

    // Assert
    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "variables/search");

    mvcResult =
        this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk()).andReturn();

    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "VariableSearchControllerTest.testSearchWithScaleLevelWithNoResults");
    assertThat(variableSearchFilter.getQuery(), is(nullValue()));
    assertThat(variableSearchFilter.getScaleLevel(), is("ordinal"));
    assertThat(resource.getPage().getContent().size(), is(0));
  }
}
