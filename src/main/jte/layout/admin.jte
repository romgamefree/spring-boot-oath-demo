@import gg.jte.Content
@import dev.danvega.common.web.FlashMessage

@param gg.jte.Content content
@param String activePage = ""
@param FlashMessage flash = null

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin CMS</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="//unpkg.com/alpinejs" defer></script>
    <script src="https://unpkg.com/htmx.org@1.9.10"></script>
    <style>
        [x-cloak] {
            display: none !important;
        }
    </style>
</head>
<body class="bg-gray-100" x-data="{ sidebarOpen: false }">
    <div class="flex h-screen">

        <div    class="fixed inset-0 z-40 bg-black/50 lg:hidden"
                x-cloak
                x-show="sidebarOpen"
                @click="sidebarOpen = false"
        >
        </div>

        <!-- Sidebar -->
        <aside class="fixed inset-y-0 left-0 z-50 w-64 bg-gray-900 transform transition-transform duration-300 ease-in-out lg:translate-x-0 lg:static lg:inset-0"
               :class="sidebarOpen ? 'translate-x-0' : '-translate-x-full'">
            <div class="flex flex-col h-full">

                <div class="flex items-center justify-between h-16 px-4 bg-gray-800">
                    <h1 class="text-xl font-bold text-white">CMS Admin</h1>

                    <button
                            type="button"
                            @click="sidebarOpen = false"
                            class="lg:hidden p-2 text-gray-300 hover:text-white"
                            aria-label="Close sidebar">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor"
                             viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                  stroke-width="2" d="M6 18L18 6M6 6l12 12">
                            </path>
                        </svg>
                    </button>
                </div>

                <nav class="flex-1 px-2 py-4 space-y-2">
                    <a href="/admin"
                       class="flex items-center px-4 py-2 text-gray-300 hover:bg-gray-700 hover:text-white rounded-md ${activePage == "dashboard" ? "bg-gray-700 text-white" : ""}">
                        <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path>
                        </svg>
                        Dashboard
                    </a>
                    <a href="/admin/articles"
                       class="flex items-center px-4 py-2 text-gray-300 hover:bg-gray-700 hover:text-white rounded-md ${activePage == "articles" ? "bg-gray-700 text-white" : ""}">
                        <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z"></path>
                        </svg>
                        Articles
                    </a>
                    <a href="/admin/pages"
                       class="flex items-center px-4 py-2 text-gray-300 hover:bg-gray-700 hover:text-white rounded-md ${activePage == "pages" ? "bg-gray-700 text-white" : ""}">
                        <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                        </svg>
                        Pages
                    </a>
                    @if (dev.danvega.security.SecurityUtils.hasRole("ROLE_ADMIN"))
                    <a href="/admin/users"
                       class="flex items-center px-4 py-2 text-gray-300 hover:bg-gray-700 hover:text-white rounded-md ${activePage == "users" ? "bg-gray-700 text-white" : ""}">
                        <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path>
                        </svg>
                        Users
                    </a>
                    <a href="/admin/roles"
                       class="flex items-center px-4 py-2 text-gray-300 hover:bg-gray-700 hover:text-white rounded-md ${activePage == "roles" ? "bg-gray-700 text-white" : ""}">
                        <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
                        </svg>
                        Roles
                    </a>
                    <a href="/admin/permissions"
                       class="flex items-center px-4 py-2 text-gray-300 hover:bg-gray-700 hover:text-white rounded-md ${activePage == "permissions" ? "bg-gray-700 text-white" : ""}">
                        <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path>
                        </svg>
                        Permissions
                    </a>
                    <a href="/admin/settings"
                       class="flex items-center px-4 py-2 text-gray-300 hover:bg-gray-700 hover:text-white rounded-md ${activePage == "settings" ? "bg-gray-700 text-white" : ""}">
                        <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path>
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                        </svg>
                        Settings
                    </a>
                    @endif
                </nav>
                <div class="p-4 border-t border-gray-800">
                    <form action="/logout" method="post">
                        ${dev.danvega.config.CsrfHiddenInput.get()}
                        <button type="submit"
                                class="w-full flex items-center justify-center px-4 py-2 text-sm font-medium text-white bg-red-600 rounded-md hover:bg-red-700">
                            Logout
                        </button>
                    </form>
                </div>
            </div>
        </aside>

        <!-- Main content -->
        <div class="flex-1 flex flex-col lg:ml-0">
            <!-- Mobile header -->
            <div class="lg:hidden flex items-center justify-between h-16 bg-white shadow">
                <button @click="sidebarOpen = !sidebarOpen" class="p-4 text-gray-500 hover:text-gray-700">
                    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
                    </svg>
                </button>
                <h1 class="text-xl font-semibold">CMS Admin</h1>
                <div class="w-12"></div>
            </div>

            <!-- Content -->
            <main class="flex-1 overflow-y-auto p-6">
                <!-- Toast container -->
                !{ var currentFlash = flash != null ? flash : dev.danvega.common.web.FlashMessage.getCurrent(); }
                <div id="toast-area" class="fixed top-4 right-4 z-50 flex flex-col gap-2 pointer-events-none">
                    @if (currentFlash != null)
                        @template.fragments.toast(message = currentFlash.message(), type = currentFlash.typeString())
                    @endif
                </div>

                @template.fragments.delete-confirm-modal()

                ${content}
            </main>
        </div>
    </div>
</body>
</html>
