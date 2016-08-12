package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Question Types.
 */
public class QuestionTypes {
  public static final I18nString OPEN = new I18nString("offen", "open");
  public static final I18nString SINGLE_CHOICE = new I18nString("single choice", "single choice");
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<I18nString>(Arrays.asList(OPEN, SINGLE_CHOICE)));
}