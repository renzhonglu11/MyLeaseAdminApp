package com.rz.lease.web.admin.repository;

import com.rz.lease.model.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class BaseJpaRepositoryImpl<T extends BaseEntity> extends SimpleJpaRepository<T, Long> implements BaseJpaRepository<T> {

    private static final byte NOT_DELETED = 0;
    private static final byte DELETED = 1;

    private final EntityManager entityManager;

    public BaseJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    private Specification<T> notDeletedSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.equal(root.get("isDeleted"), NOT_DELETED),
                criteriaBuilder.isNull(root.get("isDeleted"))
        );
    }

    private Specification<T> withNotDeleted(@Nullable Specification<T> specification) {
        return specification == null ? notDeletedSpecification() : specification.and(notDeletedSpecification());
    }

    private Specification<T> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }

    @Override
    public Optional<T> findById(Long id) {
        return super.findOne(withNotDeleted(hasId(id)));
    }

    @Override
    public List<T> findAll() {
        return super.findAll(notDeletedSpecification());
    }

    @Override
    public List<T> findAll(Sort sort) {
        return super.findAll(notDeletedSpecification(), sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return super.findAll(notDeletedSpecification(), pageable);
    }

    @Override
    public Optional<T> findOne(Specification<T> specification) {
        return super.findOne(withNotDeleted(specification));
    }

    @Override
    public List<T> findAll(@Nullable Specification<T> specification) {
        return super.findAll(withNotDeleted(specification));
    }

    @Override
    public Page<T> findAll(@Nullable Specification<T> specification, Pageable pageable) {
        return super.findAll(withNotDeleted(specification), pageable);
    }

    @Override
    public List<T> findAll(@Nullable Specification<T> specification, Sort sort) {
        return super.findAll(withNotDeleted(specification), sort);
    }

    @Override
    public long count() {
        return super.count(notDeletedSpecification());
    }

    @Override
    public long count(@Nullable Specification<T> specification) {
        return super.count(withNotDeleted(specification));
    }

    @Override
    public boolean existsById(Long id) {
        return super.count(withNotDeleted(hasId(id))) > 0;
    }

    @Override
    public boolean exists(Specification<T> specification) {
        return super.exists(withNotDeleted(specification));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        super.findById(id).ifPresent(this::markDeleted);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        markDeleted(entity);
    }

    @Override
    @Transactional
    public void deleteAllById(Iterable<? extends Long> ids) {
        ids.forEach(this::deleteById);
    }

    @Override
    @Transactional
    public void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(this::markDeleted);
    }

    @Override
    @Transactional
    public void deleteAll() {
        super.findAll(notDeletedSpecification()).forEach(this::markDeleted);
    }

    @Override
    @Transactional
    public void deleteAllInBatch() {
        entityManager.createQuery(
                        "update " + getDomainClass().getName() + " entity " +
                                "set entity.isDeleted = :deleted " +
                                "where entity.isDeleted = :notDeleted or entity.isDeleted is null"
                )
                .setParameter("deleted", DELETED)
                .setParameter("notDeleted", NOT_DELETED)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deleteAllInBatch(Iterable<T> entities) {
        entities.forEach(this::markDeleted);
    }

    @Override
    @Transactional
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        ids.forEach(this::deleteById);
    }

    @Transactional
    protected void markDeleted(T entity) {
        if (entity.getIsDeleted() != null && entity.getIsDeleted() == DELETED) {
            return;
        }
        entity.setIsDeleted(DELETED);
        entityManager.merge(entity);
    }
}
