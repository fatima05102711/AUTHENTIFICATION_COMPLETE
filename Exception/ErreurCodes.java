package com.ogn.orange.domain.Exception;
public enum ErreurCodes {
    CLIENT_NOT_FOUND(200),
    COLLECTOR_NOT_FOUND(100),
    TYPE_COLLECTE_NOT_FOUND(50),
    PME_NOT_FOUND(60),
    USER_NOT_FOUND(14),
    ZONE_NOT_FOUND(55),
    DEPOT_NOT_FOUND(22),
    COMMUNE_NOT_FOUND(54),
    QUARTIER_NOT_FOUND(58),
    PERMISSION_NOT_FOUND(55),
    ROLES_NOT_FOUND(65),
    ABONNEMENT_NOT_FOUND(45)
    ;

    private int code;
    ErreurCodes(int code )
    {
        this.code =code;
    }
    public int getCode()
    {
        return code;
    }
}
