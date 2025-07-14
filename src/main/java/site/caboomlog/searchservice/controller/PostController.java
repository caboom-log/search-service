package site.caboomlog.searchservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.caboomlog.searchservice.document.PostDocument;
import site.caboomlog.searchservice.dto.PostPageResponse;
import site.caboomlog.searchservice.dto.PostRequest;
import site.caboomlog.searchservice.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    /**
     * 게시글을 Elasticsearch에 인덱싱합니다.
     *
     * @param postRequest 게시글 요청 본문 (Post ID, Blog FID, 제목, 내용 등)
     * @return 생성된 게시글 ID를 포함한 메시지와 함께 201 Created 응답
     */
    @PostMapping("/posts")
    public ResponseEntity<String> createPost(@RequestBody PostRequest postRequest) {
        postService.createPost(postRequest);
        return ResponseEntity.status(201)
                .body(String.format("등록 완료: %d", postRequest.getPostId()));
    }

    /**
     * postId를 기반으로 Elasticsearch에서 해당 게시글 인덱스를 삭제합니다.
     *
     * @param postId 삭제할 게시글의 ID
     * @return 삭제 완료 메시지와 함께 204 No Content 응답
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId) {
        postService.deleteByPostId(postId);
        return ResponseEntity.status(204)
                .body(String.format("삭제 완료: %d", postId));
    }

    /**
     * Elasticsearch에서 키워드, 검색 유형, 날짜 범위, 블로그 ID 기준으로 게시글을 검색합니다.
     *
     * @param keyword    검색 키워드
     * @param searchType 검색 유형: "title", "content", "title_content" 중 하나
     * @param size       페이지 크기 (기본값: 10)
     * @param offset     페이지 번호 (1부터 시작)
     * @param startDate  검색 시작일 (기본값: 2025-01-01)
     * @param endDate    검색 종료일 (기본값: 3000-12-31)
     * @param blogFid    검색할 블로그 식별자 (선택값)
     * @return {@link PostPageResponse} 객체를 포함한 200 OK 응답
     */
    @GetMapping("/search/posts")
    public ResponseEntity<PostPageResponse> searchPosts(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "search_type") String searchType,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "offset", defaultValue = "1") int offset,
            @RequestParam(name = "start_date", defaultValue = "2025-01-01") String startDate,
            @RequestParam(name = "end_date", defaultValue = "3000-12-31") String endDate,
            @RequestParam(name = "blog_fid", required = false) String blogFid) {
        Page<PostDocument> result = postService
                                .searchPosts(keyword, searchType, size, offset - 1, startDate, endDate, blogFid);
        return ResponseEntity.ok(
                new PostPageResponse(
                        result.getContent(), result.getTotalElements(),
                        result.getTotalPages(), result.getNumber() + 1
                )
        );
    }
}
