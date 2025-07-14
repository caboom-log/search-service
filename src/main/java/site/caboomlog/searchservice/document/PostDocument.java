package site.caboomlog.searchservice.document;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import site.caboomlog.searchservice.dto.PostRequest;

import java.time.LocalDateTime;
import java.util.List;

@Document(indexName = "posts")
@Getter
@Setting(settingPath = "/elasticsearch/caboomlog-settings.json")
public class PostDocument {
    @Id
    @Field(name = "post_id")
    private Long postId;

    @Field(type = FieldType.Keyword, index = false, name = "blog_fid")
    private String blogFid;

    @Field(type = FieldType.Text, analyzer = "caboomlog_analyzer", name = "post_title")
    private String postTitle;

    @Field(type = FieldType.Text, analyzer = "caboomlog_analyzer", name = "post_content")
    private String postContent;

    @Field(type = FieldType.Date, index = false, name = "created_at")
    private LocalDateTime createdAt;

    @Field(type = FieldType.Text, analyzer = "caboomlog_analyzer")
    private List<String> topics;

    protected PostDocument(Long postId, String blogFid, String postTitle, String postContent,
                           LocalDateTime createdAt, List<String> topics) {
        this.postId = postId;
        this.blogFid = blogFid;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.createdAt = createdAt;
        this.topics = topics;
    }

    public static PostDocument fromDto(PostRequest postRequest) {
        return new PostDocument(postRequest.getPostId(), postRequest.getBlogFid(),
                postRequest.getPostTitle(), postRequest.getPostContent(),
                postRequest.getCreatedAt(), postRequest.getTopics());
    }
}
