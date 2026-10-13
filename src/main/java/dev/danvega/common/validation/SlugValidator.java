package dev.danvega.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validator implementation cho {@link ValidSlug}.
 *
 * <p>Pattern hợp lệ: {@code ^[a-z0-9]+(-[a-z0-9]+)*$}</p>
 * <ul>
 *   <li>Bắt đầu và kết thúc bằng chữ/số</li>
 *   <li>Ở giữa có thể có dấu gạch ngang nhưng không double dash</li>
 *   <li>Không cho phép chữ hoa, khoảng trắng, ký tự đặc biệt</li>
 * </ul>
 */
public class SlugValidator implements ConstraintValidator<ValidSlug, String> {

    /** Regex cho slug hợp lệ: chữ thường/số, phân cách bởi dấu gạch ngang đơn */
    private static final Pattern SLUG_PATTERN =
            Pattern.compile("^[a-z0-9]+(-[a-z0-9]+)*$");

    private boolean required;

    @Override
    public void initialize(ValidSlug annotation) {
        this.required = annotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null/blank: hợp lệ nếu required=false (slug sẽ tự generate)
        if (value == null || value.isBlank()) {
            return !required;
        }

        boolean matches = SLUG_PATTERN.matcher(value.trim()).matches();

        if (!matches) {
            // Customize message để giải thích rõ hơn
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    buildDetailedMessage(value)
            ).addConstraintViolation();
        }

        return matches;
    }

    private String buildDetailedMessage(String value) {
        if (value.contains(" ")) {
            return "Slug không được chứa khoảng trắng. Gợi ý: '" +
                    value.trim().toLowerCase().replace(" ", "-") + "'";
        }
        if (value.chars().anyMatch(Character::isUpperCase)) {
            return "Slug không được chứa chữ hoa. Gợi ý: '" + value.toLowerCase() + "'";
        }
        if (value.startsWith("-") || value.endsWith("-")) {
            return "Slug không được bắt đầu hoặc kết thúc bằng dấu gạch ngang";
        }
        if (value.contains("--")) {
            return "Slug không được có hai dấu gạch ngang liên tiếp";
        }
        return "Slug chỉ được chứa chữ thường (a-z), số (0-9) và dấu gạch ngang (-)";
    }
}
