package site.caboomlog.searchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.caboomlog.searchservice.document.PostDocument;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostPageResponse {
    private List<PostDocument> posts;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}
