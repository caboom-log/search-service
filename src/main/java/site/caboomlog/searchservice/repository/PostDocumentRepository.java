package site.caboomlog.searchservice.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import site.caboomlog.searchservice.document.PostDocument;

public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument, Long> {
}
