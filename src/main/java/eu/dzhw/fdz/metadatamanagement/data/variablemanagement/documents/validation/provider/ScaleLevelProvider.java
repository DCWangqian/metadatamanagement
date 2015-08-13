package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.provider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * This enumeration includes all accepted scale levels.
 * 
 * @author Daniel Katzberg
 *
 */
@Component
public class ScaleLevelProvider {

  public static final String ENGLISH_METRIC = "metric";
  public static final String ENGLISH_NOMINAL = "nominal";
  public static final String ENGLISH_ORDINAL = "ordinal";

  public static final String GERMAN_METRIC = "metrisch";
  public static final String GERMAN_NOMINAL = "nominal";
  public static final String GERMAN_ORDINAL = "ordinal";
  /**
   * This fields holds the accepted translations of all supported languages.
   */
  private HashMap<Locale, HashSet<String>> scaleLevelMap;

  /**
   * The basic constructor fills a map with valid language translations of the Metric, Nominal and
   * Ordinal scale level.
   */
  public ScaleLevelProvider() {
    this.scaleLevelMap = new HashMap<>();

    // put english
    HashSet<String> allTranslationsEnglish = new HashSet<>();
    allTranslationsEnglish.add(ENGLISH_METRIC);
    allTranslationsEnglish.add(ENGLISH_NOMINAL);
    allTranslationsEnglish.add(ENGLISH_ORDINAL);
    this.scaleLevelMap.put(Locale.ENGLISH, allTranslationsEnglish);

    // put german
    HashSet<String> allTranslationsGerman = new HashSet<>();
    allTranslationsGerman.add(GERMAN_METRIC);
    allTranslationsGerman.add(GERMAN_NOMINAL);
    allTranslationsGerman.add(GERMAN_ORDINAL);
    this.scaleLevelMap.put(Locale.GERMAN, allTranslationsGerman);
  }

  /**
   * @return Returns the translated field of metric depending at the actual locale.
   */
  public String getMetricByLocal() {
    if (LocaleContextHolder.getLocale().equals(Locale.GERMAN)) {
      return GERMAN_METRIC;
    } else {
      return ENGLISH_METRIC;
    }
  }

  /**
   * @return Returns the translated field of nominal depending at the actual locale.
   */
  public String getNominalByLocal() {
    if (LocaleContextHolder.getLocale().equals(Locale.GERMAN)) {
      return GERMAN_NOMINAL;
    } else {
      return ENGLISH_NOMINAL;
    }
  }

  /**
   * @return Returns the translated field of ordinal depending at the actual locale.
   */
  public String getOrdinalByLocal() {
    if (LocaleContextHolder.getLocale().equals(Locale.GERMAN)) {
      return GERMAN_ORDINAL;
    } else {
      return ENGLISH_ORDINAL;
    }
  }

  /* GETTER / SETTER */
  public HashMap<Locale, HashSet<String>> getScaleLevelMap() {
    return scaleLevelMap;
  }
}
