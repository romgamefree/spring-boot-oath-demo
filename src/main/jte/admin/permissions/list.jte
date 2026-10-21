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
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <div>
                <h1 class="text-2xl font-bold text-gray-900">Quản lý Quyền hạn (Permissions)</h1>
                <p class="text-sm text-gray-500 mt-1">Quản lý danh sách quyền hạn trong hệ thống</p>
            </div>
            <a href="/admin/permissions/new"
               class="inline-flex items-center px-4 py-2 bg-indigo-600 text-white text-sm font-medium rounded-md hover:bg-indigo-700 shadow-sm transition-colors">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                </svg>
                Thêm quyền mới
            </a>
        </div>
    </div>

    <!-- Filters -->
    <div class="bg-white p-4 rounded-lg shadow mb-6">
        <form method="get"
              action="/admin/permissions"
              hx-get="/admin/permissions"
              hx-target="#permissions-table"
              hx-indicator="#loading"
              class="grid grid-cols-1 md:grid-cols-2 gap-4">

            <div>
                <label for="search" class="block text-xs font-medium text-gray-700 uppercase mb-1">Tìm kiếm</label>
                <div class="relative">
                    <input type="text"
                           id="search"
                           name="search"
                           value="${search}"
                           placeholder="Tên quyền..."
                           hx-get="/admin/permissions"
                           hx-target="#permissions-table"
                           hx-trigger="keyup changed delay:300ms, search"
                           hx-include="[name='module']"
                           class="block w-full pl-3 pr-10 py-2 border border-gray-300 rounded-md text-sm placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500">
                    <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                        <svg class="h-4 w-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                        </svg>
                    </div>
                </div>
            </div>

            <div>
                <label for="module" class="block text-xs font-medium text-gray-700 uppercase mb-1">Module</label>
                <input type="text"
                       id="module"
                       name="module"
                       value="${module}"
                       placeholder="vd: articles, users..."
                       hx-get="/admin/permissions"
                       hx-target="#permissions-table"
                       hx-trigger="keyup changed delay:300ms, search"
                       hx-include="[name='search']"
                       class="block w-full px-3 py-2 border border-gray-300 rounded-md text-sm placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500">
            </div>
        </form>

        <div id="loading" class="htmx-indicator mt-2 text-xs text-indigo-600 font-medium flex items-center gap-2">
            <svg class="animate-spin h-3.5 w-3.5 text-indigo-600" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            Đang tải dữ liệu...
        </div>
    </div>

    <!-- Table Container -->
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
