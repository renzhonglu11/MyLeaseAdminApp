package com.rz.lease.web.app.service.impl;

import com.rz.lease.model.entity.GraphInfo;
import com.rz.lease.model.entity.ViewAppointment;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.web.app.repository.GraphInfoRepository;
import com.rz.lease.web.app.repository.ViewAppointmentRepository;
import com.rz.lease.web.app.service.ApartmentInfoService;
import com.rz.lease.web.app.service.ViewAppointmentService;
import com.rz.lease.web.app.vo.apartment.ApartmentItemVo;
import com.rz.lease.web.app.vo.appointment.AppointmentDetailVo;
import com.rz.lease.web.app.vo.appointment.AppointmentItemVo;
import com.rz.lease.web.app.vo.graph.GraphVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViewAppointmentServiceImpl implements ViewAppointmentService {

    private final ViewAppointmentRepository viewAppointmentRepository;
    private final ApartmentInfoService apartmentInfoService;
    private final GraphInfoRepository graphInfoRepository;

    public ViewAppointmentServiceImpl(ViewAppointmentRepository viewAppointmentRepository,
                                      ApartmentInfoService apartmentInfoService,
                                      GraphInfoRepository graphInfoRepository) {
        this.viewAppointmentRepository = viewAppointmentRepository;
        this.apartmentInfoService = apartmentInfoService;
        this.graphInfoRepository = graphInfoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentItemVo> listItem(Long userId) {
        return viewAppointmentRepository.findByUserIdOrderByAppointmentTimeDesc(userId)
                .stream()
                .map(this::toAppointmentItemVo)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDetailVo getDetailById(Long id) {
        ViewAppointment viewAppointment = viewAppointmentRepository.findById(id).orElse(null);
        if (viewAppointment == null) {
            return null;
        }
        ApartmentItemVo apartmentItemVo = apartmentInfoService.selectApartmentItemVoById(viewAppointment.getApartmentId());
        AppointmentDetailVo appointmentDetailVo = new AppointmentDetailVo();
        BeanUtils.copyProperties(viewAppointment, appointmentDetailVo);
        appointmentDetailVo.setApartmentItemVo(apartmentItemVo);
        return appointmentDetailVo;
    }

    @Override
    @Transactional
    public void saveOrUpdate(ViewAppointment viewAppointment) {
        viewAppointmentRepository.save(viewAppointment);
    }

    private AppointmentItemVo toAppointmentItemVo(ViewAppointment viewAppointment) {
        AppointmentItemVo itemVo = new AppointmentItemVo();
        itemVo.setId(viewAppointment.getId());
        itemVo.setAppointmentTime(viewAppointment.getAppointmentTime());
        itemVo.setAppointmentStatus(viewAppointment.getAppointmentStatus());
        itemVo.setApartmentName(viewAppointment.getApartment() == null ? null : viewAppointment.getApartment().getName());
        itemVo.setGraphVoList(findGraphVoList(ItemType.APARTMENT, viewAppointment.getApartmentId()));
        return itemVo;
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
