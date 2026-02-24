package com.ngv.zns_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.ngv.zns_service.constant.ErrorMessages;
import com.ngv.zns_service.dto.request.*;
import com.ngv.zns_service.dto.response.DCDviDto;
import com.ngv.zns_service.dto.response.MauZNSResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ngv.zns_service.dto.response.ZSSMauZNZResponse;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.model.entity.*;
import com.ngv.zns_service.repository.*;
import com.ngv.zns_service.service.ZNSService;
import com.ngv.zns_service.service.ZSSMauZNZService;

import com.ngv.zns_service.util.date.DateTimeConverter;
import com.ngv.zns_service.util.file.FileUtil;
import com.ngv.zns_service.util.page.PageUtils;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ZSSMauZNZServiceImpl extends BaseStoredProcedureService implements ZSSMauZNZService {
    private final ZSSMauZNZRepository zssMauZNZRepository;
    private final ExcelService excelService;
    private final ZSSDmucCtietRepository zssDmucCtietRepository;
    private final DCDviRepository dcDviRepository;
    private final ZSSCtaoGtriTsoRepository zssCtaoGtriTsoRepository;
    private final ZSSMauZNSHanhRepository mauZNSHanhRepository;
    private final ZSSMauZNSBangRepository zssMauZNSBangRepository;
    private final ZSSMauZNSButtonRepository zssMauZNSButtonRepository;
    private final ZSSMauZNSTsoRepository zssMauZNSTsoRepository;
    
    private final DateTimeConverter dateTimeConverter;
    private final ZNSService znsService;
    private final ZZSMauZNSTToanRepository zssMauZNSTToanRepository;
    private final ZssTKhoanZoaRepository zssTKhoanZoaRepository;

    @Override
    public Page<ZSSMauZNZResponse> findAll(ZSSMauZNZSearchRequest request) {
        Pageable pageable = PageUtils.toPageable(request.getPageable());
        Page<ZSSMauZNZResponse> zssMauZNZPage = zssMauZNZRepository.search(
                request.getMaLoaiMau(),
                request.getTthaiNvu(),
                request.getTthaiZns(),
                request.getMaDvi(),
                pageable);
        return zssMauZNZPage;
    }

    @Override
    public Page<ZSSMauZNZResponse> searchAll(SearchFilterRequest request) {
        Pageable pageable = PageUtils.toPageable(request.getPageable());
        Page<ZSSMauZNZResponse> page = zssMauZNZRepository.searchAll(request.getFilter(), pageable);
        return page;
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(ZSSMauZNZSearchRequest request, String fileName) {
        List<ZSSMauZNZResponse> zssMauZNZResponseList = zssMauZNZRepository.searchToExportExcel(
                request.getMaLoaiMau(),
                request.getTthaiNvu(),
                request.getTthaiZns(),
                request.getMaDvi());
        return excelService.exportToExcel(zssMauZNZResponseList, request.getLabel(), ZSSMauZNZResponse.class, fileName);
    }

    @Override
    public Map<String, String> listLoaiNdungMau() {
        List<ZSSDmucCtiet> dmucCtiets = zssDmucCtietRepository.listLoaiNdungMau();
        return dmucCtiets.stream()
                .collect(Collectors.toMap(ZSSDmucCtiet::getMaDmucCtiet, ZSSDmucCtiet::getTenDmucCtiet));
    }

    @Override
    public Map<String, String> listTthaiMau() {
        List<ZSSDmucCtiet> dmucCtiets = zssDmucCtietRepository.listTthaiMau();
        return dmucCtiets.stream()
                .collect(Collectors.toMap(ZSSDmucCtiet::getMaDmucCtiet, ZSSDmucCtiet::getTenDmucCtiet));
    }

    @Override
    public Map<String, String> listTenDvi() {
        List<DCDviDto> dcDvis = dcDviRepository.listTenDviDkyThueBao();
        return dcDvis.stream().collect(Collectors.toMap(
                DCDviDto::getMaDvi,
                DCDviDto::getTenDvi,
                (existing, replacement) -> existing));
    }

    @Override
    public Map<String, String> listMucDichGui() {
        List<ZSSDmucCtiet> dmucCtiets = zssDmucCtietRepository.listMucDichGui();
        return dmucCtiets.stream()
                .collect(Collectors.toMap(ZSSDmucCtiet::getMaDmucCtiet, ZSSDmucCtiet::getTenDmucCtiet));
    }

    @Override
    public Map<String, String> listLoaiButton() {
        List<ZSSDmucCtiet> dmucCtiets = zssDmucCtietRepository.listLoaiButton();
        return dmucCtiets.stream()
                .collect(Collectors.toMap(ZSSDmucCtiet::getMaDmucCtiet, ZSSDmucCtiet::getTenDmucCtiet));
    }

    @Override
    public Map<String, String> listGiaTriTSo() {
        List<ZSSCtaoGtriTso> zssCtaoGtriTsos = zssCtaoGtriTsoRepository.findAll();
        return zssCtaoGtriTsos.stream()
                .collect(Collectors.toMap(ZSSCtaoGtriTso::getMaCtaoGtriTso, ZSSCtaoGtriTso::getTenCtaoGtriTso));
    }

    @Override
    public List<MauZNSResponse> syncTemplateZns(String maDvi) throws ValidationException {
        Boolean checkMaDviExist = zssMauZNZRepository.existsByMaDvi(maDvi);
        if (!checkMaDviExist) {
            throw new ValidationException("MaDvi: " + maDvi, ErrorMessages.NOT_FOUND);
        }
        List<MauZNSResponse> responses = zssMauZNZRepository.syncTemplateZns(maDvi);

        // Lấy tất cả maMau từ responses
        List<String> maMauList = responses.stream()
                .map(MauZNSResponse::getMaMau)
                .collect(Collectors.toList());

        // Lấy toàn bộ buttons và group theo maMau
        Map<String, List<MauButtonRequest>> buttonMap = zssMauZNSButtonRepository.findButtonsByMaMaus(maMauList)
                .stream()
                .collect(Collectors.groupingBy(MauButtonRequest::getMaMau));

        // // Lấy toàn bộ tSoRequests và group theo maMau
        // Map<String, List<CaiDatTSoRequest>> tsoMap =
        // zssMauZNSTsoRepository.findTSoByMaMaus(maMauList)
        // .stream()
        // .collect(Collectors.groupingBy(CaiDatTSoRequest::getMaMau));
        List<CaiDatTSoRequest> caiDatTSoRequests = zssMauZNSTsoRepository.findTSoByMaMaus(maMauList);
        for (CaiDatTSoRequest caiDatTSoRequest : caiDatTSoRequests) {
            List<CtaoGtriTsoRequest> ctaoGtriTsoRequestList = zssCtaoGtriTsoRepository
                    .listByMaCtaoTso(caiDatTSoRequest.getMaCtaoGtriTso());
            caiDatTSoRequest.setCtaoGtriTsoRequests(ctaoGtriTsoRequestList);
        }
        // Lấy toàn bộ tSoRequests và group theo maMau
        Map<String, List<CaiDatTSoRequest>> tsoMap = caiDatTSoRequests
                .stream()
                .collect(Collectors.groupingBy(CaiDatTSoRequest::getMaMau));

        // Lấy toàn bộ MauBangRequest và group theo maMau
        Map<String, List<MauBangRequest>> bangMap = zssMauZNSBangRepository.findMauBang(maMauList)
                .stream()
                .collect(Collectors.groupingBy(MauBangRequest::getMaMau));

        // Gán vào từng response
        responses.forEach(response -> {
            response.setMauBangs(bangMap.getOrDefault(response.getMaMau(), Collections.emptyList()));
            response.setMauButtons(buttonMap.getOrDefault(response.getMaMau(), Collections.emptyList()));
            response.setTSoRequests(tsoMap.getOrDefault(response.getMaMau(), Collections.emptyList()));
        });

        return responses;
    }

    @Override
    @Transactional
    public void syncTemplateDetailZns(String oaId, String templateId) throws ValidationException {
        MauZNSRequest request = new MauZNSRequest();
        ObjectNode res = znsService.sendGetTemplateInfo(oaId, templateId);
        ObjectNode listRes = znsService.sendGetTemplateList(oaId, 0, 100, 0);

        if (res.get("error").asInt() == 0) {
            JsonNode data = res.get("data");

            String maDvi = zssTKhoanZoaRepository.findMaDviByZoaId(oaId);
            if (maDvi == null) {
                throw new ValidationException("MaDvi: " + maDvi, ErrorMessages.NOT_FOUND);
            }
            request.setMaDvi(maDvi);

            Optional<String> existingMaMau = zssMauZNZRepository.findMaMauByMaMauDtacAndZoaId(templateId, oaId);
            String maMau;
            if (existingMaMau.isPresent()) {
                maMau = existingMaMau.get();
            } else {
                maMau = UUID.randomUUID().toString().replace("-", "");
            }

            request.setMaMau(maMau);
            request.setZoaId(oaId);
            request.setTenMau(data.has("templateName") ? data.get("templateName").asText() : null);
            request.setDonGia(data.has("price") ? new BigDecimal(data.get("price").asText()) : BigDecimal.ZERO);
            request.setMaCluongMau(data.has("templateQuality") ? data.get("templateQuality").asText() : null);
            request.setLyDo(data.has("reason") ? data.get("reason").asText() : null);
            request.setTimeout(data.has("timeout") ? new BigDecimal(data.get("timeout").asText()) : BigDecimal.ZERO);
            request.setLket(data.has("previewUrl") ? data.get("previewUrl").asText() : null);
            request.setTthaiZns(data.has("status") ? data.get("status").asText() : null);
            request.setMaMauDtac(templateId);
            request.setLoaiZns("1");

            // Gán maLoaiMau dựa trên templateTag
            String templateTag = data.has("templateTag") ? data.get("templateTag").asText() : null;
            String maLoaiMau = null;

            if ("TRANSACTION".equalsIgnoreCase(templateTag)) {
                maLoaiMau = "1";
            } else if ("CUSTOMER_CARE".equalsIgnoreCase(templateTag)) {
                maLoaiMau = "2";
            } else if ("PROMOTION".equalsIgnoreCase(templateTag)) {
                maLoaiMau = "3";
            }

            request.setMaLoaiMau(maLoaiMau);

            // Map danh sách tham số (listParams → CaiDatTSoRequest)
            if (data.has("listParams") && data.get("listParams").isArray()) {
                List<CaiDatTSoRequest> caiDatTSoList = new ArrayList<>();
                for (JsonNode param : data.get("listParams")) {
                    String tenTso = param.has("name") ? param.get("name").asText() : null;

                    // Check nếu đã tồn tại trong DB
                    List<String> existingMaCtaoGtriTsoList = zssMauZNSTsoRepository.findMaCtaoGtriTsoByMaMauAndZoaIdAndTenTso(maMau, oaId, tenTso);
                    String maCtaoGtriTso = existingMaCtaoGtriTsoList.isEmpty() ? null : existingMaCtaoGtriTsoList.get(0);

                    CaiDatTSoRequest paramRequest = new CaiDatTSoRequest(
                            maMau,
                            tenTso,
                            maCtaoGtriTso,
                            null,
                            null,
                            param.has("require") ? param.get("require").asText() : null,
                            param.has("type") ? param.get("type").asText() : null,
                            param.has("maxLength") ? new BigDecimal(param.get("maxLength").asInt()) : null,
                            param.has("minLength") ? new BigDecimal(param.get("minLength").asInt()) : null,
                            param.has("acceptNull") ? (param.get("acceptNull").asBoolean() ? "1" : "0") : null
                    );

                    caiDatTSoList.add(paramRequest);
                }
                request.setCaiDatTSo(caiDatTSoList);
            }


            // Map danh sách button (listButtons → MauButtonRequest)
            if (data.has("listButtons") && data.get("listButtons").isArray()) {
                List<MauButtonRequest> mauButtonsList = new ArrayList<>();
                for (JsonNode button : data.get("listButtons")) {
                    MauButtonRequest buttonRequest = new MauButtonRequest();
                    buttonRequest.setMaMau(maMau);
                    buttonRequest.setLoaiButton(button.has("type") ? String.valueOf(button.get("type").asInt()) : null);
                    buttonRequest.setNdung(button.has("title") ? button.get("title").asText() : null);
                    buttonRequest.setLket(button.has("content") ? button.get("content").asText() : null);
                    mauButtonsList.add(buttonRequest);
                }
                request.setMauButtons(mauButtonsList);
            }

            // Map danh sách bảng (listBangs → MauBangRequest)
            if (data.has("listTables") && data.get("listTables").isArray()) {
                List<MauBangRequest> mauBangsList = new ArrayList<>();
                for (JsonNode bang : data.get("listTables")) {
                    MauBangRequest bangRequest = new MauBangRequest();
                    bangRequest.setMaMau(maMau);
                    bangRequest.setTdeBang(bang.get("tdeBang").asText());
                    bangRequest.setNdungBang(bang.get("ndungBang").asText());
                    mauBangsList.add(bangRequest);
                }
                request.setMauBangs(mauBangsList);
            }

            if (listRes.get("error").asInt() == 0) {
                JsonNode dataList = listRes.get("data");
                if (dataList != null && dataList.isArray()) {
                    for (JsonNode item : dataList) {
                        if (item.has("templateId") && item.get("templateId").asText().equals(templateId)) {
                            request.setMaCluongMau(item.has("templateQuality") ? item.get("templateQuality").asText() : null);
                        }
                    }
                }
            }

            if (existingMaMau.isPresent()) {
                updateMauZns(request);
            } else {
                createMauZns(request);
            }
        }
    }

    @Override
    @Transactional
    public void syncAllTemplateZns(String oaId, int offset, int limit, int status) throws ValidationException {
        boolean hasMore = true;

        while (hasMore) {
            ObjectNode response = znsService.sendGetTemplateList(oaId, offset, limit, status);

            if (response.get("error").asInt() != 0) {
                throw new ValidationException(
                        String.format("Lỗi khi lấy danh sách template từ ZNS: %s",
                                response.get("message").asText()),
                        ErrorMessages.NOT_FOUND
                );
            }

            JsonNode data = response.get("data");

            List<String> templateIds = StreamSupport.stream(data.spliterator(), false)
                    .map(node -> node.get("templateId").asText())
                    .collect(Collectors.toList());

            if (templateIds.isEmpty()) {
                break;
            }

            for (String templateId : templateIds) {
                try {
                    syncTemplateDetailZns(oaId, templateId);
                } catch (Exception e) {
                    throw new ValidationException("Error: " + e, ErrorMessages.NOT_FOUND);
                }
            }

            hasMore = data.size() == limit;
            offset += limit;
        }
    }

    @Override
    @Transactional
    public void changeTemplateStatus(ObjectNode data) throws ValidationException {
        String templateId = data.get("template_id").asText();
        String newStatus = data.get("status").get("new_status").asText();
        String reason = data.get("reason").asText();

        Optional<ZSSMauZNZ> mauZNZOptional = zssMauZNZRepository.findByMaMauDtac(templateId);
        if (!mauZNZOptional.isPresent()) {
            throw new ValidationException("templateId: " + templateId, ErrorMessages.NOT_FOUND);
        }

        ZSSMauZNZ mauZNS = mauZNZOptional.get();
        mauZNS.setTthaiZns(newStatus);
        mauZNS.setLyDo(reason);
        zssMauZNZRepository.save(mauZNS);
    }

    @Override
    @Transactional
    public void createMauZns(MauZNSRequest request) {
        String username = getCurrentUserName();
        String dateTimeNow = dateTimeConverter.dateTimeNow();
//        String zoaId = request.getZoaId();
//        String lastThreeZoaId = zoaId.substring(zoaId.length() - 3);
//        Optional<ZSSMauZNZ> mauZNZOptional = zssMauZNZRepository.findLatestByZoaId(zoaId);
//        String maMau;
//        if (mauZNZOptional.isEmpty()) {
//            maMau = lastThreeZoaId + "0001";
//        } else {
//            String maMauLast = mauZNZOptional.get().getMaMau();
//            String numberPart = maMauLast.substring(6);
//            int sequenceNumber = Integer.parseInt(numberPart) +1;
//            maMau = lastThreeZoaId + "000" + sequenceNumber;
//        }
        String maMau = UUID.randomUUID().toString().replace("-", "");

        saveMauZNS(request, maMau, username, dateTimeNow);
        saveMauZNSTsos(request, maMau, username, dateTimeNow);
        saveMauZNSHanh(request, maMau, username, dateTimeNow);

        if (!"2".equals(request.getLoaiZns())) {
            saveMauZNSBangs(request, maMau, username, dateTimeNow);
            saveMauZNSButtons(request, maMau, username, dateTimeNow);
        }

        if ("3".equals(request.getLoaiZns())) {
            saveMauZNSTToan(request, maMau, username, dateTimeNow);
        }
    }

    private void saveMauZNS(MauZNSRequest request, String maMau, String username, String dateTimeNow) {
        ZSSMauZNZ mauZNS = new ZSSMauZNZ();
        mauZNS.setMaDvi(request.getMaDvi());
        mauZNS.setZoaId(request.getZoaId());
        mauZNS.setMaMau(maMau);
        mauZNS.setTenMau(request.getTenMau());
        mauZNS.setLoaiZns(request.getLoaiZns());
        mauZNS.setMaLoaiMau(request.getMaLoaiMau());
        mauZNS.setDonGia(request.getDonGia());
        mauZNS.setTdeMau(request.getTdeMau());
        mauZNS.setNdungTde(request.getNdungTde());
        mauZNS.setVban1(request.getVban1());
        mauZNS.setVban2(request.getVban2());
        mauZNS.setVban3(request.getVban3());
        mauZNS.setVban4(request.getVban4());
        mauZNS.setGhiChu(request.getGhiChu());
        mauZNS.setTthaiNvu("DSD");
        mauZNS.setNguoiNhap(username);
        mauZNS.setNguoiSua(username);
        mauZNS.setNgayNhap(dateTimeNow);
        mauZNS.setNgaySua(dateTimeNow);
        mauZNS.setMaCluongMau(request.getMaCluongMau());
        mauZNS.setLyDo(request.getLyDo());
        mauZNS.setTimeout(request.getTimeout());
        mauZNS.setLket(request.getLket());
        mauZNS.setTthaiZns(request.getTthaiZns());
        mauZNS.setMaMauDtac(request.getMaMauDtac());

        zssMauZNZRepository.save(mauZNS);
    }

    private void saveMauZNSBangs(MauZNSRequest request, String maMau, String username, String dateTimeNow) {

        List<ZSSMauZNSBang> zssMauZNSBangs = request.getMauBangs().stream()
                .map(mauBang -> {
                    ZSSMauZNSBang bang = new ZSSMauZNSBang();
                    bang.setMaMau(maMau);
                    bang.setZoaId(request.getZoaId());
                    bang.setMaDvi(request.getMaDvi());
                    bang.setTdeBang(mauBang.getTdeBang());
                    bang.setNdungBang(mauBang.getNdungBang());
                    bang.setNguoiNhap(username);
                    bang.setNguoiSua(username);
                    bang.setNgayNhap(dateTimeNow);
                    bang.setNgaySua(dateTimeNow);

                    return bang;
                }).collect(Collectors.toList());

        zssMauZNSBangRepository.saveAll(zssMauZNSBangs);
    }

    private void saveMauZNSButtons(MauZNSRequest request, String maMau, String username, String dateTimeNow) {
        List<ZSSMauZNSButton> zssMauZNSButtons = request.getMauButtons().stream()
                .map(mauButton -> {
                    ZSSMauZNSButton button = new ZSSMauZNSButton();
                    button.setMaMau(maMau);
                    button.setZoaId(request.getZoaId());
                    button.setMaButton(UUID.randomUUID().toString().replace("-", ""));
                    button.setMaDvi(request.getMaDvi());
                    button.setLoaiButton(mauButton.getLoaiButton());
                    button.setNdung(mauButton.getNdung());
                    button.setLket(mauButton.getLket());
                    button.setNguoiNhap(username);
                    button.setNguoiSua(username);
                    button.setNgayNhap(dateTimeNow);
                    button.setNgaySua(dateTimeNow);
                    return button;
                }).collect(Collectors.toList());

        zssMauZNSButtonRepository.saveAll(zssMauZNSButtons);
    }

    private void saveMauZNSTsos(MauZNSRequest request, String maMau, String username, String dateTimeNow) {
        List<ZSSMauZNSTso> zssMauZNSTsos = request.getCaiDatTSo().stream()
                .map(tSoRequest -> {
                    ZSSMauZNSTso tso = new ZSSMauZNSTso();
                    tso.setMaMau(maMau);
                    tso.setZoaId(request.getZoaId());
                    tso.setMaDvi(request.getMaDvi());
                    tso.setMaTso(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
                    tso.setTenTso(tSoRequest.getTenTso());
                    tso.setMaCtaoGtriTso(tSoRequest.getMaCtaoGtriTso());
                    tso.setParamType(tSoRequest.getParamType());
                    tso.setNdungTso(tSoRequest.getNdungTso());
                    tso.setNguoiNhap(username);
                    tso.setNguoiSua(username);
                    tso.setNgayNhap(dateTimeNow);
                    tso.setNgaySua(dateTimeNow);
                    tso.setBbuoc(tSoRequest.getBbuoc());
                    tso.setKieuTso(tSoRequest.getKieuTso());
                    tso.setNhanGtriNull(tSoRequest.getNhanGtriNull());
                    tso.setSoKtuTda(tSoRequest.getSoKtuTda());
                    tso.setSoKtuTthieu(tSoRequest.getSoKtuTthieu());

                    return tso;
                }).collect(Collectors.toList());

        zssMauZNSTsoRepository.saveAll(zssMauZNSTsos);
    }

    private void saveMauZNSHanh(MauZNSRequest request, String maMau, String username, String dateTimeNow) {
        ZSSMauZNSHanh hanh = new ZSSMauZNSHanh();
        hanh.setMaMau(maMau);
        hanh.setZoaId(request.getZoaId());
        hanh.setLoaiHanh(request.getLoaiHanh());
        hanh.setNguoiNhap(username);
        hanh.setNguoiSua(username);
        hanh.setNgayNhap(dateTimeNow);
        hanh.setNgaySua(dateTimeNow);

        if (request.getLogoSang() != null && !request.getLogoSang().isEmpty()) {
            hanh.setLogoSang(FileUtil.decode(request.getLogoSang()));
            hanh.setTenLogoSang(request.getTenLogoSang());
        }

        if (request.getLogoToi() != null && !request.getLogoToi().isEmpty()) {
            hanh.setLogoToi(FileUtil.decode(request.getLogoToi()));
            hanh.setTenLogoToi(request.getTenLogoToi());
        }

        if (request.getAnh1() != null && !request.getAnh1().isBlank()) {
            hanh.setAnh1(FileUtil.decode(request.getAnh1()));
            hanh.setTenAnh1(request.getTenAnh1());
        }

        if (request.getAnh2() != null && !request.getAnh2().isEmpty()) {
            hanh.setAnh2(FileUtil.decode(request.getAnh2()));
            hanh.setTenAnh2(request.getTenAnh2());
        }

        if (request.getAnh3() != null && !request.getAnh3().isEmpty()) {
            hanh.setAnh3(FileUtil.decode(request.getAnh3()));
            hanh.setTenAnh3(request.getTenAnh3());
        }

        mauZNSHanhRepository.save(hanh);
    }

    private void saveMauZNSTToan(MauZNSRequest request, String maMau, String username, String dateTimeNow) {
        ZSSMauZNSTToan tToan = new ZSSMauZNSTToan();
        tToan.setMaMau(maMau);
        tToan.setZoaId(request.getZoaId());
        tToan.setNguoiNhap(username);
        tToan.setNguoiSua(username);
        tToan.setNgayNhap(dateTimeNow);
        tToan.setNgaySua(dateTimeNow);

        tToan.setMaNganHang(request.getMaNganHang());
        tToan.setTenTkhoan(request.getTenTKhoan());
        tToan.setStk(request.getStk());
        tToan.setSoTien(request.getSoTien());
        tToan.setNoiDungCKhoan(request.getNoiDungCKhoan());

        zssMauZNSTToanRepository.save(tToan);
    }

    @Override
    @Transactional
    public void updateMauZns(MauZNSRequest request) {
        String username = getCurrentUserName();
        String dateTimeNow = dateTimeConverter.dateTimeNow();
        updateMauZNS(request, username, dateTimeNow);
        updateMauZNSTsos(request, username, dateTimeNow);
        updateMauZNSHanh(request, username, dateTimeNow);

        if (!"2".equals(request.getLoaiZns())) {
            updateMauZNSBangs(request, username, dateTimeNow);
            updateMauZNSButtons(request, username, dateTimeNow);
        }

        if ("3".equals(request.getLoaiZns())) {
            updateMauZNSTToan(request, username, dateTimeNow);
        }
    }

    private void updateMauZNS(MauZNSRequest request, String username, String dateTimeNow) {
        Optional<ZSSMauZNZ> mauZNZOptional = zssMauZNZRepository.findByMaMau(request.getMaMau());
        if (mauZNZOptional.isPresent()) {
            ZSSMauZNZ mauZNS = mauZNZOptional.get();
            mauZNS.setMaDvi(request.getMaDvi());
            mauZNS.setZoaId(request.getZoaId());
            mauZNS.setTenMau(request.getTenMau());
            mauZNS.setLoaiZns(request.getLoaiZns());
            mauZNS.setMaLoaiMau(request.getMaLoaiMau());
            mauZNS.setDonGia(request.getDonGia());
            mauZNS.setTdeMau(request.getTdeMau());
            mauZNS.setNdungTde(request.getNdungTde());
            mauZNS.setVban1(request.getVban1());
            mauZNS.setVban2(request.getVban2());
            mauZNS.setVban3(request.getVban3());
            mauZNS.setVban4(request.getVban4());
            mauZNS.setGhiChu(request.getGhiChu());
            mauZNS.setNguoiSua(username);
            mauZNS.setNgaySua(dateTimeNow);
            mauZNS.setMaCluongMau(request.getMaCluongMau());
            mauZNS.setLyDo(request.getLyDo());
            mauZNS.setTimeout(request.getTimeout());
            mauZNS.setLket(request.getLket());
            mauZNS.setTthaiZns(request.getTthaiZns());
            mauZNS.setMaMauDtac(request.getMaMauDtac());
            zssMauZNZRepository.save(mauZNS);
        }

    }

    private void updateMauZNSBangs(MauZNSRequest request, String username, String dateTimeNow) {
        zssMauZNSBangRepository.deleteAllByMaMau(request.getMaMau());
        List<ZSSMauZNSBang> newBangs = request.getMauBangs().stream()
                .map(mauBang -> {
                    ZSSMauZNSBang bang = new ZSSMauZNSBang();
                    bang.setMaMau(request.getMaMau());
                    bang.setZoaId(request.getZoaId());
                    bang.setMaDvi(request.getMaDvi());
                    bang.setTdeBang(mauBang.getTdeBang());
                    bang.setNdungBang(mauBang.getNdungBang());
                    bang.setNguoiSua(username);
                    bang.setNgaySua(dateTimeNow);
                    return bang;
                })
                .collect(Collectors.toList());

        zssMauZNSBangRepository.saveAll(newBangs);
    }

    private void updateMauZNSButtons(MauZNSRequest request, String username, String dateTimeNow) {
        List<ZSSMauZNSButton> newButtons = request.getMauButtons().stream()
                .map(mauButton -> {
                    String maMau = request.getMaMau();
                    String zoaId = request.getZoaId();

                    Optional<ZSSMauZNSButton> existing = zssMauZNSButtonRepository
                            .findByMaMauAndZoaIdAndNdung(maMau, zoaId, mauButton.getNdung());

                    ZSSMauZNSButton button = existing.orElse(new ZSSMauZNSButton());
                    boolean isNew = !existing.isPresent();

                    button.setMaMau(maMau);
                    button.setZoaId(zoaId);

                    if (isNew) {
                        button.setMaButton(UUID.randomUUID().toString().replace("-", ""));
                        button.setNguoiNhap(username);
                        button.setNgayNhap(dateTimeNow);
                    }

                    button.setMaDvi(request.getMaDvi());
                    button.setLoaiButton(mauButton.getLoaiButton());
                    button.setNdung(mauButton.getNdung());
                    button.setLket(mauButton.getLket());
                    button.setNguoiSua(username);
                    button.setNgaySua(dateTimeNow);

                    return button;
                })
                .collect(Collectors.toList());

        zssMauZNSButtonRepository.saveAll(newButtons);
    }

    private void updateMauZNSTsos(MauZNSRequest request, String username, String dateTimeNow) {
        Optional<ZSSMauZNZ> mauZNZOptional = zssMauZNZRepository.findByMaMau(request.getMaMau());
        if (!mauZNZOptional.isPresent()) {
            return;
        }
        List<ZSSMauZNSTso> newTsos = request.getCaiDatTSo().stream()
                .map(tSoRequest -> {
                    String maMau = request.getMaMau();
                    String zoaId = request.getZoaId();
                    String tenTso = tSoRequest.getTenTso();

                    Optional<ZSSMauZNSTso> existing = zssMauZNSTsoRepository
                            .findByMaMauAndZoaIdAndTenTso(maMau, zoaId, tenTso);

                    ZSSMauZNSTso tso = existing.orElse(new ZSSMauZNSTso());
                    boolean isNew = !existing.isPresent();

                    if (isNew) {
                        tso.setMaTso(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
                        tso.setNguoiNhap(username);
                        tso.setNgayNhap(dateTimeNow);
                    }

                    tso.setMaMau(maMau);
                    tso.setZoaId(zoaId);
                    tso.setMaDvi(request.getMaDvi());
                    tso.setTenTso(tenTso);
                    tso.setMaCtaoGtriTso(tSoRequest.getMaCtaoGtriTso());
                    tso.setParamType(tSoRequest.getParamType());
                    tso.setNdungTso(tSoRequest.getNdungTso());
                    tso.setBbuoc(tSoRequest.getBbuoc());
                    tso.setKieuTso(tSoRequest.getKieuTso());
                    tso.setSoKtuTda(tSoRequest.getSoKtuTda());
                    tso.setSoKtuTthieu(tSoRequest.getSoKtuTthieu());
                    tso.setNhanGtriNull(tSoRequest.getNhanGtriNull());
                    tso.setNguoiSua(username);
                    tso.setNgaySua(dateTimeNow);

                    return tso;
                })
                .collect(Collectors.toList());

        zssMauZNSTsoRepository.saveAll(newTsos);
    }

    private void updateMauZNSHanh(MauZNSRequest request, String username, String dateTimeNow) {
        Optional<ZSSMauZNZ> mauZNZOptional = zssMauZNZRepository.findByMaMau(request.getMaMau());
        if (!mauZNZOptional.isPresent()) {
            return;
        }
        mauZNSHanhRepository.deleteByMaMau(request.getMaMau());

        ZSSMauZNSHanh hanh = new ZSSMauZNSHanh();
        hanh.setMaMau(request.getMaMau());
        hanh.setZoaId(request.getZoaId());
        hanh.setLoaiHanh(request.getLoaiHanh());
        hanh.setNguoiSua(username);
        hanh.setNgaySua(dateTimeNow);

        if (isValidBase64(request.getLogoSang())) {
            hanh.setLogoSang(FileUtil.decode(request.getLogoSang()));
            hanh.setTenLogoSang(request.getTenLogoSang());
        }
        if (isValidBase64(request.getLogoToi())) {
            hanh.setLogoToi(FileUtil.decode(request.getLogoToi()));
            hanh.setTenLogoToi(request.getTenLogoToi());
        }
        if (isValidBase64(request.getAnh1())) {
            hanh.setAnh1(FileUtil.decode(request.getAnh1()));
            hanh.setTenAnh1(request.getTenAnh1());
        }
        if (isValidBase64(request.getAnh2())) {
            hanh.setAnh2(FileUtil.decode(request.getAnh2()));
            hanh.setTenAnh2(request.getTenAnh2());
        }
        if (isValidBase64(request.getAnh3())) {
            hanh.setAnh3(FileUtil.decode(request.getAnh3()));
            hanh.setTenAnh3(request.getTenAnh3());
        }
        mauZNSHanhRepository.save(hanh);
    }

    private void updateMauZNSTToan(MauZNSRequest request, String username, String dateTimeNow) {
        ZSSMauZNSTToan tToan = new ZSSMauZNSTToan();
        tToan.setMaMau(request.getMaMau());
        tToan.setZoaId(request.getZoaId());
        tToan.setNguoiNhap(username);
        tToan.setNguoiSua(username);
        tToan.setNgayNhap(dateTimeNow);
        tToan.setNgaySua(dateTimeNow);

        tToan.setMaNganHang(request.getMaNganHang());
        tToan.setTenTkhoan(request.getTenTKhoan());
        tToan.setStk(request.getStk());
        tToan.setSoTien(request.getSoTien());
        tToan.setNoiDungCKhoan(request.getNoiDungCKhoan());

        zssMauZNSTToanRepository.save(tToan);
    }

    private boolean isValidBase64(String base64) {
        return base64 != null && !base64.isBlank();
    }

    @Override
    public MauZNSResponse mauZnsDetail(String maMau) throws ValidationException {
        // Query mã mẫu để lấy ra xem mã mẫu có tồn tại ko và lấy ra loại zns
        Optional<ZSSMauZNZ> mauZNZOptional = zssMauZNZRepository.findByMaMau(maMau);
        MauZNSResponse mauZNSResponse;

        if (!mauZNZOptional.isPresent()) {
            throw new ValidationException("maMau: " + maMau, ErrorMessages.NOT_FOUND);
        }

        // Tùy theo loại zns là gì thì mình gọi phương phức templateZnsDetail tương ứng
        switch (mauZNZOptional.get().getLoaiZns()) {
            case "3":
                // Loại zns là 3, cần query thêm bảng ZSSMauZNSTToan
                mauZNSResponse = zssMauZNZRepository.templateZnsDetailThanhToan(maMau);
                break;
            default:
                // Loại zns khác, vẫn trả về detail cũ
                mauZNSResponse = zssMauZNZRepository.templateZnsDetail(maMau);
                break;
        }

        // Query các thông tin khác
        List<MauButtonRequest> buttons = zssMauZNSButtonRepository.findButtonsByMaMau(maMau);
        List<CaiDatTSoRequest> caiDatTSoRequests = zssMauZNSTsoRepository.findTSoByMaMau(maMau);
        List<MauBangRequest> mauBangs = zssMauZNSBangRepository.findMauBangByMaMau(maMau);
        mauZNSResponse.setMauBangs(mauBangs);
        mauZNSResponse.setMauButtons(buttons);
        mauZNSResponse.setTSoRequests(caiDatTSoRequests);

        return mauZNSResponse;
    }

    @Transactional
    @Override
    public void deleteMauZns(String maMau) throws ValidationException {
        deleteMauZNS(maMau);
        deleteMauZNSBangs(maMau);
        deleteMauZNSButtons(maMau);
        deleteMauZNSTsos(maMau);
        deleteMauZNSHanh(maMau);
        deleteMauZNSTToan(maMau);
    }

    @Override
    public ObjectNode sendApproval(MauZNSRequest request) throws IOException {
        ObjectNode objectNodeImg = uploadImages(request);
        List<String> mediaIdList = new ArrayList<>();
        if (objectNodeImg.has("media_ids")) {
            ArrayNode mediaIdsArray = (ArrayNode) objectNodeImg.get("media_ids");
            for (JsonNode node : mediaIdsArray) {
                mediaIdList.add(node.asText());
            }
        }
        Map<String, Object> payload = createTemplatePayload(request, mediaIdList);
        ObjectNode objectNode = znsService.sendCreateTemplate(request.getZoaId(), payload);
        if (objectNode.has("data")) {
            JsonNode dataNode = objectNode.get("data");
            if (dataNode.has("preview_url")) {
                String previewUrl = dataNode.get("preview_url").asText();
                Optional<ZSSMauZNZ> mauZNZOptional = zssMauZNZRepository.findByMaMau(request.getMaMau());
                if (mauZNZOptional.isPresent()) {
                    ZSSMauZNZ zssMauZNZ = mauZNZOptional.get();
                    zssMauZNZ.setLket(previewUrl);
                    zssMauZNZ.setTthaiZns("PENDING_REVIEW");
                    zssMauZNZRepository.save(zssMauZNZ);
                }
            }
        }
        return objectNode;
    }

    private void deleteMauZNS(String maMau) throws ValidationException {
        Optional<ZSSMauZNZ> mauZNZOptional = zssMauZNZRepository.findByMaMau(maMau);
        if (mauZNZOptional.isPresent()) {
            zssMauZNZRepository.deleteByMaMau(maMau);
        } else {
            throw new ValidationException("maMau: " + maMau, ErrorMessages.NOT_FOUND);
        }

    }

    private void deleteMauZNSBangs(String maMau) {
        zssMauZNSBangRepository.deleteAllByMaMau(maMau);
    }

    private void deleteMauZNSButtons(String maMau) {
        zssMauZNSButtonRepository.deleteAllByMaMau(maMau);
    }

    private void deleteMauZNSTsos(String maMau) {
        zssMauZNSTsoRepository.deleteAllByMaMau(maMau);
    }

    private void deleteMauZNSHanh(String maMau) {
        mauZNSHanhRepository.deleteByMaMau(maMau);
    }

    private void deleteMauZNSTToan(String maMau) {
        zssMauZNSTToanRepository.deleteByMaMau(maMau);
    }

    public ObjectNode uploadImages(MauZNSRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ImageData> images = new ArrayList<>();
        ArrayNode mediaIds = objectMapper.createArrayNode();

        if (request.getLogoSang() != null) {
            images.add(new ImageData(request.getLogoSang(), request.getTenLogoSang()));
        }
        if (request.getLogoToi() != null) {
            images.add(new ImageData(request.getLogoToi(), request.getTenLogoToi()));
        }

        if (request.getAnh1() != null)
            images.add(new ImageData(request.getAnh1(), request.getTenAnh1()));
        if (request.getAnh2() != null)
            images.add(new ImageData(request.getAnh2(), request.getTenAnh2()));
        if (request.getAnh3() != null)
            images.add(new ImageData(request.getAnh3(), request.getTenAnh3()));

        for (ImageData img : images) {
            if (img.getBase64() != null && img.getFilename() != null) {
                File file = convertBase64ToFile(img.getBase64(), img.getFilename());
                if (file != null) {
                    ObjectNode response = znsService.sendUploadImage(request.getZoaId(), file.getAbsolutePath());
                    if (response != null && response.has("data") && response.get("data").has("media_id")) {
                        mediaIds.add(response.get("data").get("media_id").asText());
                    }
                    file.delete();
                }
            }
        }

        ObjectNode result = objectMapper.createObjectNode();
        result.set("media_ids", mediaIds);
        return result;
    }

    private File convertBase64ToFile(String base64, String filename) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64);
        File tempFile = File.createTempFile("upload-", filename);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(imageBytes);
        }
        return tempFile;
    }

    public Map<String, Object> createTemplatePayload(MauZNSRequest request, List<String> imageIds) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("template_name", request.getTenMau());
        payload.put("template_type", request.getLoaiZns());
        payload.put("tag", request.getMaLoaiMau());

        Map<String, Object> layout = new HashMap<>();
        Map<String, Object> header = new HashMap<>();
        List<Map<String, Object>> headerComponents = new ArrayList<>();

        // 🏆 Phân loại ảnh
        List<String> logoIds = new ArrayList<>();
        List<String> imageIdsList = new ArrayList<>();

        for (int i = 0; i < imageIds.size(); i++) {
            String mediaId = imageIds.get(i);
            String loaiHanh = request.getLoaiHanh();

            if ("logo".equalsIgnoreCase(loaiHanh)) {
                logoIds.add(mediaId);
            } else {
                imageIdsList.add(mediaId);
            }
        }

        // 🎨 Thêm Logo (có cả sáng và tối)
        if (logoIds.size() >= 2) {
            Map<String, Object> logoNode = new HashMap<>();

            Map<String, Object> lightLogo = new HashMap<>();
            lightLogo.put("type", "IMAGE");
            lightLogo.put("media_id", logoIds.get(0)); // Logo sáng
            logoNode.put("light", lightLogo);

            Map<String, Object> darkLogo = new HashMap<>();
            darkLogo.put("type", "IMAGE");
            darkLogo.put("media_id", logoIds.get(1)); // Logo tối
            logoNode.put("dark", darkLogo);

            Map<String, Object> logoWrapper = new HashMap<>();
            logoWrapper.put("LOGO", logoNode);
            headerComponents.add(logoWrapper);
        }

        // 🖼️ Thêm hình ảnh (tối đa 3 ảnh)
        if (!imageIdsList.isEmpty()) {
            Map<String, Object> imagesNode = new HashMap<>();
            List<Map<String, Object>> imagesArray = new ArrayList<>();

            int maxImages = Math.min(imageIdsList.size(), 3);
            for (int i = 0; i < maxImages; i++) {
                Map<String, Object> imageItem = new HashMap<>();
                imageItem.put("type", "IMAGE");
                imageItem.put("media_id", imageIdsList.get(i));
                imagesArray.add(imageItem);
            }

            imagesNode.put("items", imagesArray);
            Map<String, Object> imagesWrapper = new HashMap<>();
            imagesWrapper.put("IMAGES", imagesNode);
            headerComponents.add(imagesWrapper);
        }

        header.put("components", headerComponents);
        layout.put("header", header);

        // 📄 BODY
        Map<String, Object> body = new HashMap<>();
        List<Map<String, Object>> bodyComponents = new ArrayList<>();

        Map<String, Object> titleNode = new HashMap<>();
        titleNode.put("TITLE", Map.of("value", request.getTdeMau()));
        bodyComponents.add(titleNode);

        Map<String, Object> paragraphNode = new HashMap<>();
        paragraphNode.put("PARAGRAPH", Map.of("value", request.getNdungTde()));
        bodyComponents.add(paragraphNode);

        // 📊 TABLE
        if (request.getMauBangs() != null && !request.getMauBangs().isEmpty()) {
            Map<String, Object> tableNode = new HashMap<>();
            List<Map<String, Object>> tableRows = new ArrayList<>();

            for (MauBangRequest row : request.getMauBangs()) {
                tableRows.add(createTableRow(row.getNdungBang(), row.getTdeBang(), 1));
            }

            tableNode.put("rows", tableRows);
            Map<String, Object> tableWrapper = new HashMap<>();
            tableWrapper.put("TABLE", tableNode);
            bodyComponents.add(tableWrapper);
        }

        body.put("components", bodyComponents);
        layout.put("body", body);

        // 📎 FOOTER
        Map<String, Object> footer = new HashMap<>();
        List<Map<String, Object>> footerComponents = new ArrayList<>();

        if (request.getMauButtons() != null && !request.getMauButtons().isEmpty()) {
            Map<String, Object> buttonWrapper = new HashMap<>();
            Map<String, Object> buttonItems = new HashMap<>();
            List<Map<String, Object>> buttonsArray = new ArrayList<>();

            for (MauButtonRequest btn : request.getMauButtons()) {
                Map<String, Object> button = new HashMap<>();
                button.put("content", btn.getLket());
                button.put("type", btn.getLoaiButton());
                button.put("title", btn.getNdung());
                buttonsArray.add(button);
            }

            buttonItems.put("items", buttonsArray);
            buttonWrapper.put("BUTTONS", buttonItems);
            footerComponents.add(buttonWrapper);
        }

        footer.put("components", footerComponents);
        layout.put("footer", footer);

        payload.put("layout", layout);

        // 🎯 PARAMS
        if (request.getCaiDatTSo() != null && !request.getCaiDatTSo().isEmpty()) {
            List<Map<String, Object>> paramsArray = new ArrayList<>();
            for (CaiDatTSoRequest param : request.getCaiDatTSo()) {
                paramsArray.add(createParam(param.getParamType(), param.getTenTso(), param.getNdungTso()));
            }
            payload.put("params", paramsArray);
        }

        payload.put("note", request.getGhiChu());
        payload.put("tracking_id", "abc123");

        return payload;
    }

    // ✅ Tạo bảng từ request
    private Map<String, Object> createTableRow(String value, String title, int rowType) {
        Map<String, Object> row = new HashMap<>();
        row.put("value", value);
        row.put("title", title);
        row.put("row_type", rowType);
        return row;
    }

    // ✅ Tạo params từ request
    private Map<String, Object> createParam(String type, String name, String sampleValue) {
        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
        param.put("name", name);
        param.put("sample_value", sampleValue);
        return param;
    }

    @Transactional
    @Override
    public void updateMaCtaoGtriTso(ZssMauZnsTsoRequest request) {
        String maMau = request.getMaMau();
        String username = getCurrentUserName();
        String dateTimeNow = dateTimeConverter.dateTimeNow();

        List<ZSSMauZNSTso> existingRecords = zssMauZNSTsoRepository.findByMaMau(maMau);

        if (existingRecords.isEmpty()) {
            return;
        }

        Map<String, String> requestMap = request.getCaiDatTSo().stream()
                .collect(Collectors.toMap(CaiDatTSoClientRequest::getTenTso, CaiDatTSoClientRequest::getMaCtaoGtriTso));

        for (ZSSMauZNSTso record : existingRecords) {
            String newValue = requestMap.get(record.getTenTso());
            if (newValue != null) {
                record.setMaCtaoGtriTso(newValue);
                record.setNguoiSua(username);
                record.setNgaySua(dateTimeNow);
            }
        }

        zssMauZNSTsoRepository.saveAll(existingRecords);
    }

    @Override
    public String previewMau(String maMau) throws ValidationException {
        ZSSMauZNZ mau = zssMauZNZRepository.findByMaMau(maMau)
                .orElseThrow(
                        () -> new ValidationException("Không tìm thấy mẫu với mã: " + maMau, ErrorMessages.NOT_FOUND));
        if (mau.getLket() == null || mau.getLket().isBlank()) {
            throw new ValidationException("Mẫu không có đường dẫn liên kết preview (lket)", ErrorMessages.NOT_FOUND);
        }
        return mau.getLket();
    }
}
