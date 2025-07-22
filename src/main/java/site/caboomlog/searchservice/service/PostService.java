package site.caboomlog.searchservice.service;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import site.caboomlog.searchservice.document.PostDocument;
import site.caboomlog.searchservice.repository.PostDocumentRepository;
import site.caboomlog.searchservice.dto.PostRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostDocumentRepository postDocumentRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * 전달받은 게시글 요청 데이터를 Elasticsearch에 저장합니다.
     *
     * @param postRequest 저장할 게시글 요청 DTO
     */
    public void createPost(PostRequest postRequest) {
        PostDocument postDocument = PostDocument.fromDto(postRequest);
        postDocumentRepository.save(postDocument);
    }

    /**
     * postId를 기준으로 Elasticsearch에서 해당 게시글 인덱스를 삭제합니다.
     *
     * @param postId 삭제할 게시글의 ID
     */
    public void deleteByPostId(Long postId) {
        postDocumentRepository.deleteById(postId);
    }

    /**
     * 키워드, 검색 유형, 날짜 범위, blogFid 조건을 기반으로 게시글을 검색합니다.
     *
     * @param keyword    검색 키워드 (예: "안녕하세요")
     * @param searchType 검색 유형 ("title", "content", "title_content")
     * @param size       페이지 당 결과 개수
     * @param offset     페이지 번호 (0부터 시작)
     * @param startDate  검색 시작일 (예: "2025-01-01")
     * @param endDate    검색 종료일 (예: "2025-12-31")
     * @param blogFid    블로그 고유 식별자 (선택값)
     * @return 검색된 게시글 목록을 포함한 Page 객체
     * @throws IllegalArgumentException searchType이 유효하지 않은 경우
     */
    public Page<PostDocument> searchPosts(String keyword, String searchType,
                                          int size, int offset,
                                          String startDate, String endDate, String blogFid)  {
        Query matchQuery;
        switch (searchType) {
            case "title" :
                matchQuery = MatchQuery.of(m -> m
                        .field("post_title")
                        .query(keyword)
                        .fuzziness("AUTO")
                )._toQuery();
                break;
            case "content" :
                matchQuery = MatchQuery.of(m -> m
                        .field("post_content")
                        .query(keyword)
                        .fuzziness("AUTO")
                )._toQuery();
                break;
            case "title_content" :
                matchQuery = MultiMatchQuery.of(m -> m
                        .query(keyword)
                        .fields("post_title^2", "post_content")
                        .fuzziness("AUTO")
                )._toQuery();
                break;
            default :
                throw new IllegalArgumentException("searchType은 title, content, title_content 중 하나로 입력해야 합니다.");
        }

        Query dateRangeFilter = DateRangeQuery.of(r -> r
                .field("created_at")
                .gte(startDate)
                .lte(endDate)
        )._toRangeQuery()._toQuery();

        Query boolQuery;
        if (blogFid == null || blogFid.isBlank()) {
            boolQuery = BoolQuery.of(b -> b
                    .must(matchQuery)
                    .filter(dateRangeFilter)
            )._toQuery();
        } else {
            boolQuery = BoolQuery.of(b -> b
                    .must(matchQuery)
                    .filter(dateRangeFilter,
                            TermQuery.of(t -> t
                                    .field("blog_fid")
                                    .value(blogFid)
                            )._toQuery())
                    )._toQuery();
        }

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(offset, size))
                .build();

        SearchHits<PostDocument> searchHits = elasticsearchOperations.search(
                nativeQuery,
                PostDocument.class
        );

        List posts = searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();
        return new PageImpl<>(posts, PageRequest.of(offset + 1, size),
                getTotalElements());
    }

    /**
     * Elasticsearch 인덱스에 저장된 전체 문서 수를 반환합니다.
     *
     * @return 전체 문서 개수
     */
    private long getTotalElements() {
        return postDocumentRepository.count();
    }
}
