# NeveraChef — Sistema de recetas local + RAG + fallback LLM

## Objetivo

Implementar una base local de recetas para NeveraChef, orientada a inventario doméstico, lista de compra y recomendación de comidas según los alimentos disponibles.

La app no debe comportarse como una web genérica de recetas. Debe responder principalmente a:

> “¿Qué puedo cocinar hoy con lo que tengo?”

## Principios del sistema

- Las recetas viven en una base local estructurada.
- El RAG se usa para encontrar recetas candidatas.
- El LLM solo debe usarse como fallback cuando no haya recetas adecuadas.
- Los cálculos de raciones, coincidencia de ingredientes, dificultad, tiempo y filtros deben hacerse por código, no por LLM.
- Las recetas deben estar redactadas de forma propia, sin copiar textos de webs externas.
- No usar imágenes de terceros salvo licencia explícita.

## Flujo funcional

text Usuario   ↓ Inventario local   ↓ Motor de búsqueda / RAG local   ↓ Recetas candidatas   ↓ Motor local de scoring   ├─ Coincidencia con inventario   ├─ Ingredientes disponibles   ├─ Ingredientes faltantes   ├─ Alimentos próximos a caducar   ├─ Tiempo   ├─ Dificultad   └─ Preferencias del usuario   ↓ UI de recetas recomendadas

Si no hay coincidencias suficientes:

text RAG sin resultados adecuados   ↓ Fallback LLM   ↓ Generar receta adaptada

## Modelo de datos sugerido

kotlin data class Recipe(     val id: String,     val name: String,     val category: RecipeCategory,     val baseServings: Int,     val preparationTimeMinutes: Int,     val difficulty: Difficulty,     val tags: List<RecipeTag>,     val ingredients: List<RecipeIngredient>,     val steps: List<RecipeStep> )  data class RecipeIngredient(     val foodId: String,     val name: String,     val quantity: Double,     val unit: IngredientUnit,     val optional: Boolean = false )  data class RecipeStep(     val order: Int,     val title: String,     val instruction: String,     val tip: String? = null,     val estimatedMinutes: Int? = null )  enum class Difficulty {     EASY,     MEDIUM,     HARD }

## Reglas de preparación

Cada paso debe ser claro, breve y accionable.

Correcto:

text 1. Pica la cebolla. 2. Sofríe la cebolla durante 5 minutos. 3. Añade el tomate. 4. Cocina 5 minutos. 5. Incorpora los garbanzos.

Evitar pasos largos con demasiadas acciones mezcladas.

Incorrecto:

text Pica la cebolla, sofríela, añade el tomate, remueve, cocina y después añade los garbanzos.

## Scoring de recetas

El match no debe calcularlo el LLM.

Debe calcularse por código:

text match = ingredientesDisponibles / ingredientesRequeridos

Ejemplo:

text Receta: Garbanzos con espinacas y tomate  Tienes: - Garbanzos - Espinacas - Tomate  Faltan: - Cebolla - Aceite de oliva  Match aproximado: 60%

También se debe priorizar:

1. Ingredientes disponibles.
2. Ingredientes próximos a caducar.
3. Tiempo máximo configurado.
4. Dificultad configurada.
5. Preferencias del usuario.
6. Categoría: desayuno, comida, cena, niños, saludable, rápida.

## Recalcular raciones

Las recetas deben tener baseServings.

Ejemplo:

text baseServings = 4 400 g garbanzos 100 g espinacas

Si el usuario selecciona 2 personas:

text 200 g garbanzos 50 g espinacas

Esto debe hacerlo el código mediante regla de tres.

## Categorías iniciales

Crear una base inicial con recetas mediterráneas y familiares.

Categorías recomendadas:

- Desayunos
- Ensaladas
- Pastas
- Arroces
- Pollo
- Cerdo
- Ternera
- Pescado
- Legumbres
- Cenas rápidas
- Niños
- Verano
- Invierno
- Saludables

## Recetas iniciales

### Desayunos

- Tostada con tomate
- Tostada con aceite
- Tostada con jamón
- Tostada con aguacate
- Tostada con anchoas
- Tostada con queso fresco
- Tostada con pavo
- Tostada con pechuga de pollo
- Tostada con mermelada
- Yogur natural con fruta
- Yogur con avena
- Yogur con muesli
- Yogur con frutos secos
- Avena con plátano
- Avena con manzana
- Macedonia
- Tortilla francesa
- Tortilla francesa con queso
- Huevos revueltos
- Huevos revueltos con pavo
- Pan con aceite y fruta
- Queso fresco con tomate
- Batido de plátano y yogur
- Porridge básico
- Fruta con yogur

### Ensaladas

- Ensalada mixta
- Ensalada de pasta
- Ensalada de arroz
- Ensalada campera
- Ensalada César
- Ensalada de garbanzos
- Ensalada de lentejas
- Ensalada de atún
- Ensalada de tomate
- Ensalada de pepino
- Ensalada de tomate, pepino y aguacate
- Ensalada de pollo
- Ensalada de huevo cocido
- Ensalada de queso fresco
- Ensalada de salmón
- Ensalada griega
- Ensalada de pasta con atún
- Ensalada de arroz con gambas
- Ensalada templada de verduras
- Ensalada de tomate y mozzarella

### Pastas

- Espaguetis boloñesa
- Espaguetis carbonara
- Espaguetis con tomate
- Espaguetis con atún
- Espaguetis con gambas
- Espaguetis con gulas y gambas
- Espaguetis al ajillo
- Espaguetis con pollo
- Espaguetis con verduras
- Espaguetis con salmón
- Macarrones con tomate
- Macarrones con atún y tomate
- Macarrones boloñesa
- Macarrones carbonara
- Macarrones con gulas y gambas
- Macarrones con chorizo
- Macarrones con pollo
- Macarrones con verduras
- Tallarines con gambas
- Tallarines con pollo
- Pasta al pesto
- Pasta con nata y champiñones
- Pasta con espinacas
- Pasta con queso
- Pasta gratinada
- Lasaña boloñesa
- Lasaña de verduras
- Canelones de carne
- Canelones de atún
- Fideos con pollo

### Arroces

- Arroz con tomate
- Arroz a la cubana
- Arroz tres delicias
- Arroz con pollo
- Arroz con verduras
- Arroz con gambas
- Arroz con atún
- Arroz con ternera
- Arroz con carne picada
- Arroz con salchichas
- Arroz con conejo
- Arroz con costillas
- Arroz caldoso de pollo
- Arroz caldoso de marisco
- Arroz meloso con verduras
- Paella mixta
- Paella de marisco
- Paella de pollo
- Paella de verduras
- Fideuá
- Risotto sencillo de champiñones
- Arroz al horno sencillo

### Pollo

- Pollo al horno con patatas
- Pollo al horno con cebolla
- Pollo al ajillo
- Pollo al limón
- Pollo a la plancha
- Pechuga con cebolla
- Pechuga con pimientos verdes
- Pechuga con verduras
- Pechuga empanada
- Nuggets caseros
- Alitas al horno
- Alitas fritas
- Muslos de pollo al horno
- Pollo con arroz
- Pollo con tomate
- Pollo con champiñones
- Pollo al curry suave
- Pollo guisado con patatas
- Ensalada de pollo
- Hamburguesa de pollo

### Cerdo

- Lomo a la plancha
- Lomo con pimientos
- Lomo al ajillo
- Libritos de lomo
- Costillas al horno
- Costillas guisadas
- Solomillo de cerdo
- Solomillo con champiñones
- Salchichas con cebolla y vino
- Longaniza con cebolla
- Chorizo al vino
- Magra con tomate
- Chuletas de cerdo
- Cinta de lomo al horno
- Bocaditos de lomo empanado
- Arroz con costillas
- Pasta con chorizo
- Huevos rotos con chorizo

### Ternera

- Ternera a la plancha
- Ternera al ajillo
- Ternera con pimientos
- Ternera con cebolla
- Ternera guisada
- Estofado de ternera
- Filetes empanados
- Albóndigas con tomate
- Hamburguesa casera
- Carne picada con tomate
- Arroz con ternera
- Macarrones boloñesa
- Lasaña boloñesa
- Ternera con champiñones
- Ternera salteada con verduras

### Pescado

- Merluza rebozada
- Merluza al horno
- Merluza a la plancha
- Merluza en salsa verde
- Lubina al horno con patatas
- Lubina a la plancha
- Dorada al horno con patatas
- Dorada a la plancha con limón
- Salmón a la plancha
- Salmón al horno
- Bacalao con tomate
- Bacalao al horno
- Atún a la plancha
- Bonito con tomate
- Sardinas al horno
- Sepia a la plancha
- Calamares a la romana
- Gambas al ajillo
- Gulas con gambas
- Arroz con gambas
- Pasta con gambas
- Ensalada de atún

### Legumbres

- Lentejas con chorizo
- Lentejas con verduras
- Lentejas con arroz
- Lentejas con patata y chorizo
- Garbanzos con espinacas
- Garbanzos con tomate
- Garbanzos con chorizo
- Potaje de garbanzos
- Garbanzos salteados
- Ensalada de garbanzos
- Alubias con verduras
- Alubias con chorizo
- Judías blancas guisadas
- Fabada sencilla
- Hummus casero
- Lentejas rápidas

### Cenas rápidas

- Tortilla francesa
- Revuelto de huevos
- Ensalada de atún
- Ensalada de pollo
- Sándwich mixto
- Tostada de aguacate y huevo
- Quesadillas
- Hamburguesa casera
- Pizza casera rápida
- Crema de verduras
- Sopa de fideos
- Pechuga a la plancha
- Merluza a la plancha
- Dorada a la plancha
- Arroz con tomate
- Pasta con atún
- Verduras salteadas
- Gulas con gambas
- Huevos con tomate
- Yogur con fruta y frutos secos

### Niños

- Macarrones con tomate
- Macarrones con atún
- Arroz con tomate
- Arroz a la cubana
- Nuggets caseros
- Hamburguesa casera
- Albóndigas con tomate
- Croquetas
- Tortilla francesa
- Pizza casera
- Pechuga empanada
- Lasaña boloñesa
- Canelones
- Salchichas con arroz
- Sopa de fideos
- Puré de verduras
- Pescado rebozado
- Patatas con huevo

## Pantallas

### Pantalla de recetas

Debe mostrar:

- Recetas recomendadas según inventario.
- Filtros rápidos:
    - Recomendadas
    - Rápidas
    - Desayuno
    - Cena
    - Niños
    - Saludables
- Cards con:
    - Imagen
    - Nombre
    - Tiempo
    - Dificultad
    - Ingredientes principales usados
    - CTA “Ver receta”

### Detalle de receta

Debe mostrar:

- Nombre
- Tiempo
- Raciones
- Dificultad
- Ingredientes disponibles
- Ingredientes faltantes
- Preparación paso a paso
- Opción para cambiar raciones
- Opción para añadir ingredientes faltantes a la lista de compra

## Regla importante

No usar el LLM para:

- Calcular raciones.
- Calcular match.
- Filtrar por tiempo.
- Filtrar por dificultad.
- Ordenar recetas.
- Saber qué ingredientes faltan.

Eso debe hacerlo código local.

Usar LLM solo para:

- Fallback cuando no haya receta.
- Adaptar una receta existente.
- Sugerir sustituciones.
- Reescribir instrucciones en un tono más sencillo.
- Generar una receta nueva si el usuario lo pide explícitamente.
# NeveraChef — Sistema inteligente de recetas local + RAG + fallback LLM

## Objetivo del documento

Este documento define cómo debe funcionar el sistema de recetas de NeveraChef.

NeveraChef no debe ser una app genérica de recetas. Su valor diferencial es combinar:

- Inventario doméstico.
- Lista de compra.
- Recetas locales estructuradas.
- Recomendación inteligente.
- RAG local.
- Fallback LLM solo cuando aporte valor real.

La pregunta principal que debe responder la app es:

> “¿Qué puedo cocinar hoy con lo que tengo en casa?”

La app debe priorizar utilidad real, rapidez, claridad y ahorro. No debe mostrar cientos de recetas irrelevantes. Debe mostrar pocas recetas, bien ordenadas y justificadas.

---

## Visión de producto

El usuario abre la pantalla de recetas y debe entender rápidamente:

- Qué recetas puede hacer ya.
- Qué recetas puede hacer casi completas.
- Qué ingredientes le faltan.
- Qué alimentos conviene gastar antes.
- Qué receta encaja mejor con su tiempo disponible.
- Qué receta encaja mejor con desayuno, comida, cena, niños, saludable o rápida.

Ejemplo:

```text
Tienes:
- Garbanzos
- Espinacas
- Tomate
- Huevos
- Arroz

La app recomienda:
1. Garbanzos con espinacas y tomate
2. Ensalada de garbanzos
3. Arroz a la cubana
```

La app no debe limitarse a buscar por nombre. Debe razonar con datos estructurados.

---

## Principios fundamentales

1. Las recetas deben vivir en una base local estructurada.
2. El RAG debe usarse para encontrar recetas candidatas, no para calcular reglas de negocio.
3. El LLM solo debe usarse como fallback o asistente de adaptación.
4. El cálculo de match, raciones, filtros, dificultad y ordenación debe hacerse con código local.
5. El usuario debe poder cocinar siguiendo pasos claros, cortos y sin ambigüedad.
6. Las recetas deben estar redactadas con texto propio.
7. No se deben copiar textos literales ni imágenes de webs externas.
8. La app debe poder funcionar sin IA para la mayoría de casos.
9. La IA debe ser una mejora, no una dependencia crítica.
10. El sistema debe ser escalable: empezar con pocas recetas, pero permitir crecer a cientos o miles.

---

## Arquitectura funcional

```text
Inventario local
  ↓
Normalización de alimentos
  ↓
Motor de búsqueda / RAG local
  ↓
Recetas candidatas
  ↓
Motor local de scoring
  ├─ Ingredientes disponibles
  ├─ Ingredientes faltantes
  ├─ Ingredientes opcionales
  ├─ Caducidad próxima
  ├─ Tiempo disponible
  ├─ Dificultad
  ├─ Momento del día
  ├─ Preferencias del usuario
  └─ Categorías / tags
  ↓
Recetas recomendadas
  ↓
Detalle de receta
  ↓
Preparación paso a paso
```

Si no hay resultados adecuados:

```text
RAG sin resultados útiles
  ↓
Fallback LLM
  ↓
Generación o adaptación de receta
  ↓
Mostrar resultado indicando que es una receta generada
```

---

## Qué debe hacer cada capa

### Base local de recetas

Contiene recetas estructuradas:

- Nombre.
- Categoría principal.
- Tags.
- Ingredientes.
- Cantidades base.
- Raciones base.
- Tiempo.
- Dificultad.
- Pasos.
- Consejos.
- Variantes.
- Sustituciones.

### RAG local

Debe encontrar recetas candidatas según:

- Ingredientes disponibles.
- Nombre de receta.
- Sinónimos.
- Tags.
- Categoría.
- Tipo de comida.
- Petición del usuario.

Ejemplos de búsqueda:

```text
Tengo espinacas
```

Debe devolver recetas como:

- Garbanzos con espinacas.
- Pasta con espinacas.
- Tortilla con espinacas.
- Ensalada de espinacas.

```text
Quiero algo rápido con arroz
```

Debe devolver:

- Arroz con tomate.
- Arroz a la cubana.
- Arroz con atún.
- Arroz tres delicias rápido.

### Motor local de scoring

Debe ordenar las recetas candidatas.

No debe usar LLM.

Debe calcular:

- Porcentaje de ingredientes disponibles.
- Ingredientes obligatorios que faltan.
- Ingredientes opcionales que faltan.
- Tiempo compatible con el filtro del usuario.
- Dificultad compatible.
- Ajuste por caducidad.
- Ajuste por momento del día.
- Ajuste por preferencias.

### LLM

Solo debe usarse para:

- Generar receta si no hay coincidencia suficiente.
- Adaptar una receta existente.
- Sugerir sustituciones.
- Convertir una receta a versión rápida.
- Convertir una receta a versión saludable.
- Simplificar pasos.
- Responder dudas del usuario durante la preparación.

No debe usarse para:

- Calcular raciones.
- Calcular match.
- Filtrar por tiempo.
- Filtrar por dificultad.
- Ordenar recetas.
- Saber qué ingredientes faltan.
- Decidir si una receta puede hacerse si los datos estructurados ya lo permiten.

---

## Modelo de datos recomendado

```kotlin
data class Recipe(
    val id: String,
    val name: String,
    val description: String,
    val category: RecipeCategory,
    val mealTypes: List<MealType>,
    val baseServings: Int,
    val preparationTimeMinutes: Int,
    val cookingTimeMinutes: Int,
    val totalTimeMinutes: Int,
    val difficulty: Difficulty,
    val tags: List<RecipeTag>,
    val ingredients: List<RecipeIngredient>,
    val steps: List<RecipeStep>,
    val variants: List<RecipeVariant> = emptyList(),
    val substitutions: List<IngredientSubstitution> = emptyList(),
    val source: RecipeSource = RecipeSource.Internal
)

data class RecipeIngredient(
    val foodId: String,
    val name: String,
    val quantity: Double,
    val unit: IngredientUnit,
    val optional: Boolean = false,
    val mainIngredient: Boolean = false,
    val group: IngredientGroup = IngredientGroup.Main
)

data class RecipeStep(
    val order: Int,
    val title: String,
    val instruction: String,
    val tip: String? = null,
    val estimatedMinutes: Int? = null,
    val actionType: CookingActionType? = null
)

data class RecipeVariant(
    val id: String,
    val name: String,
    val description: String,
    val timeAdjustmentMinutes: Int = 0,
    val difficulty: Difficulty? = null,
    val tags: List<RecipeTag> = emptyList()
)

data class IngredientSubstitution(
    val missingFoodId: String,
    val substituteFoodId: String,
    val description: String
)

sealed class RecipeSource {
    data object Internal : RecipeSource()
    data class Adapted(
        val sourceName: String?,
        val sourceUrl: String?,
        val sourceAuthor: String?
    ) : RecipeSource()
    data object GeneratedByLlm : RecipeSource()
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
}

enum class RecipeCategory {
    BREAKFAST,
    SALAD,
    PASTA,
    RICE,
    CHICKEN,
    PORK,
    BEEF,
    FISH,
    LEGUMES,
    QUICK_DINNER,
    KIDS,
    SUMMER,
    WINTER,
    HEALTHY,
    VEGETABLES,
    SOUP,
    DESSERT
}

enum class RecipeTag {
    QUICK,
    HEALTHY,
    KIDS,
    FAMILY,
    MEDITERRANEAN,
    HIGH_PROTEIN,
    LIGHT,
    BATCH_COOKING,
    CHEAP,
    SUMMER,
    WINTER,
    VEGETARIAN,
    FISH,
    MEAT,
    LEGUMES,
    PASTA,
    RICE,
    BREAKFAST,
    DINNER
}

enum class IngredientUnit {
    GRAM,
    MILLILITER,
    UNIT,
    TABLESPOON,
    TEASPOON,
    PINCH,
    TO_TASTE
}

enum class IngredientGroup {
    Main,
    Sauce,
    Seasoning,
    Garnish,
    Optional
}

enum class CookingActionType {
    CUT,
    BOIL,
    FRY,
    SAUTE,
    BAKE,
    GRILL,
    MIX,
    REST,
    SERVE
}
```

---

## Modelo de resultado recomendado

Cuando el sistema recomiende una receta, debe devolver un modelo enriquecido.

```kotlin
data class RecipeRecommendation(
    val recipe: Recipe,
    val matchPercentage: Int,
    val availableIngredients: List<RecipeIngredient>,
    val missingRequiredIngredients: List<RecipeIngredient>,
    val missingOptionalIngredients: List<RecipeIngredient>,
    val expiringSoonIngredients: List<RecipeIngredient>,
    val score: Double,
    val reasons: List<RecommendationReason>
)

data class RecommendationReason(
    val type: RecommendationReasonType,
    val message: String
)

enum class RecommendationReasonType {
    HAS_MOST_INGREDIENTS,
    USES_EXPIRING_FOOD,
    QUICK_RECIPE,
    EASY_RECIPE,
    MATCHES_MEAL_TYPE,
    KIDS_FRIENDLY,
    HEALTHY_OPTION
}
```

Ejemplo de razones:

```text
- Tienes 4 de 5 ingredientes principales.
- Usa espinacas que caducan pronto.
- Lista en 25 minutos.
- Dificultad fácil.
```

---

## Scoring de recetas

El scoring debe ser determinista y local.

Propuesta inicial:

```text
score = 0

+50 si tiene todos los ingredientes obligatorios
+35 si tiene al menos el 75% de ingredientes obligatorios
+20 si tiene al menos el 50% de ingredientes obligatorios

+15 si usa alimentos próximos a caducar
+10 si encaja con el tiempo máximo configurado
+10 si encaja con la dificultad configurada
+10 si encaja con el mealType solicitado
+10 si encaja con tags del usuario
-20 si falta un ingrediente principal
-10 si falta más de un ingrediente obligatorio
```

El match visual puede calcularse como:

```text
matchPercentage = ingredientesObligatoriosDisponibles / ingredientesObligatoriosTotales * 100
```

Los ingredientes opcionales no deben penalizar igual que los obligatorios.

Ejemplo:

```text
Receta: Garbanzos con espinacas y tomate

Obligatorios:
- Garbanzos
- Espinacas
- Tomate
- Cebolla

Opcionales:
- Curry
- Limón

Si el usuario tiene garbanzos, espinacas y tomate:
match = 3 / 4 = 75%
```

---

## Recalcular raciones

Las recetas deben guardarse con `baseServings`.

Ejemplo:

```text
baseServings = 4
400 g garbanzos
100 g espinacas
```

Si el usuario selecciona 2 raciones:

```text
factor = 2 / 4 = 0.5
400 g × 0.5 = 200 g
100 g × 0.5 = 50 g
```

Si el usuario selecciona 6 raciones:

```text
factor = 6 / 4 = 1.5
400 g × 1.5 = 600 g
100 g × 1.5 = 150 g
```

Reglas:

- Las cantidades en gramos y mililitros se recalculan directamente.
- Las unidades enteras deben redondearse con criterio.
- Las especias pueden mantenerse como `al gusto`.
- El tiempo no debe escalar linealmente con las raciones.
- El tiempo puede aumentar ligeramente si se duplican cantidades grandes.

---

## Reglas de preparación

Cada receta debe tener pasos claros, breves y accionables.

Regla principal:

> Un paso debe contener una acción principal.

Correcto:

```text
1. Pica la cebolla.
2. Sofríe la cebolla durante 5 minutos.
3. Añade el tomate.
4. Cocina 5 minutos.
5. Incorpora los garbanzos.
```

Incorrecto:

```text
Pica la cebolla, sofríela, añade el tomate, remueve, cocina y después añade los garbanzos.
```

Cada paso debe poder mostrarse bien en UI.

Estructura visual esperada:

```text
Paso 1
Título: Sofríe la cebolla
Instrucción: Cocina la cebolla con aceite durante 5 minutos a fuego medio.
Consejo: No subas demasiado el fuego para evitar que se queme.
Tiempo: 5 min
```

---

## Estilo de redacción de las recetas

Las recetas deben estar escritas con lenguaje claro, doméstico y directo.

Usar:

- “Corta”.
- “Añade”.
- “Cocina”.
- “Remueve”.
- “Sirve”.
- “Hornea”.
- “Cuece”.

Evitar:

- Lenguaje literario.
- Introducciones largas.
- Historias sobre el plato.
- Frases copiadas de webs.
- Pasos ambiguos como “cocina hasta que esté hecho” sin referencia visual o temporal.

Mejor:

```text
Cocina 8-10 minutos, hasta que la cebolla esté blanda y transparente.
```

Peor:

```text
Cocina hasta que tenga el punto perfecto.
```

---

## Estrategia legal y de contenido

Para evitar problemas legales:

- No copiar textos literales de webs.
- No copiar imágenes de recetas.
- No extraer una base masiva de una única web.
- Usar recetas tradicionales y redactarlas internamente.
- Guardar fuentes solo como trazabilidad interna cuando se usen referencias.
- Si una receta se inspira en varias fuentes, crear una versión propia y estructurada.

Los ingredientes, cantidades y técnicas comunes pueden usarse como referencia general, pero la expresión escrita debe ser propia.

Ejemplo correcto:

```text
Macarrones con atún y tomate
1. Cuece la pasta.
2. Mezcla el atún con tomate.
3. Junta la pasta con la salsa.
```

Ejemplo incorrecto:

```text
Copiar literalmente los pasos, trucos, descripción o introducción de una web externa.
```

---

## Categorías iniciales

Crear una base inicial mediterránea, familiar y útil.

Categorías prioritarias:

- Desayunos.
- Ensaladas.
- Pastas.
- Arroces.
- Pollo.
- Cerdo.
- Ternera.
- Pescado.
- Legumbres.
- Cenas rápidas.
- Niños.
- Verano.
- Invierno.
- Saludables.

Una receta puede pertenecer a varias categorías mediante tags.

Ejemplo:

```text
Tortilla francesa
category = BREAKFAST
mealTypes = [BREAKFAST, DINNER]
tags = [QUICK, KIDS, HIGH_PROTEIN, DINNER]
```

---

## Recetas iniciales

### Desayunos

- Tostada con tomate
- Tostada con aceite
- Tostada con jamón
- Tostada con aguacate
- Tostada con anchoas
- Tostada con queso fresco
- Tostada con pavo
- Tostada con pechuga de pollo
- Tostada con mermelada
- Yogur natural con fruta
- Yogur con avena
- Yogur con muesli
- Yogur con frutos secos
- Avena con plátano
- Avena con manzana
- Macedonia
- Tortilla francesa
- Tortilla francesa con queso
- Huevos revueltos
- Huevos revueltos con pavo
- Pan con aceite y fruta
- Queso fresco con tomate
- Batido de plátano y yogur
- Porridge básico
- Fruta con yogur

### Ensaladas

- Ensalada mixta
- Ensalada de pasta
- Ensalada de arroz
- Ensalada campera
- Ensalada César
- Ensalada de garbanzos
- Ensalada de lentejas
- Ensalada de atún
- Ensalada de tomate
- Ensalada de pepino
- Ensalada de tomate, pepino y aguacate
- Ensalada de pollo
- Ensalada de huevo cocido
- Ensalada de queso fresco
- Ensalada de salmón
- Ensalada griega
- Ensalada de pasta con atún
- Ensalada de arroz con gambas
- Ensalada templada de verduras
- Ensalada de tomate y mozzarella

### Pastas

- Espaguetis boloñesa
- Espaguetis carbonara
- Espaguetis con tomate
- Espaguetis con atún
- Espaguetis con gambas
- Espaguetis con gulas y gambas
- Espaguetis al ajillo
- Espaguetis con pollo
- Espaguetis con verduras
- Espaguetis con salmón
- Macarrones con tomate
- Macarrones con atún y tomate
- Macarrones boloñesa
- Macarrones carbonara
- Macarrones con gulas y gambas
- Macarrones con chorizo
- Macarrones con pollo
- Macarrones con verduras
- Tallarines con gambas
- Tallarines con pollo
- Pasta al pesto
- Pasta con nata y champiñones
- Pasta con espinacas
- Pasta con queso
- Pasta gratinada
- Lasaña boloñesa
- Lasaña de verduras
- Canelones de carne
- Canelones de atún
- Fideos con pollo

### Arroces

- Arroz con tomate
- Arroz a la cubana
- Arroz tres delicias
- Arroz con pollo
- Arroz con verduras
- Arroz con gambas
- Arroz con atún
- Arroz con ternera
- Arroz con carne picada
- Arroz con salchichas
- Arroz con conejo
- Arroz con costillas
- Arroz caldoso de pollo
- Arroz caldoso de marisco
- Arroz meloso con verduras
- Paella mixta
- Paella de marisco
- Paella de pollo
- Paella de verduras
- Fideuá
- Risotto sencillo de champiñones
- Arroz al horno sencillo

### Pollo

- Pollo al horno con patatas
- Pollo al horno con cebolla
- Pollo al ajillo
- Pollo al limón
- Pollo a la plancha
- Pechuga con cebolla
- Pechuga con pimientos verdes
- Pechuga con verduras
- Pechuga empanada
- Nuggets caseros
- Alitas al horno
- Alitas fritas
- Muslos de pollo al horno
- Pollo con arroz
- Pollo con tomate
- Pollo con champiñones
- Pollo al curry suave
- Pollo guisado con patatas
- Ensalada de pollo
- Hamburguesa de pollo

### Cerdo

- Lomo a la plancha
- Lomo con pimientos
- Lomo al ajillo
- Libritos de lomo
- Costillas al horno
- Costillas guisadas
- Solomillo de cerdo
- Solomillo con champiñones
- Salchichas con cebolla y vino
- Longaniza con cebolla
- Chorizo al vino
- Magra con tomate
- Chuletas de cerdo
- Cinta de lomo al horno
- Bocaditos de lomo empanado
- Arroz con costillas
- Pasta con chorizo
- Huevos rotos con chorizo

### Ternera

- Ternera a la plancha
- Ternera al ajillo
- Ternera con pimientos
- Ternera con cebolla
- Ternera guisada
- Estofado de ternera
- Filetes empanados
- Albóndigas con tomate
- Hamburguesa casera
- Carne picada con tomate
- Arroz con ternera
- Macarrones boloñesa
- Lasaña boloñesa
- Ternera con champiñones
- Ternera salteada con verduras

### Pescado

- Merluza rebozada
- Merluza al horno
- Merluza a la plancha
- Merluza en salsa verde
- Lubina al horno con patatas
- Lubina a la plancha
- Dorada al horno con patatas
- Dorada a la plancha con limón
- Salmón a la plancha
- Salmón al horno
- Bacalao con tomate
- Bacalao al horno
- Atún a la plancha
- Bonito con tomate
- Sardinas al horno
- Sepia a la plancha
- Calamares a la romana
- Gambas al ajillo
- Gulas con gambas
- Arroz con gambas
- Pasta con gambas
- Ensalada de atún

### Legumbres

- Lentejas con chorizo
- Lentejas con verduras
- Lentejas con arroz
- Lentejas con patata y chorizo
- Garbanzos con espinacas
- Garbanzos con tomate
- Garbanzos con chorizo
- Potaje de garbanzos
- Garbanzos salteados
- Ensalada de garbanzos
- Alubias con verduras
- Alubias con chorizo
- Judías blancas guisadas
- Fabada sencilla
- Hummus casero
- Lentejas rápidas

### Cenas rápidas

- Tortilla francesa
- Revuelto de huevos
- Ensalada de atún
- Ensalada de pollo
- Sándwich mixto
- Tostada de aguacate y huevo
- Quesadillas
- Hamburguesa casera
- Pizza casera rápida
- Crema de verduras
- Sopa de fideos
- Pechuga a la plancha
- Merluza a la plancha
- Dorada a la plancha
- Arroz con tomate
- Pasta con atún
- Verduras salteadas
- Gulas con gambas
- Huevos con tomate
- Yogur con fruta y frutos secos

### Niños

- Macarrones con tomate
- Macarrones con atún
- Arroz con tomate
- Arroz a la cubana
- Nuggets caseros
- Hamburguesa casera
- Albóndigas con tomate
- Croquetas
- Tortilla francesa
- Pizza casera
- Pechuga empanada
- Lasaña boloñesa
- Canelones
- Salchichas con arroz
- Sopa de fideos
- Puré de verduras
- Pescado rebozado
- Patatas con huevo

---

## Pantalla de recetas

Debe mostrar recetas calculadas contra el inventario.

Elementos recomendados:

- Header con título `Recetas`.
- Indicador `IA local privada`.
- Resumen de inventario: número de alimentos disponibles.
- Resumen de recetas disponibles.
- Filtros rápidos:
  - Recomendadas.
  - Rápidas.
  - Desayuno.
  - Comida.
  - Cena.
  - Niños.
  - Saludables.
- Cards con:
  - Imagen o placeholder.
  - Nombre.
  - Tiempo.
  - Raciones.
  - Dificultad.
  - Match con inventario.
  - Ingredientes principales usados.
  - Ingredientes que faltan.
  - CTA `Ver receta`.

La pantalla debe priorizar recetas con mayor utilidad real, no orden alfabético.

---

## Detalle de receta

Debe mostrar:

- Imagen o placeholder.
- Nombre.
- Etiquetas principales.
- Tiempo.
- Raciones.
- Dificultad.
- Porcentaje de match.
- Ingredientes que tiene el usuario.
- Ingredientes que faltan.
- Botón para añadir ingredientes faltantes a la lista de compra.
- Selector de raciones.
- Preparación paso a paso.
- Consejos opcionales.
- Sustituciones opcionales.

---

## Pantalla por alimento

Desde un alimento del inventario, el usuario debe poder ver recetas relacionadas.

Ejemplo:

```text
Alimento: Espinacas

Recetas:
- Garbanzos con espinacas
- Pasta con espinacas
- Tortilla con espinacas
- Ensalada de espinacas
```

Esta pantalla debe ordenar por:

1. Recetas donde el alimento sea ingrediente principal.
2. Recetas que el usuario pueda hacer ya.
3. Recetas con pocos ingredientes faltantes.
4. Recetas rápidas.

---

## Estados vacíos

Si no hay recetas recomendadas:

```text
No hemos encontrado recetas claras con tu inventario actual.
Puedes añadir más alimentos o generar una receta con IA.
```

Si faltan pocos ingredientes:

```text
Te falta poco para esta receta.
Añade los ingredientes faltantes a la lista de compra.
```

Si el LLM genera una receta:

```text
Receta generada con IA a partir de tu inventario.
Revísala antes de cocinar.
```

---

## MVP recomendado

Primera iteración:

1. Crear modelos de dominio.
2. Crear datos seed locales.
3. Crear cálculo de raciones.
4. Crear cálculo de match contra inventario.
5. Crear filtros básicos.
6. Mostrar recetas recomendadas en UI.
7. Mostrar detalle de receta.
8. Añadir ingredientes faltantes a lista de compra.

No implementar todavía:

- RAG real complejo.
- LLM.
- Embeddings remotos.
- Generación automática de recetas.
- Scraping de recetas.

Primero debe funcionar el motor local. Luego se añade IA. Sin base sólida, la IA solo decora el caos con purpurina cara.

---

## Prompt recomendado para Codex

```text
Lee este documento completo y úsalo como especificación funcional para implementar la primera versión del sistema de recetas de NeveraChef.

Objetivo de esta iteración:
- No implementar LLM todavía.
- No implementar RAG real todavía.
- Crear modelos de dominio para recetas.
- Crear una fuente local de recetas seed.
- Implementar cálculo de raciones.
- Implementar cálculo de match contra inventario.
- Implementar filtros por tiempo, dificultad, categoría, mealType y tags.
- Implementar ordenación de recetas recomendadas.
- Preparar la arquitectura para añadir RAG y fallback LLM más adelante.

Restricciones:
- No usar IA para cálculos deterministas.
- No copiar recetas de internet.
- Mantener lógica de negocio fuera de la UI.
- Mantener modelos simples, testeables y multiplataforma si el proyecto usa KMP.
- Añadir tests unitarios para scoring y raciones.

Entrega esperada:
- Modelos creados.
- Datos seed iniciales.
- Motor de recomendación local.
- Funciones de escalado de raciones.
- Tests unitarios.
- Breve resumen de archivos modificados.
```