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
        <div class="flex justify-between items-center mb-4">
            <h1 class="text-2xl font-bold text-gray-900">Roles</h1>
            <a href="/admin/roles/new"
               class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700">
                Add New
            </a>
        </div>
    </div>

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
