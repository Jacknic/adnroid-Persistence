package com.jacknic.persistence.file.data

import android.content.Context
import android.util.Log
import com.jacknic.persistence.file.model.Note
import java.io.File

/**
 * 内部文件存储
 *
 * @author Jacknic
 */
class ExternalFileRepository(private val context: Context) : NoteRepository {
    private val TAG = "ExternalFileRepository"

    override fun addNote(note: Note) {
        val noteFile = noteFile(note.fileName)
        if (noteFile.canWrite()) {
            noteFile.outputStream().use {
                it.write(note.content.toByteArray())
            }
        } else {
            Log.w(TAG, "addNote: Failed")
        }
    }

    override fun getNote(fileName: String): Note {
        val note = Note(fileName, "")
        val noteFile = noteFile(note.fileName)
        if (noteFile.canRead()) {
            noteFile.inputStream().use { fin ->
                val content = fin.bufferedReader().use { br -> br.readText() }
                note.content = content
            }
        }
        return note
    }

    override fun deleteNote(fileName: String): Boolean {
        return noteFile(fileName).delete()
    }

    private fun noteFile(fileName: String) = File(context.getExternalFilesDir(null), fileName)
}