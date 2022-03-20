package com.jacknic.persistence.file.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jacknic.persistence.file.model.Note
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptedFileRepositoryTest {
    private lateinit var repo: EncryptedFileRepository
    private val content = "这是需要加密的内容"
    private val fileName = "encrypt_file.txt"

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        repo = EncryptedFileRepository(context, false)
    }

    @After
    fun destroy() {
        repo.deleteNote(fileName)
    }

    @Test
    fun whenEncrypt_shouldReadSameContent() {
        val note = Note(fileName, content)
        repo.addNote(note)
        val noteSaved = repo.getNote(fileName)
        Assert.assertEquals(note, noteSaved)
    }
}