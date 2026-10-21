@import dev.danvega.common.web.PagedResult
@import dev.danvega.config.CsrfHiddenInput
@import dev.danvega.model.Permission

@param PagedResult<Permission> permissions
@param String search = ""
@param String module = ""
@param CsrfHiddenInput csrfHiddenInput

!{
    var csrf = csrfHiddenInput != null ? csrfHiddenInput : dev.danvega.config.CsrfHiddenInput.get();
}

@if(permissions == null || permissions.totalElements() == 0)
    <div class="p-8 text-center text-gray-500">
        <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path>
        </svg>
        <p class="mt-2 text-sm">Chưa có quyền nào trong hệ thống</p>
        <a href="/admin/permissions/new" class="mt-2 inline-flex items-center text-sm text-indigo-600 hover:text-indigo-700">
            Thêm quyền mới
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
                        Tên quyền
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Module
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Mô tả
                    </th>
                    <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Thao tác
                    </th>
                </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
                @for(Permission p : permissions.content())
                    <tr class="hover:bg-gray-50">
                        <td class="px-6 py-4 whitespace-nowrap">
                            <div class="text-sm font-medium text-gray-900">${p.getName()}</div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap">
                            <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800">
                                ${p.getModule()}
                            </span>
                        </td>
                        <td class="px-6 py-4">
                            <div class="text-sm text-gray-500">${p.getDescription() != null ? p.getDescription() : "-"}</div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                            <div class="flex items-center justify-end gap-2">
                                <a href="/admin/permissions/${p.getId()}/edit"
                                   class="text-indigo-600 hover:text-indigo-900">
                                    Chỉnh sửa
                                </a>
                                <form id="delete-permission-${p.getId()}" action="/admin/permissions/${p.getId()}/delete"
                                      method="post"
                                      hx-post="/admin/permissions/${p.getId()}/delete"
                                      hx-target="#permissions-table"
                                      hx-swap="innerHTML"
                                      class="inline ml-2">
                                    ${csrf}
                                    <button type="button"
                                            onclick="showDeleteModal(document.getElementById('delete-permission-${p.getId()}'), 'Are you sure you want to delete this permission?')"
                                            class="text-red-600 hover:text-red-900">
                                        Xóa
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                @endfor
            </tbody>
        </table>
    </div>

    !{
        var qs = "";
        if (search != null && !search.isBlank()) {
            qs = qs + "&search=" + search;
        }
        if (module != null && !module.isBlank()) {
            qs = qs + "&module=" + module;
        }
    }
    @template.fragments.pagination(
        page = permissions,
        baseUrl = "/admin/permissions",
        target = "#permissions-table",
        queryString = qs
    )
@endif
