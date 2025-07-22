package site.caboomlog.searchservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.caboomlog.searchservice.document.BlogDocument;
import site.caboomlog.searchservice.dto.BlogPageResponse;
import site.caboomlog.searchservice.dto.BlogRequest;
import site.caboomlog.searchservice.service.BlogService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BlogController {

    private final BlogService blogService;

    /**
     * 블로그를 Elasticsearch에 인덱싱합니다.
     *
     * @param blogRequest 요청 본문 (BlogFid, BlogName, BlogType 등)
     * @return 생성된 블로그 FID를 포함한 메시지와 함께 201 Created 응답
     */
    @PostMapping("/blogs")
    public ResponseEntity<String> createBlog(@RequestBody @Valid BlogRequest blogRequest) {
        blogService.createBlog(blogRequest);
        return ResponseEntity.status(201)
                .body(String.format("등록 완료: %s", blogRequest.getBlogFid()));
    }

    /**
     * blogFid를 기반으로 Elasticsearch에서 해당 블로그 인덱스를 삭제합니다.
     *
     * @param blogFid 삭제할 블로그의 FID
     * @return 삭제 완료 메시지와 함께 204 No Content 응답
     */
    @DeleteMapping("/blogs/{blogFid}")
    public ResponseEntity<String> deleteBlog(@PathVariable("blogFid") String blogFid) {
        blogService.deleteByBlogFid(blogFid);
        return ResponseEntity.status(204)
                .body(String.format("삭제 완료: %s", blogFid));
    }

    /**
     * Elasticsearch에서 블로그 FID 또는 블로그 이름 기준으로 블로그를 검색합니다.
     *
     * @param keyword    검색 키워드
     * @param blogType 블로그 유형 (선택값) : "team", "personal" 중 하나
     * @param size       페이지 크기 (기본값: 10)
     * @param offset     페이지 번호 (1부터 시작)
     * @return {@link BlogPageResponse} 객체를 포함한 200 OK 응답
     */
    @GetMapping("/search/blogs")
    public ResponseEntity<BlogPageResponse> searchPosts(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "blogType", required = false) String blogType,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "offset", defaultValue = "1") int offset) {
        Page<BlogDocument> result = blogService.searchBlogs(keyword, blogType, size, offset - 1);
        return ResponseEntity.ok(
                new BlogPageResponse(
                        result.getContent(), result.getTotalElements(),
                        result.getTotalPages(), result.getNumber() + 1
                )
        );
    }
}
