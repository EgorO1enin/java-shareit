package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Репозиторий для работы с запросами на добавление вещей
 */
@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    /**
     * Найти все запросы пользователя, отсортированные по дате создания (новые сначала)
     */
    List<ItemRequest> findByRequestorIdOrderByCreatedDesc(Long requestorId);

    /**
     * Найти все запросы, созданные другими пользователями, отсортированные по дате создания (новые сначала)
     */
    @Query("SELECT ir FROM ItemRequest ir WHERE ir.requestor.id != :userId ORDER BY ir.created DESC")
    Page<ItemRequest> findAllByRequestorIdNotOrderByCreatedDesc(@Param("userId") Long userId, Pageable pageable);
}