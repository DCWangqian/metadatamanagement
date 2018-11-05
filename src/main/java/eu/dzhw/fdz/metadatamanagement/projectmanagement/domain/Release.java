package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The release object contains the version and a timestamp of the current release.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class Release {

  /**
   * A valid semver version (major.minor.patch).
   * 
   * Must not be empty and must not contain more than 32 characters. A version of a
   * {@link DataAcquisitionProject} must not be decreased.
   */
  @NotEmpty(message = "data-acquisition-project-management."
      + "error.release.version.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "data-acquisition-project-management.error.release.version.size")
  @Pattern(regexp = Patterns.SEMVER,
      message = "data-acquisition-project-management.error.release.version.pattern")
  private String version;

  /**
   * The timestamp (in UTC) indicates when a publisher has released the
   * {@link DataAcquisitionProject}.
   * 
   * Must not be empty.
   */
  @NotNull(message = "data-acquisition-project-management.error.release.date.not-null")
  private LocalDateTime date;
}
