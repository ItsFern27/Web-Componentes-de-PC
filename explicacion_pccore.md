# Explicación Detallada del Proyecto PCCore

## 1. ¿Qué es Thymeleaf?

Thymeleaf es un motor de plantillas del lado del servidor para aplicaciones web y entornos de Java. Su principal objetivo es proporcionar una alternativa elegante y de alto rendimiento a los motores de plantillas tradicionales como JSP.

**Características clave de Thymeleaf:**

*   **HTML Natural:** Las plantillas de Thymeleaf son archivos HTML válidos que pueden ser abiertos directamente en un navegador. Esto significa que los diseñadores pueden trabajar en las plantillas sin necesidad de un servidor de aplicaciones, lo que facilita la colaboración entre diseñadores y desarrolladores.
*   **Integración con Spring Framework:** Se integra muy bien con Spring Boot y Spring MVC, permitiendo que los datos del backend (controladores, modelos) se inyecten fácilmente en las vistas HTML.
*   **Sintaxis Expresiva:** Utiliza atributos especiales (`th:text`, `th:each`, `th:if`, `th:action`, etc.) para procesar los datos y la lógica en el HTML.
*   **Seguridad:** Ofrece protección contra ataques comunes como Cross-Site Scripting (XSS) al escapar automáticamente el contenido.

En tu proyecto, los archivos `.html` dentro de `src/main/resources/templates` son plantillas de Thymeleaf. El servidor Spring Boot procesa estas plantillas, inyecta los datos que le envían los controladores y genera el HTML final que se envía al navegador del usuario.

## 2. Página Principal (index.html)

La página principal muestra los productos disponibles.

### 2.1. Controlador (`IndexController.java`)

*   **Manejo de Rutas:** El método `getIndex` está anotado con `@GetMapping({"/", "/index"})`, lo que significa que se ejecuta cuando un usuario accede a la raíz del sitio (`/`) o a `/index`.
*   **Inyección de Dependencias:** El controlador tiene inyectados `ProductosRepository` y `CategoriasRepository`. Estos son interfaces de Spring Data JPA que permiten interactuar con la base de datos para obtener información de productos y categorías.
*   **Obtención de Datos:**
    *   `List<Productos> productos = productosRepository.findAllWithItems();`: Esta línea es clave. Llama a un método (probablemente personalizado en `ProductosRepository`) que recupera una lista de todos los productos de la base de datos, incluyendo sus `ItemProducto` asociados (que son las variantes o detalles específicos de cada producto, como color, tamaño, etc.).
    *   `List<Productos> novedades = productosRepository.findTop3ByOrderByIdDesc();`: Esta línea obtiene los 3 productos más recientes para mostrarlos como "novedades".
*   **Paso de Datos a la Vista:**
    *   `model.addAttribute("productos", productos);`: Añade la lista completa de productos al objeto `Model`. Esta lista estará disponible en la plantilla HTML bajo el nombre "productos".
    *   `model.addAttribute("novedades", novedades);`: De manera similar, añade la lista de novedades bajo el nombre "novedades".
*   **Renderizado de la Vista:**
    *   `return "index";`: Indica a Spring Boot que debe renderizar la plantilla Thymeleaf llamada `index.html` (ubicada en `src/main/resources/templates/index.html`), pasándole los datos que se agregaron al `Model`.

### 2.2. Plantilla Thymeleaf (`index.html`)

La plantilla `index.html` recibe las listas "productos" y "novedades" del controlador y utiliza Thymeleaf para mostrarlas.

*   **Fragmentos Reutilizables:** Incluye un encabezado y un pie de página (`fragments/header.html` y `fragments/footer.html`) usando `th:replace`, lo que ayuda a mantener el código modular y DRY (Don't Repeat Yourself).
*   **Carrusel de Novedades:**
    *   Utiliza `th:if` para asegurarse de que solo se renderice el carrusel si hay productos en la lista `novedades`.
    *   `th:each="novedad, iterStat : ${novedades}"` itera sobre cada objeto `Producto` en la lista `novedades` que el controlador le pasó.
    *   `th:classappend` se usa para marcar el primer elemento del carrusel como `active`.
    *   La lógica para las imágenes (`th:if` anidados) verifica si la URL de la imagen es absoluta (empieza con `http`) o relativa (se asume que está en `/static/imagenes/`) y muestra un placeholder si no hay imagen.
    *   `th:text="${novedad.nombre}"`, `th:text="${novedad.descripcion}"`: Muestra imágenes, nombres y descripciones de cada novedad.
*   **Listado de Productos Destacados:**
    *   Similar al carrusel, `th:each="producto : ${productos}"` itera sobre la lista completa de productos.
    *   Por cada producto, se crea una "card" de Bootstrap.
    *   La gestión de imágenes es idéntica a la del carrusel.
    *   `th:text` se utiliza para mostrar el nombre, marca, modelo, precio formateado (`precioStr`) y descripción del producto.
    *   `th:href="@{/producto/{id}(id=${producto.id})}"` genera dinámicamente un enlace a la página de detalles de cada producto, utilizando su `id`.

## 3. Página de Contacto (contacto.html)

### 3.1. Controlador (`IndexController.java`)

*   El método `getContacto()` está anotado con `@GetMapping("/contacto")`.
*   Simplemente devuelve la cadena `"contacto"`, lo que indica que Spring Boot renderizará la plantilla `contacto.html`.
*   No se añade ningún objeto `Model` con datos al contexto.

### 3.2. Plantilla (`contacto.html`)

*   Contiene un formulario HTML con campos para nombre, email, asunto y mensaje.
*   El formulario tiene `th:action="@{/contacto}"` y `method="post"`.

### 3.3. Funcionamiento Actual y Limitación

*   Actualmente, el `IndexController` solo maneja las solicitudes GET a `/contacto`.
*   **No hay un método `@PostMapping("/contacto")` correspondiente** en el backend para recibir y procesar los datos enviados por el formulario.
*   Si un usuario envía el formulario, la solicitud POST no será manejada, lo que probablemente resultará en un error HTTP (como un "405 Method Not Allowed").
*   **Para que sea funcional:** Se necesitaría implementar un método `@PostMapping("/contacto")` en un controlador para recibir los datos del formulario, procesarlos (ej. enviar un email, guardar en DB) y devolver una respuesta adecuada.

## 4. Panel de Administración, Menú de Administración y Gestión de Productos

### 4.1. Visión General de `AdminController.java`

*   **`@Controller` y `@RequestMapping("/admin")`**: Esta clase es un controlador de Spring MVC, y todas sus rutas comienzan con `/admin`.
*   **Dependencias**: Inyecta `ProductosRepository`, `ItemProductoRepository`, `CategoriasRepository`, y `ProveedoresRepository` para interactuar con las entidades de la base de datos.

### 4.2. Admin Dashboard (`admin_dashboard.html`)

*   **`@GetMapping` (para `/admin`)**: El método `adminHome()` maneja la ruta `/admin` y devuelve la plantilla `admin/admin_dashboard.html`.
*   Es el punto de entrada principal a la sección de administración, probablemente mostrando un resumen o enlaces a otras funcionalidades.

### 4.3. Admin Menu (`admin_menu.html` - Inferido)

*   Es un fragmento de Thymeleaf (`th:replace="~{admin/admin_menu :: admin_menu}"`) incluido en las páginas de administración.
*   Contiene enlaces de navegación a las diferentes secciones de gestión (productos, categorías, ítems, proveedores, búsqueda avanzada).

### 4.4. Gestión de Productos (`gestionar_productos.html`) y Operaciones CRUD

Esta sección es manejada por varios métodos en `AdminController` que operan sobre la ruta `/admin/productos`.

#### 4.4.1. Listar/Leer Productos (Read)

*   **Backend (`AdminController.java`):**
    *   **`@GetMapping("/productos")`**: El método `gestionarProductos()`:
        *   Obtiene todos los `Productos` de la base de datos con `productosRepository.findAll()`.
        *   Añade esta lista al `Model` bajo la clave "productos".
        *   Añade un objeto `Productos` vacío (`productoNuevo`) para el formulario de creación.
        *   Renderiza la plantilla `admin/gestionar_productos.html`.
*   **Frontend (`gestionar_productos.html`):**
    *   Muestra los productos en una tabla (`<table class="table">`).
    *   `th:each="producto : ${productos}"`: Itera sobre la lista de productos.
    *   `th:text="${producto.id}"`, `th:text="${producto.nombre}"`, etc.: Muestra las propiedades de cada producto en celdas de la tabla.

#### 4.4.2. Crear Producto (Create)

*   **Frontend (`gestionar_productos.html`):**
    *   Un botón "Crear Nuevo Producto" activa un modal de Bootstrap.
    *   Dentro del modal, un formulario (`<form th:action="@{/admin/productos/crear}" th:object="${productoNuevo}" method="post">`) permite introducir los datos del nuevo producto.
    *   `th:field="*{nombre}"`, etc.: Vincula los campos de entrada a las propiedades del objeto `productoNuevo`.
*   **Backend (`AdminController.java`):**
    *   **`@PostMapping("/productos/crear")`**: El método `crearProducto()`:
        *   Recibe los datos del formulario mapeados a un objeto `Productos` (`@ModelAttribute("productoNuevo") Productos producto`).
        *   Guarda el nuevo producto en la base de datos con `productosRepository.save(producto)`.
        *   Maneja `DataIntegrityViolationException` (ej. si el modelo ya existe).
        *   Utiliza `RedirectAttributes` para añadir mensajes de éxito/error.
        *   Redirige a `/admin/productos`.

#### 4.4.3. Editar/Actualizar Producto (Update)

*   **Frontend (`gestionar_productos.html`):**
    *   Un botón "Editar" en cada fila de la tabla activa un modal de edición específico para ese producto.
    *   Dentro del modal, un formulario (`<form th:action="@{/admin/productos/editar/{id}(id=${producto.id})}" method="post">`) con campos pre-rellenados (`th:value="${producto.nombre}"`) permite modificar los datos.
*   **Backend (`AdminController.java`):**
    *   **`@PostMapping("/productos/editar/{id}")`**: El método `editarProducto()`:
        *   Obtiene el ID del producto de la URL (`@PathVariable Long id`).
        *   Recibe los datos actualizados del formulario mapeados a un objeto `Productos` (`@ModelAttribute Productos producto`).
        *   Establece el ID en el objeto `producto` (`producto.setId(id)`) para asegurar que `productosRepository.save(producto)` realice una actualización.
        *   Maneja `DataIntegrityViolationException`.
        *   Redirige a `/admin/productos`.

#### 4.4.4. Eliminar Producto (Delete)

*   **Frontend (`gestionar_productos.html`):**
    *   Un botón "Eliminar" en cada fila de la tabla, envuelto en un formulario (`<form th:action="@{/admin/productos/eliminar/{id}(id=${producto.id})}" method="post">`).
*   **Backend (`AdminController.java`):**
    *   **`@PostMapping("/productos/eliminar/{id}")`**: El método `eliminarProducto()`:
        *   Obtiene el ID del producto de la URL (`@PathVariable Long id`).
        *   **Validación de Integridad:** `itemProductoRepository.existsByProductoId(id)`: Verifica si existen `ItemProducto`s asociados. Si los hay, impide la eliminación y muestra un mensaje de error.
        *   Elimina el producto de la base de datos con `productosRepository.deleteById(id)`.
        *   Maneja excepciones.
        *   Redirige a `/admin/productos`.

### 4.5. Otras Secciones de Administración

El `AdminController` también incluye lógica CRUD similar para:

*   **`gestionar_categorias`**: Listar, crear, editar y eliminar categorías.
*   **`gestionar_items`**: Listar, crear, editar y eliminar `ItemProducto`s. La edición de ítems tiene una lógica específica para la `fecha_ingreso`.
*   **`gestionar_proveedores`**: Solo un `@GetMapping`, sugiriendo que el CRUD completo podría estar en otro lugar o no implementado aún.
*   **`busqueda_avanzada`**: Una funcionalidad de búsqueda potente para `ItemProducto`s, utilizando `ItemProductoSpecification` para construir consultas dinámicas.
