# Wireframes y decisiones UI

## Estructura de aplicación

```text
┌ Sidebar ───────┬ Header: búsqueda / avisos / perfil ────────────┐
│ Marca          ├────────────────────────────────────────────────┤
│ Espacio        │ Título + acción principal                       │
│ Dashboard      │ Métricas                                        │
│ Cursos         │ Contenido: tarjetas, tabla o editor             │
│ Notas          │                                                 │
│ Calendario     │                                                 │
│ Buscar         │                                                 │
│ Configuración  │                                                 │
└────────────────┴────────────────────────────────────────────────┘
```

- Dashboard: métricas, cursos en curso, próximos eventos y actividad.
- Cursos: pestañas de estado; las tarjetas usan color e icono por curso.
- Notas: composición editorial limpia y etiquetas para exploración rápida.
- Calendario: rejilla mensual, con eventos diferenciados por tipo.

La implementación usa una escala de 8 px, contraste de texto AA, áreas táctiles de al menos 40 px y un modo reducido para pantallas pequeñas. El modo oscuro se activa sin perder la jerarquía visual.
