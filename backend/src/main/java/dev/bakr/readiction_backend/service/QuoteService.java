package dev.bakr.readiction_backend.service;

import dev.bakr.readiction_backend.exceptions.AccessDeniedException;
import dev.bakr.readiction_backend.exceptions.ExistsException;
import dev.bakr.readiction_backend.exceptions.NotFoundException;
import dev.bakr.readiction_backend.mappers.QuoteMapper;
import dev.bakr.readiction_backend.model.ReaderBook;
import dev.bakr.readiction_backend.repository.QuoteRepository;
import dev.bakr.readiction_backend.repository.ReaderBookRepository;
import dev.bakr.readiction_backend.repository.ReaderRepository;
import dev.bakr.readiction_backend.requests.QuoteDtoRequest;
import dev.bakr.readiction_backend.responses.QuoteDtoResponse;
import dev.bakr.readiction_backend.utils.SecurityCheck;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;
    private final ReaderRepository readerRepository;
    private final ReaderBookRepository readerBookRepository;
    private final QuoteMapper quoteMapper;

    public QuoteService(QuoteRepository quoteRepository,
            ReaderRepository readerRepository,
            ReaderBookRepository readerBookRepository, QuoteMapper quoteMapper) {
        this.quoteRepository = quoteRepository;
        this.readerRepository = readerRepository;
        this.readerBookRepository = readerBookRepository;
        this.quoteMapper = quoteMapper;
    }

    public QuoteDtoResponse addQuote(Long readerId, Long bookId, QuoteDtoRequest quoteDtoRequest) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found in the database!"));

        var readerBookId = ReaderBook.createCompositeKey(readerId, bookId);

        var readerBook = readerBookRepository.findById(readerBookId).orElseThrow(() -> new NotFoundException(
                "Book not found in your collection to add the quote!"
        ));

        boolean isQuoteExistsInReaderBook = quoteRepository.existsByTextAndReaderBookId(quoteDtoRequest.text(),
                                                                                        readerBookId
        );
        if (isQuoteExistsInReaderBook) {
            throw new ExistsException("You already have this quote in this book copy!");
        }

        var newQuote = quoteMapper.toEntity(quoteDtoRequest);
        newQuote.setReaderBook(readerBook);
        var savedQuote = quoteRepository.save(newQuote);

        readerBook.getQuotes().add(savedQuote);
        readerBookRepository.save(readerBook);

        return quoteMapper.toDto(newQuote);
    }

    public QuoteDtoResponse updateQuote(Long readerId, Long bookId, Long quoteId, QuoteDtoRequest quoteDtoRequest) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found in the database!"));

        var readerBookId = ReaderBook.createCompositeKey(readerId, bookId);

        readerBookRepository.findById(readerBookId).orElseThrow(() -> new NotFoundException(
                "Book not found in your collection to update its quote!"
        ));

        var theQuoteToUpdate = quoteRepository.findByIdAndReaderBookId(quoteId, readerBookId);

        if (theQuoteToUpdate == null) {
            throw new ExistsException("This quote isn't found in this book copy to update it!");
        }

        theQuoteToUpdate.setText(quoteDtoRequest.text());
        theQuoteToUpdate.setPageNumber(quoteDtoRequest.pageNumber());
        quoteRepository.save(theQuoteToUpdate);

        return quoteMapper.toDto(theQuoteToUpdate);
    }

    public String deleteQuote(Long readerId, Long bookId, Long quoteId) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found in the database!"));

        var readerBookId = ReaderBook.createCompositeKey(readerId, bookId);

        var readerBook = readerBookRepository.findById(readerBookId).orElseThrow(() -> new NotFoundException(
                "Book not found in your collection to delete its quote!"
        ));

        var theQuoteToDelete = quoteRepository.findByIdAndReaderBookId(quoteId, readerBookId);
        if (theQuoteToDelete == null) {
            throw new ExistsException("This quote isn't found in this book copy to delete it!");
        }
        quoteRepository.delete(theQuoteToDelete);

        readerBook.getQuotes().removeIf((quote) -> quote.equals(theQuoteToDelete));
        readerBookRepository.save(readerBook);

        return "You've successfully deleted the quote.";
    }

    public QuoteDtoResponse getQuote(Long readerId, Long bookId, Long quoteId) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found in the database!"));

        var readerBookId = ReaderBook.createCompositeKey(readerId, bookId);

        readerBookRepository.findById(readerBookId).orElseThrow(() -> new NotFoundException(
                "This book copy isn't found in your collection to get its quote!"
        ));

        var theQuoteToGet = quoteRepository.findByIdAndReaderBookId(quoteId, readerBookId);

        if (theQuoteToGet == null) {
            throw new ExistsException("This quote isn't found in this book copy to get from");
        }

        return quoteMapper.toDto(theQuoteToGet);
    }

    public List<QuoteDtoResponse> getQuotes(Long readerId, Long bookId) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found in the database!"));

        var readerBookId = ReaderBook.createCompositeKey(readerId, bookId);

        var readerBook = readerBookRepository.findById(readerBookId).orElseThrow(() -> new NotFoundException(
                "This book copy isn't found in your collection to get the quotes from!"
        ));

        return readerBook.getQuotes().stream().map(quoteMapper::toDto).toList();
    }
}
