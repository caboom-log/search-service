package site.caboomlog.searchservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlogRequest {
    @NotBlank(message = "blogFid는 필수 항목입니다.")
    @Size(min = 3, max = 50, message = "blogFid는 3~50글자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "blogFid는 영문자와 숫자만 포함해야 합니다.")
    private String blogFid;

    @NotBlank(message = "blogName은 필수 항목입니다.")
    @Size(min = 1, max = 50, message = "blogName은 1 ~ 50자까지 입력할 수 있습니다.")
    private String blogName;

    private String blogMainImg;

    @Size(max = 100, message = "blogDesc는 최대 100자까지 입력할 수 있습니다.")
    private String blogDescription;

    @Pattern(regexp = "personal|team", message = "blogType은 'personal' 또는 'team'만 허용됩니다.")
    private String blogType;
}
