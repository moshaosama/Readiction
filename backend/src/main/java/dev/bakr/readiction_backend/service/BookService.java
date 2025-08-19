package dev.bakr.readiction_backend.service;

import dev.bakr.readiction_backend.exceptions.AccessDeniedException;
import dev.bakr.readiction_backend.exceptions.ExistsException;
import dev.bakr.readiction_backend.exceptions.InvalidInputsException;
import dev.bakr.readiction_backend.exceptions.NotFoundException;
import dev.bakr.readiction_backend.mappers.BookMapper;
import dev.bakr.readiction_backend.model.Book;
import dev.bakr.readiction_backend.model.Reader;
import dev.bakr.readiction_backend.model.ReaderBook;
import dev.bakr.readiction_backend.model.ReaderBookId;
import dev.bakr.readiction_backend.repository.BookRepository;
import dev.bakr.readiction_backend.repository.ReaderBookRepository;
import dev.bakr.readiction_backend.repository.ReaderRepository;
import dev.bakr.readiction_backend.requests.BookDtoRequest;
import dev.bakr.readiction_backend.requests.ReaderBookDtoRequest;
import dev.bakr.readiction_backend.responses.ReaderBookDtoResponse;
import dev.bakr.readiction_backend.utils.SecurityCheck;
import dev.bakr.readiction_backend.utils.StatusValidator;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookService {
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final ReaderBookRepository readerBookRepository;
    private final BookMapper bookMapper;

    public BookService(AuthorService authorService,
            CategoryService categoryService,
            PublisherService publisherService,
            BookRepository bookRepository,
            ReaderRepository readerRepository, ReaderBookRepository readerBookRepository,
            BookMapper bookMapper) {
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.readerBookRepository = readerBookRepository;
        this.bookMapper = bookMapper;
    }

    public List<ReaderBookDtoResponse> getReaderBooks(Long readerId) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        Reader reader = readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found! With id: " + readerId));

        return reader.getReaderBooks().stream().map(readerBook -> {
            var theBookItself = readerBook.getBook();
            return new ReaderBookDtoResponse(theBookItself.getId(),
                                             theBookItself.getTitle(),
                                             theBookItself.getSubtitle(),
                                             theBookItself.getDescription(),
                                             theBookItself.getIsbn(),
                                             theBookItself.getPagesCount(),
                                             theBookItself.getImageLink(),
                                             theBookItself.getPrintingType(),
                                             theBookItself.getPublishingYear(),
                                             theBookItself.getAuthor().getFullName(),
                                             theBookItself.getCategory().getName(),
                                             theBookItself.getPublisher().getName(),
                                             readerBook.getStatus(),
                                             readerBook.getAddingDate(),
                                             readerBook.getLeftOffPage(),
                                             readerBook.getQuotes(),
                                             readerBook.getWords()
            );
        }).toList();
    }

    public ReaderBookDtoResponse getReaderBook(Long readerId, Long bookId) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        Reader reader = readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found! With id: " + readerId));

        ReaderBookId readerBookToGetId = ReaderBook.createCompositeKey(reader.getId(), bookId);
        var readerBookToGet = readerBookRepository.findById(readerBookToGetId).orElseThrow(() -> new NotFoundException(
                "You don't have this book in your collection!"
        ));

        var theBookItself = readerBookToGet.getBook();

        return new ReaderBookDtoResponse(theBookItself.getId(),
                                         theBookItself.getTitle(),
                                         theBookItself.getSubtitle(),
                                         theBookItself.getDescription(),
                                         theBookItself.getIsbn(),
                                         theBookItself.getPagesCount(),
                                         theBookItself.getImageLink(),
                                         theBookItself.getPrintingType(),
                                         theBookItself.getPublishingYear(),
                                         theBookItself.getAuthor().getFullName(),
                                         theBookItself.getCategory().getName(),
                                         theBookItself.getPublisher().getName(),
                                         readerBookToGet.getStatus(),
                                         readerBookToGet.getAddingDate(),
                                         readerBookToGet.getLeftOffPage(),
                                         readerBookToGet.getQuotes(),
                                         readerBookToGet.getWords()
        );
    }

    public String addReaderBook(Long readerId, BookDtoRequest bookDtoRequest) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        // Still check to avoid NullPointerException if reader was deleted after token issued (Always prefer robustness over optimism)
        Reader reader = readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found! With id: " + readerId));

        Book existingBookInDatabase = bookRepository.findByIsbn(bookDtoRequest.isbn());

        ReaderBook readerBookToAdd;

        if (existingBookInDatabase == null) {
            Book newBookEntity = bookMapper.toEntity(bookDtoRequest);
            newBookEntity.setAuthor(authorService.findOrCreateAuthor(bookDtoRequest.authorFullName()));
            newBookEntity.setCategory(categoryService.findOrCreateCategory(bookDtoRequest.categoryName()));
            newBookEntity.setPublisher(publisherService.findOrCreatePublisher(bookDtoRequest.publisherName()));
            Book savedBook = bookRepository.save(newBookEntity);

            readerBookToAdd = new ReaderBook(reader, savedBook);
            readerBookRepository.save(readerBookToAdd);

            reader.getReaderBooks().add(readerBookToAdd);
            readerRepository.save(reader);

            return "We've successfully created the book and added it to your books.";
        } else {
            var readerBookToAddId = ReaderBook.createCompositeKey(reader.getId(), existingBookInDatabase.getId());
            boolean isBookExistsInReaderCollection = readerBookRepository.existsById(readerBookToAddId);
            if (isBookExistsInReaderCollection) {
                throw new ExistsException("You already have this book in your collection!");
            }

            readerBookToAdd = new ReaderBook(reader, existingBookInDatabase);
            readerBookRepository.save(readerBookToAdd);

            reader.getReaderBooks().add(readerBookToAdd);
            readerRepository.save(reader);

            return "This book already exists in the database. We've added it to your books.";
        }
    }

    public ReaderBookDtoResponse updateReaderBook(Long readerId,
            Long bookId,
            ReaderBookDtoRequest readerBookDtoRequest) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        // Still check to avoid NullPointerException if reader was deleted after token issued (Always prefer robustness over optimism)
        readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "This reader has been deleted from the database! With id: " + readerId));

        var readerBookToUpdateId = ReaderBook.createCompositeKey(readerId, bookId);

        var readerBookToUpdate = readerBookRepository.findById(readerBookToUpdateId).orElseThrow(() -> new NotFoundException(
                "Book not found in your collection."
        ));

        boolean isStatusValid = StatusValidator.validateStatus(readerBookDtoRequest.status());

        if (!isStatusValid) {
            throw new InvalidInputsException("Enter a valid status (UNREAD, READING, READ)! Can be lowercase.");
        }

        readerBookToUpdate.setStatus(readerBookDtoRequest.status());
        readerBookToUpdate.setLeftOffPage(readerBookDtoRequest.leftOffPage());

        var updatedReaderBook = readerBookRepository.save(readerBookToUpdate);

        var theBookItself = updatedReaderBook.getBook();

        return new ReaderBookDtoResponse(theBookItself.getId(),
                                         theBookItself.getTitle(),
                                         theBookItself.getSubtitle(),
                                         theBookItself.getDescription(),
                                         theBookItself.getIsbn(),
                                         theBookItself.getPagesCount(),
                                         theBookItself.getImageLink(),
                                         theBookItself.getPrintingType(),
                                         theBookItself.getPublishingYear(),
                                         theBookItself.getAuthor().getFullName(),
                                         theBookItself.getCategory().getName(),
                                         theBookItself.getPublisher().getName(),
                                         updatedReaderBook.getStatus(),
                                         updatedReaderBook.getAddingDate(),
                                         updatedReaderBook.getLeftOffPage(),
                                         updatedReaderBook.getQuotes(),
                                         updatedReaderBook.getWords()
        );
    }

    public String deleteReaderBook(Long readerId, Long bookId) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        // in case it is authenticated but was deleted accidentally from the database
        Reader reader = readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found! With id: " + readerId));

        ReaderBookId readerBookToDeleteId = ReaderBook.createCompositeKey(readerId, bookId);

        var readerBookToDelete = readerBookRepository.findById(readerBookToDeleteId).orElseThrow(() -> new NotFoundException(
                "Book not found in your collection."));
        readerBookRepository.delete(readerBookToDelete);


        Book bookToDeleteIfNoReaders = readerBookToDelete.getBook();

        reader.getReaderBooks().removeIf(rb -> rb.getBook().equals(bookToDeleteIfNoReaders));
        readerRepository.save(reader);

        // Optional: if no other readers have the book, delete it
        if (readerBookRepository.countReadersByBookId(bookToDeleteIfNoReaders.getId()) == 0) {
            bookRepository.delete(bookToDeleteIfNoReaders);
        }

        return "Book deleted successfully.";
    }
}
