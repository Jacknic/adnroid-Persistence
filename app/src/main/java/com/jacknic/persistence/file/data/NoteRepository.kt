package com.jacknic.persistence.file.data

import com.jacknic.persistence.file.model.Note
import java.io.IOException

/**
 * 简单笔记文件数据读取操作
 *
 * @author Jacknic
 */
interface NoteRepository {

    /**
     * 添加笔记
     */
    @Throws(IOException::class)
    fun addNote(note: Note)

    /**
     * 获取指定笔记
     */
    @Throws(IOException::class)
    fun getNote(fileName: String): Note?

    /**
     * 删除笔记
     */
    fun deleteNote(fileName: String): Boolean
}