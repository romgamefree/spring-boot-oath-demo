@import dev.danvega.common.web.PagedResult
@import dev.danvega.config.CsrfHiddenInput
@import dev.danvega.model.Article
@import java.net.URLEncoder
@import java.time.format.DateTimeFormatter

@param CsrfHiddenInput csrfHiddenInput
@param PagedResult<Article> articles
@param String search = ""
@param Boolean published = null

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
            <th scope="col"
                class="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                Title
            </th>
            <th scope="col"
                class="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                Status
            </th>
            <th scope="col"
                class="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                Created
            </th>
            <th scope="col"
                class="px-6 py-3 text-right text-xs font-medium uppercase tracking-wider text-gray-500">
                Actions
            </th>
        </tr>
        </thead>

        <tbody class="divide-y divide-gray-200 bg-white">
        @for(var article : articles.content())
            <tr>
                <td class="whitespace-nowrap px-6 py-4">
                    <div class="text-sm font-medium text-gray-900">
                        ${article.getTitle()}
                    </div>
                </td>

                <td class="whitespace-nowrap px-6 py-4">
                    @if(Boolean.TRUE.equals(article.getPublished()))
                        <span class="inline-flex rounded-full bg-green-100 px-2 text-xs font-semibold leading-5 text-green-800">
                            Published
                        </span>
                    @else
                        <span class="inline-flex rounded-full bg-yellow-100 px-2 text-xs font-semibold leading-5 text-yellow-800">
                            Draft
                        </span>
                    @endif
                </td>

                <td class="whitespace-nowrap px-6 py-4 text-sm text-gray-500">
                    @if(article.getCreatedAt() != null)
                        ${article.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}
                    @endif
                </td>

                <td class="whitespace-nowrap px-6 py-4 text-right text-sm font-medium">
                    <a href="/admin/articles/${article.getId()}/edit"
                       class="text-indigo-600 hover:text-indigo-900">
                        Edit
                    </a>

                    <form id="delete-article-${article.getId()}" action="/admin/articles/${article.getId()}/delete?page=${articles.currentPage()}&size=${articles.pageSize()}${queryString}"
                          method="post"
                          hx-post="/admin/articles/${article.getId()}/delete?page=${articles.currentPage()}&size=${articles.pageSize()}${queryString}"
                          hx-target="#articles-table"
                          hx-swap="innerHTML"
                          class="inline ml-4">
                        ${csrfHiddenInput}
                        <button type="button"
                                onclick="showDeleteModal(document.getElementById('delete-article-${article.getId()}'), 'Are you sure you want to delete this article?')"
                                class="text-red-600 hover:text-red-900">
                            Delete
                        </button>
                    </form>
                </td>
            </tr>
        @else
            <tr>
                <td colspan="4" class="px-6 py-8 text-center text-sm text-gray-500">
                    No articles found.
                </td>
            </tr>
        @endfor
        </tbody>
    </table>
</div>

@template.fragments.pagination(
page = articles,
baseUrl = "/admin/articles",
target = "#articles-table",
queryString = queryString
)