package api.com.medhead.localize.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SpecialityGroupRequest {
    private String name;
    @NotNull
    @Min(value = 0, message = "No specialty group chosen")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
