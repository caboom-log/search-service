package site.caboomlog.searchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.caboomlog.searchservice.document.BlogDocument;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BlogPageResponse {
    private List<BlogDocument> blogs;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}
