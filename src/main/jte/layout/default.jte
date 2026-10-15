@import org.springframework.security.web.csrf.CsrfToken
@import dev.danvega.common.web.FlashMessage

@param gg.jte.Content content
@param FlashMessage flash = null

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Spring Security Demo</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="//unpkg.com/alpinejs" defer></script>
    <script src="https://unpkg.com/htmx.org@1.9.10"></script>
</head>
<body class="bg-gray-100">
    <!-- Toast container -->
    !{ var currentFlash = flash != null ? flash : dev.danvega.common.web.FlashMessage.getCurrent(); }
    <div id="toast-area" class="fixed top-4 right-4 z-50 flex flex-col gap-2 pointer-events-none">
        @if (currentFlash != null)
            @template.fragments.toast(message = currentFlash.message(), type = currentFlash.typeString())
        @endif
    </div>

    ${content}
</body>
</html>