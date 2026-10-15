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

                ${content}
            </main>
        </div>
    </div>
</body>
</html>
