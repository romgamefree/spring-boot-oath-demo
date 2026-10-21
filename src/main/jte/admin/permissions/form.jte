@import dev.danvega.config.CsrfHiddenInput
@import dev.danvega.model.Permission

@param Permission permission
@param CsrfHiddenInput csrfHiddenInput

!{
    var csrf = csrfHiddenInput != null ? csrfHiddenInput : dev.danvega.config.CsrfHiddenInput.get();
    boolean isEdit = permission.getId() != null;
}

@template.layout.admin(
activePage = "permissions",
content = @`
    <div class="mb-6">
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-2xl font-bold text-gray-900">
                    ${isEdit ? "Chỉnh sửa quyền" : "Thêm quyền mới"}
                </h1>
                <p class="text-sm text-gray-500 mt-1">
                    ${isEdit ? "Cập nhật thông tin quyền" : "Tạo quyền mới trong hệ thống"}
                </p>
            </div>
            <a href="/admin/permissions"
               class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 transition-colors">
                Quay lại danh sách
            </a>
        </div>
    </div>

    <div class="bg-white shadow rounded-lg p-6 max-w-3xl">
        <form action="${isEdit ? "/admin/permissions/" + permission.getId() : "/admin/permissions"}"
              method="post"
              class="space-y-6">

            ${csrf}

            @if(isEdit)
                <input type="hidden" name="_method" value="put">
            @endif

            <div class="grid grid-cols-1 gap-6">
                <!-- Permission Name -->
                <div>
                    <label for="name" class="block text-sm font-medium text-gray-700">
                        Tên quyền (Permission Name) <span class="text-red-500">*</span>
                    </label>
                    <input type="text"
                           id="name"
                           name="name"
                           value="${permission.getName() != null ? permission.getName() : ""}"
                           required
                           minlength="3"
                           maxlength="100"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="vd: article:create">
                </div>

                <!-- Module -->
                <div>
                    <label for="module" class="block text-sm font-medium text-gray-700">
                        Module <span class="text-red-500">*</span>
                    </label>
                    <input type="text"
                           id="module"
                           name="module"
                           value="${permission.getModule() != null ? permission.getModule() : ""}"
                           required
                           maxlength="50"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="vd: articles, users, pages">
                </div>

                <!-- Description -->
                <div>
                    <label for="description" class="block text-sm font-medium text-gray-700">
                        Mô tả
                    </label>
                    <textarea id="description"
                              name="description"
                              rows="3"
                              maxlength="255"
                              class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
                              placeholder="Mô tả chức năng của quyền này">${permission.getDescription() != null ? permission.getDescription() : ""}</textarea>
                </div>
            </div>

            <!-- Submit -->
            <div class="pt-4 border-t border-gray-200 flex items-center justify-end gap-3">
                <a href="/admin/permissions"
                   class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 transition-colors">
                    Hủy
                </a>
                <button type="submit"
                        class="px-6 py-2 bg-indigo-600 text-white rounded-md text-sm font-medium hover:bg-indigo-700 shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                    ${isEdit ? "Cập nhật quyền" : "Lưu quyền"}
                </button>
            </div>
        </form>
    </div>
`
)
