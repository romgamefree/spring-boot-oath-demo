@import dev.danvega.model.Article
@import java.util.List

@param List<Article> articles

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
        <main class="max-w-7xl mx-auto py-10 sm:px-6 lg:px-8">
            <div class="px-4 sm:px-0">
                <div class="mb-8">
                    <h2 class="text-3xl font-bold text-gray-900">Latest Articles</h2>
                </div>
                @if(articles == null || articles.isEmpty())
                    <div class="text-center py-10">
                        <p class="text-gray-500">No articles published yet.</p>
                    </div>
                @else
                    <div class="grid gap-6">
                        @for(var article : articles)
                            <div class="bg-white shadow rounded-lg p-6 hover:shadow-lg transition-shadow">
                                <h3 class="text-xl font-semibold text-gray-900 mb-2">
                                    <a href="/articles/${article.getSlug()}" class="hover:text-indigo-600">
                                        ${article.getTitle()}
                                    </a>
                                </h3>
                                <p class="text-sm text-gray-500 mb-4">
                                    By ${article.getAuthor()} · ${article.getCreatedAt() != null ? article.getCreatedAt().toString() : ""}
                                </p>
                                @if(article.getContent() != null && !article.getContent().isEmpty())
                                    <p class="text-gray-600 line-clamp-3">
                                        ${article.getContent().substring(0, Math.min(200, article.getContent().length()))}...
                                    </p>
                                @endif
                                <a href="/articles/${article.getSlug()}"
                                   class="inline-block mt-4 text-indigo-600 hover:text-indigo-900 font-medium">
                                    Read more →
                                </a>
                            </div>
                        @endfor
                    </div>
                @endif
            </div>
        </main>
    </div>
`)
