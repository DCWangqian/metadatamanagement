package eu.dzhw.fdz.metadatamanagement.data.common.documents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a field representation of nested and not nested fields within filters and aggregations.
 * 
 * @author Daniel Katzberg
 *
 */
public class Field implements Cloneable {

  /*
   * A slf4j logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(Field.class);

  /**
   * The document Path. This path is used in elastic search filter and aggregations
   */
  private String path;

  /**
   * A nested field to support recursively any depth for nested.
   */
  private Field nestedField;

  /*
   * Boolean variable for the use of a term filer.
   */
  private boolean termFilter;

  /*
   * Boolean variable for the use of a range filer.
   */
  private boolean rangeFilter;

  /**
   * Boolean variable for the use of a aggregation.
   */
  private boolean aggregation;

  /**
   * This is the constructor for a not nested field. It has only the field path.
   * 
   * @param path the name of a filter / aggregation field
   */
  public Field(String path) {
    this(path, null);
  }

  /**
   * This is the constructor for a nested field. It has a field path and a nested field.
   * 
   * @param path the name of a filter / aggregation field (basic path)
   * @param nestedField the nested field of a filter / aggregation.
   */
  public Field(String path, Field nestedField) {
    this.path = path;
    this.nestedField = nestedField;
    this.setAllFilterToFalse();
    this.aggregation = false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#clone()
   */
  @Override
  public Field clone() {
    try {
      Field clone = (Field) super.clone();
      clone.path = this.path;
      if (this.isNested()) {
        clone.nestedField = this.nestedField.clone();
      }
      return clone;
    } catch (CloneNotSupportedException e) {
      LOG.error("Cannot clone Field (" + this.path + ")", e);
      throw new RuntimeException(e);
    }
  }

  /**
   * @param nestedField A nested field.
   * @return Fluent Filter for easy readable extensions of the given field.
   */
  public Field withNestedField(Field nestedField) {
    this.nestedField = nestedField;
    return this;
  }

  /**
   * All filter types are exclusive. If the term filter is used, all other filters will be set to
   * false.
   * 
   * @return Fluent Filter which uses a range filter
   */
  public Field withRangeFilter() {
    // only one filter could be true
    this.setAllFilterToFalse();
    this.rangeFilter = true;
    return this;
  }

  /**
   * All filter types are exclusive. If the term filter is used, all other filters will be set to
   * false.
   * 
   * @return Fluent Filter which uses a term filter
   */
  public Field withTermFilter() {
    // only one filter could be true
    this.setAllFilterToFalse();
    this.termFilter = true;
    return this;
  }

  /**
   * @return This Field use a Aggregation.
   */
  public Field withAggregation() {
    this.aggregation = true;
    return this;
  }

  /**
   * Sets all possible boolean fields to false.
   */
  private void setAllFilterToFalse() {
    this.termFilter = false;
    this.rangeFilter = false;
  }

  /**
   * Return the correct and complete nested Path.
   * 
   * @return the complete (nested) path
   */
  public String getNestedPath() {
    if (this.isNested()) {
      return this.getNestedField().getNestedPath();
    } else {
      return this.getPath();
    }
  }

  /**
   * @return Returns true if there is a nested Field in this field.
   */
  public boolean isNested() {
    return this.nestedField != null;
  }

  /**
   * @return This isAggregation method is a recursive return value of the deepest layer (the
   *         specific field). A specific field overwrite the value of the generic layer
   */
  public boolean isAggregation() {
    if (this.isNested()) {
      return this.getNestedField().isAggregation();
    } else {
      return this.aggregation;
    }
  }

  /* GETTER / SETTER */
  public String getPath() {
    return path;
  }

  public Field getNestedField() {
    return nestedField;
  }

  public boolean isTermFilter() {
    return termFilter;
  }

  public boolean isDateRangeFilter() {
    return rangeFilter;
  }
}
