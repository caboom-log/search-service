package site.caboomlog.searchservice.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import site.caboomlog.searchservice.dto.PostRequest;

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
    @JsonProperty("title")
    private String postTitle;

    @Field(type = FieldType.Text, analyzer = "caboomlog_analyzer", name = "post_content")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String postContent;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String summary;

    @Field(type = FieldType.Keyword)
    private String thumbnail;

    @Field(type = FieldType.Date, format = DateFormat.year_month_day, index = false, name = "created_at")
    private String createdAt;

    @Field(type = FieldType.Text, analyzer = "caboomlog_analyzer")
    private List<String> topics;

    protected PostDocument(Long postId, String blogFid, String postTitle, String postContent,
                           String thumbnail, String createdAt, List<String> topics) {
        this.postId = postId;
        this.blogFid = blogFid;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
        this.topics = topics;
        if (postContent.length() > 50) {
            summary = postContent.substring(0, 50) + " ... ";
        } else {
            summary = postContent;
        }
    }

    public static PostDocument fromDto(PostRequest postRequest) {
        return new PostDocument(postRequest.getPostId(), postRequest.getBlogFid(),
                postRequest.getPostTitle(), postRequest.getPostContent(),
                postRequest.getThumbnail(),
                postRequest.getCreatedAt(), postRequest.getTopics());
    }
}
