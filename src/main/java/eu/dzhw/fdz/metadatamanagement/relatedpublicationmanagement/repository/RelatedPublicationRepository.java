package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;

/**
 * Related Publication Repository.
 * 
 * @author Daniel Katzberg
 *
 */
@RepositoryRestResource(path = "/related-publications")
public interface RelatedPublicationRepository extends MongoRepository<RelatedPublication, String>, 
    QueryDslPredicateExecutor<RelatedPublication> {
  
  @RestResource(exported = false)
  Slice<RelatedPublication> findBy(Pageable pageable);
}
