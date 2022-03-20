package com.jacknic.persistence.file.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jacknic.persistence.file.model.Note
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InternalFileRepositoryTest {
    private var appContext = InstrumentationRegistry.getInstrumentation().targetContext
    private var repo = InternalFileRepository(appContext)
    private val fileName = "note1.txt"
    private val note = Note(fileName, "这是测试的笔记内容1234567abc")

    fun setup() {

    }

    @Test
    fun shouldReadContent_afterWrite() {
        repo.addNote(note)
        val noteSaved = repo.getNote(fileName)
        Assert.assertEquals(note, noteSaved)
    }
}