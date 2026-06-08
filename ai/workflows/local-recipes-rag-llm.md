# Recetas locales con RAG + LLM

## Objetivo

La pantalla de recetas debe funcionar sin backend, sin login, sin internet y sin enviar inventario fuera del dispositivo. El usuario no escribe prompts libres: solo pulsa generar desde el inventario o desde el detalle de un alimento.

## Arquitectura

```text
Inventario local
  -> normalizador de ingredientes
  -> retriever RAG local
  -> tarjetas de recetas locales
  -> LLM local pequeno
  -> validador local
  -> UI de recetas
```

## Por que se separa asi

- El RAG no es el LLM. El RAG recupera tarjetas locales seguras y filtradas.
- El LLM no debe decidir que buscar. Solo debe redactar o adaptar usando las tarjetas recuperadas.
- El validador es obligatorio porque un LLM pequeno puede inventar ingredientes o saltarse limites.
- Si el LLM no esta instalado, la app debe seguir siendo usable con RAG local y mostrarlo claramente.

## Modelo recomendado

Primera opcion:

- Runtime: LiteRT-LM.
- Modelo: Gemma 3 270M instruction-tuned convertido a `.litertlm`.
- Motivo: tamano pequeno, enfoque on-device y APIs oficiales Kotlin/Swift.

Fuentes usadas:

- LiteRT-LM Android: https://developers.google.com/edge/litert-lm/android
- LiteRT-LM Swift: https://developers.google.com/edge/litert-lm/swift
- Gemma 3 270M: https://developers.googleblog.com/introducing-gemma-3-270m/

Alternativa:

- llama.cpp + GGUF.
- Motivo: muy portable y maduro.
- Coste: integracion nativa Android/iOS mas manual.
- Fuente: https://github.com/ggml-org/llama.cpp/blob/master/docs/android.md

## Estado de esta rama

Implementado:

- Corpus local propio en `LocalRecipeKnowledgeBase`, orientado a cocina espanola casera.
- Corpus inicial actual: 120 tarjetas locales.
- Imagenes locales: 5 especificas de Wikimedia Commons y 12 fallback por familia desde Openverse/Wikimedia.
- Metadatos de licencia y atribucion en `LocalRecipeImageCatalog`.
- Matching local por ingredientes, tiempo y foco de producto.
- Generacion desde pantalla de recetas.
- Boton en detalle de producto para generar recetas con ese alimento.
- Contrato `LocalLlmEngine` y `expect/actual` Android/iOS.
- Fallback honesto: si no hay modelo instalado, se marca como `LOCAL_RAG`, no como IA.

## Corpus RAG

Regla principal:

- No copiar textos de blogs, libros ni webs.
- Usar fuentes abiertas solo como referencia de tipos de platos, ingredientes y cobertura.
- Escribir cada tarjeta con redaccion propia, corta y funcional.
- Priorizar cocina espanola casera: legumbres, tortilla, arroces, guisos, pescado, pollo, patatas, verduras, ensaladas y desayunos sencillos.
- Permitir recetas neutras solo cuando resuelven inventario real, por ejemplo fruta, yogur o avena.

Fuentes utiles para ampliar:

- Wikilibros recetas, CC BY-SA 4.0: https://es.wikibooks.org/wiki/Categor%C3%ADa:Recetas
- Wikilibros cocina espanola, CC BY-SA 4.0: https://es.wikibooks.org/wiki/Categor%C3%ADa:Cocina_espa%C3%B1ola
- Wikibooks Cookbook, varios miles de recetas bajo CC BY-SA: https://en.wikibooks.org/wiki/Cookbook:Recipes
- Project Gutenberg, libros antiguos en dominio publico segun ficha de cada obra: https://www.gutenberg.org/
- U.S. Copyright Office sobre recetas: https://www.copyright.gov/help/faq/faq-protect.html

No usar como corpus directo:

- Blogs de cocina actuales.
- Libros modernos con copyright.
- Fotos, introducciones, comentarios personales o colecciones completas de terceros.
- Datasets sin licencia clara.

Escala recomendada:

1. Fase 1: 120 tarjetas caseras para validar matching.
2. Fase 2: 200-300 tarjetas espanolas comunes.
3. Fase 3: 400-800 tarjetas si el tamano de app sigue siendo razonable.

Formato de tarjeta:

```text
id
titulo
tipo: desayuno/comida/cena/cualquiera
minutos
dificultad
raciones
ingredientes_obligatorios
ingredientes_opcionales
pasos cortos
tags
imagen
licencia
```

## Formato de almacenamiento

El formato limpio a medio plazo es un JSON versionado incluido como asset local, importado a una base local privada al primer arranque:

```text
recipes.v1.json -> indice local por ingredientes/tags/minutos -> resultados RAG
```

En esta rama el corpus queda en Kotlin tipado para no meter parser ni migracion de base todavia. Si crece a 200-800 recetas, conviene moverlo a JSON y generar el indice local desde ese fichero.

El LLM no necesita que la app guarde todo como JSON. JSON es util para pasarle solo las tarjetas recuperadas en el prompt cerrado.

Pendiente para activar LLM real:

1. Elegir modelo final y licencia.
2. Convertir o descargar modelo `.litertlm`.
3. Incluirlo como asset privado o descargarlo una vez a almacenamiento privado.
4. Implementar `PlatformLocalLlmEngine.android.kt` con LiteRT-LM Kotlin.
5. Implementar `PlatformLocalLlmEngine.ios.kt` con LiteRT-LM Swift o un bridge nativo.
6. Parsear salida JSON del LLM.
7. Rechazar cualquier receta que no pase `RecipeValidator`.

## Prompt cerrado

El prompt debe contener solo:

- inventario local,
- tiempo maximo,
- tarjetas RAG recuperadas,
- instrucciones de no usar conocimiento externo,
- formato JSON esperado.

No debe incluir texto libre del usuario.

## Seguridad

- No anadir permiso `INTERNET` para esta feature.
- No guardar prompts con inventario en logs.
- No enviar errores de generacion a telemetria externa.
- No usar APIs cloud ni claves.
- No copiar textos de libros con copyright. Las tarjetas deben ser propias, de dominio publico o con licencia compatible.
