package site.caboomlog.searchservice.document;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import site.caboomlog.searchservice.dto.BlogRequest;
@Document(indexName = "blogs")
@Getter
@Setting(settingPath = "/elasticsearch/blog-settings.json")
public class BlogDocument {
    @Id
    @Field(type = FieldType.Text, analyzer = "caboomlog_blog_analyzer", name = "blog_fid")
    private String blogFid;

    @Field(type = FieldType.Text, analyzer = "caboomlog_blog_analyzer", name = "blog_name")
    private String blogName;

    @Field(type = FieldType.Text, index = false, name = "blog_main_img")
    private String blogMainImg;

    @Field(type = FieldType.Text, index = false, name = "blog_description")
    private String blogDescription;

    @Field(type = FieldType.Keyword, name = "blog_type")
    private String blogType;

    public BlogDocument(String blogFid, String blogName, String blogMainImg, String blogDescription,
                        String blogType) {
        this.blogFid = blogFid;
        this.blogName = blogName;
        this.blogMainImg = blogMainImg;
        this.blogDescription = blogDescription;
        this.blogType = blogType;
    }

    public static BlogDocument fromDto(BlogRequest blogRequest) {
        return new BlogDocument(blogRequest.getBlogFid(), blogRequest.getBlogName(),
                blogRequest.getBlogMainImg(), blogRequest.getBlogDescription(),
                blogRequest.getBlogType());
    }
}
