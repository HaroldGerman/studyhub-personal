# API inicial

Todas las rutas, salvo autenticación, se servirán bajo `/api` y requieren `Authorization: Bearer <JWT>` cuando el filtro JWT esté habilitado.

| Método | Ruta | Propósito |
|---|---|---|
| POST | `/auth/login` | Inicia sesión y devuelve tokens |
| POST | `/auth/refresh` | Renueva el token de acceso |
| POST | `/auth/forgot-password` | Solicita recuperación (preparada para proveedor de correo) |
| GET, POST | `/courses` | Lista o crea cursos |
| GET, PATCH, DELETE | `/courses/{id}` | Consulta, actualiza o elimina un curso |
| GET, POST | `/courses/{id}/lessons` | Gestiona clases |
| GET, PUT | `/lessons/{id}/note` | Editor de apuntes Markdown |
| GET, POST | `/events` | Agenda, exámenes y recordatorios |
| GET | `/search?q=` | Búsqueda global paginada |

Las respuestas de error siguen `{ "code": "...", "message": "..." }`; nunca se devuelve el detalle interno de una excepción.
