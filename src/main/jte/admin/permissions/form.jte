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
        <div class="flex justify-between items-center">
            <h1 class="text-2xl font-bold text-gray-900">
                ${isEdit ? "Edit" : "New"} Permission
            </h1>

            <a href="/admin/permissions"
               class="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700">
                Back to List
            </a>
        </div>
    </div>

    <div class="bg-white shadow rounded-lg p-6">
        <form action="${isEdit ? "/admin/permissions/" + permission.getId() : "/admin/permissions"}"
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
                    <input type="text"
                           id="name"
                           name="name"
                           value="${permission.getName() != null ? permission.getName() : ""}"
                           required
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                </div>

                <div>
                    <label for="module" class="block text-sm font-medium text-gray-700">
                        Module
                    </label>
                    <input type="text"
                           id="module"
                           name="module"
                           value="${permission.getModule() != null ? permission.getModule() : ""}"
                           required
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                </div>

                <div>
                    <label for="description" class="block text-sm font-medium text-gray-700">
                        Description
                    </label>
                    <textarea id="description"
                              name="description"
                              rows="3"
                              class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">${permission.getDescription() != null ? permission.getDescription() : ""}</textarea>
                </div>

                <div>
                    <button type="submit"
                            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        ${isEdit ? "Update" : "Create"} Permission
                    </button>
                </div>
            </div>
        </form>
    </div>
`
)
