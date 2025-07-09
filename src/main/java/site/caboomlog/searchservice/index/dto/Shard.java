package site.caboomlog.searchservice.index.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Shard {
    private Long total;
    private Long successful;
    private Long failed;
}
