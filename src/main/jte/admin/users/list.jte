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
        <div class="flex justify-between items-center mb-4">
            <h1 class="text-2xl font-bold text-gray-900">Users</h1>
            <a href="/admin/users/new"
               class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700">
                Add New
            </a>
        </div>

        <form method="get" hx-get="/admin/users" hx-target="#users-table" hx-indicator="#loading"
              class="bg-white p-4 rounded-lg shadow mb-4">
            <div class="flex flex-wrap gap-4">
                <div class="flex-1 min-w-64">
                    <input type="text" name="search" value="${search}" placeholder="Search users..."
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                           hx-trigger="keyup changed delay:300ms">
                </div>
                <div class="min-w-32">
                    <select name="roleId"
                            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                            hx-trigger="change">
                        <option value="">All Roles</option>
                        @if(roles != null)
                            @for(Role r : roles)
                                <option value="${r.getId()}" selected?="${roleId != null && roleId.equals(r.getId())}">
                                    ${r.getName()}
                                </option>
                            @endfor
                        @endif
                    </select>
                </div>
                <div class="min-w-32">
                    <select name="enabled"
                            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                            hx-trigger="change">
                        <option value="">All Status</option>
                        <option value="true" selected?="${Boolean.TRUE.equals(enabled)}">Active</option>
                        <option value="false" selected?="${Boolean.FALSE.equals(enabled)}">Disabled</option>
                    </select>
                </div>
                <button type="submit"
                        class="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700">
                    Filter
                </button>
                <a href="/admin/users" hx-get="/admin/users" hx-target="#users-table"
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
