package dev.bakr.readiction_backend.controller;

import dev.bakr.readiction_backend.requests.BookDtoRequest;
import dev.bakr.readiction_backend.requests.ReaderBookDtoRequest;
import dev.bakr.readiction_backend.responses.ReaderBookDtoResponse;
import dev.bakr.readiction_backend.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
// For OpenAPI (instead of book-controller on Swagger UI)
@Tag(name = "Book")
// For OpenAPI to update the Swagger UI to show all the returned HTTP status codes
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "403", description = "Don't have access to the resources"),
        @ApiResponse(responseCode = "401", description = "Unauthorized due to invalid inputs or credentials")
})
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // For OpenAPI to show more info about a certain request.
    @Operation(summary = "Gets all books for a specified reader", description = "Returns the collection of book that the reader has")
    @GetMapping(path = "/readers/{readerId}/books")
    public ResponseEntity<List<ReaderBookDtoResponse>> getReaderBooks(@PathVariable Long readerId) {
        List<ReaderBookDtoResponse> allBooks = bookService.getReaderBooks(readerId);
        return ResponseEntity.ok(allBooks);
    }

    @Operation(summary = "Gets a certain book by its ID for a specified reader", description = "Returns the book that that has the entered ID")
    @GetMapping(path = "/readers/{readerId}/books/{bookId}")
    public ResponseEntity<ReaderBookDtoResponse> getReaderBook(@PathVariable Long readerId, @PathVariable Long bookId) {
        ReaderBookDtoResponse readingCopy = bookService.getReaderBook(readerId,
                                                                      bookId
        );  // this might throw BookNotFoundException
        return ResponseEntity.status(HttpStatus.OK).body(readingCopy);
    }

    @Operation(summary = "Adds a book to the collection for that reader with the entered ID", description = "Returns the new book that was added to the collection with all its info")
    @PostMapping(path = "/readers/{readerId}/books")
    public ResponseEntity<String> addReaderBook(@PathVariable Long readerId,
            @Valid @RequestBody BookDtoRequest bookDtoRequest) {
        String newBookMessage = bookService.addReaderBook(readerId, bookDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBookMessage);
    }

    @Operation(summary = "Updates a certain book by its ID for a specified reader", description = "Returns the book that was updated with all its info")
    @PutMapping(path = "/readers/{readerId}/books/{bookId}")
    public ResponseEntity<ReaderBookDtoResponse> updateReaderBook(@PathVariable Long readerId,
            @PathVariable Long bookId,
            @Valid @RequestBody ReaderBookDtoRequest readerBookDtoRequest) {
        ReaderBookDtoResponse updatedReadingCopy = bookService.updateReaderBook(readerId, bookId, readerBookDtoRequest);
        return ResponseEntity.ok(updatedReadingCopy);
    }

    @Operation(summary = "Deletes a certain book by its ID for a specified reader", description = "Returns a message saying that this book has been successfully deleted")
    @DeleteMapping(path = "/readers/{readerId}/books/{bookId}")
    public ResponseEntity<String> deleteReaderBook(@PathVariable Long readerId, @PathVariable Long bookId) {
        String deletedBookMessage = bookService.deleteReaderBook(readerId, bookId);
        return ResponseEntity.ok(deletedBookMessage);
    }
}
