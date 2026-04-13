package com.rz.lease.web.admin.service.impl;

import com.rz.lease.common.exception.LeaseException;
import com.rz.lease.common.result.ResultCodeEnum;
import com.rz.lease.model.entity.ApartmentFacility;
import com.rz.lease.model.entity.ApartmentFeeValue;
import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.model.entity.ApartmentLabel;
import com.rz.lease.model.entity.FacilityInfo;
import com.rz.lease.model.entity.FeeKey;
import com.rz.lease.model.entity.FeeValue;
import com.rz.lease.model.entity.GraphInfo;
import com.rz.lease.model.entity.LabelInfo;
import com.rz.lease.model.entity.LeaseAgreement;
import com.rz.lease.model.entity.RoomInfo;
import com.rz.lease.model.entity.ViewAppointment;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.model.enums.LeaseStatus;
import com.rz.lease.model.enums.ReleaseStatus;
import com.rz.lease.web.admin.repository.ApartmentFacilityRepository;
import com.rz.lease.web.admin.repository.ApartmentFeeValueRepository;
import com.rz.lease.web.admin.repository.ApartmentLabelRepository;
import com.rz.lease.web.admin.repository.ApartmentInfoRepository;
import com.rz.lease.web.admin.repository.FacilityInfoRepository;
import com.rz.lease.web.admin.repository.FeeKeyRepository;
import com.rz.lease.web.admin.repository.FeeValueRepository;
import com.rz.lease.web.admin.repository.GraphInfoRepository;
import com.rz.lease.web.admin.repository.LabelInfoRepository;
import com.rz.lease.web.admin.repository.LeaseAgreementRepository;
import com.rz.lease.web.admin.repository.RoomInfoRepository;
import com.rz.lease.web.admin.repository.ViewAppointmentRepository;
import com.rz.lease.web.admin.service.ApartmentInfoService;
import com.rz.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.rz.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.rz.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.rz.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.rz.lease.web.admin.vo.fee.FeeValueVo;
import com.rz.lease.web.admin.vo.graph.GraphVo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author rz
 * @description 针对表的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl implements ApartmentInfoService {
    private static final Logger log = LoggerFactory.getLogger(ApartmentInfoServiceImpl.class);

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
    private FacilityInfoRepository facilityInfoRepository;
    @Autowired
    private LabelInfoRepository labelInfoRepository;
    @Autowired
    private FeeValueRepository feeValueRepository;
    @Autowired
    private FeeKeyRepository feeKeyRepository;
    @Autowired
    private RoomInfoRepository roomInfoRepository;
    @Autowired
    private LeaseAgreementRepository leaseAgreementRepository;
    @Autowired
    private ViewAppointmentRepository viewAppointmentRepository;

    @Transactional
    @Override
    public void saveOrUpdateApartmentInfo(ApartmentSubmitVo apartmentSubmitVo) {
        ApartmentInfo apartmentInfo;
        Long id = apartmentSubmitVo.getId();
        if (id == null) {
            apartmentInfo = new ApartmentInfo();
            BeanUtils.copyProperties(apartmentSubmitVo, apartmentInfo);
            apartmentInfo = apartmentInfoRepository.save(apartmentInfo);
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
                Sort.by(Sort.Direction.ASC, "id"));

        // filtering apartments by province, city, district
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
            log.info("Apartment {}: totalRoomCount={}, occupiedRoomCount={}",
                    entry.getKey(), totalRoomCount, occupiedRoomCount);
            freeRoomCountMap.put(entry.getKey(), Math.max(totalRoomCount - occupiedRoomCount, 0L));
        }
        return freeRoomCountMap;
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        ApartmentInfo apartmentInfo = apartmentInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ApartmentInfo not found"));

        ApartmentDetailVo detailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo, detailVo);

        // set graph info list
        List<GraphVo> graphVoList = graphInfoRepository.findAll((root, query, cb) -> cb.and(
                cb.equal(root.get("itemType"), ItemType.APARTMENT),
                cb.equal(root.get("itemId"), id)))
                .stream()
                .map(graphInfo -> GraphVo.builder()
                        .name(graphInfo.getName())
                        .url(graphInfo.getUrl())
                        .build())
                .toList();
        detailVo.setGraphVoList(graphVoList);

        // get the facility ids for a apartment
        List<Long> facilityIds = apartmentFacilityRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("apartmentId"), id))
                .stream()
                .map(ApartmentFacility::getFacilityId)
                .toList();

        // get the facility info according to the found facility ids
        List<FacilityInfo> facilityInfoList = facilityIds.isEmpty()
                ? Collections.emptyList()
                : facilityInfoRepository.findAll((root, query, cb) -> root.get("id").in(facilityIds));
        detailVo.setFacilityInfoList(facilityInfoList);

        List<Long> labelIds = apartmentLabelRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("apartmentId"), id))
                .stream()
                .map(ApartmentLabel::getLabelId)
                .toList();

        List<LabelInfo> labelInfoList = labelIds.isEmpty()
                ? Collections.emptyList()
                : labelInfoRepository.findAll((root, query, cb) -> root.get("id").in(labelIds));
        detailVo.setLabelInfoList(labelInfoList);

        List<Long> feeValueIds = apartmentFeeValueRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("apartmentId"), id))
                .stream()
                .map(ApartmentFeeValue::getFeeValueId)
                .toList();
        List<FeeValueVo> feeValueVoList = buildFeeValueVoList(feeValueIds);
        detailVo.setFeeValueVoList(feeValueVoList);

        return detailVo;
    }

    @Transactional
    @Override
    public void removeApartmentById(Long id) {
        ApartmentInfo apartmentInfo = apartmentInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ApartmentInfo not found"));

        boolean hasVisibleRooms = roomInfoRepository.exists(
                (root, query, cb) -> cb.equal(root.get("apartmentId"), id));
        if (hasVisibleRooms) {
            throw new LeaseException(ResultCodeEnum.ADMIN_APARTMENT_DELETE_ERROR);
        }

        deleteExistingRelations(id);
        deleteApartmentDependentData(id);
        apartmentInfoRepository.delete(apartmentInfo);
    }

    @Transactional
    @Override
    public void updateReleaseStatusById(Long id, ReleaseStatus status) {
        ApartmentInfo apartmentInfo = apartmentInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ApartmentInfo not found"));
        apartmentInfo.setIsRelease(status);
        apartmentInfoRepository.save(apartmentInfo);
    }

    private void deleteApartmentDependentData(Long apartmentId) {
        List<ViewAppointment> appointments = viewAppointmentRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("apartmentId"), apartmentId));
        if (!appointments.isEmpty()) {
            viewAppointmentRepository.deleteAll(appointments);
        }
    }

    private List<FeeValueVo> buildFeeValueVoList(List<Long> feeValueIds) {
        if (feeValueIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<FeeValue> feeValues = feeValueRepository.findAll((root, query, cb) -> root.get("id").in(feeValueIds));
        Set<Long> feeKeyIds = feeValues.stream()
                .map(FeeValue::getFeeKeyId)
                .filter(keyId -> keyId != null)
                .collect(java.util.stream.Collectors.toSet());

        Map<Long, String> feeKeyNameMap = feeKeyIds.isEmpty()
                ? Collections.emptyMap()
                : feeKeyRepository.findAll((root, query, cb) -> root.get("id").in(feeKeyIds))
                        .stream()
                        .collect(java.util.stream.Collectors.toMap(FeeKey::getId, FeeKey::getName));

        return feeValues.stream().map(feeValue -> {
            FeeValueVo feeValueVo = new FeeValueVo();
            BeanUtils.copyProperties(feeValue, feeValueVo);
            feeValueVo.setFeeKeyName(feeKeyNameMap.get(feeValue.getFeeKeyId()));
            return feeValueVo;
        }).toList();
    }

    @Override
    public List<ApartmentItemVo> listInfoByDistrictId(Long id) {
        List<ApartmentInfo> apartmentInfos = apartmentInfoRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("districtId"), id));

        if (apartmentInfos.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> apartmentIds = apartmentInfos.stream().map(ApartmentInfo::getId).toList();
        List<RoomInfo> rooms = roomInfoRepository.findAll(
                (root, query, cb) -> root.get("apartmentId").in(apartmentIds));
        Map<Long, Long> totalRoomCountMap = buildTotalRoomCountMap(rooms);
        Map<Long, Long> freeRoomCountMap = buildFreeRoomCountMap(apartmentIds, rooms);

        return apartmentInfos.stream().map(apartmentInfo -> {
            ApartmentItemVo itemVo = new ApartmentItemVo();
            BeanUtils.copyProperties(apartmentInfo, itemVo);
            itemVo.setTotalRoomCount(totalRoomCountMap.getOrDefault(apartmentInfo.getId(), 0L));
            itemVo.setFreeRoomCount(freeRoomCountMap.getOrDefault(apartmentInfo.getId(), 0L));
            return itemVo;
        }).toList();
    }
}
