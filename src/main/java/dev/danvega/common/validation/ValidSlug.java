package dev.danvega.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Constraint annotation đảm bảo slug URL đúng format: chỉ chứa chữ thường,
 * số, và dấu gạch ngang; không bắt đầu/kết thúc bằng dấu gạch ngang.
 *
 * <h3>Hợp lệ:</h3>
 * <ul>
 *   <li>{@code "my-article"}</li>
 *   <li>{@code "spring-boot-4"}</li>
 *   <li>{@code "gioi-thieu-san-pham"}</li>
 * </ul>
 *
 * <h3>Không hợp lệ:</h3>
 * <ul>
 *   <li>{@code "My Article"} – có chữ hoa và khoảng trắng</li>
 *   <li>{@code "-leading-dash"} – bắt đầu bằng dấu gạch ngang</li>
 *   <li>{@code "double--dash"} – hai dấu gạch ngang liên tiếp</li>
 * </ul>
 *
 * <h3>Sử dụng trên Entity:</h3>
 * <pre>{@code
 * @ValidSlug
 * @Column(unique = true)
 * private String slug;
 * }</pre>
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SlugValidator.class)
public @interface ValidSlug {

    String message() default "Slug chỉ được chứa chữ thường (a-z), số (0-9) và dấu gạch ngang (-), không bắt đầu hoặc kết thúc bằng dấu gạch ngang";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** Cho phép slug null/blank (dùng khi slug được tự động generate). */
    boolean required() default false;
}
