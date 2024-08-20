package com.ogn.orange.domain.Service.Impl;
import com.ogn.orange.domain.Dto.ApiResponse;
import com.ogn.orange.domain.Dto.TypeCollecteDto;
import com.ogn.orange.domain.Entity.TypeCollecte;
import com.ogn.orange.domain.Exception.ErreurCodes;
import com.ogn.orange.domain.Exception.InvalidEntityException;
import com.ogn.orange.domain.Repository.TypeCollecteRepository;
import com.ogn.orange.domain.Service.TypeCollecteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TypeCollecteServiceImpl implements TypeCollecteService {
    private final TypeCollecteRepository typeCollecteRepository;
    public TypeCollecteServiceImpl(TypeCollecteRepository typeCollecteRepository) {
        this.typeCollecteRepository = typeCollecteRepository;
    }
    @Override
    public List<TypeCollecte> getAllTypeCollectes() {
        List<TypeCollecte> typeCollectes =  typeCollecteRepository.findTypeCollectesByIsDeletedFalse();
        Collections.reverse(typeCollectes);
        return typeCollectes;
    }

    @Override
    public Optional<TypeCollecte> getTypeCollecteById(Long id)  throws InvalidEntityException {
        Optional<TypeCollecte> typeCollecteOptional = typeCollecteRepository.findById(id);
        if(typeCollecteOptional.isPresent())
        {
           if(!typeCollecteOptional.get().isDeleted())
               return typeCollecteOptional;
        }
        throw  new InvalidEntityException("Cet id n'existe pas dans la BD",String.valueOf(ErreurCodes.TYPE_COLLECTE_NOT_FOUND),String.valueOf(HttpStatus.BAD_REQUEST));
    }

    @Override
    public ResponseEntity<ApiResponse<TypeCollecteDto>> createTypeCollecte(TypeCollecteDto typeCollecteDto) throws InvalidEntityException {
        if (typeCollecteDto == null) {
            throw new InvalidEntityException("L'objet TypeCollecteDto est null",String.valueOf(ErreurCodes.TYPE_COLLECTE_NOT_FOUND),String.valueOf(HttpStatus.BAD_REQUEST));
        }


        if (typeCollecteDto.getNom() == null || typeCollecteDto.getNom().isEmpty()) {
            throw new InvalidEntityException("Veuillez renseigner le champs nom",String.valueOf(ErreurCodes.TYPE_COLLECTE_NOT_FOUND),String.valueOf(HttpStatus.BAD_REQUEST));
        }


        try {

            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    TypeCollecteDto.fromEntity(typeCollecteRepository.save(TypeCollecteDto.toEntity(typeCollecteDto))),
                    "typeCollecte  enregistré avec succès",
                    HttpStatus.OK.value()));
        } catch (Exception e) {
            // Gérer l'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                    false,
                    null,
                    "Erreur lors de l'enregistrement de la pme: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    @Override
    public ResponseEntity<ApiResponse<TypeCollecteDto>> updateTypeCollecte(Long id, TypeCollecteDto typeCollecteDto) {

        Optional <TypeCollecte> optionalTypeCollecte = typeCollecteRepository.findById(id);
        if(optionalTypeCollecte.isPresent())
        {


            TypeCollecte existTypeCollecte = optionalTypeCollecte.get();
                existTypeCollecte.setNom(typeCollecteDto.getNom());
                existTypeCollecte.setDescription(typeCollecteDto.getDescription());
                existTypeCollecte.setNombreRamassage(typeCollecteDto.getNombreRamassage());
                existTypeCollecte.setDureeAbonnement(typeCollecteDto.getDureeAbonnement());

                existTypeCollecte.setMontant(typeCollecteDto.getMontant());

              existTypeCollecte.setDateModification(LocalDateTime.now());
              typeCollecteRepository.save(existTypeCollecte);


            ApiResponse<TypeCollecteDto> response = new ApiResponse<>(
                    true,
                    TypeCollecteDto.fromEntity(existTypeCollecte),
                    "TypeCollecte mis à jour avec succès",
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(response);
        }
        else throw  new InvalidEntityException("Cet id n'existe pas dans la BD",String.valueOf(ErreurCodes.TYPE_COLLECTE_NOT_FOUND),String.valueOf(HttpStatus.BAD_REQUEST));

    }
    @Override
    public void deleteTypeCollecteById(Long id) throws InvalidEntityException {
        Optional <TypeCollecte> optionalTypeCollecte = typeCollecteRepository.findById(id);
        if(optionalTypeCollecte.isPresent())
        {
           optionalTypeCollecte.get().setDeleted(true);
           typeCollecteRepository.save(optionalTypeCollecte.get());
        }
        else
        {
            throw new InvalidEntityException("impossible de supprimer un id qui n'exite pas",String.valueOf(ErreurCodes.CLIENT_NOT_FOUND),String.valueOf(HttpStatus.BAD_REQUEST));
        }
    }
}

