# Estructura de Diapositivas para Presentación PCCore

---

### Diapositiva 1: Título

*   **Título:** Análisis Técnico del Proyecto "PCCore"
*   **Subtítulo:** Funcionamiento de la Página Principal, Contacto y Administración de Productos
*   **Tu Nombre**
*   **Fecha**

---

### Diapositiva 2: Introducción al Proyecto

*   **Título:** ¿Qué es PCCore?
*   **Puntos Clave:**
    *   Aplicación web para la venta de componentes de PC.
    *   Desarrollada con Spring Boot (Java) en el backend.
    *   Utiliza Thymeleaf para la generación de vistas HTML dinámicas.
    *   Base de datos relacional para almacenar productos, usuarios, ventas, etc.

---

### Diapositiva 3: Entendiendo Thymeleaf

*   **Título:** Thymeleaf: El Motor de Plantillas
*   **Puntos Clave:**
    *   **¿Qué es?** Motor de plantillas del lado del servidor para Java.
    *   **HTML Natural:** Las plantillas son HTML válidas, se pueden abrir directamente en el navegador.
    *   **Integración con Spring:** Se integra perfectamente con Spring Boot y Spring MVC.
    *   **Sintaxis:** Utiliza atributos especiales (`th:text`, `th:each`, `th:if`, `th:action`) para inyectar datos y lógica.
    *   **Función en PCCore:** Transforma plantillas HTML estáticas en páginas dinámicas con datos del backend.

---

### Diapositiva 4: Página Principal (index.html) - Visión General

*   **Título:** La Página Principal: Mostrando Nuestros Productos
*   **Puntos Clave:**
    *   **Propósito:** Punto de entrada para los usuarios, muestra productos destacados y novedades.
    *   **Componentes:** Carrusel de novedades, listado de productos.
    *   **Tecnologías:** Spring Boot (backend), Thymeleaf (frontend), Bootstrap (estilos).

---

### Diapositiva 5: Página Principal - Backend (IndexController)

*   **Título:** `IndexController.java`: El Cerebro de la Página Principal
*   **Puntos Clave:**
    *   **Rutas:** Maneja `/` y `/index` con el método `getIndex()`.
    *   **Obtención de Datos:**
        *   `productosRepository.findAllWithItems()`: Recupera todos los productos (con sus ítems).
        *   `productosRepository.findTop3ByOrderByIdDesc()`: Obtiene los 3 productos más recientes (novedades).
    *   **Paso de Datos a la Vista:**
        *   `model.addAttribute("productos", listaProductos)`
        *   `model.addAttribute("novedades", listaNovedades)`
    *   **Renderizado:** Devuelve `"index"` para cargar `index.html`.

---

### Diapositiva 6: Página Principal - Frontend (index.html)

*   **Título:** `index.html`: Dando Vida a los Productos
*   **Puntos Clave:**
    *   **Fragmentos:** Incluye `fragments/header.html` y `fragments/footer.html` para modularidad.
    *   **Carrusel de Novedades:**
        *   `th:if` para mostrar solo si hay novedades.
        *   `th:each="novedad : ${novedades}"`: Itera sobre la lista de novedades.
        *   `th:src`, `th:text`: Muestra imágenes, nombres y descripciones de cada novedad.
    *   **Listado de Productos:**
        *   `th:each="producto : ${productos}"`: Itera sobre la lista completa de productos.
        *   Muestra detalles (nombre, marca, modelo, precio, descripción) y un botón "Comprar" (`th:href` dinámico).
    *   **Lógica de Imágenes:** Maneja URLs absolutas, relativas y placeholders.

---

### Diapositiva 7: Página de Contacto (contacto.html) - Visión General

*   **Título:** La Página de Contacto: Conectando con el Usuario
*   **Puntos Clave:**
    *   **Propósito:** Permite a los usuarios enviar consultas o comentarios.
    *   **Interfaz:** Contiene un formulario con campos para nombre, email, asunto y mensaje.
    *   **Tecnologías:** Thymeleaf (frontend), Bootstrap (estilos).

---

### Diapositiva 8: Página de Contacto - Funcionamiento Actual

*   **Título:** `contacto.html`: Formulario sin Procesamiento Backend
*   **Puntos Clave:**
    *   **Ruta:** `IndexController` tiene un `@GetMapping("/contacto")` que solo renderiza la plantilla.
    *   **Formulario:** El `contacto.html` tiene un `<form th:action="@{/contacto}" method="post">`.
    *   **Limitación Actual:** **No existe un `@PostMapping("/contacto")`** en el backend.
    *   **Resultado:** El envío del formulario actualmente no es procesado por el servidor (posiblemente un error 405).
    *   **Mejora Necesaria:** Implementar un método `@PostMapping` en un controlador para recibir y procesar los datos del formulario (ej. enviar email, guardar en DB).

---

### Diapositiva 9: Sección de Administración - Visión General

*   **Título:** Panel de Administración: Control Total del Contenido
*   **Puntos Clave:**
    *   **Propósito:** Gestionar productos, categorías, ítems, proveedores y otras configuraciones.
    *   **Controlador Principal:** `AdminController.java` (todas las rutas bajo `/admin`).
    *   **Seguridad:** (Mencionar si aplica, ej. requiere autenticación de usuario).

---

### Diapositiva 10: Admin Dashboard y Menú

*   **Título:** `admin_dashboard.html` y `admin_menu.html`
*   **Puntos Clave:**
    *   **Dashboard:** `AdminController` con `@GetMapping("/admin")` devuelve `admin/admin_dashboard.html`. Es la página de inicio del panel.
    *   **Menú:** `admin_menu.html` es un fragmento incluido en las páginas de administración.
    *   **Navegación:** Proporciona enlaces a las diferentes secciones de gestión (productos, categorías, ítems, etc.).

---

### Diapositiva 11: Gestión de Productos (`gestionar_productos.html`)

*   **Título:** `gestionar_productos.html`: CRUD de Productos
*   **Puntos Clave:**
    *   **Propósito:** Listar, crear, editar y eliminar productos.
    *   **Controlador:** Métodos específicos en `AdminController` para `/admin/productos`.
    *   **Interfaz:** Tabla para listar productos, modales de Bootstrap para formularios de creación y edición.
    *   **Mensajes:** Muestra `successMessage` y `errorMessage` del controlador.

---

### Diapositiva 12: CRUD - Listar Productos (Read)

*   **Título:** Listar Productos (`@GetMapping("/admin/productos")`)
*   **Puntos Clave:**
    *   **Backend:** `AdminController.gestionarProductos()`:
        *   `productosRepository.findAll()`: Obtiene todos los productos.
        *   `model.addAttribute("productos", ...)`: Pasa la lista a la vista.
        *   `model.addAttribute("productoNuevo", new Productos())`: Inicializa un objeto vacío para el formulario de creación.
    *   **Frontend:** `gestionar_productos.html`:
        *   `<table class="table">`: Muestra los productos en una tabla.
        *   `<tr th:each="producto : ${productos}">`: Itera sobre la lista de productos.
        *   `th:text="${producto.propiedad}"`: Muestra los detalles de cada producto.

---

### Diapositiva 13: CRUD - Crear Producto (Create)

*   **Título:** Crear Producto (`@PostMapping("/admin/productos/crear")`)
*   **Puntos Clave:**
    *   **Interfaz:** Botón "Crear Nuevo Producto" abre un modal con un formulario.
    *   **Formulario:** `<form th:action="@{/admin/productos/crear}" th:object="${productoNuevo}" method="post">`
        *   `th:field="*{nombre}"`, etc.: Vincula campos a propiedades del objeto `productoNuevo`.
    *   **Backend:** `AdminController.crearProducto()`:
        *   `@ModelAttribute("productoNuevo") Productos producto`: Recibe los datos del formulario.
        *   `productosRepository.save(producto)`: Guarda el nuevo producto en la DB.
        *   Manejo de `DataIntegrityViolationException` (ej. modelo duplicado).
        *   Redirige a `/admin/productos` con mensaje de éxito/error.

---

### Diapositiva 14: CRUD - Editar Producto (Update)

*   **Título:** Editar Producto (`@PostMapping("/admin/productos/editar/{id}")`)
*   **Puntos Clave:**
    *   **Interfaz:** Botón "Editar" en cada fila de la tabla abre un modal de edición.
    *   **Formulario:** `<form th:action="@{/admin/productos/editar/{id}(id=${producto.id})}" method="post">`
        *   Campos pre-rellenados con `th:value="${producto.propiedad}"`.
    *   **Backend:** `AdminController.editarProducto()`:
        *   `@PathVariable Long id`: Obtiene el ID del producto de la URL.
        *   `@ModelAttribute Productos producto`: Recibe los datos actualizados.
        *   `producto.setId(id)`: Asegura que el ID se establezca para la actualización.
        *   `productosRepository.save(producto)`: Actualiza el producto existente en la DB.
        *   Manejo de `DataIntegrityViolationException`.
        *   Redirige a `/admin/productos` con mensaje de éxito/error.

---

### Diapositiva 15: CRUD - Eliminar Producto (Delete)

*   **Título:** Eliminar Producto (`@PostMapping("/admin/productos/eliminar/{id}")`)
*   **Puntos Clave:**
    *   **Interfaz:** Botón "Eliminar" en cada fila de la tabla (dentro de un formulario POST).
    *   **Formulario:** `<form th:action="@{/admin/productos/eliminar/{id}(id=${producto.id})}" method="post">`
    *   **Backend:** `AdminController.eliminarProducto()`:
        *   `@PathVariable Long id`: Obtiene el ID del producto a eliminar.
        *   **Validación de Integridad:** `itemProductoRepository.existsByProductoId(id)`: **Crucial** para evitar eliminar productos con ítems asociados.
        *   `productosRepository.deleteById(id)`: Elimina el producto de la DB.
        *   Manejo de excepciones.
        *   Redirige a `/admin/productos` con mensaje de éxito/error.

---

### Diapositiva 16: Conclusión y Preguntas

*   **Título:** Resumen y Preguntas
*   **Puntos Clave:**
    *   El proyecto PCCore demuestra una arquitectura robusta con Spring Boot y Thymeleaf.
    *   La separación de responsabilidades (Controladores, Repositorios, Vistas) facilita el desarrollo y mantenimiento.
    *   Thymeleaf permite crear interfaces dinámicas y amigables.
    *   La sección de administración es fundamental para la gestión del contenido.
*   **¡Gracias por su atención!**
*   **¿Preguntas?**
