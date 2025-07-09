package site.caboomlog.searchservice.index.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import site.caboomlog.searchservice.index.dto.PostRequest;
import site.caboomlog.searchservice.index.service.IndexService;

@RestController
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;

    /**
     * 게시글 정보를 기반으로 Elasticsearch에 인덱스를 생성합니다.
     *
     * <p>
     * 전달받은 {@link PostRequest} 객체를 이용해 search-service에서 운영 중인 Elasticsearch에
     * 새로운 문서(_doc)를 생성하고, 생성 성공 여부에 따라 HTTP 상태 코드를 반환합니다.
     * </p>
     *
     * @param postRequest 게시글 인덱싱 요청 데이터
     * @return Elasticsearch 응답에 따라 201(Created) 또는 500(Internal Server Error)을 포함한 {@link ResponseEntity}
     */
    @PostMapping("/api/index/posts")
    public Mono<ResponseEntity<String>> createPost(@RequestBody PostRequest postRequest) {
        return indexService.createPost(postRequest);
    }
}
