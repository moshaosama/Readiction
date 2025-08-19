package dev.bakr.readiction_backend.service;

import dev.bakr.readiction_backend.model.Publisher;
import dev.bakr.readiction_backend.repository.PublisherRepository;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher findOrCreatePublisher(String publisherName) {
        return publisherRepository.findByName(publisherName)
                .orElseGet(() -> {
                    Publisher publisher = new Publisher();
                    publisher.setName(publisherName);
                    return publisherRepository.save(publisher);
                });
    }
}
