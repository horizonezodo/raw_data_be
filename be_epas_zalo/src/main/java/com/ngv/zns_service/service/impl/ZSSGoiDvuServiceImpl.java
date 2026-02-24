package com.ngv.zns_service.service.impl;


import com.ngv.zns_service.constant.ErrorMessages;
import com.ngv.zns_service.dto.response.dvu.ZSSDvuDto;
import com.ngv.zns_service.dto.response.goiDv.*;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.model.entity.*;
import com.ngv.zns_service.repository.*;
import com.ngv.zns_service.service.ZSSGoiDvuService;

import com.ngv.zns_service.util.date.DateTimeConverter;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZSSGoiDvuServiceImpl extends BaseStoredProcedureService implements ZSSGoiDvuService {
    private static final int MAX_COLUMN_WIDTH = 10000;

    private final ZSSGoiDvuRepository zssGoiDvuRepository;
    private final ZSSDvuRepository zssDvuRepository;
    private final ZSSDviThueBaoRepository zssDviThueBaoRepository;
    private final ZSSGoiDvuCTietRepository zssGoiDvuCTietRepository;
    private final ZSSGoiDvuLsuRepository zssGoiDvuLsuRepository;
    private final ZSSGoiDvuCTietLsuRepository zssGoiDvuCTietLsuRepository;
    
    private final DateTimeConverter dateTimeConverter;
    private final ZSSDvuGDichRepository zssDvuGDichRepository;

    @Override
    public ZSSGoiDvuDtos syncData(String maDvi) {
        ZSSGoiDvuDtos response = new ZSSGoiDvuDtos();
        response.setMaDvi(maDvi);

        List<ZSSGoiDvuDto> goiDvuDtoList = new ArrayList<>();
        List<ZSSDviThueBao> zssDviThueBaoList = zssDviThueBaoRepository.findAllByMaDvi(maDvi);

        for (ZSSDviThueBao dviThueBao : zssDviThueBaoList) {
            List<ZSSGoiDvuDto> goiDvuDtos = zssGoiDvuRepository.getAll(dviThueBao.getMaGoiDvu()).stream()
                    .map(zssGoiDvu -> {
                        ZSSGoiDvuDto dto = new ZSSGoiDvuDto();
                        BeanUtils.copyProperties(zssGoiDvu, dto);
                        dto.setOaId(dviThueBao.getZoaId());
                        return dto;
                    }).toList();

            List<String> maGoiDvuList = goiDvuDtos.stream()
                    .map(ZSSGoiDvuDto::getMaGoiDvu)
                    .toList();

            List<ZSSDvuDto> dvuDtoList = zssDvuRepository.getAll(maGoiDvuList).stream()
                    .map(zssDvu -> {
                        ZSSDvuDto dto = new ZSSDvuDto();
                        BeanUtils.copyProperties(zssDvu, dto);
                        return dto;
                    }).toList();

            Map<String, List<ZSSDvuDto>> dichVuMap = dvuDtoList.stream()
                    .collect(Collectors.groupingBy(ZSSDvuDto::getMaGoiDvu));

            goiDvuDtos.forEach(g -> g.setZssDvuDtoList(dichVuMap.getOrDefault(g.getMaGoiDvu(), new ArrayList<>())));

            // Lấy danh sách mã loại dịch vụ cho từng dịch vụ
            for (ZSSDvuDto dvuDto : dvuDtoList) {
                // Giả sử phương thức này trả về List<String> chứa các mã loại dịch vụ
                List<String> maLoaiGDichList = zssDvuGDichRepository.findByMaDvu(dvuDto.getMaDvu());
                dvuDto.setListMaLoaiGdich(maLoaiGDichList); // Gán danh sách vào thuộc tính mới
            }

            goiDvuDtoList.addAll(goiDvuDtos);
        }

        response.setGoiDvuDtoList(goiDvuDtoList);
        return response;
    }

    @Override
    public Page<ZSSGoiDvuDto> listGoiDvu(Pageable pageable) {
        Page<ZSSGoiDvu> page = zssGoiDvuRepository.findAll(pageable);
        return page.map(zssGoiDvu -> {
            ZSSGoiDvuDto dto = new ZSSGoiDvuDto();
            BeanUtils.copyProperties(zssGoiDvu, dto);
            return dto;
        });
    }

    @Override
    @Transactional
    public void deleteIds(List<String> maGoiDvu) throws ValidationException {
        List<String> existGoiDvu = zssDviThueBaoRepository.existsByMaGoiDvu(maGoiDvu);
        Map<String, String> error = new HashMap<>();

        if (!existGoiDvu.isEmpty()) {
            error.put(
                    ErrorMessages.GOI_DVU,
                    MessageFormat.format(ErrorMessages.GOI_DVU_EXIST, existGoiDvu.get(0)));
        }
        throwError(error);
        zssGoiDvuCTietRepository.deleteByMaGoiDvuList(maGoiDvu);
        zssGoiDvuRepository.deleteByMaGoiDvuList(maGoiDvu);
    }

    private void throwError(Map<String, String> error) throws ValidationException {
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }

    @Override
    public ZSSGoiDvuDetailDto create(ZSSGoiDvuDetailDto dto) throws ValidationException {
        String username = getCurrentUserName();
        String now = dateTimeConverter.dateNow();
        Map<String, String> error = new HashMap<>();
        ZSSGoiDvuDetailDto response = new ZSSGoiDvuDetailDto();
        boolean checkEntity = zssGoiDvuRepository.existsAllByMaGoiDvu(dto.gettTinChungDto().getMaGoiDvu());

        if (checkEntity) {
            error.put(ErrorMessages.MA_GOI_DVU, ErrorMessages.MA_GOI_DVU_EXIST);
        }

        ZSSGoiDvu goiDvu = new ZSSGoiDvu();
        goiDvu.setMaGoiDvu(dto.gettTinChungDto().getMaGoiDvu());
        goiDvu.setNguoiNhap(username);
        goiDvu.setNgayNhap(now);
        goiDvu.setNguoiSua(username);
        goiDvu.setNgaySua(now);
        goiDvu.setTenGoiDvu(dto.gettTinChungDto().getTenGoiDvu());
        goiDvu.setMota(dto.gettTinChungDto().getMoTa());
        goiDvu.setTthaiNvu(dto.gettTinChungDto().gettThaiNvu());

        this.zssGoiDvuRepository.save(goiDvu);
        ZSSGoiDvuLsu goiDvuLs = new ZSSGoiDvuLsu();
        BeanUtils.copyProperties(goiDvu, goiDvuLs);
        this.zssGoiDvuLsuRepository.save(goiDvuLs);
        GoiDvuDetailTTinChungDto tTinChungDto = new GoiDvuDetailTTinChungDto(dto.gettTinChungDto().getMaGoiDvu(),
                dto.gettTinChungDto().getTenGoiDvu(),
                dto.gettTinChungDto().gettThaiNvu(),
                dto.gettTinChungDto().getMoTa());

        List<ZSSGoiDvuCTiet> list = new ArrayList<>();

        if (dto.getMappingDvuDtos() != null) {
            for (GoiDvuDetailMappingDvuDto i : dto.getMappingDvuDtos()) {
                ZSSGoiDvuCTiet zssGoiDvuCTiet = new ZSSGoiDvuCTiet();
                zssGoiDvuCTiet.setMaDvu(i.getMaDvu());
                zssGoiDvuCTiet.setMaGoiDvu(dto.gettTinChungDto().getMaGoiDvu());
                zssGoiDvuCTiet.setNguoiSua(username);
                zssGoiDvuCTiet.setNguoiNhap(username);
                zssGoiDvuCTiet.setNgaySua(now);
                zssGoiDvuCTiet.setNgayNhap(now);

                list.add(zssGoiDvuCTiet);
            }
        }
        zssGoiDvuCTietRepository.saveAll(list);
        List<ZSSGoiDvuCTietLsu> zssGoiDvuCTietLsus = list.stream().map(zssGoiDvuCTiet -> {
            ZSSGoiDvuCTietLsu lsu = new ZSSGoiDvuCTietLsu();
            BeanUtils.copyProperties(zssGoiDvuCTiet, lsu);
            return lsu;
        }).toList();

        this.zssGoiDvuCTietLsuRepository.saveAll(zssGoiDvuCTietLsus);
        response.settTinChungDto(tTinChungDto);
        response.setMappingDvuDtos(dto.getMappingDvuDtos());
        throwError(error);
        return response;
    }

    @Override
    @Transactional
    public ZSSGoiDvuDetailDto update(ZSSGoiDvuDetailDto dto) throws ValidationException {
        String username = getCurrentUserName();
        String now = dateTimeConverter.dateNow();
        Map<String, String> error = new HashMap<>();
        ZSSGoiDvuDetailDto response = new ZSSGoiDvuDetailDto();

        Optional<ZSSGoiDvu> entity = zssGoiDvuRepository.findById(dto.gettTinChungDto().getMaGoiDvu());
        if (entity.isPresent()) {

            if (dto.gettTinChungDto().getTenGoiDvu().isEmpty()) {
                error.put(ErrorMessages.MA_GOI_DVU, ErrorMessages.NOT_EMPTY_TEN_GOI_DVU);
            }

            ZSSGoiDvu goiDvu = new ZSSGoiDvu();
            goiDvu.setMaGoiDvu(dto.gettTinChungDto().getMaGoiDvu());
            goiDvu.setNguoiSua(username);
            goiDvu.setNguoiNhap(entity.get().getNguoiNhap());
            goiDvu.setNgaySua(now);
            goiDvu.setNgayNhap(entity.get().getNgayNhap());
            goiDvu.setTenGoiDvu(dto.gettTinChungDto().getTenGoiDvu());
            goiDvu.setMota(dto.gettTinChungDto().getMoTa());
            goiDvu.setTthaiNvu(dto.gettTinChungDto().gettThaiNvu());

            this.zssGoiDvuRepository.save(goiDvu);
            ZSSGoiDvuLsu goiDvuLs = new ZSSGoiDvuLsu();
            BeanUtils.copyProperties(goiDvu, goiDvuLs);
            this.zssGoiDvuLsuRepository.save(goiDvuLs);
        } else {
            error.put(
                    ErrorMessages.MA_GOI_DVU,
                    MessageFormat.format(ErrorMessages.NOT_FOUND_GOI_DVU, dto.gettTinChungDto().getMaGoiDvu()));
        }
        GoiDvuDetailTTinChungDto tTinChungDto = new GoiDvuDetailTTinChungDto(dto.gettTinChungDto().getMaGoiDvu(),
                dto.gettTinChungDto().getTenGoiDvu(),
                dto.gettTinChungDto().getMoTa(),
                dto.gettTinChungDto().gettThaiNvu());

        List<ZSSGoiDvuCTiet> list = new ArrayList<>();

        if (dto.getMappingDvuDtos() != null) {
            for (GoiDvuDetailMappingDvuDto i : dto.getMappingDvuDtos()) {
                ZSSGoiDvuCTiet zssGoiDvuCTiet = new ZSSGoiDvuCTiet();
                zssGoiDvuCTiet.setMaDvu(i.getMaDvu());
                zssGoiDvuCTiet.setMaGoiDvu(dto.gettTinChungDto().getMaGoiDvu());
                zssGoiDvuCTiet.setNguoiSua(username);
                zssGoiDvuCTiet.setNguoiNhap(username);
                zssGoiDvuCTiet.setNgaySua(now);
                zssGoiDvuCTiet.setNgayNhap(now);

                list.add(zssGoiDvuCTiet);
            }
        }
        zssGoiDvuCTietRepository.saveAll(list);
        List<ZSSGoiDvuCTietLsu> zssGoiDvuCTietLsus = list.stream().map(zssGoiDvuCTiet -> {
            ZSSGoiDvuCTietLsu lsu = new ZSSGoiDvuCTietLsu();
            BeanUtils.copyProperties(zssGoiDvuCTiet, lsu);
            return lsu;
        }).toList();

        List<String> maDvuList = Optional.ofNullable(dto.getMappingDvuDtos())
                .orElse(Collections.emptyList())
                .stream()
                .map(GoiDvuDetailMappingDvuDto::getMaDvu)
                .toList();

        List<String> maDvus;

        if (maDvuList.isEmpty()) {
            maDvus = this.zssGoiDvuCTietRepository.findAllMaDvuByMaGoiDvu(dto.gettTinChungDto().getMaGoiDvu());
        } else {
            maDvus = this.zssGoiDvuCTietRepository.findMaDvuToDelete(dto.gettTinChungDto().getMaGoiDvu(), maDvuList);
        }

        if (!maDvus.isEmpty()) {
            this.zssGoiDvuCTietRepository.deleteByInMaGoiDvuAndInMaDvu(dto.gettTinChungDto().getMaGoiDvu(), maDvus);
        }

        this.zssGoiDvuCTietLsuRepository.saveAll(zssGoiDvuCTietLsus);

        response.settTinChungDto(tTinChungDto);
        response.setMappingDvuDtos(dto.getMappingDvuDtos());
        throwError(error);
        return response;
    }

    @Override
    public ZSSGoiDvuDetailDto getDetail(String mgdv) {
        ZSSGoiDvuDetailDto dto = new ZSSGoiDvuDetailDto();
        dto.settTinChungDto(zssGoiDvuRepository.findGoiDvu(mgdv));
        dto.setMappingDvuDtos(zssGoiDvuRepository.findMappingGoiDvu(mgdv));
        return dto;
    }

    @Override
    public List<ZSSTthaiHdongDto> listTthaiHd(String mdm) {
        return zssGoiDvuRepository.listTthaiHd(mdm);
    }

    @Override
    public byte[] exportExcel() throws IOException {
        List<ZSSGoiDvuExcelDto> dataExcel = zssGoiDvuRepository.exportExcel();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Gói Dịch Vụ");

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
        String[] columns = { "STT", "Mã gói dịch vụ", "Tên gói dịch vụ", "Mô tả", "Trạng thái hoạt động", "Ngày tạo" };
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Ghi dữ liệu từ database vào Excel
        int rowIdx = 1;
        int stt = 1;
        for (ZSSGoiDvuExcelDto dto : dataExcel) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(stt++);
            row.createCell(1).setCellValue(dto.getMaGoiDvu());
            row.createCell(2).setCellValue(dto.getTenGoiDvu());
            row.createCell(3).setCellValue(dto.getMota());
            row.createCell(4).setCellValue(dto.getTthaiNvu());
            row.createCell(5).setCellValue(dto.getNgayNhap());

            // Áp dụng style cho từng ô
            for (int i = 0; i < columns.length; i++) {
                if (i == 3) { // Cột "Mô tả"
                    row.getCell(i).setCellStyle(leftAlignStyle);
                } else {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }
        }

        // Tự động điều chỉnh độ rộng của cột để không bị cắt chữ
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
            // sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000); // Tăng thêm khoảng
            // trống để chữ không bị cắt
            int newWidth = sheet.getColumnWidth(i) + 1000; // Thêm khoảng trống để tránh bị cắt chữ
            sheet.setColumnWidth(i, Math.min(newWidth, MAX_COLUMN_WIDTH));
        }

        // Xuất dữ liệu ra mảng byte
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}
