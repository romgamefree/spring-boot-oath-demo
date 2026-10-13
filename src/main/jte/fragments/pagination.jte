@import dev.danvega.common.web.PagedResult

@param PagedResult<?> page
@param String baseUrl
@param String target
@param String queryString = ""

!{
    int currentPage = page.currentPage();
    int totalPages = page.totalPages();

    int startPage = Math.max(0, currentPage - 2);
    int endPage = Math.min(totalPages - 1, currentPage + 2);

    if (startPage == 0) {
        endPage = Math.min(totalPages - 1, 4);
    }

    if (endPage == totalPages - 1) {
        startPage = Math.max(0, totalPages - 5);
    }
}

@if(totalPages > 0)
    <div class="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <p class="text-sm text-gray-500">
            Showing ${currentPage * page.pageSize() + 1}
            to ${Math.min(
                (currentPage + 1) * page.pageSize(),
                page.totalElements()
            )}
            of ${page.totalElements()} results
        </p>

        @if(page.isPaged())
            <nav aria-label="Pagination">
                <ul class="flex items-center gap-1">

                    <%-- Previous --%>
                    <li>
                        @if(page.hasPrevious())
                            <a
                                    href="${baseUrl}?page=${currentPage - 1}&size=${page.pageSize()}${queryString}"
                                    hx-get="${baseUrl}?page=${currentPage - 1}&size=${page.pageSize()}${queryString}"
                                    hx-target="${target}"
                                    hx-swap="innerHTML"
                                    aria-label="Go to previous page"
                                    class="inline-flex h-9 items-center gap-1 rounded-md px-3 text-sm hover:bg-gray-100">
                                <span aria-hidden="true">‹</span>
                                <span>Previous</span>
                            </a>
                        @else
                            <span
                                    aria-disabled="true"
                                    class="inline-flex h-9 items-center gap-1 rounded-md px-3 text-sm text-gray-400">
                                <span aria-hidden="true">‹</span>
                                <span>Previous</span>
                            </span>
                        @endif
                    </li>

                    <%-- First page --%>
                    @if(startPage > 0)
                        <li>
                            <a
                                    href="${baseUrl}?page=0&size=${page.pageSize()}${queryString}"
                                    hx-get="${baseUrl}?page=0&size=${page.pageSize()}${queryString}"
                                    hx-target="${target}"
                                    hx-swap="innerHTML"
                                    aria-label="Go to page 1"
                                    class="inline-flex size-9 items-center justify-center rounded-md text-sm hover:bg-gray-100">
                                1
                            </a>
                        </li>

                        @if(startPage > 1)
                            <li>
                                <span aria-hidden="true"
                                      class="inline-flex size-9 items-center justify-center text-gray-500">
                                    …
                                </span>
                            </li>
                        @endif
                    @endif

                    <%-- Visible page numbers --%>
                    @for(int i = startPage; i <= endPage; i++)
                        <li>
                            @if(i == currentPage)
                                <span
                                        aria-current="page"
                                        class="inline-flex size-9 items-center justify-center rounded-md border border-gray-200 bg-gray-100 text-sm font-medium">
                                    ${i + 1}
                                </span>
                            @else
                                <a
                                        href="${baseUrl}?page=${i}&size=${page.pageSize()}${queryString}"
                                        hx-get="${baseUrl}?page=${i}&size=${page.pageSize()}${queryString}"
                                        hx-target="${target}"
                                        hx-swap="innerHTML"
                                        aria-label="Go to page ${i + 1}"
                                        class="inline-flex size-9 items-center justify-center rounded-md text-sm hover:bg-gray-100">
                                    ${i + 1}
                                </a>
                            @endif
                        </li>
                    @endfor

                    <%-- Last page --%>
                    @if(endPage < totalPages - 1)
                        @if(endPage < totalPages - 2)
                            <li>
                                <span aria-hidden="true"
                                      class="inline-flex size-9 items-center justify-center text-gray-500">
                                    …
                                </span>
                            </li>
                        @endif

                        <li>
                            <a
                                    href="${baseUrl}?page=${totalPages - 1}&size=${page.pageSize()}${queryString}"
                                    hx-get="${baseUrl}?page=${totalPages - 1}&size=${page.pageSize()}${queryString}"
                                    hx-target="${target}"
                                    hx-swap="innerHTML"
                                    aria-label="Go to page ${totalPages}"
                                    class="inline-flex size-9 items-center justify-center rounded-md text-sm hover:bg-gray-100">
                                ${totalPages}
                            </a>
                        </li>
                    @endif

                    <%-- Next --%>
                    <li>
                        @if(page.hasNext())
                            <a
                                    href="${baseUrl}?page=${currentPage + 1}&size=${page.pageSize()}${queryString}"
                                    hx-get="${baseUrl}?page=${currentPage + 1}&size=${page.pageSize()}${queryString}"
                                    hx-target="${target}"
                                    hx-swap="innerHTML"
                                    aria-label="Go to next page"
                                    class="inline-flex h-9 items-center gap-1 rounded-md px-3 text-sm hover:bg-gray-100">
                                <span>Next</span>
                                <span aria-hidden="true">›</span>
                            </a>
                        @else
                            <span
                                    aria-disabled="true"
                                    class="inline-flex h-9 items-center gap-1 rounded-md px-3 text-sm text-gray-400">
                                <span>Next</span>
                                <span aria-hidden="true">›</span>
                            </span>
                        @endif
                    </li>

                </ul>
            </nav>
        @endif
    </div>
@endif