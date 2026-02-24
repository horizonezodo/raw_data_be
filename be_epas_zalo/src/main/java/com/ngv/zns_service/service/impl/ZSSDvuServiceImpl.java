package com.ngv.zns_service.service.impl;


import com.ngv.zns_service.constant.ErrorMessages;
import com.ngv.zns_service.dto.request.ZSSDvuRequest;
import com.ngv.zns_service.dto.response.DvuGroupedDTO;
import com.ngv.zns_service.dto.response.ZSSDvuResponse;
import com.ngv.zns_service.dto.response.dvu.*;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.model.entity.*;
import com.ngv.zns_service.repository.*;
import com.ngv.zns_service.service.ZSSDvuService;

import com.ngv.zns_service.util.date.DateTimeConverter;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZSSDvuServiceImpl extends BaseStoredProcedureService implements ZSSDvuService {
    private static final int MAX_COLUMN_WIDTH = 10000;
    public final ZSSDvuGDichRepository zssDvuGDichRepository;
    public final ZSSGoiDvuCTietRepository zssGoiDvuCTietRepository;
    private final ZSSDvuRepository zssDvuRepository;
    private final ZSSDmucCtietRepository zssDmucCtietRepository;
    private final ZSSGoiDvuRepository zssGoiDvuRepository;
    
    private final DateTimeConverter dateTimeConverter;
    private final DCLoaiGdRepository dcLoaiGdRepository;
    private final ZSSDvuLsuRepository zssDvuLsuRepository;
    private final ZSSDvuMauZNSRepository zssDvuMauZNSRepository;

    @Override
    public Page<ZSSDvuResponse> getList(ZSSDvuRequest request) {
        return zssDvuRepository.getList(
                        request.getLoaiDvu(), request.getTrangThaiDvu(), request.getPageable())
                .map(zssDvuProjection -> new ZSSDvuResponse(
                        zssDvuProjection.getMaDvu(),
                        zssDvuProjection.getTenDvu(),
                        zssDvuProjection.getMaLoaiDv(),
                        zssDvuProjection.getTenLoaiDv(),
                        zssDvuProjection.getTrangThai(),
                        zssDvuProjection.getNgayNhap()));
    }

    @Override
    public ChiTietDvuDto getDetail(String mdv) {
        ChiTietDvuDto dto = new ChiTietDvuDto();
        dto.setTtchungDto(zssDvuRepository.getDetail(mdv));
        dto.setGdvDtos(zssGoiDvuRepository.listMappingGdv(mdv));
        dto.setGdDtos(zssDvuGDichRepository.listMappingGd(mdv));
        dto.setTempDtos(zssDvuRepository.listMappingTemp(mdv));
        return dto;
    }

    @Override
    public ChiTietDvuDto create(ChiTietDvuDto dto) throws ValidationException {
        Map<String, String> error = new HashMap<>();
        Boolean isExitsMaDvu = zssDvuRepository.existsById(dto.getTtchungDto().getMaDvu());
        if (isExitsMaDvu) {
            error.put(ErrorMessages.DVU, ErrorMessages.MA_DVU_EXIST);
            throwError(error);
            return null;
        }
        String username = getCurrentUserName();
        String now = dateTimeConverter.dateTimeNow();
        ZSSDvu zssDvu = new ZSSDvu();
        zssDvu.setMaDvu(dto.getTtchungDto().getMaDvu());
        zssDvu.setMaLoaiDvu(dto.getTtchungDto().getMaLoaiDvu());
        zssDvu.setTenDvu(dto.getTtchungDto().getTenDvu());
        zssDvu.setTthaiNvu(dto.getTtchungDto().getTthaiNvu());
        zssDvu.setDvuLquan(dto.getTtchungDto().getDvuLQuan());
        zssDvu.setMota(dto.getTtchungDto().getMota());
        zssDvu.setHthucGui(dto.getTtchungDto().getHthucGui());
        zssDvu.setTgianGui(dto.getTtchungDto().getTgianGui());
        zssDvu.setGioBdauGui(dto.getTtchungDto().getGioBdauGui());
        zssDvu.setGioKthucGui(dto.getTtchungDto().getGioKthucGui());
        zssDvu.setSoLguiTbai(dto.getTtchungDto().getSoLguiTbai());

        zssDvu.setNguoiNhap(username);
        zssDvu.setNgayNhap(now);
        zssDvu.setNguoiSua(username);
        zssDvu.setNgaySua(now);

        if ("1".equals(dto.getTtchungDto().getDvuLQuan())) {
            zssDvu.setSuKien("Dịch vụ gắn với giao dịch");
        } else if ("2".equals(dto.getTtchungDto().getDvuLQuan())) {
            zssDvu.setSuKien("Dịch vụ gắn với chiến dịch");
        } else {
            zssDvu.setSuKien("Cả 2");
        }
        ZSSDvuLsu zssDvuLsu = new ZSSDvuLsu();
        BeanUtils.copyProperties(zssDvu, zssDvuLsu);

        this.zssDvuRepository.save(zssDvu);
        this.zssDvuLsuRepository.save(zssDvuLsu);
        List<ZSSGoiDvuCTiet> zssGoiDvuCTiets = dto.getGdvDtos().stream().map(
                ctDvMapGdvDto -> {
                    ZSSGoiDvuCTiet entity = new ZSSGoiDvuCTiet();
                    BeanUtils.copyProperties(ctDvMapGdvDto, entity);
                    entity.setMaDvu(zssDvu.getMaDvu());
                    entity.setNgayNhap(now);
                    entity.setNguoiNhap(username);
                    entity.setNgaySua(now);
                    entity.setNguoiSua(username);
                    return entity;
                }).toList();
        this.zssGoiDvuCTietRepository.saveAll(zssGoiDvuCTiets);

        List<ZSSDvuGDich> zssDvuGDiches = dto.getGdDtos().stream().map(
                ctDvMapGdDto -> {
                    ZSSDvuGDich entity = new ZSSDvuGDich();
                    BeanUtils.copyProperties(ctDvMapGdDto, entity);
                    entity.setMaDvu(zssDvu.getMaDvu());
                    entity.setNgayNhap(now);
                    entity.setNguoiNhap(username);
                    entity.setNgaySua(now);
                    entity.setNguoiSua(username);
                    return entity;
                }).toList();
        this.zssDvuGDichRepository.saveAll(zssDvuGDiches);

        List<ZSSDvuMauZNS> zssDvuMauZNS = dto.getTempDtos().stream().map(
                ctDvMapTempDto -> {
                    ZSSDvuMauZNS entity = new ZSSDvuMauZNS();
                    BeanUtils.copyProperties(ctDvMapTempDto, entity);
                    entity.setMaDvu(zssDvu.getMaDvu());
                    entity.setNgayNhap(now);
                    entity.setNguoiNhap(username);
                    entity.setNgaySua(now);
                    entity.setNguoiSua(username);
                    return entity;
                }).toList();
        this.zssDvuMauZNSRepository.saveAll(zssDvuMauZNS);
        throwError(error);
        return dto;
    }

    @Override
    @Transactional
    public ChiTietDvuDto update(ChiTietDvuDto dto) throws ValidationException {
        String username = getCurrentUserName();
        String now = dateTimeConverter.dateTimeNow();
        Map<String, String> error = new HashMap<>();
        Optional<ZSSDvu> zssDvu = zssDvuRepository.findById(dto.getTtchungDto().getMaDvu());
        if (zssDvu.isPresent()) {
            zssDvu.get().setMaDvu(dto.getTtchungDto().getMaDvu());
            zssDvu.get().setMaLoaiDvu(dto.getTtchungDto().getMaLoaiDvu());
            zssDvu.get().setTenDvu(dto.getTtchungDto().getTenDvu());
            zssDvu.get().setTthaiNvu(dto.getTtchungDto().getTthaiNvu());
            zssDvu.get().setDvuLquan(dto.getTtchungDto().getDvuLQuan());
            zssDvu.get().setMota(dto.getTtchungDto().getMota());
            zssDvu.get().setHthucGui(dto.getTtchungDto().getHthucGui());
            zssDvu.get().setTgianGui(dto.getTtchungDto().getTgianGui());
            zssDvu.get().setGioBdauGui(dto.getTtchungDto().getGioBdauGui());
            zssDvu.get().setGioKthucGui(dto.getTtchungDto().getGioKthucGui());
            zssDvu.get().setSoLguiTbai(dto.getTtchungDto().getSoLguiTbai());

            zssDvu.get().setNguoiSua(username);
            zssDvu.get().setNgaySua(now);

            if ("1".equals(dto.getTtchungDto().getDvuLQuan())) {
                zssDvu.get().setSuKien("Dịch vụ gắn với giao dịch");
            } else if ("2".equals(dto.getTtchungDto().getDvuLQuan())) {
                zssDvu.get().setSuKien("Dịch vụ gắn với chiến dịch");
            } else {
                zssDvu.get().setSuKien("Cả 2");
            }
            ZSSDvuLsu zssDvuLsu = new ZSSDvuLsu();
            BeanUtils.copyProperties(zssDvu.get(), zssDvuLsu);

            this.zssDvuRepository.save(zssDvu.get());
            this.zssDvuLsuRepository.save(zssDvuLsu);
            List<ZSSGoiDvuCTiet> zssGoiDvuCTiets = dto.getGdvDtos().stream().map(
                    ctDvMapGdvDto -> {
                        ZSSGoiDvuCTiet entity = new ZSSGoiDvuCTiet();
                        BeanUtils.copyProperties(ctDvMapGdvDto, entity);
                        entity.setMaDvu(zssDvu.get().getMaDvu());
                        entity.setNgayNhap(now);
                        entity.setNguoiNhap(username);
                        entity.setNgaySua(now);
                        entity.setNguoiSua(username);
                        return entity;
                    }).toList();

            List<String> maGoiDvuList = dto.getGdvDtos()
                    .stream()
                    .map(CtDvMapGdvDto::getMaGoiDvu)
                    .toList();
            if (maGoiDvuList.isEmpty()) {
                // Xóa tất cả các gói dịch vụ nếu mảng gửi lên là trống
                this.zssGoiDvuCTietRepository.deleteByMaDvu(zssDvu.get().getMaDvu());
            } else {
                // Xóa các gói dịch vụ không còn trong danh sách
                List<String> maGoiDvus = this.zssGoiDvuCTietRepository.findMaGoiDvuToDelete(maGoiDvuList,
                        zssDvu.get().getMaDvu());
                if (!maGoiDvus.isEmpty()) {
                    this.zssGoiDvuCTietRepository.deleteByNotInMaGoiDvuAndInMaDvu(maGoiDvus, zssDvu.get().getMaDvu());
                }
            }
            this.zssGoiDvuCTietRepository.saveAll(zssGoiDvuCTiets);

            List<ZSSDvuGDich> zssDvuGDiches = dto.getGdDtos().stream().map(
                    ctDvMapGdDto -> {
                        ZSSDvuGDich entity = new ZSSDvuGDich();
                        BeanUtils.copyProperties(ctDvMapGdDto, entity);
                        entity.setMaDvu(zssDvu.get().getMaDvu());
                        entity.setNgayNhap(now);
                        entity.setNguoiNhap(username);
                        entity.setNgaySua(now);
                        entity.setNguoiSua(username);
                        return entity;
                    }).toList();

            List<String> maLoaiGdList = dto.getGdDtos()
                    .stream()
                    .map(CtDvMapGdDto::getMaLoaiGdich)
                    .toList();
            if (maLoaiGdList.isEmpty()) {
                // Xóa tất cả các gói dịch vụ nếu mảng gửi lên là trống
                this.zssDvuGDichRepository.deleteByMaDvu(zssDvu.get().getMaDvu());
            } else {
                // Xóa các gói dịch vụ không còn trong danh sách
                List<String> maLoaiGds = this.zssDvuGDichRepository.findMaGoiDvuToDelete(maLoaiGdList,
                        zssDvu.get().getMaDvu());
                if (!maLoaiGds.isEmpty()) {
                    this.zssDvuGDichRepository.deleteByMaDvuAndMaLoaiGdich(zssDvu.get().getMaDvu(), maLoaiGds);
                }
            }
            this.zssDvuGDichRepository.saveAll(zssDvuGDiches);

            List<ZSSDvuMauZNS> zssDvuMauZNS = dto.getTempDtos().stream().map(
                    ctDvMapTempDto -> {
                        ZSSDvuMauZNS entity = new ZSSDvuMauZNS();
                        BeanUtils.copyProperties(ctDvMapTempDto, entity);
                        entity.setMaDvu(zssDvu.get().getMaDvu());
                        entity.setNgayNhap(now);
                        entity.setNguoiNhap(username);
                        entity.setNgaySua(now);
                        entity.setNguoiSua(username);
                        return entity;
                    }).toList();
            List<KeyTempDto> tempList = dto.getTempDtos()
                    .stream()
                    .map(ctDvMapTempDto -> new KeyTempDto(ctDvMapTempDto.getZoaId(), ctDvMapTempDto.getMaMau()))
                    .toList();

            List<KeyTempDto> listAll = this.zssDvuMauZNSRepository.findByMaDvu(zssDvu.get().getMaDvu())
                    .stream()
                    .map(ctDvMapTempDto -> new KeyTempDto(ctDvMapTempDto.getZoaId(), ctDvMapTempDto.getMaMau()))
                    .toList();

            List<KeyTempDto> listToDelete = listAll.stream().filter(
                            keyTempDto -> tempList.stream().noneMatch(
                                    keyTempDto1 -> keyTempDto1.getZoaId().equals(keyTempDto.getZoaId()) &&
                                            keyTempDto1.getMaMau().equals(keyTempDto.getMaMau())))
                    .toList();

            for (KeyTempDto keyTempDto : listToDelete) {
                this.zssDvuMauZNSRepository.deleteByMaDvuAndZoaIdAndMaMau(zssDvu.get().getMaDvu(),
                        keyTempDto.getZoaId(), keyTempDto.getMaMau());
            }
            this.zssDvuMauZNSRepository.saveAll(zssDvuMauZNS);
        }
        throwError(error);
        return dto;
    }

    @Transactional
    @Override
    public void deleteIds(List<String> maDvu) throws ValidationException {
        List<Object[]> existGDich = zssDvuGDichRepository.checkExistMaDvu(maDvu);
        List<Object[]> existGoiDvu = zssGoiDvuCTietRepository.checkExistMaDvuInGoi(maDvu);
        List<Object[]> existMauBieu = zssDvuMauZNSRepository.checkExistMaDvuInMauBieu(maDvu);

        Map<String, String> serviceNames = new HashMap<>();
        Map<String, List<String>> serviceUsageMap = new HashMap<>();

        // Mapping các loại liên kết
        for (Object[] row : existGDich) {
            String ma = (String) row[0];
            String ten = (String) row[1];
            serviceNames.put(ma, ten);
            serviceUsageMap.computeIfAbsent(ma, k -> new ArrayList<>()).add("giao dịch");
        }

        for (Object[] row : existGoiDvu) {
            String ma = (String) row[0];
            String ten = (String) row[1];
            serviceNames.put(ma, ten);
            serviceUsageMap.computeIfAbsent(ma, k -> new ArrayList<>()).add("gói dịch vụ");
        }

        for (Object[] row : existMauBieu) {
            String ma = (String) row[0];
            String ten = (String) row[1];
            serviceNames.put(ma, ten);
            serviceUsageMap.computeIfAbsent(ma, k -> new ArrayList<>()).add("mẫu biểu");
        }

        // Xử lý lỗi và danh sách cần xóa
        List<String> errorMessages = new ArrayList<>();
        Set<String> maDvuToDelete = new HashSet<>(maDvu);

        for (Map.Entry<String, List<String>> entry : serviceUsageMap.entrySet()) {
            String ma = entry.getKey();
            String tenDvu = serviceNames.get(ma);
            List<String> usages = entry.getValue();

            errorMessages.add("Dịch vụ '" + tenDvu + "' đang được mapping với " + String.join(", ", usages));
            maDvuToDelete.remove(ma); // Không xóa những dịch vụ đang được sử dụng
        }

        if (!maDvuToDelete.isEmpty()) {
            zssDvuRepository.deleteByMaDvuList(new ArrayList<>(maDvuToDelete));
        }

        if (!errorMessages.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put(ErrorMessages.DVU, String.join("\n", errorMessages));
            throwError(error);
        }
    }

    private void throwError(Map<String, String> error) throws ValidationException {
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }

    @Override
    public List<ZSSLoaiDvuResponse> listLoaiDvu(String maDmCtiet) {
        return zssDmucCtietRepository.findLoaiDvu(maDmCtiet).stream()
                .map(zssLoaiDvuDto -> new ZSSLoaiDvuResponse(zssLoaiDvuDto.getMaDmucCtiet(),
                        zssLoaiDvuDto.getTenDmucCtiet()))
                .toList();
    }

    @Override
    public byte[] exportExcel(ZSSDvuRequest request) throws IOException {
        List<ZSSDvuExcelDto> dataExcel = zssDvuRepository
                .getList(request.getLoaiDvu(), request.getTrangThaiDvu(), request.getPageable()).getContent().stream()
                .map(projection -> new ZSSDvuExcelDto(
                        projection.getMaDvu(),
                        projection.getTenDvu(),
                        projection.getTenLoaiDv(),
                        projection.getMaLoaiDv(),
                        projection.getTrangThai(),
                        projection.getNgayNhap()))
                .toList();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Dịch Vụ");

        // Tạo font Times New Roman cỡ 12
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setFontName("Times New Roman");
        headerFont.setBold(true);

        Font dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 12);
        dataFont.setFontName("Times New Roman");

        // Tạo style cho tiêu đề (căn giữa)
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Tạo style cho dữ liệu (căn giữa)
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setFont(dataFont);
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        // Style cho dữ liệu căn trái (chỉ dùng cho cột "Mô tả")
        CellStyle leftAlignStyle = workbook.createCellStyle();
        leftAlignStyle.setFont(dataFont);
        leftAlignStyle.setAlignment(HorizontalAlignment.LEFT);
        leftAlignStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        leftAlignStyle.setBorderTop(BorderStyle.THIN);
        leftAlignStyle.setBorderBottom(BorderStyle.THIN);
        leftAlignStyle.setBorderLeft(BorderStyle.THIN);
        leftAlignStyle.setBorderRight(BorderStyle.THIN);

        // Tạo header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"STT", "Mã dịch vụ", "Tên dịch vụ", "Mã loại dịch vụ", "Tên loại dịch vụ",
                "Trạng thái hoạt động", "Ngày tạo"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Ghi dữ liệu từ database vào Excel
        int rowIdx = 1;
        int stt = 1;
        for (ZSSDvuExcelDto dto : dataExcel) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(stt++);
            row.createCell(1).setCellValue(dto.getMaDvu());
            row.createCell(2).setCellValue(dto.getTenDvu());
            row.createCell(3).setCellValue(dto.getMaLoaiDvu());
            row.createCell(4).setCellValue(dto.getTenLoaiDvu());
            row.createCell(5).setCellValue(dto.getTthaiNvu());
            row.createCell(6).setCellValue(dto.getNgayNhap());

            // Áp dụng style cho từng ô
            for (int i = 0; i < columns.length; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        // Tự động điều chỉnh độ rộng của cột để không bị cắt chữ
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
            int newWidth = sheet.getColumnWidth(i) + 1000; // Thêm khoảng trống để tránh bị cắt chữ
            sheet.setColumnWidth(i, Math.min(newWidth, MAX_COLUMN_WIDTH));
        }

        // Xuất dữ liệu ra mảng byte
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();

    }

    @Override
    public List<CtDvMapGdvDto> listMapGdvDtos() {
        return zssGoiDvuRepository.findAll().stream().map(
                zssGoiDvu -> {
                    CtDvMapGdvDto dto = new CtDvMapGdvDto();
                    BeanUtils.copyProperties(zssGoiDvu, dto);
                    dto.setNgayTao(dateTimeConverter.dateNow());
                    return dto;
                }).toList();
    }

    @Override
    public List<CtDvMapGdDto> listMapGdDtos() {
        return dcLoaiGdRepository.findAll().stream().map(
                zssGoiDvu -> new CtDvMapGdDto(zssGoiDvu.getMaLoaiGdich(), zssGoiDvu.getTenLoaiGdich())).toList();
    }

    @Override
    public List<ZSSLoaiDvuDto> listCommon(String mdm, String mdmct) {
        return zssDvuRepository.listCommon(mdm, mdmct);
    }

    @Override
    public CtDvMapTempDto saveTemp(CtDvMapTempDto dto) {
        String username = getCurrentUserName();
        String now = dateTimeConverter.dateTimeNow();
        ZSSDvuMauZNS zns = new ZSSDvuMauZNS();
        zns.setZoaId(dto.getZoaId());
        zns.setMaDvu(dto.getZoaId());
        zns.setMaMau(dto.getMaMau());
        zns.setNguoiNhap(username);
        zns.setNgayNhap(now);
        zns.setNguoiSua(username);
        zns.setNgaySua(now);
        zns.setMaDvi(dto.getMaDvi());
        this.zssDvuMauZNSRepository.save(zns);

        return dto;
    }

    @Override
    public Map<String, List<Map<String, String>>> getGroupedDvu() {
        List<DvuGroupedDTO> list = zssDvuRepository.getGroupedDvu();

        // Sử dụng distinctBy để loại bỏ các bản ghi trùng lặp theo maLoaiDvu và maDvu
        return list.stream()
                .collect(Collectors.groupingBy(
                        DvuGroupedDTO::getMaLoaiDvu,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                groupList -> groupList.stream()
                                        // Loại bỏ các maDvu trùng lặp trong mỗi nhóm
                                        .collect(Collectors.toMap(
                                                DvuGroupedDTO::getMaDvu,
                                                dto -> {
                                                    Map<String, String> map = new HashMap<>();
                                                    map.put("maDvu", dto.getMaDvu());
                                                    map.put("tenDvu", dto.getTenDvu());
                                                    return map;
                                                },
                                                (existing, replacement) -> existing))
                                        .values()
                                        .stream()
                                        .collect(Collectors.toList())
                        )
                ));
    }
}
