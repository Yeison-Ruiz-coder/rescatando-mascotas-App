package com.example.rescatando_mascotas_forever.utils

object Constants {
    const val BASE_URL = "https://rescatando-mascotas-backend-final-production.up.railway.app/"

    fun getImageUrl(path: String?): String {
        // Log para depurar: Busca "Constants" en el Logcat para ver qué llega del servidor
        android.util.Log.d("Constants", "URL Recibida: $path")

        if (path.isNullOrEmpty() || path == "null" || path.isBlank()) {
            return "https://via.placeholder.com/600x400?text=Sin+Imagen"
        }

        // 1. Limpieza profunda: Quitar comillas y corregir escapes \/
        val normalizedPath = path.trim()
            .removeSurrounding("\"")
            .removeSurrounding("'")
            .replace("\\/", "/")

        // 2. Si ya es una URL completa (Cloudinary)
        if (normalizedPath.startsWith("http")) {
            // CORRECCIÓN DE FORMATO: Android < 12 no soporta .avif bien. 
            // Si es Cloudinary, cambiamos la extensión a .jpg para asegurar que se vea.
            return if (normalizedPath.contains("cloudinary.com") && 
                (normalizedPath.lowercase().endsWith(".avif") || normalizedPath.lowercase().endsWith(".webp"))) {
                normalizedPath.substringBeforeLast(".") + ".jpg"
            } else {
                normalizedPath
            }
        }
        
        // 3. Limpiamos el path de prefijos que Laravel suele devolver para archivos locales
        var cleanPath = normalizedPath
            .removePrefix("/")
            .removePrefix("public/")
            .removePrefix("storage/")
            .removePrefix("public/storage/")
            .removePrefix("/")
            .trim()
        
        if (cleanPath.startsWith("storage/")) {
            cleanPath = cleanPath.removePrefix("storage/").removePrefix("/")
        }
        
        if (cleanPath == "null") return "https://via.placeholder.com/600x400?text=Sin+Imagen"
        
        return "${BASE_URL}storage/${cleanPath.trimStart('/')}"
    }
}
