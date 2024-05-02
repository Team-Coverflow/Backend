package com.coverflow.notification.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class EmitterRepository {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public SseEmitter save(
            final String eventId,
            final SseEmitter sseEmitter
    ) {
        emitterMap.remove(eventId);
        emitterMap.put(eventId, sseEmitter);

        log.info("[SseEmitter] Set {}", eventId);
        return sseEmitter;
    }

    public void saveEventCache(String id, Object event) {
        eventCache.put(id, event);
    }

    public Optional<SseEmitter> get(final String memberId) {
        SseEmitter sseEmitter = emitterMap.get(memberId);

        log.info("[SseEmitter] Get {}", memberId);
        return Optional.ofNullable(sseEmitter);
    }

    public Map<String, SseEmitter> findAllEventStartWithId(final String memberId) {
        return emitterMap.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> findAllEventCacheStartWithId(final String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void delete(final String eventId) {
        emitterMap.remove(eventId);
    }

    public void deleteAllStartWithId(final String memberId) {
        emitterMap.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        emitterMap.remove(key);
                    }
                }
        );
    }

    public void deleteAllEventCacheStartWithId(final String memberId) {
        eventCache.forEach(
                (key, data) -> {
                    if (key.startsWith(memberId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }
}
