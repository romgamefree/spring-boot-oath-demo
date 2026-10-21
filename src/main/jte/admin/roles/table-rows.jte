@import dev.danvega.config.CsrfHiddenInput
@import dev.danvega.model.Role

@param java.util.List<Role> roles
@param java.util.Map<String, java.util.List<dev.danvega.model.Permission>> permissionsByModule
@param String search = ""
@param String module = ""
@param CsrfHiddenInput csrfHiddenInput

!{
    var csrf = csrfHiddenInput != null ? csrfHiddenInput : dev.danvega.config.CsrfHiddenInput.get();
}

@if(roles == null || roles.isEmpty())
    <div class="p-8 text-center text-gray-500">
        <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
        </svg>
        <p class="mt-2 text-sm">Chưa có vai trò nào trong hệ thống</p>
        <a href="/admin/roles/new" class="mt-2 inline-flex items-center text-sm text-indigo-600 hover:text-indigo-700">
            Thêm vai trò mới
            <svg class="ml-1 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
            </svg>
        </a>
    </div>
@else
    <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
                <tr>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Tên vai trò
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Mô tả
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Quyền hạn
                    </th>
                    <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Thao tác
                    </th>
                </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
                @for(Role r : roles)
                    <tr class="hover:bg-gray-50">
                        <td class="px-6 py-4 whitespace-nowrap">
                            <div class="text-sm font-medium text-gray-900">${r.getName()}</div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap">
                            <div class="text-sm text-gray-500">${r.getDescription() != null ? r.getDescription() : "-"}</div>
                        </td>
                        <td class="px-6 py-4">
                            <div class="flex flex-wrap gap-1">
                                @if(r.getPermissions() != null && !r.getPermissions().isEmpty())
                                    @for(var p : r.getPermissions())
                                        <span class="inline-flex items-center px-2 py-0.5 rounded text-xs bg-indigo-100 text-indigo-800">
                                            ${p.getName()}
                                        </span>
                                    @endfor
                                @else
                                    <span class="text-sm text-gray-400">Chưa gán quyền</span>
                                @endif
                            </div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                            <div class="flex items-center justify-end gap-2">
                                <a href="/admin/roles/${r.getId()}/edit"
                                   class="text-indigo-600 hover:text-indigo-900">
                                    Chỉnh sửa
                                </a>
                                @if(!"ROLE_ADMIN".equalsIgnoreCase(r.getName()) && !"ROLE_USER".equalsIgnoreCase(r.getName()))
                                    <form id="delete-role-${r.getId()}" action="/admin/roles/${r.getId()}/delete"
                                          method="post"
                                          hx-post="/admin/roles/${r.getId()}/delete"
                                          hx-target="#roles-table"
                                          hx-swap="innerHTML"
                                          class="inline ml-2">
                                        ${csrf}
                                        <button type="button"
                                                onclick="showDeleteModal(document.getElementById('delete-role-${r.getId()}'), 'Are you sure you want to delete this role?')"
                                                class="text-red-600 hover:text-red-900">
                                            Xóa
                                        </button>
                                    </form>
                                @endif
                            </div>
                        </td>
                    </tr>
                @endfor
            </tbody>
        </table>
    </div>
@endif
