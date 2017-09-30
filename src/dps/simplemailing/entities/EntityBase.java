package dps.simplemailing.entities;

public interface EntityBase<IdType> {
    IdType getId();
    void setId(IdType id);
}
