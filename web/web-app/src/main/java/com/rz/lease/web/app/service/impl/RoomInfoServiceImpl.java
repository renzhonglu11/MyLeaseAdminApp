package com.rz.lease.web.app.service.impl;

import com.rz.lease.common.constant.RedisConstant;
import com.rz.lease.common.login.LoginUser;
import com.rz.lease.common.login.LoginUserHolder;
import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.model.entity.AttrValue;
import com.rz.lease.model.entity.FeeValue;
import com.rz.lease.model.entity.GraphInfo;
import com.rz.lease.model.entity.RoomInfo;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.web.app.repository.ApartmentFeeValueRepository;
import com.rz.lease.web.app.repository.ApartmentInfoRepository;
import com.rz.lease.web.app.repository.GraphInfoRepository;
import com.rz.lease.web.app.repository.RoomAttrValueRepository;
import com.rz.lease.web.app.repository.RoomFacilityRepository;
import com.rz.lease.web.app.repository.RoomInfoRepository;
import com.rz.lease.web.app.repository.RoomLabelRepository;
import com.rz.lease.web.app.repository.RoomLeaseTermRepository;
import com.rz.lease.web.app.repository.RoomPaymentTypeRepository;
import com.rz.lease.web.app.service.ApartmentInfoService;
import com.rz.lease.web.app.service.BrowsingHistoryService;
import com.rz.lease.web.app.service.RoomInfoService;
import com.rz.lease.web.app.vo.apartment.ApartmentItemVo;
import com.rz.lease.web.app.vo.attr.AttrValueVo;
import com.rz.lease.web.app.vo.fee.FeeValueVo;
import com.rz.lease.web.app.vo.graph.GraphVo;
import com.rz.lease.web.app.vo.room.RoomDetailVo;
import com.rz.lease.web.app.vo.room.RoomItemVo;
import com.rz.lease.web.app.vo.room.RoomQueryVo;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RoomInfoServiceImpl implements RoomInfoService {

    private static final int ROOM_DETAIL_CACHE_EXPIRE_MINUTES = 30;

    private final RoomInfoRepository roomInfoRepository;
    private final ApartmentInfoRepository apartmentInfoRepository;
    private final GraphInfoRepository graphInfoRepository;
    private final RoomLeaseTermRepository roomLeaseTermRepository;
    private final RoomFacilityRepository roomFacilityRepository;
    private final RoomLabelRepository roomLabelRepository;
    private final RoomPaymentTypeRepository roomPaymentTypeRepository;
    private final RoomAttrValueRepository roomAttrValueRepository;
    private final ApartmentFeeValueRepository apartmentFeeValueRepository;
    private final ApartmentInfoService apartmentInfoService;
    private final BrowsingHistoryService browsingHistoryService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final boolean roomDetailCacheEnabled;

    public RoomInfoServiceImpl(RoomInfoRepository roomInfoRepository,
            ApartmentInfoRepository apartmentInfoRepository,
            GraphInfoRepository graphInfoRepository,
            RoomLeaseTermRepository roomLeaseTermRepository,
            RoomFacilityRepository roomFacilityRepository,
            RoomLabelRepository roomLabelRepository,
            RoomPaymentTypeRepository roomPaymentTypeRepository,
            RoomAttrValueRepository roomAttrValueRepository,
            ApartmentFeeValueRepository apartmentFeeValueRepository,
            ApartmentInfoService apartmentInfoService,
            BrowsingHistoryService browsingHistoryService,
            RedisTemplate<String, Object> redisTemplate,
            @Value("${app.room-detail-cache.enabled:true}") boolean roomDetailCacheEnabled) {
        this.roomInfoRepository = roomInfoRepository;
        this.apartmentInfoRepository = apartmentInfoRepository;
        this.graphInfoRepository = graphInfoRepository;
        this.roomLeaseTermRepository = roomLeaseTermRepository;
        this.roomFacilityRepository = roomFacilityRepository;
        this.roomLabelRepository = roomLabelRepository;
        this.roomPaymentTypeRepository = roomPaymentTypeRepository;
        this.roomAttrValueRepository = roomAttrValueRepository;
        this.apartmentFeeValueRepository = apartmentFeeValueRepository;
        this.apartmentInfoService = apartmentInfoService;
        this.browsingHistoryService = browsingHistoryService;
        this.redisTemplate = redisTemplate;
        this.roomDetailCacheEnabled = roomDetailCacheEnabled;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomItemVo> pageItem(Pageable pageable, RoomQueryVo queryVo) {
        Page<RoomInfo> roomPage = roomInfoRepository.findAll(buildSpecification(queryVo), pageable);
        return new PageImpl<>(roomPage.getContent().stream().map(this::toRoomItemVo).toList(), pageable,
                roomPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomItemVo> pageItemByApartmentId(Pageable pageable, Long id) {
        Page<RoomInfo> roomPage = roomInfoRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("apartmentId"), id), pageable);
        return new PageImpl<>(roomPage.getContent().stream().map(this::toRoomItemVo).toList(), pageable,
                roomPage.getTotalElements());
    }

    @Override
    @Transactional
    public RoomDetailVo getDetailById(Long id) {
        RoomDetailVo roomDetailVo = null;
        String key = RedisConstant.APP_ROOM_PREFIX + id;

        if (roomDetailCacheEnabled) {
            roomDetailVo = (RoomDetailVo) redisTemplate.opsForValue().get(key);
        }

        if (roomDetailVo == null) {
            roomDetailVo = buildRoomDetailVo(id);
            if (roomDetailVo == null) {
                return null;
            }
            if (roomDetailCacheEnabled) {
                redisTemplate.opsForValue().set(key, roomDetailVo, ROOM_DETAIL_CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            }
        }

        LoginUser loginUser = LoginUserHolder.getLoginUser();
        if (loginUser != null) {
            browsingHistoryService.saveHistory(loginUser.getUserId(), id);
        }

        return roomDetailVo;
    }

    private RoomDetailVo buildRoomDetailVo(Long id) {
        RoomInfo roomInfo = roomInfoRepository.findById(id).orElse(null);
        if (roomInfo == null) {
            return null;
        }

        ApartmentItemVo apartmentItemVo = apartmentInfoService.selectApartmentItemVoById(roomInfo.getApartmentId());

        RoomDetailVo roomDetailVo = new RoomDetailVo();
        BeanUtils.copyProperties(roomInfo, roomDetailVo);
        roomDetailVo.setApartmentItemVo(apartmentItemVo);
        roomDetailVo.setGraphVoList(findGraphVoList(ItemType.ROOM, id));
        roomDetailVo.setAttrValueVoList(roomAttrValueRepository.findAttrValuesByRoomId(id).stream()
                .map(this::toAttrValueVo)
                .toList());
        roomDetailVo.setFacilityInfoList(roomFacilityRepository.findFacilitiesByRoomId(id));
        roomDetailVo.setLabelInfoList(roomLabelRepository.findLabelsByRoomId(id));
        roomDetailVo.setPaymentTypeList(roomPaymentTypeRepository.findPaymentTypesByRoomId(id));
        roomDetailVo.setFeeValueVoList(apartmentFeeValueRepository.findFeeValuesByApartmentId(roomInfo.getApartmentId())
                .stream()
                .map(this::toFeeValueVo)
                .toList());
        roomDetailVo.setLeaseTermList(roomLeaseTermRepository.findLeaseTermsByRoomId(id));
        return roomDetailVo;
    }

    private Specification<RoomInfo> buildSpecification(RoomQueryVo queryVo) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();
            if (queryVo == null) {
                return predicate;
            }
            if (queryVo.getProvinceId() != null || queryVo.getCityId() != null || queryVo.getDistrictId() != null) {
                Join<RoomInfo, ApartmentInfo> apartment = root.join("apartment");
                if (queryVo.getProvinceId() != null) {
                    predicate = cb.and(predicate, cb.equal(apartment.get("provinceId"), queryVo.getProvinceId()));
                }
                if (queryVo.getCityId() != null) {
                    predicate = cb.and(predicate, cb.equal(apartment.get("cityId"), queryVo.getCityId()));
                }
                if (queryVo.getDistrictId() != null) {
                    predicate = cb.and(predicate, cb.equal(apartment.get("districtId"), queryVo.getDistrictId()));
                }
            }
            if (queryVo.getMinRent() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("rent"), queryVo.getMinRent()));
            }
            if (queryVo.getMaxRent() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("rent"), queryVo.getMaxRent()));
            }
            if (queryVo.getPaymentTypeId() != null) {
                List<Long> roomIds = roomPaymentTypeRepository.findRoomIdsByPaymentTypeId(queryVo.getPaymentTypeId());
                predicate = roomIds.isEmpty() ? cb.disjunction() : cb.and(predicate, root.get("id").in(roomIds));
            }
            if ("desc".equalsIgnoreCase(queryVo.getOrderType())) {
                query.orderBy(cb.desc(root.get("rent")));
            } else if ("asc".equalsIgnoreCase(queryVo.getOrderType())) {
                query.orderBy(cb.asc(root.get("rent")));
            }
            return predicate;
        };
    }

    private RoomItemVo toRoomItemVo(RoomInfo roomInfo) {
        RoomItemVo roomItemVo = new RoomItemVo();
        BeanUtils.copyProperties(roomInfo, roomItemVo);
        roomItemVo.setGraphVoList(findGraphVoList(ItemType.ROOM, roomInfo.getId()));
        roomItemVo.setLabelInfoList(roomLabelRepository.findLabelsByRoomId(roomInfo.getId()));
        roomItemVo.setApartmentInfo(roomInfo.getApartmentId() == null ? null
                : apartmentInfoRepository.findById(roomInfo.getApartmentId()).orElse(null));
        return roomItemVo;
    }

    private List<GraphVo> findGraphVoList(ItemType itemType, Long itemId) {
        if (itemId == null) {
            return List.of();
        }
        return graphInfoRepository.findByItemTypeAndItemId(itemType, itemId)
                .stream()
                .map(this::toGraphVo)
                .toList();
    }

    private GraphVo toGraphVo(GraphInfo graphInfo) {
        return GraphVo.builder()
                .name(graphInfo.getName())
                .url(graphInfo.getUrl())
                .build();
    }

    private AttrValueVo toAttrValueVo(AttrValue attrValue) {
        AttrValueVo attrValueVo = new AttrValueVo();
        BeanUtils.copyProperties(attrValue, attrValueVo);
        attrValueVo.setAttrKeyName(attrValue.getAttrKey() == null ? null : attrValue.getAttrKey().getName());
        return attrValueVo;
    }

    private FeeValueVo toFeeValueVo(FeeValue feeValue) {
        FeeValueVo feeValueVo = new FeeValueVo();
        BeanUtils.copyProperties(feeValue, feeValueVo);
        feeValueVo.setFeeKeyName(feeValue.getFeeKey() == null ? null : feeValue.getFeeKey().getName());
        return feeValueVo;
    }
}
