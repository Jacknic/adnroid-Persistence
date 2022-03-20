package com.jacknic.persistence.file.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jacknic.persistence.databinding.ActivityFileBinding
import com.jacknic.persistence.file.data.EncryptedFileRepository
import com.jacknic.persistence.file.data.ExternalFileRepository
import com.jacknic.persistence.file.data.InternalFileRepository
import com.jacknic.persistence.file.data.NoteRepository
import com.jacknic.persistence.file.model.Note

/**
 * 文件存储数据例子
 *
 * @author Jacknic
 */
class FileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFileBinding
    private var repo: NoteRepository = InternalFileRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRead.setOnClickListener { readNote() }
        binding.btnWrite.setOnClickListener { writeNote() }
        binding.btnDelete.setOnClickListener { deleteNote() }
        binding.cbExternal.setOnCheckedChangeListener { _, _ -> selectRepoType() }
        binding.cbEncrypted.setOnCheckedChangeListener { _, _ -> selectRepoType() }
    }

    /**
     * 读取记事
     */
    private fun readNote() {
        val fileName = binding.etFileName.text.toString()
        if (fileName.isBlank()) {
            Toast.makeText(this, "文件名不能为空", Toast.LENGTH_SHORT).show()
        } else {
            val note = repo.getNote(fileName)
            binding.etContent.setText(note?.content)
        }
    }

    /**
     * 写入记事
     */
    private fun writeNote() {
        val fileName = binding.etFileName.text.toString()
        if (fileName.isBlank()) {
            Toast.makeText(this, "文件名不能为空", Toast.LENGTH_SHORT).show()
        } else {
            val content = binding.etContent.text.toString()
            try {
                repo.addNote(Note(fileName, content))
            } catch (e: Exception) {
                Toast.makeText(this@FileActivity, "写入文件失败", Toast.LENGTH_SHORT).show()
            }
            clearInput()
        }
    }

    private fun clearInput() {
        binding.etFileName.text.clear()
        binding.etContent.text.clear()
    }

    /**
     * 删除记事
     */
    private fun deleteNote() {
        val fileName = binding.etFileName.text.toString()
        if (fileName.isBlank()) {
            Toast.makeText(this, "文件名不能为空", Toast.LENGTH_SHORT).show()
        } else {
            repo.deleteNote(fileName)
            clearInput()
        }
    }

    /**
     * 更新选择存储类型
     */
    private fun selectRepoType() {
        val isExternal = binding.cbExternal.isChecked
        val isEncrypted = binding.cbEncrypted.isChecked
        val selectRepo: NoteRepository = if (isEncrypted) {
            EncryptedFileRepository(this, isExternal)
        } else {
            if (isExternal) {
                ExternalFileRepository(this)
            } else {
                InternalFileRepository(this)
            }
        }
        repo = selectRepo
    }
}
