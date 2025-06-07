package global.geoguard.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import global.geoguard.model.Notification;
import global.geoguard.model.NotificationFilter;
import global.geoguard.repository.NotificationRepository;
import global.geoguard.specification.NotificationSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Operation(summary = "Lista todas as notificações")
    @ApiResponse(responseCode = "200", description = "Lista de notificações retornada com sucesso")
    @GetMapping
    public Page<Notification> index(
            NotificationFilter filter,
            @PageableDefault(size = 10, sort = "dataEnvio", direction = Direction.DESC) Pageable pageable) {

        var specification = NotificationSpecification.withFilters(filter);
        return notificationRepository.findAll(specification, pageable);
    }

    @Operation(summary = "Busca uma notificação por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificação encontrada e retornada"),
            @ApiResponse(responseCode = "404", description = "Notificação não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getById(
            @Parameter(description = "ID da notificação a ser buscada", example = "1") @PathVariable Long id) {
        Optional<Notification> optional = notificationRepository.findById(id);
        return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria uma nova notificação")
    @ApiResponse(responseCode = "201", description = "Notificação criada com sucesso")
    @PostMapping
    public ResponseEntity<Notification> create(
            @Parameter(description = "Objeto Notification para ser criado") @RequestBody Notification notification) {
        Notification saved = notificationRepository.save(notification);
        return ResponseEntity.status(201).body(saved);
    }

    @Operation(summary = "Atualiza uma notificação existente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificação atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Notificação não encontrada para atualização")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Notification> update(
            @Parameter(description = "ID da notificação a ser atualizada", example = "1") @PathVariable Long id,
            @Parameter(description = "Objeto Notification com dados atualizados") @RequestBody Notification updatedNotification) {
        Optional<Notification> optional = notificationRepository.findById(id);

        if (optional.isPresent()) {
            Notification existing = optional.get();
            existing.setTitle(updatedNotification.getTitle());
            existing.setMessage(updatedNotification.getMessage());
            existing.setLida(updatedNotification.isLida());

            Notification saved = notificationRepository.save(existing);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deleta uma notificação pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notificação deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Notificação não encontrada para exclusão")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da notificação a ser deletada", example = "1") @PathVariable Long id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
