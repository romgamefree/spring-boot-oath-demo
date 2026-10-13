@import dev.danvega.common.web.PagedResult
@import dev.danvega.model.StaticPage
@import dev.danvega.config.CsrfHiddenInput

@param PagedResult<StaticPage> pages
@param String search = ""
@param Boolean published = null
@param CsrfHiddenInput csrfHiddenInput

@template.layout.admin(
activePage = "pages",
content = @`
    <div class="mb-6">
        <div class="flex justify-between items-center mb-4">
            <h1 class="text-2xl font-bold text-gray-900">Pages</h1>
            <a href="/admin/pages/new"
               class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700">
                Add New
            </a>
        </div>

        <form method="get"
              hx-get="/admin/pages"
              hx-target="#pages-table"
              hx-indicator="#loading"
              class="bg-white p-4 rounded-lg shadow mb-4">
            <div class="flex flex-wrap gap-4">
                <div class="flex-1 min-w-64">
                    <input type="text"
                           name="search"
                           value="${search}"
                           placeholder="Search pages..."
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                           hx-get="/admin/pages"
                           hx-trigger="keyup changed delay:300ms"
                           hx-target="#pages-table"
                           hx-include="closest form">
                </div>

                <div class="min-w-32">
                    <select name="published"
                            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                            hx-get="/admin/pages"
                            hx-trigger="change"
                            hx-target="#pages-table"
                            hx-include="closest form">
                        <option value="" selected?="${published == null}">All Status</option>
                        <option value="true" selected?="${Boolean.TRUE.equals(published)}">Published</option>
                        <option value="false" selected?="${Boolean.FALSE.equals(published)}">Draft</option>
                    </select>
                </div>

                <button type="submit"
                        class="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700">
                    Filter
                </button>

                <a href="/admin/pages"
                   hx-get="/admin/pages"
                   hx-target="#pages-table"
                   hx-push-url="true"
                   class="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300">
                    Clear
                </a>
            </div>
        </form>
    </div>

    <div id="loading" class="htmx-indicator hidden">
        <div class="text-center py-4">
            <div class="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
        </div>
    </div>

    <div id="pages-table" class="bg-white shadow rounded-lg overflow-hidden">
        @template.admin.pages.table-rows(
        pages = pages,
        search = search,
        published = published,
        csrfHiddenInput = csrfHiddenInput
        )
    </div>
`
)