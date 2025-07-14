package site.caboomlog.searchservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostRequest {
    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("blog_fid")
    private String blogFid;

    @JsonProperty("post_title")
    private String postTitle;

    @JsonProperty("post_content")
    private String postContent;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private List<String> topics;
}
