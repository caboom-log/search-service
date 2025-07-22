package site.caboomlog.searchservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostRequest {
    @NotNull(message = "postId는 필수 항목입니다.")
    private long postId;

    @NotBlank(message = "blogFid는 필수 항목입니다.")
    private String blogFid;

    @NotBlank(message = "postTitle은 필수 항목입니다.")
    private String postTitle;

    @NotBlank(message = "postContent는 필수 항목입니다.")
    private String postContent;

    private String thumbnail;

    private String createdAt;

    @NotNull(message = "topics는 필수 항목입니다.")
    private List<String> topics;
}
