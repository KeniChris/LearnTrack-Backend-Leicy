package com.upc.learntrack.course.controller;

import com.upc.learntrack.course.dto.TopicDto;
import com.upc.learntrack.course.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/collections/{collectionId}/topics")
    @PreAuthorize("hasAuthority('DOCENTE')")
    public ResponseEntity<List<TopicDto>> findAllByCollection(
            @PathVariable Long collectionId,
            Principal principal
    ) {
        return ResponseEntity.ok(
                topicService.findAllByCollectionId(collectionId, principal.getName())
        );
    }

    @GetMapping("/topics/{id}")
    @PreAuthorize("hasAnyAuthority('ESTUDIANTE', 'DOCENTE')")
    public ResponseEntity<TopicDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.findById(id));
    }

    @PostMapping("/collections/{collectionId}/topics")
    @PreAuthorize("hasAuthority('DOCENTE')")
    public ResponseEntity<TopicDto> save(
            @PathVariable Long collectionId,
            @Valid @RequestBody TopicDto dto,
            Principal principal
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(topicService.save(collectionId, dto, principal.getName()));
    }

    @GetMapping("/topics")
    @PreAuthorize("hasAnyAuthority('ESTUDIANTE', 'DOCENTE')")
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        return ResponseEntity.ok(topicService.findAll());
    }

    @GetMapping("/topics/priorities")
    @PreAuthorize("hasAuthority('ESTUDIANTE')")
    public ResponseEntity<List<TopicDto>> getPrioritizedTopics(Principal principal) {
        return ResponseEntity.ok(topicService.findPrioritizedTopicsForStudent(principal.getName()));
    }

    @PutMapping("/topics/{id}")
    @PreAuthorize("hasAuthority('DOCENTE')")
    public ResponseEntity<TopicDto> update(
            @PathVariable Long id,
            @Valid @RequestBody TopicDto dto,
            Principal principal
    ) {
        return ResponseEntity.ok(topicService.update(id, dto, principal.getName()));
    }

    @DeleteMapping("/topics/{id}")
    @PreAuthorize("hasAuthority('DOCENTE')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Principal principal
    ) {
        topicService.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/collections/{collectionName}/groups/{groupId}/topics")
    @PreAuthorize("hasAnyAuthority('DOCENTE', 'ESTUDIANTE')")
    public ResponseEntity<List<TopicDto>> findTopicsForGroup(
            @PathVariable String collectionName,
            @PathVariable Long groupId,
            Principal principal
    ) {
        return ResponseEntity.ok(
                topicService.findVisibleTopicsForGroup(collectionName, groupId, principal.getName())
        );
    }

    @PutMapping("/topics/{id}/groups")
    @PreAuthorize("hasAuthority('DOCENTE')")
    public ResponseEntity<Void> assignToGroups(
            @PathVariable Long id,
            @RequestBody List<Long> groupIds,
            Principal principal
    ) {
        topicService.assignTopicToGroups(id, groupIds, principal.getName());
        return ResponseEntity.noContent().build();
    }
}