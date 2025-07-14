package site.caboomlog.searchservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlogRequest {
    @JsonProperty("blog_fid")
    private String blogFid;

    @JsonProperty("blog_name")
    private String blogName;

    @JsonProperty("blog_main_img")
    private String blogMainImg;

    @JsonProperty("blog_description")
    private String blogDescription;

    @JsonProperty("blog_type")
    private String blogType;
}
