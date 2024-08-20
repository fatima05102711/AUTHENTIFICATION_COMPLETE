package com.ogn.orange.domain.Dto;
import com.ogn.orange.domain.Entity.Abonnement;
import com.ogn.orange.domain.Entity.Client;
import com.ogn.orange.domain.Entity.TypeCollecte;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbonnementDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate dateCreationAbonnement;
   // private int dureeAbonnement ;
    private LocalDate dateExpiration;
    private TypeCollecte typeCollecte;
    private Client client;

     public static AbonnementDto fromEntity(Abonnement abonnement) {
         return AbonnementDto.builder()
                 .id(abonnement.getId())
                 .dateCreationAbonnement(abonnement.getDateCreationAbonnement())
               //  .dureeAbonnement(abonnement.getDureeAbonnement())
                 .typeCollecte(abonnement.getTypeCollecte())
                 .client(abonnement.getClient())
                 .dateExpiration(abonnement.getDateExpiration())
                 .build();
     }

    public static Abonnement toEntity(AbonnementDto abonnementDto) {
        Abonnement abonnement = new Abonnement();
         // abonnement.setDureeAbonnement(abonnementDto.getDureeAbonnement());
          abonnement.setTypeCollecte(abonnementDto.getTypeCollecte());
          abonnement.setClient(abonnementDto.getClient());
          abonnement.setDateExpiration(abonnementDto.getDateExpiration());
         return  abonnement;
    }
}
