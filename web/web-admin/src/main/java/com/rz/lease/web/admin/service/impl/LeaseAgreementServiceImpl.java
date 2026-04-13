package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.model.entity.LeaseAgreement;
import com.rz.lease.model.entity.LeaseTerm;
import com.rz.lease.model.entity.PaymentType;
import com.rz.lease.model.entity.RoomInfo;
import com.rz.lease.model.enums.LeaseStatus;
import com.rz.lease.web.admin.repository.ApartmentInfoRepository;
import com.rz.lease.web.admin.repository.LeaseAgreementRepository;
import com.rz.lease.web.admin.repository.LeaseTermRepository;
import com.rz.lease.web.admin.repository.PaymentTypeRepository;
import com.rz.lease.web.admin.repository.RoomInfoRepository;
import com.rz.lease.web.admin.service.LeaseAgreementService;
import com.rz.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.rz.lease.web.admin.vo.agreement.AgreementVo;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LeaseAgreementServiceImpl implements LeaseAgreementService {
    @Autowired
    private LeaseAgreementRepository leaseAgreementRepository;

    @Autowired
    private ApartmentInfoRepository apartmentInfoRepository;

    @Autowired
    private RoomInfoRepository roomInfoRepository;

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    @Autowired
    private LeaseTermRepository leaseTermRepository;

    @Override
    public void saveOrUpdate(LeaseAgreement leaseAgreement) {
        leaseAgreementRepository.save(leaseAgreement);
    }

    @Override
    public Page<AgreementVo> page(long current, long size, AgreementQueryVo queryVo) {
        PageRequest pageRequest = PageRequest.of(
                Math.max((int) current - 1, 0),
                Math.max((int) size, 1),
                Sort.by(Sort.Direction.ASC, "id"));

        Page<LeaseAgreement> agreementPage = leaseAgreementRepository.findAll(buildSpecification(queryVo), pageRequest);
        if (agreementPage.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageRequest, agreementPage.getTotalElements());
        }

        List<LeaseAgreement> agreements = agreementPage.getContent();

        List<Long> apartmentIds = agreements.stream().map(LeaseAgreement::getApartmentId).filter(Objects::nonNull)
                .distinct().toList();
        List<Long> roomIds = agreements.stream().map(LeaseAgreement::getRoomId).filter(Objects::nonNull).distinct()
                .toList();
        List<Long> paymentTypeIds = agreements.stream().map(LeaseAgreement::getPaymentTypeId).filter(Objects::nonNull)
                .distinct().toList();
        List<Long> leaseTermIds = agreements.stream().map(LeaseAgreement::getLeaseTermId).filter(Objects::nonNull)
                .distinct().toList();

        Map<Long, ApartmentInfo> apartmentMap = apartmentIds.isEmpty()
                ? Collections.emptyMap()
                : apartmentInfoRepository.findAll((root, query, cb) -> root.get("id").in(apartmentIds)).stream()
                        .collect(Collectors.toMap(ApartmentInfo::getId, apartment -> apartment));

        Map<Long, RoomInfo> roomMap = roomIds.isEmpty()
                ? Collections.emptyMap()
                : roomInfoRepository.findAll((root, query, cb) -> root.get("id").in(roomIds)).stream()
                        .collect(Collectors.toMap(RoomInfo::getId, room -> room));

        Map<Long, PaymentType> paymentTypeMap = paymentTypeIds.isEmpty()
                ? Collections.emptyMap()
                : paymentTypeRepository.findAll((root, query, cb) -> root.get("id").in(paymentTypeIds)).stream()
                        .collect(Collectors.toMap(PaymentType::getId, paymentType -> paymentType));

        Map<Long, LeaseTerm> leaseTermMap = leaseTermIds.isEmpty()
                ? Collections.emptyMap()
                : leaseTermRepository.findAll((root, query, cb) -> root.get("id").in(leaseTermIds)).stream()
                        .collect(Collectors.toMap(LeaseTerm::getId, leaseTerm -> leaseTerm));

        List<AgreementVo> items = agreements.stream()
                .map(agreement -> toAgreementVo(agreement, apartmentMap, roomMap, paymentTypeMap, leaseTermMap))
                .toList();

        return new PageImpl<>(items, pageRequest, agreementPage.getTotalElements());
    }

    @Override
    public AgreementVo getAgreementById(Long id) {
        var optional = leaseAgreementRepository.findById(id);
        if (optional.isEmpty()) {
            return null;
        }

        LeaseAgreement agreement = optional.get();
        AgreementVo agreementVo = new AgreementVo();
        BeanUtils.copyProperties(agreement, agreementVo);

        if (agreement.getApartmentId() != null) {
            var apartmentOptional = apartmentInfoRepository.findById(agreement.getApartmentId());
            apartmentOptional.ifPresent(agreementVo::setApartmentInfo);
        }

        if (agreement.getRoomId() != null) {
            var roomOptional = roomInfoRepository.findById(agreement.getRoomId());
            roomOptional.ifPresent(agreementVo::setRoomInfo);
        }

        if (agreement.getPaymentTypeId() != null) {
            var paymentTypeOptional = paymentTypeRepository.findById(agreement.getPaymentTypeId());
            paymentTypeOptional.ifPresent(agreementVo::setPaymentType);
        }

        if (agreement.getLeaseTermId() != null) {
            var leaseTermOptional = leaseTermRepository.findById(agreement.getLeaseTermId());
            leaseTermOptional.ifPresent(agreementVo::setLeaseTerm);
        }

        return agreementVo;
    }

    @Override
    public boolean removeById(Long id) {
        if (leaseAgreementRepository.existsById(id)) {
            leaseAgreementRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateStatusById(Long id, com.rz.lease.model.enums.LeaseStatus status) {
        var optional = leaseAgreementRepository.findById(id);
        if (optional.isEmpty()) {
            return false;
        }
        LeaseAgreement agreement = optional.get();
        agreement.setStatus(status);
        leaseAgreementRepository.save(agreement);
        return true;
    }

    @Transactional
    @Override
    public List<Long> checkLeaseStatus() {
        System.out.println("fired.....");
        Date now = new Date();
        List<LeaseAgreement> expiringAgreements = leaseAgreementRepository.findAll((root, query, cb) -> cb.and(
                cb.isNotNull(root.get("leaseEndDate")),
                cb.lessThanOrEqualTo(root.get("leaseEndDate"), now),
                root.get("status").in(
                        LeaseStatus.SIGNED,
                        LeaseStatus.WITHDRAWING)));

        if (expiringAgreements.isEmpty()) {
            return Collections.emptyList();
        }

        List<LeaseAgreement> updatedAgreements = new ArrayList<>();
        List<Long> expiredAgreementIds = new ArrayList<>();
        for (LeaseAgreement agreement : expiringAgreements) {
            agreement.setStatus(LeaseStatus.EXPIRED);
            updatedAgreements.add(agreement);
            expiredAgreementIds.add(agreement.getId());
        }

        leaseAgreementRepository.saveAll(updatedAgreements);
        return expiredAgreementIds;
    }

    private AgreementVo toAgreementVo(LeaseAgreement agreement,
            Map<Long, ApartmentInfo> apartmentMap,
            Map<Long, RoomInfo> roomMap,
            Map<Long, PaymentType> paymentTypeMap,
            Map<Long, LeaseTerm> leaseTermMap) {
        AgreementVo agreementVo = new AgreementVo();
        BeanUtils.copyProperties(agreement, agreementVo);
        agreementVo.setApartmentInfo(apartmentMap.get(agreement.getApartmentId()));
        agreementVo.setRoomInfo(roomMap.get(agreement.getRoomId()));
        agreementVo.setPaymentType(paymentTypeMap.get(agreement.getPaymentTypeId()));
        agreementVo.setLeaseTerm(leaseTermMap.get(agreement.getLeaseTermId()));
        return agreementVo;
    }

    private Specification<LeaseAgreement> buildSpecification(AgreementQueryVo queryVo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (queryVo == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (queryVo.getProvinceId() != null || queryVo.getCityId() != null || queryVo.getDistrictId() != null) {
                Join<LeaseAgreement, ApartmentInfo> apartmentJoin = root.join("apartment", JoinType.LEFT);
                if (queryVo.getProvinceId() != null) {
                    predicates.add(cb.equal(apartmentJoin.get("provinceId"), queryVo.getProvinceId()));
                }
                if (queryVo.getCityId() != null) {
                    predicates.add(cb.equal(apartmentJoin.get("cityId"), queryVo.getCityId()));
                }
                if (queryVo.getDistrictId() != null) {
                    predicates.add(cb.equal(apartmentJoin.get("districtId"), queryVo.getDistrictId()));
                }
            }
            if (queryVo.getApartmentId() != null) {
                predicates.add(cb.equal(root.get("apartmentId"), queryVo.getApartmentId()));
            }
            if (queryVo.getRoomNumber() != null && !queryVo.getRoomNumber().isBlank()) {
                Join<LeaseAgreement, RoomInfo> roomJoin = root.join("room", JoinType.LEFT);
                predicates.add(cb.like(roomJoin.get("roomNumber"), "%" + queryVo.getRoomNumber().trim() + "%"));
            }
            if (queryVo.getName() != null && !queryVo.getName().isBlank()) {
                predicates.add(cb.like(root.get("name"), "%" + queryVo.getName().trim() + "%"));
            }
            if (queryVo.getPhone() != null && !queryVo.getPhone().isBlank()) {
                predicates.add(cb.like(root.get("phone"), "%" + queryVo.getPhone().trim() + "%"));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
