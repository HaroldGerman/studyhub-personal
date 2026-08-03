# 📚 SÍLABO OFICIAL — PROGRAMA DE DATA ENGINEERING PROFESIONAL
**Profesor:** Antigravity AI — Senior Data Engineer & Mentor  
**Modalidad:** Intensivo · Teórico-Práctico · Basado en Proyectos  
**Nivel de entrada:** Cero absoluto  
**Nivel de salida:** Data Engineer Junior altamente competitivo  
**Duración estimada:** 8 a 12 meses (con dedicación de 10–15 h/semana)  
**Idioma:** Español  
**Última actualización:** Julio 2026

---

> [!IMPORTANT]
> **Regla de avance:** No se avanza al siguiente módulo hasta demostrar dominio real del anterior mediante examen, ejercicios y revisión de código. El ritmo lo marca tu comprensión, no un calendario fijo.

---

## 🗺️ ESTRUCTURA GENERAL DEL PROGRAMA

```
FASE 1 — FUNDAMENTOS         (Módulos 0–4)   ~10 semanas
FASE 2 — DATOS Y MODELADO    (Módulos 5–7)   ~6 semanas
FASE 3 — ORQUESTACIÓN        (Módulos 8–9)   ~8 semanas
FASE 4 — ARQUITECTURAS       (Módulos 10–12) ~8 semanas
FASE 5 — CLOUD E INFRA       (Módulos 13–15) ~8 semanas
FASE 6 — CALIDAD Y SEGURIDAD (Módulos 16–17) ~4 semanas
FASE 7 — OPTIMIZACIÓN        (Módulo 18)     ~4 semanas
FASE 8 — ARQUITECTURA AVANZADA(Módulo 19)   ~4 semanas
FASE 9 — CARRERA Y PORTAFOLIO(Módulo 20)    ~4 semanas
```

---

# FASE 1 — FUNDAMENTOS TÉCNICOS

---

## MÓDULO 0 — Introducción al Data Engineering
**Duración estimada:** 1 semana  
**Objetivo:** Comprender el rol, el ecosistema y la mentalidad de un Data Engineer.

### Temas
- ¿Qué es un Data Engineer?
- Historia y evolución del rol (pre y post Big Data)
- El ecosistema de datos: roles y relaciones
  - Data Engineer ↔ Data Scientist
  - Data Engineer ↔ Data Analyst
  - Data Engineer ↔ ML Engineer
  - Data Engineer ↔ Software Engineer
  - Data Engineer ↔ DevOps / Platform
  - Data Engineer ↔ Arquitecto de Datos
- Responsabilidades diarias
- Un día en la vida de un DE senior
- El stack tecnológico completo (visión general)
- Salarios, mercado laboral y oportunidades
- Mentalidad de ingeniería: cómo piensan los mejores

### Entregable
- ✅ Cuestionario de comprensión conceptual (4 preguntas abiertas)

### Recursos
- 📘 *Fundamentals of Data Engineering* — Cap. 1
- 📹 DataTalks.Club: Intro to Data Engineering

---

## MÓDULO 1 — Linux para Data Engineers
**Duración estimada:** 3 semanas  
**Prerequisito:** Módulo 0  
**Objetivo:** Dominar el entorno Linux como herramienta de trabajo diaria.

### Semana 1 — Sistema Operativo y Filesystem
| Tema | Descripción |
|------|-------------|
| ¿Qué es Linux y por qué lo usan los DE? | Historia, distribuciones, kernel |
| Sistema de archivos | `/`, `/home`, `/etc`, `/var`, `/tmp`, `/opt` |
| Navegación | `ls`, `cd`, `pwd`, `find`, `locate` |
| Archivos y directorios | `cp`, `mv`, `rm`, `mkdir`, `touch`, `tree` |
| Visualización de archivos | `cat`, `less`, `more`, `head`, `tail` |
| Usuarios y grupos | `whoami`, `useradd`, `usermod`, `groups` |
| Permisos | `chmod`, `chown`, `chgrp`, `umask` |

### Semana 2 — Procesos, Servicios y Red
| Tema | Descripción |
|------|-------------|
| Procesos | `ps`, `top`, `htop`, `kill`, `jobs`, `bg`, `fg` |
| Servicios | `systemctl`, `journalctl`, daemons |
| Red | `ping`, `curl`, `wget`, `netstat`, `ssh`, `scp` |
| Variables de entorno | `export`, `.bashrc`, `.bash_profile`, `env` |
| Cron Jobs | Sintaxis cron, `crontab -e`, logs, automatización |

### Semana 3 — Bash y Automatización
| Tema | Descripción |
|------|-------------|
| Bash scripting | Variables, condicionales, loops, funciones |
| Pipes y redirecciones | `\|`, `>`, `>>`, `<`, `2>`, `/dev/null` |
| Expresiones regulares | `grep`, `egrep`, `sed`, `awk` |
| Compresión y archivos | `tar`, `gzip`, `zip`, `unzip` |
| Automatización real | Scripts de limpieza, backup, monitoreo |

### Proyecto del Módulo: `data-pipeline-bash`
> Construir un script Bash que descargue un archivo CSV de una URL, lo limpie, filtre registros según criterios, genere un reporte de resumen y lo guarde con timestamp. Incluye logging y manejo de errores.

### Evaluación
- ✅ Examen teórico (20 preguntas)
- ✅ Ejercicios prácticos en terminal
- ✅ Revisión del script Bash del proyecto

### Recursos
- 📗 The Linux Command Line — William Shotts (gratuito online)
- 📹 Missing Semester — MIT (gratuito en YouTube)

---

## MÓDULO 2 — Git y GitHub para Data Engineers
**Duración estimada:** 2 semanas  
**Prerequisito:** Módulo 1  
**Objetivo:** Usar Git como herramienta de trabajo profesional, no solo para "guardar código".

### Semana 1 — Git Core
| Tema | Descripción |
|------|-------------|
| ¿Qué es control de versiones? | Historia, SVN vs Git |
| Instalación y configuración | `git config`, SSH keys |
| Flujo básico | `init`, `add`, `commit`, `status`, `log` |
| Branches | `branch`, `checkout`, `switch`, `merge` |
| Rebase | `rebase`, `rebase -i`, diferencia con merge |
| Resolución de conflictos | Estrategias, herramientas visuales |

### Semana 2 — GitHub y Flujo Profesional
| Tema | Descripción |
|------|-------------|
| GitHub | Repositorios, forks, clone |
| Pull Requests | Cómo hacer y revisar PRs profesionales |
| Git Flow | Feature, develop, release, hotfix branches |
| Convenciones | Conventional Commits, `.gitignore`, `.gitattributes` |
| GitHub Actions (preview) | Introducción a CI/CD (profundizamos en Módulo 15) |
| README profesional | Markdown, badges, estructura |

### Proyecto del Módulo: `portfolio-setup`
> Crear tu repositorio de portafolio en GitHub con estructura profesional. Subir el proyecto de Bash del Módulo 1 con README completo, `.gitignore` correcto y commits bien descritos usando Conventional Commits.

### Evaluación
- ✅ Examen de resolución de escenarios (conflictos, rebase, PR)
- ✅ Revisión del historial de Git del proyecto

### Recursos
- 📗 Pro Git Book (gratuito en git-scm.com)
- 📹 Atlassian Git Tutorials

---

## MÓDULO 3 — Python para Data Engineering
**Duración estimada:** 4 semanas  
**Prerequisito:** Módulo 2  
**Objetivo:** Dominar Python como lenguaje principal de un Data Engineer.

> **Nota:** No aprenderemos Python genérico. Aprenderemos Python orientado a la ingeniería de datos.

### Semana 1 — Fundamentos Sólidos
| Tema | Descripción |
|------|-------------|
| Tipos de datos | `int`, `float`, `str`, `bool`, `None` |
| Estructuras de datos | `list`, `tuple`, `dict`, `set` |
| Control de flujo | `if`, `for`, `while`, `break`, `continue` |
| Funciones | definición, parámetros, `*args`, `**kwargs`, lambdas |
| Comprehensions | List, Dict, Set comprehensions |
| Manejo de errores | `try`, `except`, `finally`, tipos de excepciones |

### Semana 2 — Python Avanzado y POO
| Tema | Descripción |
|------|-------------|
| Clases y objetos | `__init__`, métodos, herencia, encapsulamiento |
| Módulos y paquetes | `import`, `from`, `__init__.py`, namespaces |
| Decoradores | `@staticmethod`, `@classmethod`, decoradores custom |
| Generadores | `yield`, iteradores, memoria eficiente |
| Context Managers | `with`, `__enter__`, `__exit__` |
| Type Hints | `typing`, `Optional`, `Union`, `List`, `Dict` |

### Semana 3 — Manejo de Datos y Archivos
| Tema | Descripción |
|------|-------------|
| Archivos | leer/escribir CSV, JSON, XML, TXT, binario |
| Pathlib | Rutas modernas en Python |
| `json` module | parse, dump, encoding |
| `csv` module | DictReader, DictWriter |
| `xml` module | ElementTree, parsing |
| Requests | HTTP APIs, autenticación, paginación |
| Logging | `logging` module, niveles, formatters, handlers |

### Semana 4 — Entornos y Testing Profesional
| Tema | Descripción |
|------|-------------|
| Virtual Environments | `venv`, `conda` |
| PIP y gestión de dependencias | `requirements.txt`, versiones |
| `pyproject.toml` | Estándar moderno de Python |
| `uv` | Gestor moderno ultrarrápido |
| Testing | `pytest`, fixtures, mocks, parametrize |
| Clean Code | PEP 8, `black`, `ruff`, `mypy` |
| Logging en producción | Estructurado, JSON logs |

### Proyecto del Módulo: `etl-python-básico`
> Pipeline Python que se conecta a una API pública (ej: Open Meteo para clima), descarga datos en JSON, los transforma, valida su calidad, los guarda en CSV y genera un log de ejecución. Con tests unitarios y README.

### Evaluación
- ✅ Examen teórico + ejercicios de código
- ✅ Code review del proyecto (calidad, PEP8, tests, logging)

### Recursos
- 📘 *Python Cookbook* — David Beazley
- 📹 Corey Schafer Python Tutorials (YouTube)
- 🌐 docs.python.org (documentación oficial)

---

## MÓDULO 4 — SQL para Data Engineering
**Duración estimada:** 3 semanas  
**Prerequisito:** Módulo 3  
**Objetivo:** Dominar SQL a nivel avanzado, con énfasis en consultas analíticas y optimización.

### Semana 1 — Fundamentos y Joins
| Tema | Descripción |
|------|-------------|
| ¿Qué es una base de datos relacional? | Tablas, filas, columnas, relaciones |
| Tipos de datos SQL | `INT`, `VARCHAR`, `DATE`, `NUMERIC`, `BOOLEAN` |
| DDL | `CREATE`, `ALTER`, `DROP`, `TRUNCATE` |
| DML | `INSERT`, `UPDATE`, `DELETE`, `MERGE` |
| `SELECT` completo | Proyección, filtros, alias |
| `WHERE` avanzado | `IN`, `BETWEEN`, `LIKE`, `IS NULL` |
| `JOIN` types | `INNER`, `LEFT`, `RIGHT`, `FULL`, `CROSS`, `SELF` |
| `GROUP BY` y `HAVING` | Agregaciones, diferencia con `WHERE` |
| `ORDER BY` y paginación | `LIMIT`, `OFFSET`, `FETCH` |

### Semana 2 — SQL Avanzado y Analítico
| Tema | Descripción |
|------|-------------|
| Subconsultas | Correlacionadas, en `FROM`, en `WHERE`, en `SELECT` |
| CTEs | `WITH`, CTEs múltiples, CTEs recursivas |
| Window Functions | `ROW_NUMBER`, `RANK`, `DENSE_RANK`, `LAG`, `LEAD`, `OVER`, `PARTITION BY` |
| Funciones analíticas | `PERCENTILE_CONT`, `NTILE`, `CUME_DIST` |
| Set operators | `UNION`, `UNION ALL`, `INTERSECT`, `EXCEPT` |
| CASE expressions | `CASE WHEN`, condicionales en SQL |
| Fechas y tiempos | Funciones de fecha, `DATE_TRUNC`, `EXTRACT` |

### Semana 3 — Optimización y Objetos de BD
| Tema | Descripción |
|------|-------------|
| Índices | B-Tree, Hash, compuestos, cuándo usarlos |
| EXPLAIN y EXPLAIN ANALYZE | Leer planes de ejecución |
| Transacciones | `ACID`, `BEGIN`, `COMMIT`, `ROLLBACK`, `SAVEPOINT` |
| Views y Materialized Views | Cuándo usar cada una |
| Stored Procedures | Procedimientos almacenados en PostgreSQL |
| Triggers | Qué son, cuándo usarlos, cuándo evitarlos |
| Particionado | Tablas particionadas por fecha/rango/lista |

### Proyecto del Módulo: `sql-analytics-ecommerce`
> Base de datos PostgreSQL de un e-commerce con 5 tablas. Responder 20 preguntas de negocio con SQL desde básico hasta Window Functions. Incluye esquema, datos de prueba, queries documentadas y análisis de rendimiento con EXPLAIN.

### Evaluación
- ✅ Examen de 15 queries de complejidad creciente
- ✅ Análisis de optimización (dado un query lento, mejorarlo)

### Recursos
- 📘 *Learning SQL* — Alan Beaulieu
- 🌐 pgexercises.com (ejercicios de PostgreSQL)
- 🌐 mode.com/sql-tutorial

---

# FASE 2 — DATOS Y MODELADO

---

## MÓDULO 5 — Modelado de Datos
**Duración estimada:** 3 semanas  
**Prerequisito:** Módulo 4  
**Objetivo:** Diseñar estructuras de datos que soporten análisis eficiente a escala empresarial.

### Semana 1 — Conceptos Fundamentales
| Tema | Descripción |
|------|-------------|
| OLTP vs OLAP | Diferencias, usos, ejemplos reales |
| Normalización | 1NF, 2NF, 3NF, BCNF — cuándo aplicar |
| Desnormalización | Por qué se desnormaliza en analítica |
| Entidades y relaciones | ERD, cardinalidad, claves primarias/foráneas |

### Semana 2 — Dimensional Modeling
| Tema | Descripción |
|------|-------------|
| Star Schema | Fact tables, dimension tables, grain |
| Snowflake Schema | Normalización de dimensiones, pros y cons |
| Fact tables | Métricas, additive/semi-additive/non-additive |
| Dimension tables | Surrogates keys, natural keys, conformado |
| Slowly Changing Dimensions | SCD Type 1, 2, 3, 4, 6 |
| Kimball vs Inmon | Filosofías de modelado, comparación |

### Semana 3 — Modelado Avanzado
| Tema | Descripción |
|------|-------------|
| Data Vault 2.0 | Hubs, Links, Satellites — cuándo usar |
| One Big Table (OBT) | Tendencia moderna, pros y contras |
| Modelado en Data Lakes | Schema-on-read vs Schema-on-write |
| dbt (introducción) | Herramienta de transformación (profundizamos en Módulo 7) |

### Proyecto del Módulo: `dimensional-model-retail`
> Diseñar el modelo dimensional completo de un sistema de retail: diagrama ERD, Star Schema, definición de dimensiones y facts, SCD Type 2 para dimensión de clientes, implementado en PostgreSQL.

### Evaluación
- ✅ Diseñar un modelo dimensional dado un problema de negocio (en tiempo real, sin ayuda)

### Recursos
- 📘 *The Data Warehouse Toolkit* — Ralph Kimball (obligatorio)
- 📘 *Data Vault 2.0* — Dan Linstedt

---

## MÓDULO 6 — Bases de Datos Comparadas
**Duración estimada:** 2 semanas  
**Prerequisito:** Módulo 5  
**Objetivo:** Saber elegir la base de datos correcta para cada problema.

### Contenido por Motor
| Base de Datos | Tipo | Casos de Uso | Cuándo Elegirla |
|--------------|------|--------------|-----------------|
| **PostgreSQL** | Relacional | OLTP, analítica moderada | Primera opción por defecto |
| **MySQL** | Relacional | OLTP web | Proyectos legacy/web |
| **SQL Server** | Relacional | Empresas Microsoft | Ecosistemas Azure |
| **Oracle** | Relacional | Enterprises grandes | Sistemas legacy críticos |
| **MongoDB** | Documental | Datos semiestructurados | JSON flexible, jerarquías |
| **Redis** | Key-Value / Caché | Caché, sesiones, pub/sub | Velocidad extrema |
| **Cassandra** | Wide-Column | IoT, series temporales, escala masiva | Escrituras a millones/segundo |
| **Neo4j** | Grafos | Relaciones complejas, redes sociales | Cuando las relaciones son el dato |

### Proyecto del Módulo: `polyglot-persistence`
> Diseñar e implementar la persistencia de un sistema de e-commerce usando la base de datos correcta para cada caso: transacciones en PostgreSQL, catálogo de productos en MongoDB, sesiones en Redis.

### Evaluación
- ✅ Dado un escenario de negocio, justificar la elección de base de datos

---

## MÓDULO 7 — ETL, ELT y Diseño de Pipelines
**Duración estimada:** 3 semanas  
**Prerequisito:** Módulo 6  
**Objetivo:** Entender los patrones fundamentales de movimiento y transformación de datos.

### Semana 1 — Conceptos de Integración de Datos
| Tema | Descripción |
|------|-------------|
| ETL vs ELT | Qué son, diferencias, cuándo usar cada uno |
| Batch vs Streaming | Procesamiento por lotes vs tiempo real |
| Ingestión de datos | Full load, incremental, CDC (Change Data Capture) |
| Fuentes de datos | APIs REST, bases de datos, archivos, eventos |
| Destinos de datos | Warehouses, Lakes, bases de datos |

### Semana 2 — Diseño de Pipelines
| Tema | Descripción |
|------|-------------|
| Idempotencia | Por qué importa, cómo garantizarla |
| Atomicidad | Todo o nada en un pipeline |
| Manejo de errores | Retry logic, dead letter queues |
| Checkpointing | Recuperación de fallos, reinicio inteligente |
| Paralelismo | Cómo procesar datos en paralelo |
| Particionado de datos | Estrategias para procesar grandes volúmenes |

### Semana 3 — dbt (Data Build Tool)
| Tema | Descripción |
|------|-------------|
| ¿Qué es dbt? | Transformación en el warehouse |
| Modelos dbt | SQL + Jinja templating |
| Materializations | `view`, `table`, `incremental`, `ephemeral` |
| Tests en dbt | `not_null`, `unique`, `accepted_values`, custom |
| Documentation | `schema.yml`, `doc()` blocks |
| Sources y Refs | Linaje de datos |

### Proyecto del Módulo: `etl-pipeline-completo`
> Pipeline ETL completo en Python: extrae datos de 3 APIs públicas (REST), los transforma con lógica de negocio, valida su calidad, los carga a PostgreSQL con dbt. Con manejo de errores, retry, idempotencia, logging y tests.

### Evaluación
- ✅ Diseñar un pipeline dado un caso de uso real
- ✅ Code review del proyecto

---

# FASE 3 — ORQUESTACIÓN Y PROCESAMIENTO

---

## MÓDULO 8 — Apache Airflow
**Duración estimada:** 3 semanas  
**Prerequisito:** Módulo 7  
**Objetivo:** Orquestar pipelines de datos complejos de forma profesional.

### Semana 1 — Fundamentos de Airflow
| Tema | Descripción |
|------|-------------|
| ¿Qué es la orquestación? | Problema que resuelve, alternativas |
| Arquitectura de Airflow | Scheduler, Executor, Worker, Webserver, Metadata DB |
| DAGs | Directed Acyclic Graph, definición, estructura |
| Operators | PythonOperator, BashOperator, SQLOperator, etc. |
| Scheduling | `schedule_interval`, cron expressions, `timedelta` |
| TaskFlow API | `@task`, `@dag` decorators (Airflow 2.x) |

### Semana 2 — Airflow Avanzado
| Tema | Descripción |
|------|-------------|
| Sensors | External sensors, FileSensor, HttpSensor |
| XComs | Comunicación entre tasks |
| Variables y Connections | Gestión de configuraciones y credenciales |
| Branching | `BranchPythonOperator`, ejecución condicional |
| Task Groups | Organización visual de DAGs complejos |
| Dynamic DAGs | Generación programática de DAGs |
| SLAs y alertas | Monitoreo, email alerts, callbacks |

### Semana 3 — Airflow en Producción
| Tema | Descripción |
|------|-------------|
| Executors | SequentialExecutor, LocalExecutor, CeleryExecutor, KubernetesExecutor |
| Docker deployment | `docker-compose` con Airflow completo |
| Testing de DAGs | `pytest` para DAGs, `dagtest` |
| Buenas prácticas | Idempotencia, atomicidad, design patterns |
| Monitoreo | Logs, métricas, alertas |

### Proyecto del Módulo: `airflow-pipeline-orquestado`
> Pipeline orquestado con Airflow: DAG que diariamente extrae datos de 2 APIs, los transforma, valida con Great Expectations y carga a PostgreSQL. Con alertas, retry automático, Docker Compose, tests y README.

### Evaluación
- ✅ Diseñar y explicar un DAG para un caso de uso real
- ✅ Debugging de un DAG roto (examen práctico)

### Recursos
- 🌐 airflow.apache.org (documentación oficial)
- 📘 *Data Pipelines with Apache Airflow* — Bas Harenslak

---

## MÓDULO 9 — Apache Spark y PySpark
**Duración estimada:** 4 semanas  
**Prerequisito:** Módulo 8  
**Objetivo:** Procesar datos a escala masiva con el estándar de la industria.

### Semana 1 — Spark Core y Arquitectura
| Tema | Descripción |
|------|-------------|
| ¿Por qué Spark? | Limitaciones de Hadoop MapReduce |
| Arquitectura | Driver, Executors, Cluster Manager, DAG Scheduler |
| RDDs | Resilient Distributed Datasets, transformaciones y acciones |
| Lazy Evaluation | Por qué es clave para la optimización |
| Particiones | Cómo distribuye Spark los datos |

### Semana 2 — DataFrames y Spark SQL
| Tema | Descripción |
|------|-------------|
| DataFrames | Estructura, schema, tipado |
| Transformaciones | `select`, `filter`, `groupBy`, `agg`, `join` |
| Funciones built-in | `col`, `lit`, `when`, `udf` (y cuándo NO usar UDFs) |
| Spark SQL | Registrar tablas temporales, SQL sobre DataFrames |
| Reading/Writing | Parquet, CSV, JSON, Delta Lake |

### Semana 3 — Optimización
| Tema | Descripción |
|------|-------------|
| Catalyst Optimizer | Cómo Spark optimiza tus queries |
| Shuffle | El peor enemigo del rendimiento |
| Broadcast Joins | Cuándo y cómo usar |
| Caching y Persistence | `cache()`, `persist()`, storage levels |
| Repartition vs Coalesce | Diferencias, cuándo usar cada uno |
| Data Skew | Problema de datos sesgados y soluciones |
| Adaptive Query Execution | AQE en Spark 3.x |

### Semana 4 — Spark Streaming y Casos Reales
| Tema | Descripción |
|------|-------------|
| Structured Streaming | Micro-batch, continuous processing |
| Fuentes de streaming | Kafka, files, sockets |
| Watermarks y Late Data | Manejo de datos tardíos |
| Output modes | `append`, `complete`, `update` |
| Checkpointing en Streaming | Tolerancia a fallos |
| Spark en la nube | Databricks, EMR, Dataproc |

### Proyecto del Módulo: `spark-big-data-processing`
> Procesar un dataset masivo (ej: NYC Taxi Trips, ~10GB) con PySpark: ingestión, limpieza, transformaciones complejas, análisis con Window Functions en Spark SQL, escritura en Parquet particionado. Con Docker, optimización documentada y README.

### Evaluación
- ✅ Examen de optimización: dado código Spark lento, mejorarlo
- ✅ Explicar el plan de ejecución de un query

### Recursos
- 📘 *Learning Spark* — Damji, Wenig, Das, Lee (O'Reilly, 2a edición)
- 🌐 spark.apache.org/docs

---

# FASE 4 — ARQUITECTURAS DE DATOS MODERNAS

---

## MÓDULO 10 — Data Lakes, Warehouses y Lakehouse
**Duración estimada:** 3 semanas  
**Prerequisito:** Módulo 9  
**Objetivo:** Diseñar e implementar plataformas modernas de almacenamiento y análisis de datos.

### Contenido
| Tema | Descripción |
|------|-------------|
| Data Warehouse tradicional | Historia, arquitectura, limitaciones |
| Data Lake | Concepto, ventajas, el problema del "data swamp" |
| Lakehouse | Por qué surge, características, herramientas |
| Medallion Architecture | Bronze → Silver → Gold |
| Bronze Layer | Raw data, sin transformación, append-only |
| Silver Layer | Limpieza, estandarización, validación |
| Gold Layer | Datos listos para negocio, modelado dimensional |
| Delta Lake | ACID en un Data Lake, time travel, schema evolution |
| Apache Iceberg | Alternativa moderna, características |
| Apache Hudi | Upserts en Data Lakes, CDC |
| Comparación | Delta Lake vs Iceberg vs Hudi — cuándo elegir cada uno |

### Proyecto del Módulo: `medallion-lakehouse`
> Implementar arquitectura Medallion completa con Delta Lake: Bronze (ingestión raw), Silver (limpieza y validación), Gold (modelo dimensional). Con PySpark, Docker, catálogo de datos y README.

---

## MÓDULO 11 — Cloud para Data Engineering
**Duración estimada:** 4 semanas  
**Prerequisito:** Módulo 10  
**Objetivo:** Operar en los tres principales clouds con los servicios de datos más importantes.

### Semana 1 — Conceptos de Cloud
| Tema | Descripción |
|------|-------------|
| IaaS, PaaS, SaaS | Modelos de servicio |
| Regiones y zonas | Disponibilidad, latencia, compliance |
| IAM | Identity and Access Management en cloud |
| Pricing | Cómo se cobra, cómo optimizar costos |

### Semana 2 — AWS para Data Engineers
| Servicio | Descripción |
|----------|-------------|
| S3 | Object storage, políticas, versioning, lifecycle |
| Glue | ETL managed, catálogo de datos, crawlers |
| Athena | SQL sobre S3 con Presto |
| Redshift | Data Warehouse managed, distribución de datos |
| Lambda | Serverless para pipelines event-driven |
| Kinesis | Streaming managed |
| EMR | Spark/Hadoop managed |

### Semana 3 — Google Cloud para Data Engineers
| Servicio | Descripción |
|----------|-------------|
| Cloud Storage | GCS, equivalente a S3 |
| BigQuery | Data Warehouse serverless, el mejor del mercado |
| Dataflow | Apache Beam managed (batch y streaming) |
| Dataproc | Spark managed en GCP |
| Pub/Sub | Mensajería y streaming |
| Data Catalog | Gobernanza de datos |

### Semana 4 — Azure para Data Engineers
| Servicio | Descripción |
|----------|-------------|
| Azure Data Lake Storage | ADLS Gen2, equivalente a S3 |
| Azure Data Factory | ETL/ELT managed, visual |
| Synapse Analytics | Warehouse + Spark + SQL integrado |
| Azure Databricks | Spark enterprise en Azure |
| Event Hubs | Kafka-compatible streaming |
| Azure Purview | Gobernanza y catálogo |

### Proyecto del Módulo: `cloud-data-platform`
> Desplegar un pipeline completo en la nube (mínimo un proveedor): ingestión en storage, transformación con Spark/dbt, carga a Warehouse, orquestación con Airflow o servicio managed. Con IaC (Terraform).

---

## MÓDULO 12 — Streaming y Mensajería
**Duración estimada:** 3 semanas  
**Prerequisito:** Módulo 11  
**Objetivo:** Diseñar y construir sistemas que procesen datos en tiempo real a escala.

### Semana 1 — Fundamentos de Streaming
| Tema | Descripción |
|------|-------------|
| ¿Por qué streaming? | Casos de uso donde el batch no basta |
| Arquitectura event-driven | Eventos, productores, consumidores |
| Message brokers | Qué son, cómo funcionan |
| Latencia vs throughput | Trade-offs fundamentales |
| Exactly-once vs At-least-once | Semántica de entrega |

### Semana 2 — Apache Kafka
| Tema | Descripción |
|------|-------------|
| Arquitectura de Kafka | Brokers, topics, partitions, offsets |
| Productores | Configuración, serialización, particionado |
| Consumidores | Consumer groups, offset management |
| Kafka Connect | Conectores source/sink, sin código |
| Kafka Streams | Stream processing nativo |
| Schema Registry | Avro, compatibilidad de schemas |
| Confluent Platform | Enterprise Kafka |

### Semana 3 — Flink y Comparaciones
| Tema | Descripción |
|------|-------------|
| Apache Flink | Stateful stream processing, ventanas |
| Flink vs Spark Streaming | Cuándo elegir cada uno |
| RabbitMQ | Message queue tradicional |
| Kafka vs RabbitMQ | Casos de uso, diferencias |
| Patrones de streaming | CQRS, Event Sourcing, Outbox Pattern |

### Proyecto del Módulo: `real-time-streaming-pipeline`
> Pipeline de streaming completo: productores Python → Kafka → Kafka Connect → PostgreSQL/S3, con Spark Streaming para análisis en tiempo real. Caso de uso: sistema de detección de anomalías en transacciones financieras.

---

# FASE 5 — CLOUD E INFRAESTRUCTURA

---

## MÓDULO 13 — Orquestación Avanzada y Comparaciones
**Duración estimada:** 2 semanas  
**Prerequisito:** Módulo 12  
**Objetivo:** Elegir la herramienta de orquestación correcta y dominarla.

### Comparación de Herramientas
| Aspecto | Airflow | Dagster | Prefect |
|---------|---------|---------|---------|
| Paradigma | Task-centric | Asset-centric | Flow-centric |
| Curva de aprendizaje | Alta | Media | Baja |
| Observabilidad | Básica | Excelente | Muy buena |
| Testing | Complejo | Nativo | Nativo |
| Comunidad | Enorme | Creciente | Buena |
| Cloud managed | MWAA, Astronomer | Dagster Cloud | Prefect Cloud |
| Cuándo elegir | Empresa grande, equipo dedicado | Proyectos modernos, data assets | Simplicidad y rapidez |

### Proyecto del Módulo
> Migrar el pipeline de Airflow (Módulo 8) a Dagster. Documentar diferencias, pros/cons, tiempo de migración.

---

## MÓDULO 14 — Infraestructura como Código
**Duración estimada:** 3 semanas  
**Prerequisito:** Módulo 13  
**Objetivo:** Gestionar infraestructura de datos de forma reproducible y automatizable.

### Contenido
| Tema | Descripción |
|------|-------------|
| Docker | Imágenes, contenedores, Dockerfile, networking |
| Docker Compose | Orquestación local multi-servicio |
| Kubernetes | Pods, Services, Deployments, ConfigMaps, Secrets |
| Helm | Package manager para Kubernetes |
| Terraform | IaC declarativo, providers, state, módulos |
| Infraestructura de datos | Desplegar Airflow, Kafka, Spark en K8s |

### Proyecto del Módulo: `infrastructure-as-code`
> Toda la infraestructura del stack de datos (Airflow + PostgreSQL + Kafka) desplegada con Docker Compose localmente y con Terraform en la nube. Con Helm para Kubernetes.

---

## MÓDULO 15 — CI/CD para Data Pipelines
**Duración estimada:** 2 semanas  
**Prerequisito:** Módulo 14  
**Objetivo:** Automatizar testing, validación y despliegue de pipelines de datos.

### Contenido
| Tema | Descripción |
|------|-------------|
| ¿Por qué CI/CD para datos? | Diferencias con CI/CD de software |
| GitHub Actions | Workflows, jobs, steps, secrets |
| Pipeline de CI para datos | Lint, tests, validación de DAGs |
| Pipeline de CD para datos | Despliegue automatizado a producción |
| Testing de pipelines | Unit, integration, end-to-end |
| GitLab CI | Alternativa enterprise |
| Data-specific CI/CD | dbt Cloud, Astronomer CI |

---

# FASE 6 — CALIDAD Y SEGURIDAD

---

## MÓDULO 16 — Calidad, Observabilidad y Monitoreo de Datos
**Duración estimada:** 2 semanas  
**Prerequisito:** Módulo 15  
**Objetivo:** Garantizar que los datos que llegan sean confiables, completos y correctos.

### Contenido
| Tema | Descripción |
|------|-------------|
| ¿Por qué la calidad de datos importa? | Casos reales de fallas catastróficas |
| Dimensiones de calidad | Completeness, Accuracy, Consistency, Timeliness |
| Great Expectations | Expectations, suites, checkpoints, Data Docs |
| dbt Tests | Built-in y custom tests |
| Soda Core | Alternativa moderna |
| Data Profiling | Estadísticas automáticas de columnas |
| Data Observability | Monte Carlo, Acyclica, custom monitoring |
| Data Contracts | Contratos formales entre productores y consumidores |
| Alertas y SLAs | Cuándo y cómo alertar |

### Proyecto del Módulo: `data-quality-platform`
> Integrar Great Expectations y dbt tests en el pipeline del Módulo 7. Con dashboard de calidad, alertas automáticas y Data Contracts documentados.

---

## MÓDULO 17 — Seguridad y Gobernanza de Datos
**Duración estimada:** 2 semanas  
**Prerequisito:** Módulo 16  
**Objetivo:** Implementar seguridad y cumplimiento en plataformas de datos.

### Contenido
| Tema | Descripción |
|------|-------------|
| IAM en datos | Roles, políticas, principio de menor privilegio |
| Secrets Management | Vault, AWS Secrets Manager, .env |
| Encriptación | En reposo (at rest) y en tránsito (in transit) |
| Column-level security | Enmascaramiento de datos sensibles |
| Row-level security | Acceso granular a filas |
| GDPR y cumplimiento | Right to erasure, data residency |
| Auditoría | Logs de acceso, quién vio qué |
| Data Lineage | Origen y transformaciones de cada dato |
| Data Catalog | Unity Catalog, AWS Glue Catalog, Apache Atlas |

---

# FASE 7 — OPTIMIZACIÓN

---

## MÓDULO 18 — Optimización de Costos y Rendimiento
**Duración estimada:** 2 semanas  
**Prerequisito:** Módulo 17  
**Objetivo:** Construir plataformas eficientes que no quemen el presupuesto de la empresa.

### Contenido
| Tema | Descripción |
|------|-------------|
| Formatos de archivo | Parquet vs ORC vs Avro vs JSON — comparación |
| Compresión | Snappy, GZIP, ZSTD, LZ4 |
| Particionado | Por fecha, categoría, hash — estrategias |
| Clustering y Z-ordering | BigQuery, Delta Lake |
| Caching de queries | Cuándo y cómo cachear |
| Columnar Storage | Por qué es 10x más rápido para analítica |
| Optimización de costos en cloud | S3 storage tiers, BigQuery slots, Redshift RA3 |
| VACUUM y OPTIMIZE | Mantenimiento de tablas Delta/Iceberg |
| Benchmarking | Cómo medir y comparar rendimiento |

---

# FASE 8 — ARQUITECTURA AVANZADA

---

## MÓDULO 19 — Arquitecturas de Datos Modernas
**Duración estimada:** 2 semanas  
**Prerequisito:** Módulo 18  
**Objetivo:** Diseñar arquitecturas de datos de nivel enterprise.

### Contenido
| Arquitectura | Descripción | Cuándo usar |
|-------------|-------------|-------------|
| Lambda Architecture | Batch + Streaming layers | Sistemas mixtos legacy |
| Kappa Architecture | Solo streaming, simplificada | Cuando todo es streaming |
| Data Mesh | Dominios propietarios de datos | Organizaciones grandes |
| Data Fabric | Integración inteligente y virtual | Multi-cloud, legado |
| Event-Driven Architecture | Todo basado en eventos | Microservicios modernos |
| Microservicios de datos | Data products independientes | Escalabilidad horizontal |
| Serverless Data | Sin gestión de infraestructura | Workloads variables |

---

# FASE 9 — PREPARACIÓN LABORAL

---

## MÓDULO 20 — Portafolio, CV y Preparación para Entrevistas
**Duración estimada:** 3 semanas  
**Prerequisito:** Todos los módulos anteriores  
**Objetivo:** Conseguir tu primer trabajo como Data Engineer.

### Semana 1 — Portafolio Profesional
- Estructura de un portafolio de DE
- Cómo documentar proyectos para impresionar
- GitHub profile README
- Los 5 proyectos que debes tener sí o sí
- Qué NO poner en tu portafolio

### Semana 2 — CV y LinkedIn
- CV de Data Engineer que supera ATS
- Cómo cuantificar logros sin experiencia previa
- Optimización de perfil LinkedIn
- Red de contactos: cómo hacer networking real
- Cómo contactar recruiters y hiring managers

### Semana 3 — Entrevistas Técnicas
- Tipos de entrevistas: técnica, sistema design, behavioral
- SQL en entrevistas: los 10 patrones más frecuentes
- Python coding challenges para DE
- System Design: cómo diseñar arquitecturas en vivo
- Behavioral questions: STAR method
- Mock Interviews (simulacros completos)
- Cómo negociar salario

---

# 🏗️ PROYECTOS PROFESIONALES DEL PROGRAMA

| # | Proyecto | Tecnologías | Módulo |
|---|----------|-------------|--------|
| 1 | Pipeline Bash de automatización | Linux, Bash, Cron | 1 |
| 2 | Setup de portafolio profesional | Git, GitHub, Markdown | 2 |
| 3 | ETL con Python y APIs públicas | Python, Requests, CSV, JSON | 3 |
| 4 | Analytics SQL sobre e-commerce | PostgreSQL, Window Functions | 4 |
| 5 | Modelo dimensional de retail | PostgreSQL, Kimball, SCD | 5 |
| 6 | Persistencia poliglota | PostgreSQL, MongoDB, Redis | 6 |
| 7 | ETL completo con dbt | Python, dbt, PostgreSQL | 7 |
| 8 | Pipelines orquestados con Airflow | Airflow, Docker, PostgreSQL | 8 |
| 9 | Procesamiento Big Data con Spark | PySpark, Parquet, Docker | 9 |
| 10 | Arquitectura Medallion con Delta Lake | Spark, Delta Lake, Docker | 10 |
| 11 | Plataforma de datos en la nube | AWS/GCP/Azure, Terraform | 11 |
| 12 | Sistema de streaming en tiempo real | Kafka, Spark Streaming, Python | 12 |
| 13 | Migración Airflow → Dagster | Dagster, Docker | 13 |
| 14 | Infraestructura como código | Docker, Kubernetes, Terraform | 14 |
| 15 | CI/CD para pipelines de datos | GitHub Actions, pytest, dbt | 15 |
| 16 | Plataforma de calidad de datos | Great Expectations, dbt tests | 16 |
| 17 | Sistema seguro con gobernanza | IAM, Vault, Lineage | 17 |
| 18 | Optimización de plataforma existente | Parquet, particionado, benchmarks | 18 |
| 19 | Diseño de arquitectura empresarial | Data Mesh, Architecture diagrams | 19 |
| 20 | **PROYECTO INTEGRADOR FINAL** | Todo el stack | 20 |

---

# 📊 EVALUACIONES DEL PROGRAMA

Cada módulo tiene:
- **Cuestionario de comprensión** (al finalizar la teoría)
- **Ejercicios prácticos** (fáciles → intermedios → difíciles)
- **Revisión de código** (antes de cerrar el módulo)
- **Examen de módulo** (sin respuestas visibles, corrección detallada)

Módulos de alto riesgo (requieren nota mínima del 80% para avanzar):
- Módulo 4 — SQL
- Módulo 9 — Spark
- Módulo 10 — Lakehouse
- Módulo 11 — Cloud
- Módulo 12 — Streaming

---

# 📅 ROADMAP TEMPORAL ESTIMADO

```
MES 1     MES 2     MES 3     MES 4     MES 5     MES 6     MES 7     MES 8+
  │         │         │         │         │         │         │         │
  ▼         ▼         ▼         ▼         ▼         ▼         ▼         ▼
M0+M1     M2+M3     M4+M5     M6+M7     M8+M9    M10+M11   M12+M13  M14→M20
Fundamentos Python   SQL+     ETL+dbt   Airflow+ Lakehouse  Streaming  Infra,
Linux+Git  Core    Modelado             Spark    +Cloud               CI/CD,
                                                                       Carrera
```

> **Nota:** Los tiempos son estimados con 10-15 horas/semana de dedicación. Con mayor dedicación se puede comprimir. Con menor dedicación se debe estirar. **La velocidad real la define tu comprensión.**

---

# 📐 ESTÁNDARES DE CALIDAD DEL PROGRAMA

Todo el código y proyectos que produzcas durante el programa deben cumplir:

- ✅ Versionado en Git con commits descriptivos (Conventional Commits)
- ✅ README.md profesional con objetivo, arquitectura, instrucciones y mejoras futuras
- ✅ Manejo de errores y logging apropiado
- ✅ Tests automatizados
- ✅ Variables de entorno para credenciales (nunca hardcodeadas)
- ✅ Docker para reproducibilidad
- ✅ Documentación de código (docstrings, type hints)
- ✅ Arquitectura documentada con diagramas

---

*Sílabo v1.0 — Programa de Data Engineering Profesional*  
*Diseñado para convertir a un principiante absoluto en un Data Engineer Junior altamente competitivo.*
