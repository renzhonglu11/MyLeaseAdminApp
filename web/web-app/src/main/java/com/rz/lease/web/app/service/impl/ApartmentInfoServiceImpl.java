package com.rz.lease.web.app.service.impl;

import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.model.entity.FacilityInfo;
import com.rz.lease.model.entity.GraphInfo;
import com.rz.lease.model.entity.LabelInfo;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.web.app.repository.ApartmentFacilityRepository;
import com.rz.lease.web.app.repository.ApartmentInfoRepository;
import com.rz.lease.web.app.repository.ApartmentLabelRepository;
import com.rz.lease.web.app.repository.GraphInfoRepository;
import com.rz.lease.web.app.repository.RoomInfoRepository;
import com.rz.lease.web.app.service.ApartmentInfoService;
import com.rz.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.rz.lease.web.app.vo.apartment.ApartmentItemVo;
import com.rz.lease.web.app.vo.graph.GraphVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ApartmentInfoServiceImpl implements ApartmentInfoService {

    private final ApartmentInfoRepository apartmentInfoRepository;
    private final ApartmentLabelRepository apartmentLabelRepository;
    private final ApartmentFacilityRepository apartmentFacilityRepository;
    private final GraphInfoRepository graphInfoRepository;
    private final RoomInfoRepository roomInfoRepository;

    public ApartmentInfoServiceImpl(ApartmentInfoRepository apartmentInfoRepository,
                                    ApartmentLabelRepository apartmentLabelRepository,
                                    ApartmentFacilityRepository apartmentFacilityRepository,
                                    GraphInfoRepository graphInfoRepository,
                                    RoomInfoRepository roomInfoRepository) {
        this.apartmentInfoRepository = apartmentInfoRepository;
        this.apartmentLabelRepository = apartmentLabelRepository;
        this.apartmentFacilityRepository = apartmentFacilityRepository;
        this.graphInfoRepository = graphInfoRepository;
        this.roomInfoRepository = roomInfoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ApartmentItemVo selectApartmentItemVoById(Long id) {
        ApartmentInfo apartmentInfo = apartmentInfoRepository.findById(id).orElse(null);
        if (apartmentInfo == null) {
            return null;
        }

        ApartmentItemVo apartmentItemVo = new ApartmentItemVo();
        BeanUtils.copyProperties(apartmentInfo, apartmentItemVo);
        apartmentItemVo.setLabelInfoList(apartmentLabelRepository.findLabelsByApartmentId(id));
        apartmentItemVo.setGraphVoList(findGraphVoList(ItemType.APARTMENT, id));
        apartmentItemVo.setMinRent(roomInfoRepository.findMinRentByApartmentId(id));
        return apartmentItemVo;
    }

    @Override
    @Transactional(readOnly = true)
    public ApartmentDetailVo getApartmentDetailById(Long id) {
        ApartmentInfo apartmentInfo = apartmentInfoRepository.findById(id).orElse(null);
        if (apartmentInfo == null) {
            return null;
        }

        List<LabelInfo> labelInfoList = apartmentLabelRepository.findLabelsByApartmentId(id);
        List<FacilityInfo> facilityInfoList = apartmentFacilityRepository.findFacilitiesByApartmentId(id);
        BigDecimal minRent = roomInfoRepository.findMinRentByApartmentId(id);

        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(findGraphVoList(ItemType.APARTMENT, id));
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setMinRent(minRent);
        return apartmentDetailVo;
    }

    private List<GraphVo> findGraphVoList(ItemType itemType, Long itemId) {
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
}
