@import dev.danvega.config.CsrfHiddenInput
@import dev.danvega.model.Permission
@import dev.danvega.model.Role

@param Role role
@param java.util.Map<String, java.util.List<Permission>> permissionsByModule
@param CsrfHiddenInput csrfHiddenInput

!{
    var csrf = csrfHiddenInput != null ? csrfHiddenInput : dev.danvega.config.CsrfHiddenInput.get();
    boolean isEdit = role.getId() != null;
}

@template.layout.admin(
activePage = "roles",
content = @`
    <div class="mb-6">
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-2xl font-bold text-gray-900">
                    ${isEdit ? "Chỉnh sửa vai trò" : "Thêm vai trò mới"}
                </h1>
                <p class="text-sm text-gray-500 mt-1">
                    ${isEdit ? "Cập nhật thông tin vai trò và quyền hạn" : "Tạo vai trò mới và gán quyền hạn"}
                </p>
            </div>
            <a href="/admin/roles"
               class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 transition-colors">
                Quay lại danh sách
            </a>
        </div>
    </div>

    <div class="bg-white shadow rounded-lg p-6 max-w-3xl">
        <form action="${isEdit ? "/admin/roles/" + role.getId() : "/admin/roles"}"
              method="post"
              class="space-y-6">

            ${csrf}

            @if(isEdit)
                <input type="hidden" name="_method" value="put">
            @endif

            <div class="grid grid-cols-1 gap-6">
                <!-- Role Name -->
                <div>
                    <label for="name" class="block text-sm font-medium text-gray-700">
                        Tên vai trò (Role Name) <span class="text-red-500">*</span>
                    </label>
                    <input type="text"
                           id="name"
                           name="name"
                           value="${role.getName() != null ? role.getName() : ""}"
                           required
                           minlength="3"
                           maxlength="50"
                           readonly="${isEdit}"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 ${isEdit ? "bg-gray-100 text-gray-500 cursor-not-allowed" : ""}"
                           placeholder="vd: ROLE_EDITOR">
                    @if(isEdit)
                        <p class="mt-1 text-xs text-gray-400">Tên vai trò không thể thay đổi sau khi tạo</p>
                    @endif
                </div>

                <!-- Description -->
                <div>
                    <label for="description" class="block text-sm font-medium text-gray-700">
                        Mô tả
                    </label>
                    <input type="text"
                           id="description"
                           name="description"
                           value="${role.getDescription() != null ? role.getDescription() : ""}"
                           maxlength="255"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="vd: Biên tập viên nội dung">
                </div>
            </div>

            <!-- Permissions Selection -->
            <div class="pt-4 border-t border-gray-200">
                <label class="block text-sm font-semibold text-gray-900 mb-2">
                    Quyền hạn (Permissions)
                </label>
                <p class="text-xs text-gray-500 mb-4">Chọn quyền hạn cho vai trò này:</p>

                <div class="space-y-4">
                    @if(permissionsByModule != null)
                        @for(var entry : permissionsByModule.entrySet())
                            !{
                                String moduleName = entry.getKey();
                                java.util.List<Permission> permissions = entry.getValue();
                            }
                            <div class="border rounded-lg p-4 bg-gray-50">
                                <h3 class="text-sm font-bold text-gray-900 mb-3">${moduleName}</h3>
                                <div class="grid grid-cols-1 md:grid-cols-2 gap-2">
                                    @for(Permission p : permissions)
                                        !{
                                            boolean hasPermission = role.hasPermission(p.getName());
                                        }
                                        <label class="flex items-center gap-2 cursor-pointer">
                                            <input type="checkbox"
                                                   name="permissionIds"
                                                   value="${p.getId()}"
                                                   checked="${hasPermission}"
                                                   class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded">
                                            <span class="text-sm text-gray-700">${p.getName()}</span>
                                        </label>
                                    @endfor
                                </div>
                            </div>
                        @endfor
                    @endif
                </div>
            </div>

            <!-- Submit -->
            <div class="pt-4 border-t border-gray-200 flex items-center justify-end gap-3">
                <a href="/admin/roles"
                   class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 transition-colors">
                    Hủy
                </a>
                <button type="submit"
                        class="px-6 py-2 bg-indigo-600 text-white rounded-md text-sm font-medium hover:bg-indigo-700 shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                    ${isEdit ? "Cập nhật vai trò" : "Lưu vai trò"}
                </button>
            </div>
        </form>
    </div>
`
)
