package com.example.localscurestorage.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.io.File
import java.security.MessageDigest


class General {

    companion object {
        private fun String.toSHA256(): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) } // Convert to Hex String
        }

        fun hashString(word: String): String {
            return word.toSHA256();
        }

        private fun getContentResolver(context: Context): ContentResolver {
            try {
                return context.contentResolver;
            } catch (e: Exception) {
                throw e;
            }
        }

        private fun getFileInfo(context: Context, fileUri: Uri): Array<Any> {
            var fileName = ""
            var minFileType = "";
            var fileSize = 0L;
            try {
                val resolver = getContentResolver(context);
                resolver.query(fileUri, null, null, null, null)
                    .use {
                        cursor->
                        if(cursor==null) throw Exception("could not accesss Local Storage")

                        cursor.moveToFirst()
                        val displayNameIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                        val minTypeIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
                        val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)

                        fileName = cursor.getString(displayNameIndex)
                        minFileType = cursor.getString(minTypeIndex)
                        fileSize = cursor.getLong(sizeIndex)

                    }
                return  arrayOf(fileName,minFileType,fileSize)
            } catch (e: Exception) {
                throw e;
            }
        }

        fun encryptionFactory(databaseName: String): SupportFactory {
            val hashDatabaseName = databaseName.toSHA256()
            val passPhraseBytes = SQLiteDatabase.getBytes(hashDatabaseName.toCharArray())
            return SupportFactory(passPhraseBytes)
        }


        fun fileInfoAndByteFromUri(context: Context,fileUri: Uri):Array<Any> {
            val inputStream = context.contentResolver.openInputStream(fileUri)
            val fileInfoArray = getFileInfo(context, fileUri)
            val fileExtention = fileInfoArray[1].toString().split('/')[1]
            val tempFile = File.createTempFile("fffack",fileExtention )
            var fileData: ByteArray = byteArrayOf(0);
            try {
                tempFile.outputStream().use { it ->
                    inputStream?.copyTo(it)
                }
                tempFile.deleteOnExit();
                inputStream?.close()
                fileData =  tempFile.readBytes()
                if(fileData.isEmpty())
                    throw Exception("no file Found")

            } catch (e: Exception) {
                throw e
            }

               return arrayOf(fileInfoArray[0],fileInfoArray[1],fileInfoArray[2],fileData)

        }


    }

}