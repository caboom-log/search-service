package site.caboomlog.searchservice.index.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import site.caboomlog.searchservice.index.adaptor.IndexAdaptor;
import site.caboomlog.searchservice.index.dto.CreateIndexResponse;
import site.caboomlog.searchservice.index.dto.PostRequest;

@Service
@RequiredArgsConstructor
public class IndexService {

    @Value("${elastic.index.post}")
    private String postIndexName;

    private final IndexAdaptor indexAdaptor;

    /**
     * 게시글 정보를 받아 Elasticsearch에 문서를 등록합니다.
     * <p>
     * 인덱싱 성공 시 생성된 문서의 ID를 반환하며, 실패하거나 예외가 발생할 경우 500 에러와 메시지를 반환합니다.
     *
     * @param postRequest 인덱싱할 게시글 정보
     * @return 생성된 문서 ID 또는 에러 메시지를 포함한 HTTP 응답 Mono
     */
    public Mono<ResponseEntity<String>> createPost(PostRequest postRequest) {
        return indexAdaptor.createPostIndex(postIndexName, postRequest)
                .map(response -> {
                    CreateIndexResponse body = response.getBody();
                    if (body != null && body.getShards().getSuccessful() > 0) {
                        return ResponseEntity.status(201).body(body.getId());
                    } else {
                        return ResponseEntity.status(500).body("인덱스 생성 실패");
                    }
                })
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(500).body(e.getMessage()));
                });
    }

}
