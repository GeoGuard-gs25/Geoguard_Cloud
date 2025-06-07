package global.geoguard.controller;

import global.geoguard.dto.EnergyBillDTO;
import global.geoguard.dto.UserDTO;
import global.geoguard.model.EnergyBill;
import global.geoguard.model.User;
import global.geoguard.repository.EnergyBillRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.Optional;

@RestController
@RequestMapping("/bills")
public class EnergyBillController {

    @Autowired
    private EnergyBillRepository energyBillRepository;

    @Operation(summary = "Retorna todas as faturas de energia do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Faturas retornadas com sucesso")
    })
    @GetMapping
    public Page<EnergyBillDTO> getUserBills(@AuthenticationPrincipal User user, Pageable pageable) {
        return energyBillRepository.findByOwnerId(user.getId(), pageable)
                .map(this::toDTO);
    }

    @Operation(summary = "Retorna uma fatura específica do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fatura retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado à fatura")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnergyBillDTO> getBillById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Optional<EnergyBill> bill = energyBillRepository.findById(id);

        if (bill.isPresent() && bill.get().getOwner().getId().equals(user.getId())) {
            return ResponseEntity.ok(toDTO(bill.get()));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @Operation(summary = "Cria uma nova fatura de energia para o usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fatura criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<EnergyBillDTO> createBill(@RequestBody @Valid EnergyBill energyBill,
            @AuthenticationPrincipal User user) {
        energyBill.setOwner(user);
        EnergyBill saved = energyBillRepository.save(energyBill);
        return ResponseEntity.ok(toDTO(saved));
    }

    @Operation(summary = "Atualiza uma fatura existente do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fatura atualizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado à fatura")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EnergyBillDTO> updateBill(@PathVariable Long id,
            @RequestBody @Valid EnergyBill updatedBill,
            @AuthenticationPrincipal User user) {
        Optional<EnergyBill> existing = energyBillRepository.findById(id);

        if (existing.isPresent() && existing.get().getOwner().getId().equals(user.getId())) {
            EnergyBill bill = existing.get();
            bill.setConsumoKwh(updatedBill.getConsumoKwh());
            bill.setValorKwh(updatedBill.getValorKwh());
            bill.setAmount(updatedBill.getAmount());
            bill.setMonth(updatedBill.getMonth());
            EnergyBill saved = energyBillRepository.save(bill);
            return ResponseEntity.ok(toDTO(saved));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @Operation(summary = "Deleta uma fatura existente do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Fatura deletada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado à fatura")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Optional<EnergyBill> bill = energyBillRepository.findById(id);

        if (bill.isPresent() && bill.get().getOwner().getId().equals(user.getId())) {
            energyBillRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    private EnergyBillDTO toDTO(EnergyBill bill) {
        User owner = bill.getOwner();
        UserDTO ownerDTO = new UserDTO(owner.getId(), owner.getName(), owner.getEmail());

        return new EnergyBillDTO(
                bill.getId(),
                bill.getConsumoKwh(),
                bill.getValorKwh(),
                bill.getAmount(),
                bill.getMonth(),
                ownerDTO);
    }
}
