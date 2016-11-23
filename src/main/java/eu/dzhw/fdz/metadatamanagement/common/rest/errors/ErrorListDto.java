package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error DTO which is returned when a client sends a json which cannot be converted to a Java
 * Object.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
//TODO DKatzberg Comments
public class ErrorListDto {
  private List<ErrorDto> errorDtos = new ArrayList<>();
  
  public ErrorListDto(String errorMessage, String entity, 
      String invalidValue, String property) {
    errorDtos.add(new ErrorDto(entity, errorMessage, invalidValue, property));
  }
  
  public ErrorListDto(ErrorDto errorDto) {
    errorDtos.add(errorDto);
  }
  
  public ErrorListDto() { }
  
  /**
   * Add an Error Dto to the ErrorDtoList.
   * 
   * @param errorDto An Error Dto.
   * @return Returns the boolean value from the List.add Command.
   */
  public boolean add(ErrorDto errorDto) {
    return this.errorDtos.add(errorDto);
  }
  
  @JsonProperty("errors")
  public List<ErrorDto> getErrors() {
    return this.errorDtos;
  }
}
