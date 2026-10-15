@import dev.danvega.common.web.PagedResult
@import dev.danvega.config.CsrfHiddenInput
@import dev.danvega.model.Role
@import dev.danvega.model.User

@param PagedResult<User> users
@param java.util.List<Role> roles = null
@param String search = ""
@param Long roleId = null
@param Boolean enabled = null
@param CsrfHiddenInput csrfHiddenInput = null

@template.layout.admin(
activePage = "users",
content = @`
    <div class="mb-6">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <div>
                <h1 class="text-2xl font-bold text-gray-900">Quản lý Người dùng & Phân quyền</h1>
                <p class="text-sm text-gray-500 mt-1">Quản lý danh sách tài khoản, gán vai trò và phân quyền trong hệ thống</p>
            </div>
            <a href="/admin/users/new"
               class="inline-flex items-center px-4 py-2 bg-indigo-600 text-white text-sm font-medium rounded-md hover:bg-indigo-700 shadow-sm transition-colors">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                </svg>
                Thêm người dùng mới
            </a>
        </div>
    </div>

    <!-- Filters -->
    <div class="bg-white p-4 rounded-lg shadow mb-6">
        <form method="get"
              action="/admin/users"
              hx-get="/admin/users"
              hx-target="#users-table"
              hx-indicator="#loading"
              class="grid grid-cols-1 md:grid-cols-3 gap-4">

            <div>
                <label for="search" class="block text-xs font-medium text-gray-700 uppercase mb-1">Tìm kiếm</label>
                <div class="relative">
                    <input type="text"
                           id="search"
                           name="search"
                           value="${search}"
                           placeholder="Username, họ tên, email..."
                           hx-get="/admin/users"
                           hx-target="#users-table"
                           hx-trigger="keyup changed delay:300ms, search"
                           hx-include="[name='roleId'], [name='enabled']"
                           class="block w-full pl-3 pr-10 py-2 border border-gray-300 rounded-md text-sm placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500">
                    <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                        <svg class="h-4 w-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                        </svg>
                    </div>
                </div>
            </div>

            <div>
                <label for="roleId" class="block text-xs font-medium text-gray-700 uppercase mb-1">Vai trò</label>
                <select id="roleId"
                        name="roleId"
                        hx-get="/admin/users"
                        hx-target="#users-table"
                        hx-trigger="change"
                        hx-include="[name='search'], [name='enabled']"
                        class="block w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500">
                    <option value="">-- Tất cả vai trò --</option>
                    @if(roles != null)
                        @for(Role r : roles)
                            <option value="${r.getId()}" selected="${roleId != null && roleId.equals(r.getId())}">
                                ${r.getName()} (${r.getDescription() != null ? r.getDescription() : ""})
                            </option>
                        @endfor
                    @endif
                </select>
            </div>

            <div>
                <label for="enabled" class="block text-xs font-medium text-gray-700 uppercase mb-1">Trạng thái</label>
                <select id="enabled"
                        name="enabled"
                        hx-get="/admin/users"
                        hx-target="#users-table"
                        hx-trigger="change"
                        hx-include="[name='search'], [name='roleId']"
                        class="block w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500">
                    <option value="">-- Tất cả trạng thái --</option>
                    <option value="true" selected="${Boolean.TRUE.equals(enabled)}">Đang hoạt động</option>
                    <option value="false" selected="${Boolean.FALSE.equals(enabled)}">Đã vô hiệu</option>
                </select>
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
    <div id="users-table" class="bg-white shadow rounded-lg overflow-hidden">
        @template.admin.users.table-rows(
            users = users,
            roles = roles,
            search = search,
            roleId = roleId,
            enabled = enabled,
            csrfHiddenInput = csrfHiddenInput
        )
    </div>
`
)
