package com.jacknic.persistence.file.data

import android.content.Context
import com.jacknic.persistence.file.model.Note
import java.io.File

/**
 * 内部文件存储
 *
 * @author Jacknic
 */
class InternalFileRepository(private val context: Context) : NoteRepository {

    override fun addNote(note: Note) {
        context.openFileOutput(note.fileName, Context.MODE_PRIVATE).use {
            it.write(note.content.toByteArray())
        }
    }

    override fun getNote(fileName: String): Note {
        val note = Note(fileName, "")
        val noteFile = noteFile(fileName)
        if (noteFile.canRead()) {
            context.openFileInput(fileName).use {
                val content = it.bufferedReader().use { br -> br.readText() }
                note.content = content
            }
        }
        return note
    }

    override fun deleteNote(fileName: String): Boolean {
        val noteFile = noteFile(fileName)
        if (noteFile.canWrite()) {
            return context.deleteFile(fileName)
        }
        return false
    }

    private fun noteFile(fileName: String) = File(context.filesDir, fileName)
}