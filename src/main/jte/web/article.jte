@import dev.danvega.model.Article

@param Article article

@template.layout.default(
content = @`
    <div class="min-h-screen bg-gray-100">
        <nav class="bg-white shadow-sm">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between h-16">
                    <div class="flex items-center">
                        <h1 class="text-xl font-semibold">My Blog</h1>
                    </div>
                    <div class="flex items-center space-x-4">
                        <a href="/"
                           class="text-gray-700 hover:text-gray-900">Home</a>
                    </div>
                </div>
            </div>
        </nav>
        <main class="max-w-4xl mx-auto py-10 sm:px-6 lg:px-8">
            <div class="px-4 sm:px-0">
                <article class="bg-white shadow rounded-lg p-8">
                    <h1 class="text-3xl font-bold text-gray-900 mb-4">${article.getTitle()}</h1>
                    <div class="flex items-center text-sm text-gray-500 mb-6">
                        <span>By ${article.getAuthor()}</span>
                        <span class="mx-2">·</span>
                        <span>${article.getCreatedAt() != null ? article.getCreatedAt().toString() : ""}</span>
                    </div>
                    <div class="prose max-w-none text-gray-700">
                        ${article.getContent()}
                    </div>
                    <div class="mt-8 pt-6 border-t border-gray-200">
                        <a href="/"
                           class="text-indigo-600 hover:text-indigo-900 font-medium">
                            ← Back to articles
                        </a>
                    </div>
                </article>
            </div>
        </main>
    </div>
`)
