@import dev.danvega.model.StaticPage
@import dev.danvega.config.CsrfHiddenInput

@param StaticPage page
@param CsrfHiddenInput csrfHiddenInput

@template.layout.admin(
activePage = "pages",
content = @`
    <div class="mb-6">
        <div class="flex justify-between items-center">
            <h1 class="text-2xl font-bold text-gray-900">
                ${page.getId() != null ? "Edit" : "New"} Page
            </h1>

            <a href="/admin/pages"
               class="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700">
                Back to List
            </a>
        </div>
    </div>

    <div class="bg-white shadow rounded-lg p-6">
        <form action="${page.getId() != null ? "/admin/pages/" + page.getId() : "/admin/pages"}"
              method="post">
            ${csrfHiddenInput}

            @if(page.getId() != null)
                <input type="hidden" name="_method" value="put">
            @endif

            <div class="space-y-6">
                <div>
                    <label for="title" class="block text-sm font-medium text-gray-700">
                        Title
                    </label>
                    <input type="text"
                           id="title"
                           name="title"
                           value="${page.getTitle() != null ? page.getTitle() : ""}"
                           required
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                </div>

                <div>
                    <label for="slug" class="block text-sm font-medium text-gray-700">
                        Slug
                    </label>
                    <input type="text"
                           id="slug"
                           name="slug"
                           value="${page.getSlug() != null ? page.getSlug() : ""}"
                           required
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                </div>

                <div>
                    <label for="content" class="block text-sm font-medium text-gray-700">
                        Content
                    </label>
                    <textarea id="content"
                              name="content"
                              rows="10"
                              required
                              class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">${page.getContent() != null ? page.getContent() : ""}</textarea>
                </div>

                <div>
                    <label class="flex items-center">
                        <input type="checkbox"
                               name="published"
                               checked="${Boolean.TRUE.equals(page.getPublished())}"
                               class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded">
                        <span class="ml-2 text-sm text-gray-700">Published</span>
                    </label>
                </div>

                <div>
                    <button type="submit"
                            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        ${page.getId() != null ? "Update" : "Create"} Page
                    </button>
                </div>
            </div>
        </form>
    </div>
`
)