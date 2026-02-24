package com.ngv.zns_service.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.constant.ErrorMessages;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSNDungSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ngv.zns_service.dto.response.ZSSNDungResponse;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.model.entity.ZSSDvu;
import com.ngv.zns_service.model.entity.ZSSNDung;
import com.ngv.zns_service.repository.ZSSDvuRepository;
import com.ngv.zns_service.repository.ZSSNDungRepository;
import com.ngv.zns_service.service.ZSSNDungService;
import com.ngv.zns_service.util.date.DateTimeConverter;
import com.ngv.zns_service.util.page.PageUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZSSNDungServiceImpl implements ZSSNDungService {
    private final ExcelService excelService;
    private final ZSSNDungRepository zssnDungRepository;
    private final ZSSDvuRepository zssDvuRepository;
    private final DateTimeConverter dateTimeConverter;

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(ZSSNDungSearchRequest request, String fileName) {
        List<ZSSNDungResponse> zssnDungResponses = zssnDungRepository.searchToExcel(
                request.getTuNgay(),
                request.getDenNgay(),
                request.getMaDvu(),
                request.getMaDvi(),
                request.getTthaiGuizns());

        return excelService.exportToExcel(
                zssnDungResponses, request.getLabel(), ZSSNDungResponse.class, fileName);
    }

    @Override
    public Page<ZSSNDungResponse> searchAll(SearchFilterRequest filterRequest) {
        Pageable pageable = PageUtils.toPageable(filterRequest.getPageable());
        Page<ZSSNDungResponse> ctaoGtriTsoResponses = zssnDungRepository.searchAll(filterRequest.getFilter(), pageable);
        return ctaoGtriTsoResponses;
    }

    @Override
    public Page<ZSSNDungResponse> findAll(ZSSNDungSearchRequest request) {

        Pageable pageable = PageUtils.toPageable(request.getPageable());
        Page<ZSSNDungResponse> page = zssnDungRepository.search(
                request.getTuNgay(),
                request.getDenNgay(),
                request.getMaDvu(),
                request.getMaDvi(),
                request.getTthaiGuizns(),
                pageable);
        return page;
    }

    @Override
    public Map<String, String> listTenDvu() {
        return zssDvuRepository.findAll().stream().collect(Collectors.toMap(ZSSDvu::getMaDvu, ZSSDvu::getTenDvu));
    }

    @Transactional
    public void userReceivedMessage(ObjectNode data) throws ValidationException {
        try {
            // Lấy dữ liệu từ payload JSON
            String eventName = data.path("event_name").asText(null);
            String msgId = data.path("message").path("msg_id").asText(null);
            long deliveryTime = data.path("message").path("delivery_time").asLong(0);

            if (deliveryTime == 0) {
                deliveryTime = data.path("timestamp").asLong(0);
            }

            if (msgId == null || deliveryTime == 0 || eventName == null) {
                throw new ValidationException("webhook_data",
                        "Sự kiện gửi tin từ OA!");
            }

            // Gọi method mới tìm theo msgId
            Optional<ZSSNDung> optional = zssnDungRepository.findByIdMessage(msgId);
            if (optional.isEmpty()) {
                throw new ValidationException("Không tìm thấy bản ghi ZSS_NDUNG với msg_id: " + msgId,
                        ErrorMessages.NOT_FOUND);
            }

            ZSSNDung zns = optional.get();
            String formattedTime = dateTimeConverter.formatToYmdHms(Instant.ofEpochMilli(deliveryTime));
            zns.setTgianGuiTcong(formattedTime);
            zns.setTthaiGuizns("30"); // Trạng thái gửi thành công
            zns.setMaPhhoi(eventName); // MA_PHHOI = event_name

            zssnDungRepository.save(zns);

        } catch (Exception e) {
            throw new ValidationException("userReceivedMessage", "Lỗi xử lý userReceivedMessage: " + e.getMessage());
        }
    }

}
