package com.rz.lease.web.app.service.impl;

import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.model.entity.GraphInfo;
import com.rz.lease.model.entity.LeaseAgreement;
import com.rz.lease.model.entity.LeaseTerm;
import com.rz.lease.model.entity.PaymentType;
import com.rz.lease.model.entity.RoomInfo;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.model.enums.LeaseStatus;
import com.rz.lease.web.app.repository.ApartmentInfoRepository;
import com.rz.lease.web.app.repository.GraphInfoRepository;
import com.rz.lease.web.app.repository.LeaseAgreementRepository;
import com.rz.lease.web.app.repository.LeaseTermRepository;
import com.rz.lease.web.app.repository.PaymentTypeRepository;
import com.rz.lease.web.app.repository.RoomInfoRepository;
import com.rz.lease.web.app.service.LeaseAgreementService;
import com.rz.lease.web.app.vo.agreement.AgreementDetailVo;
import com.rz.lease.web.app.vo.agreement.AgreementItemVo;
import com.rz.lease.web.app.vo.graph.GraphVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaseAgreementServiceImpl implements LeaseAgreementService {

    private final LeaseAgreementRepository leaseAgreementRepository;
    private final ApartmentInfoRepository apartmentInfoRepository;
    private final RoomInfoRepository roomInfoRepository;
    private final GraphInfoRepository graphInfoRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final LeaseTermRepository leaseTermRepository;

    public LeaseAgreementServiceImpl(LeaseAgreementRepository leaseAgreementRepository,
                                     ApartmentInfoRepository apartmentInfoRepository,
                                     RoomInfoRepository roomInfoRepository,
                                     GraphInfoRepository graphInfoRepository,
                                     PaymentTypeRepository paymentTypeRepository,
                                     LeaseTermRepository leaseTermRepository) {
        this.leaseAgreementRepository = leaseAgreementRepository;
        this.apartmentInfoRepository = apartmentInfoRepository;
        this.roomInfoRepository = roomInfoRepository;
        this.graphInfoRepository = graphInfoRepository;
        this.paymentTypeRepository = paymentTypeRepository;
        this.leaseTermRepository = leaseTermRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgreementItemVo> listItem(String username) {
        return leaseAgreementRepository.findByPhoneOrderByIdDesc(username)
                .stream()
                .map(this::toAgreementItemVo)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AgreementDetailVo getDetailById(Long id) {
        LeaseAgreement leaseAgreement = leaseAgreementRepository.findById(id).orElse(null);
        if (leaseAgreement == null) {
            return null;
        }

        ApartmentInfo apartmentInfo = findApartment(leaseAgreement.getApartmentId());
        RoomInfo roomInfo = findRoom(leaseAgreement.getRoomId());
        PaymentType paymentType = findPaymentType(leaseAgreement.getPaymentTypeId());
        LeaseTerm leaseTerm = findLeaseTerm(leaseAgreement.getLeaseTermId());

        AgreementDetailVo agreementDetailVo = new AgreementDetailVo();
        BeanUtils.copyProperties(leaseAgreement, agreementDetailVo);
        agreementDetailVo.setApartmentName(apartmentInfo == null ? null : apartmentInfo.getName());
        agreementDetailVo.setRoomNumber(roomInfo == null ? null : roomInfo.getRoomNumber());
        agreementDetailVo.setApartmentGraphVoList(findGraphVoList(ItemType.APARTMENT, leaseAgreement.getApartmentId()));
        agreementDetailVo.setRoomGraphVoList(findGraphVoList(ItemType.ROOM, leaseAgreement.getRoomId()));
        agreementDetailVo.setPaymentTypeName(paymentType == null ? null : paymentType.getName());
        agreementDetailVo.setLeaseTermMonthCount(leaseTerm == null ? null : leaseTerm.getMonthCount());
        agreementDetailVo.setLeaseTermUnit(leaseTerm == null ? null : leaseTerm.getUnit());
        return agreementDetailVo;
    }

    @Override
    @Transactional
    public void updateStatusById(Long id, LeaseStatus leaseStatus) {
        LeaseAgreement leaseAgreement = leaseAgreementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaseAgreement not found"));
        leaseAgreement.setStatus(leaseStatus);
        leaseAgreementRepository.save(leaseAgreement);
    }

    @Override
    @Transactional
    public void saveOrUpdate(LeaseAgreement leaseAgreement) {
        leaseAgreementRepository.save(leaseAgreement);
    }

    private AgreementItemVo toAgreementItemVo(LeaseAgreement leaseAgreement) {
        AgreementItemVo itemVo = new AgreementItemVo();
        itemVo.setId(leaseAgreement.getId());
        itemVo.setLeaseStatus(leaseAgreement.getStatus());
        itemVo.setLeaseStartDate(leaseAgreement.getLeaseStartDate());
        itemVo.setLeaseEndDate(leaseAgreement.getLeaseEndDate());
        itemVo.setSourceType(leaseAgreement.getSourceType());
        itemVo.setRent(leaseAgreement.getRent());

        ApartmentInfo apartmentInfo = findApartment(leaseAgreement.getApartmentId());
        RoomInfo roomInfo = findRoom(leaseAgreement.getRoomId());
        itemVo.setApartmentName(apartmentInfo == null ? null : apartmentInfo.getName());
        itemVo.setRoomNumber(roomInfo == null ? null : roomInfo.getRoomNumber());
        itemVo.setRoomGraphVoList(findGraphVoList(ItemType.ROOM, leaseAgreement.getRoomId()));
        return itemVo;
    }

    private ApartmentInfo findApartment(Long id) {
        return id == null ? null : apartmentInfoRepository.findById(id).orElse(null);
    }

    private RoomInfo findRoom(Long id) {
        return id == null ? null : roomInfoRepository.findById(id).orElse(null);
    }

    private PaymentType findPaymentType(Long id) {
        return id == null ? null : paymentTypeRepository.findById(id).orElse(null);
    }

    private LeaseTerm findLeaseTerm(Long id) {
        return id == null ? null : leaseTermRepository.findById(id).orElse(null);
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
}
