package dev.bakr.readiction_backend.controller;

import dev.bakr.readiction_backend.requests.QuoteDtoRequest;
import dev.bakr.readiction_backend.responses.QuoteDtoResponse;
import dev.bakr.readiction_backend.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class QuoteController {
    private final QuoteService quoteService;

    QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping("/readers/{readerId}/books/{bookId}/quotes")
    public ResponseEntity<QuoteDtoResponse> addQuote(@PathVariable Long readerId,
            @PathVariable Long bookId,
            @Valid @RequestBody QuoteDtoRequest quoteDtoRequest) {
        QuoteDtoResponse addedQuote = quoteService.addQuote(readerId, bookId, quoteDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedQuote);
    }

    @PutMapping("/readers/{readerId}/books/{bookId}/quotes/{quoteId}")
    public ResponseEntity<QuoteDtoResponse> updateQuote(@PathVariable Long readerId,
            @PathVariable Long bookId,
            @PathVariable Long quoteId,
            @Valid @RequestBody QuoteDtoRequest quoteDtoRequest) {
        QuoteDtoResponse updatedQuote = quoteService.updateQuote(readerId, bookId, quoteId, quoteDtoRequest);
        return ResponseEntity.ok(updatedQuote);
    }

    @DeleteMapping("/readers/{readerId}/books/{bookId}/quotes/{quoteId}")
    public ResponseEntity<String> deleteQuote(@PathVariable Long readerId,
            @PathVariable Long bookId,
            @PathVariable Long quoteId) {
        String deletedQuoteMessage = quoteService.deleteQuote(readerId, bookId, quoteId);
        return ResponseEntity.ok(deletedQuoteMessage);
    }


    @GetMapping("/readers/{readerId}/books/{bookId}/quotes/{quoteId}")
    public ResponseEntity<QuoteDtoResponse> getQuote(@PathVariable Long readerId,
            @PathVariable Long bookId,
            @PathVariable Long quoteId) {
        QuoteDtoResponse returnedQuote = quoteService.getQuote(readerId, bookId, quoteId);
        return ResponseEntity.ok(returnedQuote);
    }

    @GetMapping("/readers/{readerId}/books/{bookId}/quotes")
    public ResponseEntity<List<QuoteDtoResponse>> getQuotes(@PathVariable Long readerId, @PathVariable Long bookId) {
        List<QuoteDtoResponse> returnedQuotes = quoteService.getQuotes(readerId, bookId);
        return ResponseEntity.ok(returnedQuotes);
    }
}
