package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;


/**
 * DataSetTypes.
 *
 */
public class DataSetTypes {

  public static final I18nString PERSONAL_RECORD = 
      new I18nString("Personendatensatz", "Personal Record");
  public static final I18nString EPISODE_RECORD = 
      new I18nString("Episodendatensatz", "Episode Record");
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(
          new HashSet<I18nString>(Arrays.asList(PERSONAL_RECORD, EPISODE_RECORD)));
}
