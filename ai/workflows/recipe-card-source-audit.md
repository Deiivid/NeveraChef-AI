# Auditoria de fuentes de recetas

## Resumen

- Cartas RAG activas: `120`.
- Archivo usado por el motor actual: `shared/src/commonMain/kotlin/es/neverachefai/feature/recipes/data/rag/LocalRecipeKnowledgeBase.kt`.
- Estado de fuente: las cartas activas no tienen URL, autor, web de origen ni referencia por receta.
- Conclusion: ahora mismo son cartas locales creadas/curadas dentro del repo, no recetas verificadas una a una desde una web.

## Fuentes encontradas

- `LocalRecipeKnowledgeBase.kt`: fuente real del runtime. Contiene las cartas que usa el RAG.
- `shared/src/commonMain/composeResources/files/recipes.v1.sample.json`: contiene 50 recetas semilla y 4 URLs generales de referencia, pero no esta conectado al runtime actual y no mapea fuente por receta.
- `LocalRecipeImageCatalog.kt`: contiene fuentes/licencias de imagenes. No es fuente culinaria de la receta.

## URLs generales del JSON semilla

- https://www.spain.info/en/top/most-famous-spanish-food-recipes/
- https://bonviveur.com/es/recetas/lista/legumbres/
- https://www.directoalpaladar.com/recetas-de-legumbres/nueve-originales-recetas-legumbres-para-renovar-tu-recetario
- https://elpais.com/gastronomia/recetas/2024-06-21/fritada-aragonesa-la-receta-perfecta-para-aprovechar-la-temporada-del-calabacin.html

Estas URLs son referencias generales. No prueban que cada carta concreta venga de esa pagina.

## Cartas RAG activas

Todas las cartas de esta lista tienen fuente actual: `LocalRecipeKnowledgeBase.kt`, sin fuente externa individual.

1. `spanish-tortilla-francesa` - Tortilla francesa jugosa
2. `spanish-tortilla-patata-rapida` - Tortilla de patata rapida
3. `spanish-huevos-rotos` - Huevos rotos con patata
4. `spanish-huevos-flamenca-simple` - Huevos a la flamenca sencillos
5. `spanish-revuelto-espinacas` - Revuelto de espinacas
6. `spanish-tostada-tomate-huevo` - Tostada de tomate y huevo
7. `spanish-pan-tomate-jamon` - Pan con tomate y jamon
8. `spanish-lentejas-arroz-pimenton` - Lentejas rapidas con arroz y pimenton
9. `spanish-lentejas-chorizo` - Lentejas con chorizo de diario
10. `spanish-garbanzos-espinacas` - Garbanzos con espinacas
11. `spanish-potaje-garbanzos-bacalao` - Potaje rapido de garbanzos y bacalao
12. `spanish-alubias-verduras` - Alubias con verduras
13. `spanish-arroz-cubana` - Arroz a la cubana de casa
14. `spanish-arroz-pollo` - Arroz con pollo sencillo
15. `spanish-arroz-verduras` - Arroz con verduras
16. `spanish-pasta-atun-tomate` - Macarrones con atun y tomate
17. `spanish-ensaladilla-rapida` - Ensaladilla rapida
18. `spanish-pisto-huevo` - Pisto con huevo
19. `spanish-sopa-castellana-simple` - Sopa castellana sencilla
20. `spanish-sopa-fideos` - Sopa de fideos de diario
21. `spanish-patatas-riojana-rapidas` - Patatas a la riojana rapidas
22. `spanish-patatas-verduras` - Patatas guisadas con verduras
23. `spanish-pollo-ajillo` - Pollo al ajillo sencillo
24. `spanish-pollo-tomate` - Pollo con tomate
25. `spanish-ternera-ajo-perejil` - Filete de ternera con ajo
26. `spanish-merluza-salsa-verde` - Merluza en salsa verde sencilla
27. `spanish-pescado-romana` - Merluza a la romana rapida
28. `spanish-lubina-patata` - Lubina con patata panadera sencilla
29. `spanish-bacalao-tomate` - Bacalao con tomate
30. `spanish-atun-pisto` - Atun con pisto rapido
31. `spanish-ensalada-murciana-simple` - Ensalada murciana sencilla
32. `spanish-gazpacho-simple` - Gazpacho sencillo
33. `spanish-salmorejo-simple` - Salmorejo sencillo
34. `spanish-judias-verdes-patata` - Judias verdes con patata
35. `spanish-champinones-ajillo` - Champinones al ajillo
36. `spanish-calabacin-tortilla` - Tortilla de calabacin
37. `spanish-zanahoria-huevo-ensalada` - Ensalada de zanahoria y huevo
38. `spanish-macedonia-casera` - Macedonia casera
39. `spanish-yogur-fruta-avena` - Yogur con fruta y avena
40. `spanish-compota-manzana` - Compota rapida de manzana
41. `spanish-tortilla-bacalao` - Tortilla de bacalao sencilla
42. `spanish-tortilla-esparragos` - Tortilla de esparragos
43. `spanish-revuelto-setas` - Revuelto de setas
44. `spanish-huevos-guisantes` - Huevos con guisantes
45. `spanish-bocadillo-tortilla` - Bocadillo de tortilla
46. `spanish-migas-pan-huevo` - Migas rapidas con huevo
47. `spanish-migas-chorizo` - Migas con chorizo
48. `spanish-lentejas-verduras` - Lentejas viudas con verduras
49. `spanish-lentejas-patata` - Lentejas con patata
50. `spanish-garbanzos-tomate-huevo` - Garbanzos con tomate y huevo
51. `spanish-garbanzos-arroz` - Garbanzos con arroz
52. `spanish-cocido-rapido` - Cocido rapido de garbanzos
53. `spanish-caldo-cocido-fideos` - Caldo con fideos y garbanzos
54. `spanish-fabada-simple` - Fabada sencilla de diario
55. `spanish-alubias-chorizo` - Alubias con chorizo
56. `spanish-alubias-setas` - Alubias con setas
57. `spanish-potaje-acelgas-garbanzos` - Potaje de acelgas y garbanzos
58. `spanish-paella-verduras` - Arroz estilo paella de verduras
59. `spanish-paella-pollo` - Arroz de pollo en paellera
60. `spanish-arroz-costilla` - Arroz con costilla
61. `spanish-arroz-conejo` - Arroz con conejo
62. `spanish-arroz-caldoso-pescado` - Arroz caldoso con merluza
63. `spanish-fideua-pescado` - Fideua sencilla de merluza
64. `spanish-fideos-cazuela-pollo` - Fideos a la cazuela con pollo
65. `spanish-arroz-horno-simple` - Arroz al horno sencillo
66. `spanish-arroz-negro-simple` - Arroz negro sencillo
67. `spanish-arroz-bacalao-alcachofa` - Arroz con bacalao y alcachofa
68. `spanish-patatas-bravas` - Patatas bravas caseras
69. `spanish-patatas-pobre` - Patatas a lo pobre
70. `spanish-patatas-importancia` - Patatas a la importancia sencillas
71. `spanish-patatas-revolconas` - Patatas revolconas suaves
72. `spanish-patatas-bacalao` - Patatas con bacalao
73. `spanish-patatas-sepia` - Patatas con sepia
74. `spanish-ensalada-campera` - Ensalada campera
75. `spanish-pisto-calabacin` - Pisto de calabacin
76. `spanish-zarangollo` - Zarangollo murciano sencillo
77. `spanish-escalivada` - Escalivada sencilla
78. `spanish-menestra-verduras` - Menestra rapida de verduras
79. `spanish-acelgas-patata` - Acelgas con patata
80. `spanish-coliflor-ajada` - Coliflor con ajada
81. `spanish-alcachofas-jamon` - Alcachofas con jamon
82. `spanish-alcachofas-huevo` - Alcachofas con huevo
83. `spanish-berenjena-atun` - Berenjena rellena rapida de atun
84. `spanish-crema-calabaza` - Crema de calabaza
85. `spanish-porrusalda` - Porrusalda sencilla
86. `spanish-sopa-pescado` - Sopa sencilla de merluza
87. `spanish-caldo-gallego-simple` - Caldo gallego sencillo
88. `spanish-pollo-chilindron` - Pollo al chilindron sencillo
89. `spanish-pollo-pepitoria-simple` - Pollo en pepitoria facil
90. `spanish-pollo-arroz-caldoso` - Arroz caldoso con pollo
91. `spanish-estofado-ternera-patata` - Estofado de ternera con patata
92. `spanish-carne-tomate` - Carne con tomate
93. `spanish-magro-tomate` - Magro con tomate
94. `spanish-albondigas-salsa` - Albondigas en salsa sencilla
95. `spanish-albondigas-tomate` - Albondigas con tomate
96. `spanish-conejo-ajillo` - Conejo al ajillo
97. `spanish-conejo-arroz` - Conejo guisado con arroz
98. `spanish-lomo-pimientos` - Lomo con pimientos
99. `spanish-costillas-patatas` - Costillas guisadas con patatas
100. `spanish-merluza-bilbaina` - Merluza a la bilbaina sencilla
101. `spanish-merluza-horno-patatas` - Merluza con patatas al horno
102. `spanish-marmitako-simple` - Marmitako sencillo
103. `spanish-bonito-tomate` - Bonito o atun con tomate
104. `spanish-sardinas-horno` - Sardinas al horno
105. `spanish-boquerones-adobo` - Boquerones en adobo suave
106. `spanish-calamares-cebolla` - Calamares encebollados
107. `spanish-sepia-plancha` - Sepia a la plancha con ajo
108. `spanish-mejillones-marinera` - Mejillones a la marinera faciles
109. `spanish-gambas-ajillo` - Gambas al ajillo
110. `spanish-almejas-marinera` - Almejas a la marinera sencillas
111. `spanish-pipirrana` - Pipirrana sencilla
112. `spanish-empedrat-simple` - Empedrat sencillo
113. `spanish-esqueixada-simple` - Ensalada de bacalao con tomate
114. `spanish-arroz-leche` - Arroz con leche sencillo
115. `spanish-natillas-caseras` - Natillas caseras simples
116. `spanish-flan-huevo-simple` - Flan de huevo sencillo
117. `spanish-torrijas-rapidas` - Torrijas rapidas
118. `spanish-naranja-canela` - Naranja con canela
119. `spanish-manzana-asada` - Manzana asada sencilla
120. `spanish-fresas-yogur` - Fresas con yogur
