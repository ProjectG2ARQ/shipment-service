package ar.edu.unju.fi.arquitecturas.shipmentservice.mapper;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.DireccionDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Direccion;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Envio;

public final class EnvioMapper {

    private EnvioMapper() {}

    public static Envio toEntity(EnvioRequestDto dto) {
        if (dto == null) return null;

        Envio envio = new Envio();
        envio.setCodigoSeguimiento(dto.getCodigoSeguimiento());
        envio.setPeso(dto.getPeso());
        envio.setAncho(dto.getAncho());
        envio.setAlto(dto.getAlto());
        envio.setLargo(dto.getLargo());

        if (dto.getOrigen() != null) {
            envio.setOrigen(new Direccion(
                    dto.getOrigen().getCalle(),
                    dto.getOrigen().getNumero(),
                    dto.getOrigen().getCiudad(),
                    dto.getOrigen().getCodigoPostal()
            ));
        }

        if (dto.getDestino() != null) {
            envio.setDestino(new Direccion(
                    dto.getDestino().getCalle(),
                    dto.getDestino().getNumero(),
                    dto.getDestino().getCiudad(),
                    dto.getDestino().getCodigoPostal()
            ));
        }

        return envio;
    }

    public static EnvioResponseDto toResponseDto(Envio entity) {
        if (entity == null) return null;

        DireccionDto origenDto = entity.getOrigen() != null ? DireccionDto.builder()
                .calle(entity.getOrigen().getCalle())
                .numero(entity.getOrigen().getNumero())
                .ciudad(entity.getOrigen().getCiudad())
                .codigoPostal(entity.getOrigen().getCodigoPostal())
                .build() : null;

        DireccionDto destinoDto = entity.getDestino() != null ? DireccionDto.builder()
                .calle(entity.getDestino().getCalle())
                .numero(entity.getDestino().getNumero())
                .ciudad(entity.getDestino().getCiudad())
                .codigoPostal(entity.getDestino().getCodigoPostal())
                .build() : null;

        return EnvioResponseDto.builder()
                .id(entity.getId())
                .codigoSeguimiento(entity.getCodigoSeguimiento())
                .peso(entity.getPeso())
                .ancho(entity.getAncho())
                .alto(entity.getAlto())
                .largo(entity.getLargo())
                .tarifa(entity.getTarifa())
                .estado(entity.getEstado())
                .origen(origenDto)
                .destino(destinoDto)
                .fechaCreacion(entity.getFechaCreacion())
                .fechaUltimaModificacion(entity.getFechaUltimaModificacion())
                .build();
    }
}