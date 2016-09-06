package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * The 'avriableTextOnly' Projection of a variable domain object.
 * 'variableTextOnly' means only some attributes will be
 * displayed.
 */
@Projection(name = "variableTextOnly", types = Variable.class)
public interface VariableSearchProjection {
  String getId();
  
  String getName();

  I18nString getLabel();
}
