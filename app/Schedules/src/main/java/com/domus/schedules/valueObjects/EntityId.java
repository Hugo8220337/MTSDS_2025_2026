package com.domus.schedules.valueObjects;

public class EntityId {
    private final Long id;

    public EntityId(Long id) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("Invalid ID value: " + id);
        }
        this.id = id;
    }

    private boolean isValidId(Long id) {
        return id != null && id > 0;
    }

    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntityId other = (EntityId) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}
