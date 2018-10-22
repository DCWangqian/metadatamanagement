package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericDomainObjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;

/**
 * Service responsible for retrieving and initializing the data set history.
 * 
 * @author René Reitmann
 */
@Service
public class DataSetVersionsService
    extends GenericDomainObjectVersionsService<DataSet, DataSetRepository> {
  /**
   * Construct the service.
   */
  @Autowired
  public DataSetVersionsService(Javers javers, DataSetRepository dataSetRepository,
      MetadataManagementProperties metadataManagementProperties) {
    super(DataSet.class, javers, dataSetRepository, metadataManagementProperties);
  }

  /**
   * Init Javers with all current studies if there are no study commits in Javers yet.
   */
  @PostConstruct
  public void initJaversForDataSets() {
    super.initJaversWithCurrentVersions();
  }
}
