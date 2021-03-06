package io.github.manuelernesto.kotlinepring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Table("AUTHOR")
data class Author(@Id var id: Long?, val name: String, val birth: String)

@RestController
class AuthorControl(val service: AuthorService) {

    @GetMapping
    fun getAll() = service.getAll()


    @PostMapping
    fun save(@RequestBody author: Author): ResponseEntity<Author> {
        service.postAuthor(author)
        return ResponseEntity.ok().build()
    }


    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        val isAuthor = service.getAuthor(id)

        if (isAuthor.isPresent) {
            service.deleteAuthor(id)
            return ResponseEntity.ok().build()
        }

        return ResponseEntity.notFound().build()
    }


    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody author: Author): ResponseEntity<Any> {
        val isAuthor = service.getAuthor(id)
        if (isAuthor.isPresent) {
            author.id = isAuthor.get().id
            service.postAuthor(author)
            return ResponseEntity.ok(author)
        }

        return ResponseEntity.notFound().build()
    }

}

@Service
class AuthorService(val repository: AuthorRepository) {

    fun getAll(): List<Author> {
        return repository.getAllAuthors()
    }

    fun postAuthor(author: Author) {
        repository.save(author)
    }

    fun deleteAuthor(id: Long) {
        repository.deleteById(id)
    }

    fun getAuthor(id: Long) = repository.findById(id)

}

@Repository
interface AuthorRepository : CrudRepository<Author, Long> {
    @Query("Select * from AUTHOR")
    fun getAllAuthors(): List<Author>
}

