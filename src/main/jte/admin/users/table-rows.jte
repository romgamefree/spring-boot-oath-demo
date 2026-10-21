@import dev.danvega.common.web.PagedResult
@import dev.danvega.config.CsrfHiddenInput
@import dev.danvega.model.Role
@import dev.danvega.model.User
@import java.net.URLEncoder
@import java.time.format.DateTimeFormatter

@param PagedResult<User> users
@param java.util.List<Role> roles = null
@param String search = ""
@param Long roleId = null
@param Boolean enabled = null
@param CsrfHiddenInput csrfHiddenInput = null

!{
    String queryString = "";
    if (search != null && !search.isBlank()) {
        queryString += "&search=" + URLEncoder.encode(search, java.nio.charset.StandardCharsets.UTF_8);
    }
    if (roleId != null) {
        queryString += "&roleId=" + roleId;
    }
    if (enabled != null) {
        queryString += "&enabled=" + enabled;
    }
    var csrf = csrfHiddenInput != null ? csrfHiddenInput : dev.danvega.config.CsrfHiddenInput.get();
}

<div class="overflow-x-auto">
    <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
        <tr>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                User
            </th>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                Email
            </th>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                Roles & Permissions
            </th>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                Status
            </th>
            <th scope="col" class="px-6 py-3 text-right text-xs font-medium uppercase tracking-wider text-gray-500">
                Actions
            </th>
        </tr>
        </thead>
        <tbody class="divide-y divide-gray-200 bg-white">
        @for(User u : users.content())
            <tr class="hover:bg-gray-50 transition-colors">
                <td class="whitespace-nowrap px-6 py-4">
                    <div class="flex items-center">
                        <div class="h-10 w-10 flex-shrink-0 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-700 font-bold uppercase">
                            ${u.getUsername().substring(0, Math.min(2, u.getUsername().length()))}
                        </div>
                        <div class="ml-4">
                            <div class="text-sm font-semibold text-gray-900">${u.getUsername()}</div>
                            @if(u.getFullName() != null && !u.getFullName().isBlank())
                                <div class="text-xs text-gray-500">${u.getFullName()}</div>
                            @endif
                        </div>
                    </div>
                </td>
                <td class="whitespace-nowrap px-6 py-4 text-sm text-gray-500">
                    ${u.getEmail() != null ? u.getEmail() : "—"}
                </td>
                <td class="px-6 py-4 text-sm">
                    <div class="flex flex-wrap gap-1">
                        @if(u.getRoles() != null && !u.getRoles().isEmpty())
                            @for(Role r : u.getRoles())
                                !{
                                    String badgeClass = switch(r.getName()) {
                                        case "ROLE_ADMIN" -> "bg-red-100 text-red-800 border-red-200";
                                        case "ROLE_EDITOR" -> "bg-purple-100 text-purple-800 border-purple-200";
                                        default -> "bg-blue-100 text-blue-800 border-blue-200";
                                    };
                                }
                                <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium border ${badgeClass}">
                                    ${r.getName()}
                                </span>
                            @endfor
                        @else
                            <span class="text-xs text-gray-400">Không có vai trò</span>
                        @endif
                    </div>
                </td>
                <td class="whitespace-nowrap px-6 py-4">
                    <form action="/admin/users/${u.getId()}/toggle-status?page=${users.currentPage()}&size=${users.pageSize()}${queryString}"
                          method="post"
                          hx-post="/admin/users/${u.getId()}/toggle-status?page=${users.currentPage()}&size=${users.pageSize()}${queryString}"
                          hx-target="#users-table"
                          hx-swap="innerHTML"
                          class="inline">
                        ${csrf}
                        <button type="submit"
                                title="Click để đổi trạng thái"
                                class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium cursor-pointer transition-colors ${Boolean.TRUE.equals(u.getEnabled()) ? "bg-green-100 text-green-800 hover:bg-green-200" : "bg-gray-100 text-gray-800 hover:bg-gray-200"}">
                            <span class="w-1.5 h-1.5 mr-1.5 rounded-full ${Boolean.TRUE.equals(u.getEnabled()) ? "bg-green-500" : "bg-gray-400"}"></span>
                            ${Boolean.TRUE.equals(u.getEnabled()) ? "Hoạt động" : "Vô hiệu"}
                        </button>
                    </form>
                </td>
                <td class="whitespace-nowrap px-6 py-4 text-right text-sm font-medium">
                    <div class="flex items-center justify-end gap-3">
                        <a href="/admin/users/${u.getId()}/edit"
                           class="text-indigo-600 hover:text-indigo-900 inline-flex items-center text-xs font-semibold">
                            Sửa
                        </a>

                        @if(!"admin".equalsIgnoreCase(u.getUsername()))
                            <form id="delete-user-${u.getId()}" action="/admin/users/${u.getId()}/delete?page=${users.currentPage()}&size=${users.pageSize()}${queryString}"
                                  method="post"
                                  hx-post="/admin/users/${u.getId()}/delete?page=${users.currentPage()}&size=${users.pageSize()}${queryString}"
                                  hx-target="#users-table"
                                  hx-swap="innerHTML"
                                  class="inline">
                                ${csrf}
                                <button type="button"
                                        onclick="showDeleteModal(document.getElementById('delete-user-${u.getId()}'), 'Are you sure you want to delete user ${u.getUsername()}?')"
                                        class="text-red-600 hover:text-red-900 text-xs font-semibold cursor-pointer">
                                    Xóa
                                </button>
                            </form>
                        @endif
                    </div>
                </td>
            </tr>
        @else
            <tr>
                <td colspan="5" class="px-6 py-8 text-center text-sm text-gray-500">
                    Không tìm thấy người dùng nào.
                </td>
            </tr>
        @endfor
        </tbody>
    </table>
</div>

@template.fragments.pagination(
    page = users,
    baseUrl = "/admin/users",
    target = "#users-table",
    queryString = queryString
)
