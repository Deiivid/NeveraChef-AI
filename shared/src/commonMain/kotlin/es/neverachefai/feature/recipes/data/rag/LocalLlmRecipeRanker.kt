package es.neverachefai.feature.recipes.data.rag

internal class LocalLlmRecipeRanker {
    fun rerank(
        matches: List<RecipeMatch>,
        llmText: String?,
    ): List<RecipeMatch> {
        if (matches.size < 2 || llmText.isNullOrBlank()) return matches

        val byId = matches.associateBy { it.card.id }
        val orderedIds = byId.keys
            .filter { id -> llmText.contains("\"$id\"") || llmText.contains(id) }
            .sortedBy { id -> llmText.indexOf(id).takeIf { it >= 0 } ?: Int.MAX_VALUE }
            .distinct()

        if (orderedIds.isEmpty()) return matches

        val ordered = orderedIds.mapNotNull(byId::get)
        val remaining = matches.filterNot { match -> match.card.id in orderedIds }
        return ordered + remaining
    }
}
