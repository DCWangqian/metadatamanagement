package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.FacetedPage;

import eu.dzhw.fdz.metadatamanagement.config.elasticsearch.JacksonDocumentMapper;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;

/**
 * The aggregation result mapper is a sub class from the {@link DefaultResultMapper}. It extends the
 * MapResults method for saving the aggregations. It uses the JacksonDocumentMapper for mapping e.g.
 * LocalDate Object from Strings.
 * 
 * @author Daniel Katzberg
 *
 */
public class AggregationResultMapper extends DefaultResultMapper {



  /**
   * The default Constructor uses the JacksonDocumentMapper for the depending super call.
   */
  public AggregationResultMapper(JacksonDocumentMapper jacksonDocumentMapper) {
    super(jacksonDocumentMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.elasticsearch.core.DefaultResultMapper#mapResults(org.elasticsearch.
   * action.search.SearchResponse, java.lang.Class, org.springframework.data.domain.Pageable)
   */
  @Override
  public <T> FacetedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
    FacetedPage<T> facetedPage = super.mapResults(response, clazz, pageable);

    // extract buckets
    Map<DocumentField, Set<Bucket>> map = new HashMap<>();

    extractStringTermAggregations(map, response.getAggregations());

    // response.getHits().forEach(hit -> {
    // Set<Entry<String,HighlightField>> fieldSet = hit.getHighlightFields().entrySet();
    // fieldSet.forEach(entry -> {
    // String highlightedFieldName = entry.getKey();
    // HighlightField highlightedField = entry.getValue();
    // highlightedField.getFragments().
    // });
    // });

    return new PageWithBucketsAndHighlightedFields<T>(facetedPage, pageable, map);
  }

  /**
   * Recursively search for {@link StringTerms} aggregations and add their buckets to the given map.
   * 
   * @param map The resulting map which holds all buckets for the aggregations.
   * @param aggregations The aggregations of the response or nested aggregations.
   */
  private void extractStringTermAggregations(Map<DocumentField, Set<Bucket>> map,
      Aggregations aggregations) {
    for (Entry<String, Aggregation> entry : aggregations.asMap().entrySet()) {

      // STRINGTERM
      if (entry.getValue().getClass().isAssignableFrom(StringTerms.class)) {
        DocumentField field = new DocumentField(entry.getKey());
        map.put(field, getStringTermBuckets(field, (StringTerms) entry.getValue()));

        // INTERNAL NESTED
      } else if (entry.getValue().getClass().isAssignableFrom(InternalNested.class)) {
        InternalNested nestedAggregation = (InternalNested) entry.getValue();
        extractStringTermAggregations(map, nestedAggregation.getAggregations());

        // OTHER TERMS ARE NOT SUPPORTED YET
      } else {
        throw new IllegalStateException("Not yet implemented");
      }
    }
  }

  /**
   * Create buckets for the given field and {@link StringTerms} aggregation.
   * 
   * @param field The documents field as determined by the aggregation name.
   * @param aggregation The aggregation which holds the buckets.
   * @return buckets for the given field and {@link StringTerms} aggregation.
   */
  protected Set<Bucket> getStringTermBuckets(DocumentField field, StringTerms aggregation) {
    Set<Bucket> buckets = new HashSet<>();

    aggregation.getBuckets().forEach(bucket -> {
        buckets.add(new Bucket(bucket.getKey(), bucket.getDocCount()));
      });

    return buckets;
  }
}
