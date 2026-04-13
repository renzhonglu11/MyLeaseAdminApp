package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.model.entity.ViewAppointment;
import com.rz.lease.model.enums.AppointmentStatus;
import com.rz.lease.web.admin.repository.ApartmentInfoRepository;
import com.rz.lease.web.admin.repository.ViewAppointmentRepository;
import com.rz.lease.web.admin.service.ViewAppointmentService;
import com.rz.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.rz.lease.web.admin.vo.appointment.AppointmentVo;
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
public class ViewAppointmentServiceImpl implements ViewAppointmentService {
    @Autowired
    private ViewAppointmentRepository viewAppointmentRepository;
    @Autowired
    private ApartmentInfoRepository apartmentInfoRepository;

    @Override
    public void updateStatusById(Long id, AppointmentStatus status) {
        ViewAppointment appointment = viewAppointmentRepository.findById(id).orElse(null);
        if (appointment != null) {
            appointment.setAppointmentStatus(status);
            viewAppointmentRepository.save(appointment);
        }
    }

    @Override
    public Page<AppointmentVo> pageItems(long current, long size, AppointmentQueryVo queryVo) {
        PageRequest pageRequest = PageRequest.of(
                Math.max((int) current - 1, 0),
                Math.max((int) size, 1),
                Sort.by(Sort.Direction.ASC, "id"));

        // query database with filter
        Page<ViewAppointment> page = viewAppointmentRepository.findAll(buildSpecification(queryVo), pageRequest);
        if (page.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageRequest, page.getTotalElements());
        }

        List<Long> apartmentIds = page.getContent().stream()
                .map(ViewAppointment::getApartmentId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        // Batch-load apartments
        Map<Long, ApartmentInfo> apartmentMap = apartmentIds.isEmpty()
                ? Collections.emptyMap()
                : apartmentInfoRepository.findAll((root, query, cb) -> root.get("id").in(apartmentIds))
                        .stream()
                        .collect(Collectors.toMap(ApartmentInfo::getId, apartment -> apartment));

        // Covert entity list intno response DTO list
        List<AppointmentVo> items = page.getContent().stream()
                .map(appointment -> {
                    AppointmentVo appointmentVo = new AppointmentVo();
                    BeanUtils.copyProperties(appointment, appointmentVo);
                    appointmentVo.setApartmentInfo(apartmentMap.get(appointment.getApartmentId()));
                    return appointmentVo;
                })
                .toList();

        return new PageImpl<>(items, pageRequest, page.getTotalElements());
    }

    /**
     * provinceId, cityId, districtId, apartmentId, name, phone
     */
    private Specification<ViewAppointment> buildSpecification(AppointmentQueryVo queryVo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (queryVo == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            Join<ViewAppointment, ApartmentInfo> apartmentJoin = null;
            // Joint apartment_info and the view_appointment tables
            if (queryVo.getProvinceId() != null || queryVo.getCityId() != null || queryVo.getDistrictId() != null) {
                apartmentJoin = root.join("apartment", JoinType.LEFT);
            }

            if (queryVo.getProvinceId() != null) {
                predicates.add(cb.equal(apartmentJoin.get("provinceId"), queryVo.getProvinceId()));
            }
            if (queryVo.getCityId() != null) {
                predicates.add(cb.equal(apartmentJoin.get("cityId"), queryVo.getCityId()));
            }
            if (queryVo.getDistrictId() != null) {
                predicates.add(cb.equal(apartmentJoin.get("districtId"), queryVo.getDistrictId()));
            }
            if (queryVo.getApartmentId() != null) {
                predicates.add(cb.equal(root.get("apartmentId"), queryVo.getApartmentId()));
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
