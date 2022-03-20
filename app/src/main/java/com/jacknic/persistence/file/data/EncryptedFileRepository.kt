package com.jacknic.persistence.file.data

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.jacknic.persistence.file.model.Note
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * 加密文件信息存储
 *
 * @author Jacknic
 */
class EncryptedFileRepository(
    private val context: Context,
    private var isExternal: Boolean,
) : NoteRepository {

    private val pwdString = "12345678"

    override fun addNote(note: Note) {
        noteFile(note.fileName).outputStream().use { fos ->
            ObjectOutputStream(fos).use { oos ->
                oos.writeObject(encrypt(pwdString, note.content))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getNote(fileName: String): Note {
        val note = Note(fileName, "")
        val noteFile = noteFile(fileName)
        if (noteFile.canRead()) {
            noteFile.inputStream().use { fin ->
                ObjectInputStream(fin).use { ois ->
                    val mapFromFile = ois.readObject()
                    val decrypted = decrypt(pwdString, mapFromFile as Map<String, ByteArray>)
                    if (decrypted != null) {
                        note.content = String(decrypted)
                    }
                }
            }
        }
        return note
    }

    override fun deleteNote(fileName: String): Boolean {
        return noteFile(fileName).let { if (it.canWrite()) it.delete() else false }
    }

    private fun noteFile(fileName: String): File {
        val fileDir = if (isExternal) context.getExternalFilesDir(null) else context.filesDir
        return File(fileDir, "$fileName.decrypted")
    }

    /**
     * 信息加密处理
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun encrypt(pwdString: String, plainText: String): Map<String, ByteArray> {
        val random = SecureRandom()
        val salt = ByteArray(256)
        random.nextBytes(salt)

        val pwdChar = pwdString.toCharArray()
        val pbeKeySpec = PBEKeySpec(pwdChar, salt, 1324, 256)
        val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHa1")
        val keyBytes = secretKeyFactory.generateSecret(pbeKeySpec).encoded
        val keySpec = SecretKeySpec(keyBytes, "AES")

        val ivRandom = SecureRandom()
        val iv = ByteArray(16)
        ivRandom.nextBytes(iv)
        val ivSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val encrypted = cipher.doFinal(plainText.toByteArray())
        return mapOf(
            "salt" to salt,
            "iv" to iv,
            "encrypted" to encrypted
        )
    }

    /**
     * 信息解密处理
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun decrypt(pwdString: String, map: Map<String, ByteArray>): ByteArray? {
        var decrypted: ByteArray? = null
        try {
            val salt = map["salt"]
            val iv = map["iv"]
            val encrypted = map["encrypted"]
            val passwordChar = pwdString.toCharArray()
            val pbKeySpec = PBEKeySpec(passwordChar, salt, 1324, 256)
            val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            decrypted = cipher.doFinal(encrypted)
        } catch (e: Exception) {
            Log.e("EncryptedFileRepository", "解密文件失败：", e)
        }
        return decrypted
    }
}