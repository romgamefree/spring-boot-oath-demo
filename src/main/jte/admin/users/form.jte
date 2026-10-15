@import dev.danvega.config.CsrfHiddenInput
@import dev.danvega.model.Permission
@import dev.danvega.model.Role
@import dev.danvega.model.User

@param User user
@param java.util.List<Role> roles = null
@param CsrfHiddenInput csrfHiddenInput = null

!{
    var csrf = csrfHiddenInput != null ? csrfHiddenInput : dev.danvega.config.CsrfHiddenInput.get();
    boolean isEdit = user.getId() != null;
}

@template.layout.admin(
activePage = "users",
content = @`
    <div class="mb-6">
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-2xl font-bold text-gray-900">
                    ${isEdit ? "Chỉnh sửa người dùng" : "Thêm người dùng mới"}
                </h1>
                <p class="text-sm text-gray-500 mt-1">
                    ${isEdit ? "Cập nhật thông tin tài khoản, mật khẩu và vai trò phân quyền" : "Tạo tài khoản người dùng và thiết lập vai trò phân quyền"}
                </p>
            </div>
            <a href="/admin/users"
               class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 transition-colors">
                Quay lại danh sách
            </a>
        </div>
    </div>

    <div class="bg-white shadow rounded-lg p-6 max-w-3xl">
        <form action="${isEdit ? "/admin/users/" + user.getId() : "/admin/users"}"
              method="post"
              class="space-y-6">

            ${csrf}

            @if(isEdit)
                <input type="hidden" name="_method" value="put">
            @endif

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <!-- Username -->
                <div>
                    <label for="username" class="block text-sm font-medium text-gray-700">
                        Tên đăng nhập (Username) <span class="text-red-500">*</span>
                    </label>
                    <input type="text"
                           id="username"
                           name="username"
                           value="${user.getUsername() != null ? user.getUsername() : ""}"
                           required
                           minlength="3"
                           maxlength="50"
                           readonly="${isEdit}"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 ${isEdit ? "bg-gray-100 text-gray-500 cursor-not-allowed" : ""}"
                           placeholder="vd: nguyenvana">
                    @if(isEdit)
                        <p class="mt-1 text-xs text-gray-400">Username không thể thay đổi sau khi tạo</p>
                    @endif
                </div>

                <!-- Full Name -->
                <div>
                    <label for="fullName" class="block text-sm font-medium text-gray-700">
                        Họ và tên
                    </label>
                    <input type="text"
                           id="fullName"
                           name="fullName"
                           value="${user.getFullName() != null ? user.getFullName() : ""}"
                           maxlength="100"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="vd: Nguyễn Văn A">
                </div>

                <!-- Email -->
                <div>
                    <label for="email" class="block text-sm font-medium text-gray-700">
                        Địa chỉ Email
                    </label>
                    <input type="email"
                           id="email"
                           name="email"
                           value="${user.getEmail() != null ? user.getEmail() : ""}"
                           maxlength="100"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="vd: a.nguyen@example.com">
                </div>

                <!-- Password -->
                <div>
                    <label for="password" class="block text-sm font-medium text-gray-700">
                        Mật khẩu ${isEdit ? "(để trống nếu không đổi)" : ""} @if(!isEdit)<span class="text-red-500">*</span>@endif
                    </label>
                    <input type="password"
                           id="password"
                           name="password"
                           required="${!isEdit}"
                           minlength="6"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="${isEdit ? "•••••••• (bỏ trống nếu giữ nguyên)" : "Tối thiểu 6 ký tự"}">
                </div>
            </div>

            <!-- Status Checkbox -->
            <div class="pt-2 border-t border-gray-200">
                <label class="flex items-center gap-3">
                    <input type="checkbox"
                           name="enabled"
                           value="true"
                           checked="${user.getId() == null || Boolean.TRUE.equals(user.getEnabled())}"
                           class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded">
                    <span class="text-sm font-medium text-gray-800">Kích hoạt tài khoản</span>
                </label>
                <p class="text-xs text-gray-500 ml-7 mt-0.5">Tài khoản bị vô hiệu hóa sẽ không thể đăng nhập vào hệ thống</p>
            </div>

            <!-- Roles Selection -->
            <div class="pt-4 border-t border-gray-200">
                <label class="block text-sm font-semibold text-gray-900 mb-2">
                    Vai trò & Quyền hạn (Roles & Permissions) <span class="text-red-500">*</span>
                </label>
                <p class="text-xs text-gray-500 mb-4">Chọn ít nhất một vai trò cho người dùng. Mỗi vai trò đi kèm tập quyền hạn tương ứng:</p>

                <div class="space-y-4">
                    @if(roles != null)
                        @for(Role r : roles)
                            !{
                                boolean hasThisRole = user.hasRole(r.getName());
                            }
                            <div class="border rounded-lg p-4 hover:border-indigo-300 transition-colors bg-gray-50">
                                <label class="flex items-start gap-3 cursor-pointer">
                                    <input type="checkbox"
                                           name="roleIds"
                                           value="${r.getId()}"
                                           checked="${hasThisRole}"
                                           class="mt-1 h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded">
                                    <div class="flex-1">
                                        <div class="flex items-center gap-2">
                                            <span class="text-sm font-bold text-gray-900">${r.getName()}</span>
                                            <span class="text-xs text-gray-500">— ${r.getDescription() != null ? r.getDescription() : ""}</span>
                                        </div>

                                        @if(r.getPermissions() != null && !r.getPermissions().isEmpty())
                                            <div class="mt-2 flex flex-wrap gap-1">
                                                @for(Permission p : r.getPermissions())
                                                    <span class="inline-flex items-center px-2 py-0.5 rounded text-xs bg-white text-gray-600 border border-gray-200">
                                                        ${p.getName()}
                                                    </span>
                                                @endfor
                                            </div>
                                        @endif
                                    </div>
                                </label>
                            </div>
                        @endfor
                    @endif
                </div>
            </div>

            <!-- Submit -->
            <div class="pt-4 border-t border-gray-200 flex items-center justify-end gap-3">
                <a href="/admin/users"
                   class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 transition-colors">
                    Hủy
                </a>
                <button type="submit"
                        class="px-6 py-2 bg-indigo-600 text-white rounded-md text-sm font-medium hover:bg-indigo-700 shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                    ${isEdit ? "Cập nhật người dùng" : "Lưu người dùng"}
                </button>
            </div>
        </form>
    </div>
`
)
