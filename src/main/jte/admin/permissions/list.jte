@import dev.danvega.common.web.PagedResult
@import dev.danvega.config.CsrfHiddenInput
@import dev.danvega.model.Permission

@param PagedResult<Permission> permissions
@param String search = ""
@param String module = ""
@param CsrfHiddenInput csrfHiddenInput

@template.layout.admin(
activePage = "permissions",
content = @`
    <div class="mb-6">
        <div class="flex justify-between items-center mb-4">
            <h1 class="text-2xl font-bold text-gray-900">Permissions</h1>
            <a href="/admin/permissions/new"
               class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700">
                Add New
            </a>
        </div>

        <form method="get" hx-get="/admin/permissions" hx-target="#permissions-table" hx-indicator="#loading"
              class="bg-white p-4 rounded-lg shadow mb-4">
            <div class="flex flex-wrap gap-4">
                <div class="flex-1 min-w-64">
                    <input type="text" name="search" value="${search}" placeholder="Search permissions..."
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                           hx-trigger="keyup changed delay:300ms">
                </div>
                <div class="flex-1 min-w-64">
                    <input type="text" name="module" value="${module}" placeholder="Module..."
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                           hx-trigger="keyup changed delay:300ms">
                </div>
                <button type="submit"
                        class="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700">
                    Filter
                </button>
                <a href="/admin/permissions" hx-get="/admin/permissions" hx-target="#permissions-table"
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

    <div id="permissions-table" class="bg-white shadow rounded-lg overflow-hidden">
        @template.admin.permissions.table-rows(
        permissions = permissions,
        search = search,
        module = module,
        csrfHiddenInput = csrfHiddenInput
        )
    </div>
`
)
