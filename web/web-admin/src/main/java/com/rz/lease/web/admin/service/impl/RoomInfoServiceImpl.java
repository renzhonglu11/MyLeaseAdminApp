package com.rz.lease.web.admin.service.impl;

import com.rz.lease.common.constant.RedisConstant;
import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.model.entity.AttrKey;
import com.rz.lease.model.entity.AttrValue;
import com.rz.lease.model.entity.FacilityInfo;
import com.rz.lease.model.entity.GraphInfo;
import com.rz.lease.model.entity.LabelInfo;
import com.rz.lease.model.entity.LeaseAgreement;
import com.rz.lease.model.entity.LeaseTerm;
import com.rz.lease.model.entity.PaymentType;
import com.rz.lease.model.entity.RoomAttrValue;
import com.rz.lease.model.entity.RoomFacility;
import com.rz.lease.model.entity.RoomInfo;
import com.rz.lease.model.entity.RoomLabel;
import com.rz.lease.model.entity.RoomLeaseTerm;
import com.rz.lease.model.entity.RoomPaymentType;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.model.enums.LeaseStatus;
import com.rz.lease.model.enums.ReleaseStatus;
import com.rz.lease.web.admin.repository.ApartmentInfoRepository;
import com.rz.lease.web.admin.repository.AttrKeyRepository;
import com.rz.lease.web.admin.repository.AttrValueRepository;
import com.rz.lease.web.admin.repository.FacilityInfoRepository;
import com.rz.lease.web.admin.repository.GraphInfoRepository;
import com.rz.lease.web.admin.repository.LabelInfoRepository;
import com.rz.lease.web.admin.repository.LeaseAgreementRepository;
import com.rz.lease.web.admin.repository.LeaseTermRepository;
import com.rz.lease.web.admin.repository.PaymentTypeRepository;
import com.rz.lease.web.admin.repository.RoomAttrValueRepository;
import com.rz.lease.web.admin.repository.RoomFacilityRepository;
import com.rz.lease.web.admin.service.RoomInfoService;
import com.rz.lease.web.admin.repository.RoomLabelRepository;
import com.rz.lease.web.admin.repository.RoomLeaseTermRepository;
import com.rz.lease.web.admin.repository.RoomPaymentTypeRepository;
import com.rz.lease.web.admin.repository.RoomInfoRepository;
import com.rz.lease.web.admin.vo.attr.AttrValueVo;
import com.rz.lease.web.admin.vo.graph.GraphVo;
import com.rz.lease.web.admin.vo.room.RoomDetailVo;
import com.rz.lease.web.admin.vo.room.RoomItemVo;
import com.rz.lease.web.admin.vo.room.RoomQueryVo;
import com.rz.lease.web.admin.vo.room.RoomSubmitVo;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author rz
 * @description 针对表的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomInfoServiceImpl implements RoomInfoService {
    private static final Set<LeaseStatus> OCCUPIED_LEASE_STATUSES = EnumSet.of(
            LeaseStatus.SIGNING,
            LeaseStatus.SIGNED,
            LeaseStatus.WITHDRAWING,
            LeaseStatus.RENEWING);

    @Autowired
    private RoomInfoRepository roomInfoRepository;
    @Autowired
    private ApartmentInfoRepository apartmentInfoRepository;
    @Autowired
    private GraphInfoRepository graphInfoRepository;
    @Autowired
    private RoomAttrValueRepository roomAttrValueRepository;
    @Autowired
    private RoomFacilityRepository roomFacilityRepository;
    @Autowired
    private RoomLabelRepository roomLabelRepository;
    @Autowired
    private RoomPaymentTypeRepository roomPaymentTypeRepository;
    @Autowired
    private RoomLeaseTermRepository roomLeaseTermRepository;
    @Autowired
    private AttrValueRepository attrValueRepository;
    @Autowired
    private AttrKeyRepository attrKeyRepository;
    @Autowired
    private FacilityInfoRepository facilityInfoRepository;
    @Autowired
    private LabelInfoRepository labelInfoRepository;
    @Autowired
    private PaymentTypeRepository paymentTypeRepository;
    @Autowired
    private LeaseTermRepository leaseTermRepository;
    @Autowired
    private LeaseAgreementRepository leaseAgreementRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Transactional
    @Override
    public void saveOrUpdateRoomInfo(RoomSubmitVo roomSubmitVo) {
        RoomInfo roomInfo;
        Long id = roomSubmitVo.getId();
        if (id == null) {
            roomInfo = new RoomInfo();
            BeanUtils.copyProperties(roomSubmitVo, roomInfo);
            roomInfo = roomInfoRepository.save(roomInfo);
        } else {
            roomInfo = roomInfoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("RoomInfo not found"));

            roomInfo.setRoomNumber(roomSubmitVo.getRoomNumber());
            roomInfo.setRent(roomSubmitVo.getRent());
            roomInfo.setApartmentId(roomSubmitVo.getApartmentId());
            roomInfo.setIsRelease(roomSubmitVo.getIsRelease());
            roomInfo = roomInfoRepository.save(roomInfo);
        }

        Long roomId = roomInfo.getId();
        deleteExistingRelations(roomId);

        // Delete cache in redis
        String key = RedisConstant.APP_ROOM_PREFIX + roomSubmitVo.getId();
        redisTemplate.delete(key);

        saveAttrValueRelations(roomId, roomSubmitVo.getAttrValueIds());
        saveFacilityRelations(roomId, roomSubmitVo.getFacilityInfoIds());
        saveLabelRelations(roomId, roomSubmitVo.getLabelInfoIds());
        savePaymentTypeRelations(roomId, roomSubmitVo.getPaymentTypeIds());
        saveLeaseTermRelations(roomId, roomSubmitVo.getLeaseTermIds());
        saveGraphRelations(roomId, roomSubmitVo.getGraphVoList());
    }

    @Override
    public Page<RoomItemVo> pageItem(long current, long size, RoomQueryVo queryVo) {
        PageRequest pageRequest = PageRequest.of(
                Math.max((int) current - 1, 0),
                Math.max((int) size, 1),
                Sort.by(Sort.Direction.ASC, "id"));

        Page<RoomInfo> roomPage = roomInfoRepository.findAll(buildSpecification(queryVo), pageRequest);
        if (roomPage.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageRequest, roomPage.getTotalElements());
        }

        List<RoomInfo> rooms = roomPage.getContent();
        List<Long> roomIds = rooms.stream().map(RoomInfo::getId).toList();
        List<Long> apartmentIds = rooms.stream()
                .map(RoomInfo::getApartmentId)
                .filter(apartmentId -> apartmentId != null)
                .distinct()
                .toList();

        Map<Long, ApartmentInfo> apartmentMap = apartmentIds.isEmpty()
                ? Collections.emptyMap()
                : apartmentInfoRepository.findAll((root, query, cb) -> root.get("id").in(apartmentIds))
                        .stream()
                        .collect(java.util.stream.Collectors.toMap(ApartmentInfo::getId, apartment -> apartment));

        Map<Long, LeaseAgreement> activeLeaseMap = buildActiveLeaseMap(roomIds);

        List<RoomItemVo> items = rooms.stream().map(roomInfo -> {
            RoomItemVo itemVo = new RoomItemVo();
            BeanUtils.copyProperties(roomInfo, itemVo);

            LeaseAgreement leaseAgreement = activeLeaseMap.get(roomInfo.getId());
            itemVo.setIsCheckIn(leaseAgreement != null);
            itemVo.setLeaseEndDate(leaseAgreement != null ? leaseAgreement.getLeaseEndDate() : null);
            itemVo.setApartmentInfo(apartmentMap.get(roomInfo.getApartmentId()));
            return itemVo;
        }).toList();

        return new PageImpl<>(items, pageRequest, roomPage.getTotalElements());
    }

    @Override
    public RoomDetailVo getDetailById(Long id) {
        RoomInfo roomInfo = roomInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomInfo not found"));

        RoomDetailVo detailVo = new RoomDetailVo();
        BeanUtils.copyProperties(roomInfo, detailVo);
        if (roomInfo.getApartmentId() != null) {
            apartmentInfoRepository.findById(roomInfo.getApartmentId()).ifPresent(detailVo::setApartmentInfo);
        }

        List<GraphVo> graphVoList = graphInfoRepository.findAll((root, query, cb) -> cb.and(
                cb.equal(root.get("itemType"), ItemType.ROOM),
                cb.equal(root.get("itemId"), id)))
                .stream()
                .map(graphInfo -> GraphVo.builder()
                        .name(graphInfo.getName())
                        .url(graphInfo.getUrl())
                        .build())
                .toList();
        detailVo.setGraphVoList(graphVoList);

        List<Long> attrValueIds = roomAttrValueRepository.findAll((root, query, cb) -> cb.equal(root.get("roomId"), id))
                .stream()
                .map(RoomAttrValue::getAttrValueId)
                .toList();
        detailVo.setAttrValueVoList(buildAttrValueVoList(attrValueIds));

        List<Long> facilityIds = roomFacilityRepository.findAll((root, query, cb) -> cb.equal(root.get("roomId"), id))
                .stream()
                .map(RoomFacility::getFacilityId)
                .toList();
        List<FacilityInfo> facilityInfoList = facilityIds.isEmpty()
                ? Collections.emptyList()
                : facilityInfoRepository.findAll((root, query, cb) -> root.get("id").in(facilityIds));
        detailVo.setFacilityInfoList(facilityInfoList);

        List<Long> labelIds = roomLabelRepository.findAll((root, query, cb) -> cb.equal(root.get("roomId"), id))
                .stream()
                .map(RoomLabel::getLabelId)
                .toList();
        List<LabelInfo> labelInfoList = labelIds.isEmpty()
                ? Collections.emptyList()
                : labelInfoRepository.findAll((root, query, cb) -> root.get("id").in(labelIds));
        detailVo.setLabelInfoList(labelInfoList);

        List<Long> paymentTypeIds = roomPaymentTypeRepository
                .findAll((root, query, cb) -> cb.equal(root.get("roomId"), id))
                .stream()
                .map(RoomPaymentType::getPaymentTypeId)
                .toList();
        List<PaymentType> paymentTypeList = paymentTypeIds.isEmpty()
                ? Collections.emptyList()
                : paymentTypeRepository.findAll((root, query, cb) -> root.get("id").in(paymentTypeIds));
        detailVo.setPaymentTypeList(paymentTypeList);

        List<Long> leaseTermIds = roomLeaseTermRepository.findAll((root, query, cb) -> cb.equal(root.get("roomId"), id))
                .stream()
                .map(RoomLeaseTerm::getLeaseTermId)
                .toList();
        List<LeaseTerm> leaseTermList = leaseTermIds.isEmpty()
                ? Collections.emptyList()
                : leaseTermRepository.findAll((root, query, cb) -> root.get("id").in(leaseTermIds));
        detailVo.setLeaseTermList(leaseTermList);

        return detailVo;
    }

    @Transactional
    @Override
    public void removeRoomById(Long id) {
        RoomInfo roomInfo = roomInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomInfo not found"));
        deleteExistingRelations(id);
        roomInfoRepository.delete(roomInfo);

        // Delete cache in redis
        String key = RedisConstant.APP_ROOM_PREFIX + id;
        redisTemplate.delete(key);
    }

    @Transactional
    @Override
    public void updateReleaseStatusById(Long id, ReleaseStatus status) {
        RoomInfo roomInfo = roomInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomInfo not found"));
        roomInfo.setIsRelease(status);
        roomInfoRepository.save(roomInfo);
    }

    @Override
    public List<RoomInfo> listBasicByApartmentId(Long id) {
        return roomInfoRepository.findAll((root, query, cb) -> cb.equal(root.get("apartmentId"), id));
    }

    private Specification<RoomInfo> buildSpecification(RoomQueryVo queryVo) {
        return (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (queryVo != null) {
                if (queryVo.getApartmentId() != null) {
                    predicates.add(cb.equal(root.get("apartmentId"), queryVo.getApartmentId()));
                }

                if (queryVo.getProvinceId() != null) {
                    predicates.add(cb.equal(root.join("apartment").get("provinceId"), queryVo.getProvinceId()));
                }

                if (queryVo.getCityId() != null) {
                    predicates.add(cb.equal(root.join("apartment").get("cityId"), queryVo.getCityId()));
                }

                if (queryVo.getDistrictId() != null) {
                    predicates.add(cb.equal(root.join("apartment").get("districtId"), queryVo.getDistrictId()));
                }
            }

            return predicates.isEmpty()
                    ? cb.conjunction()
                    : cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    private void deleteExistingRelations(Long roomId) {
        List<RoomAttrValue> roomAttrValues = roomAttrValueRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("roomId"), roomId));
        if (!roomAttrValues.isEmpty()) {
            roomAttrValueRepository.deleteAll(roomAttrValues);
        }

        List<RoomFacility> roomFacilities = roomFacilityRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("roomId"), roomId));
        if (!roomFacilities.isEmpty()) {
            roomFacilityRepository.deleteAll(roomFacilities);
        }

        List<RoomLabel> roomLabels = roomLabelRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("roomId"), roomId));
        if (!roomLabels.isEmpty()) {
            roomLabelRepository.deleteAll(roomLabels);
        }

        List<RoomPaymentType> roomPaymentTypes = roomPaymentTypeRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("roomId"), roomId));
        if (!roomPaymentTypes.isEmpty()) {
            roomPaymentTypeRepository.deleteAll(roomPaymentTypes);
        }

        List<RoomLeaseTerm> roomLeaseTerms = roomLeaseTermRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("roomId"), roomId));
        if (!roomLeaseTerms.isEmpty()) {
            roomLeaseTermRepository.deleteAll(roomLeaseTerms);
        }

        List<GraphInfo> graphInfos = graphInfoRepository.findAll(
                (root, query, cb) -> cb.and(
                        cb.equal(root.get("itemType"), ItemType.ROOM),
                        cb.equal(root.get("itemId"), roomId)));
        if (!graphInfos.isEmpty()) {
            graphInfoRepository.deleteAll(graphInfos);
        }
    }

    private void saveAttrValueRelations(Long roomId, List<Long> attrValueIds) {
        if (attrValueIds == null || attrValueIds.isEmpty()) {
            return;
        }

        List<RoomAttrValue> roomAttrValues = new ArrayList<>();
        for (Long attrValueId : attrValueIds) {
            RoomAttrValue roomAttrValue = new RoomAttrValue();
            roomAttrValue.setRoomId(roomId);
            roomAttrValue.setAttrValueId(attrValueId);
            roomAttrValues.add(roomAttrValue);
        }
        roomAttrValueRepository.saveAll(roomAttrValues);
    }

    private void saveFacilityRelations(Long roomId, List<Long> facilityInfoIds) {
        if (facilityInfoIds == null || facilityInfoIds.isEmpty()) {
            return;
        }

        List<RoomFacility> roomFacilities = new ArrayList<>();
        for (Long facilityId : facilityInfoIds) {
            RoomFacility roomFacility = new RoomFacility();
            roomFacility.setRoomId(roomId);
            roomFacility.setFacilityId(facilityId);
            roomFacilities.add(roomFacility);
        }
        roomFacilityRepository.saveAll(roomFacilities);
    }

    private void saveLabelRelations(Long roomId, List<Long> labelInfoIds) {
        if (labelInfoIds == null || labelInfoIds.isEmpty()) {
            return;
        }

        List<RoomLabel> roomLabels = new ArrayList<>();
        for (Long labelId : labelInfoIds) {
            RoomLabel roomLabel = new RoomLabel();
            roomLabel.setRoomId(roomId);
            roomLabel.setLabelId(labelId);
            roomLabels.add(roomLabel);
        }
        roomLabelRepository.saveAll(roomLabels);
    }

    private void savePaymentTypeRelations(Long roomId, List<Long> paymentTypeIds) {
        if (paymentTypeIds == null || paymentTypeIds.isEmpty()) {
            return;
        }

        List<RoomPaymentType> roomPaymentTypes = new ArrayList<>();
        for (Long paymentTypeId : paymentTypeIds) {
            RoomPaymentType roomPaymentType = new RoomPaymentType();
            roomPaymentType.setRoomId(roomId);
            roomPaymentType.setPaymentTypeId(paymentTypeId);
            roomPaymentTypes.add(roomPaymentType);
        }
        roomPaymentTypeRepository.saveAll(roomPaymentTypes);
    }

    private void saveLeaseTermRelations(Long roomId, List<Long> leaseTermIds) {
        if (leaseTermIds == null || leaseTermIds.isEmpty()) {
            return;
        }

        List<RoomLeaseTerm> roomLeaseTerms = new ArrayList<>();
        for (Long leaseTermId : leaseTermIds) {
            RoomLeaseTerm roomLeaseTerm = new RoomLeaseTerm();
            roomLeaseTerm.setRoomId(roomId);
            roomLeaseTerm.setLeaseTermId(leaseTermId);
            roomLeaseTerms.add(roomLeaseTerm);
        }
        roomLeaseTermRepository.saveAll(roomLeaseTerms);
    }

    private void saveGraphRelations(Long roomId, List<GraphVo> graphVoList) {
        if (graphVoList == null || graphVoList.isEmpty()) {
            return;
        }

        List<GraphInfo> graphInfos = new ArrayList<>();
        for (GraphVo graphVo : graphVoList) {
            GraphInfo graphInfo = new GraphInfo();
            graphInfo.setName(graphVo.getName());
            graphInfo.setUrl(graphVo.getUrl());
            graphInfo.setItemType(ItemType.ROOM);
            graphInfo.setItemId(roomId);
            graphInfos.add(graphInfo);
        }
        graphInfoRepository.saveAll(graphInfos);
    }

    private List<AttrValueVo> buildAttrValueVoList(List<Long> attrValueIds) {
        if (attrValueIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<AttrValue> attrValues = attrValueRepository.findAll((root, query, cb) -> root.get("id").in(attrValueIds));
        List<Long> attrKeyIds = attrValues.stream()
                .map(AttrValue::getAttrKeyId)
                .filter(attrKeyId -> attrKeyId != null)
                .distinct()
                .toList();

        Map<Long, String> attrKeyNameMap = attrKeyIds.isEmpty()
                ? Collections.emptyMap()
                : attrKeyRepository.findAll((root, query, cb) -> root.get("id").in(attrKeyIds))
                        .stream()
                        .collect(java.util.stream.Collectors.toMap(AttrKey::getId, AttrKey::getName));

        return attrValues.stream().map(attrValue -> {
            AttrValueVo attrValueVo = new AttrValueVo();
            BeanUtils.copyProperties(attrValue, attrValueVo);
            attrValueVo.setAttrKeyName(attrKeyNameMap.get(attrValue.getAttrKeyId()));
            return attrValueVo;
        }).toList();
    }

    private Map<Long, LeaseAgreement> buildActiveLeaseMap(List<Long> roomIds) {
        if (roomIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<LeaseAgreement> leaseAgreements = leaseAgreementRepository.findAll((root, query, cb) -> cb.and(
                root.get("roomId").in(roomIds),
                root.get("status").in(OCCUPIED_LEASE_STATUSES)));

        Map<Long, LeaseAgreement> activeLeaseMap = new HashMap<>();
        for (LeaseAgreement leaseAgreement : leaseAgreements) {
            activeLeaseMap.merge(
                    leaseAgreement.getRoomId(),
                    leaseAgreement,
                    (current, candidate) -> Comparator
                            .comparing(LeaseAgreement::getLeaseEndDate,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .compare(current, candidate) >= 0 ? current : candidate);
        }
        return activeLeaseMap;
    }
}
