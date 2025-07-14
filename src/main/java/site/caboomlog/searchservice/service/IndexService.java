package site.caboomlog.searchservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.caboomlog.searchservice.document.PostDocument;
import site.caboomlog.searchservice.repository.PostDocumentRepository;
import site.caboomlog.searchservice.dto.PostRequest;

@Service
@RequiredArgsConstructor
public class IndexService {

    private final PostDocumentRepository postDocumentRepository;

    /**
     * 게시글 정보를 받아 Elasticsearch에 문서를 등록합니다.
     * <p>
     * 인덱싱 성공 시 생성된 문서의 ID를 반환하며, 실패하거나 예외가 발생할 경우 500 에러와 메시지를 반환합니다.
     *
     * @param postRequest 인덱싱할 게시글 정보
     * @return 생성된 문서 ID 또는 에러 메시지를 포함한 HTTP 응답 Mono
     */
    public void createPost(PostRequest postRequest) {
        PostDocument postDocument = PostDocument.fromDto(postRequest);
        postDocumentRepository.save(postDocument);
    }

}
