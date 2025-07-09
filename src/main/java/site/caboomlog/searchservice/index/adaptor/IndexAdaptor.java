package site.caboomlog.searchservice.index.adaptor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import site.caboomlog.searchservice.index.dto.CreateIndexResponse;
import site.caboomlog.searchservice.index.dto.PostRequest;

@Component
@RequiredArgsConstructor
public class IndexAdaptor {

    @Value("${elastic.host}")
    private String host;

    private final WebClient webClient;

    /**
     * 게시글 데이터를 Elasticsearch에 인덱싱 요청합니다.
     *
     * <p>
     * 지정된 인덱스 이름에 대해 Elasticsearch의 `_doc` 엔드포인트로 POST 요청을 전송하여,
     * 게시글 데이터를 문서로 저장합니다.
     * </p>
     *
     * @param indexName     Elasticsearch 인덱스 이름
     * @param postRequest   인덱싱할 게시글 데이터
     * @return              인덱싱 결과를 담은 {@link CreateIndexResponse}를 포함하는 {@link Mono<ResponseEntity>}
     */
    public Mono<ResponseEntity<CreateIndexResponse>> createPostIndex(String indexName, PostRequest postRequest) {
        return webClient.post()
                .uri(host + "/" + indexName + "/_doc")
                .bodyValue(postRequest)
                .retrieve()
                .toEntity(CreateIndexResponse.class);
    }

}
