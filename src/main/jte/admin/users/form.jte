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
        <div class="flex justify-between items-center">
            <h1 class="text-2xl font-bold text-gray-900">
                ${isEdit ? "Edit" : "New"} User
            </h1>

            <a href="/admin/users"
               class="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700">
                Back to List
            </a>
        </div>
    </div>

    <div class="bg-white shadow rounded-lg p-6">
        <form action="${isEdit ? "/admin/users/" + user.getId() : "/admin/users"}"
              method="post">
            ${csrf}

            @if(isEdit)
                <input type="hidden" name="_method" value="put">
            @endif

            <div class="space-y-6">
                <div>
                    <label for="username" class="block text-sm font-medium text-gray-700">
                        Username
                    </label>
                    @if(isEdit)
                        <input type="text"
                               id="username"
                               name="username"
                               value="${user.getUsername() != null ? user.getUsername() : ""}"
                               required
                               readonly
                               class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 bg-gray-100 text-gray-500 cursor-not-allowed">
                    @else
                        <input type="text"
                               id="username"
                               name="username"
                               value="${user.getUsername() != null ? user.getUsername() : ""}"
                               required
                               class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                    @endif
                </div>

                <div>
                    <label for="fullName" class="block text-sm font-medium text-gray-700">
                        Full Name
                    </label>
                    <input type="text"
                           id="fullName"
                           name="fullName"
                           value="${user.getFullName() != null ? user.getFullName() : ""}"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                </div>

                <div>
                    <label for="email" class="block text-sm font-medium text-gray-700">
                        Email
                    </label>
                    <input type="email"
                           id="email"
                           name="email"
                           value="${user.getEmail() != null ? user.getEmail() : ""}"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                </div>

                <div>
                    <label for="password" class="block text-sm font-medium text-gray-700">
                        Password ${isEdit ? "(leave blank to keep current)" : ""}
                    </label>
                    <input type="password"
                           id="password"
                           name="password"
                           required="${!isEdit}"
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="${isEdit ? "••••••••" : "Minimum 6 characters"}">
                </div>

                <div>
                    <label class="flex items-center">
                        <input type="checkbox"
                               name="enabled"
                               checked="${user.getId() == null || Boolean.TRUE.equals(user.getEnabled())}"
                               class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded">
                        <span class="ml-2 text-sm text-gray-700">Enabled</span>
                    </label>
                </div>

                <div>
                    <label class="block text-sm font-semibold text-gray-900 mb-2">
                        Roles
                    </label>
                    <p class="text-xs text-gray-500 mb-4">Select roles for this user:</p>

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

                <div>
                    <button type="submit"
                            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        ${isEdit ? "Update" : "Create"} User
                    </button>
                </div>
            </div>
        </form>
    </div>
`
)
