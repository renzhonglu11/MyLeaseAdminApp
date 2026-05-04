package com.rz.lease.web.app.service.impl;

import com.rz.lease.model.entity.BrowsingHistory;
import com.rz.lease.model.entity.GraphInfo;
import com.rz.lease.model.entity.RoomInfo;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.web.app.repository.BrowsingHistoryRepository;
import com.rz.lease.web.app.repository.GraphInfoRepository;
import com.rz.lease.web.app.repository.RoomInfoRepository;
import com.rz.lease.web.app.service.BrowsingHistoryService;
import com.rz.lease.web.app.vo.graph.GraphVo;
import com.rz.lease.web.app.vo.history.HistoryItemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BrowsingHistoryServiceImpl implements BrowsingHistoryService {

    private final BrowsingHistoryRepository browsingHistoryRepository;
    private final RoomInfoRepository roomInfoRepository;
    private final GraphInfoRepository graphInfoRepository;

    public BrowsingHistoryServiceImpl(BrowsingHistoryRepository browsingHistoryRepository,
                                      RoomInfoRepository roomInfoRepository,
                                      GraphInfoRepository graphInfoRepository) {
        this.browsingHistoryRepository = browsingHistoryRepository;
        this.roomInfoRepository = roomInfoRepository;
        this.graphInfoRepository = graphInfoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoryItemVo> pageItemByUserId(Pageable pageable, Long userId) {
        Page<BrowsingHistory> historyPage = browsingHistoryRepository.findByUserIdOrderByBrowseTimeDesc(userId, pageable);
        return new PageImpl<>(historyPage.getContent().stream().map(this::toHistoryItemVo).toList(), pageable,
                historyPage.getTotalElements());
    }

    @Override
    @Transactional
    public void saveHistory(Long userId, Long roomId) {
        BrowsingHistory browsingHistory = browsingHistoryRepository.findFirstByUserIdAndRoomId(userId, roomId)
                .orElseGet(() -> new BrowsingHistory(userId, roomId, new Date()));
        browsingHistory.setBrowseTime(new Date());
        browsingHistoryRepository.save(browsingHistory);
    }

    private HistoryItemVo toHistoryItemVo(BrowsingHistory browsingHistory) {
        HistoryItemVo historyItemVo = new HistoryItemVo();
        BeanUtils.copyProperties(browsingHistory, historyItemVo);

        RoomInfo roomInfo = roomInfoRepository.findById(browsingHistory.getRoomId()).orElse(null);
        if (roomInfo != null) {
            historyItemVo.setRoomNumber(roomInfo.getRoomNumber());
            historyItemVo.setRent(roomInfo.getRent());
            historyItemVo.setRoomGraphVoList(findGraphVoList(ItemType.ROOM, roomInfo.getId()));
            if (roomInfo.getApartment() != null) {
                historyItemVo.setApartmentName(roomInfo.getApartment().getName());
                historyItemVo.setProvinceName(roomInfo.getApartment().getProvinceName());
                historyItemVo.setCityName(roomInfo.getApartment().getCityName());
                historyItemVo.setDistrictName(roomInfo.getApartment().getDistrictName());
            }
        }
        return historyItemVo;
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
