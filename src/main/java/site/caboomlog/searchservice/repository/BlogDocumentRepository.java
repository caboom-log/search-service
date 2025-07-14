package site.caboomlog.searchservice.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import site.caboomlog.searchservice.document.BlogDocument;

public interface BlogDocumentRepository extends ElasticsearchRepository<BlogDocument, String> {
}
