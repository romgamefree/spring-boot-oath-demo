@import dev.danvega.config.CsrfHiddenInput
@import dev.danvega.model.Permission
@import dev.danvega.model.Role

@param java.util.List<Role> roles
@param java.util.Map<String, java.util.List<Permission>> permissionsByModule
@param String search = ""
@param String module = ""
@param CsrfHiddenInput csrfHiddenInput

@template.layout.admin(
activePage = "roles",
content = @`
    <div class="mb-6">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <div>
                <h1 class="text-2xl font-bold text-gray-900">Quản lý Vai trò (Roles)</h1>
                <p class="text-sm text-gray-500 mt-1">Quản lý vai trò và gán quyền hạn cho từng vai trò trong hệ thống</p>
            </div>
            <a href="/admin/roles/new"
               class="inline-flex items-center px-4 py-2 bg-indigo-600 text-white text-sm font-medium rounded-md hover:bg-indigo-700 shadow-sm transition-colors">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                </svg>
                Thêm vai trò mới
            </a>
        </div>
    </div>

    <!-- Table Container -->
    <div id="roles-table" class="bg-white shadow rounded-lg overflow-hidden">
        @template.admin.roles.table-rows(
            roles = roles,
            permissionsByModule = permissionsByModule,
            search = search,
            module = module,
            csrfHiddenInput = csrfHiddenInput
        )
    </div>
`
)
