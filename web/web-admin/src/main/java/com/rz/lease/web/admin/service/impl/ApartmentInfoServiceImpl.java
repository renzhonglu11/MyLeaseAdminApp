package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.ApartmentFacility;
import com.rz.lease.model.entity.ApartmentFeeValue;
import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.model.entity.ApartmentLabel;
import com.rz.lease.model.entity.GraphInfo;
import com.rz.lease.model.entity.LeaseAgreement;
import com.rz.lease.model.entity.RoomInfo;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.model.enums.LeaseStatus;
import com.rz.lease.web.admin.repository.ApartmentFacilityRepository;
import com.rz.lease.web.admin.repository.ApartmentFeeValueRepository;
import com.rz.lease.web.admin.service.ApartmentInfoService;
import com.rz.lease.web.admin.repository.ApartmentLabelRepository;
import com.rz.lease.web.admin.repository.ApartmentInfoRepository;
import com.rz.lease.web.admin.repository.GraphInfoRepository;
import com.rz.lease.web.admin.repository.LeaseAgreementRepository;
import com.rz.lease.web.admin.repository.RoomInfoRepository;
import com.rz.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.rz.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.rz.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.rz.lease.web.admin.vo.graph.GraphVo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liubo
 * @description 针对表的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl implements ApartmentInfoService {
    private static final Set<LeaseStatus> OCCUPIED_LEASE_STATUSES = EnumSet.of(
            LeaseStatus.SIGNING,
            LeaseStatus.SIGNED,
            LeaseStatus.WITHDRAWING,
            LeaseStatus.RENEWING);

    @Autowired
    private ApartmentInfoRepository apartmentInfoRepository;
    @Autowired
    private ApartmentFacilityRepository apartmentFacilityRepository;
    @Autowired
    private ApartmentLabelRepository apartmentLabelRepository;
    @Autowired
    private ApartmentFeeValueRepository apartmentFeeValueRepository;
    @Autowired
    private GraphInfoRepository graphInfoRepository;
    @Autowired
    private RoomInfoRepository roomInfoRepository;
    @Autowired
    private LeaseAgreementRepository leaseAgreementRepository;

    @Transactional
    @Override
    public void saveOrUpdateApartmentInfo(ApartmentSubmitVo apartmentSubmitVo) {
        ApartmentInfo apartmentInfo;
        Long id = apartmentSubmitVo.getId();
        if (id == null) {
            apartmentInfo = apartmentInfoRepository.save(apartmentSubmitVo);
        } else {
            apartmentInfo = apartmentInfoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ApartmentInfo not found"));

            apartmentInfo.setName(apartmentSubmitVo.getName());
            apartmentInfo.setIntroduction(apartmentSubmitVo.getIntroduction());
            apartmentInfo.setDistrictId(apartmentSubmitVo.getDistrictId());
            apartmentInfo.setDistrictName(apartmentSubmitVo.getDistrictName());
            apartmentInfo.setCityId(apartmentSubmitVo.getCityId());
            apartmentInfo.setCityName(apartmentSubmitVo.getCityName());
            apartmentInfo.setProvinceId(apartmentSubmitVo.getProvinceId());
            apartmentInfo.setProvinceName(apartmentSubmitVo.getProvinceName());
            apartmentInfo.setAddressDetail(apartmentSubmitVo.getAddressDetail());
            apartmentInfo.setLatitude(apartmentSubmitVo.getLatitude());
            apartmentInfo.setLongitude(apartmentSubmitVo.getLongitude());
            apartmentInfo.setPhone(apartmentSubmitVo.getPhone());
            apartmentInfo.setIsRelease(apartmentSubmitVo.getIsRelease());

            apartmentInfo = apartmentInfoRepository.save(apartmentInfo);
        }

        Long apartmentId = apartmentInfo.getId();

        deleteExistingRelations(apartmentId);
        saveFacilityRelations(apartmentId, apartmentSubmitVo.getFacilityInfoIds());
        saveLabelRelations(apartmentId, apartmentSubmitVo.getLabelIds());
        saveFeeValueRelations(apartmentId, apartmentSubmitVo.getFeeValueIds());
        saveGraphRelations(apartmentId, apartmentSubmitVo.getGraphVoList());
    }

    @Override
    public Page<ApartmentItemVo> pageItem(long current, long size, ApartmentQueryVo queryVo) {
        PageRequest pageRequest = PageRequest.of(
                Math.max((int) current - 1, 0),
                Math.max((int) size, 1),
                Sort.by(Sort.Direction.DESC, "id"));

        // database filtering
        Page<ApartmentInfo> apartmentPage = apartmentInfoRepository.findAll(buildSpecification(queryVo), pageRequest);
        if (apartmentPage.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageRequest, apartmentPage.getTotalElements());
        }

        List<ApartmentInfo> apartments = apartmentPage.getContent();
        List<Long> apartmentIds = apartments.stream().map(ApartmentInfo::getId).toList();

        List<RoomInfo> rooms = roomInfoRepository
                .findAll((root, query, cb) -> root.get("apartmentId").in(apartmentIds));

        // Get total rooms and free rooms
        Map<Long, Long> totalRoomCountMap = buildTotalRoomCountMap(rooms);
        Map<Long, Long> freeRoomCountMap = buildFreeRoomCountMap(apartmentIds, rooms);

        List<ApartmentItemVo> items = apartments.stream().map(apartmentInfo -> {
            ApartmentItemVo itemVo = new ApartmentItemVo();
            BeanUtils.copyProperties(apartmentInfo, itemVo);
            itemVo.setTotalRoomCount(totalRoomCountMap.getOrDefault(apartmentInfo.getId(), 0L));
            itemVo.setFreeRoomCount(freeRoomCountMap.getOrDefault(apartmentInfo.getId(), 0L));
            return itemVo;
        }).toList();

        return new PageImpl<>(items, pageRequest, apartmentPage.getTotalElements());
    }

    private void deleteExistingRelations(Long apartmentId) {
        List<ApartmentFacility> apartmentFacilities = apartmentFacilityRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("apartmentId"), apartmentId));
        if (!apartmentFacilities.isEmpty()) {
            apartmentFacilityRepository.deleteAll(apartmentFacilities);
        }

        List<ApartmentLabel> apartmentLabels = apartmentLabelRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("apartmentId"), apartmentId));
        if (!apartmentLabels.isEmpty()) {
            apartmentLabelRepository.deleteAll(apartmentLabels);
        }

        List<ApartmentFeeValue> apartmentFeeValues = apartmentFeeValueRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("apartmentId"), apartmentId));
        if (!apartmentFeeValues.isEmpty()) {
            apartmentFeeValueRepository.deleteAll(apartmentFeeValues);
        }

        List<GraphInfo> graphInfos = graphInfoRepository.findAll(
                (root, query, cb) -> cb.and(
                        cb.equal(root.get("itemType"), ItemType.APARTMENT),
                        cb.equal(root.get("itemId"), apartmentId)));
        if (!graphInfos.isEmpty()) {
            graphInfoRepository.deleteAll(graphInfos);
        }
    }

    private void saveFacilityRelations(Long apartmentId, List<Long> facilityInfoIds) {
        if (facilityInfoIds == null || facilityInfoIds.isEmpty()) {
            return;
        }
        List<ApartmentFacility> apartmentFacilities = new ArrayList<>();
        for (Long facilityId : facilityInfoIds) {
            ApartmentFacility apartmentFacility = new ApartmentFacility();
            apartmentFacility.setApartmentId(apartmentId);
            apartmentFacility.setFacilityId(facilityId);
            apartmentFacilities.add(apartmentFacility);
        }
        apartmentFacilityRepository.saveAll(apartmentFacilities);
    }

    private void saveLabelRelations(Long apartmentId, List<Long> labelIds) {
        if (labelIds == null || labelIds.isEmpty()) {
            return;
        }
        List<ApartmentLabel> apartmentLabels = new ArrayList<>();
        for (Long labelId : labelIds) {
            ApartmentLabel apartmentLabel = new ApartmentLabel();
            apartmentLabel.setApartmentId(apartmentId);
            apartmentLabel.setLabelId(labelId);
            apartmentLabels.add(apartmentLabel);
        }
        apartmentLabelRepository.saveAll(apartmentLabels);
    }

    private void saveFeeValueRelations(Long apartmentId, List<Long> feeValueIds) {
        if (feeValueIds == null || feeValueIds.isEmpty()) {
            return;
        }
        List<ApartmentFeeValue> apartmentFeeValues = new ArrayList<>();
        for (Long feeValueId : feeValueIds) {
            ApartmentFeeValue apartmentFeeValue = new ApartmentFeeValue();
            apartmentFeeValue.setApartmentId(apartmentId);
            apartmentFeeValue.setFeeValueId(feeValueId);
            apartmentFeeValues.add(apartmentFeeValue);
        }
        apartmentFeeValueRepository.saveAll(apartmentFeeValues);
    }

    private void saveGraphRelations(Long apartmentId, List<GraphVo> graphVoList) {
        if (graphVoList == null || graphVoList.isEmpty()) {
            return;
        }
        List<GraphInfo> graphInfos = new ArrayList<>();
        for (GraphVo graphVo : graphVoList) {
            GraphInfo graphInfo = new GraphInfo();
            graphInfo.setName(graphVo.getName());
            graphInfo.setUrl(graphVo.getUrl());
            graphInfo.setItemType(ItemType.APARTMENT);
            graphInfo.setItemId(apartmentId);
            graphInfos.add(graphInfo);
        }
        graphInfoRepository.saveAll(graphInfos);

    }

    private Specification<ApartmentInfo> buildSpecification(ApartmentQueryVo queryVo) {
        return (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (queryVo != null) {
                if (queryVo.getProvinceId() != null) {
                    predicates.add(cb.equal(root.get("provinceId"), queryVo.getProvinceId()));
                }
                if (queryVo.getCityId() != null) {
                    predicates.add(cb.equal(root.get("cityId"), queryVo.getCityId()));
                }
                if (queryVo.getDistrictId() != null) {
                    predicates.add(cb.equal(root.get("districtId"), queryVo.getDistrictId()));
                }
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    private Map<Long, Long> buildTotalRoomCountMap(List<RoomInfo> rooms) {
        Map<Long, Long> totalRoomCountMap = new HashMap<>();
        for (RoomInfo room : rooms) {
            // not in map, put 1; in map, add 1 to existing value
            totalRoomCountMap.merge(room.getApartmentId(), 1L, Long::sum);
        }
        return totalRoomCountMap;
    }

    private Map<Long, Long> buildFreeRoomCountMap(List<Long> apartmentIds, List<RoomInfo> rooms) {
        Map<Long, Set<Long>> apartmentRoomIdsMap = new HashMap<>();
        for (RoomInfo room : rooms) {
            apartmentRoomIdsMap.computeIfAbsent(room.getApartmentId(), key -> new HashSet<>()).add(room.getId());
        }

        // Find all lease agreements with apartmentId in apartmentIds and status in
        // OCCUPIED_LEASE_STATUSES
        List<LeaseAgreement> leaseAgreements = leaseAgreementRepository.findAll((root, query, cb) -> cb.and(
                root.get("apartmentId").in(apartmentIds),
                root.get("status").in(OCCUPIED_LEASE_STATUSES)));

        Map<Long, Set<Long>> occupiedRoomIdsMap = new HashMap<>();
        for (LeaseAgreement leaseAgreement : leaseAgreements) {
            occupiedRoomIdsMap
                    .computeIfAbsent(leaseAgreement.getApartmentId(), key -> new HashSet<>())
                    .add(leaseAgreement.getRoomId());
        }

        Map<Long, Long> freeRoomCountMap = new HashMap<>();
        for (Map.Entry<Long, Set<Long>> entry : apartmentRoomIdsMap.entrySet()) {
            long totalRoomCount = entry.getValue().size();
            long occupiedRoomCount = occupiedRoomIdsMap.getOrDefault(entry.getKey(), Collections.emptySet()).size();
            freeRoomCountMap.put(entry.getKey(), Math.max(totalRoomCount - occupiedRoomCount, 0L));
        }
        return freeRoomCountMap;
    }
}
