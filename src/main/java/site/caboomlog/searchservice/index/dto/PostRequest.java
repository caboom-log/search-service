package site.caboomlog.searchservice.index.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostRequest {
    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("blog_id")
    private Long blogId;

    @JsonProperty("writer_mb_no")
    private Long writerMbNo;

    @JsonProperty("post_title")
    private String postTitle;

    @JsonProperty("post_content")
    private String postContent;

    @JsonProperty("post_public")
    private boolean postPublic;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
}
