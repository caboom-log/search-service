package site.caboomlog.searchservice.index.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateIndexResponse {
    @JsonProperty("_index")
    private String index;

    @JsonProperty("_id")
    private String id;

    @JsonProperty("_version")
    private Long version;

    private String result;

    @JsonProperty("_shards")
    private Shard shards;

    @JsonProperty("_seq_no")
    private Long seqNo;

    @JsonProperty("_primary_term")
    private Long primaryTerm;

}
