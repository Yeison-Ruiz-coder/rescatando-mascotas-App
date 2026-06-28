package com.example.rescatando_mascotas_forever.utils

object Constants {
    const val BASE_URL = "https://rescatando-mascotas-backend-final-production.up.railway.app/"

    fun getImageUrl(path: String?): String {
        if (path.isNullOrEmpty() || path == "null" || path.isBlank()) {
            return "https://via.placeholder.com/600x400?text=Sin+Imagen"
        }
        if (path.startsWith("http")) return path
        
        // Limpiamos el path de prefijos que Laravel suele devolver
        var cleanPath = path
            .removePrefix("/")
            .removePrefix("public/")
            .removePrefix("storage/")
            .removePrefix("public/storage/")
            .removePrefix("/")
            .trim()
        
        // Caso especial: si después de quitar public/ quedó storage/, lo quitamos también
        if (cleanPath.startsWith("storage/")) {
            cleanPath = cleanPath.removePrefix("storage/").removePrefix("/")
        }
        
        // Evitamos que quede como "null" si hubo algún error de casteo previo
        if (cleanPath == "null") return "https://via.placeholder.com/600x400?text=Sin+Imagen"
        
        return "${BASE_URL}storage/${cleanPath.trimStart('/')}"
    }
}
