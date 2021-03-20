package ec.edu.espe.banquito.crm.posibles.clientes.enums;

public enum GenreEnum {
    MASCULINO("MAS", "MASCULINO"),
    FEMENINO("FEM", "FEMENINO");

    private final String code;
    private final String description;

    private GenreEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
