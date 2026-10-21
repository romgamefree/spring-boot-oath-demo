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
        <div class="flex justify-between items-center">
            <h1 class="text-2xl font-bold text-gray-900">
                ${isEdit ? "Edit" : "New"} Role
            </h1>

            <a href="/admin/roles"
               class="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700">
                Back to List
            </a>
        </div>
    </div>

    <div class="bg-white shadow rounded-lg p-6">
        <form action="${isEdit ? "/admin/roles/" + role.getId() : "/admin/roles"}"
              method="post">
            ${csrf}

            @if(isEdit)
                <input type="hidden" name="_method" value="put">
            @endif

            <div class="space-y-6">
                <div>
                    <label for="name" class="block text-sm font-medium text-gray-700">
                        Name
                    </label>
                    @if(isEdit)
                        <input type="text"
                               id="name"
                               name="name"
                               value="${role.getName() != null ? role.getName() : ""}"
                               required
                               readonly
                               class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 bg-gray-100 text-gray-500 cursor-not-allowed">
                    @else
                        <input type="text"
                               id="name"
                               name="name"
                               value="${role.getName() != null ? role.getName() : ""}"
                               required
                               class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                    @endif
                </div>

                <div>
                    <label for="description" class="block text-sm font-medium text-gray-700">
                        Description
                    </label>
                    <input type="text"
                           id="description"
                           name="description"
                           value="${role.getDescription() != null ? role.getDescription() : ""}"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                </div>

                <div>
                    <label class="block text-sm font-semibold text-gray-900 mb-2">
                        Permissions
                    </label>
                    <p class="text-xs text-gray-500 mb-4">Select permissions for this role:</p>

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

                <div>
                    <button type="submit"
                            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        ${isEdit ? "Update" : "Create"} Role
                    </button>
                </div>
            </div>
        </form>
    </div>
`
)
