package com.example.rescatando_mascotas_forever.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

object ReportGenerator {
    fun generateStatsPdf(context: Context, totalMascotas: Int, totalRescates: Int, totalAdopciones: Int) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.textSize = 16f
        paint.isFakeBoldText = true
        canvas.drawText("REPORTE MENSUAL - RESCATANDO MASCOTAS", 20f, 40f, paint)

        paint.textSize = 12f
        paint.isFakeBoldText = false
        canvas.drawText("Total Mascotas: $totalMascotas", 20f, 80f, paint)
        canvas.drawText("Total Rescates: $totalRescates", 20f, 110f, paint)
        canvas.drawText("Total Adopciones: $totalAdopciones", 20f, 140f, paint)

        pdfDocument.finishPage(page)

        // Usamos el directorio de descargas de la app para evitar problemas de permisos en Android 10+
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Reporte_Admin_${System.currentTimeMillis()}.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF guardado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        pdfDocument.close()
    }
}
