package com.example.localscurestorage.util

import net.sqlcipher.database.SQLiteDatabase
import androidx.room.Room.databaseBuilder
import net.sqlcipher.database.SupportFactory
import java.security.MessageDigest


class General {

    companion object{
        private fun String.toSHA256(): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) } // Convert to Hex String
        }
        fun hashString(word:String): String {
            return word.toSHA256();
        }

        fun encriptionFactory(databaseName:String): SupportFactory {
            val hashDatabaseName = databaseName.toSHA256()
            val passPhraseBytes = SQLiteDatabase.getBytes(hashDatabaseName.toCharArray())
            return SupportFactory(passPhraseBytes)
        }

    }

}