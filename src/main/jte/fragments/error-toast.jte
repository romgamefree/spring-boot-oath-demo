@param String message = "Đã có lỗi xảy ra"
@param String code = "ERROR"

<%-- Fragment lỗi dùng cho HTMX (HX-Retarget → #toast-area).
     Tự động biến mất sau 5 giây nhờ Alpine.js. --%>
<div
    x-data="{ show: true }"
    x-show="show"
    x-init="setTimeout(() => show = false, 5000)"
    x-transition:leave="transition ease-in duration-300"
    x-transition:leave-start="opacity-100 translate-y-0"
    x-transition:leave-end="opacity-0 -translate-y-2"
    class="flex items-start gap-3 px-4 py-3 bg-red-50 border border-red-200 rounded-lg shadow-md max-w-sm">

    <span class="text-red-500 text-lg mt-0.5" aria-hidden="true">✖</span>

    <div class="flex-1 min-w-0">
        <p class="text-sm font-medium text-red-800">${message}</p>
        <p class="text-xs text-red-500 mt-0.5 font-mono">${code}</p>
    </div>

    <button
        @click="show = false"
        class="text-red-400 hover:text-red-600 flex-shrink-0"
        aria-label="Đóng thông báo lỗi">
        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd"
                  d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                  clip-rule="evenodd"/>
        </svg>
    </button>
</div>
