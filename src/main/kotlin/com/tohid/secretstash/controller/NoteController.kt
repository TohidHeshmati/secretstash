package com.tohid.secretstash.controller

import com.tohid.secretstash.dtos.NoteRequest
import com.tohid.secretstash.dtos.NoteResponse
import com.tohid.secretstash.service.NoteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/notes")
class NoteController(
    private val noteService: NoteService
) {
    @PostMapping
    fun create(
        @RequestBody request: NoteRequest
    ): ResponseEntity<NoteResponse> {
        val note = noteService.createNote(request)
        return ResponseEntity.status(201).body(note)
    }

    @GetMapping
    fun getMyNotes(): List<NoteResponse> = noteService.getMyNotes()

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long
    ): NoteResponse = noteService.getNoteById(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: NoteRequest
    ): NoteResponse = noteService.updateNote(id, request)

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        noteService.deleteNote(id)
        return ResponseEntity.noContent().build()
    }
}
