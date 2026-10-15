@param String message = "Thông báo"
@param String type = "info"
@param String code = ""

<%-- Fragment toast chung dùng cho HTMX (HX-Retarget → #toast-area).
     Hỗ trợ các loại: info, error, warning, success.
     Tự động biến mất sau 5 giây nhờ Alpine.js. --%>
!{
    var t = type == null ? "info" : type.trim().toLowerCase();

    // [0]=nền+viền, [1]=màu icon & code, [2]=màu chữ, [3]=nút đóng, [4]=ký hiệu
    var s = switch (t) {
        case "error"   -> java.util.List.of("bg-red-50 border-red-200", "text-red-500", "text-red-800", "text-red-400 hover:text-red-600", "✖");
        case "warning" -> java.util.List.of("bg-yellow-50 border-yellow-200", "text-yellow-500", "text-yellow-800", "text-yellow-400 hover:text-yellow-600", "⚠");
        case "success" -> java.util.List.of("bg-green-50 border-green-200", "text-green-500", "text-green-800", "text-green-400 hover:text-green-600", "✓");
        default        -> java.util.List.of("bg-blue-50 border-blue-200", "text-blue-500", "text-blue-800", "text-blue-400 hover:text-blue-600", "ℹ");
    };
}
<div
        data-toast
        x-data="{ show: true }"
        x-show="show"
        x-init="setTimeout(() => show = false, 5000); setTimeout(() => $el.remove(), 5300)"
        x-transition:leave="transition ease-in duration-300"
        x-transition:leave-start="opacity-100 scale-100"
        x-transition:leave-end="opacity-0 scale-95"
        class="pointer-events-auto flex items-start gap-3 px-4 py-3 border rounded-lg shadow-md max-w-sm ${s.get(0)}">

    <span class="${s.get(1)} text-lg mt-0.5" aria-hidden="true">${s.get(4)}</span>

    <div class="flex-1 min-w-0">
        <p class="text-sm font-medium ${s.get(2)}">${message}</p>
        @if (code != null && !code.isEmpty())
            <p class="text-xs ${s.get(1)} mt-0.5 font-mono">${code}</p>
        @endif
    </div>

    <button
            type="button"
            @click="show = false; setTimeout(() => $el.closest('[data-toast]').remove(), 300)"
            class="${s.get(3)} flex-shrink-0"
            aria-label="Đóng thông báo">
        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd"
                  d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                  clip-rule="evenodd"/>
        </svg>
    </button>
</div>