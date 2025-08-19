package dev.bakr.readiction_backend.service;

import dev.bakr.readiction_backend.exceptions.AccessDeniedException;
import dev.bakr.readiction_backend.exceptions.ExistsException;
import dev.bakr.readiction_backend.exceptions.NotFoundException;
import dev.bakr.readiction_backend.mappers.WordMapper;
import dev.bakr.readiction_backend.model.ReaderBook;
import dev.bakr.readiction_backend.repository.ReaderBookRepository;
import dev.bakr.readiction_backend.repository.ReaderRepository;
import dev.bakr.readiction_backend.repository.WordRepository;
import dev.bakr.readiction_backend.requests.WordDtoRequest;
import dev.bakr.readiction_backend.responses.WordDtoResponse;
import dev.bakr.readiction_backend.utils.SecurityCheck;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordService {
    private final WordRepository wordRepository;
    private final ReaderRepository readerRepository;
    private final ReaderBookRepository readerBookRepository;
    private final WordMapper wordMapper;

    public WordService(WordRepository wordRepository,
            ReaderRepository readerRepository,
            ReaderBookRepository readerBookRepository, WordMapper wordMapper) {
        this.wordRepository = wordRepository;
        this.readerRepository = readerRepository;
        this.readerBookRepository = readerBookRepository;
        this.wordMapper = wordMapper;
    }

    public WordDtoResponse addWord(Long readerId, Long bookId, WordDtoRequest wordDtoRequest) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found in the database!"));

        var readerBookId = ReaderBook.createCompositeKey(readerId, bookId);

        var readerBook = readerBookRepository.findById(readerBookId).orElseThrow(() -> new NotFoundException(
                "Book not found in your collection to add the word!"
        ));

        boolean isWordExistsInReaderBook = wordRepository.existsByWordContentAndReaderBookId(wordDtoRequest.wordContent(),
                                                                                             readerBookId
        );
        if (isWordExistsInReaderBook) {
            throw new ExistsException("You already have this word in this book copy!");
        }

        var newWord = wordMapper.toEntity(wordDtoRequest);
        newWord.setReaderBook(readerBook);
        var savedWord = wordRepository.save(newWord);

        readerBook.getWords().add(savedWord);
        readerBookRepository.save(readerBook);

        return wordMapper.toDto(newWord);
    }

    public WordDtoResponse updateWord(Long readerId, Long bookId, Long wordId, WordDtoRequest wordDtoRequest) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found in the database!"));

        var readerBookId = ReaderBook.createCompositeKey(readerId, bookId);

        readerBookRepository.findById(readerBookId).orElseThrow(() -> new NotFoundException(
                "Book not found in your collection to update its word!"
        ));

        var theWordToUpdate = wordRepository.findByIdAndReaderBookId(wordId, readerBookId);

        if (theWordToUpdate == null) {
            throw new ExistsException("This word isn't found in this book copy to update it!");
        }

        theWordToUpdate.setWordContent(wordDtoRequest.wordContent());
        theWordToUpdate.setTranslation(wordDtoRequest.translation());
        theWordToUpdate.setRelatedSentence(wordDtoRequest.relatedSentence());
        theWordToUpdate.setPageNumber(wordDtoRequest.pageNumber());
        wordRepository.save(theWordToUpdate);

        return wordMapper.toDto(theWordToUpdate);
    }

    public String deleteWord(Long readerId, Long bookId, Long wordId) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found in the database!"));

        var readerBookId = ReaderBook.createCompositeKey(readerId, bookId);

        var readerBook = readerBookRepository.findById(readerBookId).orElseThrow(() -> new NotFoundException(
                "Book not found in your collection to delete its word!"
        ));

        var theWordToDelete = wordRepository.findByIdAndReaderBookId(wordId, readerBookId);
        if (theWordToDelete == null) {
            throw new ExistsException("This word isn't found in this book copy to delete it!");
        }
        wordRepository.delete(theWordToDelete);

        readerBook.getWords().removeIf((word) -> word.equals(theWordToDelete));
        readerBookRepository.save(readerBook);

        return "You've successfully deleted the word.";
    }

    public WordDtoResponse getWord(Long readerId, Long bookId, Long wordId) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found in the database!"));

        var readerBookId = ReaderBook.createCompositeKey(readerId, bookId);

        readerBookRepository.findById(readerBookId).orElseThrow(() -> new NotFoundException(
                "This book copy isn't found in your collection to get its word!"
        ));

        var theWordToGet = wordRepository.findByIdAndReaderBookId(wordId, readerBookId);

        if (theWordToGet == null) {
            throw new ExistsException("This word isn't found in this book copy to get from");
        }

        return wordMapper.toDto(theWordToGet);
    }

    public List<WordDtoResponse> getWords(Long readerId, Long bookId) {
        Long authenticatedReaderId = SecurityCheck.getAuthenticatedUserId();

        if (!authenticatedReaderId.equals(readerId)) {
            throw new AccessDeniedException("You cannot access this resource.");
        }

        readerRepository.findById(readerId).orElseThrow(() -> new NotFoundException(
                "Reader not found in the database!"));

        var readerBookId = ReaderBook.createCompositeKey(readerId, bookId);

        var readerBook = readerBookRepository.findById(readerBookId).orElseThrow(() -> new NotFoundException(
                "This book copy isn't found in your collection to get the words from!"
        ));

        return readerBook.getWords().stream().map(wordMapper::toDto).toList();
    }
}
