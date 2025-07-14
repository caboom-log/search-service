package site.caboomlog.searchservice.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import site.caboomlog.searchservice.document.BlogDocument;
import site.caboomlog.searchservice.dto.BlogRequest;
import site.caboomlog.searchservice.repository.BlogDocumentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogDocumentRepository blogDocumentRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * 전달받은 블로그 데이터를 Elasticsearch에 저장합니다.
     *
     * @param blogRequest 저장할 게시글 요청 DTO
     */
    public void createBlog(BlogRequest blogRequest) {
        BlogDocument blogDocument = BlogDocument.fromDto(blogRequest);
        blogDocumentRepository.save(blogDocument);
    }

    /**
     * blogFid를 기준으로 Elasticsearch에서 해당 블로그 인덱스를 삭제합니다.
     *
     * @param blogFid 삭제할 블로그의 FID
     */
    public void deleteByBlogFid(String blogFid) {
        blogDocumentRepository.deleteById(blogFid);
    }

    /**
     * 블로그 문서를 검색하는 메서드입니다.
     * blogType이 주어지면 해당 타입으로 필터링된 검색을 수행하고,
     * 없으면 blog_name 또는 blog_fid 기준으로 전체 검색을 수행합니다.
     *
     * @param keyword   검색할 키워드 (예: 블로그 이름 또는 ID 일부)
     * @param blogType  필터링할 블로그 유형 (예: "team", "personal"), 비워두면 전체 검색
     * @param size      페이지당 결과 수
     * @param offset    페이지 번호 (0부터 시작)
     * @return          페이징된 블로그 문서 리스트
     */
    public Page<BlogDocument> searchBlogs(String keyword, String blogType, int size, int offset) {
        SearchHits<BlogDocument> searchHits;
        if (blogType.isBlank()) {
            searchHits = searchBlogs(keyword, size, offset);
        } else {
            searchHits = searchBlogsFilteredByBlogType(keyword, blogType, size, offset);
        }

        List blogs = searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();
        return new PageImpl<>(blogs, PageRequest.of(offset + 1, size),
                getTotalElements());
    }

    /**
     * 블로그 타입 필터 없이 blog_name, blog_fid 기준으로 키워드 검색을 수행합니다.
     *
     * @param keyword   검색 키워드
     * @param size      페이지당 결과 수
     * @param offset    페이지 번호 (0부터 시작)
     * @return          검색 결과 SearchHits 객체
     */
    private SearchHits<BlogDocument> searchBlogs(String keyword, int size, int offset) {
        Query multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(keyword)
                .fields("blog_name^2", "blog_fid")
                .fuzziness("AUTO")
        )._toQuery();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(multiMatchQuery)
                .withPageable(PageRequest.of(offset, size))
                .build();

        return elasticsearchOperations.search(
                nativeQuery,
                BlogDocument.class
        );
    }

    /**
     * 주어진 blogType으로 필터링된 블로그 문서를 검색합니다.
     * 검색 필드는 blog_name과 blog_fid이며, fuzziness는 AUTO로 설정되어 있습니다.
     *
     * @param keyword   검색 키워드
     * @param blogType  필터링할 블로그 유형
     * @param size      페이지당 결과 수
     * @param offset    페이지 번호 (0부터 시작)
     * @return          검색 결과 SearchHits 객체
     */
    private SearchHits<BlogDocument> searchBlogsFilteredByBlogType(String keyword, String blogType, int size, int offset) {
        Query multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(keyword)
                .fields("blog_name^2", "blog_fid")
                .fuzziness("AUTO")
        )._toQuery();

        Query blogTypeFilter = TermQuery.of(t -> t
                .field("blog_type")
                .value(blogType)
        )._toQuery();

        Query boolQuery = BoolQuery.of(b -> b
                .must(multiMatchQuery)
                .filter(blogTypeFilter)
        )._toQuery();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(offset, size))
                .build();

        return elasticsearchOperations.search(
                nativeQuery,
                BlogDocument.class
        );
    }

    /**
     * Elasticsearch 인덱스에 저장된 전체 문서 수를 반환합니다.
     *
     * @return 전체 문서 개수
     */
    private long getTotalElements() {
        return blogDocumentRepository.count();
    }
}
