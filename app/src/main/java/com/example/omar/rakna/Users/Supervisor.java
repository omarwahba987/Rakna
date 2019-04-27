package com.example.omar.rakna.Users;

public class Supervisor {
    String id,name,email,garageName,garageId,type;

    public Supervisor() {
    }

    public Supervisor(String id, String name, String email, String garageName, String garageId, String type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.garageName = garageName;
        this.garageId = garageId;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public String getGarageId() {
        return garageId;
    }

    public void setGarageId(String garageId) {
        this.garageId = garageId;
    }
}
