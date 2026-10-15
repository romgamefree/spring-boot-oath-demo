package dev.danvega.dto;

import dev.danvega.common.validation.ValidSlug;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO for updating an existing article.
 * Uses Spring validation annotations.
 */
@Data
public class ArticleUpdateRequest {

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề tối đa 255 ký tự")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @ValidSlug
    private String slug;

    @NotBlank(message = "Tác giả không được để trống")
    @Size(max = 100, message = "Tác giả tối đa 100 ký tự")
    private String author;

    private Boolean published;
}
