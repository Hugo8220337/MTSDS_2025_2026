package com.domus.applications.valueObjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Embeddable
@Getter
public class ApplicantInfo {
    private String nif;
    private String fullName;

    protected ApplicantInfo() {} // JPA

    public ApplicantInfo(String nif, String fullName) {
        if (nif == null || nif.isBlank()) {
            throw new IllegalArgumentException("Tax ID cannot be blank");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be blank");
        }
        this.nif = nif;
        this.fullName = fullName;
    }

    public String getNif() {
        return nif;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicantInfo)) return false;
        ApplicantInfo that = (ApplicantInfo) o;
        return Objects.equals(nif, that.nif);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nif);
    }
}