@import dev.danvega.common.web.PagedResult
@import dev.danvega.config.CsrfHiddenInput
@import dev.danvega.model.StaticPage
@import java.net.URLEncoder

@param PagedResult<StaticPage> pages
@param String search = ""
@param Boolean published = null
@param CsrfHiddenInput csrfHiddenInput

!{
    String queryString = "";

    if (search != null && !search.isBlank()) {
        queryString += "&search=" + URLEncoder.encode(
            search,
            java.nio.charset.StandardCharsets.UTF_8
        );
    }

    if (published != null) {
        queryString += "&published=" + published;
    }
}

<div class="overflow-x-auto">
    <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
        <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Title</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Slug</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created</th>
            <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
        </tr>
        </thead>

        <tbody class="bg-white divide-y divide-gray-200">
        @for(var page : pages.content())
            <tr>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    ${page.getTitle()}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    ${page.getSlug()}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                    @if(Boolean.TRUE.equals(page.getPublished()))
                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">Published</span>
                    @else
                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-100 text-yellow-800">Draft</span>
                    @endif
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    ${page.getCreatedAt() != null ? page.getCreatedAt().toString() : ""}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <a href="/admin/pages/${page.getId()}/edit"
                       class="text-indigo-600 hover:text-indigo-900 mr-4">Edit</a>

                    <form action="/admin/pages/${page.getId()}/delete" method="post" style="display:inline"
                          hx-post="/admin/pages/${page.getId()}/delete?page=${pages.currentPage()}&size=${pages.pageSize()}${queryString}"
                          hx-target="#pages-table"
                          hx-swap="innerHTML"
                          hx-confirm="Are you sure?">
                        ${csrfHiddenInput}
                        <button type="submit" class="text-red-600 hover:text-red-900">Delete</button>
                    </form>
                </td>
            </tr>
        @else
            <tr>
                <td colspan="5" class="px-6 py-8 text-center text-sm text-gray-500">
                    No pages found.
                </td>
            </tr>
        @endfor
        </tbody>
    </table>
</div>

@template.fragments.pagination(
page = pages,
baseUrl = "/admin/pages",
target = "#pages-table",
queryString = queryString
)