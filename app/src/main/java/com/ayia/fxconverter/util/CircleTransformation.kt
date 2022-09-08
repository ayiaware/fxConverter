package com.ayia.fxconverter.util

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import kotlin.math.min


class CircleTransform : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return circleCrop(pool, toTransform)!!
    }

    val id: String
        get() = javaClass.name

    companion object {
        private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
            if (source == null) return null
            val size = min(source.width, source.height)
            val x = (source.width - size) / 2
            val y = (source.height - size) / 2
            val squared = Bitmap.createBitmap(source, x, y, size, size)

            var result: Bitmap? = pool[size, size, Bitmap.Config.ARGB_8888]

            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            }
            val canvas = Canvas(result!!)

            val paint = Paint()

            paint.shader =
                BitmapShader(squared,Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.isAntiAlias = true
            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)
            return result
        }
    }
}
//class GlideCircleTransformation : BitmapTransformation() {
//    override fun transform(
//        pool: BitmapPool,
//        source: Bitmap,
//        outWidth: Int,
//        outHeight: Int
//    ): Bitmap {
//        return bitmapTransform(source)
//    }
//
//    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
//    }
//
//    val id: String get() = "Glide_Circle_Transformation"
//}
//
//private fun bitmapTransform(source: Bitmap): Bitmap {
//    val size = min(source.width, source.height)
//    val x = (source.width - size) / 2
//    val y = (source.height - size) / 2
//    val result = Bitmap.createBitmap(source, x, y, size, size)
//    if (result != source) {
//        source.recycle()
//    }
//
//    val bitmap = Bitmap.createBitmap(size, size, result.config)
//
//    val canvas = Canvas(bitmap)
//    val paint = Paint()
//    val shader = BitmapShader(result, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//    paint.shader = shader
//    paint.isAntiAlias = true
//
//    val r = size / 2f
//    canvas.drawCircle(r, r, r, paint)
//    result.recycle()
//
//    return bitmap
//}