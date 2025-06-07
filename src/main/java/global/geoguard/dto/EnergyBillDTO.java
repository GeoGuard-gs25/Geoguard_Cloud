package global.geoguard.dto;

public record EnergyBillDTO(
        Long id,
        Double consumoKwh,
        Double valorKwh,
        Double amount,
        String month,
        UserDTO owner) {
}