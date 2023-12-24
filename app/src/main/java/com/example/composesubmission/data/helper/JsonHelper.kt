package com.example.composesubmission.data.helper

import android.content.Context
import com.example.composesubmission.R
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class JsonHelper {
    companion object {

        fun getJsonDataFromAsset(context: Context): String? {
            val jsonString: String?
            try {
                val xmlFileInputStream: InputStream = context.resources.openRawResource(R.raw.rumahdata)
                jsonString = readTextFile(xmlFileInputStream)
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }

        fun readTextFile(inputStream: InputStream): String? {
            val outputStream = ByteArrayOutputStream()
            val buf = ByteArray(1024)
            var len: Int
            try {
                while (inputStream.read(buf).also { len = it } != -1) {
                    outputStream.write(buf, 0, len)
                }
                outputStream.close()
                inputStream.close()
            } catch (e: IOException) {
            }
            return outputStream.toString()
        }

    }
}